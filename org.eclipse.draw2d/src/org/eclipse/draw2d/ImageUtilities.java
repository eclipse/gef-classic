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
 * Returns a new Image with the given String rotated left (by 90 degrees).  The String
 * will be rendered using the provided colors and fonts.  The client is responsible for
 * disposing the returned Image.  Strings cannot contain newline or tab characters.
 * 
 * @param string the String to be rendered
 * @param font the font
 * @param foreground the text's color
 * @param background the background color
 * @return an Image which must be disposed
 */
public static Image createRotatedImageOfString(String string, Font font, 
                                               Color foreground, Color background) {
	Display display = Display.getCurrent();
	if (display == null)
		SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
	
	FontMetrics metrics = FigureUtilities.getFontMetrics(font);
	Dimension strSize = FigureUtilities.getStringExtents(string, font);
	// the width is being reduced by 1 because FigureUtilties, for some reason, increases
	// it by 1
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

/**
 * Returns a new Image that is the given Image rotated left by 90 degrees.  The client is
 * responsible for disposing the returned Image.
 * <p> 
 * <b>IMPORTANT:</b> Rotating images that have padded scanlines may have undesired
 * effects.
 * 
 * @param	srcImage	the Image that is to be rotated left
 * @return	the rotated Image (the client is responsible for disposing it)
 */
public static Image createRotatedImage(Image srcImage) {
	Display display = Display.getCurrent();
	if (display == null)
		SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);

	ImageData srcData = srcImage.getImageData();
	int bytesPerPixel = srcData.bytesPerLine / srcData.width;
	int destBytesPerLine = srcData.height * bytesPerPixel;
	byte[] newData = new byte[srcData.data.length];
	for (int srcY = 0; srcY < srcData.height; srcY++) {
		for (int srcX = 0; srcX < srcData.width; srcX++) {
			int destX = srcY;
			int destY = srcData.width - srcX - 1;
			int destIndex = (destY * destBytesPerLine) + (destX * bytesPerPixel);
			int srcIndex = (srcY * srcData.bytesPerLine) + (srcX * bytesPerPixel);
			System.arraycopy(srcData.data, srcIndex, newData, destIndex, bytesPerPixel);
		}
	}
	// destBytesPerLine is used as scanlinePad to ensure that no padding is required
	return new Image(display, new ImageData(srcData.height, srcData.width, 
					srcData.depth, srcData.palette, destBytesPerLine, newData));
}

}