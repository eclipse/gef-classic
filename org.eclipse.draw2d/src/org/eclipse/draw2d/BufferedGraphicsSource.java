/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.internal.ImageCache;

class BufferedGraphicsSource
	implements GraphicsSource
{

private Image imageBuffer;
private GC        imageGC;
private GC      controlGC;
private Control   control;
private Rectangle inUse;

public BufferedGraphicsSource(Control c) {
	control = c;
}

public void flushGraphics(Rectangle region) {
	if (inUse.isEmpty())
		return;
	controlGC.drawImage(getImage(),
		0, 0, inUse.width, inUse.height,
		inUse.x, inUse.y, inUse.width, inUse.height);
	controlGC.dispose();
	imageGC.dispose();
	ImageCache.checkin(imageBuffer);
}

public Graphics getGraphics(Rectangle region) {
	if (control == null || control.isDisposed())
		return null;

	org.eclipse.swt.graphics.Point ptSWT = control.getSize();
	inUse = new Rectangle(0, 0, ptSWT.x, ptSWT.y);
	inUse.intersect(region);
	if (inUse.isEmpty())
		return null;
 	imageBuffer = ImageCache.checkout(inUse.getSize(), this);

	imageGC = new GC(getImage());
	controlGC = new GC(control);
	imageGC.setBackground(controlGC.getBackground());
	imageGC.setForeground(controlGC.getForeground());
	imageGC.setFont(controlGC.getFont());
	imageGC.setLineStyle(controlGC.getLineStyle());
	imageGC.setLineWidth(controlGC.getLineWidth());
	imageGC.setXORMode(controlGC.getXORMode());

	Graphics g = new SWTGraphics(imageGC);
	g.translate(inUse.getLocation().negate());
	g.setClip(region);

	g.clipRect(new Rectangle(0, 0, ptSWT.x, ptSWT.y));
	return g;
}

protected Image getImage() {
	return imageBuffer;
}

protected GC getImageGC() {
	return imageGC;
}

}