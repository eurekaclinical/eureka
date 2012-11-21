/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 Emory University
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
import javax.ws.rs.core.MediaType;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;


public class CommonsFileUploadServlet extends HttpServlet {

	/**
	 * Temp directory to write to if file size is too big.
	 */
	private static final String TMP_DIR_PATH = "/tmp";
	private File tmpDir;

	/**
	 * Normal directory to save files to.
	 * TODO: set value
	 */
	private static String DESTINATION_DIR_PATH = "";
	private File destinationDir;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		DESTINATION_DIR_PATH = config.getInitParameter("dest_dir");
		tmpDir = new File(TMP_DIR_PATH);
		if(!tmpDir.isDirectory()) {
			throw new ServletException(TMP_DIR_PATH + " is not a directory");
		}
		String realPath = getServletContext().getRealPath("/")+DESTINATION_DIR_PATH;

		System.out.println(realPath);
		destinationDir = new File(realPath);
		if(!destinationDir.isDirectory()) {
			throw new ServletException(DESTINATION_DIR_PATH+" is not a directory");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		DiskFileItemFactory  fileItemFactory = new DiskFileItemFactory ();
		String eurekaServicesUrl = request.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
		/*
		 *Set the size threshold, above which content will be stored on disk.
		 */
		fileItemFactory.setSizeThreshold(5*1024*1024); //5 MB
		/*
		 * Set the temporary directory to store the uploaded files of size above threshold.
		 */
		fileItemFactory.setRepository(tmpDir);

		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		try {
			/*
			 * Parse the request
			 */
			List items = uploadHandler.parseRequest(request);
			Iterator itr = items.iterator();
			while(itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				/*
				 * Handle Form Fields.
				 */
				if(item.isFormField()) {
					System.out.println("File Name = "+item.getFieldName()+", Value = "+item.getString());
				} else {
					//Handle Uploaded files.
					System.out.println("Field Name = "+item.getFieldName()+
						", File Name = "+item.getName()+
						", Content type = "+item.getContentType()+
						", File Size = "+item.getSize());

					Client c = CommUtils.getClient();
					Principal principal = request.getUserPrincipal();
					String userName = principal.getName();

					WebResource webResource = c.resource(eurekaServicesUrl);
					User user = webResource.path("/api/user/byname/"+userName)
							.accept(MediaType.APPLICATION_JSON).get(User.class);

					/*
					 * Write file to the ultimate location.
					 */
					File file = new File(destinationDir,""+user.getId());
					item.write(file);


					FileUpload fileUpload = new FileUpload();
					fileUpload.setLocation(getServletContext().getRealPath("/") +
							DESTINATION_DIR_PATH + "/" + user.getId());
					fileUpload.setUserId(user.getId());

					ClientResponse clientResponse = webResource.path("/api/job/add").post(
							ClientResponse.class, fileUpload);
					System.out.println(clientResponse.getClientResponseStatus());
				}


				response.sendRedirect(request.getContextPath() + "/protected/jobs");
			}
		}catch(FileUploadException ex) {
			log("Error encountered while parsing the request",ex);
		} catch(Exception ex) {
			log("Error encountered while uploading file",ex);
		}

	}

}
