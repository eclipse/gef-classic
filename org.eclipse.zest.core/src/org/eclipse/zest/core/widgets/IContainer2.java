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

import org.eclipse.swt.widgets.Widget;

import org.eclipse.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.zest.layouts.interfaces.LayoutContext;

import org.eclipse.draw2d.IFigure;

/**
 * Extend the IConnectionStyleProvider interface to provide additional methods
 * introduced by Zest 2.x.
 *
 * WARNING: THIS API IS UNDER CONSTRUCTION AND SHOULD NOT BE USED
 *
 * @author Ian Bull
 * @noextend This interface is not intended to be extended by clients.
 * @noreference This interface is not intended to be referenced by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 * @since 1.12
 */
// TODO Zest 2.x - Integrate into IContainer
public interface IContainer2 extends IContainer {

	public Widget getItem();

	/**
	 * Returns list of connections laying inside this container. Only connections
	 * which both source and target nodes lay directly in this container are
	 * returned.
	 */
	public List<? extends GraphConnection> getConnections();

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 * @param graphNode
	 */
	public void addNode(GraphNode graphNode);

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 * @param figure
	 */
	public void addSubgraphFigure(IFigure figure);

	public DisplayIndependentRectangle getLayoutBounds();

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public LayoutContext getLayoutContext();
}
