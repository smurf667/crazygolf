package de.engehausen.mobile.crazygolf;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import de.engehausen.mobile.crazygolf.model.Ball;
import de.engehausen.mobile.crazygolf.model.Course;
import de.engehausen.mobile.crazygolf.renderer.HolePainter;

/**
 * The main midlet class driving the whole game (menu and playing the
 * game itself). The game is designed for a 320x480 (HVGA) screen.
 */
public class Main extends MIDlet {

	public static final int WIDTH = 320, HEIGHT = 480;

	private final Display display;
	private Canvas currentCanvas;
	private MainCanvas menuCanvas;
	private GolfGameCanvas gameCanvas;
	private GameManager gameManager;

	/**
	 * Creates the midlet and obtains the display.
	 */
	public Main() {
		super();
		display = Display.getDisplay(this);
	}

	/**
	 * Stops the midlet.
	 */
	public void exit() {
		try {
			destroyApp(false);
			notifyDestroyed();
		} catch (MIDletStateChangeException e) { //NOPMD
			// nothing we can do, exit with as much grace as is left...
		}		
	}

	// non-javadoc: see interface
	protected void destroyApp(final boolean force) throws MIDletStateChangeException {
		// do nothing
	}

	// non-javadoc: see interface
	protected void pauseApp() {
		// do nothing
	}

	// non-javadoc: see interface
	protected void startApp() throws MIDletStateChangeException {
		if (currentCanvas == null) {
			// first time around, startup...
			try {
				final Element[] elements = ElementReader.getInstance().read(getClass().getResourceAsStream("/elements.txt"));				
				final Font font = new Font("font", WIDTH, HEIGHT);
				gameCanvas = new GolfGameCanvas();
				final HolePainter holePainter = new HolePainter(elements, WIDTH, HEIGHT);
				final Image colors = Image.createImage("/colors.png");
				gameManager = new GameManager(this, gameCanvas, new Ball(holePainter.getImage(elements[0]), colors, elements[0], WIDTH, HEIGHT), holePainter, font, elements);
				gameCanvas.setFullScreenMode(true);
				menuCanvas = new MainCanvas(this, font, gameManager.getCourses());
				menuCanvas.setFullScreenMode(true);
				showMenu();
			} catch (IOException e) {
				throw new MIDletStateChangeException(e.getMessage()); //NOPMD no exception nesting on small device
			}
		}
		activateCanvas();
	}

	/**
	 * Activates the current canvas and displays it.
	 */
	protected void activateCanvas() {
		display.setCurrent(currentCanvas);		
		currentCanvas.repaint();
	}

	/**
	 * Shows the main menu.
	 */
	public void showMenu() {
		gameCanvas.stop();
		menuCanvas.repaint(MainCanvas.RENDER_FULL);
		currentCanvas = menuCanvas;
		activateCanvas();
	}
	
	/**
	 * Plays the given course.
	 * @param course the course to play, must not be <code>null</code>.
	 * @param players the number of players
	 * @param sound <code>true</code> if sounds should be played during the game, <code>false</code> otherwise.
	 */
	public void playCourse(final Course course, final int players, final boolean sound) {
		gameManager.setCourse(course, players, sound);
		currentCanvas = gameCanvas;
		activateCanvas();
		gameCanvas.start();
	}
	
}
