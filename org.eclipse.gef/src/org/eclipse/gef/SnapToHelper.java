package org.eclipse.gef;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;

/**
 * Tools use SnapToHelpers on a per-operation basis.  For example, for a move operation,
 * the life-cycle of a SnapToHelper begins when a drag is initiated, and ends when the
 * drag is over.  If another drag is initiated right after the first one is completed,
 * new SnapToHelpers are employed.  The same applies for creation and resize operations.
 * 
 * @author Randy Hudson
 * @author Pratik Shah
 */
public abstract class SnapToHelper 
	implements PositionConstants
{
	
protected void makeAbsolute(IFigure fig, Translatable t) {
	fig.translateToAbsolute(t);
}

protected void makeRelative(IFigure fig, Translatable t) {
	fig.translateToRelative(t);
}
	
public int snapLocation(Request request, PrecisionPoint location, PrecisionPoint result, 
		int snapOrientation) {
	PrecisionRectangle baseRect = new PrecisionRectangle(new Rectangle());
	PrecisionRectangle resultRect = baseRect.getPreciseCopy();
	baseRect.translate(location);
	resultRect.translate(result);
	snapOrientation = snapResizeRect(request, baseRect, resultRect,
			snapOrientation & ~SOUTH_EAST);
	result.preciseX = resultRect.preciseX;
	result.preciseY = result.preciseY;
	result.updateInts();
	return snapOrientation;
}

/**
 * Note that even if the selectionRect is snapped, it's the baseRect that is modified
 * to indicate the adjustment amount.
 */
public int snapRectangle(Request request, PrecisionRectangle baseRect, 
		PrecisionRectangle result, boolean canResize, int snapOrientation) {
	if (canResize)
		return snapResizeRect(request, baseRect, result, snapOrientation);
	else
		return snapMoveRect(request, baseRect, result, snapOrientation);
}

protected int snapMoveRect(Request request, PrecisionRectangle baseRect, 
		PrecisionRectangle result, int snapOrientation) {
	return snapOrientation;
}

protected int snapResizeRect(Request request, PrecisionRectangle baseRect, 
		PrecisionRectangle result, int snapOrientation) {
	return snapOrientation;
}

}