package org.eclipse.gef;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * @author Randy Hudson
 * @author Pratik Shah
 */
public class SnapToGrid
	extends SnapToHelper 
{
	
public static final String PROPERTY_GRID_ENABLED = "SnapToGrid + $Property"; //$NON-NLS-1$
public static final String PROPERTY_GRID_SPACING = "SnapToGrid - Grid Spacing"; //$NON-NLS-1$
public static final String PROPERTY_GRID_ORIGIN = "SnapToGrid - $Grid Origin"; //$NON-NLS-1$
public static final int DEFAULT_GAP = 12;
	
protected GraphicalEditPart container;
protected int gridX, gridY;
protected Point origin;

/**
 * Constructs a snap-to-grid strategy on the given editpart.  The editpart should be the
 * graphical editpart whose content-pane figure is used as the reference for the grid.
 * @param container the editpart which owns the grid
 */
public SnapToGrid(GraphicalEditPart gep) {
	container = gep;
	Dimension spacing = (Dimension)container.getViewer()
			.getProperty(PROPERTY_GRID_SPACING);
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

protected int performCenteredResize(PrecisionRectangle baseRect, int snapOrientation) {
	if ((snapOrientation & EAST_WEST) != 0 && gridX > 0) {
		double leftCorrection = Math.IEEEremainder(
				baseRect.preciseX - origin.x, gridX);
		double rightCorrection = Math.IEEEremainder(
				baseRect.preciseRight() - origin.x, gridX);
		if (Math.abs(leftCorrection) <= Math.abs(rightCorrection)) {
			baseRect.preciseWidth += (leftCorrection * 2);
			baseRect.preciseX -= leftCorrection;
		} else {
			baseRect.preciseWidth -= (2 * rightCorrection);
			baseRect.preciseX += rightCorrection;
		}
		snapOrientation &= ~EAST_WEST;
	}
	
	if ((snapOrientation & NORTH_SOUTH) != 0 && gridY > 0) {
		double topCorrection = Math.IEEEremainder(baseRect.preciseY - origin.y, gridY);
		double bottom = Math.IEEEremainder(baseRect.preciseBottom() - origin.y, gridY);
		if (Math.abs(topCorrection) <= Math.abs(bottom)) {
			baseRect.preciseHeight += (topCorrection * 2);
			baseRect.preciseY -= topCorrection;
		} else {
			baseRect.preciseHeight -= (bottom * 2);
			baseRect.preciseY += bottom;
		}
		snapOrientation &= ~NORTH_SOUTH;
	}
	
	return snapOrientation;
}

protected int snapMoveRect(Request request, PrecisionRectangle baseRect,
		PrecisionRectangle compoundRect, int snapOrientation) {
	makeRelative(container.getContentPane(), baseRect);

	double xCorrection = 0.0, yCorrection = 0.0;
	if ((snapOrientation & WEST) != 0 && gridX > 0)
		xCorrection = Math.IEEEremainder(baseRect.preciseX - origin.x, gridX);
	if ((snapOrientation & NORTH) != 0 && gridY > 0)
		yCorrection = Math.IEEEremainder(baseRect.preciseY - origin.y, gridY);
	
	baseRect.preciseX -= xCorrection;
	baseRect.preciseY -= yCorrection;
	makeAbsolute(container.getContentPane(), baseRect);
	baseRect.updateInts();
	return NONE;
}

protected int snapResizeRect(Request request, PrecisionRectangle baseRect, 
		int snapOrientation) {	
	makeRelative(container.getContentPane(), baseRect);

	if (request instanceof ChangeBoundsRequest 
			&& ((ChangeBoundsRequest)request).isCenteredResize()) {
		snapOrientation = performCenteredResize(baseRect, snapOrientation);
	} else {
		if ((snapOrientation & EAST) != 0 && gridX > 0) {
			double rightCorrection = Math.IEEEremainder(
					baseRect.preciseRight() - origin.x, gridX);
			baseRect.preciseWidth -= rightCorrection;
			snapOrientation &= ~EAST;
		}
		if ((snapOrientation & WEST) != 0 && gridX > 0) {
			double leftCorrection = Math.IEEEremainder(
					baseRect.preciseX - origin.x, gridX);
			baseRect.preciseWidth += leftCorrection;
			baseRect.preciseX -= leftCorrection;
			snapOrientation &= ~WEST;
		}
		if ((snapOrientation & SOUTH) != 0 && gridY > 0) {
			double bottom = Math.IEEEremainder(
					baseRect.preciseBottom() - origin.y, gridY);
			baseRect.preciseHeight -= bottom;
			snapOrientation &= ~SOUTH;
		}
		if ((snapOrientation & NORTH) != 0 && gridY > 0) {
			double topCorrection = Math.IEEEremainder(
					baseRect.preciseY - origin.y, gridY);
			baseRect.preciseHeight += topCorrection;
			baseRect.preciseY -= topCorrection;
			snapOrientation &= ~NORTH;
		}
	}

	makeAbsolute(container.getContentPane(), baseRect);
	baseRect.updateInts();	
	return snapOrientation;
}

}