/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphviewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.SelectionManager;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphItem;

/**
 * This Selection Manager converts edit parts to the user data type and returns
 * those. If this were not here, Zest would return the Edit parts as selection
 * and not what the user passed in through the content provider.
 * 
 * @author Ian Bull
 * 
 */
public class ZestSelectionManager extends SelectionManager {
	
	/**
	 * Gets a list of currently selected nodes.  The selection will include the "user objects"
	 * that have been selected.
	 * 
	 * @return A List of user objects that has been selected.
	 */
	public List getSelectedModelElements() {
		List selections = new ArrayList(1);
		ISelection selection = super.getSelection();
		
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		Iterator elements = structuredSelection.iterator();
		
		// Loop through all selected elements and get the user data for these
		while (elements.hasNext()) {
			AbstractEditPart editPart = (AbstractEditPart) elements.next();
			if (editPart.getModel() == null) {
				return Collections.EMPTY_LIST;
			}
			if (editPart.getModel() instanceof IGraphItem) {
				IGraphItem item = (IGraphItem) editPart.getModel();

				if (!(item instanceof GraphModel)) {
					selections.add(item.getData());
				}
			}
		}
		return selections;
	}
	
	/**
	 * Gets the current selection in the Zest viewer as an ISelection.
	 */
	public ISelection getSelection() {
		List selections = getSelectedModelElements();
		if ( selections.size() == 0 ) return StructuredSelection.EMPTY;
		return new StructuredSelection( selections );
	}

}
