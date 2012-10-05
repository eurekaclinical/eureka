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
