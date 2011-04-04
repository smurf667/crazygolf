package de.engehausen.mobile.crazygolf.model;

/**
 * Operation on an element. This defines where an element
 * template is placed on the actual hole (position x,y) and
 * how - if - the element or its move vector is flipped.
 */
public class ElementOp {
	
	public final int id, x, y, flags, vectorFlags;
	
	/**
	 * Creates the operation.
	 * @param anId the id
	 * @param anX the x-position
	 * @param anY the y-position
	 * @param aFlags the flags for flipping the images
	 * @param aVectorFlags the flags for flipping the move vectors.
	 */
	public ElementOp(final int anId, final int anX, final int anY, final int aFlags, final int aVectorFlags) {
		id = anId;
		x = anX;
		y = anY;
		flags = aFlags;
		vectorFlags = aVectorFlags;
	}
}
