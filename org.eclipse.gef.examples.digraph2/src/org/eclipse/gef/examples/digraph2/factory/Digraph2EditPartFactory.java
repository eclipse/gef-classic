/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.gef.examples.digraph2.factory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.examples.digraph2.editpart.Digraph2EdgeEditPart;
import org.eclipse.gef.examples.digraph2.editpart.Digraph2GraphEditPart;
import org.eclipse.gef.examples.digraph2.editpart.Digraph2NodeEditPart;
import org.eclipse.gef.examples.digraph2.model.Digraph2Edge;
import org.eclipse.gef.examples.digraph2.model.Digraph2Graph;
import org.eclipse.gef.examples.digraph2.model.Digraph2Node;

/**
 * A factory for creating new EditParts for the directed graph.
 * 
 * @author Anthony Hunter
 */
public class Digraph2EditPartFactory implements EditPartFactory {

	/*
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 *      java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (model instanceof Digraph2Graph) {
			editPart = new Digraph2GraphEditPart();
		} else if (model instanceof Digraph2Edge) {
			editPart = new Digraph2EdgeEditPart();
		} else if (model instanceof Digraph2Node) {
			editPart = new Digraph2NodeEditPart();
		}

		if (editPart != null) {
			editPart.setModel(model);
		}

		return editPart;
	}
}
