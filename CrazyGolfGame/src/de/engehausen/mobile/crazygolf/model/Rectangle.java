package de.engehausen.mobile.crazygolf.model;

/**
 * Defines a rectangle.
 */
public class Rectangle extends ElementOp {
	
	public final int ex,ey;
	public final int cx, cy;

	/**
	 * Creates the rectangle.
	 * 
	 * @param anID an ID
	 * @param x starting position
	 * @param y starting position
	 * @param flags flags
	 * @param vectorFlags flags
	 * @param w width
	 * @param h height
	 */
	public Rectangle(final int anID, final int x, final int y, final int flags, final int vectorFlags, final int w, final int h) {
		super(anID, x, y, flags, vectorFlags);
		ex = x+w;
		ey = y+h;
		cx = x+w/2;
		cy = y+h/2;
	}

	/**
	 * Checks if the given coordinates lie within the rectangle.
	 * @param tx the x-position
	 * @param ty the y-position
	 * @return <code>true</code> if the position is inside the rectangle, <code>false</code> otherwise.
	 */
	public boolean contains(final int tx, final int ty) {
		return tx>=x && tx<ex && ty>=y && ty<ey;
	}
	
}
