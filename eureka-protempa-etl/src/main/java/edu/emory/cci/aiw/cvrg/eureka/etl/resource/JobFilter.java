package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;


public class JobFilter {

	private final Long jobId;
	private final Long userId;
	private final String state;
	private final java.util.Date from;
	private final java.util.Date to;

	private final byte score;


	public JobFilter (Long jobId , Long userId , String state , java.util.Date from , java.util.Date to) {

		this.jobId = jobId;
		this.userId = userId;
		this.state = state;
		this.from = from;
		this.to = to;

		byte b = 0;			
		b |= (jobId != null) ? 1:0;
		b |= (userId != null) ? 2:0;
		b |= (state != null) ? 4:0;
		b |= (from != null) ? 8:0;
		b |= (to != null) ? 16:0;
		score = b;
	}

	public boolean evaluate (Job j) {

		if (score == 0) {

			return true;
		}

		byte result = 0;
		result |= (((score & 1) != 0) && this.jobId == j.getId()) ? 1:0;
		result |= (((score & 2) != 0) && this.userId == j.getUserId()) ? 2:0;
		result |= (((score & 4) != 0) && this.state.equals(j.getCurrentState())) ? 4:0;
		result |= (((score & 8) != 0) && this.from.getTime() <= j.getCreationTime().getTime()) ? 8:0;
		result |= (((score & 16) != 0) && this.to.getTime() >= j.getCreationTime().getTime()) ? 16:0;

		return result == score;
	}
}
