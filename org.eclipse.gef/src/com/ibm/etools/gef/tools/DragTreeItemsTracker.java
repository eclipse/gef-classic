package com.ibm.etools.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import com.ibm.etools.common.command.Command;
import com.ibm.etools.common.command.UnexecutableCommand;
import com.ibm.etools.draw2d.geometry.Point;
import com.ibm.etools.draw2d.geometry.Dimension;
import com.ibm.etools.gef.*;
import com.ibm.etools.gef.requests.*;
import com.ibm.etools.gef.commands.CompoundCommand;

public class DragTreeItemsTracker
	extends SelectEditPartTracker
{

private Request sourceCommandRequest;

public DragTreeItemsTracker(EditPart sourceEditPart){
	super(sourceEditPart);
	setDefaultCursor(SharedCursors.CURSOR_TREE_MOVE);
	setDisabledCursor(SharedCursors.NO);
}

protected Cursor calculateCursor() {
	if (isInState(STATE_INITIAL | STATE_DRAG))
		return null;
	return super.calculateCursor();
}

protected Request createTargetRequest(){
	ChangeBoundsRequest request = new ChangeBoundsRequest(REQ_MOVE);
	request.setEditParts(getCurrentViewer().getSelectedEditParts());
	return request;
}

protected Command getCommand(){
	CompoundCommand command = new CompoundCommand();
	command.setDebugLabel("Drag Tree Tracker");//$NON-NLS-1$

	Iterator iter = getOperationSet().iterator();

	Request  request = getTargetRequest();
	request.setType(isMove() ? REQ_MOVE : REQ_ORPHAN);

	while (iter.hasNext()){
		EditPart editPart = (EditPart)iter.next();
		command.add(editPart.getCommand(request));
	}

	//If reparenting, add all editparts to target editpart.
	if (!isMove()){
		request.setType(REQ_ADD);
		if(getTargetEditPart() == null)
			command.add(UnexecutableCommand.INSTANCE);
		else
			command.add(getTargetEditPart().getCommand(getTargetRequest()));
	}
	return command;
}

protected String getCommandName(){
	if (isMove())
		return REQ_MOVE;
	return REQ_ADD;
}

protected String getDebugName(){
	return "Tree Tracker: " + getCommandName();//$NON-NLS-1$
}

protected Collection getExclusionSet(){
	List operationSet = getOperationSet();
	List result = new ArrayList(operationSet);
	result.addAll(includeChildren(operationSet));
	return result;
}

protected boolean handleButtonUp(int button){
	super.handleButtonUp(button);
	executeCurrentCommand();
	return true;
}

protected boolean handleDragInProgress(){
	updateTargetRequest();
	updateTargetUnderMouse();
	showTargetFeedback();
	setCurrentCommand(getCommand());
	return true;
}

protected List includeChildren(List list){
	List result = new ArrayList();
	for(int i = 0; i < list.size(); i++){
		List children = ((EditPart)list.get(i)).getChildren();
		result.addAll(children);
		result.addAll(includeChildren(children));
	}
	return result;
}

protected boolean isMove(){
	return getSourceEditPart().getParent() == getTargetEditPart();
}

protected void performSelection(){
}

protected void updateTargetRequest(){
	ChangeBoundsRequest request = (ChangeBoundsRequest)getTargetRequest();
	request.setLocation(getLocation());
	request.setType(getCommandName());
}

}


