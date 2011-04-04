package de.engehausen.mobile.crazygolf.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import de.engehausen.mobile.crazygolf.Element;

/**
 * A 18-hole course of the game.
 */
public class Course {
	
	private final String courseName;
	private final Hole[] holes;

	/**
	 * Creates a course.
	 * @param name the name of the course
	 * @param allHoles all hols of the course
	 */
	protected Course(final String name, final Hole[] allHoles) { //NOPMD direct array storage is done intentionally
		courseName = name;
		holes = allHoles;
	}

	/**
	 * Returns the name of the course.
	 * @return the name of the course.
	 */
	public String getName() {
		return courseName;
	}

	/**
	 * Returns all holes of the course.
	 * @return all holes of the course.
	 */
	public Hole[] getHoles() {
		return holes; //NOPMD exposure of internal array okay for a small game
	}

	/**
	 * Loads a course from its description, based on the given elements.
	 * @param elements the elements which are referenced in the course description.
	 * @param stream the input stream holding the course description.
	 * @return the course, never <code>null</code>.
	 * @throws IOException in case of error
	 */
	public static Course load(final Element[] elements, final InputStream stream) throws IOException {
		final StringBuffer buffer = new StringBuffer(1024);
		readLine(buffer, stream);
		return new Course(buffer.toString(), readHoles(elements, buffer, stream));
	}

	/**
	 * Loads all holes from their description, based on the given elements.
	 * @param elements the elements which are referenced in the hole description.
	 * @param sb the string buffer used for string operations
	 * @param stream the input stream holding the hole description.
	 * @return the holes, never <code>null</code>.
	 * @throws IOException in case of error
	 */
	private static Hole[] readHoles(final Element[] elements, final StringBuffer sb, final InputStream stream) throws IOException {
		final Hole[] result = new Hole[18];
		for (int i = 0; i < result.length && stream.available() > 0; i++) {
			readLine(sb, stream);
			result[i] = readHole(elements, sb, stream, Integer.parseInt(sb.toString()));
		}
		return result;
	}
	
	/**
	 * Loads a hole from its description, based on the given elements.
	 * @param elements the elements which are referenced in the hole description.
	 * @param sb the string buffer used for string operations
	 * @param stream the input stream holding the hole description.
	 * @param par the par of the hole
	 * @return the hole, never <code>null</code>.
	 * @throws IOException in case of error
	 */
	private static Hole readHole(final Element[] elements, final StringBuffer sb, final InputStream is, final int par) throws IOException {
		final Vector ops = new Vector(128);
		while (is.available()>0) {
			final int id = readInt(sb, is);
			if (id >= 0) {
				ops.addElement(new ElementOp(id,  //NOPMD
						 readInt(sb, is),
						 readInt(sb, is),
						 readInt(sb, is),
						 readInt(sb, is)));
				if (sb.charAt(0) == '\n') {
					break;
				}				
			} else {
				break;
			}
		}
		final ElementOp[] array = new ElementOp[ops.size()];
		ops.copyInto(array);
		return new Hole(elements, par, array);
	}
	
	/**
	 * Reads an int from the input stream.
	 * @param sb the string buffer to use for buffering
	 * @param is the input stream
	 * @return the int
	 * @throws IOException in case of error
	 */
	private static int readInt(final StringBuffer sb, final InputStream is) throws IOException {
		sb.setLength(0);
		while (is.available()>0) {
			final char c = (char) is.read();
			if (Character.isDigit(c)) {
				sb.append(c);
			} else {
				if (sb.length()>0) {
					final int result = Integer.parseInt(sb.toString());
					sb.setCharAt(0, c);
					return result;					
				} else {
					return -1;
				}
			}
		}
		return 0;
	}
	
	/**
	 * Reads one line from the input stream into the buffer.
	 * @param sb the string buffer to use for buffering
	 * @param is the input stream
	 * @throws IOException in case of error
	 */
	private static void readLine(final StringBuffer sb, final InputStream is) throws IOException {
		sb.setLength(0);
		while (is.available()>0) {
			final char c = (char) is.read();
			if (c == '\n') {
				break;
			} else {
				sb.append(c);
			}
		}
	}

}
