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
import org.eclipse.gef.ui.parts.RulerProvider;

/**
 * @author Randy Hudson
 */
public class SnapToGuides implements SnapToStrategy {

	
public static final String VERTICAL_GUIDE = "vertical guide"; //$NON-NLS-1$
public static final String HORIZONTAL_GUIDE = "horizontal guide"; //$NON-NLS-1$
public static final String VERTICAL_ANCHOR = "vertical attachment"; //$NON-NLS-1$
public static final String HORIZONTAL_ANCHOR = "horizontal attachment"; //$NON-NLS-1$

private static final double THRESHOLD = 7.01;
private GraphicalEditPart container;
private int[] verticalGuides, horizontalGuides;

public SnapToGuides(GraphicalEditPart container) {
	this.container = container;
}

/*
 * @TODO:Pratik   make sure it's okay to cache these
 */
int[] getHorizontalGuides() {
	if (horizontalGuides == null) {
		RulerProvider rProvider = ((RulerProvider)container.getViewer()
				.getProperty(RulerProvider.VERTICAL));
		if (rProvider != null) {
			horizontalGuides = rProvider.getGuidePositions();	
		} else {
			horizontalGuides = new int[0];
		}
	}
	return horizontalGuides;
}

int[] getVerticalGuides() {
	if (verticalGuides == null) {
		RulerProvider rProvider = ((RulerProvider)container.getViewer()
				.getProperty(RulerProvider.HORIZONTAL));
		if (rProvider != null) {
			verticalGuides = rProvider.getGuidePositions();	
		} else {
			verticalGuides = new int[0];
		}
	}
	return verticalGuides;
}


private double getCorrectionFor(int[] guides, double near, double far, Map extendedData, 
                                boolean isVertical) {
	double result = getCorrectionFor(guides, near, extendedData, isVertical, -1);
	if (result == THRESHOLD)
		result = getCorrectionFor(guides, (near + far) / 2, extendedData, isVertical, 0); 
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
			extendedData.put(vert ? VERTICAL_GUIDE : HORIZONTAL_GUIDE, 
					new Integer(guides[i]));
			extendedData.put(vert ? VERTICAL_ANCHOR : HORIZONTAL_ANCHOR, 
					new Integer(side));
			resultMag = magnitude;
			result = offset - value;
		}
	}
	return result;
}


/**
 * @see SnapToStrategy#snapMoveRequest(ChangeBoundsRequest, PrecisionRectangle)
 */
public boolean snapMoveRequest(ChangeBoundsRequest request,	PrecisionRectangle baseRect) {
	PrecisionPoint move = new PrecisionPoint(request.getMoveDelta());
	IFigure fig = container.getContentPane();
	baseRect.translate(move);
	fig.translateToRelative(baseRect);
	fig.translateToRelative(move);

	double xcorrect = getCorrectionFor(getVerticalGuides(), baseRect.preciseX,
			baseRect.preciseRight(), request.getExtendedData(), true);
	double ycorrect = getCorrectionFor(getHorizontalGuides(), baseRect.preciseY,
			baseRect.preciseBottom(), request.getExtendedData(), false);

	//If neither value is being corrected, return false
	if (xcorrect == THRESHOLD && ycorrect == THRESHOLD)
		return false;

	if (ycorrect == THRESHOLD)
		ycorrect = 0.0;
	else if (xcorrect == THRESHOLD)
		xcorrect = 0.0;
	
	move.preciseX += xcorrect;
	move.preciseY += ycorrect;
	move.updateInts();
	fig.translateToAbsolute(move);
	request.setMoveDelta(move);
	// If there are more than one edit parts being moved, detach them from all guides.
	if (request.getEditParts().size() > 1) {
		request.getExtendedData().clear();
	}
	return true;

}

/**
 * @see SnapToStrategy#snapResizeRequest(ChangeBoundsRequest, PrecisionRectangle)
 */
public boolean snapResizeRequest(ChangeBoundsRequest request, PrecisionRectangle baseRec) {
	PrecisionDimension resize = new PrecisionDimension(request.getSizeDelta());
	PrecisionPoint move = new PrecisionPoint(request.getMoveDelta());

	IFigure fig = container.getContentPane();
	baseRec.resize(resize);
	baseRec.translate(move);
	
	fig.translateToRelative(baseRec);
	fig.translateToRelative(resize);
	fig.translateToRelative(move);

	boolean change = false;
	/*
	 * In order to preserve connections to guides, there can be no optimizations.
	 * getCorrectionFor(...) must be invoked for all directions, not just along the ones
	 * that are being resized.
	 */
	// east
	double rightCorrection = getCorrectionFor(getVerticalGuides(), 
			baseRec.preciseRight(), request.getExtendedData(), true, 1);
	if (rightCorrection != THRESHOLD) {
		change = true;
		resize.preciseWidth += rightCorrection;
	}
	// west
	double leftCorrection = getCorrectionFor(getVerticalGuides(), baseRec.preciseX, 
			request.getExtendedData(), true, -1);
	if (leftCorrection != THRESHOLD) {
		change = true;
		resize.preciseWidth -= leftCorrection;
		move.preciseX += leftCorrection;
	}
	// south
	double bottom = getCorrectionFor(getHorizontalGuides(), baseRec.preciseBottom(),
			request.getExtendedData(), false, 1);
	if (bottom != THRESHOLD) {
		change = true;
		resize.preciseHeight += bottom;
	}
	// north
	double topCorrection = getCorrectionFor(getHorizontalGuides(), baseRec.preciseY,
			request.getExtendedData(), false, -1);
	if (topCorrection != THRESHOLD) {
		change = true;
		resize.preciseHeight -= topCorrection;
		move.preciseY += topCorrection;
	}

	if (!change)
		return false;
	fig.translateToAbsolute(resize);
	fig.translateToAbsolute(move);
	resize.updateInts();	
	move.updateInts();

	request.setSizeDelta(resize);
	request.setMoveDelta(move);
	// If there are more than one edit parts being resized, detach them from all guides.
	if (request.getEditParts().size() > 1) {
		request.getExtendedData().clear();
	}
	return true;
}

}
