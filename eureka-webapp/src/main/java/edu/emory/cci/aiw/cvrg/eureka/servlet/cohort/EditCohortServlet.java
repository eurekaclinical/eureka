package edu.emory.cci.aiw.cvrg.eureka.servlet.cohort;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2014 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
import com.google.inject.Inject;
import org.eurekaclinical.eureka.client.comm.BinaryOperator;
import org.eurekaclinical.eureka.client.comm.Cohort;
import org.eurekaclinical.eureka.client.comm.CohortDestination;
import org.eurekaclinical.eureka.client.comm.Phenotype;
import org.eurekaclinical.eureka.client.comm.PhenotypeField;
import org.eurekaclinical.eureka.client.comm.Literal;
import org.eurekaclinical.eureka.client.comm.Node;
import org.eurekaclinical.eureka.client.comm.NodeVisitor;
import org.eurekaclinical.eureka.client.comm.UnaryOperator;
import org.eurekaclinical.common.comm.clients.ClientException;
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
				req.setAttribute("destId", destination.getId());
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

	private class EditCohortNodeVisitor implements NodeVisitor {

		List<Literal> phenotypes = new ArrayList<>();

		@Override
		public void visit(Literal literal) {
			if (literal.getStart() != null || literal.getFinish() != null) {
				throw new UnsupportedOperationException("Literal start and finish not supported yet");
			}
			this.phenotypes.add(literal);
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
			binaryOperator.getLeftNode().accept(v);
			this.phenotypes.addAll(v.getPhenotypesInt());
			v = new EditCohortNodeVisitor();
			binaryOperator.getRightNode().accept(v);
			this.phenotypes.addAll(v.getPhenotypesInt());
		}

		private List<Literal> getPhenotypesInt() {
			return this.phenotypes;
		}

		List<PhenotypeField> getPhenotypes() throws ClientException {
			List<PhenotypeField> result = new ArrayList<>();
			for (Literal literal : phenotypes) {
				Phenotype userPhenotype;
				if (literal.getName().startsWith("USER:")) {
					userPhenotype = servicesClient.getUserPhenotype(literal.getName(), true);
				} else {
					userPhenotype = servicesClient.getSystemPhenotype(literal.getName(), true);
				}
				result.add(new PhenotypeField(userPhenotype));
			}
			return result;
		}
		
	}
}
