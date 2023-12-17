/*******************************************************************************
 * Copyright 2005-2010, 2023 CHISEL Group, University of Victoria, Victoria, BC,
 * 						Canada.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 *               Mateusz Matela
 *******************************************************************************/
package org.eclipse.zest.core.widgets;

import java.util.List;

import org.eclipse.swt.widgets.Widget;

import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentRectangle;

import org.eclipse.draw2d.IFigure;

/**
 * This interface describes all Zest components that are Containers. This is an
 * internal interface and thus should not be used outside of Zest. Implementors
 * of this interface must include the following two methods o addNode(GraphNode)
 * o addNode(GraphContainer)
 *
 * These are not actually listed here because Java does not allow protected
 * methods in interfaces.
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @author Ian Bull
 */
public interface IContainer {

	public Graph getGraph();

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 * @param graphNode
	 */
	public void addNode(GraphNode graphNode);

	// All implementers must include this method
	/* protected void addNode(GraphContainer container); */

	public int getItemType();

	/**
	 * Re-applies the current layout algorithm
	 */
	public void applyLayout();

	/**
	 * Sets the LayoutAlgorithm for this container and optionally applies it.
	 *
	 * @param algorithm   The layout algorithm to set
	 * @param applyLayout
	 */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm, boolean applyLayout);

	public List getNodes();

	/* protected void highlightNode(GraphNode node); */

	/* protected void highlightNode(GraphContainer container); */

	/* protected void unhighlightNode(GraphNode node); */

	/* protected void unhighlightNode(GraphContainer container); */

	/**
	 * Returns list of connections laying inside this container. Only connections
	 * which both source and target nodes lay directly in this container are
	 * returned.
	 *
	 * @return
	 * @since 2.0
	 */
	public abstract List getConnections();

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 * @param figure
	 */
	public abstract void addSubgraphFigure(IFigure figure);

	/**
	 * @return
	 * @since 2.0
	 */
	public abstract DisplayIndependentRectangle getLayoutBounds();

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 * @return
	 */
	public abstract InternalLayoutContext getLayoutContext();

	/**
	 * Takes a list of connections and returns only those which source and target
	 * nodes lay directly in this container.
	 *
	 * @param connections list of GraphConnection to filter
	 * @return filtered list
	 */
	// protected List filterConnections(List connections) {
	// List result = new ArrayList();
	// for (Iterator iterator = connections.iterator(); iterator.hasNext();) {
	// GraphConnection connection = (GraphConnection) iterator.next();
	// if (connection.getSource().getParent() == this &&
	// connection.getDestination().getParent() == this)
	// result.add(connection);
	// }
	// return result;
	// }

	/**
	 * @since 2.0
	 */
	public Widget getItem();
} // end of IContainer
