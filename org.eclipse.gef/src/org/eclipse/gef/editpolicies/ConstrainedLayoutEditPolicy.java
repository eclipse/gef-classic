package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;


import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.*;

abstract public class ConstrainedLayoutEditPolicy
	extends LayoutEditPolicy
{

abstract protected Command createAddCommand(
	EditPart child,
	Object constraint);

abstract protected Command createChangeConstraintCommand(
	EditPart child,
	Object constraint);

protected Command getAddCommand(Request generic) {
	ChangeBoundsRequest request = (ChangeBoundsRequest)generic;
	List editParts = request.getEditParts();
	CompoundCommand command = new CompoundCommand();
	command.setDebugLabel("Add in ConstrainedLayoutEditPolicy");//$NON-NLS-1$
	GraphicalEditPart childPart;
	Rectangle r;
	Object constraint;

	for (int i = 0; i < editParts.size(); i++) {
		childPart = (GraphicalEditPart)editParts.get(i);
		r = childPart.getFigure().getBounds().getCopy();
		//convert r to absolute from childpart figure
		childPart.getFigure().translateToAbsolute(r);
		r = request.getTransformedRectangle(r);
		//convert this figure to relative 
		r.translate(getLayoutOrigin().getNegated());
		getLayoutContainer().translateToRelative(r);
		getLayoutContainer().translateFromParent(r);
		constraint = getConstraintFor(r);
		command.add(createAddCommand(childPart,
			translateToModelConstraint(constraint)));
	}
	return command.unwrap();
}

protected Command getAlignChildrenCommand(AlignmentRequest request) {
	return getResizeChildrenCommand(request);
}

public Command getCommand(Request request) {
	if (REQ_RESIZE_CHILDREN.equals(request.getType()))
		return getResizeChildrenCommand((ChangeBoundsRequest)request);
	if (REQ_ALIGN_CHILDREN.equals(request.getType()))
		return getAlignChildrenCommand((AlignmentRequest)request);

	return super.getCommand(request);
}

/**
 * Returns a draw2d constraint object for the given request.
 * The returned object can be translated to the model using
 * translateToModelConstraint(Object)
 * @see #translateToModelConstraint(Object)
 */
protected Object getConstraintFor (ChangeBoundsRequest request, GraphicalEditPart child) {
	Rectangle rect = child.getFigure().getBounds();
	rect = request.getTransformedRectangle(rect);
	rect.translate(getLayoutOrigin().getNegated());
	return getConstraintFor(rect);
}

/**
 * The point here is relative to the client area of the figure.
 * It is not absolute.
 */
abstract protected Object getConstraintFor (Point point);

/**
 * The rectangle here is relative to the client area of the figure.
 * It is not absolute.
 */
abstract protected Object getConstraintFor (Rectangle rect);

protected Object getConstraintFor(CreateRequest request) {
	IFigure figure = getLayoutContainer();

	Point where = request.getLocation().getCopy();
	Dimension size = request.getSize();

	figure.translateToRelative(where);
	figure.translateFromParent(where);
	where.translate(getLayoutOrigin().getNegated());

	if (size == null || size.isEmpty())
		return getConstraintFor(where);
	else
		return getConstraintFor(new Rectangle(where, size));
}

abstract protected Command getCreateCommand(CreateRequest request);

/**
 * Converts a constraint from the way it is stored in the model to the
 * form used by the LayoutManager
 * @deprecated This is never used.
 */
protected Object translateToFigureConstraint(Object modelConstraint) {
	return modelConstraint;
}

/**
 * Converts a constraint from the format used by LayoutManagers,
 * to the form stored in the model.
 */
protected Object translateToModelConstraint(Object figureConstraint) {
	return figureConstraint;
}

protected Command getResizeChildrenCommand(ChangeBoundsRequest request) {
	CompoundCommand resize = new CompoundCommand();
	Command c;
	GraphicalEditPart child;
	List children = request.getEditParts();

	for (int i = 0; i < children.size(); i++) {
		child = (GraphicalEditPart)children.get(i);
		c = createChangeConstraintCommand(child,
			translateToModelConstraint(
				getConstraintFor(request, child)));
		resize.add(c);
	}
	return resize.unwrap();
}

protected Command getMoveChildrenCommand(Request request) {
	//By default, move and resize are treated the same for constrained layouts.
	return getResizeChildrenCommand((ChangeBoundsRequest)request);
}

protected Point getLayoutOrigin() {
	return getLayoutContainer().getClientArea().getLocation();
}

}