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
package org.eclipse.mylar.zest.core.internal.springgraphviewer.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode;


/**
 * Creates the edit parts associated with the different models.
 * @author ccallendar
 */
public class GraphEditPartFactory implements EditPartFactory {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (model instanceof GraphModelNode) {
			editPart = new GraphNodeEditPart();
			((GraphModelNode)model).setEditPart( editPart );
		} else if (model instanceof GraphModelConnection) {
			editPart = new GraphConnectionEditPart();
		} else if (model instanceof GraphModel) {
			editPart = new GraphEditPart();
		} else {
			throw new RuntimeException("Can't create part for model element: " + 
					((model != null) ? model.getClass().getName() : "null"));
		}
		editPart.setModel(model);
		
		return editPart;
	}

}
