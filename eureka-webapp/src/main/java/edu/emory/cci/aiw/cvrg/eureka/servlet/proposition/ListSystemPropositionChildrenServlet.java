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
package edu.emory.cci.aiw.cvrg.eureka.servlet.proposition;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class ListSystemPropositionChildrenServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ListSystemPropositionChildrenServlet.class);

	private String getDisplayName(DataElement e) {
        String displayName = "";

        if (e.getAbbrevDisplayName() != null
                && !e.getAbbrevDisplayName().equals("")) {

            displayName = e.getAbbrevDisplayName() + "(" + e.getKey() + ")";

        } else if (e.getDisplayName() != null && !e.getDisplayName().equals("")) {

            displayName = e.getDisplayName() + "(" + e.getKey() + ")";

        } else {

            displayName = e.getKey();

        }

        return displayName;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        super.init(config);
        String eurekaServicesUrl = config.getServletContext().getInitParameter(
                "eureka-services-url");

        Client client;

        client = CommUtils.getClient();
    }

    // private JsonTreeData createData(String id, String data) {
    private JsonTreeData createData(SystemElement se) {
        JsonTreeData d = new JsonTreeData();
        d.setData(this.getDisplayName(se));
        d.setKeyVal("id", se.getKey());
        LOGGER.info("key: " + se.getKey());
        if (se.getChildren() != null && se.getChildren().size() > 0) {
                d.setKeyVal("class", "jstree-closed");
            System.out.println("key: " + se.getKey());
        }

        return d;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Client client = null;
        client = CommUtils.getClient();
        Principal principal = req.getUserPrincipal();
        String userName = principal.getName();
        String eurekaServicesUrl = req.getSession().getServletContext()
                .getInitParameter("eureka-services-url");

        LOGGER.debug("got username {}", userName);
        WebResource webResource = client.resource(eurekaServicesUrl);
        User user = webResource.path("/api/user/byname/" + userName)
                .accept(MediaType.APPLICATION_JSON).get(User.class);

        List<JsonTreeData> l = new ArrayList<JsonTreeData>();
        String propId = req.getParameter("propId");

        // "/system/{userId}/{propKey}"
        String path = UriBuilder.fromPath("/").segment("api")
                .segment("proposition").segment("system")
                .segment("" + user.getId()).segment(propId).build().toString();
        SystemElement sysElement = webResource.path(path)
                .accept(MediaType.APPLICATION_JSON)
                .get(SystemElement.class);

        for (SystemElement propChild : sysElement.getChildren()) {
                JsonTreeData newData = createData(propChild);
                newData.setType("system");
                l.add(newData);
        }

        /*
         * for (String sysTarget : propWrapper.getSystemTargets()) {
         * PropositionWrapper propWrapperChild =
         * webResource.path("/api/proposition/system/"+ user.getId() + "/" +
         * sysTarget) .accept(MediaType.APPLICATION_JSON)
         * .get(PropositionWrapper.class); JsonTreeData newData =
         * createData(sysTarget, this.getDisplayName(propWrapperChild));
         * newData.setType("system"); l.add(newData); }
         */

        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        mapper.writeValue(out, l);
    }
}
