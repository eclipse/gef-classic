package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

abstract public class OrderedLayoutEditPolicy
	extends LayoutEditPolicy
{

protected abstract EditPart getInsertionReference(Request request);

protected abstract Command createAddCommand(EditPart child, EditPart after);
protected abstract Command createMoveChildCommand(EditPart child, EditPart after);

protected Command getMoveChildrenCommand(Request request){
	CompoundCommand command = new CompoundCommand();
	List editParts = ((ChangeBoundsRequest)request).getEditParts();
	
	EditPart insertionReference = getInsertionReference(request);
	for(int i=0;i<editParts.size(); i++){
		EditPart child = (EditPart)editParts.get(i);
		command.add(createMoveChildCommand(child, insertionReference));
	}
	return command.unwrap();
}

protected Command getAddCommand(Request req){
	ChangeBoundsRequest request = (ChangeBoundsRequest)req;
	List editParts = request.getEditParts();
	Point where = request.getLocation();
	IFigure f = ((GraphicalEditPart)getHost()).getFigure();
	where = where.getTranslated(f.getBounds().getTopLeft());
	CompoundCommand command = new CompoundCommand();
	for(int i=0;i<editParts.size();i++){
		EditPart child = (EditPart) editParts.get(i);
		command.add(
			createAddCommand(child,
				getInsertionReference(request)));
	}
	return command.unwrap();
}

abstract protected Command getCreateCommand(CreateRequest request);

}

