package de.engehausen.mobile.crazygolf;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 * Font for displaying text horizontally
 * or vertically.
 */
public class Font {
	
	/** horizontal orientation */
	public static final int ORIENTATION_HORIZONTAL = Sprite.TRANS_NONE,
	/** vertical orientation */
	                        ORIENTATION_VERTICAL = Sprite.TRANS_ROT90;
	/** left alignment */
	public static final int ALIGN_LEFT = 0,
	/** right alignment */
	                        ALIGN_RIGHT = 1;
	
	private static final int SPACE_SIZE = 6;
	
	private final int widths[];
	private final int position[];
	private final int height;
	private final Image fontImage;
	private final int mx, my;
		
	/**
	 * Creates the font from the given graphics file (name).
	 * @param name the file name of the font graphics data, must not be <code>null</code>.
	 * @param maxx the maximum x value
	 * @param maxy the maximum y value
	 * @throws IOException in case of error
	 */
	public Font(final String name, final int maxx, final int maxy) throws IOException {
		mx = maxx;
		my = maxy;
		final InputStream desc = getClass().getResourceAsStream("/"+name+".txt");
		final StringBuffer sb = new StringBuffer(64);
		int c, max = 0;		
		while ( (c = desc.read()) != '\n' ) {
			sb.append((char) c);
			if (c > max) {
				max = c;
			}
		}
		widths = new int[max+1];
		position = new int[max+1];
		parse(desc, sb);
		desc.close();
		fontImage = Image.createImage("/"+name+".png");
		if (fontImage == null) {
			throw new IllegalStateException("no font image available");
		}
		height = fontImage.getHeight();
	}

	/**
	 * Paints the given text to the graphics object.
	 * @param text the text to paint
	 * @param graphics the graphics object to paint on
	 * @param x the x position where the text starts
	 * @param y the y position where the text starts
	 * @param orientation the orientation of the text ({@link #ORIENTATION_HORIZONTAL}
	 * or {@link #ORIENTATION_VERTICAL}).
	 */
	public void drawString(final String text, final Graphics graphics, final int x, final int y, final int orientation) {
		int cx = x, cy = y;
		final int max = text.length();
		for (int i = 0; i < max; i++) {
			final int idx = text.charAt(i);
			int w = widths[idx];
			if (w>0) {
				graphics.drawRegion(fontImage, position[idx], 0, w, height, orientation, cx, cy, Graphics.TOP|Graphics.LEFT);
				w++;
			} else {
				w = SPACE_SIZE;
			}
			if (orientation == ORIENTATION_HORIZONTAL) {
				cx += w;
			} else {
				cy += w;
			}
		}
	}

	/**
	 * Paints a string centered on the screen.
	 * @param text the text to paint
	 * @param graphics the graphics object to paint on
	 * @param pos the offset from the "top".
	 * @param orientation the orientation of the text ({@link #ORIENTATION_HORIZONTAL}
	 * or {@link #ORIENTATION_VERTICAL}).
	 */
	public void drawStringCentered(final String text, final Graphics graphics, final int pos, final int orientation) {
		final int size = size(text);
		if (orientation == ORIENTATION_HORIZONTAL) {
			drawString(text, graphics, (mx-size)/2, pos, orientation);
		} else {
			drawString(text, graphics, pos, (my-size)/2, orientation);			
		}
	}

	/**
	 * Paints a text aligned in the given way
	 * @param text the text to paint
	 * @param graphics the graphics object to paint on
	 * @param pos the offset from the "top".
	 * @param orientation the orientation of the text ({@link #ORIENTATION_HORIZONTAL}
	 * or {@link #ORIENTATION_VERTICAL}).
	 * @param align the alignment if the text ({@link #ALIGN_LEFT} or {@link #ALIGN_RIGHT}).
	 * @param border border offset
	 */
	public void drawStringAligned(final String text, final Graphics graphics, final int pos, final int orientation, final int align, final int border) {
		int other;
		if (align == ALIGN_LEFT) {
			other = border;
		} else {
			other = (orientation==ORIENTATION_HORIZONTAL)?mx:my;
			other -= border;
			other -= size(text);
		}
		if (orientation == ORIENTATION_HORIZONTAL) {
			drawString(text, graphics, other, pos, orientation);
		} else {
			drawString(text, graphics, pos, other, orientation);			
		}
	}

	/**
	 * Returns the maximum x value the font paints on.
	 * @return the maximum x value the font paints on.
	 */
	public int getMaxX() {
		return mx;
	}

	/**
	 * Returns the maximum y value the font paints on.
	 * @return the maximum y value the font paints on.
	 */
	public int getMaxY() {
		return my;
	}

	/**
	 * Returns the height of a single character.
	 */
	public int getCharHeight() {
		return height;
	}

	/**
	 * Computes the size of the text in pixels.
	 * @param text the text to size.
	 * @return the size of the text in pixels.
	 */
	private int size(final String text) {
		int result = 0;
		for (int i = text.length(); i-- > 0; ) {
			final int w = widths[text.charAt(i)];
			result += w>0?w+1:SPACE_SIZE;
		}		
		return result;
	}

	/**
	 * Parses the font description.
	 * @param is stream holding the font description.
	 * @param characters the characters of the font
	 * @throws IOException in case of error
	 */
	private void parse(final InputStream is, final StringBuffer characters) throws IOException {
		int pos = 0;
		int idx = 0;
		int w = 0;
		while (is.available()>0) {
			final int d = is.read();
			if (d == ',' || d == '\n') {
				final int i = (int) characters.charAt(idx);
				widths[i] = w;
				position[i] = pos;
				idx++;
				pos += w;
				w = 0;
			} else {
				final int digit = ((char) d) - '0';
				if (digit >= 0 && digit < 10) {
					w *= 10;
					w += digit;
				}
			}
		}
	}
	
}
