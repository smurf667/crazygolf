package de.engehausen.mobile.crazygolf.model;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import de.engehausen.mobile.crazygolf.Element;
import de.engehausen.mobile.crazygolf.GameManager;

/**
 * The golf ball in the game...
 */
public class Ball {

	private static final double FRICTION = 0.987d;
	private static final double ANGLES[][] = new double[45][2];
	private static final double RING[][] = new double[9][2];
	
	static {
		final double PI2 = Math.PI*2;
		for (int i = 0; i < ANGLES.length; i++) {
			final double a = PI2*i/ANGLES.length;
			ANGLES[i][0] = Math.cos(a);
			ANGLES[i][1] = Math.sin(a);	
		}
		for (int i = 0; i < RING.length; i++) {
			final double a = PI2*i/RING.length;
			RING[i][0] = 3d*Math.cos(a);
			RING[i][1] = 3d*Math.sin(a);
		}
	}
	
	private final Image elementsImage;
	private final Element element;	
	private double x, y;
	private double vx, vy;
	private final int[] pixel;
	private boolean holed;
	private final int maxx, maxy;
	private final int[] colors; // colors that are not considered a collision
	private final int width, height;

	/**
	 * Creates the ball.
	 * @param ballImage the image of the ball, must not be <code>null</code>.
	 * @param nonCollisionColors image with all pixel colors that are not causing a collision, must not be <code>null</code>.
	 * @param e the element describing the ball, must not be <code>null</code>.
	 * @param screenWidth the width of the screen.
	 * @param screenHeight the height of the screen.
	 */
	public Ball(final Image ballImage, final Image nonCollisionColors, final Element e, final int screenWidth, final int screenHeight) {
		elementsImage = ballImage;
		width = ballImage.getWidth();
		height = ballImage.getHeight();
		element = e;
		pixel = new int[1];
		maxx = screenWidth;
		maxy = screenHeight;
		colors = getNonCollisionColors(nonCollisionColors);
	}
	
	/**
	 * Returns the colors that do not cause a collision.
	 * @param img the image with the non-collision colors (height: 1px, width: number of colors)
	 * @return the non-collision colors, never <code>null</code>.
	 */
	private int[] getNonCollisionColors(final Image img) {
		final int s = img.getWidth();
		final int[] pixels = new int[s];
		img.getRGB(pixels, 0, s, 0, 0, s, 1);		
		return pixels;
	}

	/**
	 * Returns the width of the ball.
	 * @return the width of the ball.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the ball.
	 * @return the height of the ball.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the x-position of the ball
	 * @return the x-position of the ball
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the y-position of the ball
	 * @return the y-position of the ball
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Returns the x-position of where the ball image starts to be displayed
	 * @return the x-position of the ball on the graphics
	 */
	public int getPixelX() {
		return (int) x-3;
	}

	/**
	 * Returns the y-position of where the ball image starts to be displayed
	 * @return the y-position of the ball on the graphics
	 */
	public int getPixelY() {
		return (int) y-3;
	}

	/**
	 * Sets the position of the ball
	 * @param xVal the x-position of the ball
	 * @param yVal the y-position of the ball
	 */
	public void setPosition(final int xVal, final int yVal) {
		x = xVal;
		y = yVal;
		vx = 0;
		vy = 0;
		holed = false;
	}

	/**
	 * Adds the given speed vector to the ball.
	 * @param deltaX the x component of the speed vector
	 * @param deltaY the y component of the speed vector
	 */
	public void addSpeed(final double deltaX, final double deltaY) {
		vx += deltaX;
		vy += deltaY;
	}

	/**
	 * Moves the ball and paints it into the graphics.
	 * @param g the graphics to paint on, not <code>null</code>.
	 * @param background the background image used to restore pixels, not <code>null</code>.
	 * @param manager the game manager keeping track of all state, not <code>null</code>.
	 */
	public void moveAndPaint(final Graphics g, final Image background, final GameManager manager) {
		final Hole hole = manager.getCurrentHole();
		// restore old background
		final int px = getPixelX(), py = getPixelY();
		g.drawRegion(background, px, py, width, height, Sprite.TRANS_NONE, px, py, Graphics.TOP|Graphics.LEFT);
		x += vx;
		y += vy;
		final double speed = vx*vx+vy*vy;
		if (collisionCheck(background)) {
			manager.playSound(GameManager.SOUND_HIT);
			fitCollision(background);
			performCollision(background);
			if (collisionCheck(background)) {
				fitCollision(background);			
			}
		} else {
			final int type = getSurfaceType((int) x, (int) y, background);
			if (type == Element.TYPE_NORMAL) {
				if (speed < 0.42d) { 
					vx = 0;
					vy = 0;
				}				
			} else {
				final VectorField field = hole.findVectorField((int) x, (int) y, type);
				if (field != null) {
					vx += field.getDeltaX();
					vy += field.getDeltaY();
				}
			}
		}
		vx *= FRICTION;
		vy *= FRICTION;
		// check holes
		final Rectangle[] rects = hole.getHoles();
		for (int i = rects.length; i-->0; ) {
			final double dx = (rects[i].cx-x), dy = (rects[i].cy-y);
			double dist = dx*dx+dy*dy;
			if (dist < 81) {
				if (dist < 49 && speed < 16) {
					if (dist < 9) {
						x = rects[i].cx;
						y = rects[i].cy;
						vx = 0;
						vy = 0;
						holed = true;
				    	manager.playSound(GameManager.SOUND_HOLE);
					} else {
						// directly aim at the center of the hole
						dist = 2.5d*Math.sqrt(dist);
						vx = (dx/dist);
						vy = (dy/dist);						
					}
				} else {
					if (dist != 0) {
						dist = 2d*Math.sqrt(dist);
						vx += (dx/dist);
						vy += (dy/dist);
					}
				}
			}
		}
		paint(g);
	}

	/**
	 * Simply paints the ball.
	 * @param g the graphics to paint on, not <code>null</code>.
	 */
	public void paint(final Graphics g) {
		// paint ball
		g.drawRegion(elementsImage, 0, 0, element.getWidth(), element.getHeight(), Sprite.TRANS_NONE, getPixelX(), getPixelY(), Graphics.TOP|Graphics.LEFT);
	}

	/**
	 * Returns whether to ball is moving or not.
	 * @return whether to ball is moving or not.
	 */
	public boolean isMoving() {
		return vx != 0 || vy != 0;
	}

	/**
	 * Returns whether the ball is in a hole or not.
	 * @return whether the ball is in a hole or not.
	 */
	public boolean isHoled() {
		return holed;		
	}
	
	/**
	 * Check and act on collision.
	 * @param background the image in which to check for collision
	 */
	protected boolean collisionCheck(final Image background) {
		for (int i = RING.length; i-->0; ) {
			if (isMasked((int) (x+RING[i][0]), (int) (y+RING[i][1]), background)) {
				return true; //NOPMD
			}
		}
		return false;
	}
	
	/**
	 * Fit the ball exactly against the "wall". The assumption
	 * is that the ball was not colliding at the previous collision
	 * and is at the time of calling the method.
	 * @param background the image in which to check for collision
	 */
	protected void fitCollision(final Image background) {
		double nvx, nvy;
		if (Math.abs(vx)>Math.abs(vy)) {
			nvx = vx/Math.abs(vx);
			nvy = vy/Math.abs(vx);
		} else {
			nvx = vx/Math.abs(vy);
			nvy = vy/Math.abs(vy);			
		}
		int max = 9; // emergency break out
		do {
			x -= nvx;
			y -= nvy;
		} while (max-->0 && collisionCheck(background));
	}	

	/**
	 * Performs the actual collision.
	 * @param background the image in which to check for collision
	 */
	protected void performCollision(final Image background) {
		double nx = 0, ny = 0;
		// determine collision normal vector
		for (int i = 0; i < ANGLES.length; i++) {
			final double rx = 4d*ANGLES[i][0];
			final double ry = 4d*ANGLES[i][1];
			if (isMasked((int) (rx+x), (int) (ry+y), background)) {
				nx += ANGLES[i][0];
				ny += ANGLES[i][1];
			}
		}
		final double d = nx*nx+ny*ny;
		if (d != 0) {
			final double k = ((-vx)*nx+(-vy)*ny)/d;
			vx = 0.75d*(2*k*nx+vx);
			vy = 0.75d*(2*k*ny+vy);			
			x += vx;
			y += vy;
		}
	}
	
	/**
	 * Returns the type of surface the ball is on.
	 * @param tx x-position
	 * @param ty y-position
	 * @param background the background the ball is on
	 * @return the surface type
	 */
	private int getSurfaceType(final int tx, final int ty, final Image background) {
		background.getRGB(pixel, 0, 1, tx, ty, 1, 1);
		if (pixel[0] == colors[0]) {
			return Element.TYPE_NORMAL;
		} else if (pixel[0] == colors[1]) {
			return Element.TYPE_DOWN;
		} else if (pixel[0] == colors[2]) {
			return Element.TYPE_UP;
		}
		return Element.TYPE_NORMAL;
	}

	/**
	 * Returns whether the ball collides with the landscape or not
	 * @param tx x-position
	 * @param ty y-position
	 * @param background the background the ball is on
	 * @return <code>true</code> if the ball collides, <code>false</code> otherwise
	 */
	private boolean isMasked(final int tx, final int ty, final Image background) { // TODO more efficient?
		if (tx >=0 && tx < maxx && ty >=0 && ty < maxy) {
			background.getRGB(pixel, 0, 1, tx, ty, 1, 1);
			for (int i = 0; i < colors.length; i++) {
				if (colors[i] == pixel[0]) {
					return false;
				}
			}
			return true;
		} else {
			return true;
		}
	}
	
}
