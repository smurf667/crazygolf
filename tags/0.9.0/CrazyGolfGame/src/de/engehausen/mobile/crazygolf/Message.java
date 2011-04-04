package de.engehausen.mobile.crazygolf;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 * Displays a text for a number of frames.
 */
public class Message {
	
	private final Font font;
	private int count;
	private final int y;
	private final String text;
	private boolean initial;

	/**
	 * Creates the message for the given text using the specified font,
	 * position and lifetime.
	 * @param aFont the font to use, must not be <code>null</code>.
	 * @param aText the text to display, must not be <code>null</code>.
	 * @param pos the position from the top of the screen.
	 * @param frameCount the number of frames the text should show.
	 */
	public Message(final Font aFont, final String aText, final int pos, final int frameCount) {
		font = aFont;
		text = aText;
		y = pos;
		count = frameCount;
		initial = true;
	}

	/**
	 * Indicates if the message is currently showing.
	 * @return <code>true</code> if the message is showing, <code>false</code> otherwise
	 */
	public boolean isShowing() {
		return count>0;
	}

	/**
	 * Paints the message into the given graphics contents.
	 * @param g the graphics context, must not be <code>null</code>.
	 * @param background the background (used for restoring the pixel overlayed by the message), must not be <code>null</code>.
	 */
	public void paint(final Graphics g, final Image background) {
		if (--count > 0) {
			if (initial) {
				// restore background first time round
				hide(g, background);
				initial = false;
			}
			font.drawStringCentered(text, g, y, Font.ORIENTATION_HORIZONTAL);
		} else {
			hide(g, background);
		}
	}

	/**
	 * Removes the message from the graphics and paints the pixels
	 * of the background in its place.
	 * @param g the graphics context, must not be <code>null</code>.
	 * @param background the background (used for restoring the pixel overlayed by the message), must not be <code>null</code>.
	 */
	protected void hide(final Graphics g, final Image background) {
		g.drawRegion(background, 0, y, font.getMaxX(), font.getCharHeight(), Sprite.TRANS_NONE, 0, y, Graphics.TOP|Graphics.LEFT);
	}

}
