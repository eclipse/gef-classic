/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import java.util.Map;

import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.rulers.RulerProvider;

/**
 * @author Randy Hudson
 * @author Pratik Shah
 */
public class SnapToGuides 
	extends SnapToHelper 
{

/**
 * The property key used to identify the Vertical Guide. 
 */
public static final String KEY_VERTICAL_GUIDE =
	"SnapToGuides.VerticalGuide"; //$NON-NLS-1$

public static final String KEY_HORIZONTAL_GUIDE = "SnapToGuides.HorizontalGuide"; //$NON-NLS-1$

public static final String KEY_VERTICAL_ANCHOR = "SnapToGuides.VerticalAttachment"; //$NON-NLS-1$

public static final String KEY_HORIZONTAL_ANCHOR = "SnapToGuides.HorizontalAttachment"; //$NON-NLS-1$

protected static final double THRESHOLD = 7.01;
protected GraphicalEditPart container;
protected int[] verticalGuides, horizontalGuides;

public SnapToGuides(GraphicalEditPart container) {
	this.container = container;
}

protected int[] getHorizontalGuides() {	
	if (horizontalGuides == null) {
		RulerProvider rProvider = ((RulerProvider)container.getViewer()
					.getProperty(RulerProvider.PROPERTY_VERTICAL_RULER));
		if (rProvider != null)
			horizontalGuides = rProvider.getGuidePositions();	
		else
			horizontalGuides = new int[0];
	}
	return horizontalGuides;
}

protected int[] getVerticalGuides() {
	if (verticalGuides == null) {
		RulerProvider rProvider = ((RulerProvider)container.getViewer()
				.getProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER));
		if (rProvider != null)
			verticalGuides = rProvider.getGuidePositions();	
		else
			verticalGuides = new int[0];
	}
	return verticalGuides;
}


protected double getCorrectionFor(int[] guides, double near, double far, Map extendedData, 
                                  boolean isVertical) {
	double result = getCorrectionFor(guides, (near + far) / 2, extendedData, isVertical, 0);
	if (result == THRESHOLD)
		result = getCorrectionFor(guides, near, extendedData, isVertical, -1); 
	if (result == THRESHOLD)
		result = getCorrectionFor(guides, far, extendedData, isVertical, 1);
	return result;
}

protected double getCorrectionFor(int[] guides, double value, Map extendedData, 
                                  boolean vert, int side) {
	double resultMag = THRESHOLD;
	double result = THRESHOLD;
	
	for (int i = 0; i < guides.length; i++) {
		int offset = guides[i];
		double magnitude;
		
		magnitude = Math.abs(value - offset);
		if (magnitude < resultMag) {
			extendedData.put(vert ? KEY_VERTICAL_GUIDE : KEY_HORIZONTAL_GUIDE, 
					new Integer(guides[i]));
			extendedData.put(vert ? KEY_VERTICAL_ANCHOR : KEY_HORIZONTAL_ANCHOR, 
					new Integer(side));
			resultMag = magnitude;
			result = offset - value;
		}
	}
	return result;
}

protected int performCenteredResize(Request request, PrecisionRectangle baseRect, 
		PrecisionRectangle result, int snapOrientation) {
	if ((snapOrientation & EAST_WEST) != 0) {
		double rightCorrection = getCorrectionFor(getVerticalGuides(), 
				baseRect.preciseRight(), request.getExtendedData(), true, 1);
		// Store the guide and anchor information, in case leftCorrection over-writes it
		Object vGuide = request.getExtendedData().get(KEY_VERTICAL_GUIDE);
		Object vAnchor = request.getExtendedData().get(KEY_VERTICAL_ANCHOR);
		double leftCorrection = getCorrectionFor(getVerticalGuides(), 
				baseRect.preciseX, request.getExtendedData(), true, -1);
		if(Math.abs(leftCorrection) <= Math.abs(rightCorrection)
				&& leftCorrection != THRESHOLD) {
			snapOrientation &= ~EAST_WEST;
			result.preciseWidth -= (leftCorrection * 2);
			result.preciseX += leftCorrection;
			
		} else if (rightCorrection != THRESHOLD) {
			// Restore the guide and anchor information, in case it was over-written
			// by leftCorrection
			request.getExtendedData().put(KEY_VERTICAL_GUIDE, vGuide);
			request.getExtendedData().put(KEY_VERTICAL_ANCHOR, vAnchor);
			snapOrientation &= ~EAST_WEST;
			result.preciseWidth += (rightCorrection * 2);
			result.preciseX -= rightCorrection;
		}
	}
	
	if ((snapOrientation & NORTH_SOUTH) != 0) {
		double topCorrection = getCorrectionFor(getHorizontalGuides(), 
				baseRect.preciseY, request.getExtendedData(), false, -1);
		Object hGuide = request.getExtendedData().get(KEY_VERTICAL_GUIDE);
		Object hAnchor = request.getExtendedData().get(KEY_VERTICAL_ANCHOR);
		double bottom = getCorrectionFor(getHorizontalGuides(), 
				baseRect.preciseBottom(), request.getExtendedData(), false, 1);
		if(Math.abs(topCorrection) <= Math.abs(bottom)
				&& topCorrection != THRESHOLD) {
			request.getExtendedData().put(KEY_VERTICAL_GUIDE, hGuide);
			request.getExtendedData().put(KEY_VERTICAL_ANCHOR, hAnchor);
			snapOrientation &= ~NORTH_SOUTH;
			result.preciseHeight -= (topCorrection * 2);
			result.preciseY += topCorrection;
		} else if (bottom != THRESHOLD) {
			snapOrientation &= ~NORTH_SOUTH;
			result.preciseHeight += (2 * bottom);
			result.preciseY -= bottom;
		}
	}

	return snapOrientation;
}

/**
 * @see SnapToHelper#snapRectangle(Request, int, PrecisionRectangle, PrecisionRectangle)
 */
public int snapRectangle(Request request, int snapOrientation,
		PrecisionRectangle baseRect, PrecisionRectangle result) {
	if (request instanceof GroupRequest 
			&& ((GroupRequest)request).getEditParts().size() > 1)
		return snapOrientation;
	
	baseRect = baseRect.getPreciseCopy();
	makeRelative(container.getContentPane(), baseRect);
	PrecisionRectangle correction = new PrecisionRectangle();
	makeRelative(container.getContentPane(), correction);

	if ((snapOrientation & HORIZONTAL) != 0) {
		double xcorrect = getCorrectionFor(getVerticalGuides(), baseRect.preciseX,
				baseRect.preciseRight(), request.getExtendedData(), true);
		if (xcorrect != THRESHOLD) {
			snapOrientation &= ~HORIZONTAL;
			correction.preciseX += xcorrect;
		}
	}
	
	if ((snapOrientation & VERTICAL) != 0) {
		double ycorrect = getCorrectionFor(getHorizontalGuides(), baseRect.preciseY,
				baseRect.preciseBottom(), request.getExtendedData(), false);
		if (ycorrect != THRESHOLD) {
			snapOrientation &= ~VERTICAL;
			correction.preciseY += ycorrect;
		}
	}
	
	boolean snapped = false;
	if (!snapped && (snapOrientation & WEST) != 0) {
		double leftCorrection = getCorrectionFor(getVerticalGuides(), 
				baseRect.preciseX, request.getExtendedData(), true, -1);
		if (leftCorrection != THRESHOLD) {
			snapOrientation &= ~WEST;
			correction.preciseWidth -= leftCorrection;
			correction.preciseX += leftCorrection;
		}
	}

	if (!snapped && (snapOrientation & EAST) != 0) {
		double rightCorrection = getCorrectionFor(getVerticalGuides(), 
				baseRect.preciseRight(), request.getExtendedData(), true, 1);
		if (rightCorrection != THRESHOLD) {
			snapped = true;
			snapOrientation &= ~EAST;
			correction.preciseWidth += rightCorrection;
		}
	}
	
	snapped = false;
	if (!snapped && (snapOrientation & NORTH) != 0) {	
		double topCorrection = getCorrectionFor(getHorizontalGuides(), 
				baseRect.preciseY, request.getExtendedData(), false, -1);
		if (topCorrection != THRESHOLD) {
			snapOrientation &= ~NORTH;
			correction.preciseHeight -= topCorrection;
			correction.preciseY += topCorrection;
		}
	}

	if (!snapped && (snapOrientation & SOUTH) != 0) {
		double bottom = getCorrectionFor(getHorizontalGuides(), 
				baseRect.preciseBottom(), request.getExtendedData(), false, 1);
		if (bottom != THRESHOLD) {
			snapped = true;
			snapOrientation &= ~SOUTH;
			correction.preciseHeight += bottom;
		}
	}

	correction.updateInts();
	makeAbsolute(container.getContentPane(), correction);
	result.preciseX += correction.preciseX;
	result.preciseY += correction.preciseY;
	result.preciseWidth += correction.preciseWidth;
	result.preciseHeight += correction.preciseHeight;
	result.updateInts();
	
	return snapOrientation;
}

}