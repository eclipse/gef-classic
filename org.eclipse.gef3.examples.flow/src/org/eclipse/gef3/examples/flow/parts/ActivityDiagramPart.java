/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.examples.flow.parts;

import java.util.Map;

import org.eclipse.draw2dl.Figure;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.draw2dl.graph.CompoundDirectedGraph;
import org.eclipse.gef3.EditPolicy;
import org.eclipse.gef3.commands.CommandStack;
import org.eclipse.gef3.commands.CommandStackEventListener;
import org.eclipse.gef3.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef3.examples.flow.policies.ActivityContainerEditPolicy;
import org.eclipse.gef3.examples.flow.policies.StructuredActivityLayoutEditPolicy;
import org.eclipse.gef3.editparts.AbstractEditPart;

/**
 * @author hudsonr Created on Jul 16, 2003
 */
public class ActivityDiagramPart extends StructuredActivityPart {

	CommandStackEventListener stackListener = new CommandStackEventListener() {

		public void stackChanged(
				org.eclipse.gef3.commands.CommandStackEvent event) {
			if ((event.getDetail() & CommandStack.POST_MASK) != 0) {
				if (!GraphAnimation.captureLayout(getFigure()))
					return;
				while (GraphAnimation.step())
					getFigure().getUpdateManager().performUpdate();
				GraphAnimation.end();
			}
		};
	};

	protected void applyOwnResults(CompoundDirectedGraph graph, Map map) {
	}

	/**
	 * @see org.eclipse.gef3.examples.flow.parts.ActivityPart#activate()
	 */
	public void activate() {
		super.activate();
		getViewer().getEditDomain().getCommandStack()
				.addCommandStackEventListener(stackListener);
	}

	/**
	 * @see org.eclipse.gef3.examples.flow.parts.ActivityPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.NODE_ROLE, null);
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new RootComponentEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new StructuredActivityLayoutEditPolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE,
				new ActivityContainerEditPolicy());
	}

	protected IFigure createFigure() {
		Figure f = new Figure() {
			public void setBounds(Rectangle rect) {
				int x = bounds.x, y = bounds.y;

				boolean resize = (rect.width != bounds.width)
						|| (rect.height != bounds.height), translate = (rect.x != x)
						|| (rect.y != y);

				if (isVisible() && (resize || translate))
					erase();
				if (translate) {
					int dx = rect.x - x;
					int dy = rect.y - y;
					primTranslate(dx, dy);
				}
				bounds.width = rect.width;
				bounds.height = rect.height;
				if (resize || translate) {
					fireFigureMoved();
					repaint();
				}
			}
		};
		f.setLayoutManager(new GraphLayoutManager(this));
		return f;
	}

	/**
	 * @see org.eclipse.gef3.examples.flow.parts.ActivityPart#deactivate()
	 */
	public void deactivate() {
		getViewer().getEditDomain().getCommandStack()
				.removeCommandStackEventListener(stackListener);
		super.deactivate();
	}

	/**
	 * @see AbstractEditPart#isSelectable()
	 */
	public boolean isSelectable() {
		return false;
	}

	/**
	 * @see org.eclipse.gef3.examples.flow.parts.StructuredActivityPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
	}

}
