package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Data {
	
	private List<Data> children = new ArrayList<Data>();	
	private Map<String,String> attr = new HashMap<String, String>();
	private String id;
	private String data;

	public List<Data> getChildren() {
		return children;
	}

	public void setChildren(List<Data> nodes) {
		this.children = nodes;
	}
	
	public void addNodes(Data ...thechildren) {
		for (Data t : thechildren) {
			children.add(t);	
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getData() {
		return data;
	}

	public void setData(String text) {
		this.data = text;
	}

	public Map<String, String> getAttr() {
		return attr;
	}

	public void setAttr(Map<String, String> attr) {
		this.attr = attr;
	}
	
	public void setKeyVal(String key, String val) {
		attr.put(key, val);
	}
	
}
