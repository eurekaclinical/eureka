/*
 * #%L
 * Eureka Services
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
/**
 * This package implements conversion of Eureka! derived data elements to
 * Protempa proposition definitions.
 * 
 * The following conversions take place:
 * <dl>
 * <dt>Value threshold data elements</dt>
 * <dd>If single threshold, convert to a low-level abstraction definition with
 * id <code>data element key + "_PRIMARY"</code>. If
 * multiple thresholds, convert to a compound low-level abstraction definition
 * with id <code>data element key + "_PRIMARY"</code> that is abstracted from
 * low-level abstraction definitions with id 
 * <code>data element key + "_SUB" + the proposition id of the thresholded
 * parameter</code>. Intervals satisfying the value threshold(s) are given the 
 * value <code>data element key + "_VALUE"</code>. Intervals not satisfying the
 * threshold(s) are given the value <code>data element key + "_VALUE_COMP"</code>.
 * Before sending to the backend layer, all such abstraction definitions are
 * wrapped in a high-level abstraction definition with id 
 * <code>data element key + "_WRAPPER" that is abstracted from intervals with
 * value <code>data element key + "_VALUE"</code>.
 * 
 * <dt>Sequence data elements</dt>
 * <dd>Convert to a high-level abstraction definition with id
 * <code>data element key + "_PRIMARY"</code>.
 * </dd>
 * 
 * <dt>Category data elements</dt>
 * <dd>Convert to the same type of proposition definition as the children and 
 * with id <code>data element key + "_PRIMARY"</code>.
 * </dd>
 * 
 * <dt>Frequency data elements</dt>
 * <dd>
 * <ul>
 * <li>If abstracted from a value threshold data element
 * <ul>
 * <li>If the value of the <code>consecutive</code> field has value 
 * <code>true</code>
 * <ul>
 * <li>If the frequency type is <code>at least</code>, then create a
 * compound low-level abstraction definition with id 
 * <code>data element key + "_SUB" that is wrapped by a high-level abstraction
 * definition with id <code>data element key + "_PRIMARY"</code>.</li>
 * <li>If the frequency type is <code>first</code>, then create a
 * compound low-level abstraction definition with id
 * <code>data element key + "_SUB" that is wrapped by a slice definition with
 * id <code>data element key + "_SUBSUB"</code> for the first interval with
 * value <code>data element key + "_VALUE"</code>. The slice definition is
 * in turn wrapped by a high-level abstraction definition with id
 * <code>data element key + "_PRIMARY".</code></li>
 * </ul>
 * </li>
 * <li>Else
 * <ul>
 * <li>If the frequency type is <code>at least</code>, then create a high-
 * level abstraction definition with id 
 * <code>data element key + "_PRIMARY"</code> with <code>n</code> temporal 
 * extended parameter definitions for the value threshold, where
 * <code>n</code> is the specified count.</li>
 * <li>If the frequency type is <code>first</code>, then create a slice
 * definition with id <code>data element key + "_SUB"</code> that is wrapped
 * by a high-level abstraction definition with id 
 * <code>data element key + "_PRIMARY"</code>.</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * <li>If not abstracted from a value threshold data element
 * <ul>
 * <li>If the frequency type is <code>at least</code>, then create a high-
 * level abstraction definition with id 
 * <code>data element key + "_PRIMARY"</code> with <code>n</code> temporal 
 * extended parameter definitions for the value threshold, where
 * <code>n</code> is the specified count.</li>
 * <li>If the frequency type is <code>first</code>, then create a slice
 * definition with id <code>data element key + "_SUB"</code> that is wrapped
 * by a high-level abstraction definition with id 
 * <code>data element key + "_PRIMARY"</code>.</li>
 * </ul>
 * </li>
 * </ul>
 * </dl>
 * 
 * 
 */
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;
