/**
 * 
 */
package de.engehausen.mobile.crazygolf.renderer;

import javax.microedition.lcdui.Graphics;

import de.engehausen.mobile.crazygolf.Area;
import de.engehausen.mobile.crazygolf.Font;
import de.engehausen.mobile.crazygolf.FrameRenderer;
import de.engehausen.mobile.crazygolf.GameManager;

/**
 * Renders the introductory text before the game is played.
 * This text is displayed once per game, i.e. before the first
 * hole is played by the first player.
 */
public class IntroRenderer implements FrameRenderer {
	
	private static final String GOLF_COURSE = "golf course ";
	
	private final GameManager manager;
	private final Font font;
	
	private boolean repaint;

	/**
	 * Creates the intro renderer.
	 * @param aManager the game manager keeping track of all states.
	 * @param aFont the font to use for displaying text.
	 */
	public IntroRenderer(final GameManager aManager, final Font aFont) {
		manager = aManager;
		font = aFont;
	}

	// non-javadoc: see interface
	public boolean handleClick(final int x, final int y) {
		manager.setState(GameManager.STATE_PLACE_BALL);
		return false;
	}

	// non-javadoc: see interface
	public void handleSwipe(final int sx, final int sy, final int ex, final int ey, final long time, final double distsq) {
		// ignore
	}

	// non-javadoc: see interface
	public Area paint(final Graphics graphics) {
		int pos = manager.getWidth() - 64;
		graphics.drawImage(manager.getCurrentBackground(), 0, 0, Graphics.TOP|Graphics.LEFT);
		Util.getInstance().grayOut(graphics, 0, 0, manager.getWidth(), manager.getHeight());
		font.drawStringCentered(GOLF_COURSE + manager.getCurrentCourse().getName(), graphics, pos, Font.ORIENTATION_VERTICAL);
		pos -= 56;
		font.drawStringCentered("instructions: to place the ball touch", graphics, pos, Font.ORIENTATION_VERTICAL);
		pos -= 28;
		font.drawStringCentered("one of the blinking starting areas.", graphics, pos, Font.ORIENTATION_VERTICAL);
		pos -= 28;
		font.drawStringCentered("to move the ball swipe across the", graphics, pos, Font.ORIENTATION_VERTICAL);
		pos -= 28;
		font.drawStringCentered("screen in the desired direction. you", graphics, pos, Font.ORIENTATION_VERTICAL);
		pos -= 28;
		font.drawStringCentered("must hit the ball during the swipe.", graphics, pos, Font.ORIENTATION_VERTICAL);
		pos -= 56;
		font.drawStringCentered("tap the screen to continue...", graphics, pos, Font.ORIENTATION_VERTICAL);
		repaint = false;
		return null;
	}
	
	// non-javadoc: see interface
	public void reset() {
		repaint = true;
	}

	// non-javadoc: see interface
	public boolean needsRepaint() {
		return repaint;
	}

}