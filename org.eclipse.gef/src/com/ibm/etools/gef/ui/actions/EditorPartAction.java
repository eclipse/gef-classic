package com.ibm.etools.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.action.*;
import org.eclipse.ui.IEditorPart;

import com.ibm.etools.common.command.Command;
import com.ibm.etools.common.command.CommandStack;

import com.ibm.etools.gef.Disposable;

/**
 * Base class for actions used by GEF editors.
 */
public abstract class EditorPartAction
	extends Action
	implements Disposable
{

/**
 * The default style.  Uses {@link Action#isEnabled()} to 
 * determine an action's enabled state.  If an action is not 
 * dynamic, {@link #calculateEnabled()} is used.
 */
public static final int DYNAMIC = 1;

/*
 * The editor associated with this action.
 */
private IEditorPart editorPart;

/**
 * A style bitmask.
 */
protected final int style;

{
	init();
}

/**
 * Creates a new EditorPartAction and sets the editor and style.
 */
public EditorPartAction(IEditorPart editor, int style){
	this.style = checkStyle(style);
	setEditorPart(editor);
}

/**
 * Creates a new EditorPartAction and sets the editor.  Sets the
 * style to {@link #DYNAMIC}.
 *
 * @param editor The editor to be associated with this action.
 */
public EditorPartAction(IEditorPart editor) {
	this(editor, DYNAMIC);
}

/**
 * Calculates and returns the enabled state of this action.  
 * Subclasses that don't use the default {@link #DYNAMIC} style 
 * should override this method. 
 */
protected boolean calculateEnabled() {
	return false;
}

/**
 * Checks the given style to ensure it is not invalid.
 */
protected int checkStyle(int style){
	return style;
}

/**
 * Called when the action is about to be disposed.  Subclasses
 * should override {@link #unhookEditorPart()} instead of this
 * method to perform any final clean-up.
 */
public void dispose(){
	if (isDynamic())
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

protected boolean isDynamic(){
	return (style & DYNAMIC) != 0;
}

/**
 * If this action uses the default {@link #DYNAMIC} style, 
 * {@link Action#isEnabled()} is called.  Otherwise, 
 * {@link #calculateEnabled()} is used to determine the 
 * enabled state of this action.
 */
public boolean isEnabled() {
	if (isDynamic())
		return super.isEnabled();
	else
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
	if (editorPart != null && (style & DYNAMIC) != 0)
		hookEditorPart();
}

/**
 * Remove any needed listeners.
 */
protected void unhookEditorPart(){}

}