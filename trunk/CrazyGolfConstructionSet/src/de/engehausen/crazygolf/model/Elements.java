package de.engehausen.crazygolf.model;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import de.engehausen.crazygolf.Editor;
import de.engehausen.mobile.crazygolf.Element;

public class Elements {

	private final List<Element> elementsList;
	private final IdentityHashMap<Element, BufferedImage> elementImages;
	private final Map<String, Element[]> allModes;
	private final Map<String, String> id2name;
	private GraphicsConfiguration configuration;	
	
	public Elements(final Element[] anElementList) {
		elementsList = Collections.unmodifiableList(Arrays.asList(anElementList));
		elementImages = new IdentityHashMap<Element, BufferedImage>(anElementList.length);
		allModes = new HashMap<String, Element[]>(elementsList.size(), 1f);		
		id2name = new HashMap<String, String>(elementsList.size(), 1f);		
		for (Element e : elementsList) {
			addMapping(e);
		}
	}

	/**
	 * Returns an unmodifiable list with all elements.
	 * @return an unmodifiable list with all elements, never <code>null</code>.
	 */
	public List<Element> getAllElements() {
		return elementsList;
	}
	
	public Element[] getAllModes(final Element e) {
		return getAllModes(e.getName());
	}
	
	public Element[] getAllModes(final String name) {
		return allModes.get(name);
	}
	
	public String getName(final int id) {
		return id2name.get(Integer.toString(id));
	}

	/**
	 * Returns the image for the given element.
	 * @param e the element for which to return the image; must not be <code>null</code>.
	 * @return the image for the given element, never <code>null</code>.
	 */
	public BufferedImage getImage(final Element e) {
		BufferedImage result = elementImages.get(e);
		if (result == null) {
			try {
				result = createImage(e);
				elementImages.put(e, result);
			} catch (IOException ex) {
				result = null;
			}
		}
		return result;
	}
	
	protected GraphicsConfiguration getGraphicsConfiguration(final BufferedImage anImage) {
		if (configuration == null) {
			final Graphics2D temp = anImage.createGraphics();
			configuration = temp.getDeviceConfiguration();
			temp.dispose();
		}
		return configuration;
	}
	
	protected BufferedImage createImage(final Element e) throws IOException {
		final BufferedImage template = ImageIO.read(Editor.class.getResourceAsStream(e.getFileName()));		
		final BufferedImage result = getGraphicsConfiguration(template).createCompatibleImage(e.getWidth(), e.getHeight(), Transparency.BITMASK);
		final Graphics2D g = result.createGraphics();
		try {
			g.setComposite(AlphaComposite.Src);
			g.drawImage(template, 0, 0, e.getWidth(), e.getHeight(), e.getOffset(), 0, e.getOffset()+e.getWidth(), e.getHeight(), null);
		} finally {
			g.dispose();			
		}
		return result;
	}

	private void addMapping(final Element e) {
		final String name = e.getName();
		Element[] modes = allModes.get(name);
		if (modes == null) {
			modes = new Element[3];
			allModes.put(name, modes);
		}
		modes[e.getType()] = e;
		id2name.put(Integer.toString(e.getID()), name);
	}

}
