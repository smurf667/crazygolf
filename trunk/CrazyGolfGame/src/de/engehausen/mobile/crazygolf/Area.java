package de.engehausen.mobile.crazygolf;

/**
 * Definition of an area. This can be used to keep track of the
 * modified area in a canvas for more efficient repainting.
 */
public class Area {

	/** min/max x/y */
	public int minx, miny, maxx, maxy;
	
	public Area() {
		reset();
	}

	/**
	 * Resets the area to minimum and maximum settings.
	 */
	public final void reset() {
		maxx = maxy = 0;
		minx = miny = Integer.MAX_VALUE;
	}

	/**
	 * Adds an area to this area. The new total area is recomputed.
	 * @param rx the starting x point of the area to add
	 * @param ry the starting y point of the area to add
	 * @param rwidth the width of the area to add
	 * @param rheight the height of the area to add
	 */
	public void addArea(final int rx, final int ry, final int rwidth, final int rheight) {
		minx = Math.min(minx, rx);
		miny = Math.min(miny, ry);
		maxx = Math.max(maxx, rx+rwidth);
		maxy = Math.max(maxy, ry+rheight);
	}

}
