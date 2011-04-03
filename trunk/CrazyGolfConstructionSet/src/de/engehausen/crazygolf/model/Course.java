package de.engehausen.crazygolf.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Golf course with 18 holes.
 */
public class Course {
	
	private final String courseName;
	private final List<Hole> holes;

	/**
	 * Creates a new course with the given name.
	 * @param aName the name of the course, must not be <code>null</code>.
	 */
	public Course(final String aName) {
		courseName = aName;
		final List<Hole> temp = new ArrayList<Hole>(18);
		for (int i = 0; i < 18; i++) {
			temp.add(new Hole()); 
		}
		holes = Collections.unmodifiableList(temp);
	}

	public String getName() {
		return courseName;
	}
	
	public String toString() {
		final StringBuilder sb = new StringBuilder(8192);
		sb.append(courseName).append('\n');
		final int max = holes.size();
		for (int i = 0; i < max; i++) {
			if (i<9) {
				sb.append(' ');
			}
			sb.append(i+1).append('=').append(holes.get(i).toString()).append('\n');
		}
		return sb.toString();
	}
	
	public List<Hole> getHoles() {
		return holes;
	}
	
}
