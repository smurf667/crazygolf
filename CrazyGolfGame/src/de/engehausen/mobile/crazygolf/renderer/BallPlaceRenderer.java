package de.engehausen.mobile.crazygolf.renderer;

import javax.microedition.lcdui.Graphics;

import de.engehausen.mobile.crazygolf.Area;
import de.engehausen.mobile.crazygolf.Element;
import de.engehausen.mobile.crazygolf.Font;
import de.engehausen.mobile.crazygolf.FrameRenderer;
import de.engehausen.mobile.crazygolf.GameManager;
import de.engehausen.mobile.crazygolf.model.Rectangle;

/**
 * A frame renderer which lets the user place the ball in one
 * of the starting zones of the hole.
 */
public class BallPlaceRenderer implements FrameRenderer {
	
	private int frameCounter;
	private final GameManager manager;
	private final Font font;
	private final HolePainter elementRenderer;
	private String text;
	private String par;
	private Rectangle[] zones;

	/**
	 * Creates the renderer.
	 * @param aHoleRenderer the painter for the holes.
	 * @param aManager the game manager
	 * @param aFont the font to use for displaying texts
	 */
	public BallPlaceRenderer(final HolePainter aHoleRenderer, final GameManager aManager, final Font aFont) {
		manager = aManager;
		font = aFont;
		elementRenderer = aHoleRenderer;
	}

	/**
	 * Check if the ball was placed into a starting zone; if yes,
	 * move to playing the game.
	 * @return <code>true</code> ball was placed and player is now
	 * playing, <code>false</code>: click was not handled.
	 */
	public boolean handleClick(final int x, final int y) {
		for (int i = zones.length; i-->0; ) {
			if (zones[i].contains(x, y)) {
				manager.getBall().setPosition(x, y);
				manager.setState(GameManager.STATE_PLAY);
				break;	
			}
		}
		return false;
	}

	// non-javadoc: see interface
	public boolean needsRepaint() {
		frameCounter++;
		return frameCounter == 1 || (frameCounter%10)==0;
	}
	
	// non-javadoc: see interface
	public Area paint(final Graphics graphics) {
		if (frameCounter == 1) {
			graphics.drawImage(manager.getCurrentBackground(), 0, 0, Graphics.TOP|Graphics.LEFT);			
		} else {
			final int idx = (frameCounter>>1)&0x1;
			for (int i = zones.length; i-->0; ) {
				final Rectangle op = zones[i];
				final Element e = manager.getElement(idx+op.id);
				elementRenderer.renderElement(graphics, e, op);
			}
		}
		font.drawStringCentered(text, graphics, 256, Font.ORIENTATION_VERTICAL);
		font.drawStringCentered(par, graphics, 256 - font.getCharHeight(), Font.ORIENTATION_VERTICAL);
		return null; // TODO this *could* be more detailed
	}

	// non-javadoc: see interface
	public void reset() {
		frameCounter = 0;
		final StringBuffer sb = new StringBuffer(32);
		sb.append(manager.getCurrentPlayer().getName()).append(" - place the ball");
		text = sb.toString();
		sb.setLength(0);
		sb.append("the par is ").append(manager.getCurrentHole().getPar());
		par = sb.toString();
		zones = manager.getCurrentHole().getStartZones();
	}

	// non-javadoc: see interface
	public void handleSwipe(final int sx, final int sy, final int ex, final int ey, final long time, final double distsq) {
		// ignore
	}

}
