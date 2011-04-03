package de.engehausen.mobile.crazygolf.renderer;

import javax.microedition.lcdui.Graphics;

public final class Util {
	
	private static final Util INSTANCE = new Util();
	private final static int SIZE = 16;
	
	private final int[] darkBlock;
	
	private Util() {
		darkBlock = new int[SIZE*SIZE];
		for (int i = darkBlock.length; i-->0; ) {
			darkBlock[i] = 0x44000000;
		}
	}
	
	public static Util getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Gray out an existing area in a graphics object.
	 * @param graphics the graphics object to operate on
	 * @param x the starting position
	 * @param y the starting position
	 * @param width the width, dividable by {@link #SIZE}
	 * @param height the height, dividable by {@link #SIZE}
	 */
	public void grayOut(final Graphics graphics, final int x, final int y, final int width, final int height) {
		final int maxx = x+width, maxy = y+height;
		for (int cy = y; cy < maxy; cy += SIZE) {
			for (int cx = x; cx < maxx; cx += SIZE) {
				graphics.drawRGB(darkBlock, 0, SIZE, cx, cy, SIZE, SIZE, true);
			}
		}
	}
	

}
