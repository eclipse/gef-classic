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

package org.eclipse.gef3.examples.digraph1.editpart;

import java.util.List;

import org.eclipse.draw2dl.FreeformLayer;
import org.eclipse.draw2dl.FreeformLayout;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.gef3.EditPolicy;
import org.eclipse.gef3.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef3.examples.digraph1.model.Digraph1Graph;
import org.eclipse.gef3.examples.digraph1.model.Digraph1Node;
import org.eclipse.gef3.examples.digraph1.policy.Digraph1XYLayoutEditPolicy;

/**
 * The edit part for the directed graph.
 * 
 * @author Anthony Hunter
 */
public class Digraph1GraphEditPart extends AbstractGraphicalEditPart {

	/*
	 * @see org.eclipse.gef3.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new Digraph1XYLayoutEditPolicy());
	}

	/*
	 * @see org.eclipse.gef3.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		FreeformLayer freeformLayer = new FreeformLayer();
		freeformLayer.setLayoutManager(new FreeformLayout());
		return freeformLayer;
	}

	/*
	 * @see org.eclipse.gef3.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List<Digraph1Node> getModelChildren() {
		List<Digraph1Node> nodes = ((Digraph1Graph) getModel()).getNodes();
		return nodes;
	}

}
