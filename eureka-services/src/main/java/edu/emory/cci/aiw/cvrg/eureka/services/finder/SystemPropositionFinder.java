/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.finder;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@Singleton
public class SystemPropositionFinder extends AbstractPropositionFinder<Long,
	String> {

	private static final String CACHE_NAME = "systemPropositions";
	private final CacheManager cacheManager;
	private final Cache cache;

	@Inject
	public SystemPropositionFinder(SystemPropositionRetriever inRetriever) {
		super(inRetriever);
		this.cacheManager = CacheManager.create();
		this.cache = this.cacheManager.getCache(CACHE_NAME);
	}

	@Override
	protected List<String> getPrefetchKeys(PropositionWrapper inWrapper) {
		List<String> keys = new ArrayList<String>();
		for (PropositionWrapper child : inWrapper.getChildren()) {
			if (child.isInSystem()) {
				keys.add(child.getKey());
			}
		}
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
