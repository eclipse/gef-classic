package org.eclipse.gef;

import org.eclipse.draw2d.geometry.*;

/**
 * @author Randy Hudson
 * @author Pratik Shah
 */
public class SnapToGrid
	extends SnapToHelper
{
	
public static final String PROPERTY_GRID_ENABLED = "SnapToGrid.isEnabled"; //$NON-NLS-1$
public static final String PROPERTY_GRID_VISIBLE = "SnapToGrid.isVisible"; //$NON-NLS-1$
public static final String PROPERTY_GRID_SPACING = "SnapToGrid.GridSpacing"; //$NON-NLS-1$
public static final String PROPERTY_GRID_ORIGIN = "SnapToGrid.GridOrigin"; //$NON-NLS-1$
public static final int DEFAULT_GAP = 12;

protected GraphicalEditPart container;
protected int gridX;
protected int gridY;
protected Point origin;

/**
 * Constructs a gridded snap helper on the given editpart.  The editpart should be the
 * graphical editpart whose contentspane figure is used as the reference for the grid.
 * @param container the editpart which the grid is on
 */
public SnapToGrid(GraphicalEditPart container) {
	this.container = container;
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

/**
 * @see SnapToHelper#snapRectangle(Request, int, PrecisionRectangle, PrecisionRectangle)
 */
public int snapRectangle(Request request, int snapLocations,
		PrecisionRectangle rect, PrecisionRectangle result) {
	
	rect = rect.getPreciseCopy();
	makeRelative(container.getContentPane(), rect);
	PrecisionRectangle correction = new PrecisionRectangle();
	makeRelative(container.getContentPane(), correction);
	
	if (gridX > 0 && (snapLocations & EAST) != 0) {
		correction.preciseWidth -= Math.IEEEremainder(rect.preciseRight() - origin.x, gridX);
		snapLocations &= ~EAST;
	}
	
	if ((snapLocations & (WEST | HORIZONTAL)) != 0 && gridX > 0) {
		double leftCorrection = Math.IEEEremainder(rect.preciseX - origin.x, gridX);
		correction.preciseX -= leftCorrection;
		if ((snapLocations & HORIZONTAL) == 0)
			correction.preciseWidth += leftCorrection;
		snapLocations &= ~(WEST | HORIZONTAL);
	}
	
	if ((snapLocations & SOUTH) != 0 && gridY > 0) {
		correction.preciseHeight -= Math.IEEEremainder(rect.preciseBottom() - origin.y, gridY);
		snapLocations &= ~SOUTH;
	}
	
	if ((snapLocations & (NORTH | VERTICAL)) != 0 && gridY > 0) {
		double topCorrection = Math.IEEEremainder(
				rect.preciseY - origin.y, gridY);
		correction.preciseY -= topCorrection;
		if ((snapLocations & VERTICAL) == 0)
			correction.preciseHeight += topCorrection;
		snapLocations &= ~(NORTH | VERTICAL);
	}

	correction.updateInts();
	makeAbsolute(container.getContentPane(), correction);
	result.preciseX += correction.preciseX;
	result.preciseY += correction.preciseY;
	result.preciseWidth += correction.preciseWidth;
	result.preciseHeight += correction.preciseHeight;
	result.updateInts();

	return snapLocations;
}

}