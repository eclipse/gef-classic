package com.ibm.etools.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import com.ibm.etools.gef.tools.*;
import com.ibm.etools.gef.commands.*;
import com.ibm.etools.common.command.Command;
import com.ibm.etools.gef.*;
import com.ibm.etools.gef.handles.*;
import com.ibm.etools.gef.requests.*;
import com.ibm.etools.draw2d.geometry.*;
import com.ibm.etools.draw2d.*;
import org.eclipse.swt.widgets.Listener;

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

