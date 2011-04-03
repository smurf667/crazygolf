package de.engehausen.mobile.crazygolf;

/**
 * A graphical element. The elements' graphics data may
 * contain more "types" of the element, so the offset defines
 * which "types" of the element shall be used.
 */
public class Element {
	
	/** display type "normal" */
	public static final int TYPE_NORMAL = 0;
	/** display type "down" (moves ball "down") */
	public static final int TYPE_DOWN = 1;
	/** display type "up" (moves ball "up") */
	public static final int TYPE_UP = 2;
		
	private final String name, file;
	private final int id, type, offset, width, height;
	private final boolean hFlippable, vFlippable;
	private final double vx, vy;

	/**
	 * Creates the element.
	 * @param anID the id of the element
	 * @param aName the element name
	 * @param aFileName file holding the graphics data
	 * @param aType type
	 * @param anOffset offset in the graphics data
	 * @param aWidth the width of the element
	 * @param aHeight the height of the element
	 * @param flipsH indicates if the element can be flipped horizontally
	 * @param flipsV indicates if the element can be flipped vertically
	 * @param deltaX ball move vector (x)
	 * @param deltaY ball move vector (y)
	 */
	public Element(final int anID, final String aName, final String aFileName, final int aType, final int anOffset, 
			       final int aWidth, final int aHeight, final boolean flipsH, final boolean flipsV, final double deltaX, final double deltaY) {  //NOPMD
		id = anID;
		name = aName;
		file = aFileName;
		type = aType;
		offset = anOffset;
		width = aWidth;
		height = aHeight;
		hFlippable = flipsH;
		vFlippable = flipsV;
		vx = deltaX;
		vy = deltaY;
	}
	
	/**
	 * Human readable representation of the element.
	 * @return a human readable representation of the element.
	 */
	public String toString() {
		final StringBuffer sb = new StringBuffer(128);
		sb.append('{').append(file).append(Constants.SEPARATOR_COMMA)
		  .append(name).append(Constants.SEPARATOR_COMMA)
		  .append(type).append(Constants.SEPARATOR_COMMA)
		  .append(offset).append(Constants.SEPARATOR_COMMA)
		  .append(width).append(Constants.SEPARATOR_COMMA)
		  .append(height).append(Constants.SEPARATOR_COMMA)
		  .append(hFlippable?'1':'0').append(Constants.SEPARATOR_COMMA)
		  .append(vFlippable?'1':'0').append(Constants.SEPARATOR_COMMA)
		  .append(vx).append(Constants.SEPARATOR_COMMA)
		  .append(vy).append('}');
		return sb.toString();
	}

	/**
	 * Returns the ID of the element.
	 * @return the ID of the element.
	 */
	public int getID() {
		return id;
	}

	/**
	 * The name of the element. Several elements may
	 * have the same name (this is usual for elements of
	 * the same name but different type).
	 * @return the name of the element
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The name of the file containing the graphics data.
	 * @return the name of the file containing the graphics data.
	 */
	public String getFileName() {
		return file;
	}

	/**
	 * Returns he type of the element.
	 * @return the type of the element. One of
	 * {@link #TYPE_NORMAL}, {@link #TYPE_DOWN}, {@link #TYPE_UP}.
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Returns the offset in pixels for this element in its
	 * grapics data.
	 * @return the offset.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * The width of the element.
	 * @return the width of the element.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * The height of the element.
	 * @return the height of the element.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the x component of the ball move vector.
	 * @return the x component of the ball move vector.
	 */
	public double getDeltaX() {
		return vx;
	}

	/**
	 * Returns the y component of the ball move vector.
	 * @return the y component of the ball move vector.
	 */
	public double getDeltaY() {
		return vy;
	}

	/**
	 * Returns if the element has a non-zero ball movement vector.
	 * @return <code>true</code> if the ball movement is non-zero.
	 */
	public boolean hasDelta() {
		return vx!=0||vy!=0;
	}

	/**
	 * Returns whether or not the element can be flipped horizontally.
	 * @return <code>true</code> if the element can be flipped horizontally.
	 */
	public boolean canFlipHorizontal() {
		return hFlippable;
	}

	/**
	 * Returns whether or not the element can be flipped vertically.
	 * @return <code>true</code> if the element can be flipped vertically.
	 */
	public boolean canFlipVertical() {
		return vFlippable;
	}

}
