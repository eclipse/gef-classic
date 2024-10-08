/*******************************************************************************
 * Copyright (c) 2009-2010, 2024 Mateusz Matela and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Mateusz Matela - initial API and implementation Ian Bull
 ******************************************************************************/
package org.eclipse.zest.core.widgets;

import org.eclipse.zest.layouts.dataStructures.DisplayIndependentDimension;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentPoint;
import org.eclipse.zest.layouts.interfaces.EntityLayout;
import org.eclipse.zest.layouts.interfaces.LayoutContext;
import org.eclipse.zest.layouts.interfaces.NodeLayout;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A subgraph layout that represents a subgraph as a single figure. An entity
 * representing subgraph is not resizable by layout algorithm unless proper
 * methods are redefined in a subclass.
 *
 * @since 1.14
 */
public abstract class FigureSubgraph extends DefaultSubgraph {

	protected IFigure figure;
	private DisplayIndependentPoint location;
	private boolean isLayoutBeingApplied = false;

	/**
	 * Listens to changes in this subgraph's figure and fires proper event in its
	 * layout context.
	 */
	protected class SubgraphFigrueListener implements FigureListener {
		private Rectangle previousBounds = figure.getBounds().getCopy();

		@Override
		public void figureMoved(IFigure source) {
			if (Animation.isAnimating() || isLayoutBeingApplied) {
				return;
			}
			Rectangle newBounds = figure.getBounds();
			if (!newBounds.getSize().equals(previousBounds.getSize())) {
				(context).fireSubgraphResizedEvent(FigureSubgraph.this);
			} else if (!newBounds.getLocation().equals(previousBounds.getLocation())) {
				(context).fireSubgraphMovedEvent(FigureSubgraph.this);
			}
			previousBounds = newBounds.getCopy();
		}
	}

	/**
	 * Creates a figure for this subgraph and stores it in {@link #figure}.
	 *
	 * This method may not be called right after creation of the subgraph but later
	 * when the figure is actually needed (lazy initialization).
	 */
	protected abstract void createFigure();

	/**
	 * Updates the figure stored in {@link #figure} depending on current nodes
	 * contained in this subgraph. If this method creates a new instance of IFigure,
	 * it should remember to add a {@link SubgraphFigrueListener} to it.
	 */
	protected abstract void updateFigure();

	public IFigure getFigure() {
		if (figure == null) {
			createFigure();
			updateFigure();
			figure.addFigureListener(new SubgraphFigrueListener());
			(context).container.addSubgraphFigure(figure);
		}
		return figure;
	}

	protected FigureSubgraph(NodeLayout[] nodes, LayoutContext context) {
		super(context);
		addNodes(nodes);
	}

	/**
	 * {@inheritDoc}
	 *
	 * All nodes added to this subgraph are moved to the center of the figure (so
	 * that collapsing and expanding animation looks cool).
	 */
	@Override
	public void addNodes(NodeLayout[] nodes) {
		int initialCount = this.nodes.size();
		super.addNodes(nodes);
		if (this.nodes.size() > initialCount && figure != null) {
			updateFigure();
			if (location != null) {
				for (NodeLayout node : nodes) {
					node.setLocation(location.x, location.y);
				}
			}
		}
	}

	@Override
	public void removeNodes(NodeLayout[] nodes) {
		int initialCount = this.nodes.size();
		super.removeNodes(nodes);
		if (this.nodes.size() < initialCount && figure != null && !disposed) {
			updateFigure();
		}
	}

	@Override
	public EntityLayout[] getSuccessingEntities() {
		// TODO Auto-generated method stub
		return super.getSuccessingEntities();
	}

	@Override
	public EntityLayout[] getPredecessingEntities() {
		// TODO Auto-generated method stub
		return super.getPredecessingEntities();
	}

	@Override
	public DisplayIndependentDimension getSize() {
		Dimension size = getFigure().getSize();
		return new DisplayIndependentDimension(size.width, size.height);
	}

	@Override
	public DisplayIndependentPoint getLocation() {
		if (location == null) {
			Point location2 = getFigure().getBounds().getLocation();
			Dimension size = getFigure().getSize();
			return new DisplayIndependentPoint(location2.x + size.width / 2, location2.y + size.height / 2);
		}
		return new DisplayIndependentPoint(location);
	}

	@Override
	public void setLocation(double x, double y) {
		super.setLocation(x, y);
		for (Object node2 : nodes) {
			NodeLayout node = (NodeLayout) node2;
			node.setLocation(x, y);
		}

		if (location != null) {
			location.x = x;
			location.y = y;
		} else {
			location = new DisplayIndependentPoint(x, y);
			// the first location change will be applied immediately
			applyLayoutChanges();
		}
	}

	@Override
	protected void refreshLocation() {
		Rectangle bounds = figure.getBounds();
		if (location == null) {
			location = new DisplayIndependentPoint(0, 0);
		}
		location.x = bounds.x + bounds.width / 2;
		location.y = bounds.y + bounds.height / 2;
	}

	@Override
	public boolean isGraphEntity() {
		return true;
	}

	@Override
	public boolean isMovable() {
		return true;
	}

	@Override
	protected void dispose() {
		if (!disposed) {
			super.dispose();
			if (figure != null) {
				context.container.getGraph().removeSubgraphFigure(figure);
			}
		}
	}

	@Override
	protected void applyLayoutChanges() {
		getFigure();
		if (location != null) {
			isLayoutBeingApplied = true;
			Dimension size = figure.getSize();
			figure.setLocation(new PrecisionPoint(location.x - size.width / 2, location.y - size.height / 2));
			isLayoutBeingApplied = false;
		}
	}

}
