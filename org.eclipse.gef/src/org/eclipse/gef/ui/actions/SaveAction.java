package org.eclipse.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchActionConstants;

import org.eclipse.gef.internal.GEFMessages;

/**
 * An action to save the editor's current state.
 */
public class SaveAction
	extends EditorPartAction
	implements IPropertyListener
{

/**
 * Creates a <code>SaveAction</code> and associates it with the 
 * given editor.
 */
public SaveAction(IEditorPart editor) {
	super(editor);
}

/**
 * Returns <code>true<code> if the editor is dirty.
 */
protected boolean calculateEnabled() {
	return getEditorPart().isDirty();
}

/**
 * Adds this action to the editor's list of 
 * {@link IPropertyListener}s.
 */
protected void hookEditorPart(){
	getEditorPart().addPropertyListener(this);
}

/**
 * Initializes this action's text.
 */
protected void init(){
	setText( GEFMessages.SaveAction_ActionLabelText + 
			 GEFMessages.SaveAction_ActionShortcutText);
	setToolTipText(GEFMessages.SaveAction_ActionToolTipText);
	setId(IWorkbenchActionConstants.SAVE);
}

/**
 * Updates this action when the editor's state
 * becomes dirty.
 *
 * @param o The object whose property has changed.
 * @param i The property that has changed.
 */
public void propertyChanged(Object o, int i) {
	if (i == IEditorPart.PROP_DIRTY)
		refresh();
}

/**
 * Saves the state of the associated editor.
 */
public void run() {
	getEditorPart().getSite().getPage().saveEditor(getEditorPart(), false);
}

/**
 * Removes this action from the editor's list of 
 * {@link IPropertyListener}s.
 */
public void unhookEditorPart() {
	getEditorPart().removePropertyListener(this);
}

}
