package de.engehausen.mobile.crazygolf;

import javax.microedition.lcdui.Graphics;

/**
 * Defines a renderer that can paint frames.
 */
public interface FrameRenderer extends ClickHandler {
	
	/**
	 * Paints a frame.
	 * @param graphics the graphics to operate on, must not be <code>null</code>
	 * @return the area that was modified, or <code>null</code> (meaning the complete
	 * graphics was changed)
	 */
	Area paint(Graphics graphics);
	
	/**
	 * Handle a swipe.
	 * @param sx starting position
	 * @param sy starting position
	 * @param ex ending position
	 * @param ey ending position
	 * @param time time difference
	 * @param distSquare the squared distance of the swipe
	 */
	void handleSwipe(int sx, int sy, int ex, int ey, long time, double distSquare);

	/**
	 * Reset the renderer back to initial painting
	 * state (next {@link #paint(Graphics)} will be a full
	 * paint.
	 */
	void reset();

	/**
	 * Indicates if a repaint is required. 
	 * @return <code>true</code> if a paint is required - in this case
	 * a call to {@link #paint(Graphics)} clears the flag.
	 */
	boolean needsRepaint();

}
