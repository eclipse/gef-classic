/*******************************************************************************
 * Copyright 2005 CHISEL Group, University of Victoria, Victoria, BC,
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
package org.eclipse.zest.layouts.exampleStructures;

import org.eclipse.zest.layouts.Filter;
import org.eclipse.zest.layouts.LayoutItem;

/**
 * A very simple example of a filter. This filter never filters any object.
 *
 * @author Casey Best
 */
public class SimpleFilter implements Filter {

	/**
	 * Doesn't filter anything
	 */
	@Override
	public boolean isObjectFiltered(LayoutItem object) {
		return false;
	}
}
