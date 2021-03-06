package de.engehausen.crazygolf.event;

import de.engehausen.crazygolf.model.Element;

/**
 * Listener for element modifications.
 */
public interface ElementModificationListener {

	/**
	 * Triggered when an element is modified.
	 * @param element the modified element, never <code>null</code>.
	 */
	void elementModified(final Element element);
	
}
