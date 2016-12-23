package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TabularFileDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TabularFileDestinationTableColumnEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.protempa.PropositionDefinition;
import org.protempa.dest.AbstractQueryResultsHandler;
import org.protempa.dest.QueryResultsHandlerCloseException;
import org.protempa.dest.QueryResultsHandlerProcessingException;
import org.protempa.dest.QueryResultsHandlerValidationFailedException;
import org.protempa.proposition.Proposition;
import org.protempa.proposition.UniqueId;
import org.protempa.query.Query;

/**
 *
 * @author Andrew Post
 */
public class TabularFileQueryResultsHandler extends AbstractQueryResultsHandler {

	private final String queryId;
	private final String username;
	private final TabularFileDestinationEntity config;
	private Map<String, BufferedWriter> writers;
	private final EtlProperties etlProperties;

	TabularFileQueryResultsHandler(Query query, TabularFileDestinationEntity inTabularFileDestinationEntity, EtlProperties inEtlProperties) {
		assert inTabularFileDestinationEntity != null : "inTabularFileDestinationEntity cannot be null";
		this.etlProperties = inEtlProperties;
		this.queryId = query.getName();
		this.username = query.getUsername();
		this.config = inTabularFileDestinationEntity;
	}

	@Override
	public void validate() throws QueryResultsHandlerValidationFailedException {
	}

	@Override
	public void start(Collection<PropositionDefinition> cache) throws QueryResultsHandlerProcessingException {
		try {
			File outputFileDirectory = this.etlProperties.outputFileDirectory(this.config.getName());
			List<String> tableNames = this.config.getTableColumns()
					.stream()
					.map(TabularFileDestinationTableColumnEntity::getTableName)
					.distinct()
					.collect(Collectors.toCollection(ArrayList::new));
			this.writers = new HashMap<>();
			for (int i = 0, n = tableNames.size(); i < n; i++) {
				String tableName = tableNames.get(i);
				File file = new File(outputFileDirectory, tableName);
				this.writers.put(tableName, new BufferedWriter(new FileWriter(file)));
			}
		} catch (IOException ex) {
			throw new QueryResultsHandlerProcessingException(ex);
		}
	}

	@Override
	public void handleQueryResult(String keyId, List<Proposition> propositions, Map<Proposition, List<Proposition>> forwardDerivations, Map<Proposition, List<Proposition>> backwardDerivations, Map<UniqueId, Proposition> references) throws QueryResultsHandlerProcessingException {
	}

	@Override
	public void finish() throws QueryResultsHandlerProcessingException {
	}

	@Override
	public void close() throws QueryResultsHandlerCloseException {
		QueryResultsHandlerCloseException exception = null;
		if (this.writers != null) {
			for (BufferedWriter writer : this.writers.values()) {
				try {
					writer.close();
				} catch (IOException ex) {
					if (exception != null) {
						exception.addSuppressed(ex);
					} else {
						exception = new QueryResultsHandlerCloseException(ex);
					}
				}
				this.writers = null;
			}
		}
		if (exception != null) {
			throw exception;
		}
	}

}