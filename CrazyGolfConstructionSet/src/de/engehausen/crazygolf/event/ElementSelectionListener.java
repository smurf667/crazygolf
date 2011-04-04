package de.engehausen.crazygolf.event;

import de.engehausen.crazygolf.model.Element;

/**
 * Element selection listener.
 */
public interface ElementSelectionListener {

	/**
	 * Invoked when an element is selected.
	 * @param element the selected element, never <code>null</code>
	 */
	void elementSelected(final Element element);

}
