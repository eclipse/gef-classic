package org.eclipse.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.Disposable;
import org.eclipse.gef.Updatable;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;

/**
 * Base class for actions used by GEF editors.
 */
public abstract class EditorPartAction
	extends Action
	implements Disposable, Updatable
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
 * Called when the action is about to be disposed.
 */
public void dispose() {}

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
protected void refresh() {
	setEnabled(calculateEnabled());
}

/**
 * Sets the editor.
 */
protected void setEditorPart(IEditorPart part) {
	editorPart = part;
}

/**
 * @see org.eclipse.gef.Updatable#update()
 */
public void update() {
	refresh();
}

}