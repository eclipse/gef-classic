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

import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A Figure that simply contains an Image.  Use this Figure, instead of a Label, when
 * displaying Images without any accompanying text.  Using Labels for displaying Images
 * can stress your resources.
 * 
 * @author Pratik Shah
 */
public class ImageFigure 
	extends Figure 
{

private Image img;
private Dimension size;
private int alignment;

/**
 * Constructor
 */
public ImageFigure() {
	setAlignment(PositionConstants.CENTER);
}

/**
 * Constructor
 * 
 * @param image	The Image to be displayed
 */
public ImageFigure(Image image) {
	this();
	setImage(image);
}

/**
 * @return The Image that this Figure displays
 */
public Image getImage() {
	return img;
}

/**
 * Returns the size of the Image that this Figure displays; or (0,0) if no Image has been
 * set yet.
 * 			
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	Dimension size = new Dimension();
	if (getImage() != null) {
		org.eclipse.swt.graphics.Rectangle imgSize = getImage().getBounds();
		size.width = imgSize.width;
		size.height = imgSize.height;
	}
	return size;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics graphics) {
	int x, y;
	Rectangle area = getClientArea();
	switch (alignment & PositionConstants.NORTH_SOUTH) {
		case PositionConstants.NORTH:
			y = area.y;
			break;
		case PositionConstants.SOUTH:
			y = area.y + area.height - size.height;
			break;
		default:
			y = (area.height - size.height) / 2 + area.y;
			break;
	}
	switch (alignment & PositionConstants.EAST_WEST) {
		case PositionConstants.EAST:
			x = area.x;
			break;
		case PositionConstants.WEST:
			x = area.x + area.width - size.width;
			break;
		default:
			x = (area.width - size.width) / 2 + area.x;
			break;
	}
	graphics.drawImage(getImage(), x, y);
}

/**
 * Sets the alignment of the Image within this Figure.  The alignment comes into play
 * when the ImageFigure is larger than the Image.  The alignment could be any valid
 * combination of the following:
 * 
 * <UL>
 * 		<LI>PositionConstants.NORTH</LI>
 * 		<LI>PositionConstants.SOUTH</LI>
 * 		<LI>PositionConstants.EAST</LI>
 * 		<LI>PositionConstants.WEST</LI>
 * 		<LI>PositionConstants.CENTER or PositionConstants.NONE</LI>
 * </UL>
 * 
 * @param flag A constant indicating the alignment
 */
public void setAlignment(int flag) {
	alignment = flag;
}

/**
 * Sets the Image that this ImageFigure displays.
 * 
 * @param image	The Image to be displayed.
 */
public void setImage(Image image) {
	img = image;
	size = new Rectangle(image.getBounds()).getSize();
	revalidate();
	repaint();
}

}
