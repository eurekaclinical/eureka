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
package edu.emory.cci.aiw.cvrg.eureka.services.finder;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.arp.javautil.arrays.Arrays;
import org.protempa.PropositionDefinition;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class SystemPropositionFinder extends AbstractPropositionFinder<
	String> {

	private static final String CACHE_NAME = "systemPropositions";
	private final SystemPropositionRetriever retriever;
	private final CacheManager cacheManager;
	private final Cache cache;

	@Inject
	public SystemPropositionFinder(SystemPropositionRetriever inRetriever) {
		super(inRetriever);
		this.retriever = inRetriever;
		this.cacheManager = CacheManager.create();
		this.cache = this.cacheManager.getCache(CACHE_NAME);
	}
	
	/**
	 * Finds all of the system elements given by the keys for the given user
	 * 
	 * @param sourceConfigId the ID of the source config to use for the look-up
	 * @param inKeys the keys of the system elements to look up
	 * @param withChildren whether to find the given system elements' children as well
	 * @return a {@link List} of {@link SystemElement}s
	 * @throws PropositionFindException
	 */
	public List<PropositionDefinition> findAll(
	        String sourceConfigId, List<String> inKeys, Boolean withChildren) throws PropositionFindException {
		return this.retriever.retrieveAll(sourceConfigId, inKeys, withChildren);
	}

	@Override
	protected List<String> getPrefetchKeys(PropositionDefinition inProposition) {
		List<String> keys = new ArrayList<>();

		keys.addAll(Arrays.<String> asList(inProposition.getInverseIsA()));
		return keys;
	}

	@Override
	protected Cache getCache() {
		return cache;
	}

	@Override
	protected CacheManager getCacheManager() {
		return cacheManager;
	}
}
