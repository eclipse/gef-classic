/*
 * Created on Nov 12, 2003
 */
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * @author Pratik Shah
 */
public class ImageUtilities {

public static Image getRotatedLeft(String str, Font font, 
								   Color foreground, Color background) {
	Display display = Display.getCurrent();
	if (display == null) {
		display = Display.getDefault();
	}
	FontMetrics fMetrics = FigureUtilities.getFontMetrics(font);
	Dimension strSize = FigureUtilities.getStringExtents(str, font);
	strSize.width--;
	Image srcImage = new Image(display, strSize.width, fMetrics.getAscent());
	GC gc = new GC(srcImage);
	gc.setFont(font);
	gc.setForeground(foreground);
	gc.setBackground(background);
	gc.fillRectangle(srcImage.getBounds());
	gc.drawText(str, 0, 0 - fMetrics.getLeading());
	return rotateLeft(srcImage);
}

public static Image rotateLeft(Image srcImage) {
	/*
	 * @TODO:Pratik    optimize this by directly manipulating the byte array
	 */
	Display display = Display.getCurrent();
	if (display == null) {
		display = Display.getDefault();
	}
	ImageData srcData = srcImage.getImageData();
	ImageData destData = new ImageData(srcData.height, srcData.width, srcData.depth, 
			srcData.palette); 
	for (int y = 0; y < srcData.height; y++) {
		for (int x = 0; x < srcData.width; x++) {
			destData.setPixel(y, srcData.width - x - 1,	srcData.getPixel(x, y));
		}
	}
	srcImage.dispose();
	return new Image(display, destData);	
}
	
}
