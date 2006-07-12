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
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.SelectionManager;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;

/**
 * This Selection Manager converts edit parts to the user data type and returns
 * those. If this were not here, Zest would return the Edit parts as selection
 * and not what the user passed in through the content provider.
 * 
 * @author Ian Bull
 * 
 */
public class ZestSelectionManager extends SelectionManager {

	public ISelection getSelection() {
		ISelection selection = super.getSelection();
		List selections = new ArrayList(1);

		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		Iterator elements = structuredSelection.iterator();
		
		// Loop through all selected elements and get the user data for these
		while (elements.hasNext()) {
			AbstractEditPart editPart = (AbstractEditPart) elements.next();
			GraphItem item = (GraphItem) editPart.getModel();
			if (!(item instanceof GraphModel)) {
				selections.add(item.getData());
			}
		}

		// If nothing is selected just return null.  what should happen on an
		// unselection?
		if ( selections.size() == 0 ) return null;
		return new StructuredSelection( selections );

	}

}
