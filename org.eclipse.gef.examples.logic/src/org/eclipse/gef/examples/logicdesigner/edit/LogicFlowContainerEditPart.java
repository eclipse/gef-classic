/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.EditPolicy;

import org.eclipse.gef.examples.logicdesigner.figures.LogicFlowBorder;

public class LogicFlowContainerEditPart extends LogicContainerEditPart {

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.NODE_ROLE, null);
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new LogicFlowEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE,
				new ContainerHighlightEditPolicy());
	}

	protected IFigure createFigure() {
		Figure figure = new Figure();
		figure.setLayoutManager(new FlowLayout());
		figure.setBorder(new LogicFlowBorder());
		figure.setOpaque(true);
		return figure;
	}

}
