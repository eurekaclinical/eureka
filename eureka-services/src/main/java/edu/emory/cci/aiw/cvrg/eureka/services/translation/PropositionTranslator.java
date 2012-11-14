package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;

/**
 * Translates a UI data element into a proposition as understood by the service
 * layer data model.
 * 
 * @param <E>
 *            The data element type to translate from, a subclass of {@link DataElement}..
 * @param <P>
 *            The proposition type to translate to, an implementation of {@link Proposition}.
 */
interface PropositionTranslator<E extends DataElement, P extends Proposition> {

	/**
	 * Translates the given data element to a proposition understood by the
	 * services layer data model.
	 * 
	 * @param element
	 *            the data element to translate from
	 * @return A {@link Proposition} equivalent to the data element.
	 */
	P translate(E element);
}
