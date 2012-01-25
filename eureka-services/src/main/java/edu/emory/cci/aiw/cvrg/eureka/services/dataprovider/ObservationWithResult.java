package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

/**
 * Holds information about an observation during a patient's visit, including
 * results of that observation.
 * 
 * @author hrathod
 * 
 */
public abstract class ObservationWithResult extends Observation {
	/**
	 * The results of the lab test, held as a String.
	 */
	private String resultAsStr;
	/**
	 * The results of the lab test, held as a number.
	 */
	private Double resultAsNum;
	/**
	 * The type of the lab performed.
	 */
	private int type;
	/**
	 * The units used to measure the results of the lab test.
	 */
	private String units;
	/**
	 * Any flags resulting from the results of the lab test.
	 */
	private String flag;

	/**
	 * Get the results of the lab test as a string.
	 * 
	 * @return the results of the lab test as a string.
	 */
	public String getResultAsStr() {
		return this.resultAsStr;
	}

	/**
	 * Set the results of the lab test, as a string.
	 * 
	 * @param inResultAsStr the results, as a string.
	 */
	public void setResultAsStr(String inResultAsStr) {
		this.resultAsStr = inResultAsStr;
	}

	/**
	 * Get the results of the lab test, as a number.
	 * 
	 * @return The results of the lab test, as a number.
	 */
	public Double getResultAsNum() {
		return this.resultAsNum;
	}

	/**
	 * Set the results of the lab test, as a number
	 * 
	 * @param inResultAsNum The results of the lab test, as a number.
	 */
	public void setResultAsNum(Double inResultAsNum) {
		this.resultAsNum = inResultAsNum;
	}

	/**
	 * Get the type of the lab test.
	 * 
	 * @return The type of the lab test.
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * Set the type of the lab test.
	 * 
	 * @param inTypeype The type of the lab test.
	 */
	public void setType(int inTypeype) {
		this.type = inTypeype;
	}

	/**
	 * Get the units used to measure the lab test results.
	 * 
	 * @return The units units to measure the lab test results.
	 */
	public String getUnits() {
		return this.units;
	}

	/**
	 * Set the units used to measure the lab test results.
	 * 
	 * @param inUnits The units units to measure the lab test results.
	 */
	public void setUnits(String inUnits) {
		this.units = inUnits;
	}

	/**
	 * Get the flag for the lab test.
	 * 
	 * @return The flag for the lab test.
	 */
	public String getFlag() {
		return this.flag;
	}

	/**
	 * Set the flag for the lab test.
	 * 
	 * @param inFlag The flat for the lab test.
	 */
	public void setFlag(String inFlag) {
		this.flag = inFlag;
	}
}
