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
 * GetTag from java documentation examples
 *
 */
public class GetTag extends TagSupport {
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public int doStartTag() throws JspException {
		Stack stack = (Stack) pageContext.getAttribute("template-stack",
				PageContext.REQUEST_SCOPE);

		if (stack == null)
			throw new JspException("GetTag.doStartTag(): " + "NO STACK");

		Hashtable params = (Hashtable) stack.peek();

		if (params == null)
			throw new JspException("GetTag.doStartTag(): " + "NO HASHTABLE");

		PageParameter param = (PageParameter) params.get(name);

		if (param != null) {
			String content = param.getContent();
			if (param.isDirect()) {
				try {
					pageContext.getOut().print(content);
				} catch (java.io.IOException ex) {
					throw new JspException(ex.getMessage());
				}
			} else {
				try {
					pageContext.getOut().flush();
					pageContext.include(content);
				} catch (Exception ex) {
					throw new JspException(ex.getMessage());
				}
			}
		}
		return SKIP_BODY;
	}

	public void release() {
		name = null;
	}
}
