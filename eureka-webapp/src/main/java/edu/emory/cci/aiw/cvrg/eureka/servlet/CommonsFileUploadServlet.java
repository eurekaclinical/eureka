/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
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
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.servlet;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobSpec;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.protempa.proposition.interval.Interval.Side;

public class CommonsFileUploadServlet extends HttpServlet {
	
	private File tmpDir;
	/**
	 * Normal directory to save files to. TODO: set value
	 */
	private ServicesClient servicesClient;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		tmpDir = FileUtils.getTempDirectory();
		if (!tmpDir.isDirectory()) {
			throw new ServletException(tmpDir.getAbsolutePath() + " is not a directory");
		}
		
		this.servicesClient = new ServicesClient(
				config.getServletContext()
				.getInitParameter("eureka-services-url"));
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			throw new ServletException(
					"Spreadsheet upload attempt: no user associated with the request");
		}
		try {
			Long jobId = submitJob(request, principal);
			response.sendRedirect(request.getContextPath() + "/protected/jobs?jobId=" + jobId);
		} catch (ParseException ex) {
			throw new ServletException("Invalid date range", ex);
		} catch (ClientException ex) {
			String msg = "Error encountered calling add job service";
			log("Spreadsheet upload attempt for user " + principal.getName()
					+ ": " + msg);
			throw new ServletException(msg, ex);
		} catch (FileUploadException ex) {
			String msg =
					"Error encountered while parsing the job upload request";
			log("Spreadsheet upload attempt for user " + principal.getName()
					+ ": " + msg);
			throw new ServletException(msg, ex);
		} catch (IOException ex) { //item.write, a third party library, throws Exception...
			String msg =
					"Error encountered while writing the uploaded file";
			log("Spreadsheet upload attempt for user " + principal.getName()
					+ ": " + msg);
			throw new ServletException(msg, ex);
		}
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
		fileItemFactory.setRepository(tmpDir);
		
		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		/*
		 * Parse the request
		 */
		List items = uploadHandler.parseRequest(request);
		Properties fields = new Properties();
		for (Iterator itr = items.iterator(); itr.hasNext();) {
			FileItem item = (FileItem) itr.next();
			/*
			 * Handle Form Fields.
			 */
			if (item.isFormField()) {
				fields.setProperty(item.getFieldName(), item.getString());
			}
		}
		for (Iterator itr = items.iterator(); itr.hasNext();) {
			FileItem item = (FileItem) itr.next();
			if (!item.isFormField()) {
				//Handle Uploaded files.
				log("Spreadsheet upload for user " + principal.getName() + ": Field Name = " + item.getFieldName()
						+ ", File Name = " + item.getName()
						+ ", Content type = " + item.getContentType()
						+ ", File Size = " + item.getSize());
				if (item.getSize() > 0) {
					InputStream is = item.getInputStream();
					try {
						this.servicesClient.upload(item.getName(),
								fields.getProperty("source"),
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
		JobSpec jobSpec = new JobSpec();
		jobSpec.setSourceConfigId(fields.getProperty("source"));
		jobSpec.setDestinationId(fields.getProperty("destination"));
		jobSpec.setDateRangeDataElementKey(
				fields.getProperty("dateRangeDataElementKey"));
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String earliestStr = fields.getProperty("earliestDate");
		if (earliestStr != null && !earliestStr.trim().isEmpty()) {
			jobSpec.setEarliestDate(df.parse(earliestStr));
		}
		jobSpec.setEarliestDateSide(
				Side.valueOf(
				fields.getProperty("dateRangeEarliestDateSide")));
		String latestStr = fields.getProperty("latestDate");
		if (latestStr != null && !latestStr.trim().isEmpty()) {
			jobSpec.setLatestDate(df.parse(latestStr));
		}
		jobSpec.setLatestDateSide(
				Side.valueOf(
				fields.getProperty("dateRangeLatestDateSide")));
		Long jobId = this.servicesClient.submitJob(jobSpec);
		log("Job " + jobId + " submitted for user " + principal.getName());
		return jobId;
	}
}
