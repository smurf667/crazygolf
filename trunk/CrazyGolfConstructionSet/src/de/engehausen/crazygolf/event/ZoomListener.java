package de.engehausen.crazygolf.event;

import java.awt.image.BufferedImage;

public interface ZoomListener {
	
	void zoomPositionChanges(int x, int y);
	
	void zoomContentsChanges(BufferedImage image);

}
