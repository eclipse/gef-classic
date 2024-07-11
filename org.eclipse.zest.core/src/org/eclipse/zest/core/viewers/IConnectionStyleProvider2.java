/*******************************************************************************
 * Copyright 2005-2010, 2024 CHISEL Group, University of Victoria, Victoria, BC,
 *                      Canada.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.viewers;

import org.eclipse.draw2d.ConnectionRouter;

/**
 * Extend the IConnectionStyleProvider interface to provide additional methods
 * introduced by Zest 2.x.
 *
 * WARNING: THIS API IS UNDER CONSTRUCTION AND SHOULD NOT BE USED
 *
 * @author Del Myers
 * @see IGraphContentProvider
 * @see IEntityStyleProvider
 * @since 1.12
 * @noreference This interface is not intended to be referenced by clients.
 */
//@tag bug(151327-Styles) : created to solve this bug
//TODO Zest 2.x - Integrate into IConnectionStyleProvider
public interface IConnectionStyleProvider2 extends IConnectionStyleProvider {
	/**
	 * Returns the connection router of the single relation.
	 *
	 * @param rel the relationship represented by this connection.
	 * @return the connection router for rel. Null for default.
	 * @since 1.12
	 */
	ConnectionRouter getRouter(Object rel);
}
