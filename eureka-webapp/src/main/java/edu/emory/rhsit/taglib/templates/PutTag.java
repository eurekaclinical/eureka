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
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * PutTag from java documentation examples
 *
 */
public class PutTag extends TagSupport {
	private String name, content, direct = "false";

	public void setName(String s) {
		name = s;
	}

	public void setContent(String s) {
		content = s;
	}

	public void setDirect(String s) {
		direct = s;
	}

	public int doStartTag() throws JspException {
		InsertTag parent =
			(InsertTag) findAncestorWithClass
				(this, edu.emory.rhsit.taglib.templates.InsertTag.class);
		if (parent == null)
			throw new JspException("PutTag.doStartTag(): "
					+ "No InsertTag ancestor");

		Stack template_stack = parent.getStack();

		if (template_stack == null)
			throw new JspException("PutTag: no template stack");

		Hashtable params = (Hashtable) template_stack.peek();

		if (params == null)
			throw new JspException("PutTag: no hashtable");

		params.put(name, new PageParameter(content, direct));

		return SKIP_BODY;
	}

	public void release() {
		name = content = direct = null;
	}

}
