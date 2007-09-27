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

package org.eclipse.gef.examples.digraph1.factory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.examples.digraph1.editpart.Digraph1NodeEditPart;
import org.eclipse.gef.examples.digraph1.editpart.Digraph1GraphEditPart;
import org.eclipse.gef.examples.digraph1.model.Digraph1Node;
import org.eclipse.gef.examples.digraph1.model.Digraph1Graph;

/**
 * A factory for creating new EditParts for the directed graph.
 *
 * @author Anthony Hunter
 */
public class Digraph1EditPartFactory implements EditPartFactory {

	/*
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 *      java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (model instanceof Digraph1Graph) {
			editPart = new Digraph1GraphEditPart();
		} else if (model instanceof Digraph1Node) {
			editPart = new Digraph1NodeEditPart();
		}

		if (editPart != null) {
			editPart.setModel(model);
		}

		return editPart;
	}
}
