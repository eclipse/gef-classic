package org.eclipse.gef.examples.logicdesigner.edit;

import java.util.List;

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

int[] getHorizontalGuides() {
	List guides = diagramEditPart.getLogicDiagram()
		.getRuler(PositionConstants.WEST).getGuides();

	int result[] = new int[guides.size()];
	
	for (int i = 0; i < guides.size(); i++) {
		Guide g = (Guide)guides.get(i);
		result[i] = g.getPosition();
	}
	return result;
}

int[] getVerticalGuides() {
	List guides = diagramEditPart.getLogicDiagram()
		.getRuler(PositionConstants.NORTH).getGuides();

	int result[] = new int[guides.size()];
	
	for (int i = 0; i < guides.size(); i++) {
		Guide g = (Guide)guides.get(i);
		result[i] = g.getPosition();
	}
	return result;
}


private double getCorrectionFor(int[] guides, double near, double far) {
	double result = getCorrectionFor(guides, near);
	if (result == THRESHOLD)
		result = getCorrectionFor(guides, (near + far) / 2);
	if (result == THRESHOLD)
		result = getCorrectionFor(guides, far);
	return result;
}

private double getCorrectionFor(int[] guides, double value) {
	double resultMag = THRESHOLD;
	double result = THRESHOLD;
	
	for (int i = 0; i < guides.length; i++) {
		int offset = guides[i];
		double magnitude;
		
		magnitude = Math.abs(value - offset);
		if (magnitude < resultMag) {
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
			baseRect.preciseRight());
	double ycorrect = getCorrectionFor(getHorizontalGuides(), baseRect.preciseY,
			baseRect.preciseBottom());

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
	if ((dir & PositionConstants.EAST) != 0) {
		double rightCorrection = getCorrectionFor(getVerticalGuides(), baseRec.preciseRight());
		if (rightCorrection != THRESHOLD) {
			change = true;
			resize.preciseWidth += rightCorrection;
		}
	} else if ((dir & PositionConstants.WEST) != 0) {
		double leftCorrection = getCorrectionFor(getVerticalGuides(), baseRec.preciseX);
		if (leftCorrection != THRESHOLD) {
			change = true;
			resize.preciseWidth -= leftCorrection;
			move.preciseX += leftCorrection;
		}
	}
	
	if ((dir & PositionConstants.SOUTH) != 0) {
		double bottom = getCorrectionFor(getHorizontalGuides(), baseRec.preciseBottom());
		if (bottom != THRESHOLD) {
			change = true;
			resize.preciseHeight += bottom;
		}
	} else if ((dir & PositionConstants.NORTH) != 0) {
		double topCorrection = getCorrectionFor(getHorizontalGuides(), baseRec.preciseY);
		if (topCorrection != THRESHOLD) {
			change = true;
			resize.preciseHeight -= topCorrection;
			move.preciseY += topCorrection;
		}
	}

	if (!change)
		return false;
	fig.translateToAbsolute(resize);
	fig.translateToAbsolute(move);
	resize.updateInts();	
	move.updateInts();

	request.setSizeDelta(resize);
	request.setMoveDelta(move);
	return true;

}

}
