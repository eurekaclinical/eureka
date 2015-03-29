package edu.emory.cci.aiw.cvrg.eureka.services.resource;

/*
 * #%L
 * Eureka Services
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
import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LocalUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.clients.I2b2Client;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailException;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PasswordGenerator;
import edu.emory.cci.aiw.cvrg.eureka.common.util.StringUtil;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.LocalUserDao;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
@Path("/passwordresetrequest")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PasswordResetResource {

	private static final ResourceBundle messages
			= ResourceBundle.getBundle("Messages");

	/**
	 * The class logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			PasswordResetResource.class);
	private final LocalUserDao localUserDao;
	private final PasswordGenerator passwordGenerator;
	private final I2b2Client i2b2Client;
	private final EmailSender emailSender;
	private final ServiceProperties properties;

	@Inject
	public PasswordResetResource(LocalUserDao inLocalUserDao,
			EmailSender inEmailSender, I2b2Client inClient,
			PasswordGenerator inPasswordGenerator,
			ServiceProperties inProperties) {
		this.localUserDao = inLocalUserDao;
		this.emailSender = inEmailSender;
		this.i2b2Client = inClient;
		this.passwordGenerator = inPasswordGenerator;
		this.properties = inProperties;
	}

	/**
	 * Reset the given user's password. The password is randomly generated.
	 *
	 * @param inUsername The username for the user whose password should be
	 * reset.
	 * @throws HttpStatusException if the password can not be reset properly.
	 */
	@Path("/{username}")
	@POST
	public void resetPassword(@PathParam("username") final String inUsername) {
		LocalUserEntity user = this.localUserDao.getByName(inUsername);
		LOGGER.debug("Resetting user: {}", user);
		if (user == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		} else {
			String passwordHash;
			String password = this.passwordGenerator.generatePassword();
			try {
				passwordHash = StringUtil.md5(password);
			} catch (NoSuchAlgorithmException e) {
				LOGGER.error(e.getMessage(), e);
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, e.getMessage(), e);
			}

			user.setPassword(passwordHash);
			user.setPasswordExpiration(Calendar.getInstance().getTime());
			if (this.properties.getI2b2URL() != null) {
				this.i2b2Client.changePassword(user.getEmail(), password);
			}
			this.localUserDao.update(user);
			try {
				this.emailSender.sendPasswordResetMessage(user, password);
			} catch (EmailException e) {
				LOGGER.error(e.getMessage(), e);
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, e.getMessage(), e);
			}
		}
		LOGGER.debug("Reset user to: {}", user);
	}
}
