/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.common.props;


/**
 *
 * @author Miao Ai
 */
public class AppPropertiesMode {
	private boolean demoMode;
	private boolean ephiProhibited;
	/**
	 * Constructor.
	 *
	 */
	public AppPropertiesMode() {
		AppProperties appProperties = AppProperties.getInstance();
		demoMode = Boolean.parseBoolean(appProperties.getValue("eureka.webapp.ephiprohibited"));
		ephiProhibited = Boolean.parseBoolean(appProperties.getValue("eureka.webapp.ephiprohibited"));
	}
	/**
	 * Gets the demoMode status.
	 *
	 * @return true or false.
	 */
	public boolean isDemoMode() {
		return this.demoMode;                
	}
	/**
	 * Sets the demoMode status.
	 * 
	 * @param inMode
	 */
	public void setDemoMode(boolean inMode) {
		this.demoMode = inMode;           
	}
	/**
	 * Gets the ephiProhibited status.
	 *
	 * @return ture or false.
	 */           
	public boolean isEphiProhibited() {		
		return this.ephiProhibited;
	}
	/**
	 * Sets the ephiProhibited status.
	 *
	 * @param inEphiProhibited
	 */           
	public void setEphiProhibited(boolean inEphiProhibited) {		
		this.ephiProhibited = inEphiProhibited;
	} 
}
