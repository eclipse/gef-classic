package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.*;
import org.eclipse.gef.requests.DropRequest;

public abstract class FlowLayoutEditPolicy
	extends OrderedLayoutEditPolicy
{
protected Polyline insertionLine;

protected void eraseDragTargetFeedback(Request request) {
	super.eraseDragTargetFeedback(request);
	if (insertionLine != null) {
		removeFeedback(insertionLine);
		insertionLine = null;
	}
}

protected Rectangle getAbsoluteBounds(GraphicalEditPart ep) {
	Rectangle bounds = ep.getFigure().getBounds().getCopy();
	ep.getFigure().translateToAbsolute(bounds);
	return bounds;
}

protected EditPart getInsertionReference(Request request) {
	List children = getHost().getChildren();

	if (request.getType().equals(RequestConstants.REQ_CREATE)) {
		int i = getFeedbackIndexFor(request);
		if (i == -1)
			return null;
		return (EditPart)children.get(i);
	}

	int index = getFeedbackIndexFor(request);
	if (index != -1) {
		List selection = getHost().getRoot().getViewer().getSelectedEditParts();
		do {
			EditPart editpart = (EditPart) children.get(index);
			if (!selection.contains(editpart))
				return editpart;
		} while (++index  < children.size());
	}
	return null; //Not found, add at the end.
}

protected int getFeedbackIndexFor(Request request) {
	FlowLayout layoutManager = getLayoutManager();
	List children = getHost().getChildren();
	if (children.isEmpty())
		return -1;
		
	Transposer transposer = new Transposer();
	transposer.setEnabled (!layoutManager.isHorizontal());
	
	Point p = transposer.t(getLocationFromRequest(request));

	// Current row bottom, initialize to above the top.
	int rowBottom = Integer.MIN_VALUE;
	int candidate = -1; // The current candidate VO to be before.
	for (int i = 0; i < children.size(); i++) {
		EditPart child = (EditPart) children.get(i);
		Rectangle rect = transposer.t(getAbsoluteBounds(((GraphicalEditPart)child)));
		if (rect.y > rowBottom) {
			// We are in a new row, so if we don't have a candidate but yet are within the
			// previous row, then the current entry becomes the candidate. This is because
			// we know we must be to the right of center of the last control in the previous row,
			// so this control (which is at the start of a new row) is the candidate.
			if (p.y <= rowBottom) {
				if (candidate == -1)
					candidate = i;
				break;
			} else
				candidate = -1; // If we had a candidate, we reset it because it wasn't within the row.
		}
		int rectBottom = rect.y + rect.height;
		if (rectBottom > rowBottom)
			rowBottom = rectBottom;
		if (candidate == -1) {
			// See if we have a possible candidate. It is a candidate if the cursor is left of the center of this candidate.
			if (p.x <= rect.x + (rect.width / 2))
				candidate = i;
		}
		if (candidate != -1) {
			// We either just found one, or we had one pending, check if now within the current row
			if (p.y <= rowBottom)
				break; // The cursor is now known to be within the current row and left of the candidate, so the candidate is it.
		}
	}
	return candidate;
}
/**
 * Answer the layout manager to use to do the appropriate transpose functions.
 */
protected FlowLayout getLayoutManager() {
	return (FlowLayout) getHostFigure().getLayoutManager();
}

protected Polyline getLineFeedback() {
	if (insertionLine == null) {
		insertionLine = new Polyline();
		insertionLine.setLineWidth(2);
		insertionLine.addPoint(new Point(0, 0));
		insertionLine.addPoint(new Point(10, 10));
		addFeedback(insertionLine);
	}
	return insertionLine;
}

protected Point getLocationFromRequest(Request request) {
	// add, move, create and resize
	if (request instanceof DropRequest)
		return ((DropRequest)request).getLocation();
	return null;
}

protected void showDragTargetFeedback(Request request) {
	if (getHost().getChildren().size() == 0)
		return;
	FlowLayout layoutManager = getLayoutManager();
	Polyline fb = getLineFeedback();
	Transposer transposer = new Transposer();
	transposer.setEnabled(!layoutManager.isHorizontal());
	
	boolean before = true;
	int epIndex = getFeedbackIndexFor(request);
	Rectangle r = null;
	if (epIndex == -1) {
		before = false;
		epIndex = getHost().getChildren().size() - 1;
		EditPart editPart = (EditPart) getHost().getChildren().get(epIndex);
		r = transposer.t(getAbsoluteBounds((GraphicalEditPart)editPart));
	} else {
		EditPart editPart = (EditPart) getHost().getChildren().get(epIndex);
		r = transposer.t(getAbsoluteBounds((GraphicalEditPart)editPart));
		Point p = transposer.t(getLocationFromRequest(request));
		if (p.x <= r.x + (r.width / 2))
			before = true;
		else {
			// We are not to the left of this control, so the emphasis line
			// needs to be to the right of the previous control, which must
			// be on the previous row.
			before = false;
			epIndex--;
			editPart = (EditPart) getHost().getChildren().get(epIndex);
			r = transposer.t(getAbsoluteBounds((GraphicalEditPart)editPart));
		}
	}
	int x = Integer.MIN_VALUE;
	if (before) {
		// Want the line to be halfway between the end of the previous and the beginning of this one.
		// If at the begining of a line, then start halfway between the left edge of the parent and
		// the beginning of the box, but no more than 5 pixels (it would be too far and be confusing
		// otherwise).
		if (epIndex > 0) {
			// Need to determine if a line break.
			Rectangle voPrev = transposer.t(getAbsoluteBounds((GraphicalEditPart)getHost().getChildren().get(epIndex - 1)));
			int prevRight = voPrev.x + voPrev.width;
			if (prevRight < r.x) {
				// Not a line break
				x = prevRight + (r.x - prevRight) / 2;
			} else if (prevRight == r.x) {
				x = prevRight + 1;	
			}
		}
		if (x == Integer.MIN_VALUE) {
			// It is a line break.
			Rectangle parentBox = transposer.t(getAbsoluteBounds((GraphicalEditPart)getHost()));
			x = r.x - 5;
			if (x < parentBox.x)
				x = parentBox.x + (r.x - parentBox.x) / 2;
		}
	} else {
		// We only have before==false if we are at the end of a line, so go halfway between the
		// right edge and the right edge of the parent, but no more than 4 pixels.
		Rectangle parentBox = transposer.t(getAbsoluteBounds((GraphicalEditPart)getHost()));
		int rRight = r.x + r.width;
		int pRight = parentBox.x + parentBox.width;
		x = rRight + 5;
		if (x > pRight)
			x = rRight + (pRight - rRight) / 2;
	}
	Point p1 = new Point(x, r.y - 6);
	fb.translateToRelative(p1);
	p1 = transposer.t(p1);
	Point p2 = new Point(x, r.y + r.height + 6);
	fb.translateToRelative(p2);
	p2 = transposer.t(p2);
	fb.setPoint(p1, 0);
	fb.setPoint(p2, 1);
}
}


