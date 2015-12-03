/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
package edu.emory.rhsit.taglib.templates;

import java.util.Hashtable;
import java.util.Stack;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * InsertTag from java documentation examples
 *
 */
public class InsertTag extends TagSupport {
	private String template;
	private Stack stack;

	public void setTemplate(String template) {
		this.template = template;
	}
	public int doStartTag() throws JspException {
		stack = getStack();
		stack.push(new Hashtable());
		return EVAL_BODY_INCLUDE;
	}
	public int doEndTag() throws JspException {
		try {
			pageContext.include(template);
		}
		catch(Exception ex) { // IOException or ServletException
			throw new JspException(ex.getMessage());
		}
		stack.pop();
		return EVAL_PAGE;
	}

	public Stack getStack() {
		Stack s = (Stack)pageContext.getAttribute(
								"template-stack",
								PageContext.REQUEST_SCOPE);
		if(s == null) {
			s = new Stack();
			pageContext.setAttribute("template-stack", s,
								PageContext.REQUEST_SCOPE);
		}
		return s;
	}
}
