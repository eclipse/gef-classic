package org.eclipse.gef.examples.logicdesigner.edit;

import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.SnapToStrategy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import org.eclipse.gef.examples.logicdesigner.rulers.Guide;

/**
 * @author Randy Hudson
 */
public class SnapToGuides implements SnapToStrategy {

private static final double THRESHOLD = 5.01;
private LogicDiagramEditPart diagramEditPart;

public SnapToGuides(LogicDiagramEditPart diagramEditPart) {
	this.diagramEditPart = diagramEditPart;
}

Guide[] getHorizontalGuides() {
	List guides = diagramEditPart.getLogicDiagram()
			.getRuler(PositionConstants.WEST).getGuides();
	return (Guide[])guides.toArray(new Guide[guides.size()]);
}

Guide[] getVerticalGuides() {
	List guides = diagramEditPart.getLogicDiagram()
			.getRuler(PositionConstants.NORTH).getGuides();
	return (Guide[])guides.toArray(new Guide[guides.size()]);
}


private double getCorrectionFor(Guide[] guides, double near, double far, Map extendedData, 
                                boolean isVertical) {
	double result = getCorrectionFor(guides, near, extendedData, isVertical, -1);
	if (result == THRESHOLD)
		result = getCorrectionFor(guides, (near + far) / 2, extendedData, isVertical, 0); 
	if (result == THRESHOLD)
		result = getCorrectionFor(guides, far, extendedData, isVertical, 1);
	return result;
}

private double getCorrectionFor(Guide[] guides, double value, Map extendedData, 
                                boolean vert, int side) {
	double resultMag = THRESHOLD;
	double result = THRESHOLD;
	
	for (int i = 0; i < guides.length; i++) {
		int offset = guides[i].getPosition();
		double magnitude;
		
		magnitude = Math.abs(value - offset);
		if (magnitude < resultMag) {
			extendedData.put(vert ? ChangeBoundsRequest.VERTICAL_GUIDE 
			                      : ChangeBoundsRequest.HORIZONTAL_GUIDE, guides[i]);
			extendedData.put(vert ? ChangeBoundsRequest.VERTICAL_ANCHOR 
			                : ChangeBoundsRequest.HORIZONTAL_ANCHOR, new Integer(side));
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
	IFigure fig = diagramEditPart.getContentPane();
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
	if (request.getEditParts().size() > 1) {
		request.getExtendedData().clear();
	}
	return true;

}

/**
 * @see SnapToStrategy#snapResizeRequest(ChangeBoundsRequest, PrecisionRectangle)
 */
public boolean snapResizeRequest(ChangeBoundsRequest request, PrecisionRectangle baseRec) {
	int dir = request.getResizeDirection();

	PrecisionDimension resize = new PrecisionDimension(request.getSizeDelta());
	PrecisionPoint move = new PrecisionPoint(request.getMoveDelta());

	IFigure fig = diagramEditPart.getContentPane();
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
	if (request.getEditParts().size() > 1) {
		request.getExtendedData().clear();
	}
	return true;
}

}
