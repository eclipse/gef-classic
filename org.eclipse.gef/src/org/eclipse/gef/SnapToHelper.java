package org.eclipse.gef;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.*;

/**
 * Tools use SnapToHelpers on a per-operation basis. For example, for a move operation,
 * the life-cycle of a SnapToHelper begins when a drag is initiated, and ends when the
 * drag is over. If another drag is initiated right after the first one is completed, new
 * SnapToHelpers are employed. The same applies for creation and resize operations.
 * 
 * @author Randy Hudson
 * @author Pratik Shah
 */
public abstract class SnapToHelper implements PositionConstants {

protected void makeAbsolute(IFigure fig, Translatable t) {
	fig.translateToAbsolute(t);
}

protected void makeRelative(IFigure fig, Translatable t) {
	fig.translateToRelative(t);
}

/**
 * Applies a snapping correction to the given result paremeter.
 * @since 3.0
 * @param request a request or <code>null</code>
 * @param snapDirections the directions in which snapping should occur.
 * @param where the rectangle used to determine snapping
 * @param result the result
 * @return the remaining snap locations
 */
public int snapPoint(Request request, int snapDirections,
		PrecisionPoint where, PrecisionPoint result) {
	PrecisionRectangle rect = new PrecisionRectangle();
	PrecisionRectangle resultRect = new PrecisionRectangle();
	rect.preciseX = where.preciseX;
	rect.preciseY = where.preciseY;
	rect.updateInts();
	
	snapDirections = snapRectangle(request, snapDirections,
			rect, resultRect);
	result.preciseX += resultRect.preciseX;
	result.preciseY += resultRect.preciseY;
	result.updateInts();
	return snapDirections;
}

public int snapPoint(Request request, int snapLocations,
		PrecisionRectangle rects[], PrecisionPoint result) {
	PrecisionRectangle resultRect = new PrecisionRectangle();
	snapLocations = snapRectangle(request, snapLocations, rects, resultRect);

	result.preciseX += resultRect.preciseX;
	result.preciseY += resultRect.preciseY;
	result.updateInts();
	
	return snapLocations;
}

/**
 * Applies the snap correction to the given result parameter.
 * @since 3.0
 * @param request the request
 * @param baseRects the prioritized rectangle to snap to
 * @param result the output
 * @param snapOrientation the input snap locations
 * @return the remaining snap locations
 */
public int snapRectangle(Request request, int snapOrientation,
		PrecisionRectangle baseRects[], PrecisionRectangle result) {

	for (int i = 0; i < baseRects.length && snapOrientation != 0; i++)
		snapOrientation = snapRectangle(request, snapOrientation, baseRects[i], result);
	
	return snapOrientation;
}

/**
 * Applies the snap correction to the given result parameter.
 * @since 3.0
 * @param request the request
 * @param baseRect the rectangle with which to snap
 * @param result the output
 * @param snapOrientation the input snap locations
 * @return the remaining snap locations
 */
public abstract int snapRectangle(Request request, int snapOrientation,
		PrecisionRectangle baseRect, PrecisionRectangle result);

}