package edu.emory.cci.aiw.cvrg;

public class JobEvent {

	//	id & serialNumber are a compound key in this table.
	//	the corresponding orm tool should be made to recognize
	//	that the serialNumber field increases as event records
	//	get added... sorting desc would give the most recent
	//	event. timeStamp is too noisy to be reliably used in
	//	place of serialNumber.

	private long id;			//	foreignKey pointing to Job.id
	private long serialNumber;	//	obtained from an Oracle sequence object

	private java.util.Date timeStamp;
	private String state;
	private String[] exceptionStackTrace;
	private String message;
}
