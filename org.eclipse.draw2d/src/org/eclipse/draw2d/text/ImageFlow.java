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
package org.eclipse.draw2d.text;

import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A FlowFigure to insert images in a block.
 * <p>
 * WARNING: This class is not intended to be subclassed by clients. The API is subject
 * to change.
 * </p>
 * 
 * @author Pratik Shah
 * @since 3.1
 */
public class ImageFlow 
	extends FlowFigure
{

private Image img;
private ContentBox box;
private FlowContext context;

/**
 * Default Constructor
 */
public ImageFlow() {
	super();
	box = new ContentBox();
}

/**
 * Constructor
 * @param img the Image to be displayed
 */
public ImageFlow(Image img) {
	this();
	setImage(img);
}

/**
 * This FlowFigure contributes an Object Replacement Character.
 * @see org.eclipse.draw2d.text.FlowFigure#contributeBidi(org.eclipse.draw2d.text.BidiProcessor)
 */
protected void contributeBidi(BidiProcessor proc) {
	// contributes the object replacement character
	proc.add(this, "\ufffc"); //$NON-NLS-1$
}

/**
 * ImageFlow does not use a layout manager.
 * @see org.eclipse.draw2d.text.FlowFigure#createDefaultFlowLayout()
 */
protected FlowFigureLayout createDefaultFlowLayout() {
	return null;
}

/**
 * @see org.eclipse.draw2d.Figure#layout()
 */
protected void layout() {
	int[] bidiValues = getBidiValues();
	if (bidiValues != null)
		box.setBidiLevel(bidiValues[0]);
	else
		box.setBidiLevel(-1);
	if (img != null) {
		org.eclipse.swt.graphics.Rectangle bounds = img.getBounds();
		box.width = bounds.width;
		box.height = bounds.height;
	} else
		box.width = box.height = 0;
	context.getCurrentLine().add(box);
}

/**
 * Paints the image.
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics graphics) {
	graphics.drawImage(img, box.x, box.y);
}

/**
 * Updates the bounds of the ImageFlow to match that of its content box.
 * @see org.eclipse.draw2d.text.FlowFigure#postValidate()
 */
public void postValidate() {
	setBounds(new Rectangle(box.x, box.y, box.width, box.height));
}

/**
 * Since ImageFlow doesn't use a LayoutManager, it saves a reference to it.
 * @see org.eclipse.draw2d.text.FlowFigure#setFlowContext(org.eclipse.draw2d.text.FlowContext)
 */
public void setFlowContext(FlowContext flowContext) {
	context = flowContext;
}

/**
 * Sets the image to be displayed by this ImageFlow 
 * @param image the Image to be displayed
 */
public void setImage(Image image) {
	if (img != image) {
		img = image;
		revalidate();
		repaint();
	}
}

}