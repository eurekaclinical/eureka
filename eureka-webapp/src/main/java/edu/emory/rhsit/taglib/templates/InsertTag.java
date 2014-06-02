/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
