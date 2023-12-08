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
package org.eclipse.zest.layouts.constraints;

/**
 * @author Ian Bull
 * @author Chris Bennett
 */
public class BasicEdgeConstraints implements LayoutConstraint {

	// These should all be accessed directly.
	public boolean isBiDirectional = false;
	public int weight = 1;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.zest.layouts.constraints.LayoutConstraint#clear()
	 */
	@Override
	public void clear() {
		this.isBiDirectional = false;
		this.weight = 1;
	}

}
