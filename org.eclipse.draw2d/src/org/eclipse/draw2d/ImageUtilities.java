package org.eclipse.draw2d;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * @author Pratik Shah
 */
public class ImageUtilities {

/**
 * Returns a newly created image for the given string.  The string will be rendered using
 * the provided colors and fonts.  The caller is responsible for disposing the returned
 * Image.  Strings cannot contain newlines or tab characters.
 * @param string the String to be rendered
 * @param font the font
 * @param foreground the text's color
 * @param background the background color
 * @return an Image which must be disposed
 */
public static Image createRotatedImageOfString(
		String string,
		Font font,
		Color foreground,
		Color background) {
	Display display = Display.getCurrent();
	if (display == null)
		SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
	
	FontMetrics metrics = FigureUtilities.getFontMetrics(font);
	Dimension strSize = FigureUtilities.getStringExtents(string, font);
	strSize.width--;
	Image srcImage = new Image(display, strSize.width, metrics.getAscent());
	GC gc = new GC(srcImage);
	gc.setFont(font);
	gc.setForeground(foreground);
	gc.setBackground(background);
	gc.fillRectangle(srcImage.getBounds());
	gc.drawString(string, 0, 0 - metrics.getLeading());
	Image result = createRotatedImage(srcImage);
	gc.dispose();
	srcImage.dispose();
	return result;
}

static Image createRotatedImage(Image srcImage) {
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

	return new Image(display, destData);	
}
	
}
