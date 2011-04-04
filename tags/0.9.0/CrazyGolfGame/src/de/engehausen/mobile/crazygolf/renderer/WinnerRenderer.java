package de.engehausen.mobile.crazygolf.renderer;

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import de.engehausen.mobile.crazygolf.Area;
import de.engehausen.mobile.crazygolf.Constants;
import de.engehausen.mobile.crazygolf.Font;
import de.engehausen.mobile.crazygolf.FrameRenderer;
import de.engehausen.mobile.crazygolf.GameManager;
import de.engehausen.mobile.crazygolf.model.Hole;
import de.engehausen.mobile.crazygolf.model.Player;

/**
 * Shows the winning player congratulation message.
 */
public class WinnerRenderer implements FrameRenderer {
	
	private final GameManager manager;
	private final Font font;
	
	private boolean repaint;
	private String winner;
	private String info;

	/**
	 * Creates the winner renderer.
	 * @param aManager the game manager keeping track of all states.
	 * @param aFont the font to use for displaying text.
	 */
	public WinnerRenderer(final GameManager aManager, final Font aFont) {
		manager = aManager;
		font = aFont;
	}

	// non-javadoc: see interface
	public boolean handleClick(final int x, final int y) {
		manager.setState(GameManager.STATE_SHOW_MENU);
		return false;
	}

	// non-javadoc: see interface
	public void handleSwipe(final int sx, final int sy, final int ex, final int ey, final long time, final double distSquare) {
		handleClick(sx, sy);
	}

	// non-javadoc: see interface
	public boolean needsRepaint() {
		return repaint;
	}

	// non-javadoc: see interface
	public Area paint(final Graphics graphics) {
		repaint = false;
		graphics.setColor(Constants.LAWN_GREEN);
		graphics.fillRect(0, 0, manager.getWidth(), manager.getHeight());
		int y = 64;
		final Image image = getImage();
		if (image != null) {
			graphics.drawImage(image, (manager.getWidth()-image.getWidth())/2, y, Graphics.TOP|Graphics.LEFT);
			font.drawStringCentered(winner, graphics, y+(image.getHeight()-font.getCharHeight())/2, Font.ORIENTATION_HORIZONTAL);
			y += 8+image.getHeight();
		}
		font.drawStringCentered(info, graphics, y, Font.ORIENTATION_HORIZONTAL);
		y += 64;
		font.drawStringCentered("tap screen to continue", graphics, y, Font.ORIENTATION_HORIZONTAL);
		return null;
	}

	// non-javadoc: see interface
	public void reset() {
		repaint = true;
		final Player[] players = manager.getPlayers();
		int idx = 0, min = Integer.MAX_VALUE;
		for (int i = players.length; i-->0; ) {
			int s = 0;
			for (int j = 0; j < 18; j++) {
				s += players[i].getStrokeCount(j);
			}
			if (s < min) {
				min = s;
				idx = i;
			}
		}
		final StringBuffer sb = new StringBuffer(48);
		sb.append(players[idx].getName()).append(" wins");
		winner = sb.toString();
		sb.setLength(0);
		final Hole[] holes = manager.getCurrentCourse().getHoles();
		int par = 0;
		for (int i = holes.length; i-->0; ) {
			par += holes[i].getPar();
		}
		if (min < par) {
			sb.append(Integer.toString(par-min)).append(" strokes under par!");			
		} else if (min == par) {
			sb.append("on par! (").append(Integer.toString(par)).append(" strokes)");
		} else {
			sb.append("...with ").append(Integer.toString(min)).append(" strokes");			
		}
		info = sb.toString();
	}

	/**
	 * Returns the laurel wreath image.
	 * @return the laurel wreath image.
	 */
	protected Image getImage() {
		try {
			return Image.createImage("/winner.png");
		} catch (IOException e) {
			return null;
		}
	}
}
