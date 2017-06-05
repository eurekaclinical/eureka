/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.net.URI;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.EtlClient;

import org.eurekaclinical.eureka.client.comm.JobSpec;
import org.eurekaclinical.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;

import java.io.PrintWriter;
import javax.inject.Singleton;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.map.ObjectMapper;

@Singleton
public class JobSubmitServlet extends HttpServlet {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final long serialVersionUID = 1L;

	static {
		MAPPER.setDateFormat(new SimpleDateFormat("MM/dd/yyyy"));
	}

	/**
	 * Normal directory to save files to. TODO: set value
	 */
	private File tmpDir;
	private final Injector injector;

	@Inject
	public JobSubmitServlet(Injector inInjector) {
		this.injector = inInjector;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.tmpDir = FileUtils.getTempDirectory();
		if (!this.tmpDir.isDirectory()) {
			throw new ServletException(this.tmpDir.getAbsolutePath() + " is not a directory");
		}
	}

	private static final class SubmitJobResponse {

		private Long jobId;
		private int statusCode;
		private String errorThrown;
		private String message;

		public Long getJobId() {
			return jobId;
		}

		public void setJobId(Long jobId) {
			this.jobId = jobId;
		}

		public String getErrorThrown() {
			return errorThrown;
		}

		public void setErrorThrown(String errorThrown) {
			this.errorThrown = errorThrown;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			throw new ServletException(
					"Spreadsheet upload attempt: no user associated with the request");
		}
		SubmitJobResponse jobResponse = new SubmitJobResponse();
		String value;
		try {
			Long jobId = submitJob(request, principal);
			jobResponse.setJobId(jobId);
		} catch (ParseException ex) {
			jobResponse.setMessage("The date range you specified is invalid.");
			jobResponse.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
			jobResponse.setErrorThrown("Bad request");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (ClientException | FileUploadException | IOException ex) {
			String msg = "Upload failed due to an internal error";
			jobResponse.setMessage(msg);
			jobResponse.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			jobResponse.setErrorThrown("Internal server error");
			log("Upload failed for user " + principal.getName(), ex);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		value = MAPPER.writeValueAsString(jobResponse);
		response.setContentLength(value.length());
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(value);
	}

	private Long submitJob(
			HttpServletRequest request, Principal principal)
			throws FileUploadException, IOException, ClientException, ParseException {

		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();

		/*
		 *Set the size threshold, above which content will be stored on disk.
		 */
		fileItemFactory.setSizeThreshold(5 * 1024 * 1024); //5 MB
		/*
		 * Set the temporary directory to store the uploaded files of size above threshold.
		 */
		fileItemFactory.setRepository(this.tmpDir);

		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		/*
		 * Parse the request
		 */
		List items = uploadHandler.parseRequest(request);
		Properties fields = new Properties();
		for (Iterator itr = items.iterator(); itr.hasNext(); ) {
			FileItem item = (FileItem) itr.next();
			/*
			 * Handle Form Fields.
			 */
			if (item.isFormField()) {
				fields.setProperty(item.getFieldName(), item.getString());
			}
		}

		JobSpec jobSpec = MAPPER.readValue(fields.getProperty("jobSpec"), JobSpec.class);

		for (Iterator itr = items.iterator(); itr.hasNext(); ) {
			FileItem item = (FileItem) itr.next();
			if (!item.isFormField()) {
				//Handle Uploaded files.
				log("Spreadsheet upload for user " + principal.getName() + ": Field Name = " + item.getFieldName()
						+ ", File Name = " + item.getName()
						+ ", Content type = " + item.getContentType()
						+ ", File Size = " + item.getSize());
				if (item.getSize() > 0) {
					InputStream is = item.getInputStream();
					EtlClient etlClient = this.injector.getInstance(EtlClient.class);
					try {
						etlClient.upload(
								FilenameUtils.getName(item.getName()),
								jobSpec.getSourceConfigId(),
								item.getFieldName(), is);
						log("File '" + item.getName() + "' uploaded successfully");
					} finally {
						if (is != null) {
							try {
								is.close();
							} catch (IOException ignore) {
							}
						}
					}
				} else {
					log("File '" + item.getName() + "' ignored because it was zero length");
				}
			}
		}
		ServicesClient servicesClient = this.injector.getInstance(ServicesClient.class);
		URI uri = servicesClient.submitJob(jobSpec);
		String uriStr = uri.toString();
		Long jobId = Long.valueOf(uriStr.substring(uriStr.lastIndexOf("/") + 1));
		log("Job " + jobId + " submitted for user " + principal.getName());
		return jobId;
	}
}
