package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;

/**
 * REST Web Service
 *
 * @author hrathod
 */
@Path("/proposition")
public class PropositionResource {

	private final PropositionDao propositionDao;
	private final UserDao userDao;
	private final ServiceProperties applicationProperties;

	/**
	 * Creates a new instance of PropositionResource
	 */
	@Inject
	public PropositionResource(PropositionDao inPropositionDao,
			UserDao inUserDao, ServiceProperties inApplicationProperties) {
		this.propositionDao = inPropositionDao;
		this.userDao = inUserDao;
		this.applicationProperties = inApplicationProperties;
	}

	@GET
	@Path("/system/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PropositionWrapper> getSystemPropositions() {
		List<PropositionWrapper> wrappers = new
				ArrayList<PropositionWrapper>();
		wrappers.add(wrap(this.fetchSystemProposition("ICD9:ICD9")));
		return wrappers;
	}

	@GET
	@Path("/system/{propKey}")
	@Produces(MediaType.APPLICATION_JSON)
	public PropositionWrapper getSystemProposition(
			@PathParam("propKey") String inKey) {
		return wrap(fetchSystemProposition(inKey));
	}

	@GET
	@Path("/user/list/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Proposition> getUserPropositions(
			@PathParam("userId") Long inUserId) {
		return this.userDao.retrieve(inUserId).getPropositions();
	}

	@GET
	@Path("/user/get/{propId}")
	@Produces(MediaType.APPLICATION_JSON)
	public PropositionWrapper getUserProposition(
			@PathParam("propId") Long inPropositionId) {
		PropositionWrapper wrapper = null;
		Proposition proposition = this.propositionDao.retrieve
				(inPropositionId);
		if (proposition != null) {
			if (proposition.isInSystem()) {
				wrapper = wrap(this.fetchSystemProposition(proposition
						.getKey()));
			} else {
				wrapper = wrap(proposition);
			}
		}
		return wrapper;
	}

	@PUT
	@Path("/user/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateProposition(PropositionWrapper inWrapper) {
		Proposition proposition = unwrap(inWrapper);
		proposition.setLastModified(new Date());
		this.propositionDao.update(proposition);
	}

	@POST
	@Path("/user/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertProposition(PropositionWrapper inWrapper) {
		Proposition proposition = unwrap(inWrapper);
		Date now = new Date();
		proposition.setCreated(now);
		proposition.setLastModified(now);
		this.propositionDao.create(proposition);
	}

	private Proposition fetchSystemProposition(String inId) {

		PropositionWrapper wrapper = null;
		Proposition proposition = null;

		try {
			Client client = CommUtils.getClient();
			WebResource resource = client.resource(applicationProperties
					.getEtlPropositionGetUrl());
			wrapper = resource.path(inId).accept(MediaType.APPLICATION_JSON)
			                  .get(PropositionWrapper.class);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		if (wrapper != null) {
			proposition = unwrap(wrapper);
		}

		return proposition;
	}

	private Proposition unwrap(PropositionWrapper inWrapper) {
		Proposition proposition;
		List<Proposition> targets = new ArrayList<Proposition>();

		if (inWrapper.getId() != null) {
			Long id = Long.valueOf(inWrapper.getId());
			proposition = this.propositionDao.retrieve(id);
		} else {
			proposition = new Proposition();
		}

		for (Long id : inWrapper.getUserTargets()) {
			targets.add(this.propositionDao.retrieve(id));
		}

		for (String key : inWrapper.getSystemTargets()) {
			Proposition p = this.propositionDao.getByKey(key);
			if (p == null) {
				p = new Proposition();
				p.setKey(key);
				p.setInSystem(true);
			}
			targets.add(p);
		}

		if (inWrapper.getType() == PropositionWrapper.Type.AND) {
			proposition.setAbstractedFrom(targets);
		} else {
			proposition.setInverseIsA(targets);
		}

		proposition.setKey(inWrapper.getKey());
		proposition.setAbbrevDisplayName(inWrapper.getAbbrevDisplayName());
		proposition.setDisplayName(inWrapper.getDisplayName());
		proposition.setInSystem(inWrapper.isInSystem());
		proposition.setUser(this.userDao.retrieve(inWrapper.getUserId()));
		return proposition;
	}

	private PropositionWrapper.Type getType(Proposition inProposition) {
		if ((inProposition.getTemporalPattern() != null) || (
				(inProposition.getAbstractedFrom() != null) && (!inProposition
						.getAbstractedFrom().isEmpty()))) {
			return PropositionWrapper.Type.AND;
		} else {
			return PropositionWrapper.Type.OR;
		}

	}

	private List<Proposition> getTargets(Proposition inProposition,
			PropositionWrapper.Type inType) {
		List<Proposition> propositions;
		List<Proposition> targets;

		if (inType == PropositionWrapper.Type.AND) {
			targets = inProposition.getAbstractedFrom();
		} else {
			targets = inProposition.getInverseIsA();
		}

		if (targets == null) {
			propositions = new ArrayList<Proposition>();
		} else {
			propositions = targets;
		}

		return propositions;
	}

	private PropositionWrapper wrap(Proposition inProposition) {

		PropositionWrapper wrapper = new PropositionWrapper();
		PropositionWrapper.Type type = this.getType(inProposition);
		List<String> systemTargets = new ArrayList<String>();
		List<Long> userTargets = new ArrayList<Long>();

		for (Proposition target : this.getTargets(inProposition, type)) {
			if (target.isInSystem()) {
				systemTargets.add(target.getKey());
			} else {
				userTargets.add(target.getId());
			}
		}

		if (inProposition.getId() != null) {
			wrapper.setId(String.valueOf(inProposition.getId()));
		}

		if (inProposition.getUser() != null) {
			wrapper.setUserId(inProposition.getUser().getId());
		}

		wrapper.setInSystem(inProposition.isInSystem());
		wrapper.setType(type);
		wrapper.setAbbrevDisplayName(inProposition.getAbbrevDisplayName());
		wrapper.setDisplayName(inProposition.getDisplayName());
		wrapper.setKey(inProposition.getKey());
		wrapper.setSystemTargets(systemTargets);
		wrapper.setUserTargets(userTargets);

		return wrapper;
	}
}
