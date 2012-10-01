package edu.emory.cci.aiw.cvrg.eureka.servlet.proposition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class JsonTreeData {
	
	private List<JsonTreeData> children = new ArrayList<JsonTreeData>();	
	private Map<String,String> attr = new HashMap<String, String>();
	private String id;
	private String data;
	private String type;
    private String state;
	
	public List<JsonTreeData> getChildren() {
		return children;
	}

	public void setChildren(List<JsonTreeData> nodes) {
		this.children = nodes;
	}
	
	public void addNodes(JsonTreeData ...thechildren) {
		for (JsonTreeData t : thechildren) {
			children.add(t);	
		}
	}

	public String getId() {
		return attr.get("id");
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
    
    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }
}
