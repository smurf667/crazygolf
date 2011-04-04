package de.engehausen.mobile.crazygolf;

import javax.microedition.lcdui.Graphics;

/**
 * A selector is a UI widget which shows a selection.
 * The user can change between selections by tapping
 * left and right arrows which appear to both sides
 * of the selections' text.
 */
public class Selector implements ClickHandler {
	
	private static final int WIDTH = 12;
	
	private final Painter painter;
	private final Font font;
	private final String[] values;
	private final int y;
	private final int my;
	private int idx;
	private final int renderMode;

	/**
	 * Creates the selector.
	 * @param p the painter on which the selector resides, must not be <code>null</code>.
	 * @param mode the mode in which to call the painter
	 * @param f the font to use for displaying text, must not be <code>null</code>.
	 * @param v the selection text values, must not be <code>null</code>.
	 * @param pos the vertical position of the selector
	 */
	public Selector(final Painter p, final int mode, final Font f, final String[] v, final int pos) { //NOPMD direct array storage is done intentionally
		painter = p;
		renderMode = mode;
		font = f;
		values = v;
		y = pos;
		my = pos+f.getCharHeight();
		idx = 0;
	}

	// non-javadoc: see interface
	public boolean handleClick(final int cx, final int cy) {
		final boolean result;
		if (cy >= y-8 && cy<my+8) {
			if (cx < font.getMaxX()/2 && idx > 0) {
				idx--;
				painter.repaint(renderMode);
				result = true;
			} else if (idx < values.length-1) {
				idx++;
				painter.repaint(renderMode);
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Returns the index of the currently selected value.
	 * @return the index of the currently selected value.
	 */
	public int getSelectionIndex() {
		return idx;
	}
	
	/**
	 * Paints the selector into the given graphics.
	 * @param g the graphics to paint the selector into, must not be <code>null</code>.
	 */
	public void paint(final Graphics g) {
		g.setColor(Constants.LAWN_GREEN);
		g.fillRect(0, y, font.getMaxX(), font.getCharHeight());
		font.drawStringCentered(values[idx], g, y, Font.ORIENTATION_HORIZONTAL);
		if (idx < values.length-1) {
			g.setColor(Constants.WHITE);
			final int x = font.getMaxX()-WIDTH-16;
			final int h = font.getCharHeight()-1;
			g.fillTriangle(x, y, x+WIDTH, y+h/2, x, my);
		}
		if (idx > 0) {
			g.setColor(Constants.WHITE);
			final int h = font.getCharHeight()-1;
			g.fillTriangle(16, y+h/2, 16+WIDTH, y, 16+WIDTH, my);
		}
	}

}
