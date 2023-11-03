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
package org.eclipse.draw2d;

/**
 * Classes which implement this interface provide a method to notify that an
 * anchor has moved.
 * <P>
 * Instances of this class can be added as listeners of an Anchor using the
 * <code>addAnchorListener</code> method and removed using the
 * <code>removeAnchorListener</code> method.
 */
public interface AnchorListener {

	/**
	 * Called when an anchor has moved to a new location.
	 *
	 * @param anchor The anchor that has moved.
	 */
	void anchorMoved(ConnectionAnchor anchor);

}
