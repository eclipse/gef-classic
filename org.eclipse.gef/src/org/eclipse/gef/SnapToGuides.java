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

import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.rulers.RulerProvider;

/**
 * @author Randy Hudson
 * @author Pratik Shah
 */
public class SnapToGuides 
	extends SnapToHelper 
{

public static final String PROPERTY_VERTICAL_GUIDE = "vertical guide"; //$NON-NLS-1$
public static final String PROPERTY_HORIZONTAL_GUIDE = "horizontal guide"; //$NON-NLS-1$
public static final String PROPERTY_VERTICAL_ANCHOR = "vertical attachment"; //$NON-NLS-1$
public static final String PROPERTY_HORIZONTAL_ANCHOR = "horizontal attachment"; //$NON-NLS-1$

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
			extendedData.put(vert ? PROPERTY_VERTICAL_GUIDE : PROPERTY_HORIZONTAL_GUIDE, 
					new Integer(guides[i]));
			extendedData.put(vert ? PROPERTY_VERTICAL_ANCHOR : PROPERTY_HORIZONTAL_ANCHOR, 
					new Integer(side));
			resultMag = magnitude;
			result = offset - value;
		}
	}
	return result;
}

protected int performCenteredResize(Request request, PrecisionRectangle baseRect, 
		int snapOrientation) {
	if ((snapOrientation & EAST_WEST) != 0) {
		double rightCorrection = getCorrectionFor(getVerticalGuides(), 
				baseRect.preciseRight(), request.getExtendedData(), true, 1);
		// Store the guide and anchor information, in case leftCorrection over-writes it
		Object vGuide = request.getExtendedData().get(PROPERTY_VERTICAL_GUIDE);
		Object vAnchor = request.getExtendedData().get(PROPERTY_VERTICAL_ANCHOR);
		double leftCorrection = getCorrectionFor(getVerticalGuides(), 
				baseRect.preciseX, request.getExtendedData(), true, -1);
		if(Math.abs(leftCorrection) <= Math.abs(rightCorrection)
				&& leftCorrection != THRESHOLD) {
			snapOrientation &= ~EAST_WEST;
			baseRect.preciseWidth -= (leftCorrection * 2);
			baseRect.preciseX += leftCorrection;
			
		} else if (rightCorrection != THRESHOLD) {
			// Restore the guide and anchor information, in case it was over-written
			// by leftCorrection
			request.getExtendedData().put(PROPERTY_VERTICAL_GUIDE, vGuide);
			request.getExtendedData().put(PROPERTY_VERTICAL_ANCHOR, vAnchor);
			snapOrientation &= ~EAST_WEST;
			baseRect.preciseWidth += (rightCorrection * 2);
			baseRect.preciseX -= rightCorrection;
		}
	}
	
	if ((snapOrientation & NORTH_SOUTH) != 0) {
		double topCorrection = getCorrectionFor(getHorizontalGuides(), 
				baseRect.preciseY, request.getExtendedData(), false, -1);
		Object hGuide = request.getExtendedData().get(PROPERTY_VERTICAL_GUIDE);
		Object hAnchor = request.getExtendedData().get(PROPERTY_VERTICAL_ANCHOR);
		double bottom = getCorrectionFor(getHorizontalGuides(), 
				baseRect.preciseBottom(), request.getExtendedData(), false, 1);
		if(Math.abs(topCorrection) <= Math.abs(bottom)
				&& topCorrection != THRESHOLD) {
			request.getExtendedData().put(PROPERTY_VERTICAL_GUIDE, hGuide);
			request.getExtendedData().put(PROPERTY_VERTICAL_ANCHOR, hAnchor);
			snapOrientation &= ~NORTH_SOUTH;
			baseRect.preciseHeight -= (topCorrection * 2);
			baseRect.preciseY += topCorrection;
		} else if (bottom != THRESHOLD) {
			snapOrientation &= ~NORTH_SOUTH;
			baseRect.preciseHeight += (2 * bottom);
			baseRect.preciseY -= bottom;
		}
	}

	return snapOrientation;
}

protected int snapMoveRect(Request request, PrecisionRectangle baseRect,
		PrecisionRectangle compoundRect, int snapOrientation) {
	// If there are more than one edit parts being moved, detach them from all guides.
	if (request instanceof GroupRequest 
			&& ((GroupRequest)request).getEditParts().size() > 1)
		return snapOrientation;
	
	makeRelative(container.getContentPane(), baseRect);

	double xcorrect = THRESHOLD, ycorrect = THRESHOLD;
	if ((snapOrientation & EAST_WEST) != 0)
		xcorrect = getCorrectionFor(getVerticalGuides(), baseRect.preciseX,
				baseRect.preciseRight(), request.getExtendedData(), true);
	if ((snapOrientation & NORTH_SOUTH) != 0)
		ycorrect = getCorrectionFor(getHorizontalGuides(), baseRect.preciseY,
				baseRect.preciseBottom(), request.getExtendedData(), false);
	
	if (xcorrect == THRESHOLD)
		xcorrect = 0.0;
	else
		snapOrientation &= ~EAST_WEST;
	
	if (ycorrect == THRESHOLD)
		ycorrect = 0.0;
	else
		snapOrientation &= ~NORTH_SOUTH;

	baseRect.preciseX += xcorrect;
	baseRect.preciseY += ycorrect;
	makeAbsolute(container.getContentPane(), baseRect);
	baseRect.updateInts();
	return snapOrientation;
}

protected int snapResizeRect(Request request, PrecisionRectangle baseRect,
		int snapOrientation) {
	if (request instanceof GroupRequest 
			&& ((GroupRequest)request).getEditParts().size() > 1)
		return snapOrientation;
	
	makeRelative(container.getContentPane(), baseRect);

	if (request instanceof ChangeBoundsRequest && 
			((ChangeBoundsRequest)request).isCenteredResize()) {
		snapOrientation = performCenteredResize(request, baseRect, snapOrientation);
	} else {
		boolean snapped = false;
		if ((snapOrientation & EAST) != 0) {
			double rightCorrection = getCorrectionFor(getVerticalGuides(), 
					baseRect.preciseRight(), request.getExtendedData(), true, 1);
			if (rightCorrection != THRESHOLD) {
				snapped = true;
				snapOrientation &= ~EAST;
				baseRect.preciseWidth += rightCorrection;
			}
		} 
		if (!snapped && (snapOrientation & WEST) != 0) {
			double leftCorrection = getCorrectionFor(getVerticalGuides(), 
					baseRect.preciseX, request.getExtendedData(), true, -1);
			if (leftCorrection != THRESHOLD) {
				snapOrientation &= ~WEST;
				baseRect.preciseWidth -= leftCorrection;
				baseRect.preciseX += leftCorrection;
			}
		}
		snapped = false;
		if ((snapOrientation & SOUTH) != 0) {
			double bottom = getCorrectionFor(getHorizontalGuides(), 
					baseRect.preciseBottom(), request.getExtendedData(), false, 1);
			if (bottom != THRESHOLD) {
				snapped = true;
				snapOrientation &= ~SOUTH;
				baseRect.preciseHeight += bottom;
			}
		}
		if (!snapped && (snapOrientation & NORTH) != 0) {	
			double topCorrection = getCorrectionFor(getHorizontalGuides(), 
					baseRect.preciseY, request.getExtendedData(), false, -1);
			if (topCorrection != THRESHOLD) {
				snapOrientation &= ~NORTH;
				baseRect.preciseHeight -= topCorrection;
				baseRect.preciseY += topCorrection;
			}
		}
	}
	
	makeAbsolute(container.getContentPane(), baseRect);
	baseRect.updateInts();
	return snapOrientation;
}

}