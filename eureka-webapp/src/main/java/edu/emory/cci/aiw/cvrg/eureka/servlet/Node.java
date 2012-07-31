package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.util.ArrayList;
import java.util.List;

import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider.Text;
import com.sun.xml.bind.CycleRecoverable;


/*
	{
		"text": "1. Pre Lunch (120 min)",
		"expanded": true,
		"classes": "important",
		"children":
		[
			{
				"text": "1.1 The State of the Powerdome (30 min)"
			},
		 	{
				"text": "1.2 The Future of jQuery (30 min)"
			},
		 	{
				"text": "1.2 jQuery UI - A step to richnessy (60 min)"
			}
		]
	}
 */
public class Node implements CycleRecoverable {

	public boolean expanded = true;
	public String classes = "important";
	private List<Node> children = new ArrayList<Node>();	
	private String text;
	private String id;
	private boolean hasChildren = false;

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> nodes) {
		this.children = nodes;
	}
	
	public void addNodes(Node ...thechildren) {
		for (Node t : thechildren) {
			children.add(t);	
		}
	}

	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}



	/*
	public static class Data {
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public List<Attribute> getAttributes() {
			return attributes;
		}
		public void setAttributes(List<Attribute> attributes) {
			this.attributes = attributes;
		}
		public List<Data> getChildren() {
			return children;
		}
		public void setChildren(List<Data> children) {
			this.children = children;
		}
		public void addAttribute(Attribute a) {
			attributes.add(a);
		}
		public void addChild(Data ...thechildren) {
			for (Data d : thechildren) {
				children.add(d);	
			}
		}
		private String title;
		private List<Attribute> attributes = new ArrayList<Attribute>();
		private List<Data> children = new ArrayList<Data>(0);
		
	}
	public static class Attribute {
		public String getHref() {
			return href;
		}
		public void setHref(String href) {
			this.href = href;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		private String href;
		private String id;
		
	}
	
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}

	private Data data;
	private State state;

	*/
	
	@Override
	public Object onCycleDetected(Context arg0) {
		return null;
	}
	
}
