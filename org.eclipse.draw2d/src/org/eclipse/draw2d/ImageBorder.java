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
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.draw2d.geometry.Insets;

/**
 * @author Pratik Shah
 */
public class ImageBorder 
	extends AbstractBorder 
{
	
/*
 * @TODO:Pratik			Need to test this class extensively
 */

private Insets imgInsets, origInsets;
private Image image;
private int position;
	
public ImageBorder(int padding) {
	this(padding, padding, padding, padding);
}

public ImageBorder(int t, int l, int b, int r) {
	origInsets = new Insets(t, l, b, r);
	imgInsets = origInsets;
}
	
public Insets getInsets(IFigure figure) {
	return imgInsets;
}

public Image getImage() {
	return image;
}

public void paint(IFigure figure, Graphics graphics, Insets insets) {
	if (getImage() == null)
		return;
	
	int x = 0, y = 0;
	org.eclipse.draw2d.geometry.Rectangle figBounds = figure.getBounds();
	switch (position) {
		case PositionConstants.EAST :
			x = figBounds.right() - imgInsets.right + origInsets.right;
			y = figBounds.y + origInsets.top;
			break;
		case PositionConstants.WEST :
			x = figBounds.x + origInsets.left;
			y = figBounds.y + origInsets.top;
			break;
		case PositionConstants.NORTH :
			x = (figBounds.width - image.getBounds().width) / 2;
			x = Math.max(x, 0);
			x += figBounds.x + origInsets.left;
			y = figBounds.y + origInsets.top;
			break;
		case PositionConstants.SOUTH :
			x = (figBounds.width - image.getBounds().width) / 2;
			x = Math.max(x, 0);
			x += figBounds.x + origInsets.left;
			y = figBounds.bottom() - imgInsets.bottom + origInsets.bottom;		
	}
	graphics.drawImage(getImage(), x, y);
}

// EAST and WEST are top-aligned; NORTH and SOUTH are center-aligned
public void setImage(Image img, int position) {
	image = img;
	this.position = position;
	if (image != null) {
		Rectangle imgSize = image.getBounds();
		imgInsets = new Insets(origInsets);
		switch (position) {
			case PositionConstants.WEST :
				imgInsets.left += imgInsets.left + imgSize.width;
				break;
			case PositionConstants.EAST :
				imgInsets.right	+= imgInsets.right + imgSize.width;
				break;
			case PositionConstants.NORTH :
				imgInsets.top += imgInsets.top + imgSize.height;
				break;
			case PositionConstants.SOUTH :
				imgInsets.bottom += imgInsets.bottom + imgSize.height;
		}
	} else
		imgInsets = origInsets;
}

}