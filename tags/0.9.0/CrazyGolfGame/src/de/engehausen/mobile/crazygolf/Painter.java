package de.engehausen.mobile.crazygolf;

/**
 * Defines a repaintable object; the meaning of the
 * mode is undefined.
 */
public interface Painter {

	/**
	 * Repaint the object.
	 * @param mode the mode to repaint the object in.
	 */
	void repaint(int mode);
	
}
