/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.actions;

import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteTemplateEntry;
import org.eclipse.gef.ui.actions.Clipboard;

/**
 * Copies the currently selected template in the palatte to the system clipboard. 
 * @author Eric Bordeau
 */
public class CopyTemplateAction 
	extends WorkbenchPartAction
	implements ISelectionChangedListener
{

private Object template;

/**
 * Constructs a new CopyTemplateAction.  You must manually add this action to the palette
 * viewer's list of selection listeners.  Otherwise, this action's enabled state won't be
 * updated properly.
 * 
 * @param editor the workbench part
 * @see org.eclipse.gef.ui.actions.EditorPartAction#EditorPartAction(IEditorPart)
 */
public CopyTemplateAction(IEditorPart editor) {
	super(editor);
	setId(ActionFactory.COPY.getId());
	setText(GEFMessages.CopyAction_Label);
}

/**
 * Returns whether the selected EditPart is a TemplateEditPart.
 * @return whether the selected EditPart is a TemplateEditPart
 */
protected boolean calculateEnabled() {
	return template != null;
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#dispose()
 */
public void dispose() {
	template = null;
}

/**
 * Sets the default {@link Clipboard Clipboard's} contents to be the currently selected
 * template.
 */
public void run() {
	org.eclipse.swt.dnd.Clipboard cb = 
			new org.eclipse.swt.dnd.Clipboard(Display.getDefault());
	cb.setContents(
			new Object[] {template}, new Transfer[] {TemplateTransfer.getInstance()});
	cb.dispose();
	/*
	 * The template is being added to the System clipboard so that it will clear other
	 * items on the clipboard, and will be cleared when something else is placed on the
	 * System clipboard (See Bug# 71395).  TemplateTransfer doesn't write the actual 
	 * object to the Clipboard, but just refers to it instead.  This could get lost during 
	 * a drag-and-drop from the palette.  To prevent that, we also set the template on the
	 * GEF clipboard.  So, pasting is enabled based on the System clipboard, but the
	 * actual template is retrieved from the GEF clipboard (since DND could have
	 * over-written it in the TemplateTransfer instance).
	 */
	Clipboard.getDefault().setContents(template);
}

/**
 * Sets the selected EditPart and refreshes the enabled state of this action.
 * 
 * @see ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
 */
public void selectionChanged(SelectionChangedEvent event) {
	ISelection s = event.getSelection();
	if (!(s instanceof IStructuredSelection))
		return;
	IStructuredSelection selection = (IStructuredSelection)s;
	template = null;
	if (selection != null && selection.size() == 1) {
		Object obj = selection.getFirstElement();
		if (obj instanceof EditPart) {
			Object model = ((EditPart)obj).getModel();
			if (model instanceof CombinedTemplateCreationEntry)
				template = ((CombinedTemplateCreationEntry)model).getTemplate();
			else if (model instanceof PaletteTemplateEntry)
				template = ((PaletteTemplateEntry)model).getTemplate();
		}
	}
	refresh();
}

}
