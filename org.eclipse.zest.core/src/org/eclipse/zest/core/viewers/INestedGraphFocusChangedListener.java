/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.viewers;

/**
 * A listener for when focus (root-level) nodes change in a nested graph viewer.
 * @author Del Myers
 *
 */
//@tag bug(151889-ViewCoupling)
public interface INestedGraphFocusChangedListener {

	/**
	 * Signals that focus (root-level) nodes change in a nested graph viewer.
	 * If either of the nodes is set to null, the client can assume the top-level
	 * of the nested graph.
	 * @param oldNode the old node of focus.
	 * @param newNode the new node of focus.
	 */
	public void focusChanged(Object oldNode, Object newNode);
}
