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

public abstract class XYLayoutEditPolicy
	extends ConstrainedLayoutEditPolicy
{

protected static final Dimension DEFAULT_SIZE = new Dimension(-1, -1);

public Object getConstraintFor(Rectangle r) {
	return new Rectangle(r);
}

public Object getConstraintFor(Point p) {
	return new Rectangle(p,DEFAULT_SIZE);
}

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

protected Rectangle getCurrentConstraintFor(GraphicalEditPart child){
	IFigure fig = child.getFigure();
	return (Rectangle)fig.getParent().getLayoutManager().getConstraint(fig);
}

public Object getDefaultConstraint() {
	return new Rectangle(new Point(10,10), DEFAULT_SIZE);
}

protected Point getLayoutOrigin(){
	IFigure container = getLayoutContainer();
	XYLayout layout = (XYLayout)container.getLayoutManager();
	return layout.getOrigin(container);
}

protected Dimension getMinimumSizeFor(GraphicalEditPart part) {
	return new Dimension(8, 8);
}

protected void showSizeOnDropFeedback(CreateRequest request) {	
	Point p = new Point(request.getLocation());
	IFigure feedback = getSizeOnDropFeedback();
	feedback.translateToRelative(p);
	feedback.setBounds(new Rectangle(p, request.getSize()));
}

}
