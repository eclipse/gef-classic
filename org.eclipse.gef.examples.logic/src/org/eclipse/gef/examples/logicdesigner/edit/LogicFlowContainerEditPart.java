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

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.ToolbarLayout;

import org.eclipse.gef.EditPolicy;

import org.eclipse.gef.examples.logicdesigner.figures.LogicFlowBorder;
import org.eclipse.gef.examples.logicdesigner.model.LogicFlowContainer;

public class LogicFlowContainerEditPart extends LogicContainerEditPart {

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.NODE_ROLE, null);
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new LogicFlowEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE,
				new ContainerHighlightEditPolicy());
	}

	public void activate() {
		if (isActive()) {
			return;
		}
		super.activate();
		updateLayout(getLogicFlowContainer().getLayout());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (LogicFlowContainer.ID_LAYOUT.equals(prop)) {
			updateLayout((Integer) evt.getNewValue());
		} else {
			super.propertyChange(evt);
		}
	}

	protected void updateLayout(Integer newLayout) {
		getFigure().setLayoutManager(createLayout(newLayout));
	}

	private OrderedLayout createLayout(Integer newLayout) {
		OrderedLayout layout;
		if (newLayout.equals(LogicFlowContainer.LAYOUT_SINGLE_ROW)) {
			layout = new ToolbarLayout(false);
			((ToolbarLayout) layout).setSpacing(5);
		} else {
			layout = new FlowLayout();
		}
		return layout;
	}

	protected LogicFlowContainer getLogicFlowContainer() {
		return (LogicFlowContainer) getModel();
	}

	protected IFigure createFigure() {
		Figure figure = new Figure();
		figure.setLayoutManager(createLayout(getLogicFlowContainer()
				.getLayout()));
		figure.setBorder(new LogicFlowBorder());
		figure.setOpaque(true);
		return figure;
	}

}
