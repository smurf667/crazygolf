package de.engehausen.mobile.crazygolf.renderer;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import de.engehausen.mobile.crazygolf.Constants;
import de.engehausen.mobile.crazygolf.Element;
import de.engehausen.mobile.crazygolf.model.ElementOp;
import de.engehausen.mobile.crazygolf.model.Hole;

/**
 * The hole painter is responsible for painting an image of the hole.
 */
public class HolePainter {
	
	private static final int[] FLAG_MAP = {
		Sprite.TRANS_NONE,
		Sprite.TRANS_MIRROR,
		Sprite.TRANS_MIRROR_ROT180,
		Sprite.TRANS_ROT180
	};
	
	private final Element[] elements;
	private final int w, h;
	private final Hashtable images;

	/**
	 * Creates the hole painter.
	 * @param allElements the elements backing all hole graphics descriptions.
	 * @param width the width of the screen.
	 * @param height the height of the screen.
	 */
	public HolePainter(final Element[] allElements, final int width, final int height) { //NOPMD direct array storage is done intentionally
		elements = allElements;
		w = width;
		h = height;
		images = new Hashtable();
	}
	
	/**
	 * Returns an image for the given element.
	 * @param element the element for which to return an image.
	 * @return the image of the element.
	 */
	public Image getImage(final Element element) {
		final WeakReference ref = (WeakReference) images.get(element.getName());
		Image result = null;
		if (ref != null) {
			result = (Image) ref.get();
		}
		if (result == null) {
			result = readImage(element.getFileName());
			images.put(element.getName(), new WeakReference(result));
		}
		return result;
	}

	/**
	 * Reads the image for the given file name.
	 * @param name the file name of the image
	 * @return the image
	 */
	private Image readImage(final String name) {
		try {
			return Image.createImage(name);
		} catch (IOException e) {
			throw new IllegalStateException("cannot read "+name);
		}
	}

	/**
	 * Renders the given hole onto the given graphics.
	 * @param g the graphics to render on
	 * @param hole the hole to render.
	 */
	public void render(final Graphics g, final Hole hole) {
		g.setColor(Constants.LAWN_GREEN);
		g.fillRect(0, 0, w, h);
		final ElementOp[] operations = hole.getOperations();
		for (int i = 0; i < operations.length; i++) {
			final ElementOp op = operations[i];
			final Element e = elements[op.id];
			renderElement(g, e, op);
		}
	}
	
	/**
	 * Renders a single element.
	 * @param g the graphics to render on
	 * @param e the element to render
	 * @param op the operation to perform on the element
	 */
	public void renderElement(final Graphics g, final Element e, final ElementOp op) {
		final Image image = getImage(e);
		g.drawRegion(image, e.getOffset(), 0, e.getWidth(), e.getHeight(), FLAG_MAP[op.flags], op.x, op.y, Graphics.TOP|Graphics.LEFT);
//		if (e.hasDelta()) {
//			paintArrows(g, op.x+(e.getWidth()/2), op.y+(e.getHeight()/2), getVector(e.getDeltaX(), 1, op.vectorFlags), getVector(e.getDeltaY(), 2, op.vectorFlags), 0.2d);
//		}
	}
	
//	private int getVector(final double original, final int bit, final int flags) {
//		return (int) (((flags&bit)==bit?-32d:32d)*original);
//	}
//	
//    private void paintArrows(final Graphics g, final int sx, final int sy, final int vx, final int vy, final double frac){
//    	g.setColor(0xffff00);
//		g.drawLine(sx, sy, sx-vx, sy-vy);
//		final int ex = sx+vx, ey = sy+vy;
//    	g.drawLine(sx, sy, ex, ey);
//    	g.drawLine(sx + (int)((1-frac)*vx + frac*vy), sy + (int)((1-frac)*vy - frac*vx), ex, ey);
//    	g.drawLine(sx + (int)((1-frac)*vx - frac*vy), sy + (int)((1-frac)*vy + frac*vx), ex, ey);
//    }

}
