package org.eclipse.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.action.*;
import org.eclipse.ui.IEditorPart;


import org.eclipse.gef.Disposable;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;

/**
 * Base class for actions used by GEF editors.
 */
public abstract class EditorPartAction
	extends Action
	implements Disposable
{

/*
 * The editor associated with this action.
 */
private IEditorPart editorPart;

/**
 * Creates a new EditorPartAction and sets the editor.
 *
 * @param editor The editor to be associated with this action.
 */
public EditorPartAction(IEditorPart editor) {
	setEditorPart(editor);
	init();
}

/**
 * Calculates and returns the enabled state of this action.  
 */
protected abstract boolean calculateEnabled();

/**
 * Called when the action is about to be disposed.  Subclasses
 * should override {@link #unhookEditorPart()} instead of this
 * method to perform any final clean-up.
 */
public void dispose(){
	unhookEditorPart();
}

/**
 * Executes the given {@link Command}.
 */
protected void execute(Command command) {
	if (command == null || !command.canExecute())
		return;
	getCommandStack().execute(command);
}

/**
 * Returns the editor's command stack.
 */
protected CommandStack getCommandStack() {
	return (CommandStack)getEditorPart().getAdapter(CommandStack.class);
}

/**
 * Returns the editor associated with this action.
 */
protected IEditorPart getEditorPart() {
	return editorPart;
}

/**
 * Adds any needed listeners.
 */
protected void hookEditorPart() {}

/**
 * Initializes this action.
 */
protected void init(){}

/**
 * Calls {@link #calculateEnabled()} to determine the enabled state of this action.
 */
public boolean isEnabled() {
	return calculateEnabled();
}

/**
 * Refreshes the properties of this action.
 */
protected void refresh(){
	setEnabled(calculateEnabled());
}

/**
 * Sets the editor.
 */
protected void setEditorPart(IEditorPart part) {
	editorPart = part;
	if (editorPart != null)
		hookEditorPart();
}

/**
 * Remove any needed listeners.
 */
protected void unhookEditorPart(){}

}