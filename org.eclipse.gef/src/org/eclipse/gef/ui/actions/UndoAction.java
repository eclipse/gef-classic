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
 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
 */
protected boolean calculateEnabled() {
	return getCommandStack().canUndo();
}

/**
 * Initializes this action's text and images.
 */
protected void init() {
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
protected void refresh() {
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
