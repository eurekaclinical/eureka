package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import edu.emory.cci.aiw.cvrg.eureka.common.authentication.LoginType;

/*
 * #%L
 * Eureka Common
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

/**
 *
 * @author Andrew Post
 */
public final class LocalUserRequest extends UserRequest {

	/**
	 * The user's password.
	 */
	private String password;
	/**
	 * The user's password verification.
	 */
	private String verifyPassword;

	public LocalUserRequest() {
		setLoginType(LoginType.INTERNAL);
	}
	
	/**
	 * Get the user's password.
	 *
	 * @return The user's password.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Set the user's password.
	 *
	 * @param inPassword The user's password.
	 */
	public void setPassword(String inPassword) {
		this.password = inPassword;
	}

	/**
	 * Get the password verification.
	 *
	 * @return The password verification.
	 */
	public String getVerifyPassword() {
		return this.verifyPassword;
	}

	/**
	 * Set the password verification.
	 *
	 * @param inVerifyPassword The password verification.
	 */
	public void setVerifyPassword(String inVerifyPassword) {
		this.verifyPassword = inVerifyPassword;
	}

	@Override
	public String[] validate() {
		String[] results = super.validate();
		
		PasswordValidator passwordValidator = new PasswordValidator();
		if (!passwordValidator.validate(this.password)) {
			String[] newResults = new String[results.length + 1];
			System.arraycopy(results, 0, newResults, 0, results.length);
			newResults[results.length] = "Password must be at least 8 characters long.";
			results = newResults;
		} else if ((this.verifyPassword == null) || 
				(!this.password.equals(this.verifyPassword))) {
			String[] newResults = new String[results.length + 1];
			System.arraycopy(results, 0, newResults, 0, results.length);
			newResults[results.length] = "Mismatched passwords";
			results = newResults;
		}
		return results;
	}

	@Override
	public void accept(UserRequestVisitor userRequestVisitor) {
		userRequestVisitor.visit(this);
	}
	
	@Override
	public edu.emory.cci.aiw.cvrg.eureka.common.authentication.AuthenticationMethod authenticationMethod() {
		return edu.emory.cci.aiw.cvrg.eureka.common.authentication.AuthenticationMethod.LOCAL;
	}
}
