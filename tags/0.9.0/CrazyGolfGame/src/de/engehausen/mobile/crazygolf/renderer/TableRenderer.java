package de.engehausen.mobile.crazygolf.renderer;

import javax.microedition.lcdui.Graphics;

import de.engehausen.mobile.crazygolf.Area;
import de.engehausen.mobile.crazygolf.Font;
import de.engehausen.mobile.crazygolf.FrameRenderer;
import de.engehausen.mobile.crazygolf.GameManager;
import de.engehausen.mobile.crazygolf.model.Player;

/**
 * Shows the standings after a hole has been played.
 */
public class TableRenderer implements FrameRenderer {

	private static final String[] COLUMNS;
	static {
		COLUMNS = new String[18];
		for (int i = 0; i < COLUMNS.length; i++) {
			COLUMNS[i] = Integer.toString(i+1);
		}
	}
	
	private boolean repaint;
	private final GameManager manager;
	private final Font font;

	/**
	 * Creates the table standings renderer.
	 * @param aManager the game manager keeping track of all states.
	 * @param aFont the font to use for displaying text.
	 */
	public TableRenderer(final GameManager aManager, final Font aFont) {
		manager = aManager;
		font = aFont;
	}

	// non-javadoc: see interface
	public boolean handleClick(final int x, final int y) {
		if (!manager.nextHole()) {
			manager.setState(GameManager.STATE_SHOW_WINNER);
		}
		return false;
	}

	// non-javadoc: see interface
	public void handleSwipe(final int sx, final int sy, final int ex, final int ey, final long time, final double distSquare) {
		// ignore
	}

	// non-javadoc: see interface
	public boolean needsRepaint() {
		return repaint;
	}

	// non-javadoc: see interface
	public Area paint(final Graphics graphics) {
		repaint = false;
		graphics.drawImage(manager.getCurrentBackground(), 0, 0, Graphics.TOP|Graphics.LEFT);
		Util.getInstance().grayOut(graphics, 0, 0, manager.getWidth(), manager.getHeight());
		int pos = manager.getWidth()-64;
		font.drawStringCentered("current standings", graphics, pos, Font.ORIENTATION_VERTICAL);
		pos -= 48;
		final int start = Math.max(0, manager.getCurrentHoleIndex()-7);
		// columns
		font.drawStringAligned("hole", graphics, pos, Font.ORIENTATION_VERTICAL, Font.ALIGN_LEFT, 32);
		for (int i = 0; i < 8; i++) {
			font.drawStringAligned(COLUMNS[i+start], graphics, pos, Font.ORIENTATION_VERTICAL, Font.ALIGN_RIGHT, manager.getHeight()-160-40*i);
		}
		pos -= 48;
		final Player[] players = manager.getPlayers();
		for (int i = 0; i < players.length; i++) {
			font.drawStringAligned(players[i].getName(), graphics, pos, Font.ORIENTATION_VERTICAL, Font.ALIGN_LEFT, 32);
			for (int j = 0; j < 8; j++) {
				final int s = players[i].getStrokeCount(start+j);
				final String str;
				if (s < 10) {
					if (s>0) {
						str = Integer.toString(s);						
					} else {
						str = "-";
					}
				} else {
					str = "x";
				}
				font.drawStringAligned(str, graphics, pos, Font.ORIENTATION_VERTICAL, Font.ALIGN_RIGHT, manager.getHeight()-160-40*j);
			}
			pos -= 40;
		}
		return null;
	}
	
	// non-javadoc: see interface
	public void reset() {
		repaint = true;
	}

}
