package org.eclipse.gef.dnd;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.*;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;

public class LocalTransferDropListener extends AbstractTransferDropTargetListener {

public LocalTransferDropListener(EditPartViewer viewer) {
	super(viewer, LocalTransfer.getInstance());
}

/**
 * @deprecated */
public LocalTransferDropListener(EditPartViewer viewer, Transfer xfer) {
	super(viewer, xfer);
}

protected Request createTargetRequest() {
	getCurrentEvent().feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
	ChangeBoundsRequest request = new ChangeBoundsRequest(RequestConstants.REQ_MOVE);
	request.setEditParts((List)LocalTransfer.getInstance().getObject());
	return request;
}

protected Command getCommand() {
	CompoundCommand command = new CompoundCommand();

	Iterator iter = ((List)LocalTransfer.getInstance().getObject()).iterator();

	Request  request = getTargetRequest();
	request.setType(isMove() ? RequestConstants.REQ_MOVE : RequestConstants.REQ_ORPHAN);

	while (iter.hasNext()) {
		EditPart editPart = (EditPart)iter.next();
		command.add(editPart.getCommand(request));
	}

	//If reparenting, add all editparts to target editpart.
	if (!isMove()) {
		request.setType(RequestConstants.REQ_ADD);
		if (getTargetEditPart() == null)
			command.add(UnexecutableCommand.INSTANCE);
		else
			command.add(getTargetEditPart().getCommand(getTargetRequest()));
	}
	return command;
}

protected String getCommandName() {
	if (isMove())
		return RequestConstants.REQ_MOVE;
	return RequestConstants.REQ_ADD;
}

protected void handleDragOver() {
	if (LocalTransfer.getInstance().getViewer() != getViewer()) {
		getCurrentEvent().detail = DND.DROP_NONE;
		return;
	}
	super.handleDragOver();
}

protected EditPart getSourceEditPart() {
	List selection = (List)LocalTransfer.getInstance().getObject();
	if (selection == null || selection.isEmpty() || !(selection.get(0) instanceof EditPart))
		return null;
	return (EditPart)selection.get(0);
}

protected boolean isMove() {
	return getSourceEditPart().getParent() == getTargetEditPart();
}

protected void updateTargetRequest() {
	ChangeBoundsRequest request = (ChangeBoundsRequest)getTargetRequest();
	request.setLocation(getDropLocation());
	request.setType(getCommandName());
}

}
