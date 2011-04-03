package de.engehausen.crazygolf.ui;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JWindow;
import javax.swing.Timer;

import de.engehausen.crazygolf.Editor;

public class Splash extends JWindow {

	private static final long serialVersionUID = 1L;
	
	private final BufferedImage image;

	public Splash(final Editor editor) throws IOException {
		super(new Frame());
		final Rectangle rect = editor.getBounds();
		image = ImageIO.read(getClass().getResourceAsStream("splash.png"));

		setBounds(rect.x + (rect.width - image.getWidth())/2, rect.y + (rect.height - image.getHeight())/2, image.getWidth(), image.getHeight());
		setVisible(true);

		final Timer timer = new Timer(Integer.MAX_VALUE, new ActionListener() {
	        public void actionPerformed(final ActionEvent event) {
	          ((Timer) event.getSource()).stop();
	          close();
	        };
	      });	    
	    timer.setInitialDelay(2500);
	    timer.start();
	}
	
	protected void close() {
		setVisible(false);
		dispose();
	}

	public void paint(final Graphics graphics) {
		graphics.drawImage(image, 0, 0, this);
	}
}
