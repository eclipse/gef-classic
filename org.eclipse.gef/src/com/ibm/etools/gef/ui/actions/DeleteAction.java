package com.ibm.etools.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.*;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.internal.*;
import org.eclipse.ui.internal.WorkbenchImages;

import com.ibm.etools.common.command.Command;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.commands.CompoundCommand;
import com.ibm.etools.gef.internal.GEFMessages;
import com.ibm.etools.gef.internal.Internal;
import com.ibm.etools.gef.internal.SharedImages;
import com.ibm.etools.gef.requests.*;

/**
 * An action to delete selected objects.
 */
final public class DeleteAction
	extends SelectionAction
{

public static final String ID = org.eclipse.ui.IWorkbenchActionConstants.DELETE;

/**
 * Creates a <code>DeleteAction</code> with a default label.
 *
 * @param editor  The editor this action will be associated with.
 * @param style  The style bitmask.
 */
public DeleteAction(IEditorPart editor, int style) {
	super(editor, style);
}

/**
 * Creates a <code>DeleteAction</code> with a default label.
 *
 * @param editor The editor this action will be associated with.
 */
public DeleteAction(IEditorPart editor) {
	super(editor);
}

/**
 * Creates a <code>DeleteAction</code> with the given label.
 *
 * @param editor  The editor this action will be associated with.
 * @param style  The style bitmask.
 * @param label   The label to be displayed for this action.
 */
public DeleteAction(IEditorPart editor, int style, String label) {
	super(editor, style);
	setText(label);
	setToolTipText(label);
}

/**
 * Initializes this action's text and images.
 */
protected void init(){
	super.init();
	setText(GEFMessages.DeleteAction_ActionLabelText);
	setToolTipText(GEFMessages.DeleteAction_ActionToolTipText);
	setId(ID);
	setHoverImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			IWorkbenchGraphicConstants.IMG_CTOOL_DELETE_EDIT_HOVER));
	setImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			IWorkbenchGraphicConstants.IMG_CTOOL_DELETE_EDIT));
	setDisabledImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			IWorkbenchGraphicConstants.IMG_CTOOL_DELETE_EDIT_DISABLED));

	setEnabled(false);
}

/**
 * Creates a <code>DeleteAction</code> with the given label.
 *
 * @param editor The editor this action will be associated with.
 * @param label  The label to be displayed for this action.
 */
public DeleteAction(IEditorPart editor, String label) {
	super(editor);
	setText(label);
}

/**
 * Create a command to remove the selected objects.
 *
 * @param objects The objects to be deleted.
 *
 * @return The command to remove the selected objects.
 */
public static Command createDeleteCommand(List objects) {
	if (objects.isEmpty())
		return null;
	if (!(objects.get(0) instanceof EditPart))
		return null;

	DeleteRequest deleteReq =
		new DeleteRequest(RequestConstants.REQ_DELETE);

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


