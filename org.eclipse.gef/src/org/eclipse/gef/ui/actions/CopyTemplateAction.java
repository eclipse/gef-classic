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

import org.eclipse.jface.viewers.*;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;

/**
 * Copies the currently selected template in the palatte to the default GEF 
 * {@link org.eclipse.gef.ui.actions.Clipboard}.
 * @author Eric Bordeau
 */
public class CopyTemplateAction 
	extends EditorPartAction
	implements ISelectionChangedListener
{

private CombinedTemplateCreationEntry selectedEntry;

/**
 * Constructs a new CopyTemplateAction.  You must manually add this action to the palette
 * viewer's list of selection listeners.  Otherwise, this action's enabled state won't be
 * updated properly.
 * 
 * @see org.eclipse.gef.ui.actions.EditorPartAction#EditorPartAction(IEditorPart)
 */
public CopyTemplateAction(IEditorPart editor) {
	super(editor);
}

/**
 * Returns whether the selected EditPart is a {@link TemplateEditPart}.
 * @return whether the selected EditPart is a TemplateEditPart
 */
protected boolean calculateEnabled() {
	return selectedEntry != null;
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#dispose()
 */
public void dispose() {
	selectedEntry = null;
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
 */
protected void init() {
	setId(GEFActionConstants.COPY);
	setText(GEFMessages.CopyAction_Label);
}

/**
 * Sets the default {@link Clipboard Clipboard's} contents to be the currently selected
 * template.
 */
public void run() {
	Clipboard.getDefault().setContents(selectedEntry.getTemplate());
}

/**
 * Sets the selected EditPart and refreshes the enabled state of this action.
 * 
 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
 */
public void selectionChanged(SelectionChangedEvent event) {
	ISelection s = event.getSelection();
	if (!(s instanceof IStructuredSelection))
		return;
	IStructuredSelection selection = (IStructuredSelection)s;
	selectedEntry = null;
	if (selection != null && selection.size() == 1) {
		Object obj = selection.getFirstElement();
		if (obj instanceof EditPart) {
			Object model = ((EditPart)obj).getModel();
			if (model instanceof CombinedTemplateCreationEntry)
				selectedEntry = (CombinedTemplateCreationEntry)model;
		}
	}
	refresh();
}

}
