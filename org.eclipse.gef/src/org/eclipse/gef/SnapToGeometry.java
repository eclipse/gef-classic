package org.eclipse.gef;

import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author Randy Hudson
 */
public class SnapToGeometry 
	implements SnapToHelper 
{
	
public static final String PROPERTY_SNAP_ENABLED = "SnapToGeometry - $Property"; //$NON-NLS-1$	
public static final String PROPERTY_HORIZONTAL_ANCHOR = "SnapToGeometry - $H Anchor"; //$NON-NLS-1$
public static final String PROPERTY_VERTICAL_ANCHOR = "SnapToGeometry - $V Anchor"; //$NON-NLS-1$

protected static class Entry {
	int side;
	int offset;
	
	Entry(int side, int offset) {
		this.side = side;
		this.offset = offset;
	}
}

protected static final double THRESHOLD = 5.0001;
protected boolean cachedCloneBool;
protected Entry rows[];
protected Entry cols[];
protected GraphicalEditPart container;

public SnapToGeometry(GraphicalEditPart container) {
	this.container = container;
}

/**
 * Returns the correction value for the given entries and sides.  During a move, the left,
 * right, or center is free to snap to a location.
 * @param entries the entries
 * @param near the left/top side
 * @param far the right/bottom side
 * @return the correction amount or THRESHOLD if no correction is required
 */
private double getCorrectionFor(Entry entries[], Map extendedData, boolean vert, 
                                double near, double far) {
	double result = getCorrectionFor(entries, extendedData, vert, (near + far - 1) / 2, 0);
	if (result == THRESHOLD)
		result = getCorrectionFor(entries, extendedData, vert, near, -1);
	if (result == THRESHOLD)
		result = getCorrectionFor(entries, extendedData, vert, far, 1);
	return result;
}

/**
 * Returns the correction value between ± {@link #THRESHOLD}, or the THRESHOLD if no
 * corrections were found.
 * @param entries the entries
 * @param value
 * @param side
 * @return
 */
private double getCorrectionFor(Entry entries[], Map extendedData, boolean vert, 
                                double value, int side) {
	double resultMag = THRESHOLD;
	double result = THRESHOLD;
	
	for (int i = 0; i < entries.length; i++) {
		Entry entry = entries[i];
		double magnitude;
		
		if (entry.side == -1 && side != 0) {
			magnitude = Math.abs(value - entry.offset);
			if (magnitude < resultMag) {
				resultMag = magnitude;
				result = entry.offset - value;
				extendedData.put(vert ? PROPERTY_VERTICAL_ANCHOR : PROPERTY_HORIZONTAL_ANCHOR, 
						new Integer(entry.offset));
			}
		} else if (entry.side == 0 && side == 0) {
			magnitude = Math.abs(value - entry.offset);
			if (magnitude < resultMag) {
				resultMag = magnitude;
				result = entry.offset - value;
				extendedData.put(vert ? PROPERTY_VERTICAL_ANCHOR : PROPERTY_HORIZONTAL_ANCHOR, 
									  new Integer(entry.offset));
			}
		} else if (entry.side == 1 && side != 0) {
			magnitude = Math.abs(value - entry.offset);
			if (magnitude < resultMag) {
				resultMag = magnitude;
				result = entry.offset - value;
				extendedData.put(vert ? PROPERTY_VERTICAL_ANCHOR : PROPERTY_HORIZONTAL_ANCHOR, 
									  new Integer(entry.offset));
			}
		}
	}
	return result;
}

protected Rectangle getFigureBounds(IFigure fig) {
	if (fig instanceof HandleBounds)
		return ((HandleBounds)fig).getHandleBounds();
	else
		return fig.getBounds();
}

protected void populateRowsAndCols(List exclude, boolean isClone) {
	if (rows == null || cols == null || isClone != cachedCloneBool) {
		// If the operation changes from a move to a clone, or vice versa, the rows and
		// cols need to be populated again.
		cachedCloneBool = isClone;

		// Don't snap to any figure that is being dragged
		List children = new ArrayList(container.getChildren());
		children.removeAll(exclude);
		
		// Don't snap to hidden figures
		List hiddenChildren = new ArrayList();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			GraphicalEditPart child = (GraphicalEditPart) iter.next();
			if (!child.getFigure().isVisible())
				hiddenChildren.add(child);
		}
		children.removeAll(hiddenChildren);
		
		// Cache the edges that can be snapped to
		rows = new Entry[children.size() * 3];
		cols = new Entry[children.size() * 3];
		for (int i = 0; i < children.size(); i++) {
			GraphicalEditPart child = (GraphicalEditPart)children.get(i);
			Rectangle bounds = getFigureBounds(child.getFigure());
			cols[i * 3] = new Entry(-1, bounds.x);
			rows[i * 3] = new Entry(-1, bounds.y);
			cols[i * 3 + 1] = new Entry(0, bounds.x + bounds.width / 2);
			rows[i * 3 + 1] = new Entry(0, bounds.y + bounds.height / 2);
			cols[i * 3 + 2] = new Entry(1, bounds.right());
			rows[i * 3 + 2] = new Entry(1, bounds.bottom());
		}
	}
}

public int snapCreateRequest(CreateRequest request, PrecisionRectangle baseRect,
                             int snapOrientation) {
	return snapOrientation;
}

/**
 * @see SnapToHelper#snapMoveRequest(ChangeBoundsRequest, PrecisionRectangle)
 */
public int snapMoveRequest(ChangeBoundsRequest request,	PrecisionRectangle baseRect,
		PrecisionRectangle selectionRect, int snapOrientation) {
	if (request.getType().equals(RequestConstants.REQ_CLONE))
		populateRowsAndCols(Collections.EMPTY_LIST, true);
	else
		populateRowsAndCols(request.getEditParts(), false);
	PrecisionPoint move = new PrecisionPoint(request.getMoveDelta());
	IFigure fig = container.getContentPane();
	baseRect.translate(move);
	selectionRect.translate(move);
	fig.translateToRelative(baseRect);
	fig.translateToRelative(selectionRect);
	fig.translateToRelative(move);

	double xcorrect = THRESHOLD, ycorrect = THRESHOLD;
	if ((snapOrientation & SNAP_VERTICAL) != 0) {
		xcorrect = getCorrectionFor(cols, request.getExtendedData(), true, 
				baseRect.preciseX, baseRect.preciseRight());
		if (xcorrect == THRESHOLD)
			xcorrect = getCorrectionFor(cols, request.getExtendedData(), true, 
					selectionRect.preciseX, selectionRect.preciseRight());
	}
	if ((snapOrientation & SNAP_HORIZONTAL) != 0) {
		ycorrect = getCorrectionFor(rows, request.getExtendedData(), false, 
				baseRect.preciseY, baseRect.preciseBottom());
		if (ycorrect == THRESHOLD)
			ycorrect = getCorrectionFor(rows, request.getExtendedData(), false, 
					selectionRect.preciseY, selectionRect.preciseBottom());
	}

	//If neither value is being corrected, return false
	if (xcorrect == THRESHOLD && ycorrect == THRESHOLD)
		return snapOrientation;

	if (xcorrect != THRESHOLD)
		// Clear the SNAP_VERTICAL flag
		snapOrientation = snapOrientation & SNAP_HORIZONTAL;
	if (ycorrect != THRESHOLD)
		// Clear the SNAP_HORIZONTAL flag
		snapOrientation = snapOrientation & SNAP_VERTICAL;
	
	if (ycorrect == THRESHOLD)  {
		ycorrect = 0.0;
	} else if (xcorrect == THRESHOLD) {
		xcorrect = 0.0;
	}
		
	move.preciseX += xcorrect;
	move.preciseY += ycorrect;
	move.updateInts();
	fig.translateToAbsolute(move);
	request.setMoveDelta(move);
	
	return snapOrientation;
}

/**
 * @see SnapToHelper#snapResizeRequest(ChangeBoundsRequest, PrecisionRectangle)
 */
public int snapResizeRequest(ChangeBoundsRequest request, PrecisionRectangle baseRect,
                             int snapOrientation) {
	int dir = request.getResizeDirection();
	PrecisionDimension resize = new PrecisionDimension(request.getSizeDelta());
	PrecisionPoint move = new PrecisionPoint(request.getMoveDelta());
	IFigure fig = container.getContentPane();

	populateRowsAndCols(request.getEditParts(), false);	
	
	baseRect.resize(resize);
	baseRect.translate(move);
	
	fig.translateToRelative(baseRect);
	fig.translateToRelative(resize);
	fig.translateToRelative(move);

	
	boolean change = false;
	if ((snapOrientation & SNAP_VERTICAL) != 0)
		if ((dir & PositionConstants.EAST) != 0) {
			double rightCorrection = getCorrectionFor(cols, request.getExtendedData(), true,
					baseRect.preciseRight(), 1);
			if (rightCorrection != THRESHOLD) {
				change = true;
				snapOrientation = snapOrientation & SNAP_HORIZONTAL;
				resize.preciseWidth += rightCorrection;
			}
		} else if ((dir & PositionConstants.WEST) != 0) {
			double leftCorrection = getCorrectionFor(cols, request.getExtendedData(), true,
					baseRect.preciseX, -1);
			if (leftCorrection != THRESHOLD) {
				change = true;
				snapOrientation = snapOrientation & SNAP_HORIZONTAL;
				resize.preciseWidth -= leftCorrection;
				move.preciseX += leftCorrection;
			}
		}
	
	if ((snapOrientation & SNAP_HORIZONTAL) != 0)
		if ((dir & PositionConstants.SOUTH) != 0) {
			double bottom = getCorrectionFor(rows, request.getExtendedData(), false,
					baseRect.preciseBottom(), 1);
			if (bottom != THRESHOLD) {
				change = true;
				snapOrientation = snapOrientation & SNAP_VERTICAL;
				resize.preciseHeight += bottom;
			}
		} else if ((dir & PositionConstants.NORTH) != 0) {
			double topCorrection = getCorrectionFor(rows, request.getExtendedData(), false,
					baseRect.preciseY, -1);
			if (topCorrection != THRESHOLD) {
				change = true;
				snapOrientation = snapOrientation & SNAP_VERTICAL;
				resize.preciseHeight -= topCorrection;
				move.preciseY += topCorrection;
			}
		}
	
	if (!change)
		return snapOrientation;
	
	fig.translateToAbsolute(resize);
	fig.translateToAbsolute(move);
	resize.updateInts();
	move.updateInts();

	request.setSizeDelta(resize);
	request.setMoveDelta(move);
	
	return snapOrientation;
}

}