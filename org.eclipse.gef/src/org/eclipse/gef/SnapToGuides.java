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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.rulers.RulerProvider;

/**
 * @author Randy Hudson
 */
public class SnapToGuides 
	implements SnapToHelper 
{

public static final String PROPERTY_VERTICAL_GUIDE = "vertical guide"; //$NON-NLS-1$
public static final String PROPERTY_HORIZONTAL_GUIDE = "horizontal guide"; //$NON-NLS-1$
public static final String PROPERTY_VERTICAL_ANCHOR = "vertical attachment"; //$NON-NLS-1$
public static final String PROPERTY_HORIZONTAL_ANCHOR = "horizontal attachment"; //$NON-NLS-1$

private static final double THRESHOLD = 7.01;
private GraphicalEditPart container;
private int[] verticalGuides, horizontalGuides;

public SnapToGuides(GraphicalEditPart container) {
	this.container = container;
}

int[] getHorizontalGuides() {	
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

int[] getVerticalGuides() {
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


private double getCorrectionFor(int[] guides, double near, double far, Map extendedData, 
                                  boolean isVertical) {
	double result = getCorrectionFor(guides, (near + far) / 2, extendedData, isVertical, 0);
	if (result == THRESHOLD)
		result = getCorrectionFor(guides, near, extendedData, isVertical, -1); 
	if (result == THRESHOLD)
		result = getCorrectionFor(guides, far, extendedData, isVertical, 1);
	return result;
}

private double getCorrectionFor(int[] guides, double value, Map extendedData, 
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

public int snapCreateRequest(CreateRequest request, PrecisionRectangle baseRect,
		int snapOrientation) {
	return snapOrientation;
}

/**
 * @see SnapToHelper#snapMoveRequest(ChangeBoundsRequest, PrecisionRectangle)
 */
public int snapMoveRequest(ChangeBoundsRequest request,	PrecisionRectangle baseRect,
                           PrecisionRectangle selectionRect, int snapOrientation) {
	// If there are more than one edit parts being moved, detach them from all guides.
	if (request.getEditParts().size() != 1)
		return snapOrientation;
	
	PrecisionPoint move = new PrecisionPoint(request.getMoveDelta());
	IFigure fig = container.getContentPane();
	baseRect.translate(move);
	fig.translateToRelative(baseRect);
	fig.translateToRelative(move);

	double xcorrect = THRESHOLD, ycorrect = THRESHOLD;
	if ((snapOrientation & SNAP_VERTICAL) != 0)
		xcorrect = getCorrectionFor(getVerticalGuides(), baseRect.preciseX,
				baseRect.preciseRight(), request.getExtendedData(), true);
	if ((snapOrientation & SNAP_HORIZONTAL) != 0)
		ycorrect = getCorrectionFor(getHorizontalGuides(), baseRect.preciseY,
				baseRect.preciseBottom(), request.getExtendedData(), false);
	
	//If neither value is being corrected, return false
	if (xcorrect == THRESHOLD && ycorrect == THRESHOLD)
		return snapOrientation;
	
	if (xcorrect != THRESHOLD)
		snapOrientation = snapOrientation & SNAP_HORIZONTAL;
	if (ycorrect != THRESHOLD)
		snapOrientation = snapOrientation & SNAP_VERTICAL;

	if (ycorrect == THRESHOLD)
		ycorrect = 0.0;
	else if (xcorrect == THRESHOLD)
		xcorrect = 0.0;
	
	move.preciseX += xcorrect;
	move.preciseY += ycorrect;
	move.updateInts();
	fig.translateToAbsolute(move);
	request.setMoveDelta(move);
	return snapOrientation;

}

/**
 * @see SnapToHelper#snapResizeRequest(ChangeBoundsRequest, PrecisionRectangle)
 */
public int snapResizeRequest(ChangeBoundsRequest request, PrecisionRectangle baseRect,
                             int snapOrientation) {
	/*
	 * @TODO:Pratik
	 * Known Bugs: 
	 * (1) Place a figure's top and left edges close to two guides, but not attached
	 * to them.  Resize the bottom-right corner of the figure, and you will see that the
	 * figure snaps to those two guides.  Or at least, they will be highlighted.  
	 */
	int origSnapOrientation = snapOrientation;
	
	if (request.getEditParts().size() != 1)
		return snapOrientation;
	
	PrecisionDimension resize = new PrecisionDimension(request.getSizeDelta());
	PrecisionPoint move = new PrecisionPoint(request.getMoveDelta());

	IFigure fig = container.getContentPane();
	baseRect.resize(resize);
	baseRect.translate(move);
	
	fig.translateToRelative(baseRect);
	fig.translateToRelative(resize);
	fig.translateToRelative(move);

	boolean change = false;
	/*
	 * In order to preserve attachments to guides, there can be no optimizations.
	 * getCorrectionFor(...) must be invoked for all directions, not just along the ones
	 * that are being resized.
	 */
	// east
	if ((origSnapOrientation & SNAP_HORIZONTAL) != 0) {
		double rightCorrection = getCorrectionFor(getVerticalGuides(), 
				baseRect.preciseRight(), request.getExtendedData(), true, 1);
		if (rightCorrection != THRESHOLD) {
			change = true;
			snapOrientation = snapOrientation & SNAP_HORIZONTAL;
			resize.preciseWidth += rightCorrection;
		}
		// west
		// if we have a match on the right and we are resizing that way, use that match
		if (rightCorrection == THRESHOLD 
				|| (request.getResizeDirection() & PositionConstants.EAST) == 0) {
			double leftCorrection = getCorrectionFor(getVerticalGuides(), 
					baseRect.preciseX, request.getExtendedData(), true, -1);
			if (leftCorrection != THRESHOLD) {
				change = true;
				snapOrientation = snapOrientation & SNAP_HORIZONTAL;
				resize.preciseWidth -= leftCorrection;
				move.preciseX += leftCorrection;
			}
		}
	}
	// south
	if ((origSnapOrientation & SNAP_VERTICAL) != 0) {
		double bottom = getCorrectionFor(getHorizontalGuides(), baseRect.preciseBottom(),
				request.getExtendedData(), false, 1);
		if (bottom != THRESHOLD) {
			change = true;
			snapOrientation = snapOrientation & SNAP_VERTICAL;
			resize.preciseHeight += bottom;
		}
		// north
		// if we have a match on the bottom and we are resizing south, keep this.
		if (bottom == THRESHOLD 
				|| (request.getResizeDirection() & PositionConstants.SOUTH) == 0) {	
			double topCorrection = getCorrectionFor(getHorizontalGuides(), 
					baseRect.preciseY, request.getExtendedData(), false, -1);
			if (topCorrection != THRESHOLD) {
				change = true;
				snapOrientation = snapOrientation & SNAP_VERTICAL;
				resize.preciseHeight -= topCorrection;
				move.preciseY += topCorrection;
			}
		}
	}
	
	if (!change)
		return snapOrientation;
	
	fig.translateToAbsolute(resize);
	fig.translateToAbsolute(move);
	resize.updateInts();	
	move.updateInts();
	request.setSizeDelta(resize);
	request.setMoveDelta(move);
	return snapOrientation;
}

}