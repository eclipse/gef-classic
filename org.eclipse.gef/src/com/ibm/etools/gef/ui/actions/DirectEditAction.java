package com.ibm.etools.gef.ui.actions;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.EditPart;
import com.ibm.etools.gef.Request;

public class DirectEditAction
	extends SelectionAction
{

private Request directEditRequest = new Request(RequestConstants.REQ_DIRECT_EDIT);

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

}
