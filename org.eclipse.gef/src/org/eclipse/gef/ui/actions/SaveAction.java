package org.eclipse.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * An action to save the editor's current state.
 */
public class SaveAction
	extends EditorPartAction
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
 * Initializes this action's text.
 */
protected void init(){
	setText( GEFMessages.SaveAction_ActionLabelText + 
			 GEFMessages.SaveAction_ActionShortcutText);
	setToolTipText(GEFMessages.SaveAction_ActionToolTipText);
	setId(IWorkbenchActionConstants.SAVE);
}

/**
 * Saves the state of the associated editor.
 */
public void run() {
	getEditorPart().getSite().getPage().saveEditor(getEditorPart(), false);
}

}
