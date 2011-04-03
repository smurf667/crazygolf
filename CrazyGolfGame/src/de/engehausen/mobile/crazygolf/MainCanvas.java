package de.engehausen.mobile.crazygolf;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import de.engehausen.mobile.crazygolf.model.Course;

/**
 * The main canvas shows the game menu.
 */
public class MainCanvas extends Canvas implements Painter, ClickHandler {
	
	protected final static int RENDER_BACKGROUND = 1;
	protected final static int RENDER_BUTTONS = 2;
	protected final static int RENDER_PLAYERS = 4;
	protected final static int RENDER_COURSE = 8;
	protected final static int RENDER_SFX = 16;	
	protected final static int RENDER_FULL = RENDER_BACKGROUND|RENDER_BUTTONS|RENDER_PLAYERS|RENDER_COURSE|RENDER_SFX;	
	
	private final static String EXIT = "exit";
	private final static String PLAY = "play";
	
	private final Main main;
	private final Font font;
	private final Image logo;
	private int mode;
	private final int buttonY;
	private final Selector courses, players, sfx;
	private final ClickHandler clickHandlers[];
	private final Course[] allCourses;

	/**
	 * Creates the main canvas.
	 * @param m the midlet running the game, must not be <code>null</code>.
	 * @param f the font used for displaying text, must not be <code>null</code>.
	 * @param coursesArr an array with all courses that can be played, must not be <code>null</code>.
	 * @throws IOException in case of error
	 */
	public MainCanvas(final Main m, final Font f, final Course[] coursesArr) throws IOException { //NOPMD direct array storage is done intentionally
		super();
		main = m;
		font = f;
		allCourses = coursesArr;
		logo = Image.createImage(getClass().getResourceAsStream("/logo.png"));
		courses = new Selector(this, RENDER_COURSE, f, getCourseNames(coursesArr), 250);
		players = new Selector(this, RENDER_PLAYERS, f, new String[] { "one player", "two players", "three players", "four players" }, 310);
		sfx = new Selector(this, RENDER_SFX, f, new String[] { "sfx: on", "sfx: off" }, 370);
		clickHandlers = new ClickHandler[] {
				players,
				courses,
				sfx,
				this
		};
		mode = RENDER_FULL;
		buttonY = font.getMaxY()-font.getCharHeight()-24;
	}

	// non-javadoc: see interface
	public boolean handleClick(final int x, final int y) {
		if (y >= buttonY) {
			if (x < font.getMaxX()/2) {
				main.playCourse(allCourses[courses.getSelectionIndex()], 1+players.getSelectionIndex(), sfx.getSelectionIndex()==0);
			} else {
				main.exit();
			}
			return true;
		} else {
			return false;			
		}
	}

	// non-javadoc: see interface
	public void repaint(final int m) {
		mode |= m;
		repaint();
	}

	// non-javadoc: see superclass
	protected void pointerReleased(final int x, final int y) {
		for (int i = clickHandlers.length; i-->0; ) {
			if (clickHandlers[i].handleClick(x, y)) {
				return;
			}
		}
	}
	
	// non-javadoc: see superclass
	protected void paint(final Graphics g) {
		final int w = getWidth(), h = getHeight();
		if (renderMode(RENDER_BACKGROUND)) {
			g.setColor(Constants.LAWN_GREEN);
			g.fillRect(0, 0, w, h);
			final int x = (w-logo.getWidth())/2;
			g.drawImage(logo, x, 32, Sprite.TRANS_NONE);
			font.drawStringCentered("golf course:", g, 220, Font.ORIENTATION_HORIZONTAL);
		}
		if (renderMode(RENDER_BUTTONS)) {
			font.drawStringAligned(PLAY, g, buttonY, Font.ORIENTATION_HORIZONTAL, Font.ALIGN_LEFT, 32);
			font.drawStringAligned(EXIT, g, buttonY, Font.ORIENTATION_HORIZONTAL, Font.ALIGN_RIGHT, 32);
		}
		if (renderMode(RENDER_PLAYERS)) {
			players.paint(g);
		}
		if (renderMode(RENDER_COURSE)) {
			courses.paint(g);
		}
		if (renderMode(RENDER_SFX)) {
			sfx.paint(g);
		}
	}

	/**
	 * Returns the names of all given courses.
	 * @param c the courses for which to return the names; must not be <code>null</code>.
	 * @return the names of all given courses.
	 */
	private String[] getCourseNames(final Course[] c) {
		final String[] result = new String[c.length];
		for (int i = c.length; i-->0; ) {
			result[i]  = c[i].getName();
		}
		return result;
	}

	/**
	 * Checks and toggles the render mode (the mode can be full or partial...).
	 * @param flag the flag indicating the mode
	 * @return <code>true</code> if the mode changed, <code>false</code> otherwise
	 */
	private boolean renderMode(final int flag) {
		if ((mode & flag) == 0) {
			return false;
		} else {
			mode ^= flag;
			return true;
		}
	}

}
