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
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

public class AlignmentRequest extends ChangeBoundsRequest {

private int alignment;
private Rectangle alignmentRect;

public AlignmentRequest() {}

public AlignmentRequest(Object type) {
	super(type);
}

private void doNormalAlignment(Rectangle result, Rectangle reference) {
	switch (alignment) {
		case PositionConstants.LEFT: {
			result.x = reference.x;
			break;
		}
		case PositionConstants.RIGHT: {
			result.x = reference.x + reference.width - result.width;
			break;
		}
		case PositionConstants.TOP: {
			result.y = reference.y;
			break;
		}
		case PositionConstants.BOTTOM: {
			result.y = reference.y + reference.height - result.height;
			break;
		}
		case PositionConstants.CENTER: {
			result.x = reference.x + (reference.width / 2) - (result.width / 2);
			break;
		}
		case PositionConstants.MIDDLE: {
			result.y = reference.y + (reference.height / 2) - (result.height / 2);
			break;
		}
	}
}

private void doPrecisionAlignment(
	PrecisionRectangle result,
	PrecisionRectangle reference) {
	switch (alignment) {
		case PositionConstants.LEFT: {
			result.setX(reference.preciseX);
			break;
		}
		case PositionConstants.RIGHT: {
			result.setX(
				reference.preciseX + reference.preciseWidth - result.preciseWidth);
			break;
		}
		case PositionConstants.TOP: {
			result.setY(reference.preciseY);
			break;
		}
		case PositionConstants.BOTTOM: {
			result.setY(
				reference.preciseY + reference.preciseHeight - result.preciseHeight);
			break;
		}
		case PositionConstants.CENTER: {
			result.setX(
				reference.preciseX
					+ (reference.preciseWidth / 2)
					- (result.preciseWidth / 2));
			break;
		}
		case PositionConstants.MIDDLE: {
			result.setY(
				reference.preciseY
					+ (reference.preciseHeight / 2)
					- (result.preciseHeight / 2));
			break;
		}
	}

	
}

public int getAlignment() {
	return alignment;
}

public Rectangle getAlignmentRectangle() {
	return alignmentRect;
}

public Rectangle getTransformedRectangle(Rectangle rect) {
	Rectangle result = rect.getCopy();
	Rectangle reference = getAlignmentRectangle();

	if (result instanceof PrecisionRectangle) {
		if (reference instanceof PrecisionRectangle)
			doPrecisionAlignment(
				(PrecisionRectangle)result,
				(PrecisionRectangle)reference);
		else
			doPrecisionAlignment(
				(PrecisionRectangle)result,
				new PrecisionRectangle(reference));
	} else
		doNormalAlignment(result, reference);
	return result;
}

public void setAlignment(int align) {
	alignment = align;
}

public void setAlignmentRectangle(Rectangle rect) {
	alignmentRect = rect;
}

}
