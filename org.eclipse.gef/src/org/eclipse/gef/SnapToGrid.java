package org.eclipse.gef;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * @author Randy Hudson
 */
public class SnapToGrid implements SnapToStrategy {
	
private GraphicalEditPart container;
private int gridX = 10, gridY = 10;

/**
 * Constructs a snap-to-grid strategy on the given editpart.  The editpart should be the
 * graphical editpart who's content-pane figure is used as the reference for the grid.
 * @param container the editpart which owns the grid
 */
public SnapToGrid(GraphicalEditPart container) {
	this.container = container;
}

public void snapResizeRequest(ChangeBoundsRequest request, PrecisionRectangle baseRec) {
	int dir = request.getResizeDirection();
	baseRec = (PrecisionRectangle)baseRec.getCopy();
	PrecisionDimension resize = new PrecisionDimension(request.getSizeDelta());
	PrecisionPoint move = new PrecisionPoint(request.getMoveDelta());
	
	IFigure fig = container.getContentPane();
	baseRec.resize(resize);
	baseRec.translate(move);
	
	fig.translateToRelative(baseRec);
	fig.translateToRelative(resize);
	fig.translateToRelative(move);

	if ((dir & PositionConstants.EAST) != 0) {
		double rightCorrection = Math.IEEEremainder(baseRec.preciseRight(), gridX);
		resize.preciseWidth -= rightCorrection;
	} else if ((dir & PositionConstants.WEST) != 0) {
		double leftCorrection = Math.IEEEremainder(baseRec.preciseX, gridX);
		resize.preciseWidth += leftCorrection;
		move.preciseX -= leftCorrection;
	}
	
	if ((dir & PositionConstants.SOUTH) != 0) {
		double bottom = Math.IEEEremainder(baseRec.preciseBottom(), gridY);
		resize.preciseHeight -= bottom;
	} else if ((dir & PositionConstants.NORTH) != 0) {
		double topCorrection = Math.IEEEremainder(baseRec.preciseY, gridY);
		resize.preciseHeight += topCorrection;
		move.preciseY -= topCorrection;
	}

	fig.translateToAbsolute(resize);
	fig.translateToAbsolute(move);
	resize.updateInts();	
	move.updateInts();

	request.setSizeDelta(resize);
	request.setMoveDelta(move);
}

public void snapMoveRequest(ChangeBoundsRequest req, PrecisionRectangle baseRect) {
	PrecisionPoint raw = new PrecisionPoint(req.getMoveDelta());
	
	IFigure fig = container.getContentPane();
	baseRect.translate(raw);
	fig.translateToRelative(baseRect);
	fig.translateToRelative(raw);

	double xCorrection = Math.IEEEremainder(baseRect.preciseX, gridX);
	double yCorrection = Math.IEEEremainder(baseRect.preciseY, gridY);
	
	raw.preciseX -= xCorrection;
	raw.preciseY -= yCorrection;
	raw.performTranslate(0, 0);
	fig.translateToAbsolute(raw);
	req.setMoveDelta(raw);
}

}
