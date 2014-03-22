package edu.emory.cci.aiw.cvrg.eureka.common.props;

/*
 * #%L
 * Eureka Common
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
import java.net.URI;
import org.arp.javautil.arrays.Arrays;

/**
 *
 * @author Andrew Post
 */
public final class SupportUri {

	private static final String[] schemesToIncludeInName = {
		"http",
		"https"
	};

	private static final String[] hyperlinkableSchemas = {
		"http",
		"https",
		"mailto"
	};

	private final URI uri;
	private final String name;

	SupportUri(URI uri, String name) {
		assert uri != null : "uri cannot be null";
		this.uri = uri;
		this.name = name;
	}

	public URI getUri() {
		return this.uri;
	}

	public boolean isHyperlinkable() {
		return Arrays.contains(hyperlinkableSchemas, uri.getScheme());
	}

	@Override
	public String toString() {
		if (!isHyperlinkable()) {
			return uriWithoutScheme();
		} else {
			return this.uri.toString();
		}
	}

	public String getName() {
		if (this.name != null) {
			return this.name;
		} else if (Arrays.contains(schemesToIncludeInName, this.uri.getScheme())) {
			return this.uri.toString();
		} else {
			return uriWithoutScheme();
		}
	}

	private String uriWithoutScheme() {
		return this.uri.toString().split(":", 2)[1];
	}

}
