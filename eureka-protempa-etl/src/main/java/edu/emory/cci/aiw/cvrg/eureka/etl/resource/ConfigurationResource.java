package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;

@Path("/configuration")
public class ConfigurationResource {

	private static final Logger LOGGER =
			LoggerFactory.getLogger(ConfigurationResource.class);
	private final ConfDao confDao;

	@Inject
	public ConfigurationResource(ConfDao inConfDao) {
		this.confDao = inConfDao;
	}

	@GET
	@Path("/get/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Configuration getConfByUserId(@PathParam("userId") Long userId) {
		LOGGER.debug("Configuration request for user {}", userId);
		return this.confDao.getByUserId(userId);
	}

	@POST
	@Path("/submit")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response submitConfiguration(Configuration conf) {
		this.confDao.create(conf);
		LOGGER.debug("Request to save Configuration");
		return Response.ok().build();
	}
}
