package com.ibm.etools.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.text.MessageFormat;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.internal.*;

import com.ibm.etools.gef.internal.GEFMessages;

import com.ibm.etools.common.command.*;

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
 * Updates the labels and enabled state when the command stack is altered.
 *
 * @param e The <code>EventObject</code>.
 */
public void commandStackChanged(java.util.EventObject e) {
	refresh();
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
			GEFMessages.RedoAction_ActionToolTipText,	
			new Object[] {""}).trim());  //$NON-NLS-1$
	setText(MessageFormat.format(
			GEFMessages.RedoAction_ActionLabelText, 
			new Object[] {""}).trim()  //$NON-NLS-1$
			+ GEFMessages.RedoAction_ActionShortcutText);
	setId(org.eclipse.ui.IWorkbenchActionConstants.REDO);
	setHoverImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			IWorkbenchGraphicConstants.IMG_CTOOL_REDO_EDIT_HOVER));
	setImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			IWorkbenchGraphicConstants.IMG_CTOOL_REDO_EDIT));
	setDisabledImageDescriptor(
		WorkbenchImages.getImageDescriptor(
			IWorkbenchGraphicConstants.IMG_CTOOL_REDO_EDIT_DISABLED));	
}

/**
 * Refreshes this action's text to use the last undone command's label.
 */
protected void refresh(){
	Command redoCmd = getCommandStack().getRedoCommand();
	setToolTipText(MessageFormat.format(
			GEFMessages.RedoAction_ActionToolTipText,
			new Object [] {getLabelForCommand(redoCmd)}).trim());
	setText(MessageFormat.format(
			GEFMessages.RedoAction_ActionLabelText,
			new Object[]{getLabelForCommand(redoCmd)}).trim()
			+ GEFMessages.RedoAction_ActionShortcutText);
	super.refresh();
}

/**
 * Redoes the last command.
 */
public void run() {
	getCommandStack().redo();
}

}
