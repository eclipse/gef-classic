package org.eclipse.gef.ui.palette;

import org.eclipse.gef.internal.Internal;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

public class ImageConstants {

public static final Image up, upPressed, upGrayed, down, downPressed, downGrayed;
static ImageData imageData, sourceImageData;
static private RGB[] palette2 = new RGB[]{ViewForm.borderOutsideRGB, new RGB(0,0,0), new RGB(0,0,0), new RGB(0,0,0)};
static private RGB[] palette1 = new RGB[]{ViewForm.borderOutsideRGB, new RGB(0,0,0)};

static {
	ImageData image = new ImageData(Internal.class.getResourceAsStream("icons/down.gif"));//$NON-NLS-1$
	down = convert(image, palette2);

	image = new ImageData(Internal.class.getResourceAsStream("icons/up.gif"));//$NON-NLS-1$
	up = convert(image, palette2);

	upPressed = new Image(null, Internal.class.getResourceAsStream("icons/uppressed.gif"));//$NON-NLS-1$
	downPressed = new Image(null, Internal.class.getResourceAsStream("icons/downpressed.gif"));//$NON-NLS-1$

	image = new ImageData(Internal.class.getResourceAsStream("icons/upgray.gif"));//$NON-NLS-1$
	upGrayed = convert(image, palette1);

	image = new ImageData(Internal.class.getResourceAsStream("icons/downgray.gif"));//$NON-NLS-1$
	downGrayed = convert(image, palette1);
}

static private Image convert(ImageData imageData, RGB[] colors){
	imageData.palette = new PaletteData(colors);
	return new Image(Display.getCurrent(), imageData);
}

}