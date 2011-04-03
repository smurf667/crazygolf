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
	
	public Hole() {
		elements = new LinkedList<Element>();
		par = 3;
	}
	
	public int getPar() {
		return par;
	}

	public void setPar(final int i) {
		par = i;
	}

	public void add(final Element anElement) {
		elements.addLast(anElement);
	}

	public void remove(final Element anElement) {
		elements.remove(anElement);
	}
	
	public void toTop(final Element anElement) {
		elements.remove(anElement);
		elements.addLast(anElement);
	}

	public void toTop(final List<Element> topElements) {
		for (Element e : topElements) {
			elements.remove(e);
			elements.addLast(e);
		}
	}

	public void toBottom(final Element anElement) {
		elements.remove(anElement);
		elements.addFirst(anElement);
	}

	public void toBottom(final List<Element> bottomElements) {
		for (Element e : bottomElements) {
			elements.remove(e);
			elements.addFirst(e);
		}
	}
	
	public boolean contains(final Element e) {
		return elements.contains(e);
	}

	@Override
	public Iterator<Element> iterator() {
		return elements.iterator();
	}
	
	public String toString() {
		return elements.toString();
	}

}
