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
package org.eclipse.mylar.zest.core.internal.graphviewer.overview.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;


/**
 * Creates the edit parts associated with the different models.
 * @author Chris Callendar
 */
public class OverviewImageEditPartFactory implements EditPartFactory {
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (model instanceof EditPartViewer) {
			editPart = new OverviewEditPart();
		} else if (model instanceof GraphicalEditPart) {
			editPart = new OverviewImageEditPart();
		}
		editPart.setModel(model);
		
		return editPart;
	}

}
