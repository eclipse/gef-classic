/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;

/**
 * A utility for synchronizing the selection of multiple EditPartViewers.  This class
 * performs selection synchronization by taking the selection from one viewer, and mapping
 * it to the selection in another viewer.  The mapping is performed by matching the models
 * of the selected EditParts from one viewer to the EditParts with the same models in
 * another. The can be customized by overriding the {@link #convert(EditPartViewer,
 * EditPart)} method.
 * @author hudsonr
 */
public class SelectionSynchronizer
	implements ISelectionChangedListener
{

private List viewers = new ArrayList();
private boolean isDispatching = false;

/**
 * Adds a viewer to the set of synchronized viewers
 * @param viewer the viewer
 */
public void addViewer(EditPartViewer viewer) {
	viewer.addSelectionChangedListener(this);
	viewers.add(viewer);
}

/**
 * Maps the given editpart from one viewer to an editpart in another viewer. It returns
 * <code>null</code> if there is no corresponding part. This method can be overridden
 * to provide custom mapping.
 * @param viewer the viewer being mapped to
 * @param part a part from another viewer
 * @return <code>null</code> or a corresponding editpart
 */
protected EditPart convert(EditPartViewer viewer, EditPart part) {
	Object temp = viewer.getEditPartRegistry().get(part.getModel());
	EditPart newPart = null;
	if (temp != null) {
		newPart = (EditPart)temp;
	}
	return newPart;
}

/**
 * Removes the viewer from the set of synchronzied viewers
 * @param viewer the viewer to remove
 */
public void removeViewer(EditPartViewer viewer) {
	viewer.removeSelectionChangedListener(this);
	viewers.remove(viewer);
}

/**
 * Receives notification from one viewer, and maps selection to all other members.
 * @param event the selection event
 */
public void selectionChanged(SelectionChangedEvent event) {
	if (isDispatching)
		return;
	isDispatching = true;
	EditPartViewer source = (EditPartViewer)event.getSelectionProvider();
	ISelection selection = event.getSelection();
	for (int i = 0; i < viewers.size(); i++) {
		if (viewers.get(i) != source) {
			EditPartViewer viewer = (EditPartViewer)viewers.get(i);
			setViewerSelection(viewer, selection);
		}
	}
	isDispatching = false;
}

private void setViewerSelection(EditPartViewer viewer, ISelection selection) {
	ArrayList result = new ArrayList();
	Iterator iter = ((IStructuredSelection)selection).iterator();
	while (iter.hasNext()) {
		EditPart part = convert(viewer, (EditPart)iter.next());
		if (part != null)
			result.add(part);
	}
	viewer.setSelection(new StructuredSelection(result));
	if (result.size() > 0)
		viewer.reveal((EditPart)result.get(result.size() - 1));
}

}