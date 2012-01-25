package edu.emory.cci.aiw.cvrg;


public class Job {

	private long jobId;		//	serialNumber obtained from an Oracle sequence object
	private long userId;	//	foreignKey pointing to User.userId

	private java.util.Date created;


	public long getId() {

		return jobId;
	}

}
