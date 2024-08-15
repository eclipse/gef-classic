/*******************************************************************************
 * Copyright (c) 2009-2010, 2024 Mateusz Matela and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Mateusz Matela - initial API and implementation
 *               Ian Bull
 ******************************************************************************/
package org.eclipse.zest.layouts.interfaces;

import org.eclipse.zest.layouts.LayoutAlgorithm;

/**
 * @since 2.0
 */
public interface GraphStructureListener {

	public class Stub implements GraphStructureListener {

		@Override
		public boolean nodeAdded(LayoutContext context, NodeLayout node) {
			return false;
		}

		@Override
		public boolean nodeRemoved(LayoutContext context, NodeLayout node) {
			return false;
		}

		@Override
		public boolean connectionAdded(LayoutContext context, ConnectionLayout connection) {
			return false;
		}

		@Override
		public boolean connectionRemoved(LayoutContext context, ConnectionLayout connection) {
			return false;
		}
	}

	/**
	 * This method is called whenever a node is added to a context. No separate
	 * events will be fired for eventual connections adjacent to the added node.
	 *
	 * If true is returned, it means that the receiving listener has intercepted
	 * this event. Intercepted events will not be passed to the rest of the
	 * listeners. If the event is not intercepted by any listener,
	 * {@link LayoutAlgorithm#applyLayout(boolean) applyLayout(boolean)} will be
	 * called on the context's main algorithm.
	 *
	 * @param context the layout context that fired the event
	 * @param node    the added node
	 * @return true if no further operations after this event are required
	 */
	public boolean nodeAdded(LayoutContext context, NodeLayout node);

	/**
	 * This method is called whenever a node is removed from a context. No separate
	 * events will be fired for eventual connections adjacent to the removed node.
	 *
	 * If true is returned, it means that the receiving listener has intercepted
	 * this event. Intercepted events will not be passed to the rest of the
	 * listeners. If the event is not intercepted by any listener,
	 * {@link LayoutAlgorithm#applyLayout(boolean) applyLayout(boolean)} will be
	 * called on the context's main algorithm.
	 *
	 * @param context the context that fired the event
	 * @param node    the removed node
	 * @return true if no further operations after this event are required
	 */
	public boolean nodeRemoved(LayoutContext context, NodeLayout node);

	/**
	 * This method is called whenever a connection is added to a context. It can be
	 * assumed that both source and target nodes of the added connection already
	 * exist in the context.
	 *
	 * This method will be called only if both nodes connected by added connection
	 * lay directly in the node container owned by the notifying layout context.
	 *
	 * If true is returned, it means that the receiving listener has intercepted
	 * this event. Intercepted events will not be passed to the rest of the
	 * listeners. If the event is not intercepted by any listener,
	 * {@link LayoutAlgorithm#applyLayout(boolean) applyLayout(boolean)} will be
	 * called on the context's main algorithm.
	 *
	 * @param context    the context that fired the event
	 * @param connection the added connection
	 * @return true if no further operations after this event are required
	 */
	public boolean connectionAdded(LayoutContext context, ConnectionLayout connection);

	/**
	 * This method is called whenever a connection is removed from a context. It can
	 * be assumed that both source and target nodes of the removed connection still
	 * exist in the context and will not be removed along with it.
	 *
	 * This method will be called only if both nodes connected by removed connection
	 * lay directly in the node container owned by the notifying layout context.
	 *
	 * If true is returned, it means that the receiving listener has intercepted
	 * this event. Intercepted events will not be passed to the rest of the
	 * listeners. If the event is not intercepted by any listener,
	 * {@link LayoutAlgorithm#applyLayout(boolean) applyLayout(boolean)} will be
	 * called on the context's main algorithm.
	 *
	 * @param context    the context that fired the event
	 * @param connection the added connection
	 * @return true if no further operations after this event are required
	 */
	public boolean connectionRemoved(LayoutContext context, ConnectionLayout connection);

}
