package org.eclipse.gef;

import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.GroupRequest;

/**
 * @author Randy Hudson
 * @author Pratik Shah
 */
public class SnapToGeometry 
	extends SnapToHelper 
{
	
public static final String PROPERTY_SNAP_ENABLED = "SnapToGeometry.isEnabled"; //$NON-NLS-1$	
public static final String KEY_NORTH_ANCHOR = "SnapToGeometry.NorthAnchor"; //$NON-NLS-1$
public static final String KEY_SOUTH_ANCHOR = "SnapToGeometry.SouthAnchor"; //$NON-NLS-1$
public static final String KEY_WEST_ANCHOR = "SnapToGeometry.WestAnchor"; //$NON-NLS-1$
public static final String KEY_EAST_ANCHOR = "SnapToGeometry.EastAnchor"; //$NON-NLS-1$

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

public int snapRectangle(Request request, int snapOrientation,
		PrecisionRectangle baseRect, PrecisionRectangle result) {
	
	baseRect = baseRect.getPreciseCopy();
	makeRelative(container.getContentPane(), baseRect);
	PrecisionRectangle correction = new PrecisionRectangle();
	makeRelative(container.getContentPane(), correction);

	//Recaculate snapping locations if needed
	boolean isClone = request.getType().equals(RequestConstants.REQ_CLONE);
	if (rows == null || cols == null || isClone != cachedCloneBool) {
		cachedCloneBool = isClone;
		List exclusionSet = Collections.EMPTY_LIST;
		if (!isClone && request instanceof GroupRequest)
			exclusionSet = ((GroupRequest)request).getEditParts();
		populateRowsAndCols(generateSnapPartsList(exclusionSet));
	}
	
	if ((snapOrientation & HORIZONTAL) != 0) {
		double xcorrect = THRESHOLD;
		xcorrect = getCorrectionFor(cols, request.getExtendedData(), true, 
				baseRect.preciseX, baseRect.preciseRight());
		if (xcorrect != THRESHOLD) {
			snapOrientation &= ~HORIZONTAL;
			correction.preciseX += xcorrect;
		}
	}
	
	if ((snapOrientation & VERTICAL) != 0) {
		double ycorrect = THRESHOLD;
		ycorrect = getCorrectionFor(rows, request.getExtendedData(), false, 
				baseRect.preciseY, baseRect.preciseBottom());
		if (ycorrect != THRESHOLD) {
			snapOrientation &= ~VERTICAL;
			correction.preciseY += ycorrect;
		}
	}
	
	if ((snapOrientation & EAST) != 0) {
		double rightCorrection = getCorrectionFor(cols, request.getExtendedData(), 
				true, baseRect.preciseRight(), 1);
		if (rightCorrection != THRESHOLD) {
			snapOrientation &= ~EAST;
			correction.preciseWidth += rightCorrection;
		}
	}
	
	if ((snapOrientation & WEST) != 0) {
		double leftCorrection = getCorrectionFor(cols, request.getExtendedData(), 
				true, baseRect.preciseX, -1);
		if (leftCorrection != THRESHOLD) {
			snapOrientation &= ~WEST;
			correction.preciseWidth -= leftCorrection;
			correction.preciseX += leftCorrection;
		}
	}
	
	if ((snapOrientation & SOUTH) != 0) {
		double bottom = getCorrectionFor(rows, request.getExtendedData(), false,
				baseRect.preciseBottom(), 1);
		if (bottom != THRESHOLD) {
			snapOrientation &= ~SOUTH;
			correction.preciseHeight += bottom;
		}
	}
	
	if ((snapOrientation & NORTH) != 0) {
		double topCorrection = getCorrectionFor(rows, request.getExtendedData(), 
				false, baseRect.preciseY, -1);
		if (topCorrection != THRESHOLD) {
			snapOrientation &= ~NORTH;
			correction.preciseHeight -= topCorrection;
			correction.preciseY += topCorrection;
		}
	}
	
	correction.updateInts();
	makeAbsolute(container.getContentPane(), correction);
	result.preciseX += correction.preciseX;
	result.preciseY += correction.preciseY;
	result.preciseWidth += correction.preciseWidth;
	result.preciseHeight += correction.preciseHeight;
	result.updateInts();
	
	return snapOrientation;
}

}