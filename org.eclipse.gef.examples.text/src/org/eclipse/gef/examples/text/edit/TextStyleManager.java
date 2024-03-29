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

package org.eclipse.gef.examples.text.edit;

import org.eclipse.gef.examples.text.SelectionRange;

/**
 * @since 3.1
 */
public interface TextStyleManager {

	/**
	 * Returns the effective value for the requested style and range. If the range
	 * does not have a consistent value for that style, then
	 * {@link org.eclipse.gef.examples.text.actions.StyleService#UNDEFINED} is
	 * returned.
	 *
	 * @param styleID
	 * @param range
	 * @return
	 * @since 3.1
	 */
	Object getStyleValue(String styleID, SelectionRange range);

	/**
	 * Returns the effective state for the requested style and range.
	 *
	 * @param styleID
	 * @param range
	 * @return
	 * @since 3.1
	 */
	Object getStyleState(String styleID, SelectionRange range);

}
