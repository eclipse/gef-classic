/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;

import org.eclipse.draw2d.geometry.Rectangle;

class BufferedGraphicsSource implements GraphicsSource {

private Image imageBuffer;
private GC imageGC;
private GC controlGC;
private Control control;
private Rectangle inUse;

public BufferedGraphicsSource(Control c) {
	control = c;
}

public void flushGraphics(Rectangle region) {
	if (inUse.isEmpty())
		return;
	/*
	 * The imageBuffer may be null if double-buffering was not successful.
	 */
	if (imageBuffer != null) {
		controlGC.drawImage(getImage(),
				0, 0, inUse.width, inUse.height,
				inUse.x, inUse.y, inUse.width, inUse.height);
		imageGC.dispose();
		imageBuffer.dispose();
		imageBuffer = null;
		imageGC = null;
	}
	controlGC.dispose();
	controlGC = null;
}

public Graphics getGraphics(Rectangle region) {
	if (control == null || control.isDisposed())
		return null;

	org.eclipse.swt.graphics.Point ptSWT = control.getSize();
	inUse = new Rectangle(0, 0, ptSWT.x, ptSWT.y);
	inUse.intersect(region);
	if (inUse.isEmpty())
		return null;
	
	/**
	 * Bugzilla 53632 - Attempts to create large images on some platforms will fail.
	 * When this happens, do not use double-buffering for painting.
	 */
	try {
		imageBuffer = new Image(null, inUse.width, inUse.height);
	} catch (SWTError noMoreHandles) {
		imageBuffer = null;
	} catch (IllegalArgumentException tooBig) {
		imageBuffer = null;
	}

	controlGC = new GC(control);
	Graphics graphics;
	if (imageBuffer != null) {
		imageGC = new GC(imageBuffer);
		imageGC.setBackground(controlGC.getBackground());
		imageGC.setForeground(controlGC.getForeground());
		imageGC.setFont(controlGC.getFont());
		imageGC.setLineStyle(controlGC.getLineStyle());
		imageGC.setLineWidth(controlGC.getLineWidth());
		imageGC.setXORMode(controlGC.getXORMode());
		graphics = new SWTGraphics(imageGC);
		graphics.translate(inUse.getLocation().negate());
	} else {
		graphics = new SWTGraphics(controlGC);
	}

	graphics.setClip(region);
	graphics.clipRect(new Rectangle(0, 0, ptSWT.x, ptSWT.y));
	return graphics;
}

protected Image getImage() {
	return imageBuffer;
}

protected GC getImageGC() {
	return imageGC;
}

}