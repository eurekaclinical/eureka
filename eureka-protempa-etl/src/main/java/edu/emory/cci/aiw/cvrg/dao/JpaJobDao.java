package edu.emory.cci.aiw.cvrg.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;


public class JpaJobDao implements JobDao {

	private final EntityManager entityManager;

//	static class JobFilter {
//
//		long jobId;
//		long userId;
//
//		String status;
//
//		java.sql.Date from;
//		java.sql.Date to;
//
//		final byte score;
//
//
//		public JobFilter (long jobId , long userId , String status , java.sql.Date from , java.sql.Date to) {
//
//			byte mask = 0;
//
////			mask |= (jobId > 0L) ? 1:0;
////			mask |= (userId > 0L) ? 2:0;
////			mask != (status != null) ? 4:0;
//			if (jobId > 0L) {
//				mask |= 1;
//			}
//			if (userId > 0L) {
//				mask |= 2;
//			}
//			if (status != null) {
//				mask |= 4;
//			}
//			if (from != null) {
//				mask |= 8;
//			}
//			if (to != null) {
//				mask |= 16;
//			}
//			score = mask;
//		}
//
//		public boolean evaluate (Job j) {
//
//			if (score == 0) {
//
//				return true;
//			}
//			byte result = 0;
//			if (((score & 1) != 0) && this.jobId == j.getId()) {
//
//				result |= 1;
//			}
//			if (((score & 2) != 0) && this.userId == j.getUserId()) {
//
//				result |= 2;
//			}
//			if (((score & 4) != 0) && this.status.equals(j.getStatus())) {
//
//				result |= 4;
//			}
//			if (((score & 8) != 0) && this.from.getTime() <= j.getCreationTime().getTime()) {
//
//				result |= 8;
//			}
//			if (((score & 16) != 0) && this.to.getTime() >= j.getCreationTime().getTime()) {
//
//				result |= 16;
//			}
//			return result == score;
//		}
//	}

	@Inject
	public JpaJobDao (EntityManager manager) {

		this.entityManager = manager;
	}


	public void save (Job job) {

	}

	public Job get (Long id) {

		return null;
	}


    public List<Job> getJobs() {

//    	final Query query = this.entityManager.createQuery("select j from Job j");
//        @SuppressWarnings("unchecked")
//        final List<Job> resultList = query.getResultList();

//        List<Job> ret = new ArrayList<Job>(resultList.size());

//        for (Job candidate : resultList) {
//
//        	
//        }


//        return ret;
    	return null;
    }


}
