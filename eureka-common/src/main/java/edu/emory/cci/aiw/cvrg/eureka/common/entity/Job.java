package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonManagedReference;

/**
 * Holds information about a job that is sent from the services layer to the
 * back-end layer.
 * 
 * @author hrathod
 * 
 */
@XmlRootElement
@Entity
@Table(name = "jobs")
public class Job {

	/**
	 * The unique identifier for the job request.
	 */
	@Id
	@GeneratedValue
	private Long id;
	/**
	 * The initial timestamp when the job was started.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	/**
	 * The unique identifier of the configuration to use for this job.
	 */
	private Long configurationId;
	/**
	 * The unique identifier of the user submitting the job request.
	 */
	private Long userId;
	/**
	 * The events generated for the job.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = JobEvent.class)
	private List<JobEvent> jobEvents = new ArrayList<JobEvent>();;

	private static class JobEventComparator implements Comparator<JobEvent> {

		public int compare (JobEvent a , JobEvent b) {

			if (a.getId()==null && b.getId()==null) {

				return 0;
			}
			if (a.getId()==null) {

				return 1;
			}
			if (b.getId()==null) {

				return -1;
			}
			return a.getId().compareTo(b.getId());
		}

		public boolean equals (Object obj) {

			return this.equals(obj);
		}
	}

	private static JobEventComparator JOB_EVENT_COMPARATOR = new JobEventComparator();

	/**
	 * Get the unique identifier for the job request.
	 * 
	 * @return The unique identifier for the job request.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the unique identifier for the job request.
	 * 
	 * @param inId The unique identifier for the job request.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param inTimestamp the timestamp to set
	 */
	public void setTimestamp(Date inTimestamp) {
		this.timestamp = inTimestamp;
	}

	/**
	 * Get the unique identifier of the configuration to be used for the job
	 * request.
	 * 
	 * @return The unique identifier of the configuration.
	 */
	public Long getConfigurationId() {
		return this.configurationId;
	}

	/**
	 * Set the unique identifier of the configuration to be used for the job
	 * request.
	 * 
	 * @param inConfigurationId The unique identifier of the configuration.
	 */
	public void setConfigurationId(Long inConfigurationId) {
		this.configurationId = inConfigurationId;
	}

	/**
	 * Get the unique identifier for the user who submitted the request.
	 * 
	 * @return The unique identifier for the user.
	 */
	public Long getUserId() {
		return this.userId;
	}

	/**
	 * Set the unique identifier for the user who submitted the request.
	 * 
	 * @param inUserId The unique identifier for the user.
	 */
	public void setUserId(Long inUserId) {
		this.userId = inUserId;
	}

	/**
	 * @return the jobEvents
	 */
	@JsonManagedReference("job-event")
	public List<JobEvent> getJobEvents() {
		return this.jobEvents;
	}

	/**
	 * @param inJobEvents the jobEvents to set
	 */
	public void setJobEvents(List<JobEvent> inJobEvents) {
		this.jobEvents = inJobEvents;
	}





	public String getCurrentState() {

		JobEvent jev = getSortedEvents().last();
		return (jev == null) ? "" : jev.getState();
	}

	public void setNewState (String state , String message , String[] stackTrace) {

		JobEvent jev = new JobEvent();
		jev.setJob(this);
		jev.setTimeStamp (new Date (System.currentTimeMillis()));
		jev.setState (state);
		jev.setMessage (message);
		jev.setExceptionStackTrace (stackTrace);
		jev.setJob (this);
		this.jobEvents.add (jev);
	}

	public Date getCreationTime() {

		JobEvent jev = getSortedEvents().first();
		return (jev == null) ? null : jev.getTimeStamp();
	}

	private TreeSet<JobEvent> getSortedEvents() {

		if (this.jobEvents == null || this.jobEvents.size() == 0) {

			return new TreeSet<JobEvent>();
		}
		TreeSet<JobEvent> ts = new TreeSet<JobEvent> (JOB_EVENT_COMPARATOR);
		ts.addAll (this.jobEvents);
		return ts;
	}
}
