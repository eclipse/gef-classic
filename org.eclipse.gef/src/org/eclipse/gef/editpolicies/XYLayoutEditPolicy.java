/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editpolicies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * An EditPolicy for use with <code>Figures</code> in {@link XYLayout}. The constraint for
 * XYLayout is a {@link org.eclipse.draw2d.geometry.Rectangle}.
 * 
 * Created on :Nov 12, 2002
 * @author hudsonr
 * @since 2.0
 */
public abstract class XYLayoutEditPolicy
	extends ConstrainedLayoutEditPolicy
{

private static final Dimension DEFAULT_SIZE = new Dimension(-1, -1);
private XYLayout xyLayout;

/**
 * Overridden to prevent sizes from becoming too small, and to prevent preferred sizes
 * from getting lost. If the Request is a MOVE, the existing width and height are
 * preserved. During RESIZE, the new width and height have a lower bound determined by
 * {@link #getMinimumSizeFor(GraphicalEditPart)}.
 * 
 * @see ConstrainedLayoutEditPolicy#getConstraintFor(ChangeBoundsRequest, GraphicalEditPart)
 */
protected Object getConstraintFor(ChangeBoundsRequest request, GraphicalEditPart child) {
	Rectangle rect = new PrecisionRectangle(child.getFigure().getBounds());
	Rectangle original = rect.getCopy();
	child.getFigure().translateToAbsolute(rect);
	rect = request.getTransformedRectangle(rect);
	child.getFigure().translateToRelative(rect);
	rect.translate(getLayoutOrigin().getNegated());

	if (request.getSizeDelta().width == 0 && request.getSizeDelta().height == 0) {
		Rectangle cons = getCurrentConstraintFor(child);
		if (cons != null) //Bug 86473 allows for unintended use of this method
			rect.setSize(cons.width, cons.height);
	} else { // resize
		Dimension minSize = getMinimumSizeFor(child);
		if (rect.width < minSize.width) {
			rect.width = minSize.width;
			if (rect.x > (original.right() - minSize.width))
				rect.x = original.right() - minSize.width;
		}
		if (rect.height < minSize.height) {
			rect.height = minSize.height;
			if (rect.y > (original.bottom() - minSize.height))
				rect.y = original.bottom() - minSize.height;
		}
	}
	return getConstraintFor(rect);
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
 * Returns a new Rectangle equivalent to the passed Rectangle.
 * @param r the input Rectangle
 * @return a copy of the input Rectangle
 */
public Object getConstraintFor(Rectangle r) {
	return new Rectangle(r);
}

/**
 * Retrieves the child's current constraint from the <code>LayoutManager</code>.
 * @param child the child
 * @return the current constraint
 */
protected Rectangle getCurrentConstraintFor(GraphicalEditPart child) {
	IFigure fig = child.getFigure();
	return (Rectangle)fig.getParent().getLayoutManager().getConstraint(fig);
}

/**
 * Returns {@link XYLayout#getOrigin(IFigure)}.
 * @see ConstrainedLayoutEditPolicy#getLayoutOrigin()
 */
protected Point getLayoutOrigin() {
	return getXYLayout().getOrigin(getLayoutContainer());
}

/**
 * Determines the <em>minimum</em> size that the specified child can be resized to. Called
 * from {@link #getConstraintFor(ChangeBoundsRequest, GraphicalEditPart)}. By default,
 * a small <code>Dimension</code> is returned.
 * @param child the child
 * @return the minumum size
 */
protected Dimension getMinimumSizeFor(GraphicalEditPart child) {
	return new Dimension(8, 8);
}

/**
 * @return the XYLayout layout manager set on the {@link
 * LayoutEditPolicy#getLayoutContainer() container}
 */
protected XYLayout getXYLayout() {
	if (xyLayout == null) {
		IFigure container = getLayoutContainer();
		xyLayout = (XYLayout)container.getLayoutManager();
	}
	return xyLayout;
}

/**
 * @param xyLayout The xyLayout to set.
 */
public void setXyLayout(XYLayout xyLayout) {
	this.xyLayout = xyLayout;
}

/**
 * Places the feedback rectangle where the User indicated.
 * @see LayoutEditPolicy#showSizeOnDropFeedback(CreateRequest)
 */
protected void showSizeOnDropFeedback(CreateRequest request) {	
	Point p = new Point(request.getLocation().getCopy());
	IFigure feedback = getSizeOnDropFeedback(request);
	feedback.translateToRelative(p);
	Dimension size = request.getSize().getCopy();
	feedback.translateToRelative(size);
	feedback.setBounds(new Rectangle(p, size).expand(getCreationFeedbackOffset(request)));
}

}
