package com.ibm.etools.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * Superclass for an action needing the current selection.
 */
public abstract class SelectionAction
	extends EditorPartAction
	implements org.eclipse.ui.ISelectionListener
{

/*
 * The current selection.
 */
private ISelection selection;

/**
 * Creates a <code>SelectionAction</code> and associates it with the 
 * given editor.
 *
 * @param editor The editor that this action is associated with.
 * @param style  The style bitmask.
 */
public SelectionAction(IEditorPart editor, int style) {
	super(editor, style);
}

/**
 * Creates a <code>SelectionAction</code> and associates it with the 
 * given editor.
 *
 * @param editor The editor that this action is associated with.
 */
public SelectionAction(IEditorPart editor) {
	super(editor);
}

public void dispose(){
	this.selection = StructuredSelection.EMPTY;
	super.dispose();
}

/**
 * Gets the current selection.
 *
 * @return The current selection.
 */
protected ISelection getSelection() {
	if (isDynamic())
		return selection;
	return getEditorPart().
		getSite().
		getPage().
		getSelection();
}

/**
 * Returns a <code>List</code> containing the currently
 * selected objects.
 *
 * @return A List containing the currently selected objects.
 */
protected List getSelectedObjects() {
	if (!(getSelection() instanceof IStructuredSelection))
		return Collections.EMPTY_LIST;
	return ((IStructuredSelection)getSelection()).toList();
}

protected void handleSelectionChanged(){
	refresh();
}

/**
 * Add this action as a {@link org.eclipse.ui.ISelectionListener} to
 * the selection service.
 */
protected void hookEditorPart() {
	getEditorPart().
		getSite().
		getWorkbenchWindow().
		getSelectionService().
		addSelectionListener(this);
}

/**
 * Updates the enable state depending on the current selection.
 * The default is to enable of one item is selected.  Override
 * this in subclass if needed.
 * 
 * @param part The workbench.
 * @param selection The new selection.
 */
public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	setSelection(selection);
}

/**
 * Sets the current selection and calls on subclasses 
 * to handle the selectionChanged event.
 *
 * @param selection The new selection.
 */
protected void setSelection(ISelection selection) {
	this.selection = selection;
	handleSelectionChanged();
}

/**
 * Removes this selection listener from the selection service.
 */
protected void unhookEditorPart() {
	getEditorPart().
		getSite().
		getWorkbenchWindow().
		getSelectionService().
		removeSelectionListener(this);
}

}
