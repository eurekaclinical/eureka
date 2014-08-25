package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*
 * #%L
 * Eureka Protempa ETL
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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Cohort;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlCohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlI2B2Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Node;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationGroupMembership;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlGroup;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.NodeEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.NodeToNodeEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
public final class Destinations {
	
	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Destinations.class);

	private final EtlGroupDao groupDao;
	private final EtlUserEntity etlUser;
	private final DestinationDao destinationDao;
	private final EtlProperties etlProperties;

	public Destinations(EtlProperties inEtlProperties, EtlUserEntity inEtlUser,
			DestinationDao inDestinationDao, EtlGroupDao inGroupDao) {
		this.groupDao = inGroupDao;
		this.etlUser = inEtlUser;
		this.destinationDao = inDestinationDao;
		this.etlProperties = inEtlProperties;
	}

	public void create(EtlDestination etlDestination) {
		if (etlDestination instanceof EtlCohortDestination) {
			CohortDestinationEntity cde = new CohortDestinationEntity();
			cde.setName(etlDestination.getName());
			cde.setDescription(etlDestination.getDescription());
			cde.setOwner(this.etlUser);
			Date now = new Date();
			cde.setCreatedAt(now);
			cde.setUpdatedAt(now);
			Cohort cohort = ((EtlCohortDestination) etlDestination).getCohort();
			CohortEntity cohortEntity = new CohortEntity();
			Node node = cohort.getNode();
			NodeToNodeEntityVisitor v = new NodeToNodeEntityVisitor();
			node.accept(v);
			cohortEntity.setNode(v.getNodeEntity());
			cde.setCohort(cohortEntity);
			this.destinationDao.create(cde);
		} else {
			throw new HttpStatusException(Response.Status.BAD_REQUEST, "Can't create i2b2 destinations via web services yet");
		}
	}

	public void update(EtlDestination etlDestination) {
		if (etlDestination instanceof EtlCohortDestination) {
			DestinationEntity oldEntity = this.destinationDao.retrieve(etlDestination.getId());
			if (oldEntity == null || !(oldEntity instanceof CohortDestinationEntity)) {
				throw new HttpStatusException(Response.Status.NOT_FOUND);
			}
			if (!this.etlUser.getId().equals(etlDestination.getOwnerUserId())) {
				throw new HttpStatusException(Response.Status.NOT_FOUND);
			}
			CohortDestinationEntity cde = new CohortDestinationEntity();
			cde.setId(etlDestination.getId());
			cde.setName(etlDestination.getName());
			cde.setDescription(etlDestination.getDescription());
			cde.setOwner(this.etlUser);
			cde.setCreatedAt(oldEntity.getCreatedAt());
			Date now = new Date();
			cde.setUpdatedAt(now);
			
			Cohort cohort = ((EtlCohortDestination) etlDestination).getCohort();
			CohortEntity cohortEntity = new CohortEntity();
			cohortEntity.setId(cohort.getId());
			cde.setCohort(cohortEntity);
			Node node = cohort.getNode();
			NodeToNodeEntityVisitor v = new NodeToNodeEntityVisitor();
			node.accept(v);
			NodeEntity nodeEntity = v.getNodeEntity();
			cohortEntity.setNode(nodeEntity);
			this.destinationDao.update(cde);
		} else {
			throw new HttpStatusException(Response.Status.BAD_REQUEST, "Can't update i2b2 destinations via web services yet");
		}
	}

	List<DestinationEntity> configs(EtlUserEntity user) {
		return user.getDestinations();
	}

	List<DestinationGroupMembership> groupConfigs(EtlGroup group) {
		return group.getDestinations();
	}

	String toConfigId(File file) {
		return FromConfigFile.toDestId(file);
	}

	public List<EtlI2B2Destination> getAllI2B2s() {
		List<EtlI2B2Destination> result = new ArrayList<>();
		I2B2DestinationsDTOExtractor extractor
				= new I2B2DestinationsDTOExtractor(this.etlProperties, this.etlUser, this.groupDao);
		for (I2B2DestinationEntity configEntity
				: this.destinationDao.getAllI2B2Destinations()) {
			EtlI2B2Destination dto = extractor.extractDTO(configEntity);
			if (dto != null) {
				result.add(dto);
			}
		}
		return result;
	}

	public List<EtlCohortDestination> getAllCohorts() {
		List<EtlCohortDestination> result = new ArrayList<>();
		CohortDestinationsDTOExtractor extractor
				= new CohortDestinationsDTOExtractor(this.etlUser, this.groupDao);
		for (CohortDestinationEntity configEntity
				: this.destinationDao.getAllCohortDestinations()) {
			EtlCohortDestination dto = extractor.extractDTO(configEntity);
			if (dto != null) {
				result.add(dto);
			}
		}
		return result;
	}

	/**
	 * Gets the specified source extractDTO. If it does not exist or the current
	 * user lacks read permissions for it, this method returns
	 * <code>null</code>.
	 *
	 * @return a extractDTO.
	 */
	public final EtlDestination getOne(String configId) {
		if (configId == null) {
			throw new IllegalArgumentException("configId cannot be null");
		}
		DestinationDTOExtractorVisitor visitor
				= new DestinationDTOExtractorVisitor(this.etlProperties, this.etlUser, this.groupDao);
		
		DestinationEntity byName = this.destinationDao.getByName(configId);
		if (byName == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		}
		byName.accept(visitor);
		return visitor.getEtlDestination();
	}

	/**
	 * Gets all configs for which the current user has read permissions.
	 *
	 * @return a {@link List} of configs.
	 */
	public final List<EtlDestination> getAll() {
		List<EtlDestination> result = new ArrayList<>();
		DestinationDTOExtractorVisitor visitor
				= new DestinationDTOExtractorVisitor(this.etlProperties, this.etlUser, this.groupDao);
		for (DestinationEntity configEntity : this.destinationDao.getAll()) {
			configEntity.accept(visitor);
			EtlDestination dto = visitor.getEtlDestination();
			if (dto != null) {
				result.add(dto);
			}
		}
		return result;
	}

	void delete(String destId) {
		DestinationEntity dest = this.destinationDao.getByName(destId);
		if (dest == null || !this.etlUser.equals(dest.getOwner())) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		}
		this.destinationDao.remove(dest);
	}

}
