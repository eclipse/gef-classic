package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * An EditPolicy for use with <code>Figures</code> in {@link XYLayout}. The constraint for
 * XYLayout is a {@link org.eclipse.draw2d.geometry.Rectangle}.
 * 
 * Created on :Nov 12, 2002
 * @author hudsonr
 * @since 2.0 */
public abstract class XYLayoutEditPolicy
	extends ConstrainedLayoutEditPolicy
{

private static final Dimension DEFAULT_SIZE = new Dimension(-1, -1);

/**
 * Returns a new Rectangle equivalent to the passed Rectangle.
 * @param r the input Rectangle
 * @return a copy of the input Rectangle
 */
public Object getConstraintFor(Rectangle r) {
	return new Rectangle(r);
}

/**
 * Returns a Rectangle at the given Point with width and height of -1.
 * <code>XYLayout</code> uses width or height equal to '-1' to mean use the figure's
 * preferred size.
 * @param p the input Point
 * @return a Rectangle
 */
public Object getConstraintFor(Point p) {
	return new Rectangle(p, DEFAULT_SIZE);
}

/**
 * Overridden to prevent sizes from becoming too small, and to prevent preferred sizes
 * from getting lost. If the Request is a MOVE, the existing width and height are
 * preserved. During RESIZE, the new width and height have a lower bound determined by
 * {@link #getMinimumSizeFor(GraphicalEditPart)}.
 */
protected Object getConstraintFor(ChangeBoundsRequest request, GraphicalEditPart child) {
	Rectangle rect = child.getFigure().getBounds().getCopy();
	rect = request.getTransformedRectangle(rect);

	rect.translate(getLayoutOrigin().getNegated());
	if (RequestConstants.REQ_MOVE_CHILDREN.equals(request.getType())) {
		Rectangle cons = (Rectangle)getCurrentConstraintFor(child);
		rect.setSize(cons.width, cons.height);
	}
	if (RequestConstants.REQ_RESIZE_CHILDREN.equals(request.getType())) {
		Dimension minSize = getMinimumSizeFor(child);
		if (rect.width < minSize.width)
			rect.width = minSize.width;
		if (rect.height < minSize.height)
			rect.height = minSize.height;
	}
	return getConstraintFor(rect);
}

/**
 * Retrieves the child's current constraint from the <code>LayoutManager</code>.
 * @param child the child * @return the current constraint */
protected Rectangle getCurrentConstraintFor(GraphicalEditPart child) {
	IFigure fig = child.getFigure();
	return (Rectangle)fig.getParent().getLayoutManager().getConstraint(fig);
}

/**
 * Returns {@link XYLayout#getOrigin(IFigure)}.
 * @see ConstrainedLayoutEditPolicy#getLayoutOrigin() */
protected Point getLayoutOrigin() {
	IFigure container = getLayoutContainer();
	XYLayout layout = (XYLayout)container.getLayoutManager();
	return layout.getOrigin(container);
}

/**
 * Determines the <em>minimum</em> size that the specified child can be resized to. Called
 * from {@link #getConstraintFor(ChangeBoundsRequest, GraphicalEditPart)}. By default,
 * a small <code>Dimension</code> is returned.
 * @param child the child * @return the minumum size */
protected Dimension getMinimumSizeFor(GraphicalEditPart child) {
	return new Dimension(8, 8);
}

/**
 * Places the feedback rectangle where the User indicated.
 * @see LayoutEditPolicy#showSizeOnDropFeedback(CreateRequest) */
protected void showSizeOnDropFeedback(CreateRequest request) {	
	Point p = new Point(request.getLocation());
	IFigure feedback = getSizeOnDropFeedback();
	feedback.translateToRelative(p);
	feedback.setBounds(new Rectangle(p, request.getSize()));
}

}
