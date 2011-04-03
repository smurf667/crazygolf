package de.engehausen.mobile.crazygolf.model;

/**
 * Defines a rectangular vector field. A vector field
 * has a constant vector which adds to the balls' speed if the
 * ball is in the field.
 */
public class VectorField extends Rectangle {
	
	private final double deltaX, deltaY;

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
	 * @param dx vector x
	 * @param dy vector y
	 */
	public VectorField(final int anID, final int x, final int y, final int flags, final int vectorFlags, final int w, final int h, final double dx, final double dy) {
		super(anID, x, y, flags, vectorFlags, w, h);
		deltaX = dx*parseFlag(1, vectorFlags);
		deltaY = dy*parseFlag(2, vectorFlags);
	}

	/**
	 * Get x delta.
	 * @return x delta.
	 */
	public double getDeltaX() {
		return deltaX;
	}

	/**
	 * Get y delta.
	 * @return y delta.
	 */
	public double getDeltaY() {
		return deltaY;
	}
	
	/**
	 * Parses flipping flag.
	 * @param bit the bit to check
	 * @param flag the flag to check on
	 * @return -1 or 1
	 */
	private double parseFlag(final int bit, final int flag) {
		return ((flag&bit)==bit)?-1:1;
	}

}
