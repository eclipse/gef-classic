/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
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

package org.eclipse.gef.examples.text.model;

/**
 * @since 3.1
 */
public class Block extends Container {

	private static final long serialVersionUID = 1;

	/**
	 * @param type
	 * @since 3.1
	 */
	public Block(int type) {
		super(type);
	}

	/**
	 * @see org.eclipse.gef.examples.text.model.Container#newContainer()
	 */
	@Override
	Container newContainer() {
		return new Block(getType());
	}

}
