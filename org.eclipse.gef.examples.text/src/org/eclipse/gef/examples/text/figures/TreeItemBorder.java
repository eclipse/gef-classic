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

package org.eclipse.gef.examples.text.figures;

import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A border class which displays an Image on the left side of a figure.
 * @author Pratik Shah
 */
public class TreeItemBorder
	extends ListItemBorder 
{

private static final int LEFT_SPACE = 5;
private static final int RIGHT_SPACE = 5;

private Insets imgInsets;
private Image image;
private Dimension imageSize;

public TreeItemBorder(Image image) {
	setImage(image);
}

public Insets getInsets(IFigure figure) {
	return imgInsets;
}

public Image getImage() {
	return image;
}

public Dimension getPreferredSize(IFigure f) {
	return imageSize;
}

public void paintBorder(IFigure figure, Graphics graphics, Insets insets) {
	if (image == null)
		return;
	Rectangle rect = getPaintRectangle(figure, insets);
	graphics.translate(rect.x, rect.y);
	graphics.drawImage(getImage(), LEFT_SPACE, 0);
	
	int y = imageSize.height / 2;
	for (int i = 0; i < LEFT_SPACE; i += 2)
		graphics.drawPoint(i, y);
}

public void setImage(Image img) {
	image = img;
	imageSize = new Dimension(image);
	imgInsets = new Insets();
	imgInsets.left = imageSize.width + LEFT_SPACE + RIGHT_SPACE;
}

}