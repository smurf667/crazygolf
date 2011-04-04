package de.engehausen.crazygolf.event;

import java.awt.image.BufferedImage;

/**
 * Listener for zoom events.
 */
public interface ZoomListener {

	/**
	 * The zoom position changes to the given coordinate.
	 * @param x the x-position
	 * @param y the y-position
	 */
	void zoomPositionChanges(int x, int y);

	/**
	 * The zoom contents change to the given image.
	 * @param image the new zoom contents
	 */
	void zoomContentsChanges(BufferedImage image);

}
