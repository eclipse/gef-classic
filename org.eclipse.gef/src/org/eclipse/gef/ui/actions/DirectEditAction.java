/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.actions;

import org.eclipse.swt.widgets.Display;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.internal.GEFMessages;

public class DirectEditAction
	extends SelectionAction
{

/** @deprecated */
public static final String ID = GEFActionConstants.DIRECT_EDIT;

private Request directEditRequest = new Request(RequestConstants.REQ_DIRECT_EDIT);

/**
 * @deprecated use DirectEditAction(IWorkbenchPart part)
 * @param editor
 */
public DirectEditAction(IEditorPart editor){
	this((IWorkbenchPart)editor);
}

public DirectEditAction(IWorkbenchPart part) {
	super(part);
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
	setText(GEFMessages.RenameAction_Label);
	setToolTipText(GEFMessages.RenameAction_Tooltip);
	setId(GEFActionConstants.DIRECT_EDIT);
}


}
