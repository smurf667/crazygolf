package de.engehausen.crazygolf.event;

import de.engehausen.crazygolf.model.Element;

/**
 * Handle the placing of an element.
 */
public interface PlaceActionHandler {

	/**
	 * Sets the element to be place.
	 * @param element the element.
	 */
	void setPlacingElement(Element element);

}
