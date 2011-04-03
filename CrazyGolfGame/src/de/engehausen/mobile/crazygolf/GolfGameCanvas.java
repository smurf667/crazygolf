package de.engehausen.mobile.crazygolf;

import javax.microedition.lcdui.game.GameCanvas;

/**
 * The golf game canvas is the main canvas showing the game.
 * It holds a {@link FrameRenderer} which paints the pixels on demand.
 * The frame renderer is checked for the need of repaints with the
 * frame rate specified in {@link #FPS}. If the device is too slow,
 * rendering will happen as quickly as possible, but no frames will
 * be dropped (this may cause the game to look slowish...).
 */
public class GolfGameCanvas extends GameCanvas implements Runnable {

	/**
	 * The frame rate at which paints should occur.
	 */
	public static final int FPS = 20;
	
	private static final long FRAME_WAIT = 1000L/FPS;
	
	private boolean running;
	private volatile FrameRenderer frameRenderer;
	private int startX, startY;
	private long startTime;

	/**
	 * Creates the canvas.
	 */
	public GolfGameCanvas() {
		super(true);
	}

	/**
	 * Set the frame renderer to use for painting.
	 * @param renderer the frame renderer to use for painting.
	 */
	public void setFrameRenderer(final FrameRenderer renderer) {
		frameRenderer = renderer;
		frameRenderer.reset();
	}

	/**
	 * Starts the periodic painting thread.
	 */
	public void start() {
		running = true;
		new Thread(this).start();
	}
	
	/**
	 * Stops the periodic painting thread.
	 */
	public void stop() {
		running = false;
	}

	// handle rendering the frames
	public void run() {
		while (running) {
			final long begin = System.currentTimeMillis();
			if (frameRenderer != null && frameRenderer.needsRepaint()) {
				final Area a = frameRenderer.paint(getGraphics());
				if (a == null) {
					flushGraphics();					
				} else {
					flushGraphics(a.minx, a.miny, a.maxx-a.minx, a.maxy-a.miny);					
				}
			}
			final long duration = System.currentTimeMillis() - begin;
			final long wait = FRAME_WAIT - duration;
			if (wait > 0) {
				try {
					Thread.sleep(wait);
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	}
		
	// non-javadoc: see superclass
	protected void pointerReleased(final int x, final int y) {
		final double d = (x-startX)*(x-startX)+(y-startY)*(y-startY);
		if (d < 9) {
			frameRenderer.handleClick(x, y);			
		} else {
			frameRenderer.handleSwipe(startX, startY, x, y, System.currentTimeMillis()-startTime, d);
		}
	}

	// non-javadoc: see superclass
	protected void pointerPressed(final int x, final int y) {
		startTime = System.currentTimeMillis();
		startX = x;
		startY = y;
	}

}
