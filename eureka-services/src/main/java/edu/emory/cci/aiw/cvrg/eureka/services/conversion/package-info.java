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
 * There is a naming convention for the generated proposition ids and other
 * identifiers for Protempa objects. This convention avoids id clashes, which
 * is challenging given that a single derived data element may be converted 
 * into a "primary" proposition definition that is abstracted from zero or
 * "intermediate" proposition definitions, which in turn are abstracted from
 * the proposition definitions corresponding to the data elements from which 
 * the Eureka! data element is derived. The convention is as follows:
 * * "primary" proposition definition: <code>data element key + _PRIMARY</code>.
 * 
 */
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;
