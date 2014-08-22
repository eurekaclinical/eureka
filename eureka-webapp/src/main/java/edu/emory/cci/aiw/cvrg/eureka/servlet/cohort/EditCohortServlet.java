package edu.emory.cci.aiw.cvrg.eureka.servlet.cohort;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2014 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.BinaryOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Cohort;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.CohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Literal;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Node;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.NodeVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UnaryOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by akshatha on 7/23/14.
 */
public class EditCohortServlet extends HttpServlet {
	private final ServicesClient servicesClient;

	@Inject
	public EditCohortServlet(ServicesClient servicesClient) {
		this.servicesClient = servicesClient;
	}
	
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cohortName = req.getParameter("key");
		if (cohortName != null) {
			try {
				CohortDestination destination = (CohortDestination) this.servicesClient.getDestination(cohortName);
				req.setAttribute("name", destination.getName());
				req.setAttribute("description", destination.getDescription());
				Cohort cohort = destination.getCohort();
				Node node = cohort.getNode();
				EditCohortNodeVisitor v = new EditCohortNodeVisitor();
				node.accept(v);
				req.setAttribute("phenotypes", v.getPhenotypes());
			} catch (ClientException ex) {
				throw new ServletException("Error setting up cohort editor", ex);
			}
		}
		req.getRequestDispatcher("/protected/cohort_editor.jsp").forward(
				req, resp);
	}
	
	private static class EditCohortNodeVisitor implements NodeVisitor {
		List<String> phenotypes = new ArrayList<>();

		@Override
		public void visit(Literal literal) {
			if (literal.getStart() != null || literal.getFinish() != null) {
				throw new UnsupportedOperationException("Literal start and finish not supported yet");
			}
			this.phenotypes.add(literal.getName());
		}

		@Override
		public void visit(UnaryOperator unaryOperator) {
			throw new UnsupportedOperationException("NOT is not supported yet");
		}

		@Override
		public void visit(BinaryOperator binaryOperator) {
			if (binaryOperator.getOp() == BinaryOperator.Op.AND) {
				throw new UnsupportedOperationException("AND is not supported yet");
			}
			EditCohortNodeVisitor v = new EditCohortNodeVisitor();
			binaryOperator.accept(v);
			this.phenotypes.addAll(v.getPhenotypes());
		}
		
		List<String> getPhenotypes() {
			return this.phenotypes;
		}
		
	}
}
