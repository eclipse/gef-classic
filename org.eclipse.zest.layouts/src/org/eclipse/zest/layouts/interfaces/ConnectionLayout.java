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

/**
 * @since 2.0
 */
public interface ConnectionLayout {

	public NodeLayout getSource();

	public NodeLayout getTarget();

	/**
	 *
	 * @return weight assigned to this connection
	 */
	public double getWeight();

	/**
	 * Checks if this connection is directed. For undirected connections, source and
	 * target nodes should be considered just adjacent nodes without dividing to
	 * source/target.
	 *
	 * @return true if this connection is directed
	 */
	public boolean isDirected();

	/**
	 * Changes the visibility state of this connection.
	 *
	 * @param visible true if this connection should be visible, false otherwise
	 */
	public void setVisible(boolean visible);

	/**
	 * Checks the visibility state of this connection.
	 *
	 * @return true if this connection is visible, false otherwise
	 */
	public boolean isVisible();
}
