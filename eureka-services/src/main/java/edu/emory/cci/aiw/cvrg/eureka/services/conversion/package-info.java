/*
 * #%L
 * Eureka Services
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
/**
 * This package implements conversion of Eureka! derived phenotypes to
 * Protempa proposition definitions.
 * 
 * The following conversions take place:
 * <dl>
 * <dt>Value threshold phenotypes</dt>
 * <dd>If single threshold, convert to a low-level abstraction definition with
 * id <code>phenotype key + "_PRIMARY"</code>. If
 * multiple thresholds, convert to a compound low-level abstraction definition
 * with id <code>phenotype key + "_PRIMARY"</code> that is abstracted from
 * low-level abstraction definitions with id 
 * <code>phenotype key + "_SUB" + the proposition id of the thresholded
 * parameter</code>. Intervals satisfying the value threshold(s) are given the 
 * value <code>phenotype key + "_VALUE"</code>. Intervals not satisfying the
 * threshold(s) are given the value <code>phenotype key + "_VALUE_COMP"</code>.
 * Before sending to the backend layer, all such abstraction definitions are
 * wrapped in a high-level abstraction definition with id 
 * <code>phenotype key + "_WRAPPER" that is abstracted from intervals with
 * value <code>phenotype key + "_VALUE"</code>.
 * 
 * <dt>Sequence phenotypes</dt>
 * <dd>Convert to a high-level abstraction definition with id
 * <code>phenotype key + "_PRIMARY"</code>.
 * </dd>
 * 
 * <dt>Category phenotypes</dt>
 * <dd>Convert to the same type of proposition definition as the children and 
 * with id <code>phenotype key + "_PRIMARY"</code>.
 * </dd>
 * 
 * <dt>Frequency phenotypes</dt>
 * <dd>
 * <ul>
 * <li>If abstracted from a value threshold phenotype
 * <ul>
 * <li>If the value of the <code>consecutive</code> field has value 
 * <code>true</code>
 * <ul>
 * <li>If the frequency type is <code>at least</code>, then create a
 * compound low-level abstraction definition with id 
 * <code>phenotype key + "_SUB" that is wrapped by a high-level abstraction
 * definition with id <code>phenotype key + "_PRIMARY"</code>.</li>
 * <li>If the frequency type is <code>first</code>, then create a
 * compound low-level abstraction definition with id
 * <code>phenotype key + "_SUB" that is wrapped by a slice definition with
 * id <code>phenotype key + "_SUBSUB"</code> for the first interval with
 * value <code>phenotype key + "_VALUE"</code>. The slice definition is
 * in turn wrapped by a high-level abstraction definition with id
 * <code>phenotype key + "_PRIMARY".</code></li>
 * </ul>
 * </li>
 * <li>Else
 * <ul>
 * <li>If the frequency type is <code>at least</code>, then create a high-
 * level abstraction definition with id 
 * <code>phenotype key + "_PRIMARY"</code> with <code>n</code> temporal 
 * extended parameter definitions for the value threshold, where
 * <code>n</code> is the specified count.</li>
 * <li>If the frequency type is <code>first</code>, then create a slice
 * definition with id <code>phenotype key + "_SUB"</code> that is wrapped
 * by a high-level abstraction definition with id 
 * <code>phenotype key + "_PRIMARY"</code>.</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * <li>If not abstracted from a value threshold phenotype
 * <ul>
 * <li>If the frequency type is <code>at least</code>, then create a high-
 * level abstraction definition with id 
 * <code>phenotype key + "_PRIMARY"</code> with <code>n</code> temporal 
 * extended parameter definitions for the value threshold, where
 * <code>n</code> is the specified count.</li>
 * <li>If the frequency type is <code>first</code>, then create a slice
 * definition with id <code>phenotype key + "_SUB"</code> that is wrapped
 * by a high-level abstraction definition with id 
 * <code>phenotype key + "_PRIMARY"</code>.</li>
 * </ul>
 * </li>
 * </ul>
 * </dl>
 * 
 * 
 */
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;
