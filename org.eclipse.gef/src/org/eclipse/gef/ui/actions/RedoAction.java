package org.eclipse.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.text.MessageFormat;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.internal.WorkbenchImages;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.internal.GEFMessages;

/**
 * An action to redo the last command.
 */
public class RedoAction
	extends StackAction
{

/**
 * Creates a <code>RedoAction</code> and associates it with the 
 * given editor.
 *
 * @param editor The editor this action is associated with.
 */
public RedoAction(IEditorPart editor) {
	super(editor);
}

/**
 * Returns <code>true</code> if the {@link CommandStack} can redo
 * the last command.
 */
protected boolean calculateEnabled(){
	return getCommandStack().canRedo();
}

/**
 * Initializes this actions text and images.
 */
protected void init(){
	super.init();
	setToolTipText(MessageFormat.format(
			GEFMessages.RedoAction_Tooltip,	
			new Object[] {""}).trim());  //$NON-NLS-1$
	setText(MessageFormat.format(
			GEFMessages.RedoAction_Label, 
			new Object[] {""}).trim()  //$NON-NLS-1$
			);
	setId(GEFActionConstants.REDO);
	setHoverImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			ISharedImages.IMG_TOOL_REDO_HOVER));
	setImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			ISharedImages.IMG_TOOL_REDO));
	setDisabledImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			ISharedImages.IMG_TOOL_REDO_DISABLED));
}

/**
 * Refreshes this action's text to use the last undone command's label.
 */
protected void refresh() {
	Command redoCmd = getCommandStack().getRedoCommand();
	setToolTipText(MessageFormat.format(
			GEFMessages.RedoAction_Tooltip,
			new Object [] {getLabelForCommand(redoCmd)}).trim());
	setText(MessageFormat.format(
			GEFMessages.RedoAction_Label,
			new Object[]{getLabelForCommand(redoCmd)}).trim());
	super.refresh();
}

/**
 * Redoes the last command.
 */
public void run() {
	getCommandStack().redo();
}

}
