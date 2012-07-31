package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import edu.emory.cci.aiw.cvrg.eureka.servlet.Node;


public class JsonServlet extends HttpServlet {

//	private Data createData(String href, String title, String id) {
//		Attribute a = new Attribute();
//		a.setHref(href);
//		a.setId(id);
//		Data d = new Data();
//		d.setTitle(title);
//		d.addAttribute(a);
//		
//		return d;
//
//		
//	}
	

	private Data createData(String data, String id) {
		Data d = new Data();
		d.setData(data);
		d.setKeyVal("id", id);
		
		return d;
	}
	
	private Node createNode(String text, String id, boolean isExpanded) {
		Node t = new Node();
		t.setText(text);
		t.setId(id);
		t.setExpanded(isExpanded);
		return t;
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String id = req.getParameter("id");
		
		if (id.equals("root")) {
			
			Data d1 = createData("Child 1","1");
			Data d2 = createData("Child 2", "2");
			Data d3 = createData("Sub Child 1", "3");
			Data d4 = createData("Sub Child 2", "4");
			List<Data> l = new ArrayList<Data>();
			d2.addNodes(d3, d4);
			l.add(d1); l.add(d2);
			ObjectMapper mapper = new ObjectMapper();
			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			mapper.writeValue(out, l);
		} else {
			Data d1 = createData("Lazy Loaded Child","11");
			List<Data> l = new ArrayList<Data>();
			l.add(d1); 
			ObjectMapper mapper = new ObjectMapper();
			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			mapper.writeValue(out, l);
			
		}
		
		/*
		System.out.println("doGet");
		String root = req.getParameter("root");
		List<Node> l = new ArrayList<Node>();

		if (root.equals("source")) {
			
			
			Node t1 = createNode("Child 1", "2", false);
			Node t2 = createNode("Child 2", "3", false);
			Node t2child1 = createNode("Sub Child 1", "4", false);
			Node t2child2 = createNode("Sub Child 2", "5", false);
			t2.setHasChildren(true);
			t2.addNodes(t2child1, t2child2);

			l.add(t1);	l.add(t2);		
		} else {

			Node t1 = createNode("Sub Sub Child 1", "6", false);
			Node t2 = createNode("Sub Sub Child 2", "7", false);
			Node t3 = createNode("Sub Sub Child 3", "8", false);
			Node t4 = createNode("Sub Sub Child 4", "7", false);
			
			l.add(t1); l.add(t2); l.add(t3); l.add(t4);			
			
		}
		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
//		String s="[{ 'text': '1. Pre Lunch (120 min)','expanded': true,'classes': 'important',"+
//		"'children': [ {'text': '1.1 The State of the Powerdome (30 min)'},"+
//		 "{'text': '1.2 The Future of jQuery (30 min)'},"+
//		 "{'text': '1.2 jQuery UI - A step to richnessy (60 min)'}" +
//		 "]}]";
		mapper.writeValue(out, l);
		//out.write(s);
		 
		 */
	}
}
