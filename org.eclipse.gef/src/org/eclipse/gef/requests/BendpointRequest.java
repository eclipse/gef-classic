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

import org.eclipse.gef.ConnectionEditPart;

/**
 * A request to alter a bendpoint.
 */
public class BendpointRequest extends org.eclipse.gef.requests.LocationRequest {

	private int index;
	private ConnectionEditPart source;

	/**
	 * Returns the index of the bendpoint to be changed.
	 *
	 * @return the bendpoint index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Returns the ConnectionEditPart that the bendpoint belongs to.
	 *
	 * @return the bendpoint's ConnectionEditPart
	 */
	public ConnectionEditPart getSource() {
		return source;
	}

	/**
	 * Sets the index of the bendpoint to be changed.
	 *
	 * @param i the bendpoint's index
	 */
	public void setIndex(int i) {
		index = i;
	}

	/**
	 * Sets the ConnectionEditPart the bendpoint belongs to.
	 *
	 * @param s the bendpoint's ConnectionEditPart
	 */
	public void setSource(ConnectionEditPart s) {
		source = s;
	}

}
