package org.eclipse.gef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.GroupRequest;

/**
 * @author Randy Hudson
 * @author Pratik Shah
 */
public class SnapToGeometry 
	extends SnapToHelper 
{
	
public static final String PROPERTY_SNAP_TO_GEOM_ENABLED = "org.eclipse.gef.geom.snap"; //$NON-NLS-1$	
public static final String KEY_NORTH_ANCHOR = "org.eclipse.gef.geom.nAnchor"; //$NON-NLS-1$
public static final String KEY_SOUTH_ANCHOR = "org.eclipse.gef.geom.sAnchor"; //$NON-NLS-1$
public static final String KEY_WEST_ANCHOR = "org.eclipse.gef.geom.wAnchor"; //$NON-NLS-1$
public static final String KEY_EAST_ANCHOR = "org.eclipse.gef.geom.eAnchor"; //$NON-NLS-1$

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

protected List generateSnapPartsList(List operationSet) {
	// Don't snap to any figure that is being dragged
	List children = new ArrayList(container.getChildren());
	children.removeAll(operationSet);
	
	// Don't snap to hidden figures
	List hiddenChildren = new ArrayList();
	for (Iterator iter = children.iterator(); iter.hasNext();) {
		GraphicalEditPart child = (GraphicalEditPart) iter.next();
		if (!child.getFigure().isVisible())
			hiddenChildren.add(child);
	}
	children.removeAll(hiddenChildren);

	return children;
}

/**
 * Returns the correction value for the given entries and sides.  During a move, the left,
 * right, or center is free to snap to a location.
 * @param entries the entries
 * @param near the left/top side
 * @param far the right/bottom side
 * @return the correction amount or THRESHOLD if no correction is required
 */
protected double getCorrectionFor(Entry entries[], Map extendedData, boolean vert, 
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
protected double getCorrectionFor(Entry entries[], Map extendedData, boolean vert, 
                                double value, int side) {
	double resultMag = THRESHOLD;
	double result = THRESHOLD;
	
	String property;
	if (side == -1)
		property = vert ? KEY_WEST_ANCHOR : KEY_NORTH_ANCHOR;
	else
		property = vert ? KEY_EAST_ANCHOR : KEY_SOUTH_ANCHOR;
	
	for (int i = 0; i < entries.length; i++) {
		Entry entry = entries[i];
		double magnitude;
		
		if (entry.side == -1 && side != 0) {
			magnitude = Math.abs(value - entry.offset);
			if (magnitude < resultMag) {
				resultMag = magnitude;
				result = entry.offset - value;
				extendedData.put(property, new Integer(entry.offset));
			}
		} else if (entry.side == 0 && side == 0) {
			magnitude = Math.abs(value - entry.offset);
			if (magnitude < resultMag) {
				resultMag = magnitude;
				result = entry.offset - value;
				extendedData.put(property, new Integer(entry.offset));
			}
		} else if (entry.side == 1 && side != 0) {
			magnitude = Math.abs(value - entry.offset);
			if (magnitude < resultMag) {
				resultMag = magnitude;
				result = entry.offset - value;
				extendedData.put(property, new Integer(entry.offset));
			}
		}
	}
	return result;
}

protected Rectangle getFigureBounds(GraphicalEditPart part) {
	IFigure fig = part.getFigure();
	if (fig instanceof HandleBounds)
		return ((HandleBounds)fig).getHandleBounds();
	else
		return fig.getBounds();
}

/*
 * @TODO:Pratik   What about constrained resize?  For both centered and constrained 
 * resize, you could do the adjusting in the tools and then it would be in just one place, 
 * instead of being in every SnapToHelper.  You wouldn't need such special-case methods
 * either.
 */
protected int performCenteredResize(Request request, PrecisionRectangle baseRect,
		PrecisionRectangle result, int snapOrientation) {
	if ((snapOrientation & EAST_WEST) != 0) {
		double leftCorrection = getCorrectionFor(cols, request.getExtendedData(), 
				true, baseRect.preciseX, -1);
		double rightCorrection = getCorrectionFor(cols, request.getExtendedData(), 
				true, baseRect.preciseRight(), 1);
		if (Math.abs(leftCorrection) <= Math.abs(rightCorrection)
				&& leftCorrection != THRESHOLD) {
			snapOrientation &= ~EAST_WEST;
			result.preciseWidth -= (leftCorrection * 2);
			result.preciseX += leftCorrection;
			request.getExtendedData().remove(KEY_EAST_ANCHOR);
		} else if (rightCorrection != THRESHOLD) {
			snapOrientation &= ~EAST_WEST;
			result.preciseWidth += (rightCorrection * 2);
			result.preciseX -= rightCorrection;
			request.getExtendedData().remove(KEY_WEST_ANCHOR);
		}
	}
	
	if ((snapOrientation & NORTH_SOUTH) != 0) {
		double topCorrection = getCorrectionFor(rows, request.getExtendedData(), 
				false, baseRect.preciseY, -1);
		double bottom = getCorrectionFor(rows, request.getExtendedData(), false,
				baseRect.preciseBottom(), 1);
		if (Math.abs(topCorrection) <= Math.abs(bottom)
				&& topCorrection != THRESHOLD) {
			snapOrientation &= ~NORTH_SOUTH;
			result.preciseHeight -= (topCorrection * 2);
			result.preciseY += topCorrection;
			request.getExtendedData().remove(KEY_SOUTH_ANCHOR);
		} else if (bottom != THRESHOLD) {
			snapOrientation &= ~NORTH_SOUTH;
			result.preciseHeight += (bottom * 2);
			result.preciseY -= bottom;
			request.getExtendedData().remove(KEY_NORTH_ANCHOR);
		}
	}

	return snapOrientation;
}

protected void populateRowsAndCols(List parts) {
	rows = new Entry[parts.size() * 3];
	cols = new Entry[parts.size() * 3];
	for (int i = 0; i < parts.size(); i++) {
		GraphicalEditPart child = (GraphicalEditPart)parts.get(i);
		Rectangle bounds = getFigureBounds(child);
		cols[i * 3] = new Entry(-1, bounds.x);
		rows[i * 3] = new Entry(-1, bounds.y);
		cols[i * 3 + 1] = new Entry(0, bounds.x + bounds.width / 2);
		rows[i * 3 + 1] = new Entry(0, bounds.y + bounds.height / 2);
		cols[i * 3 + 2] = new Entry(1, bounds.right());
		rows[i * 3 + 2] = new Entry(1, bounds.bottom());
	}
}

protected int snapMoveRect(Request request, PrecisionRectangle baseRect,
		PrecisionRectangle result, int snapOrientation) {
	boolean isClone = request.getType().equals(RequestConstants.REQ_CLONE);
	if (rows == null || cols == null || isClone != cachedCloneBool) {
		cachedCloneBool = isClone;
		List exclusionSet = Collections.EMPTY_LIST;
		if (!isClone && request instanceof GroupRequest)
			exclusionSet = ((GroupRequest)request).getEditParts();
		populateRowsAndCols(generateSnapPartsList(exclusionSet));
	}

	makeRelative(container.getContentPane(), baseRect);

	double xcorrect = THRESHOLD, ycorrect = THRESHOLD;
	if ((snapOrientation & EAST_WEST) != 0)
		xcorrect = getCorrectionFor(cols, request.getExtendedData(), true, 
				baseRect.preciseX, baseRect.preciseRight());
	if ((snapOrientation & NORTH_SOUTH) != 0)
		ycorrect = getCorrectionFor(rows, request.getExtendedData(), false, 
				baseRect.preciseY, baseRect.preciseBottom());

	if (xcorrect == THRESHOLD)
		xcorrect = 0.0;
	else
		snapOrientation &= ~EAST_WEST;
	
	if (ycorrect == THRESHOLD)
		ycorrect = 0.0;
	else
		snapOrientation &= ~NORTH_SOUTH;
	
	result.preciseX += xcorrect;
	result.preciseY += ycorrect;
	makeAbsolute(container.getContentPane(), baseRect);
	result.updateInts();
	
	return snapOrientation;
}

protected int snapResizeRect(Request request, PrecisionRectangle baseRect,
		PrecisionRectangle result, int snapOrientation) {
	if (rows == null || cols == null) {
		List exclusionSet = Collections.EMPTY_LIST;
		if (request instanceof GroupRequest)
			exclusionSet = ((GroupRequest)request).getEditParts();
		populateRowsAndCols(generateSnapPartsList(exclusionSet));
	}

	makeRelative(container.getContentPane(), baseRect);
	
	if (request instanceof ChangeBoundsRequest && 
			((ChangeBoundsRequest)request).isCenteredResize()) {
		snapOrientation = performCenteredResize(request, baseRect, result, 
				snapOrientation);
	} else {
		if ((snapOrientation & EAST) != 0) {
			double rightCorrection = getCorrectionFor(cols, request.getExtendedData(), 
					true, baseRect.preciseRight(), 1);
			if (rightCorrection != THRESHOLD) {
				snapOrientation &= ~EAST;
				result.preciseWidth += rightCorrection;
			}
		}
		if ((snapOrientation & WEST) != 0) {
			double leftCorrection = getCorrectionFor(cols, request.getExtendedData(), 
					true, baseRect.preciseX, -1);
			if (leftCorrection != THRESHOLD) {
				snapOrientation &= ~WEST;
				result.preciseWidth -= leftCorrection;
				result.preciseX += leftCorrection;
			}
		}
		if ((snapOrientation & SOUTH) != 0) {
			double bottom = getCorrectionFor(rows, request.getExtendedData(), false,
					baseRect.preciseBottom(), 1);
			if (bottom != THRESHOLD) {
				snapOrientation &= ~SOUTH;
				result.preciseHeight += bottom;
			}
		} 
		if ((snapOrientation & NORTH) != 0) {
			double topCorrection = getCorrectionFor(rows, request.getExtendedData(), 
					false, baseRect.preciseY, -1);
			if (topCorrection != THRESHOLD) {
				snapOrientation &= ~NORTH;
				result.preciseHeight -= topCorrection;
				result.preciseY += topCorrection;
			}
		}
	}
	
	makeAbsolute(container.getContentPane(), baseRect);
	result.updateInts();
	return snapOrientation;
}

}