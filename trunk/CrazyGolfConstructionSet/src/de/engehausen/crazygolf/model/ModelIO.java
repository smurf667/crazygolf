package de.engehausen.crazygolf.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * I/O class for reading and writing courses.
 */
public class ModelIO {
	
	private static final char NEWLINE = '\n';
	private static final char COMMA = ',';
	private static final char SEMICOLON = ';';
	private static final String COMMA_STR = ",";
	private static final String SEMICOLON_STR = ";";
	
	private final Elements elements;

	/**
	 * Creates the I/O class, which will be based on all known elements.
	 * @param allElements all known elements.
	 */
	public ModelIO(final Elements allElements) {
		elements = allElements;
	}

	/**
	 * Saves a course to the given file.
	 * @param outFile the file to save the course to.
	 * @param course the course to save
	 * @throws IOException in case of error.
	 */
	public void save(final File outFile, final Course course) throws IOException {
		final BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		try {
			writer.write(course.getName());
			writer.write(NEWLINE);
			for (Hole h : course.getHoles()) {
				save(writer, h);
			}
		} finally {
			writer.close();
		}
	}

	/**
	 * Loads a course from the given file.
	 * @param inFile the file to load the course from
	 * @return the loaded course
	 * @throws IOException in case of error
	 */
	public Course load(final File inFile) throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(inFile));
		final Course result;
		try {
			result = new Course(reader.readLine());
			load(reader, result);
		} finally {
			reader.close();
		}
		return result;
	}

	private void save(final BufferedWriter writer, final Hole hole) throws IOException {
		writer.write(Integer.toString(hole.getPar()));
		writer.write(NEWLINE);
		for (de.engehausen.crazygolf.model.Element e : hole) {
			save(writer, e);
		}
		writer.write(NEWLINE);
	}
	
	private void load(final BufferedReader reader, final Course course) throws IOException {
		for (Hole h : course.getHoles()) {
			load(reader, h);
		}
	}
	
	private void save(final BufferedWriter writer, final de.engehausen.crazygolf.model.Element e) throws IOException {
		writer.write(Integer.toString(e.getID()));
		writer.write(COMMA);
		writer.write(Integer.toString(e.getX()));
		writer.write(COMMA);
		writer.write(Integer.toString(e.getY()));
		writer.write(COMMA);
		writer.write(Integer.toString(e.getFlipFlags()));
		writer.write(COMMA);
		writer.write(Integer.toString(e.getVectorFlipFlags()));
		writer.write(SEMICOLON);
	}

	private void load(final BufferedReader reader, final Hole hole) throws IOException {
		hole.setPar(Integer.parseInt(reader.readLine()));
		final String line = reader.readLine();
		if (line.length()>0) {
			final StringTokenizer tok = new StringTokenizer(line, SEMICOLON_STR);
			while (tok.hasMoreTokens()) {
				final StringTokenizer tok2 = new StringTokenizer(tok.nextToken(), COMMA_STR);
				final int id = Integer.parseInt(tok2.nextToken());
				final de.engehausen.mobile.crazygolf.Element[] templates = elements.getAllModes(elements.getName(id));
				final de.engehausen.crazygolf.model.Element e = new de.engehausen.crazygolf.model.Element(elements, templates);
				e.setX(Integer.parseInt(tok2.nextToken()));
				e.setY(Integer.parseInt(tok2.nextToken()));
				e.setFlipFlags(Integer.parseInt(tok2.nextToken()));
				e.setVectorFlipFlags(Integer.parseInt(tok2.nextToken()));
				for (int i = templates.length-1; i>=0; i--) {
					if (templates[i] != null && templates[i].getID() == id) {
						e.setMode(i);
					}
				}
				hole.add(e);
			}
		}
	}
		
}
