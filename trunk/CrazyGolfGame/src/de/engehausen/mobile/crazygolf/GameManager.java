package de.engehausen.mobile.crazygolf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;

import de.engehausen.mobile.crazygolf.model.Ball;
import de.engehausen.mobile.crazygolf.model.Course;
import de.engehausen.mobile.crazygolf.model.Hole;
import de.engehausen.mobile.crazygolf.model.Player;
import de.engehausen.mobile.crazygolf.renderer.BallPlaceRenderer;
import de.engehausen.mobile.crazygolf.renderer.GameRenderer;
import de.engehausen.mobile.crazygolf.renderer.HolePainter;
import de.engehausen.mobile.crazygolf.renderer.IntroRenderer;
import de.engehausen.mobile.crazygolf.renderer.TableRenderer;
import de.engehausen.mobile.crazygolf.renderer.WinnerRenderer;

/**
 * The game manager manages playing crazy golf.
 * It has states which define which {@link FrameRenderer} to use;
 * it defines state transitions and keeps track of the
 * players.
 */
public class GameManager {

	/** shows the introduction */
	public static final int STATE_SHOW_INTRO = 0,
	/** ball placing screen */
	                        STATE_PLACE_BALL = 1,
	/** main playing screen */
	                        STATE_PLAY = 2,
	/** shows standings */
	                        STATE_SHOW_TABLE = 3,
	/** shows the winner */
	                        STATE_SHOW_WINNER = 4,
	/** returns back to the main menu */
	                        STATE_SHOW_MENU = 5;
	/** sound: ball collides with landscape */
	public static final int SOUND_HIT = 0,
	/** sound: hitting the ball */
	                        SOUND_TEE_OFF = 1,
	/** sound: ball holed */
	                        SOUND_HOLE = 2;

	private static final String WAVES[] = { "/13957.wav", "/13959.wav", "/20428.wav" };
	private static final String MEDIA_TYPE = "audio/x-wav";

	private final GolfGameCanvas view;
	private final Main main;
	private final Ball ball;
	private final HolePainter renderer;
	private final Image background;
	private final FrameRenderer introRenderer, placementRenderer, gameRenderer, tableRenderer, winnerRenderer;
	private final Course[] courses;
	private final Element[] elements;
	private Course course;
	private int holeIndex;
	private Player[] players;
	private int player;
	private boolean sound;
	private final javax.microedition.media.Player soundPlayer[] = new javax.microedition.media.Player[WAVES.length];

	/**
	 * Creates the game manager.
	 * @param aMain the midlet running the show, must not be <code>null</code>.
	 * @param aCanvas the game canvas used for displaying the main game, must not be <code>null</code>.
	 * @param aBall the ball, a central piece of the game ;-) - must not be <code>null</code>.
	 * @param aHolePainter the painter for the individual holes, must not be <code>null</code>.
	 * @param aFont the font used for displaying text, must not be <code>null</code>.
	 * @param anElements all paintable elements, must not be <code>null</code>.
	 * @throws IOException in case of error
	 */
	public GameManager(final Main aMain, final GolfGameCanvas aCanvas, final Ball aBall, final HolePainter aHolePainter, final Font aFont, final Element[] anElements) throws IOException { //NOPMD direct array storage is done intentionally
		main = aMain;
		view = aCanvas;
		ball = aBall;
		renderer = aHolePainter;
		background = Image.createImage(view.getWidth(), view.getHeight());
		introRenderer = new IntroRenderer(this, aFont);
		placementRenderer = new BallPlaceRenderer(renderer, this, aFont);
		gameRenderer = new GameRenderer(this, aFont);
		tableRenderer = new TableRenderer(this, aFont);
		winnerRenderer = new WinnerRenderer(this, aFont);
		courses = loadCourses(anElements);
		elements = anElements;
		for (int i = soundPlayer.length; i-->0; ) {
			try {
				final InputStream stream = getClass().getResourceAsStream(WAVES[i]);
				soundPlayer[i] = Manager.createPlayer(stream, MEDIA_TYPE);
				if (soundPlayer[i].getState() != javax.microedition.media.Player.REALIZED) {
					soundPlayer[i].realize();
				}
				soundPlayer[i].prefetch();
				stream.close();
			} catch (IOException e) {
				sound = false;
			} catch (MediaException e) {
				sound = false;
			}			
		}
	}

	/**
	 * Returns the ball.
	 * @return the ball, never <code>null</code>.
	 */
	public Ball getBall() {
		return ball;
	}

	/**
	 * Returns the width of the screen.
	 * @return the width of the screen.
	 */
	public int getWidth() {
		return view.getWidth();
	}

	/**
	 * Returns the height of the screen.
	 * @return the height of the screen.
	 */
	public int getHeight() {
		return view.getHeight();
	}

	/**
	 * Returns all courses.
	 */
	public Course[] getCourses() {
		return courses;  //NOPMD exposure of internal array okay for a small game
	}

	/**
	 * Sets the course to play.
	 * @param c the course to play, must not be <code>null</code>.
	 * @param playerCount the number of players
	 * @param useSound <code>true</code> for use of sound, <code>false</code> otherwise.
	 */
	public void setCourse(final Course c, final int playerCount, final boolean useSound) {
		sound = useSound;
		course = c;
		players = new Player[playerCount];
		for (int i = 0; i < playerCount; i++) {
			players[i] = new Player(i); //NOPMD
		}
		holeIndex = -1;
		nextHole();
	}

	/**
	 * Returns an element by ID.
	 * @param id the ID of an element
	 * @return the element with the given ID.
	 */
	public Element getElement(final int id) {
		return elements[id];
	}
	
	/**
	 * Sets the state of the game manager. This usually defines
	 * the (next) frame renderer to use; or may move back to the
	 * main menu.
	 * @param state the new state.
	 */
	public void setState(final int state) {
		switch (state) {
			case STATE_SHOW_INTRO:
				view.setFrameRenderer(introRenderer);
				break;
			case STATE_PLACE_BALL:
				view.setFrameRenderer(placementRenderer);
				break;
			case STATE_PLAY:
				view.setFrameRenderer(gameRenderer);
				break;
			case STATE_SHOW_TABLE:
				view.setFrameRenderer(tableRenderer);
				break;
			case STATE_SHOW_WINNER:
				view.setFrameRenderer(winnerRenderer);
				break;
			case STATE_SHOW_MENU:
				main.showMenu();
				break;
			default:
				break;
		}
	}

	/**
	 * Lets the next player play.
	 */
	public void nextPlayer() {
		if (player < players.length-1) {
			player++;
			setState(STATE_PLACE_BALL);
		} else {
			setState(STATE_SHOW_TABLE);
		}
	}

	/**
	 * Switches to the next hole
	 * @return <code>true</code> if a next hole in the course exists,
	 * <code>false</code> otherwise.
	 */
	public boolean nextHole() {
		if (holeIndex < 17) {
			holeIndex++;
			player = 0;
			computeBackground();
			if (holeIndex == 0) {
				setState(STATE_SHOW_INTRO);				
			} else {
				setState(STATE_PLACE_BALL);				
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Increments the current players' strike.
	 * @return the strike count.
	 */
	public int nextStrike() {
		return players[player].nextStroke(holeIndex);
	}

	/**
	 * Returns the current players' strike count.
	 * @return the current players' strike count.
	 */
	public int getStrikeCount() {
		return players[player].getStrokeCount(holeIndex);
	}

	/**
	 * Returns all players.
	 * @return all players, never <code>null</code>.
	 */
	public Player[] getPlayers() {
		return players; //NOPMD exposure of internal array okay for a small game
	}

	/**
	 * Returns the current player.
	 * @return the current player, never <code>null</code>.
	 */
	public Player getCurrentPlayer() {
		return players[player];
	}

	/**
	 * Returns the curent background image.
	 * @return the curent background image, never <code>null</code>.
	 */
	public Image getCurrentBackground() {
		return background;
	}

	/**
	 * Returns the current course.
	 * @return the current course.
	 */
	public Course getCurrentCourse() {
		return course;
		
	}

	/**
	 * Returns the current hole.
	 * @return the current hole.
	 */
	public Hole getCurrentHole() {
		return course.getHoles()[holeIndex];
	}
	
	/**
	 * Returns the current hole index (0..17).
	 * @return the current hole index (0..17).
	 */
	public int getCurrentHoleIndex() {
		return holeIndex;
	}

	/**
	 * Plays the sound with the given sample index.
	 * If sounds are disabled, this does nothing.
	 * @param sample the sample index for the sound to play.
	 */
	public void playSound(final int sample) {
		if (sound) {
			try {
				if (soundPlayer[sample] != null) {
					soundPlayer[sample].start();
				}
			} catch (MediaException e) {
				sound = false;
			}
		}
	}

	/**
	 * Loads all courses.
	 * @param elements the elements backing the courses, must not be <code>null</code>.
	 * @return an array with all courses, never <code>null</code>.
	 * @throws IOException in case of error
	 */
	private Course[] loadCourses(final Element[] elements) throws IOException {
		final StringBuffer name = new StringBuffer(16);
		name.append('/');
		final Vector list = new Vector(4);
		for (int i = 0; i < 100; i++) {
			name.setLength(1);  //NOPMD not a constructor call
			if (i < 10) {
				name.append('0');
			}
			name.append(i).append(".crs");
			final InputStream is = getClass().getResourceAsStream(name.toString());
			if (is != null) {
				list.addElement(Course.load(elements, is));
			}
		}
		final Course[] result = new Course[list.size()];
		list.copyInto(result);
		return result;
	}

	/**
	 * Paints the background image for the current hole.
	 */
	private void computeBackground() {
		final Hole h = getCurrentHole();
		renderer.render(background.getGraphics(), h);
	}

}
