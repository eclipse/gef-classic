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
	
public int snapLocation(Request request, PrecisionPoint location, int snapOrientation) {
	Rectangle.SINGLETON.setSize(0, 0);
	PrecisionRectangle baseRect = new PrecisionRectangle(Rectangle.SINGLETON);
	baseRect.preciseX = location.preciseX;
	baseRect.preciseY = location.preciseY;
	baseRect.updateInts();
	snapOrientation = snapResizeRect(request, baseRect, snapOrientation & ~SOUTH_EAST);
	location.preciseX = baseRect.preciseX;
	location.preciseY = baseRect.preciseY;
	location.updateInts();
	return snapOrientation;
}

/**
 * Note that even if the selectionRect is snapped, it's the baseRect that is modified
 * to indicate the adjustment amount.
 */
public int snapRectangle(Request request, PrecisionRectangle baseRect, 
		PrecisionRectangle selectionRect, boolean canResize, int snapOrientation) {
	if (canResize)
		return snapResizeRect(request, baseRect, snapOrientation);
	else
		return snapMoveRect(request, baseRect, selectionRect, snapOrientation);
}

protected int snapMoveRect(Request request, PrecisionRectangle baseRect,
		PrecisionRectangle compoundRect, int snapOrientation) {
	return snapOrientation;
}

protected int snapResizeRect(Request request, PrecisionRectangle baseRect, 
		int snapOrientation) {
	return snapOrientation;
}

}