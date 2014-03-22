package edu.emory.cci.aiw.cvrg.eureka.webapp.taglib;

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

import edu.emory.cci.aiw.cvrg.eureka.common.props.SupportUri;
import edu.emory.cci.aiw.cvrg.eureka.webapp.config.WebappProperties;
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
	
	private static String supportUri() {
		SupportUri supportUri = properties.getSupportUri();
		if (supportUri == null) {
			return null;
		} else if (supportUri.isHyperlinkable()) {
			return "<a href=\"" + supportUri + "\">" + supportUri.getName() + "</a>";
		} else {
			return supportUri.toString();
		}
		
	}
	
	public static boolean isSupportUriDefined() {
		return supportUri() != null;
	}
}
