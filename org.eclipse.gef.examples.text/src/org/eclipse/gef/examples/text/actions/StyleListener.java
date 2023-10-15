/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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

package org.eclipse.gef.examples.text.actions;

/**
 * @since 3.1
 */
public interface StyleListener {

	/**
	 * @param styleID can be <code>null</code>; null means all StyleIDs need to be
	 *                updated
	 */
	void styleChanged(String styleID);

}
