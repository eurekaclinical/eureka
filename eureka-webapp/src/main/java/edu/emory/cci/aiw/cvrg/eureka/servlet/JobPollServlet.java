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

import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobInfo;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobStatus;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class JobPollServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String eurekaServicesUrl = req.getSession().getServletContext()
                .getInitParameter("eureka-services-url");

        resp.setContentType("application/json");

        try {
            Client client = CommUtils.getClient();
            Principal principal = req.getUserPrincipal();
            String userName = principal.getName();

            WebResource webResource = client.resource(eurekaServicesUrl);
            User user = webResource.path("/api/user/byname/" + userName)
                    .accept(MediaType.APPLICATION_JSON).get(User.class);

            JobInfo jobInfo = webResource
                    .path("/api/job/status/" + user.getId())
                    .accept(MediaType.APPLICATION_JSON).get(JobInfo.class);

            if (jobInfo.getCurrentStep() == 0) {
                String emptyJson = "{}";
                resp.setContentLength(emptyJson.length());
                PrintWriter out = resp.getWriter();
                out.println(emptyJson);
                out.close();
                out.flush();
            } else {
                JobStatus jobStatus = new JobStatus();
                jobStatus.setCurrentStep(jobInfo.getCurrentStep());
                jobStatus.setTotalSteps(jobInfo.getTotalSteps());
                jobStatus.setMessages(jobInfo.getMessages());

                // Date uploadTime = new Date();
                //
                // if (jobInfo.getCurrentStep() < 4 &&
                // jobInfo.getFileUpload().getTimestamp() != null)
                // uploadTime = jobInfo.getFileUpload().getTimestamp();
                // else if (jobInfo.getCurrentStep() >= 4 &&
                // jobInfo.getJob().getTimestamp() != null) {
                // uploadTime = jobInfo.getJob().getTimestamp();
                // }
                // jobStatus.setUploadTime(uploadTime);
                jobStatus.setUploadTime(jobInfo.getTimestamp());

                ObjectMapper mapper = new ObjectMapper();
                resp.setContentLength(mapper.writeValueAsString(jobStatus)
                        .length());
                PrintWriter out = resp.getWriter();
                out.println(mapper.writeValueAsString(jobStatus));
                out.close();
                out.flush();

            }

        } catch (NoSuchAlgorithmException nsae) {
            throw new ServletException(nsae);
        } catch (KeyManagementException kme) {
            throw new ServletException(kme);
        }

    }
}
