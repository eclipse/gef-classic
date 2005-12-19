/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text;
  
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.widgets.Caret;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

/**
 * @since 3.1
 */
public class GraphicalTextViewer 
	extends ScrollingGraphicalViewer 
{

private SelectionModel selectionModel;

/**
 * Returns the viewers selection range by <em>reference</em>.  The range should not be
 * modified directly.
 * @since 3.1
 * @return the current selection by reference
 * @deprecated in 3.2. @TODO:Pratik  remove this method and all references to it.  Use
 * getSelectionModel() instead.
 */
public SelectionRange getSelectionRange() {
	if (selectionModel != null)
		return selectionModel.getSelectionRange();
	return null;
}

public void revealCaret() {
	Assert.isNotNull(getControl(), "The control has not been created yet.");
	Caret caret = getFigureCanvas().getCaret();
	if (caret == null || !caret.isVisible())
		return;
	// @TODO:Pratik  you should expose the text location first (it might not be visible)
	Viewport port = getFigureCanvas().getViewport();
	Rectangle view = new Rectangle(port.getViewLocation(), port.getClientArea().getSize());
	Rectangle exposeRegion = new Rectangle(caret.getBounds());
	port.getContents().translateToRelative(exposeRegion);
	if (!view.contains(exposeRegion)) {
		int x = view.x, y = view.y;
		if (exposeRegion.x < view.x)
			x = exposeRegion.x;
		else if (exposeRegion.right() > view.right())
			x = view.x + exposeRegion.right() - view.right();
		if (exposeRegion.y < view.y)
			y = exposeRegion.y;
		else if (exposeRegion.bottom() > view.bottom())
			y = view.y + exposeRegion.bottom() - view.bottom();
		getFigureCanvas().scrollTo(x, y);		
	}
}

/**
 * Sets the selection range to the given value.  Updates any editparts which had or will
 * have textual selection.  Fires selection changed.  Place the caret in the appropriate
 * location.
 * 
 * @since 3.1
 * @param newRange the new selection range
 * @deprecated in 3.2. @TODO:Pratik  remove this method and all references to it.  Use
 * setSelectionModel() instead.
 */
public void setSelectionRange(SelectionRange newRange) {
	// @TODO:Pratik  change all these setSelection() methods so that they don't affect
	// any other selection.  So, setting selectionRange to null would not clear the
	// selected editparts.
	SelectionModel newModel = null;
	if (newRange != null)
		newModel = createSelectionModel(null, newRange, 
				selectionModel == null ? null : selectionModel.getSelectedEditParts(), 
				null);
	setSelectionModel(newModel);
}

public SelectionModel getSelectionModel() {
	return selectionModel;
}

public void setSelectionModel(SelectionModel selection) {
	if (selection != null) {
//		setFocus(selection.getFocusPart());
		selection.applySelection(selectionModel);
	} else if (selectionModel != null) {
		setFocus(null);
		selectionModel.deselect();
	}
	
	selectionModel = selection;
	
	fireSelectionChanged();
}

public void appendSelection(EditPart editpart) {
	if (focusPart != editpart)
		setFocus(null);
	if (selectionModel != null)
		setSelectionModel(selectionModel.getAppendedSelection(editpart));
	else
		select(editpart);
}

public void deselect(EditPart editpart) {
	if (selectionModel != null)
		setSelectionModel(selectionModel.getExcludedSelection(editpart));
}

public void deselectAll() {
	setSelectionModel(null);
}

public void select(EditPart editpart) {
	if (focusPart != editpart)
		setFocus(null);
	ArrayList list = new ArrayList();
	list.add(editpart);
	setSelectionModel(createSelectionModel(null, null, list, null));
}

// @TODO:Pratik  Hack.  This shouldn't be here.  CommandStack should be doing this automatically.
// You can make that change once you remove the GraphicalTextViewer class.
public void setEditDomain(EditDomain domain) {
	super.setEditDomain(domain);
	getEditDomain().getCommandStack().addCommandStackEventListener(new CommandStackEventListener() {
		public void stackChanged(CommandStackEvent event) {
			if (!(event.getCommand() instanceof TextCommand) || getSelectionRange() == null)
				return;
			TextCommand command = (TextCommand)event.getCommand();
			if (command != null) {
				if (event.getDetail() == CommandStack.POST_EXECUTE)
					setSelectionRange(
							command.getExecuteSelectionRange(GraphicalTextViewer.this));
				else if (event.getDetail() == CommandStack.POST_REDO)
					setSelectionRange(
							command.getRedoSelectionRange(GraphicalTextViewer.this));
				else if (event.getDetail() == CommandStack.POST_UNDO)
						setSelectionRange(
								command.getUndoSelectionRange(GraphicalTextViewer.this));
			}
		}
	});
}

public void setSelection(ISelection newSelection) {
	if (newSelection != null)
		setSelectionModel(createSelectionModel(newSelection, null, null, null));
	else
		setSelectionModel(null);
}

public ISelection getSelection() {
	if (selectionModel != null)
		return selectionModel.getSelection();
	return new StructuredSelection(getContents());
}

protected SelectionModel createSelectionModel(ISelection selection, SelectionRange range, 
		List parts, EditPart container) {
	if (selection instanceof IStructuredSelection)
		return new SelectionModel(selection);
	return new SelectionModel(range, parts, container); 
}

/**
 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#getSelectedEditParts()
 */
public List getSelectedEditParts() {
	return primGetSelectedEditParts();
}

/**
 * The method returns the same list as getSelectedEditParts().  The list is unmodifiable.
 * @deprecated
 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#primGetSelectedEditParts()
 */
protected List primGetSelectedEditParts() {
	if (selectionModel != null)
		return selectionModel.getSelectedEditParts();
	return Collections.EMPTY_LIST;
}

public boolean isTextSelected() {
	return selectionModel != null && selectionModel.isTextSelected();
}

protected LightweightSystem getLightweightSystem() {
	return super.getLightweightSystem();
}

}