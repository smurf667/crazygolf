package de.engehausen.crazygolf.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import de.engehausen.crazygolf.event.ElementModificationListener;
import de.engehausen.crazygolf.event.ElementSelectionListener;
import de.engehausen.crazygolf.event.ModificationActionHandler;
import de.engehausen.crazygolf.event.PlaceActionHandler;
import de.engehausen.crazygolf.event.VectorDisplayListener;
import de.engehausen.crazygolf.event.ZoomListener;
import de.engehausen.crazygolf.model.Element;
import de.engehausen.crazygolf.model.Hole;

public class GamePanel extends JPanel implements ElementModificationListener, MouseListener, MouseMotionListener, PlaceActionHandler, KeyListener, ModificationActionHandler, VectorDisplayListener {

	private static final long serialVersionUID = 1L;
	protected static final Dimension GAME_DIMENSIONS = new Dimension(320, 480);
	private final static Stroke SELECT_STROKE = new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] {10f, 6f}, 0f); 
	private final static Stroke SELECTION_STROKE = new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] {1f, 2f, 1f, 3f}, 0f); 
	private final static int ZOOM_WIDTH = ElementView.DIMENSION.width/2;
	private final static int ZOOM_HEIGHT = ElementView.DIMENSION.height/2;
	
	private Hole hole;
	private BufferedImage buffer;
	private Element placingElement;
	private boolean showVectors;
	private int selx1, sely1, selx2, sely2;
	private final List<Element> selection;
	private final List<ElementModificationListener> modificationListeners;
	private final List<ElementSelectionListener> elementSelectionListeners;
	private final List<ZoomListener> zoomListeners;
	
	public GamePanel() {
		super();
		selection = new ArrayList<Element>(16);
		modificationListeners = new ArrayList<ElementModificationListener>(2);
		elementSelectionListeners = new ArrayList<ElementSelectionListener>(2);
		zoomListeners = new ArrayList<ZoomListener>(2);
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(TOP_ALIGNMENT);
		setPreferredSize(GAME_DIMENSIONS);
		setMinimumSize(GAME_DIMENSIONS);
		setMaximumSize(GAME_DIMENSIONS);
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		addKeyListener(this);
		resetSelectionRect();
		showVectors = true;
	}

	/**
	 * Adds the given listener to the panel.
	 * @param aListener the listener, must not be <code>null</code>
	 */
	public void addZoomListener(final ZoomListener aListener) {
		zoomListeners.add(aListener);
	}

	/**
	 * Removes the given listener from the panel.
	 * @param aListener the listener, must not be <code>null</code>
	 */
	public void removeZoomListener(final ZoomListener aListener) {
		zoomListeners.remove(aListener);
	}

	/**
	 * Adds the given listener to the panel.
	 * @param aListener the listener, must not be <code>null</code>
	 */
	public void addModificationListener(final ElementModificationListener aListener) {
		modificationListeners.add(aListener);
	}

	/**
	 * Removes the given listener from the panel.
	 * @param aListener the listener, must not be <code>null</code>
	 */
	public void removeModificationListener(final ElementModificationListener aListener) {
		modificationListeners.remove(aListener);
	}

	/**
	 * Adds the given listener to the panel.
	 * @param aListener the listener, must not be <code>null</code>
	 */
	public void addSelectionListener(final ElementSelectionListener aListener) {
		elementSelectionListeners.add(aListener);
	}

	/**
	 * Removes the given listener from the panel.
	 * @param aListener the listener, must not be <code>null</code>
	 */
	public void removeSelectionListener(final ElementSelectionListener aListener) {
		elementSelectionListeners.remove(aListener);
	}

	@Override
	public void paint(final Graphics g) {
		if (buffer == null) {
			repaintBuffer();
		}
		g.drawImage(buffer, 0, 0, null);
		if (placingElement != null) {  //NOPMD my eyes would hurt if i switched this around, i suffer the negation here ;-)
			final int x = placingElement.getX();
			final int y = placingElement.getY();
			g.translate(x, y);
			placingElement.paint((Graphics2D) g);
			g.translate(-x, -y);
		} else {
			if (selx1 > -1) {
				final Graphics2D g2d = (Graphics2D) g;
				g2d.setColor(Color.WHITE);
				g2d.setStroke(SELECT_STROKE);
				final int x = Math.min(selx1, selx2);
				final int y = Math.min(sely1, sely2);
				final int w = Math.abs(selx1-selx2);
				final int h = Math.abs(sely1-sely2);
				g.drawRect(x, y, w, h);
				if (w > ZOOM_WIDTH && h > ZOOM_HEIGHT) {
					g2d.setStroke(SELECTION_STROKE);
					g2d.setColor(Color.RED);
					g2d.drawRect(x+(w-ZOOM_WIDTH)/2, y+(h-ZOOM_HEIGHT)/2, ZOOM_WIDTH, ZOOM_HEIGHT);
				}
			} else if (!selection.isEmpty()) {
				g.setColor(Color.WHITE);
				((Graphics2D) g).setStroke(SELECTION_STROKE);
				for (Element e : selection) {
					g.drawRect(e.getX(), e.getY(), e.getWidth()-1, e.getHeight()-1);
				}
			}
		}
	}

	@Override
	public void elementModified(final Element element) {
		if (hole != null && hole.contains(element)) {
			repaintBuffer();
			repaint();
		}
	}

	public Hole getHole() {
		return hole;
	}

	public void setHole(final Hole aHole) {
		hole = aHole;
		resetSelectionRect();
		repaintBuffer();
		repaint();
	}
	
	@Override
	public void mouseClicked(final MouseEvent mouseevent) {
		// ignore
	}

	@Override
	public void mouseEntered(final MouseEvent mouseevent) {
		// ignore
	}

	@Override
	public void mouseExited(final MouseEvent mouseevent) {
		// ignore
	}

	@Override
	public void mousePressed(final MouseEvent mouseevent) {
		if (placingElement != null && hole != null) { //NOPMD
			hole.add(placingElement);
			setPlacingElement(null);
			selection.clear();
			resetSelectionRect();
			repaintBuffer();
			repaint();
		} else {
			selx2 = selx1 = mouseevent.getX();
			sely2 = sely1 = mouseevent.getY();
		}
	}

	@Override
	public void mouseReleased(final MouseEvent mouseevent) {
		if (placingElement == null && hole != null && selx1 > -1) {
			selx2 = mouseevent.getX();
			sely2 = mouseevent.getY();
			if (selx1 == selx2 && sely1 == sely2) {
				for (Element e : hole) {
					if (e.contains(selx1, sely1)) {
						if (!selection.contains(e)) {
							selection.clear();
							selection.add(e);
							resetSelectionRect();
							repaint();
							requestFocus();
							notifySelected(e);
							return;
						}
					}
				}				
				selection.clear();
				resetSelectionRect();
				repaint();
			} else {				
				final int sx = Math.min(selx1, selx2);
				final int sy = Math.min(sely1, sely2);
				final int ex = sx+Math.abs(selx1-selx2);
				final int ey = sy+Math.abs(sely1-sely2);
				selection.clear();
				for (Element e : hole) {
					if (e.inRectangle(sx, sy, ex, ey)) {
						selection.add(e);
					}
				}
				resetSelectionRect();
				repaint();
				requestFocus();
				final int w = ex-sx, h = ey-sy;
				if (w > ZOOM_WIDTH && h > ZOOM_HEIGHT) {
					for (ZoomListener z : zoomListeners) {
						z.zoomPositionChanges(sx+(w-ZOOM_WIDTH)/2, sy+(h-ZOOM_HEIGHT)/2);
					}
				}
			}
		}
	}

	@Override
	public void mouseDragged(final MouseEvent mouseevent) {
		selx2 = mouseevent.getX();
		sely2 = mouseevent.getY();
		repaint();
	}

	@Override
	public void mouseMoved(final MouseEvent mouseevent) {
		if (placingElement != null && hole != null) {
			placingElement.setX(mouseevent.getX());
			placingElement.setY(mouseevent.getY());
			notifyModified(placingElement);
			repaint(); 
		}
	}
	
	public void setPlacingElement(final Element anElement) {
		if (hole != null) {
			placingElement = anElement;
			resetSelectionRect();			
		}
	}
	
	@Override
	public void keyPressed(final KeyEvent keyevent) {
		if (hole != null && !selection.isEmpty()) {
			switch (keyevent.getKeyCode()) {
				case KeyEvent.VK_DELETE:
					for (Element e : selection) {
						hole.remove(e);					
					}				
					selection.clear();
					repaintBuffer();
					repaint();				
					break;
				case KeyEvent.VK_UP:
					move(Direction.UP);
					break;
				case KeyEvent.VK_DOWN:
					move(Direction.DOWN);
					break;
				case KeyEvent.VK_LEFT:
					move(Direction.LEFT);
					break;
				case KeyEvent.VK_RIGHT:
					move(Direction.RIGHT);
					break;
				case KeyEvent.VK_HOME:
					toTop();
					break;
				case KeyEvent.VK_END:
					toBottom();
					break;
				case KeyEvent.VK_N:
					cycleSelection();
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void keyReleased(final KeyEvent keyevent) {
		// ignore
	}

	@Override
	public void keyTyped(final KeyEvent keyevent) {
		// ignore
	}

	@Override
	public void align(final Boundary boundary) {
		if (!selection.isEmpty()) {
			if (selection.size() == 1) {
				align(boundary, selection.get(0), 0, 0, GAME_DIMENSIONS.height, GAME_DIMENSIONS.width);
			} else {
				alignRelative(boundary);				
			}
			notifyModified(selection);
			repaintBuffer();
			repaint();
		}
	}

	@Override
	public void center(final Orientation orientation) {
		if (!selection.isEmpty()) {
			if (selection.size() == 1) {
				center(orientation, selection.get(0), 0, 0, GAME_DIMENSIONS.width, GAME_DIMENSIONS.height);
			} else {
				centerRelative(orientation);				
			}
			notifyModified(selection);
			repaintBuffer();
			repaint();
		}
	}

	@Override
	public void move(final Direction direction) {
		if (!selection.isEmpty()) {
			for (Element e : selection) {
				e.setX(e.getX()+direction.getDeltaX());
				e.setY(e.getY()+direction.getDeltaY());
			}			
			notifyModified(selection);
			repaintBuffer();
			repaint();
		}
	}

	@Override
	public void toBottom() {
		if (!selection.isEmpty() && hole != null) {
			hole.toBottom(selection);
			repaintBuffer();
			repaint();
		}
	}

	@Override
	public void toTop() {
		if (!selection.isEmpty() && hole != null) {
			hole.toTop(selection);			
			repaintBuffer();
			repaint();
		}
	}

	@Override
	public void setShowVector(final boolean flag) {
		showVectors = flag;
		repaintBuffer();
		repaint();
	}

	protected void align(final Boundary b, final Element e, final int top, final int left, final int bottom, final int right) {
		if (b == Boundary.TOP) {
			e.setY(top);
		} else if (b == Boundary.BOTTOM) {
			e.setY(bottom-e.getHeight());
		} else if (b == Boundary.LEFT) {
			e.setX(left);
		} else if (b == Boundary.RIGHT) {
			e.setX(right-e.getWidth());			
		}
	}
	
	/**
	 * Aligns all selected elements relative to the first element
	 * of the selection.
	 * @param b the boundary to align on
	 */
	protected void alignRelative(final Boundary b) {
		final Element reference = selection.get(0);
		final int top = reference.getY();
		final int bottom = reference.getY()+reference.getHeight();
		final int left = reference.getX();
		final int right = reference.getX()+reference.getWidth();
		for (int i = selection.size()-1; i>0; i--) {
			align(b, selection.get(i), top, left, bottom, right);
		}
	}

	protected void center(final Orientation o, final Element e, final int x, final int y, final int width, final int height) {
		if (o == Orientation.HORIZONTAL) {
			final int cx = width/2+x;
			e.setX(cx-e.getWidth()/2);
		} else if (o == Orientation.VERTICAL) {
			final int cy = height/2+y;
			e.setY(cy-e.getHeight()/2);
		}
	}

	/**
	 * Centers all selected elements relative to the first element
	 * of the selection.
	 * @param b the boundary to align on
	 */
	protected void centerRelative(final Orientation o) {
		final Element reference = selection.get(0);
		final int x = reference.getX();
		final int y = reference.getY();
		final int width = reference.getWidth();
		final int height = reference.getHeight();
		for (int i = selection.size()-1; i>0; i--) {
			center(o, selection.get(i), x, y, width, height);
		}
	}

	protected void notifyModified(final List<Element> elements) {
		for (Element e : elements) {
			notifyModified(e);
		}		
	}

	protected void notifyModified(final Element element) {
		for (ElementModificationListener listener : modificationListeners) {
			listener.elementModified(element);
		}		
	}

	protected void notifySelected(final Element element) {
		for (ElementSelectionListener listener : elementSelectionListeners) {
			listener.elementSelected(element);
		}		
	}

	private void resetSelectionRect() {
		selx1 = selx2 = sely1 = sely2 = -1;		
	}

	protected void repaintBuffer() {
		if (buffer == null) {
			buffer = getGraphicsConfiguration().createCompatibleImage(GAME_DIMENSIONS.width, GAME_DIMENSIONS.height);
		}		
		final Graphics2D g = (Graphics2D) buffer.getGraphics();
		try {
			if (hole != null) {
				g.setColor(Constants.GREEN);
				g.fillRect(0, 0, GAME_DIMENSIONS.width, GAME_DIMENSIONS.height);
				for (Element e : hole) {
					final int x = e.getX();
					final int y = e.getY();
					g.translate(x, y);
					e.paint(g);
					if (showVectors && e.hasDelta()) {
						paintArrow(g, e, 0.3d);
					}
					g.translate(-x, -y);
				}
			} else {
				g.setColor(Color.GRAY);
				g.fillRect(0, 0, GAME_DIMENSIONS.width, GAME_DIMENSIONS.height);
				g.setColor(Color.WHITE);
				g.drawString("create or load a course...", 16, 32);
			}
		} finally {
			g.dispose();
		}
		for (ZoomListener z : zoomListeners) {
			z.zoomContentsChanges(buffer);
		}
	}

    private void paintArrow(final Graphics g, final Element e, final double frac){
    	g.setColor(Color.YELLOW);
    	final int sx = e.getWidth()/2;
    	final int sy = e.getHeight()/2;
    	final int vx = (int) (32*e.getDeltaX());
    	final int vy = (int) (32*e.getDeltaY());
		g.drawLine(sx, sy, sx-vx, sy-vy);
		final int ex = sx+vx, ey = sy+vy;
    	g.drawLine(sx, sy, ex, ey);
    	g.drawLine(sx + (int)((1-frac)*vx + frac*vy), sy + (int)((1-frac)*vy - frac*vx), ex, ey);
    	g.drawLine(sx + (int)((1-frac)*vx - frac*vy), sy + (int)((1-frac)*vy + frac*vx), ex, ey);
    }

	protected void cycleSelection() {
		if (hole != null && selection != null && selection.size()==1) {
			final Element e = selection.get(0);
			final Iterator<Element> iterator = hole.iterator();
			while (iterator.hasNext()) {
				if (iterator.next() == e) { //NOPMD == intended
					final Element next;
					if (iterator.hasNext()) {
						next = iterator.next();
					} else {
						next = hole.iterator().next();
					}
					selection.clear();
					selection.add(next);
					resetSelectionRect();
					repaint();
					requestFocus();
					notifySelected(next);
					break;
				}
			}
		}
	}

}
