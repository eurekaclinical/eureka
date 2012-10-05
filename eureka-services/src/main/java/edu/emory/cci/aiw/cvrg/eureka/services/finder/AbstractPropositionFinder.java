package edu.emory.cci.aiw.cvrg.eureka.services.finder;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public abstract class AbstractPropositionFinder<U, K> {

	private static Logger LOGGER = LoggerFactory.getLogger
		(AbstractPropositionFinder.class);
	private static final ExecutorService executorService =
		Executors.newSingleThreadExecutor();
	private final PropositionRetriever<U, K> retriever;

	protected AbstractPropositionFinder(PropositionRetriever<U,
		K> inRetriever) {
		this.retriever = inRetriever;
	}

	public PropositionWrapper find(U inUserId, K inKey) {
		LOGGER.debug("Finding {} for user {}", inUserId, inKey);
		Cache cache = this.getCache();
		Element element = cache.get(inKey);
		if (element == null) {
			PropositionWrapper wrapper =
				this.retriever.retrieve(inUserId, inKey);
			element = new Element(inKey, wrapper);
			cache.put(element);
		}

		PropositionWrapper wrapper = (PropositionWrapper) element.getValue();
		this.prefetch(inUserId, wrapper);
		return wrapper;
	}

	public void shutdown () {
		this.getCacheManager().removalAll();
		this.getCacheManager().shutdown();
	}

	private void prefetch(U inUserId, PropositionWrapper inWrapper) {
		final Cache cache = this.getCache();
		for (K key : this.getPrefetchKeys(inWrapper)) {
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

	abstract protected List<K> getPrefetchKeys(PropositionWrapper inWrapper);

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
			this.cache.put(new Element(this.key, this.retriever.retrieve
				(this.userId, this.key)));
		}
	}
}
