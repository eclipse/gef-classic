/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.palette;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.resource.JFaceResources;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.ImageUtilities;
import org.eclipse.draw2d.geometry.Dimension;

import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Pratik Shah
 */
public class DragFigure 
	extends ImageFigure 
{

protected static final int MARGIN_SPACE = 0;
protected static final int H_GAP = 4;
protected static final int LINE_LENGTH = 30;

public DragFigure(int orientation) {
	// @TODO:Pratik  what is the banner font changes.  update?  or ignore?
	setFont(JFaceResources.getBannerFont());
	setRequestFocusEnabled(true);
	setFocusTraversable(true);
	addFocusListener(new FocusListener() {
		public void focusGained(FocusEvent fe) {
			repaint();
		}
		public void focusLost(FocusEvent fe) {
			repaint();
		}
	});
	
	Image image = createImage();
	if (orientation == SWT.VERTICAL)
		image = ImageUtilities.createRotatedImage(image);
	setImage(image);
}

protected Image createImage() {
	Image img = null;
	Dimension imageSize = FigureUtilities.getStringExtents(
			GEFMessages.Palette_Label, getFont());
	imageSize.expand(H_GAP * 2 + MARGIN_SPACE * 2 + LINE_LENGTH * 2 - 1, 
			MARGIN_SPACE * 2);
	img = new Image(null, imageSize.width, imageSize.height);
	GC gc = new GC(img);
	gc.setBackground(ColorConstants.button);
	gc.fillRectangle(0, 0, imageSize.width, imageSize.height);
	gc.setFont(getFont());
	gc.drawText(GEFMessages.Palette_Label, MARGIN_SPACE + LINE_LENGTH + H_GAP, 
			MARGIN_SPACE);
	gc.setForeground(ColorConstants.buttonLightest);
	int centerY = imageSize.height / 2;
	gc.drawLine(MARGIN_SPACE, centerY - 3, MARGIN_SPACE + LINE_LENGTH, centerY - 3);
	gc.drawLine(MARGIN_SPACE, centerY + 2, MARGIN_SPACE + LINE_LENGTH, centerY + 2);
	gc.drawLine(imageSize.width - MARGIN_SPACE - LINE_LENGTH, centerY - 3, 
			imageSize.width - MARGIN_SPACE, centerY - 3);
	gc.drawLine(imageSize.width - MARGIN_SPACE - LINE_LENGTH, centerY + 2, 
			imageSize.width - MARGIN_SPACE, centerY + 2);
	gc.setForeground(ColorConstants.buttonDarker);
	gc.drawLine(MARGIN_SPACE, centerY + 3, MARGIN_SPACE + LINE_LENGTH, centerY + 3);
	gc.drawLine(MARGIN_SPACE, centerY - 2, MARGIN_SPACE + LINE_LENGTH, centerY - 2);
	gc.drawLine(imageSize.width - MARGIN_SPACE - LINE_LENGTH, centerY - 2, 
			imageSize.width - MARGIN_SPACE, centerY - 2);
	gc.drawLine(imageSize.width - MARGIN_SPACE - LINE_LENGTH, centerY + 3, 
			imageSize.width - MARGIN_SPACE, centerY + 3);
	gc.dispose();
	return img;
}

// @TODO:Pratik   is this okay?
protected void finalize() throws Throwable {
	getImage().dispose();
}

protected void paintFigure(Graphics graphics) {
	super.paintFigure(graphics);
	if (hasFocus())
		graphics.drawFocus(0, 0, bounds.width - 1, bounds.height - 1);
}
		
}