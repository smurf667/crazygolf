package de.engehausen.crazygolf.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A hole is part of a course; there are 18 holes in a course.
 */
public class Hole implements Iterable<Element> {
	
	private final LinkedList<Element> elements;  //NOPMD need LL here
	private int par;

	/**
	 * Creates the hole.
	 */
	public Hole() {
		elements = new LinkedList<Element>();
		par = 3;
	}

	/**
	 * Returns the par value of the hole
	 * @return the par value.
	 */
	public int getPar() {
		return par;
	}

	/**
	 * Sets the par value for the hole
	 * @param i the par value
	 */
	public void setPar(final int i) {
		par = i;
	}

	/**
	 * Adds an element to the hole
	 * @param anElement the element to add
	 */
	public void add(final Element anElement) {
		elements.addLast(anElement);
	}

	/**
	 * Removes an element from the hole
	 * @param anElement the element to remove
	 */
	public void remove(final Element anElement) {
		elements.remove(anElement);
	}

	/**
	 * Moves an element to the top layer of the hole
	 * @param anElement the element to move
	 */
	public void toTop(final Element anElement) {
		elements.remove(anElement);
		elements.addLast(anElement);
	}

	/**
	 * Moves a list of elements to the top layer of the hole.
	 * @param topElements the elements to move to the top; their internal
	 * order is preserved.
	 */
	public void toTop(final List<Element> topElements) {
		for (Element e : topElements) {
			elements.remove(e);
			elements.addLast(e);
		}
	}

	/**
	 * Moves an element to the bottom layer of the hole
	 * @param anElement the element to move to the bottom.
	 */
	public void toBottom(final Element anElement) {
		elements.remove(anElement);
		elements.addFirst(anElement);
	}

	/**
	 * Moves a list of elements to the bottom layer of the hole.
	 * @param bottomElements the elements to move to the bottom; their internal
	 * order is preserved.
	 */
	public void toBottom(final List<Element> bottomElements) {
		for (Element e : bottomElements) {
			elements.remove(e);
			elements.addFirst(e);
		}
	}

	/**
	 * Checks if the hole contains the given element.
	 * @param e the element to check
	 * @return <code>true</code> if the element is part of the hole, <code>false</code> otherwise.
	 */
	public boolean contains(final Element e) {
		return elements.contains(e);
	}

	@Override
	public Iterator<Element> iterator() {
		return elements.iterator();
	}

	// non-javadoc: see superclass
	public String toString() {
		return elements.toString();
	}

}
