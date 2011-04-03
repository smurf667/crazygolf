package de.engehausen.mobile.crazygolf.renderer;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import de.engehausen.mobile.crazygolf.Area;
import de.engehausen.mobile.crazygolf.Font;
import de.engehausen.mobile.crazygolf.FrameRenderer;
import de.engehausen.mobile.crazygolf.GameManager;
import de.engehausen.mobile.crazygolf.GolfGameCanvas;
import de.engehausen.mobile.crazygolf.Message;
import de.engehausen.mobile.crazygolf.model.Ball;
import de.engehausen.mobile.crazygolf.model.Hole;
import de.engehausen.mobile.crazygolf.model.Rectangle;

/**
 * Main game frame renderer. This renderer shows the hole and the
 * moving ball on the hole.
 */
public class GameRenderer implements FrameRenderer {

	private static final String[][] TEXTS = {
		{ "you did it...", "better luck next time", "uuuh...",
		  "come on!", "finally!" },
		{ "well done!", "par..." },
		{ "it is a birdie!", "birdie", "1-under-par" },
		{ "eagle!", "an eagle..." },
		{ "bogey...", "oh - a bogey!" },
		{ "double bogey!", "man! double bogey..." },
		{ "excellent!", "fantastic!" }
	};
	private static final int DONE = 0, PAR = 1, BIRDIE = 2, EAGLE = 3, BOGEY = 4, DOUBLE_BOGEY = 5, EXCELLENT = 6;
	private static int counter;
	
	private final GameManager manager;
	private final Font font;
	private final Area area;
	private boolean fullPaint, quitArmed;
	private Message message;
	private long lastClick;

	/**
	 * Creates the game renderer.
	 * @param aManager the game manager keeping track of all states.
	 * @param aFont the font to use for displaying text.
	 */
	public GameRenderer(final GameManager aManager, final Font aFont) {
		manager = aManager;
		font = aFont;
		area = new Area();
	}

	// non-javadoc: see interface
	public boolean handleClick(final int x, final int y) {
		final long now = System.currentTimeMillis();
		if (now - lastClick < 500L) {
			if (quitArmed) {
				manager.setState(GameManager.STATE_SHOW_MENU);
			} else {
				quitArmed = true;
				lastClick = now;
				message = new Message(font, "double tap again to quit", 20, GolfGameCanvas.FPS*2) {
					public boolean isShowing() {
						final boolean result = super.isShowing();
						if (!result) {
							quitArmed = false;							
						}
						return result;
					}					
				};
			}
		} else {
			lastClick = now;
		}
		return false;
	}

	// non-javadoc: see interface
	public void handleSwipe(final int sx, final int sy, final int ex, final int ey, final long time, final double distsq) {
		final Ball b = manager.getBall();
		if (!b.isMoving()) {
			final double v1 = ex-sx;
			final double v2 = ey-sy;
			final double n1 = -v2, n2 = v1;
			final double d = v1*n2 - v2*n1;
			if (d != 0) {
				final double bx = b.getX(), by = b.getY();
				final double x = ((bx-sx)*n2-(by-sy)*n1)/d;
				final double tx = v1*x+sx;
				final double ty = v2*x+sy;
				
			    if ( (bx-tx)*(bx-tx)+(by-ty)*(by-ty) < 30*30) { // swipe closer than 30 pixels to ball
			    	if (((ex-sx)*(ex-sx)+(ey-sy)*(ey-sy))/((tx-sx)*(tx-sx)+(ty-sy)*(ty-sy)) > 1) {  //NOPMD deeply nested if okay here
				    	final double power;
				    	if (time > 24) {
				    		power = 24d/time;
				    	} else {
				    		power = 1;
				    	}
				    	final double p1 = (tx-sx)*power;
				    	final double p2 = (ty-sy)*power;
				    	if ((p1*p1+p2*p2)>2d) {
					    	b.addSpeed(p1, p2);
					    	incrementStrikes();
					    	manager.playSound(GameManager.SOUND_TEE_OFF);
				    	}
			    	}
			    } 
			}			
		}
	}

	// non-javadoc: see interface
	public boolean needsRepaint() {
		return fullPaint || message!=null || manager.getBall().isMoving();
	}

	// non-javadoc: see interface
	public Area paint(final Graphics graphics) {
		final Image background = manager.getCurrentBackground();
		area.reset();
		if (fullPaint) {
			fullPaint = false;
			graphics.drawImage(background, 0, 0, Graphics.TOP|Graphics.LEFT);
			area.addArea(0, 0, background.getWidth(), background.getHeight());
		}
		final Ball ball = manager.getBall();
		try {
			if (ball.isMoving()) {
				area.addArea(ball.getPixelX(), ball.getPixelY(), ball.getWidth(), ball.getHeight());
				ball.moveAndPaint(graphics, background, manager);
				area.addArea(ball.getPixelX(), ball.getPixelY(), ball.getWidth(), ball.getHeight());
				if (ball.isHoled()) {
					handleHoled();
				} else if (!ball.isMoving() && manager.getStrikeCount()>9) {
					// next screen
					manager.nextPlayer();
				}
			} else {
				ball.paint(graphics);
				area.addArea((int) ball.getX(), (int) ball.getY(), ball.getWidth(), ball.getHeight());
			}
		} catch (IllegalArgumentException e) {
			final Rectangle r = manager.getCurrentHole().getStartZones()[0];
			ball.setPosition(r.cx, r.cy);
		}
		if (message != null) {
			message.paint(graphics, background);
			area.addArea(0, 16, background.getWidth(), 48); // TODO just estimated
			if (!message.isShowing()) {
				message = null;
				if (ball.isHoled()) {
					// next screen
					manager.nextPlayer();
				}
			}
		}
		return area;
	}

	// non-javadoc: see interface
	public void reset() {
		fullPaint = true;
		quitArmed = false;
		message = null;
	}

	/**
	 * Increment the strike count and show a fitting message.
	 */
	protected void incrementStrikes() {
		final Hole h = manager.getCurrentHole();
		if (h != null) {
			final StringBuffer sb = new StringBuffer(8);
			sb.append(manager.nextStrike())
			  .append(" (").append(h.getPar()).append(')');
			message = new Message(font, sb.toString(), 20, GolfGameCanvas.FPS*2);
		}
	}

	/**
	 * Handle the holing of the ball (display a text).
	 */
	protected void handleHoled() {
		final int count = manager.getStrikeCount();
		final String text;
		if (count == 1) {
			text = "hole in one!";
		} else {
			final int diff = manager.getCurrentHole().getPar() - count;			
			final int idx;
			switch (diff) {
				case -2:
					idx = DOUBLE_BOGEY;
					break;
				case -1:
					idx = BOGEY;
					break;
				case 0:
					idx = PAR;
					break;
				case 1:
					idx = BIRDIE;
					break;
				case 2:
					idx = EAGLE;
					break;
				default:
					if (diff > 2) {
						idx = EXCELLENT;
					} else {
						idx = DONE;
					}
					break;
			}
			text = TEXTS[idx][counter++%TEXTS[idx].length];
		}
		message = new Message(font, text, 20, GolfGameCanvas.FPS*2);
	}
	
}
