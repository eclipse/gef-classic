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
 * An action to undo the last command.
 */
public class UndoAction
	extends StackAction
{

/**
 * Creates an <code>UndoAction</code> and associates it with the 
 * given editor.
 * 
 * @param editor The editor this action is associated with.
 */
public UndoAction(IEditorPart editor) {
	super(editor);
}

/**
 * Returns <code>true</code> if the {@link CommandStack} can undo
 * the last command.
 */
protected boolean calculateEnabled(){
	return getCommandStack().canUndo();
}

/**
 * Initializes this action's text and images.
 */
protected void init(){
	super.init();
	setToolTipText(MessageFormat.format(
			GEFMessages.UndoAction_Tooltip,	
			new Object[] {""}).trim());  //$NON-NLS-1$
	setText(MessageFormat.format(
			GEFMessages.UndoAction_Label, 
			new Object[] {""}).trim()  //$NON-NLS-1$
			);
	setId(GEFActionConstants.UNDO);
	setHoverImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			ISharedImages.IMG_TOOL_UNDO_HOVER));
	setImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			ISharedImages.IMG_TOOL_UNDO));
	setDisabledImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			ISharedImages.IMG_TOOL_UNDO_DISABLED));
}

/**
 * Refreshes this action's text to use the last executed command's label.
 */
protected void refresh(){
	Command undoCmd = getCommandStack().getUndoCommand();
	setToolTipText(MessageFormat.format(
			GEFMessages.UndoAction_Tooltip,
			new Object []{getLabelForCommand(undoCmd)}).trim());
	setText(MessageFormat.format(
			GEFMessages.UndoAction_Label,
			new Object []{getLabelForCommand(undoCmd)}).trim()
			);
	super.refresh();
}

/**
 * Undoes the last command.
 */
public void run() {
	getCommandStack().undo();
}

}
