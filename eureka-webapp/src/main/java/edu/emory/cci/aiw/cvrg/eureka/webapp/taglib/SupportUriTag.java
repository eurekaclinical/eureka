package edu.emory.cci.aiw.cvrg.eureka.webapp.taglib;

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
import edu.emory.cci.aiw.cvrg.eureka.common.props.SupportUri;
import edu.emory.cci.aiw.cvrg.eureka.webapp.config.WebappProperties;
import java.beans.IntrospectionException;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.jboss.logging.Logger;

/**
 *
 * @author Andrew Post
 */
public class SupportUriTag extends TagSupport {

	private static final WebappProperties properties = new WebappProperties();
	private static final Logger LOGGER = Logger.getLogger(SupportUriTag.class);

	private String hyperlinkClass;

	public SupportUriTag() throws IntrospectionException {
		this.hyperlinkClass = "";
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(supportUri());
		} catch (IOException ex) {
			LOGGER.error("Error writing support URI", ex);
			throw new JspException("Error writing support URI", ex);
		}
		return SKIP_BODY;
	}

	public String getHyperlinkClass() {
		return hyperlinkClass;
	}

	public void setHyperlinkClass(String hyperlinkClass) {
		if (hyperlinkClass == null) {
			this.hyperlinkClass = "";
		} else {
			this.hyperlinkClass = hyperlinkClass;
		}
	}

	private String supportUri() {
		SupportUri supportUri = properties.getSupportUri();
		if (supportUri == null) {
			return null;
		} else if (supportUri.isHyperlinkable()) {
			return "<a href=\"" + supportUri + "\" class=\"" + this.hyperlinkClass + "\">" + supportUri.getName() + "</a>";
		} else {
			return supportUri.toString();
		}
	}

	public static boolean isSupportUriDefined() {
		return properties.getSupportUri() != null;
	}
}
