/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.common.props;


import com.google.inject.Singleton;
/**
 *
 * @author Miao Ai
 */
public class AppProperties extends AbstractProperties {  
	static AppProperties theInstance;
	/**
	 * Constructor.
	 */        
	public AppProperties() {
	}
        
	static AppProperties getInstance(){
		if(theInstance==null)
			theInstance = new AppProperties();
		return theInstance;
	}
      	@Override
	public String getProxyCallbackServer() {            
		return this.getValue("eureka.common.callbackserver");
	} 
}
       


