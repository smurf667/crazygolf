package de.engehausen.crazygolf.model;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * An element making up the graphical representation of a course.
 */
public class Element {

	private final Elements elements;
	private final de.engehausen.mobile.crazygolf.Element[] templates;
	private de.engehausen.mobile.crazygolf.Element template;
	private int x, y;
	private int flip, vectorFlip;

	/**
	 * Creates the element, backed by all elements and a list
	 * of templates for each mode of the element.
	 * @param allElements all elements
	 * @param aTemplates list with template elements
	 */
	public Element(final Elements allElements, final de.engehausen.mobile.crazygolf.Element[] aTemplates) { //NOPMD
		elements = allElements;
		templates = aTemplates;
		template = aTemplates[0];
	}

	/**
	 * Returns the ID of the element.
	 * @return the ID of the element.
	 */
	public int getID() {
		return template.getID();
	}

	/**
	 * Returns whether the given type is available.
	 * @param type the type, see {@link de.engehausen.mobile.crazygolf.Element#TYPE_NORMAL} etc.
	 * @return <code>true</code> if the given type is supported
	 */
	public boolean hasMode(final int type) {
		return templates[type] != null;
	}

	/**
	 * Sets the element to the given mode if available.
	 * @param type the new mode of the element.
	 */
	public void setMode(final int type) {
		final de.engehausen.mobile.crazygolf.Element t = templates[type];
		if (t != null) {
			template = t;
		}
	}

	/**
	 * Indicates that the element has a vector delta.
	 * @return <code>true</code> if the element has a vector delta, <code>false</code> otherwise.
	 */
	public boolean hasDelta() {
		return template.hasDelta();
	}

	/**
	 * Returns the x-component of the delta associated with the element.
	 * @return the x-component of the delta associated with the element.
	 */
	public double getDeltaX() {
		return template.getDeltaX()*(isVectorFlippedH()?-1:1);
	}

	/**
	 * Returns the y-component of the delta associated with the element.
	 * @return the y-component of the delta associated with the element.
	 */
	public double getDeltaY() {
		return template.getDeltaY()*(isVectorFlippedV()?-1:1);
	}

	/**
	 * Returns the mode of the element.
	 * @return the mode of the element.
	 * @see de.engehausen.mobile.crazygolf.Element#getType()
	 */
	public int getMode() {
		return template.getType();
	}
	
	/**
	 * Sets horizontal flip flag.
	 * @param flag the flag value
	 */
	public void setFlippedH(final boolean flag) {
		flip = 0x2&flip | (flag?1:0);
	}

	/**
	 * Sets vertical flip flag.
	 * @param flag the flag value
	 */
	public void setFlippedV(final boolean flag) {
		flip = 0x1&flip | (flag?2:0);
	}

	/**
	 * Indicates whether or not the element is flipped horizontally.
	 * @return whether or not the element is flipped horizontally.
	 */
	public boolean isFlippedH() {
		return isFlippedH(flip);
	}

	/**
	 * Indicates whether or not the element is flipped vertically.
	 * @return whether or not the element is flipped vertically.
	 */
	public boolean isFlippedV() {
		return isFlippedV(flip);
	}

	protected boolean isFlippedH(final int i) {
		return (0x1&i)==1;
	}

	protected boolean isFlippedV(final int i) {
		return (0x2&i)==2;
	}

	/**
	 * Returns the flip flags of the element.
	 * @return the flip flags of the element.
	 */
	public int getFlipFlags() {
		return flip;
	}

	/**
	 * Sets the flip flags of the element.
	 * @param flags the flip flags of the element.
	 */
	public void setFlipFlags(final int flags) {
		flip = flags;
	}

	/**
	 * Returns the flip flags for the vector of the element.
	 * @return the flip flags for the vector of the element.
	 */
	public int getVectorFlipFlags() {
		return vectorFlip;
	}

	/**
	 * Indicates whether or not the vector of the element is flipped horizontally.
	 * @return whether or not the vector of the element is flipped horizontally.
	 */
	public boolean isVectorFlippedH() {
		return isFlippedH(vectorFlip);
	}

	/**
	 * Indicates whether or not the vector of the element is flipped vertically.
	 * @return whether or not the vector of the element is flipped vertically.
	 */
	public boolean isVectorFlippedV() {
		return isFlippedV(vectorFlip);
	}

	/**
	 * Sets the vector flip flags.
	 * @param flags the vector flip flags.
	 */
	public void setVectorFlipFlags(final int flags) {
		vectorFlip = flags;
	}

	/**
	 * Sets the flip flag for horizontal flipping.
	 * @param flag the flag
	 */
	public void setVectorFlippedH(final boolean flag) {
		vectorFlip = 0x2&vectorFlip | (flag?1:0);
	}

	/**
	 * Sets the flip flag for vertical flipping.
	 * @param flag the flag
	 */
	public void setVectorFlippedV(final boolean flag) {
		vectorFlip = 0x1&vectorFlip | (flag?2:0);
	}

	/**
	 * Returns the height of the element
	 * @return the height of the element
	 */
	public int getHeight() {
		return template.getHeight();
	}

	/**
	 * Returns the width of the element
	 * @return the width of the element
	 */
	public int getWidth() {
		return template.getWidth();
	}

	/**
	 * Tests if the element can be flipped horizontally.
	 * @return <code>true</code> if the element can be flipped horizontally, <code>false</code> otherwise
	 */
	public boolean canFlipHorizontal() {
		return template.canFlipHorizontal();
	}

	/**
	 * Tests if the element can be flipped vertically.
	 * @return <code>true</code> if the element can be flipped vertically, <code>false</code> otherwise
	 */
	public boolean canFlipVertical() {
		return template.canFlipVertical();
	}

	/**
	 * Returns the name of the element.
	 * @return the name of the element.
	 */
	public String getName() {
		return template.getName();
	}

	/**
	 * Returns the type of the element; same as {@link #getMode()}
	 * @return the type of the element.
	 */
	public int getType() {
		return template.getType();
	}

	/**
	 * Returns the x-position of the element.
	 * @return the x-position of the element.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y-position of the element.
	 * @return the y-position of the element.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the x-position of the element.
	 * @param newX the x-position of the element.
	 */
	public void setX(final int newX) {
		x = newX;
	}

	/**
	 * Sets the y-position of the element.
	 * @param newY the y-position of the element.
	 */
	public void setY(final int newY) {
		y = newY;
	}
	
	/**
	 * Checks whether the element is fully contained in the given rectangle.
	 * @param sx the starting point of the rectangle
	 * @param sy the starting point of the rectangle
	 * @param ex the ending point of the rectangle
	 * @param ey the ending point of the rectangle
	 * @return <code>true</code> if the element is fully contained in the
	 * rectangle, <code>false</code> otherwise
	 */
	public boolean inRectangle(final int sx, final int sy, final int ex, final int ey) {
		if (sx <= x && sy <= y) {
			return ex>x+getWidth()&&ey>y+getHeight();
		} else {
			return false;
		}
	}
	
	/**
	 * Checks whether the given point is contained inside of
	 * the element area.
	 * @param cx the point (x)
	 * @param cy the point (y)
	 * @return <code>true</code> if the given point is contained inside
	 * of the elements' boundaries, <code>false</code> otherwise
	 */
	public boolean contains(final int cx, final int cy) {
		if (cx >= x && cy >= y) {
			return cx < x+getWidth() && cy < y+getHeight();
		} else {
			return false;
		}
	}
	
	// non-javadoc: see superclass
	public String toString() {
		final StringBuilder sb = new StringBuilder(48);
		sb.append(getName()).append('=')
		  .append(getX()).append(',')
		  .append(getY()).append(',')
		  .append(getWidth()).append(',')
		  .append(getHeight()).append(',')
		  .append(getFlipFlags()).append(',')
		  .append(getMode());
		return sb.toString();
	}

	/**
	 * Paints the element onto the given graphics.
	 * @param graphics the graphics to paint on.
	 */
	public void paint(final Graphics2D graphics) {
		final BufferedImage img = elements.getImage(template);
		AffineTransform trfrm;
		switch (flip) {
			case 1:
				trfrm = AffineTransform.getScaleInstance(-1, 1);
			    trfrm.translate(-img.getWidth(), 0);
				break;
			case 2:
				trfrm = AffineTransform.getScaleInstance(1, -1);
				trfrm.translate(0, -img.getHeight());
				break;
			case 3:
				trfrm = AffineTransform.getScaleInstance(-1, -1);
				trfrm.translate(-img.getWidth(), -img.getHeight());
				break;
			case 0:
			default:
				trfrm = null;
				break;
		}
		if (trfrm == null) {
			graphics.drawImage(img, 0, 0, null);
		} else {
			graphics.drawImage(img, trfrm, null);									
		}
	}

}
