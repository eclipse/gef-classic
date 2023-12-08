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

package org.eclipse.gef.examples.text;

import org.eclipse.gef.Request;

/**
 * @since 3.1
 */
public class SelectionRangeRequest extends Request {

	private final SelectionRange range;

	/**
	 * @since 3.1
	 * @param type
	 */
	public SelectionRangeRequest(Object type, SelectionRange range) {
		super(type);
		this.range = range;
	}

	/**
	 * @return Returns the range.
	 */
	public SelectionRange getSelectionRange() {
		return range;
	}

}
