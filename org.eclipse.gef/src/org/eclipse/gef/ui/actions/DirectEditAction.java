package org.eclipse.gef.ui.actions;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gef.*;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.internal.GEFMessages;

public class DirectEditAction
	extends SelectionAction
{

private Request directEditRequest = new Request(RequestConstants.REQ_DIRECT_EDIT);
public static final String ID = "$direct edit"; //$NON-NLS-1$

public DirectEditAction(IEditorPart editor){
	super(editor);
}

public DirectEditAction(IEditorPart editor, int style){
	super(editor, style);
}

/**
 * returns <code>true</code> if there is exactly 1 EditPart selected that understand DirectEdit.
 */
protected boolean calculateEnabled() {
	if (getSelectedObjects().size() == 1
		&& (getSelectedObjects().get(0) instanceof EditPart))
	{
		EditPart part = (EditPart)getSelectedObjects().get(0);
		return part.understandsRequest(getDirectEditRequest());
	}
	return false;
}

/**
 * returns the <code>Request</code> objects that is used to calculate enabled state and
 * 
 */
protected Request getDirectEditRequest(){
	return directEditRequest;
}

public void run() {
	try {
		EditPart part = (EditPart)getSelectedObjects().get(0);
		part.performRequest(getDirectEditRequest());
	} catch (ClassCastException e) {
		Display.getCurrent().beep();
	} catch (IndexOutOfBoundsException e){
		Display.getCurrent().beep();
	}
}

/**
 * 
 */
public void setDirectEditRequest(Request req){
	directEditRequest = req;
}

protected void init() {
	super.init();
	setText(GEFMessages.RenameAction_ActionLabelText);
	setToolTipText(GEFMessages.RenameAction_ActionToolTipText);
	setId(ID);
}


}
