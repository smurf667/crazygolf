package de.engehausen.crazygolf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

// TODO deleteme
public class Migrator {
		
	private static Map<Integer, String> buildOldIDs(final String name) throws IOException {
		final InputStream is = Migrator.class.getResourceAsStream(name);
		final BufferedReader rdr = new BufferedReader(new InputStreamReader(is));
		String line;
		int i = 0;
		final Map<Integer, String> result = new HashMap<Integer, String>();
		while ( (line=rdr.readLine()) != null) {
			result.put(Integer.valueOf(i++), line.trim());
		}
		rdr.close();
		return result;
	}

	private static Map<String, Integer> buildIDs(final String name) throws IOException {
		final InputStream is = Migrator.class.getResourceAsStream(name);
		final BufferedReader rdr = new BufferedReader(new InputStreamReader(is));
		String line;
		int i = 0;
		final Map<String, Integer> result = new HashMap<String, Integer>();
		while ( (line=rdr.readLine()) != null) {
			result.put(line.trim(), Integer.valueOf(i++));
		}
		rdr.close();
		return result;
	}

	private static void convert(final Map<Integer, String> idold, final Map<String, Integer> idnew, final String course, final File out) throws IOException {
		final BufferedWriter writer = new BufferedWriter(new FileWriter(out));
		final InputStream is = Migrator.class.getResourceAsStream(course);
		final BufferedReader rdr = new BufferedReader(new InputStreamReader(is));
		String line;
		int counter = 0;
		while ( (line=rdr.readLine()) != null) {
			counter++;
			if (counter < 3 || (counter%2==0)) {
				writer.write(line);
				writer.write("\n");
			} else {
				final StringTokenizer tok1 = new StringTokenizer(line, ";");
				while (tok1.hasMoreElements()) {
					final StringTokenizer tok2 = new StringTokenizer(tok1.nextToken(), ",");
					int idx = Integer.parseInt(tok2.nextToken());
					final Integer i = idnew.get(idold.get(Integer.valueOf(idx)));
					if (i != null) {
						idx = i.intValue();
					}
					writer.write(Integer.toString(i));
					while (tok2.hasMoreTokens()) {
						writer.write(",");
						writer.write(tok2.nextToken());
					}
					writer.write(";");
				}
				writer.write("\n");
			}
		}
		
		writer.close();
	}

	public static void main(final String[] args) throws IOException {
		final String oldElements = "/elements.txt.old";
		final String newElements = "/elements.txt";
		convert(buildOldIDs(oldElements), buildIDs(newElements), "/00.crs", new File("c:\\temp\\00.crs"));
	}

}
