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
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.rulers.RulerProvider;

/**
 * @author Randy Hudson
 * @author Pratik Shah
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

protected boolean isKeyDown(Request req, String whichKey) {
	Boolean value = (Boolean)req.getExtendedData().get(whichKey);
	if (value != null)
		return value.booleanValue();
	return false;
}

public int snapCreateRequest(CreateRequest request, PrecisionRectangle baseRect,
                             int snapOrientation) {
	IFigure fig = container.getContentPane();
	fig.translateToRelative(baseRect);
	boolean change = false;
	System.out.println(baseRect);
	
	if ((snapOrientation & WEST) != 0) {
		// west
		double leftCorrection = getCorrectionFor(getVerticalGuides(), 
				baseRect.preciseX, request.getExtendedData(), true, -1);
		if (leftCorrection != THRESHOLD) {
			change = true;
			// Clear the WEST flag
			snapOrientation = snapOrientation & (NORTH_SOUTH | EAST);
			baseRect.preciseX += leftCorrection;
			baseRect.preciseWidth -= leftCorrection;
		} 
	}
	if (!change && (snapOrientation & EAST) != 0) {
		// east
		double rightCorrection = getCorrectionFor(getVerticalGuides(), 
				baseRect.preciseRight(), request.getExtendedData(), true, 1);
		if (rightCorrection != THRESHOLD) {
			change = true;
			// Clear the EAST flag
			snapOrientation = snapOrientation & (NORTH_SOUTH | WEST);
			baseRect.preciseWidth += rightCorrection;
		}
	}
	boolean snapHorizontal = false;
	if ((snapOrientation & NORTH) != 0) {
		// north
		double topCorrection = getCorrectionFor(getHorizontalGuides(), 
				baseRect.preciseY, request.getExtendedData(), false, -1);
		if (topCorrection != THRESHOLD) {
			change = true;
			snapHorizontal = true;
			// Clear the NORTH flag
			snapOrientation = snapOrientation & (EAST_WEST | SOUTH);
			baseRect.preciseHeight -= topCorrection;
			baseRect.preciseY += topCorrection;
		}
	}
	if (!snapHorizontal && (snapOrientation & SOUTH) != 0) {
		// south
		double bottom = getCorrectionFor(getHorizontalGuides(), baseRect.preciseBottom(),
				request.getExtendedData(), false, 1);
		if (bottom != THRESHOLD) {
			change = true;
			snapOrientation = snapOrientation & (EAST_WEST | NORTH);
			baseRect.preciseHeight += bottom;
		}		
	}
	
	if (!change)
		return snapOrientation;
	baseRect.updateInts();
	System.out.println(baseRect);
	fig.translateToAbsolute(baseRect);
	baseRect.updateInts();
	request.setSize(baseRect.getSize());
	request.setLocation(baseRect.getLocation());
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
	if ((snapOrientation & EAST_WEST) != 0)
		xcorrect = getCorrectionFor(getVerticalGuides(), baseRect.preciseX,
				baseRect.preciseRight(), request.getExtendedData(), true);
	if ((snapOrientation & NORTH_SOUTH) != 0)
		ycorrect = getCorrectionFor(getHorizontalGuides(), baseRect.preciseY,
				baseRect.preciseBottom(), request.getExtendedData(), false);
	
	//If neither value is being corrected, return false
	if (xcorrect == THRESHOLD && ycorrect == THRESHOLD)
		return snapOrientation;
	
	if (xcorrect != THRESHOLD)
		snapOrientation = snapOrientation & NORTH_SOUTH;
	if (ycorrect != THRESHOLD)
		snapOrientation = snapOrientation & EAST_WEST;

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
	if ((snapOrientation & EAST_WEST) != 0) {
		if (isKeyDown(request, CTRL_KEY)) {
			// This is a centered resize in the horizontal direction
			double rightCorrection = getCorrectionFor(getVerticalGuides(), 
					baseRect.preciseRight(), request.getExtendedData(), true, 1);
			// Store the guide and anchor information, in case leftCorrection over-writes 
			// it
			Object vGuide = request.getExtendedData().get(PROPERTY_VERTICAL_GUIDE);
			Object vAnchor = request.getExtendedData().get(PROPERTY_VERTICAL_ANCHOR);
			double leftCorrection = getCorrectionFor(getVerticalGuides(), 
					baseRect.preciseX, request.getExtendedData(), true, -1);
			if(Math.abs(leftCorrection) <= Math.abs(rightCorrection)
					&& leftCorrection != THRESHOLD) {
				change = true;
				// Clear the EAST and WEST flags
				snapOrientation = snapOrientation & NORTH_SOUTH;
				resize.preciseWidth -= (leftCorrection * 2);
				move.preciseX += leftCorrection;
			} else if (rightCorrection != THRESHOLD) {
				// Restore the guide and anchor information, in case it was over-written
				// by leftCorrection
				request.getExtendedData().put(PROPERTY_VERTICAL_GUIDE, vGuide);
				request.getExtendedData().put(PROPERTY_VERTICAL_ANCHOR, vAnchor);
				change = true;
				snapOrientation = snapOrientation & NORTH_SOUTH;
				resize.preciseWidth += (rightCorrection * 2);
				move.preciseX -= rightCorrection;
			}
		} else if ((request.getResizeDirection() & EAST) != 0) {
			// east
			double rightCorrection = getCorrectionFor(getVerticalGuides(), 
					baseRect.preciseRight(), request.getExtendedData(), true, 1);
			if (rightCorrection != THRESHOLD) {
				change = true;
				snapOrientation = snapOrientation & (NORTH_SOUTH | WEST);
				resize.preciseWidth += rightCorrection;
			}
		} else if ((request.getResizeDirection() & WEST) != 0) {
			// west
			double leftCorrection = getCorrectionFor(getVerticalGuides(), 
					baseRect.preciseX, request.getExtendedData(), true, -1);
			if (leftCorrection != THRESHOLD) {
				change = true;
				snapOrientation = snapOrientation & (NORTH_SOUTH | EAST);
				resize.preciseWidth -= leftCorrection;
				move.preciseX += leftCorrection;
			}
		}
	}
	if ((snapOrientation & NORTH_SOUTH) != 0) {
		if (isKeyDown(request, CTRL_KEY)) {
			// This is a centered resize in the vertical direction
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
				change = true;
				// Clear the NORTH and SOUTH flags
				snapOrientation = snapOrientation & EAST_WEST;
				resize.preciseHeight -= (topCorrection * 2);
				move.preciseY += topCorrection;
			} else if (bottom != THRESHOLD) {
				change = true;
				snapOrientation = snapOrientation & EAST_WEST;
				resize.preciseHeight += (2 * bottom);
				move.preciseY -= bottom;
			}
		} else if ((request.getResizeDirection() & SOUTH) != 0) {
			// south
			double bottom = getCorrectionFor(getHorizontalGuides(), 
					baseRect.preciseBottom(), request.getExtendedData(), false, 1);
			if (bottom != THRESHOLD) {
				change = true;
				snapOrientation = snapOrientation & (EAST_WEST | NORTH);
				resize.preciseHeight += bottom;
			}
		} else if ((request.getResizeDirection() & NORTH) != 0) {	
			// north
			double topCorrection = getCorrectionFor(getHorizontalGuides(), 
					baseRect.preciseY, request.getExtendedData(), false, -1);
			if (topCorrection != THRESHOLD) {
				change = true;
				snapOrientation = snapOrientation & (EAST_WEST | SOUTH);
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