/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.requests;

/**
 * A request that requires a location in order to drop an item.
 */
public interface DropRequest {

	/**
	 * Returns the current mouse location.
	 *
	 * @return the mouse location
	 */
	org.eclipse.draw2d.geometry.Point getLocation();

}
