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
package org.eclipse.gef.requests;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;

public class AlignmentRequest extends ChangeBoundsRequest {

private Rectangle alignmentRect;
private int alignment;

public AlignmentRequest() {}

public AlignmentRequest(Object type) {
	super(type);
}

public int getAlignment() {
	return alignment;
}

public Rectangle getAlignmentRectangle() {
	return alignmentRect;
}

public Rectangle getTransformedRectangle(Rectangle rect) {
	if (getAlignmentRectangle() == null)
		return rect;
	Rectangle changed = new Rectangle(rect);
	switch (alignment) {
		case PositionConstants.LEFT: {
			changed.x = getAlignmentRectangle().x;
			break;
		}
		case PositionConstants.RIGHT: {
			changed.x = getAlignmentRectangle().x + getAlignmentRectangle().width - changed.width;
			break;
		}
		case PositionConstants.TOP: {
			changed.y = getAlignmentRectangle().y;
			break;
		}
		case PositionConstants.BOTTOM: {
			changed.y = getAlignmentRectangle().y + getAlignmentRectangle().height - changed.height;
			break;
		}
		case PositionConstants.CENTER: {
			changed.x = getAlignmentRectangle().x + (getAlignmentRectangle().width / 2) - (changed.width / 2);
			break;
		}
		case PositionConstants.MIDDLE: {
			changed.y = getAlignmentRectangle().y + (getAlignmentRectangle().height / 2) - (changed.height / 2);
			break;
		}
	}
	return changed;
}

public void setAlignment(int align) {
	alignment = align;
}

public void setAlignmentRectangle(Rectangle rect) {
	alignmentRect = rect;
}

}
