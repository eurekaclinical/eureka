package edu.emory.cci.aiw.cvrg.eureka.common.comm;

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

/**
 *
 * @author Andrew Post
 */
public final class OAuthUser extends User {
	private String providerUsername;
	private Long oauthProvider;

	public String getProviderUsername() {
		return providerUsername;
	}

	public void setProviderUsername(String providerUsername) {
		this.providerUsername = providerUsername;
	}

	public Long getOAuthProvider() {
		return oauthProvider;
	}

	public void setOAuthProvider(Long oauthProvider) {
		this.oauthProvider = oauthProvider;
	}
	
	@Override
	public String[] validate() {
		String[] results = super.validate();
		
		if (this.oauthProvider == null) {
			String[] newResults = new String[results.length + 1];
			System.arraycopy(results, 0, newResults, 0, results.length);
			newResults[results.length] = "OAuth provider unspecified";
			results = newResults;
		}
		return results;
	}
	
	@Override
	public void accept(UserVisitor userVisitor) {
		userVisitor.visit(this);
	}
	
}
