/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.actions;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Superclass for actions needing access to the stack.
 */
public abstract class StackAction
	extends WorkbenchPartAction
{

/**
 * Creates a <code>StackAction</code> and associates it with the 
 * given editor.
 *
 * @param editor The editor this action is associated with.
 */
public StackAction(IWorkbenchPart editor) {
	super(editor);
}

/**
 * Returns the label for the given command.  Guarantees that <code>null</code> is never
 * returned.
 * @param command the command
 * @return a non-<code>null</code> String
 */
protected String getLabelForCommand(Command command) {
	if (command == null)
		return "";//$NON-NLS-1$
	if (command.getLabel() == null)
		return "";//$NON-NLS-1$
	else
		return command.getLabel();
}

}