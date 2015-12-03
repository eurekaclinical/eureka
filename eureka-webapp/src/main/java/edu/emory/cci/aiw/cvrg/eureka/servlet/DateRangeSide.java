package edu.emory.cci.aiw.cvrg.eureka.servlet;

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

import org.protempa.proposition.interval.Interval;
import org.protempa.proposition.interval.Interval.Side;

/**
 * Wrapper around <code>org.protempa.proposition.interval.Interval.Side</code>
 * objects for use in JSTL.
 * 
 * @author Andrew Post
 */
public class DateRangeSide {
	
	public static DateRangeSide[] values() {
		Side[] sides = Side.values();
		DateRangeSide[] result = new DateRangeSide[sides.length];
		for (int i = 0; i < sides.length; i++) {
			result[i] = new DateRangeSide(sides[i]);
		}
		return result;
	}
	
	private final Side side;

	/**
	 * Create an instance that wraps the specified
	 * <code>org.protempa.proposition.interval.Interval.Side</code>.
	 * 
	 * @param side the specified <code>org.protempa.proposition.interval.Interval.Side</code>.
	 */
	public DateRangeSide(Interval.Side side) {
		this.side = side;
	}
	
	/**
	 * Returns the wrapped <code>org.protempa.proposition.interval.Interval.Side</code>'s name.
	 * 
	 * @return the wrapped <code>org.protempa.proposition.interval.Interval.Side</code>'s name.
	 */
	public String getId() {
		return this.side.name();
	}
	
	/**
	 * Delegates to the wrapped <code>org.protempa.proposition.interval.Interval.Side</code>'s
	 * <code>toString</code> method.
	 * 
	 * @return the value returned by the wrapped <code>org.protempa.proposition.interval.Interval.Side</code>'s
	 * <code>toString</code> method.
	 */
	public String getDisplayName() {
		return this.side.toString();
	}
	
}
