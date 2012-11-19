/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;

/**
 * Translates a UI data element into a proposition as understood by the service
 * layer data model.
 * 
 * @param <E>
 *            The data element type to translate from, a subclass of
 *            {@link DataElement}..
 * @param <P>
 *            The proposition type to translate to, an implementation of
 *            {@link Proposition}.
 */
public interface PropositionTranslator<E extends DataElement, P extends Proposition> {

	/**
	 * Translates the given data element to a proposition understood by the
	 * services layer data model. The inverse of {@link #translateFromProposition(Proposition)}
	 * .
	 * 
	 * @param element
	 *            the data element to translate from
	 * @return A {@link Proposition} equivalent to the data element.
	 */
	P translateFromElement(E element);

	/**
	 * Translates the given proposition entity into a data element understood by
	 * the webapp layer. The inverse of {@link #translateFromElement(DataElement)}.
	 * 
	 * @param proposition
	 *            the proposition to translate from
	 * @return A {@link DataElement} equivalent to the proposition.
	 */
	E translateFromProposition(P proposition);
}
