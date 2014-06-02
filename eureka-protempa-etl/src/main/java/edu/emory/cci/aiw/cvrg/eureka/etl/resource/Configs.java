/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ConfigEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlGroup;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUser;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.GroupMembership;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfigDao;

/**
 *
 * @author arpost
 */
public abstract class Configs<E, F extends ConfigEntity> {

	private final String name;
	private final EtlUser user;
	private final EtlProperties etlProperties;
	private final File configDir;
	private final ConfigDao configDao;

	Configs(String inName, EtlProperties inEtlProperties, EtlUser inEtlUser, File inConfigDir, ConfigDao inConfigDao) {
		assert inName != null : "inName cannot be null";
		assert inEtlProperties != null : "inEtlProperties cannot be null";
		assert inEtlUser != null : "inEtlUser cannot be null";
		assert inConfigDir != null : "inConfigDir cannot be null";
		assert inConfigDao != null : "inConfigDao cannot be null";
		this.name = inName;
		this.user = inEtlUser;
		this.etlProperties = inEtlProperties;
		if (!inConfigDir.exists()) {
			try {
				inConfigDir.mkdir();
			} catch (SecurityException ex) {
				throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR,
						"Could not create " + this.name + " config directory", ex);
			}
		}
		this.configDir = inConfigDir;
		this.configDao = inConfigDao;
	}

	/**
	 * Gets the specified source config. If it does not exist or the current
	 * user lacks read permissions for it, this method returns
	 * <code>null</code>.
	 *
	 * @return a config.
	 */
	public final E getOne(String configId) {
		if (configId == null) {
			throw new IllegalArgumentException("configId cannot be null");
		}
		Perm perm = perm(configId);
		if (perm != null && perm.read) {
			return config(configId, perm);
		} else {
			return null;
		}
	}

	/**
	 * Gets all configs for which the current user has read permissions.
	 *
	 * @return a {@link List} of configs.
	 */
	public final List<E> getAll() {
		List<E> result = new ArrayList<>();
		try {
			File[] files = this.configDir.listFiles();
			if (files == null) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR,
						StringUtils.capitalize(this.name) + " config directory " + this.configDir.getAbsolutePath() + " does not exist");
			}
			for (File file : files) {
				E d = getOne(toConfigId(file));
				if (d != null) {
					result.add(d);
				}
			}
		} catch (SecurityException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR,
					StringUtils.capitalize(this.name) + " config directory " + this.configDir.getAbsolutePath() + " could not be accessed", ex);
		}
		return result;
	}

	final Perm perm(String configId) {
		ConfigEntity configEntity = this.configDao.getByName(configId);
		if (configEntity != null) {
			EtlUser owner = configEntity.getOwner();
			if (this.user.equals(owner)) {
				return new Perm(owner, true, true, true);
			}

			boolean read = false;
			boolean write = false;
			boolean execute = false;
			List<EtlGroup> groups = user.getGroups();
			if (groups != null) {
				for (EtlGroup group : groups) {
					List<? extends GroupMembership> configPerms = groupConfigs(group);
					if (configPerms != null) {
						for (GroupMembership groupMembership : configPerms) {
							if (groupMembership.configName().equals(configId)) {
								if (!read) {
									read = groupMembership.isGroupRead();
								}
								if (!write) {
									write = groupMembership.isGroupWrite();
								}
								if (!execute) {
									execute = groupMembership.isGroupExecute();
								}
							}
						}
					}
				}
			}
			return new Perm(owner, read, write, execute);
		} else {
			return null;
		}
	}

	EtlProperties getEtlProperties() {
		return etlProperties;
	}

	abstract String toConfigId(File file);

	abstract E config(String configId, Perm perm);

	abstract List<F> configs(EtlUser user);

	abstract List<? extends GroupMembership> groupConfigs(EtlGroup group);
}
