package org.eclipse.gef.dnd;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.*;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class LocalTransferDropListener
	extends AbstractTransferDropTargetListener
{

private TreeItem[] selectedItems;

public LocalTransferDropListener(EditPartViewer viewer) {
	super(viewer, LocalTransfer.getInstance());
}

/**
 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#activate()
 */
public void activate() {
	super.activate();
	selectedItems = ((Tree)getViewer().getControl()).getSelection();
}

protected Request createTargetRequest() {
	getCurrentEvent().feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
	ChangeBoundsRequest request = new ChangeBoundsRequest(RequestConstants.REQ_MOVE);
	request.setEditParts((List)LocalTransfer.getInstance().getObject());
	return request;
}

/**
 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#deactivate()
 */
public void deactivate() {
	restoreSelection();
	selectedItems = null;
	super.deactivate();
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

/**
 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDrop()
 */
protected void handleDrop() {
	selectedItems = null;
	super.handleDrop();
}


protected EditPart getSourceEditPart() {
	List selection = (List)LocalTransfer.getInstance().getObject();
	if (selection == null || selection.isEmpty() || !(selection.get(0) instanceof EditPart))
		return null;
	return (EditPart)selection.get(0);
}

private Tree getTree() {
	return (Tree)getViewer().getControl();
}

protected boolean isMove() {
	return getSourceEditPart().getParent() == getTargetEditPart();
}

/**
 * Method restoreSelection.
 */
private void restoreSelection() {
	if (selectedItems != null)
		getTree().setSelection(selectedItems);
}

/**
 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#setTargetEditPart(EditPart)
 */
protected void setTargetEditPart(EditPart ep) {
	if (getTargetEditPart() != ep){
		if (ep != null && ((TreeEditPart)ep).getWidget() instanceof TreeItem) {
			GEF.debug("bogus restore");
			restoreSelection();
			getTree().setSelection(new TreeItem[]{
				(TreeItem)((TreeEditPart)ep).getWidget()});
			GEF.debug("setting to " + ep);
		} else {
			if (ep == null){
				GEF.debug("restoring to " + selectedItems);
				restoreSelection();
			} else {
				getTree().setSelection(new TreeItem[0]);
			}
		}
	}
	super.setTargetEditPart(ep);
}

protected void updateTargetRequest() {
	ChangeBoundsRequest request = (ChangeBoundsRequest)getTargetRequest();
	request.setLocation(getDropLocation());
	request.setType(getCommandName());
}

}
