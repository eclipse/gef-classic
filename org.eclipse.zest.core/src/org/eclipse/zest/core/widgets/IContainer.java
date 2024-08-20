/*******************************************************************************
 * Copyright 2005-2010, 2024 CHISEL Group, University of Victoria, Victoria,
 *                           BC, Canada.
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

import org.eclipse.zest.layouts.LayoutAlgorithm;

/**
 * This interface describes all Zest components that are Containers. This is an
 * internal interface and thus should not be used outside of Zest. Implementors
 * of this interface must include the following two methods o addNode(GraphNode)
 * o addNode(GraphContainer)
 *
 * These are not actually listed here because Java does not allow protected
 * methods in interfaces.
 *
 * @author Ian Bull
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IContainer {

	public Graph getGraph();

	// All implementers must include this method
	/* protected void addNode(GraphNode node); */

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

	public List<? extends GraphNode> getNodes();

	/* protected void highlightNode(GraphNode node); */

	/* protected void highlightNode(GraphContainer container); */

	/* protected void unhighlightNode(GraphNode node); */

	/* protected void unhighlightNode(GraphContainer container); */

} // end of IContainer
