/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * 
 * @author sagrava
 * 
 * Extension of Simple Tag Library from sun examples.  Allows capability to
 * specify content in between tags rather than hardcoding in an attribute 
 * from the PutTag.
 * 
 * Usage: <pre>{@code <template:content name="some name">
 * 			content goes here
 * 		  </template:content>}</pre>
 * 
 */
public class ContentTag extends BodyTagSupport {
	private String name;

	public void setName(String s) {
		name = s;
	}



	@Override
	public int doAfterBody() throws JspException {
		BodyContent body = getBodyContent();
		if (body == null)
			throw new JspException("ContentTag.doStartTag(): "
					+ "No JSP body present under template:content");

		String htmlBody = body.getString();
		InsertTag parent =
			(InsertTag) findAncestorWithClass
				(this, edu.emory.rhsit.taglib.templates.InsertTag.class);
		if (parent == null)
			throw new JspException("ContentTag.doStartTag(): "
					+ "No InsertTag ancestor");

		Stack template_stack = parent.getStack();

		if (template_stack == null)
			throw new JspException("ContentTag: no template stack");

		Hashtable params = (Hashtable) template_stack.peek();

		if (params == null)
			throw new JspException("ContentTag: no hashtable");

		params.put(name, new PageParameter(htmlBody, "true"));


		return (SKIP_BODY);
	}


}
