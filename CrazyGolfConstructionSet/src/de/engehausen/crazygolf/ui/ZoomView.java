package de.engehausen.crazygolf.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import de.engehausen.crazygolf.event.ZoomListener;

/**
 * A view that shows a portion of a hole zoomed.
 */
public class ZoomView extends JPanel implements ZoomListener, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	
	private static final Dimension ZOOM_DIMENSION = new Dimension(ElementView.DIMENSION.width/2, ElementView.DIMENSION.height/2);
	
	private BufferedImage buffer;
	private int x, y;

	/**
	 * Creates the zoom view.
	 */
	public ZoomView() {
		super();
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(TOP_ALIGNMENT);
		setPreferredSize(ElementView.DIMENSION);
		setMinimumSize(ElementView.DIMENSION);
		setMaximumSize(ElementView.DIMENSION);
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
	}

	@Override
	public void paint(final Graphics g) {
		final Dimension dimensions = ElementView.DIMENSION;
		if (buffer == null) {
			g.setColor(Constants.GREEN);
			g.fillRect(0, 0, dimensions.width, dimensions.height);
		} else {
			g.drawImage(buffer, 0, 0, dimensions.width, dimensions.height, x, y, x+dimensions.width/2, y+dimensions.height/2, null);
		}
		g.setColor(Constants.DARK_GREEN);
		g.drawRect(0, 0, dimensions.width-1, dimensions.height-1);
		g.setColor(Color.WHITE);
		g.drawString("Zoom", 4, 16);
	}

	@Override
	public void zoomContentsChanges(final BufferedImage image) {
		buffer = image;
		repaint();
	}

	@Override
	public void zoomPositionChanges(final int xPos, final int yPos) {
		x = Math.min(xPos, GamePanel.GAME_DIMENSIONS.width-ZOOM_DIMENSION.width);
		y = Math.min(yPos, GamePanel.GAME_DIMENSIONS.height-ZOOM_DIMENSION.height);		
		repaint();
	}

	@Override
	public void keyPressed(final KeyEvent keyevent) {
		if (buffer != null) {
			switch (keyevent.getKeyCode()) {
				case KeyEvent.VK_UP:
					zoomPositionChanges(x, y-2);
					break;
				case KeyEvent.VK_DOWN:
					zoomPositionChanges(x, y+2);
					break;
				case KeyEvent.VK_LEFT:
					zoomPositionChanges(x-2, y);
					break;
				case KeyEvent.VK_RIGHT:
					zoomPositionChanges(x+2, y);
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
	public void mouseClicked(final MouseEvent mouseevent) {
		// do nothing
	}

	@Override
	public void mouseEntered(final MouseEvent mouseevent) {
		// do nothing
	}

	@Override
	public void mouseExited(final MouseEvent mouseevent) {
		// do nothing
	}

	@Override
	public void mousePressed(final MouseEvent mouseevent) {
		// do nothing
	}

	@Override
	public void mouseReleased(final MouseEvent mouseevent) {
		requestFocus();
	}

}
