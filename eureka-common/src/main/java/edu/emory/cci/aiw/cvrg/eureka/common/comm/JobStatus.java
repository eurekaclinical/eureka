package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.Date;
import java.util.List;

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
	 * The date of the document upload.
	 */
	private Date uploadTime;
	
	/**
	 * A list of messages about the file or job processing status.
	 */
	private List<String> messages;
	
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
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
}
