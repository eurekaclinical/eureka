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

import com.sun.jersey.api.client.UniformInterfaceException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public abstract class AbstractPropositionFinder<U, K> {

	private static Logger LOGGER = LoggerFactory.getLogger(AbstractPropositionFinder.class);
	private static final ExecutorService executorService =
			Executors.newSingleThreadExecutor();
	private final PropositionRetriever<U, K> retriever;

	protected AbstractPropositionFinder(PropositionRetriever<U, K> inRetriever) {
		this.retriever = inRetriever;
	}

	/**
	 * Retrieves a proposition definition from the {@link PropositionRetriever}
	 * specified in this object's constructor.
	 *
	 * @param inUserId a user id.
	 * @param inKey a proposition key.
	 * @return the proposition definition, or <code>null</code> if there is no
	 * proposition definition with the specified key for the specified user.
	 *
	 * @throws UniformInterfaceException if an error occurred looking for the
	 * proposition definition.
	 */
	public PropositionDefinition find(U inUserId, K inKey)
			throws PropositionFindException {
		LOGGER.debug("Finding {} for user {}", inUserId, inKey);
		Cache cache = this.getCache();
		Element element = cache.get(inKey);
		if (element == null) {
			PropositionDefinition propDef =
					this.retriever.retrieve(inUserId, inKey);
			element = new Element(inKey, propDef);
			cache.put(element);
		}

		PropositionDefinition propDef = (PropositionDefinition) element.getValue();
//		this.prefetch(inUserId, propDef);
		return propDef;
	}

	public void shutdown() {
		this.getCacheManager().removalAll();
		this.getCacheManager().shutdown();
	}

	private void prefetch(U inUserId, PropositionDefinition inProposition) {
		final Cache cache = this.getCache();
		for (K key : this.getPrefetchKeys(inProposition)) {
			Element element = cache.get(key);
			if (element == null) {
				LOGGER.debug("Prefetching {}", key);
				Runnable runnable =
						new RetrieveRunnable(inUserId, key, cache,
						this.retriever);
				executorService.execute(runnable);
			}
		}
	}

	abstract protected CacheManager getCacheManager();

	abstract protected List<K> getPrefetchKeys(PropositionDefinition inWrapper);

	abstract protected Cache getCache();

	private class RetrieveRunnable implements Runnable {

		private final U userId;
		private final K key;
		private final Cache cache;
		private final PropositionRetriever<U, K> retriever;

		RetrieveRunnable(U inUserId, K inKey, Cache inCache,
				PropositionRetriever<U, K> inRetriever) {
			this.userId = inUserId;
			this.key = inKey;
			this.cache = inCache;
			this.retriever = inRetriever;
		}

		@Override
		public void run() {
			try {
				PropositionDefinition pd =
						this.retriever.retrieve(this.userId, this.key);
				this.cache.put(new Element(this.key, pd));
			} catch (PropositionFindException ex) {
				LOGGER.error("Could not retrieve proposition definition", ex);
			}
		}
	}
}
