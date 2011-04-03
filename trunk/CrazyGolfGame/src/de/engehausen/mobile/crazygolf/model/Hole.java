/**
 * 
 */
package de.engehausen.mobile.crazygolf.model;

import java.util.Vector;

import de.engehausen.mobile.crazygolf.Element;

/**
 * Defines a hole of a course.
 */
public class Hole {
	
	private final int par;
	private final ElementOp[] operations;
	private final Rectangle[] startZones, holes;
	private final VectorField[] ups, downs;

	/**
	 * Creates the hole.
	 * @param elements the elements to use for building the hole
	 * @param parValue the par of the hole
	 * @param ops the operations on the elements which make up the graphics of the course
	 */
	public Hole(final Element[] elements, final int parValue, final ElementOp[] ops) { //NOPMD direct array storage is done intentionally
		par = parValue;
		operations = ops;
		startZones = determineStartZones(elements, ops);
		holes = determineHoles(elements, ops);
		final Vector upVectors = new Vector(32), downVectors = new Vector(32);
		determineVectors(elements, ops, upVectors, downVectors);
		ups = toVectorField(upVectors);
		downs = toVectorField(downVectors);
	}

	/**
	 * Computes the start zones of the hole.
	 * @param elements the elements backing the hole
	 * @param ops the operations making up the graphics of the hole
	 * @return an array with rectangles describing the possible start zones.
	 */
	private Rectangle[] determineStartZones(final Element[] elements, final ElementOp[] ops) {
		final Vector temp = new Vector(3);
		for (int i = ops.length; i-- > 0; ) {
			final Element e = elements[ops[i].id];
			if (e.getName().endsWith("start")) {
				temp.addElement(new Rectangle(ops[i].id, ops[i].x, ops[i].y, ops[i].flags, ops[i].vectorFlags, e.getWidth(), e.getHeight())); //NOPMD
			}
		}
		final Rectangle[] array = new Rectangle[temp.size()];
		temp.copyInto(array);
		return array;
	}
	
	/**
	 * Computes the holes the ball can fall into in this "hole".
	 * @param elements the elements backing the hole
	 * @param ops the operations making up the graphics of the hole
	 * @return an array with rectangles describing the holes of the "hole".
	 */
	private Rectangle[] determineHoles(final Element[] elements, final ElementOp[] ops) {
		final Vector temp = new Vector(2);
		for (int i = ops.length; i-- > 0; ) {
			final Element e = elements[ops[i].id];
			if (e.getName().equals("hole")) {
				temp.addElement(new Rectangle(ops[i].id, ops[i].x, ops[i].y, ops[i].flags, ops[i].vectorFlags, e.getWidth(), e.getHeight()));  //NOPMD
			}
		}
		final Rectangle[] array = new Rectangle[temp.size()];
		temp.copyInto(array);
		return array;
	}

	/**
	 * Computes the move vectors (i.e. the up/down areas) on the hole
	 * @param elements the elements backing the hole
	 * @param ops the operations making up the graphics of the hole
	 */
	private void determineVectors(final Element[] elements, final ElementOp[] ops, final Vector up, final Vector down) { //NOPMD
		for (int i = ops.length; i-- > 0; ) {
			final Element e = elements[ops[i].id];
			if (e.hasDelta()) {
				final int type = e.getType();
				final String name = e.getName();
				final Vector target;
				if (type == Element.TYPE_UP || name.indexOf("up")>0) {
					target = up;
				} else if (type == Element.TYPE_DOWN || name.indexOf("down")>0) {
					target = down;
				} else {
					target = null;
				}
				if (target != null) {
					target.addElement(new VectorField(ops[i].id, ops[i].x, ops[i].y, ops[i].flags, ops[i].vectorFlags, e.getWidth(), e.getHeight(), e.getDeltaX(), e.getDeltaY())); //NOPMD
				}
			}
		}
	}
	
	/**
	 * Returns the par of the hole.
	 * @return the par of the hole.
	 */
	public int getPar() {
		return par;
	}

	/**
	 * Returns the operations defining the graphics of the hole.
	 * @return the operations defining the graphics of the hole.
	 */
	public ElementOp[] getOperations() {
		return operations; //NOPMD exposure of internal array okay for a small game
	}

	/**
	 * Returns the start zones of the hole.
	 * @return the start zones of the hole.
	 */
	public Rectangle[] getStartZones() {
		return startZones; //NOPMD exposure of internal array okay for a small game
	}

	/**
	 * Returns the holes of the "hole".
	 * @return the holes of the "hole".
	 */
	public Rectangle[] getHoles() {
		return holes;
	}

	/**
	 * Finds the vector field applicable at the given coordinates.
	 * @param x the x-position
	 * @param y the y-position
	 * @param type the type of field (up/down)
	 * @return the field, or <code>null</code>.
	 */
	public VectorField findVectorField(final int x, final int y, final int type) {
		if (type == Element.TYPE_DOWN) {
			return findVectorField(downs, x, y);
		} else {
			return findVectorField(ups, x, y);			
		}
	}

	/**
	 * Finds the vector field the given coordinates reside in.
	 * @param field the fields to check
	 * @param x the x-position
	 * @param y the y-position
	 * @return the field, or <code>null</code>.
	 */
	private VectorField findVectorField(final VectorField[] field, final int x, final int y) {
		for (int i = field.length; i-->0; ) {
			if (field[i].contains(x, y)) {
				return field[i];
			}
		}
		return null;
	}

	/**
	 * Creates an array of vector fields.
	 * @param v the list of vector fields to turn into an array
	 * @return an array of vector fields.
	 */
	private VectorField[] toVectorField(final Vector v) { //NOPMD
		final VectorField[] array = new VectorField[v.size()];
		v.copyInto(array);
		return array;		
	}

}