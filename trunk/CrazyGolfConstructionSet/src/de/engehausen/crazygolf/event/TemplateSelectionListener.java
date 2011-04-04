package de.engehausen.crazygolf.event;

import de.engehausen.mobile.crazygolf.Element;

/**
 * Selection event for a "template". A "template" is an array
 * which holds the different modes of an element (i.e. usually
 * three - normal, down and up).
 */
public interface TemplateSelectionListener {

	/**
	 * An "template" with the given element modes has been selected.
	 * @param templates the templates
	 */
	void templateSelected(final Element[] templates);

}
