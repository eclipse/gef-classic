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
public interface ContextListener {
	public class Stub implements ContextListener {

		@Override
		public boolean boundsChanged(LayoutContext context) {
			return false;
		}

		@Override
		public void backgroundEnableChanged(LayoutContext context) {
			// do nothing
		}

		@Override
		public boolean pruningEnablementChanged(LayoutContext context) {
			return false;
		}

	}

	/**
	 * This method is called whenever the bounds available in a layout context
	 * change.
	 *
	 * If true is returned, it means that the receiving listener has intercepted
	 * this event. Intercepted events will not be passed to the rest of the
	 * listeners. If the event is not intercepted by any listener,
	 * {@link LayoutAlgorithm#applyLayout(boolean) applyLayout(boolean)} will be
	 * called on the context's main algorithm.
	 *
	 * @param context the layout context that fired the event
	 * @return true if no further operations after this event are required
	 */
	public boolean boundsChanged(LayoutContext context);

	/**
	 * This method is called whenever graph pruning is enabled or disabled in a
	 * layout context.
	 *
	 * If true is returned, it means that the receiving listener has intercepted
	 * this event. Intercepted events will not be passed to the rest of the
	 * listeners. If the event is not intercepted by any listener,
	 * {@link LayoutAlgorithm#applyLayout(boolean) applyLayout(boolean)} will be
	 * called on the context's main algorithm.
	 *
	 * @param context the layout context that fired the event
	 * @return true if no further operations after this event are required
	 */
	public boolean pruningEnablementChanged(LayoutContext context);

	/**
	 * This method is called whenever background layout is enabled or disabled in a
	 * layout context. If the receiving listener is related to a layout algorithm
	 * that performs layout in reaction to events, it should turn automatic flush of
	 * changes on or off. Also, eventual additional threads responsible for layout
	 * should be stopped or started accordingly.
	 *
	 * @param context the layout context that fired the event
	 */
	public void backgroundEnableChanged(LayoutContext context);
}
