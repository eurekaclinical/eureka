package edu.emory.cci.aiw.cvrg.eureka.common.comm;

/**
 * A communication bean to transfer information about a job status.
 * 
 * @author sagrava
 * 
 */
public class JobStatus {

	/**
	 * The current step in the process.
	 */
	private int currentStep;
	
	/**
	 * The total number of steps in the process.
	 */
	private int totalSteps;
	
	/**
	 * The date of the document upload in string format.
	 */
	private String uploadTime;
	
	public int getCurrentStep() {
		return currentStep;
	}
	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}
	public int getTotalSteps() {
		return totalSteps;
	}
	public void setTotalSteps(int totalSteps) {
		this.totalSteps = totalSteps;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	
}
