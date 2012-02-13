package edu.emory.cci.aiw.cvrg.eureka.servlet.worker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletWorker {
	
	void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException;
}
