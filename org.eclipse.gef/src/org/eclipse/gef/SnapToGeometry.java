package org.eclipse.gef;

import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * @author Randy Hudson
 */
public class SnapToGeometry implements SnapToStrategy {
	
public static final String HORIZONTAL_ANCHOR = "SnapToGeometry - $H Anchor"; //$NON-NLS-1$
public static final String VERTICAL_ANCHOR = "SnapToGeometry - $V Anchor"; //$NON-NLS-1$

private GraphicalEditPart container;
private static final double THRESHOLD = 5.0001;

private static class Entry {
	int side;
	int offset;
	
	Entry(int side, int offset) {
		this.side = side;
		this.offset = offset;
	}
}

private Entry rows[];
private Entry cols[];

public SnapToGeometry(GraphicalEditPart container) {
	this.container = container;
	List children = container.getChildren();
	rows = new Entry[children.size() * 3];
	cols = new Entry[children.size() * 3];
	for (int i = 0; i < children.size(); i++) {
		GraphicalEditPart child = (GraphicalEditPart)children.get(i);
		IFigure figure = child.getFigure();
		Rectangle bounds = figure.getBounds();
		if (figure instanceof HandleBounds)
			bounds = ((HandleBounds)figure).getHandleBounds();
		cols[i * 3] = new Entry(-1, bounds.x);
		rows[i * 3] = new Entry(-1, bounds.y);
		cols[i * 3 + 1] = new Entry(0, bounds.x + bounds.width / 2);
		rows[i * 3 + 1] = new Entry(0, bounds.y + bounds.height / 2);
		cols[i * 3 + 2] = new Entry(1, bounds.right());
		rows[i * 3 + 2] = new Entry(1, bounds.bottom());
	}
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
	double result = getCorrectionFor(entries, extendedData, vert, near, -1);
	if (result == THRESHOLD)
		result = getCorrectionFor(entries, extendedData, vert, (near + far) / 2, 0);
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
				extendedData.put(vert ? VERTICAL_ANCHOR : HORIZONTAL_ANCHOR, 
						new Integer(entry.offset));
			}
		} else if (entry.side == 0 && side == 0) {
			magnitude = Math.abs(value - entry.offset);
			if (magnitude < resultMag) {
				resultMag = magnitude;
				result = entry.offset - value;
				extendedData.put(vert ? VERTICAL_ANCHOR : HORIZONTAL_ANCHOR, 
									  new Integer(entry.offset));
			}
		} else if (entry.side == 1 && side != 0) {
			magnitude = Math.abs(value - entry.offset);
			if (magnitude < resultMag) {
				resultMag = magnitude;
				result = entry.offset - value;
				extendedData.put(vert ? VERTICAL_ANCHOR : HORIZONTAL_ANCHOR, 
									  new Integer(entry.offset));
			}
		}
	}
	return result;
}

/**
 * @see SnapToStrategy#snapMoveRequest(ChangeBoundsRequest, PrecisionRectangle)
 */
public boolean snapMoveRequest(ChangeBoundsRequest request,	PrecisionRectangle baseRect) {
	PrecisionPoint move = new PrecisionPoint(request.getMoveDelta());
	IFigure fig = container.getContentPane();
	baseRect.translate(move);
	fig.translateToRelative(baseRect);
	fig.translateToRelative(move);

	double xcorrect = getCorrectionFor(cols, request.getExtendedData(), true, 
			baseRect.preciseX, baseRect.preciseRight());
	double ycorrect = getCorrectionFor(rows, request.getExtendedData(), false, 
			baseRect.preciseY, baseRect.preciseBottom());

	// No snapping feedback is to be shown if multiple editparts are being moved. 
	if (request.getEditParts().size() > 1)
		request.getExtendedData().clear();

	//If neither value is being corrected, return false
	if (xcorrect == THRESHOLD && ycorrect == THRESHOLD)
		return false;

	if (ycorrect == THRESHOLD)
		ycorrect = 0.0;
	else if (xcorrect == THRESHOLD)
		xcorrect = 0.0;
	
	move.preciseX += xcorrect;
	move.preciseY += ycorrect;
	move.updateInts();
	fig.translateToAbsolute(move);
	request.setMoveDelta(move);
	
	Integer value = (Integer)request.getExtendedData().get(HORIZONTAL_ANCHOR);
	if (value != null) {
		Point loc = new Point(0, value.intValue());
		fig.translateToAbsolute(loc);
		request.getExtendedData().put(HORIZONTAL_ANCHOR, new Integer(loc.y));
	}
	value = (Integer)request.getExtendedData().get(VERTICAL_ANCHOR);
	if (value != null) {
		Point loc = new Point(value.intValue(), 0);
		fig.translateToAbsolute(loc);
		request.getExtendedData().put(VERTICAL_ANCHOR, new Integer(loc.x));
	}
		
	
	return true;
}

/**
 * @see SnapToStrategy#snapResizeRequest(ChangeBoundsRequest, PrecisionRectangle)
 */
public boolean snapResizeRequest(ChangeBoundsRequest request, PrecisionRectangle baseRec) {
	int dir = request.getResizeDirection();

	PrecisionDimension resize = new PrecisionDimension(request.getSizeDelta());
	PrecisionPoint move = new PrecisionPoint(request.getMoveDelta());

	IFigure fig = container.getContentPane();
	baseRec.resize(resize);
	baseRec.translate(move);
	
	fig.translateToRelative(baseRec);
	fig.translateToRelative(resize);
	fig.translateToRelative(move);

	boolean change = false;
	if ((dir & PositionConstants.EAST) != 0) {
		double rightCorrection = getCorrectionFor(cols, request.getExtendedData(), true,
				baseRec.preciseRight(), 1);
		if (rightCorrection != THRESHOLD) {
			change = true;
			resize.preciseWidth += rightCorrection;
		}
	} else if ((dir & PositionConstants.WEST) != 0) {
		double leftCorrection = getCorrectionFor(cols, request.getExtendedData(), true,
				baseRec.preciseX, -1);
		if (leftCorrection != THRESHOLD) {
			change = true;
			resize.preciseWidth -= leftCorrection;
			move.preciseX += leftCorrection;
		}
	}
	
	if ((dir & PositionConstants.SOUTH) != 0) {
		double bottom = getCorrectionFor(rows, request.getExtendedData(), false,
				baseRec.preciseBottom(), 1);
		if (bottom != THRESHOLD) {
			change = true;
			resize.preciseHeight += bottom;
		}
	} else if ((dir & PositionConstants.NORTH) != 0) {
		double topCorrection = getCorrectionFor(rows, request.getExtendedData(), false,
				baseRec.preciseY, -1);
		if (topCorrection != THRESHOLD) {
			change = true;
			resize.preciseHeight -= topCorrection;
			move.preciseY += topCorrection;
		}
	}
	if (!change)
		return false;
	
	// No snapping feedback is to be shown if multiple editparts are being resized.
	if (request.getEditParts().size() > 1) {
		request.getExtendedData().remove(HORIZONTAL_ANCHOR);
		request.getExtendedData().remove(VERTICAL_ANCHOR);
	}
	
	Integer value = (Integer)request.getExtendedData().get(HORIZONTAL_ANCHOR);
	if (value != null) {
		Point loc = new Point(0, value.intValue());
		fig.translateToAbsolute(loc);
		request.getExtendedData().put(HORIZONTAL_ANCHOR, new Integer(loc.y));
	}
	value = (Integer)request.getExtendedData().get(VERTICAL_ANCHOR);
	if (value != null) {
		Point loc = new Point(value.intValue(), 0);
		fig.translateToAbsolute(loc);
		request.getExtendedData().put(VERTICAL_ANCHOR, new Integer(loc.x));
	}
	
	fig.translateToAbsolute(resize);
	fig.translateToAbsolute(move);
	resize.updateInts();
	move.updateInts();

	request.setSizeDelta(resize);
	request.setMoveDelta(move);
	return true;
}

}
