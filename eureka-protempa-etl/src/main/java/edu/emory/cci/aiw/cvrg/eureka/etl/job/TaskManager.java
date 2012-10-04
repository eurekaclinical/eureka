package edu.emory.cci.aiw.cvrg.eureka.etl.job;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.protempa.PropositionDefinition;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class TaskManager {
	private final ExecutorService executorService = Executors
		.newFixedThreadPool(4);
	private final Provider<Task> taskProvider;

	@Inject
	public TaskManager (Provider<Task> inTaskProvider) {
		this.taskProvider = inTaskProvider;
	}

	public void queueTask (Long inJobId, List<PropositionDefinition> inPropositionDefinitions) {
		Task task = this.taskProvider.get();
		task.setJobId(inJobId);
		task.setPropositionDefinitions(inPropositionDefinitions);
		this.executorService.execute(task);
	}

	public void shutdown () {
		this.executorService.shutdown();
	}

	public void shutdownNow () {
		this.executorService.shutdownNow();
	}
}
