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

import java.util.List;

import org.eclipse.ui.*;
import org.eclipse.ui.internal.WorkbenchImages;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.requests.GroupRequest;

/**
 * An action to delete selected objects.
 */
public class DeleteAction
	extends SelectionAction
{

/** @deprecated use GEFActionConstants.DELETE */
public static final String ID = GEFActionConstants.DELETE;

/**
 * @deprecated use DeleteAction(IWorkbenchPart part)
 * @param editor The editor this action will be associated with.
 */
public DeleteAction(IEditorPart editor) {
	this((IWorkbenchPart)editor);
}

/**
 * Constructs a <code>DeleteAction</code> using the specified part.
 * @param part The part for this action
 */
public DeleteAction(IWorkbenchPart part) {
	super(part);
	setLazyEnablementCalculation(false);
}

/**
 * Initializes this action's text and images.
 */
protected void init() {
	super.init();
	setText(GEFMessages.DeleteAction_Label);
	setToolTipText(GEFMessages.DeleteAction_Tooltip);
	setId(GEFActionConstants.DELETE);
	setHoverImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			ISharedImages.IMG_TOOL_DELETE_HOVER));
	setImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			ISharedImages.IMG_TOOL_DELETE));
	setDisabledImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			ISharedImages.IMG_TOOL_DELETE_DISABLED));
	setEnabled(false);
}

/**
 * Creates a <code>DeleteAction</code> with the given label.
 * @deprecated use DeleteAction(IWorkbenchPart)
 * @param editor The editor this action will be associated with.
 * @param label  The label to be displayed for this action.
 */
public DeleteAction(IEditorPart editor, String label) {
	super(editor);
	setText(label);
}

/**
 * Create a command to remove the selected objects.
 * @param objects The objects to be deleted.
 * @return The command to remove the selected objects.
 * @deprecated this method will become an instance method in the next release.
 */
public Command createDeleteCommand(List objects) {
	if (objects.isEmpty())
		return null;
	if (!(objects.get(0) instanceof EditPart))
		return null;

	GroupRequest deleteReq =
		new GroupRequest(RequestConstants.REQ_DELETE);

	CompoundCommand compoundCmd = new CompoundCommand(
		GEFMessages.DeleteAction_ActionDeleteCommandName);
	for (int i = 0; i < objects.size(); i++) {
		EditPart object = (EditPart) objects.get(i);
		Command cmd = object.getCommand(deleteReq);
		if (cmd != null) compoundCmd.add(cmd);
	}

	return compoundCmd;
}

/**
 * Returns <code>true</code> if the selected objects can
 * be deleted.  Returns <code>false</code> if there are
 * no objects selected or the selected objects are not
 * {@link EditPart}s.
 * @return <code>true</code> if the command should be enabled
 */
protected boolean calculateEnabled() {
	Command cmd = createDeleteCommand(getSelectedObjects());
	if (cmd == null)
		return false;
	return cmd.canExecute();
}

/**
 * Performs the delete action on the selected objects.
 */
public void run() {
	execute(createDeleteCommand(getSelectedObjects()));
}

}


