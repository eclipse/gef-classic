package org.eclipse.draw2d;

import java.util.*;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.printing.Printer;

/**
 * @author danlee
 */
public class PrinterGraphics extends ScaledGraphics {

Map imageCache = new HashMap();

Printer printer;

public PrinterGraphics(Graphics g, Printer p){
	super(g);
	printer = p;
}

Font createFont(FontData data) {
	return new Font(printer, data);
}

private Image printerImage(Image image) {
	Image result = (Image)imageCache.get(image);
	if (result != null)
		return result;
		
	result = new Image(printer, image.getImageData());
	imageCache.put(image, result);
	return result;
}

public void drawImage(Image srcImage, int x, int y) {
	super.drawImage(printerImage(srcImage), x, y);
}

public void drawImage(Image srcImage,
	int sx, int sy, int sw,int sh,
	int tx, int ty, int tw,int th) {
	super.drawImage(printerImage(srcImage), sx, sy, sw, sh, tx, ty, tw, th);
}

FontData zoomFontData(FontData data) {
	return data;
}

int zoomLineWidth(int w) {
	return (int)(w * zoom);
}

}