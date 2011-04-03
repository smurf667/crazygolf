package de.engehausen.crazygolf.model;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Element {

	private final Elements elements;
	private final de.engehausen.mobile.crazygolf.Element[] templates;
	private de.engehausen.mobile.crazygolf.Element template;
	private int x, y;
	private int flip, vectorFlip;
			
	public Element(final Elements allElements, final de.engehausen.mobile.crazygolf.Element[] aTemplates) { //NOPMD
		elements = allElements;
		templates = aTemplates;
		template = aTemplates[0];
	}
	
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
	
	public void setMode(final int type) {
		final de.engehausen.mobile.crazygolf.Element t = templates[type];
		if (t != null) {
			template = t;
		}
	}
	
	public boolean hasDelta() {
		return template.hasDelta();
	}

	public double getDeltaX() {
		return template.getDeltaX()*(isVectorFlippedH()?-1:1);
	}

	public double getDeltaY() {
		return template.getDeltaY()*(isVectorFlippedV()?-1:1);
	}

	public int getMode() {
		return template.getType();
	}
	
	public void setFlippedH(final boolean flag) {
		flip = 0x2&flip | (flag?1:0);
	}

	public void setFlippedV(final boolean flag) {
		flip = 0x1&flip | (flag?2:0);
	}

	public boolean isFlippedH() {
		return isFlippedH(flip);
	}

	public boolean isFlippedV() {
		return isFlippedV(flip);
	}

	protected boolean isFlippedH(final int i) {
		return (0x1&i)==1;
	}

	protected boolean isFlippedV(final int i) {
		return (0x2&i)==2;
	}

	public int getFlipFlags() {
		return flip;
	}

	public void setFlipFlags(final int flags) {
		flip = flags;
	}

	public int getVectorFlipFlags() {
		return vectorFlip;
	}

	public boolean isVectorFlippedH() {
		return isFlippedH(vectorFlip);
	}

	public boolean isVectorFlippedV() {
		return isFlippedV(vectorFlip);
	}

	public void setVectorFlipFlags(final int flags) {
		vectorFlip = flags;
	}

	public void setVectorFlippedH(final boolean flag) {
		vectorFlip = 0x2&vectorFlip | (flag?1:0);
	}

	public void setVectorFlippedV(final boolean flag) {
		vectorFlip = 0x1&vectorFlip | (flag?2:0);
	}

	public int getHeight() {
		return template.getHeight();
	}

	public boolean canFlipHorizontal() {
		return template.canFlipHorizontal();
	}

	public boolean canFlipVertical() {
		return template.canFlipVertical();
	}

	public String getName() {
		return template.getName();
	}

	public int getType() {
		return template.getType();
	}

	public int getWidth() {
		return template.getWidth();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(final int newX) {
		x = newX;
	}

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
