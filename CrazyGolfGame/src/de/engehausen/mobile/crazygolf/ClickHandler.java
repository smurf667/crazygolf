package de.engehausen.mobile.crazygolf;

/**
 * Handles a click (a tap) on the touch screen.
 */
public interface ClickHandler {

	/**
	 * Handle a click.
	 * @param x the x position of the click
	 * @param y the y position of the click
	 * @return <code>true</code> if the click was handled, <code>false</code> otherwise.
	 */
	boolean handleClick(int x, int y);
	
}
