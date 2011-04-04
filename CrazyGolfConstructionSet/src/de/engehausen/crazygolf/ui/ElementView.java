package de.engehausen.crazygolf.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import de.engehausen.crazygolf.event.ElementSelectionListener;
import de.engehausen.crazygolf.event.VectorDisplayListener;
import de.engehausen.crazygolf.model.Element;

/**
 * Shows an element.
 */
public class ElementView extends JPanel implements ElementSelectionListener, VectorDisplayListener {

	private static final long serialVersionUID = 1L;
	protected static final Dimension DIMENSION = new Dimension(180, 180);
	
	private final ElementPanel parent;
	private boolean showVector;

	/**
	 * Creates the view based on the given parent panel.
	 * @param aParent the parent panel.
	 */
	public ElementView(final ElementPanel aParent) {
		super();
		parent = aParent;
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(TOP_ALIGNMENT);
		showVector = true;
	}

	// non-javadoc: see interface
	public void setShowVector(final boolean flag) {
		showVector = flag;
		repaint();
	}

	@Override
	public void paint(final Graphics g) {
		g.setColor(Constants.GREEN);
		g.fillRect(0, 0, getWidth(), getHeight());
		final de.engehausen.crazygolf.model.Element current = parent.getCurrent();
		if (current != null) {
			final int dx = (getWidth()-current.getWidth())/2; 
			final int dy = (getHeight()-current.getHeight())/2; 
			g.translate(dx, dy);
			try {
				current.paint((Graphics2D) g);				
			} finally {
				g.translate(-dx, -dy);
			}
			if (showVector && current.hasDelta()) {
				g.setColor(Color.YELLOW);
				paintArrows(g, getWidth()/2, getHeight()/2, (int) (current.getDeltaX()*48), (int) (current.getDeltaY()*48), 0.4d);
			}
		}
		g.setColor(Color.WHITE);
		g.drawString("Element preview", 4, 16);
	}

    private void paintArrows(final Graphics g, final int sx, final int sy, final int vx, final int vy, final double frac){
    	for (int i = 0; i < 3; i++) {
    		final int off = (i-1)*10;
        	g.translate(off, off);
    		g.drawLine(sx, sy, sx-vx, sy-vy);
    		final int ex = sx+vx, ey = sy+vy;
        	g.drawLine(sx, sy, ex, ey);
        	g.drawLine(sx + (int)((1-frac)*vx + frac*vy), sy + (int)((1-frac)*vy - frac*vx), ex, ey);
        	g.drawLine(sx + (int)((1-frac)*vx - frac*vy), sy + (int)((1-frac)*vy + frac*vx), ex, ey);
        	g.translate(-off, -off);
    	}
    }

	@Override
	public Dimension getMaximumSize() {
		return DIMENSION;
	}

	@Override
	public Dimension getMinimumSize() {
		return DIMENSION;
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSION;
	}

	@Override
	public void elementSelected(final Element e) {
		repaint();
	}

}
