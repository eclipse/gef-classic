package org.eclipse.gef;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author Randy Hudson
 * @author Pratik Shah
 */
public class SnapToGrid implements SnapToHelper {
	
public static final String PROPERTY_GRID_ENABLED = "SnapToGrid + $Property"; //$NON-NLS-1$
public static final String PROPERTY_GRID_SPACING = "SnapToGrid - Grid Spacing"; //$NON-NLS-1$
public static final String PROPERTY_GRID_ORIGIN = "SnapToGrid - $Grid Origin"; //$NON-NLS-1$
public static final int DEFAULT_GAP = 12;
	
private GraphicalEditPart container;
private int gridX, gridY;
private Point origin;

/**
 * Constructs a snap-to-grid strategy on the given editpart.  The editpart should be the
 * graphical editpart whose content-pane figure is used as the reference for the grid.
 * @param container the editpart which owns the grid
 */
public SnapToGrid(GraphicalEditPart gep) {
	container = gep;
	Dimension spacing = (Dimension)container.getViewer().getProperty(PROPERTY_GRID_SPACING);
	if (spacing != null) {
		gridX = spacing.width;
		gridY = spacing.height;
	}
	if (gridX == 0)
		gridX = DEFAULT_GAP;
	if (gridY == 0)
		gridY = DEFAULT_GAP;
	Point loc = (Point)container.getViewer().getProperty(PROPERTY_GRID_ORIGIN);
	if (loc != null)
		origin = loc;
	else
		origin = new Point();
}

public int snapCreateRequest(CreateRequest request, PrecisionRectangle baseRect,
                             int snapOrientation) {
	if (snapOrientation == NONE)
		return snapOrientation;
	
	IFigure fig = container.getContentPane();
	fig.translateToRelative(baseRect);
//	System.out.println(baseRect);
	if ((snapOrientation & WEST) != 0 && gridX > 0) {
		double xCorrection = Math.IEEEremainder(baseRect.preciseX - origin.x, gridX);
		baseRect.preciseX -= xCorrection;
		baseRect.preciseWidth += xCorrection;
//		System.out.println("leftCorrection: " + (0 - xCorrection));
//		baseRect.updateInts();
//		System.out.println(baseRect);
	}
	if ((snapOrientation & EAST) != 0 && gridX > 0) {
		double rightCorrection = Math.IEEEremainder(
				baseRect.preciseRight() - origin.x, gridX);
		baseRect.preciseWidth -= rightCorrection;
//		System.out.println("rightCorrection: " + (0 - rightCorrection));
//		baseRect.updateInts();
//		System.out.println(baseRect);
	}

	if ((snapOrientation & NORTH) != 0 && gridY > 0) {
		double yCorrection = Math.IEEEremainder(baseRect.preciseY - origin.y, gridY);
		baseRect.preciseY -= yCorrection;
		baseRect.preciseHeight += yCorrection;
	}
	if ((snapOrientation & SOUTH) != 0 && gridY > 0) {
		double bottom = Math.IEEEremainder(baseRect.preciseBottom() - origin.y, gridY);
		baseRect.preciseHeight -= bottom;
	}
	baseRect.updateInts();
//	System.out.println(baseRect);
//	System.out.println("=================="); 
	fig.translateToAbsolute(baseRect);
	baseRect.updateInts();
	request.setLocation(baseRect.getLocation());
	request.setSize(baseRect.getSize());

	return NONE;
}

public int snapMoveRequest(ChangeBoundsRequest request, PrecisionRectangle baseRect,
                           PrecisionRectangle selectionRect, int snapOrientation) {
	PrecisionPoint move = new PrecisionPoint(request.getMoveDelta());
	
	IFigure fig = container.getContentPane();
	baseRect.translate(move);
	fig.translateToRelative(baseRect);
	fig.translateToRelative(move);

	double xCorrection = 0.0, yCorrection = 0.0;
	if ((snapOrientation & EAST_WEST) != 0 && gridX > 0)
		xCorrection = Math.IEEEremainder(baseRect.preciseX - origin.x, gridX);
	if ((snapOrientation & NORTH_SOUTH) != 0 && gridY > 0)
		yCorrection = Math.IEEEremainder(baseRect.preciseY - origin.y, gridY);
	
	move.preciseX -= xCorrection;
	move.preciseY -= yCorrection;
	move.updateInts();
	fig.translateToAbsolute(move);
	request.setMoveDelta(move);
	return NONE;
}

public int snapResizeRequest(ChangeBoundsRequest request, PrecisionRectangle baseRect,
                             int snapOrientation) {
	int dir = request.getResizeDirection();
	PrecisionDimension resize = new PrecisionDimension(request.getSizeDelta());
	PrecisionPoint move = new PrecisionPoint(request.getMoveDelta());
	
	IFigure fig = container.getContentPane();
	baseRect.resize(resize);
	baseRect.translate(move);
	
	fig.translateToRelative(baseRect);
	fig.translateToRelative(resize);
	fig.translateToRelative(move);

	if ((snapOrientation & EAST_WEST) != 0 && gridX > 0)
		if (request.isCenteredResize()) {
			double leftCorrection = Math.IEEEremainder(
					baseRect.preciseX - origin.x, gridX);
			double rightCorrection = Math.IEEEremainder(
					baseRect.preciseRight() - origin.x, gridX);
			if (Math.abs(leftCorrection) <= Math.abs(rightCorrection)) {
				resize.preciseWidth += (leftCorrection * 2);
				move.preciseX -= leftCorrection;
			} else {
				resize.preciseWidth -= (2 * rightCorrection);
				move.preciseX += rightCorrection;
			}
		} else if ((dir & EAST) != 0) {
			double rightCorrection = Math.IEEEremainder(
					baseRect.preciseRight() - origin.x, gridX);
			resize.preciseWidth -= rightCorrection;
		} else if ((dir & WEST) != 0) {
			double leftCorrection = Math.IEEEremainder(
					baseRect.preciseX - origin.x, gridX);
			resize.preciseWidth += leftCorrection;
			move.preciseX -= leftCorrection;
		}
	
	if ((snapOrientation & NORTH_SOUTH) != 0 && gridY > 0)
		if (request.isCenteredResize()) {
			double topCorrection = Math.IEEEremainder(baseRect.preciseY - origin.y, gridY);
			double bottom = Math.IEEEremainder(baseRect.preciseBottom() - origin.y, gridY);
			if (Math.abs(topCorrection) <= Math.abs(bottom)) {
				resize.preciseHeight += (topCorrection * 2);
				move.preciseY -= topCorrection;
			} else {
				resize.preciseHeight -= (bottom * 2);
				move.preciseY += bottom;
			}
		} else if ((dir & SOUTH) != 0) {
			double bottom = Math.IEEEremainder(baseRect.preciseBottom() - origin.y, gridY);
			resize.preciseHeight -= bottom;
		} else if ((dir & NORTH) != 0) {
			double topCorrection = Math.IEEEremainder(baseRect.preciseY - origin.y, gridY);
			resize.preciseHeight += topCorrection;
			move.preciseY -= topCorrection;
		}

	fig.translateToAbsolute(resize);
	fig.translateToAbsolute(move);
	resize.updateInts();	
	move.updateInts();

	request.setSizeDelta(resize);
	request.setMoveDelta(move);
	return NONE;
}

}