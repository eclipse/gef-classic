package org.eclipse.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IEditorPart;

/**
 * Superclass for actions needing access to the stack.
 */
public abstract class StackAction
	extends EditorPartAction
{

/**
 * Creates a <code>StackAction</code> and associates it with the 
 * given editor.
 *
 * @param editor The editor this action is associated with.
 */
public StackAction(IEditorPart editor) {
	super(editor);
}

/**
 * Returns a label for the given command.  Ensures
 * <code>null</code> is never returned.
 */
protected String getLabelForCommand(Command command){
	if(command==null)
		return "";                  //$NON-NLS-1$
	if(command.getLabel()==null)
		return "";                  //$NON-NLS-1$
	else
		return command.getLabel();
}

}