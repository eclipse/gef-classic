/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylyn.zest.layouts.exampleStructures;

import org.eclipse.mylyn.zest.layouts.Filter;

/**
 * A very simple example of a filter.  This filter never filters
 * any object.
 * 
 * @author Casey Best
 */
public class SimpleFilter implements Filter {

	/**
	 * Doesn't filter anything
	 */
	public boolean isObjectFiltered(Object object) {
		return false;
	}
}
