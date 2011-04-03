package de.engehausen.mobile.crazygolf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * Element reader for an input stream contain element descriptions.
 * The input stream content <b>must</b> follow exactly this format:
 * <code>[file],[name],[width],[height],[hflip],[vflip],[id-n],[vx-n],[vy-n],[id-d],[vx-d],[vy-d],[id-u],[vx-u],[vy-u][NEWLINE]</code>
 * All values except the first are numerical. The "flip" values can only
 * be 0 (false) or 1 (true). [id-?], [vx-?] and [vy-?] can be an empty string
 * except for id-n.
 */
public final class ElementReader {
	
	private static ElementReader instance;
	
	public static ElementReader getInstance() {
		if (instance == null) {
			return instance = new ElementReader();
		} else {
			return instance;
		}
	}

	private ElementReader() {
	}

	/**
	 * Reads the element description contained in the given input stream
	 * and returns the elements.
	 * @param inputStream input stream containing the element description, must not be <code>null</code>.
	 * @return the parsed elements, never <code>null</code>.
	 * @throws IOException
	 */
	public Element[] read(final InputStream inputStream) throws IOException {
		final StringBuffer sb = new StringBuffer(128);
		final Vector list = new Vector(140);
		while (inputStream.available()>0) {
			final String filename = readString(inputStream, sb, Constants.SEPARATOR_COMMA);
			final String name = readString(inputStream, sb, Constants.SEPARATOR_COMMA);
			final int width = readInt(inputStream, sb, Constants.SEPARATOR_COMMA);
			final int height = readInt(inputStream, sb, Constants.SEPARATOR_COMMA);
			final boolean hflip = readBoolean(inputStream, sb, Constants.SEPARATOR_COMMA);
			final boolean vflip = readBoolean(inputStream, sb, Constants.SEPARATOR_COMMA);
			for (int i = 0; i < 3; i++) {
				final int id = readInt(inputStream, sb, Constants.SEPARATOR_COMMA);
				final double vx = readDouble(inputStream, sb, Constants.SEPARATOR_COMMA);
				final double vy = readDouble(inputStream, sb, i<2?Constants.SEPARATOR_COMMA:Constants.SEPARATOR_NEWLINE);
				if (id > Integer.MIN_VALUE) {
					list.addElement(new Element(id, name, filename, i, i*width, width, height, hflip, vflip, vx, vy));
				}
			}
		}
		final Element[] result = new Element[list.size()];
		list.copyInto(result);
		return result;
	}

	/**
	 * Reads a string from the input stream into the string buffer.
	 * @param stream the stream to read from, must not be <code>null</code>.
	 * @param sb the stream to read into, must not be <code>null</code>.
	 * @param stop the stop character
	 * @return the read string, never <code>null</code>.
	 * @throws IOException in case of error
	 */
	protected String readString(final InputStream stream, final StringBuffer sb, final char stop) throws IOException {
		sb.setLength(0);
		char c;
		while (stream.available()>0 && (c = (char) stream.read()) != stop) {
			sb.append(c);
		}
		if (sb.length() == 0) {
			throw new IOException("parse error");
		}
		return sb.toString();
	}

	/**
	 * Reads an int value from the input stream (which contains it as an ASCII string).
	 * @param stream the stream to read from, must not be <code>null</code>.
	 * @param sb the stream to read into, must not be <code>null</code>.
	 * @param stop the stop character
	 * @return the read int value
	 * @throws IOException in case of error
	 */
	protected int readInt(final InputStream stream, final StringBuffer sb, final char stop) throws IOException {
		sb.setLength(0);
		char c;
		while (stream.available()>0 && (c = (char) stream.read()) != stop) {
			sb.append(c);
		}
		if (sb.length() == 0) {
			return Integer.MIN_VALUE;
		}
		try {
			return Integer.parseInt(sb.toString().trim());			
		} catch (NumberFormatException e) {
			throw new IOException(e.getMessage());  //NOPMD no exception nesting on small device
		}
	}

	/**
	 * Reads a double value from the input stream (which contains it as an ASCII string).
	 * @param stream the stream to read from, must not be <code>null</code>.
	 * @param sb the stream to read into, must not be <code>null</code>.
	 * @param stop the stop character
	 * @return the read double value
	 * @throws IOException in case of error
	 */
	protected double readDouble(final InputStream stream, final StringBuffer sb, final char stop) throws IOException {
		sb.setLength(0);
		char c;
		while (stream.available()>0 && (c = (char) stream.read()) != stop) {
			sb.append(c);
		}
		if (sb.length() == 0) {
			return 0;
		}
		try {
			return Double.parseDouble(sb.toString().trim());			
		} catch (NumberFormatException e) {
			throw new IOException(e.getMessage());  //NOPMD no exception nesting on small device
		}
	}

	/**
	 * Reads boolean from the input stream. In the stream, "0" stands
	 * for <code>false</code>, "1" for <code>true</code>.
	 * @param stream the stream to read from, must not be <code>null</code>.
	 * @param sb the stream to read into, must not be <code>null</code>.
	 * @param stop the stop character
	 * @return a boolean value
	 * @throws IOException
	 */
	protected boolean readBoolean(final InputStream stream, final StringBuffer sb, final char stop) throws IOException {
		return readInt(stream, sb, stop)>0?true:false;
	}
	
}
