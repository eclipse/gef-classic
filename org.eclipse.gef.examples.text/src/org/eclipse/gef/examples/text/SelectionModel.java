/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.examples.text.edit.TextEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * SelectionModel is immutable.
 * @author Pratik Shah
 * @since 3.2
 */
public class SelectionModel
{
	
private final SelectionRange selectionRange;
private final EditPart selectionContainer;
private final List constantSelection;

public SelectionModel(ISelection selection) {
	this(null, 
			selection instanceof IStructuredSelection 
					? ((IStructuredSelection)selection).toList() 
					: null, 
			null);
}

public SelectionModel(SelectionRange range, List selectedParts,
		EditPart container) {
	selectionRange = range;
	selectionContainer = container;
	constantSelection = selectedParts == null ? Collections.EMPTY_LIST 
			: Collections.unmodifiableList(selectedParts);
}

protected void applySelectedParts() {
	if (!constantSelection.isEmpty()) {
		Iterator itr = constantSelection.iterator();
		while (true) {
			EditPart part = (EditPart)itr.next();
			if (!itr.hasNext()) {
				part.setSelected(EditPart.SELECTED_PRIMARY);
				break;
			}
			part.setSelected(EditPart.SELECTED);
		}
	}
}

protected void applySelectionRange() {
	SelectionRange range = getSelectionRange();
	if (range != null) {
		List currentSelection = range.getSelectedParts();
		for (int i = 0; i < currentSelection.size(); i++) {
			TextEditPart textpart = (TextEditPart)currentSelection.get(i);
			textpart.setSelection(0, textpart.getLength());
		}

		if (range.begin.part == range.end.part)
			range.begin.part.setSelection(range.begin.offset, range.end.offset);
		else {
			range.begin.part.setSelection(range.begin.offset, range.begin.part.getLength());
			range.end.part.setSelection(0, range.end.offset);
		}
	}
}

public void deselect() {
	deselectSelectedParts();
	deselectSelectionRange();
}

protected void deselectSelectedParts() {
	for (Iterator itr = constantSelection.iterator(); itr.hasNext();)
		((EditPart)itr.next()).setSelected(EditPart.SELECTED_NONE);
}

protected void deselectSelectionRange() {
	SelectionRange range = getSelectionRange();
	if (range != null) {
		List selection = range.getSelectedParts();
		for (int i = 0; i < selection.size(); i++)
			((TextEditPart)selection.get(i)).setSelection(-1, -1);
	}
}

public boolean equals(Object obj) {
	boolean result = obj == this;
	if (!result && obj instanceof SelectionModel) {
		SelectionModel other = (SelectionModel)obj;
		EditPart otherContainer = other.getSelectionContainer();
		SelectionRange otherRange = other.getSelectionRange();
		result = constantSelection.equals(other.getSelectedEditParts())
				&& (selectionContainer == otherContainer
				|| (selectionContainer != null && selectionContainer.equals(otherContainer)))
				&& (selectionRange == otherRange
				|| (selectionRange != null && selectionRange.equals(otherRange)));
	}
	return result;
}

public SelectionModel getAppendedSelection(EditPart newPart) {
	ArrayList list = new ArrayList(constantSelection);
	list.remove(newPart);
	list.add(newPart);
	return new SelectionModel(selectionRange, list, selectionContainer);
}

public SelectionModel getExcludedSelection(EditPart exclude) {
//	if (constantSelection.isEmpty())
//		return this;
	ArrayList list  = new ArrayList(constantSelection);
	list.remove(exclude);
	return new SelectionModel(selectionRange, list, selectionContainer);
}

public EditPart getFocusPart() {
	if (constantSelection.isEmpty())
		return null;
	return (EditPart)constantSelection.get(constantSelection.size() - 1);
}

public List getSelectedEditParts() {
	return constantSelection;
}

public ISelection getSelection() {
	return new StructuredSelection(constantSelection);
}

public EditPart getSelectionContainer() {
	return selectionContainer;
}

public SelectionRange getSelectionRange() {
	return selectionRange;
}

public void applySelection(SelectionModel old) {
	if (old == null) {
		applySelectedParts();
		applySelectionRange();
		return;
	}
	
	// Convert to HashSet to optimize performance.
	if (!old.getSelectedEditParts().isEmpty()) {
		Collection editparts = new HashSet(constantSelection);
		for (Iterator itr = old.getSelectedEditParts().iterator(); itr.hasNext();) {
			EditPart part = (EditPart)itr.next();
			if (!editparts.contains(part))
				part.setSelected(EditPart.SELECTED_NONE);
		}
	}
	applySelectedParts();
	
	old.deselectSelectionRange();
	applySelectionRange();
}

public boolean isTextSelected() {
	return selectionRange != null;
}

}