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

package org.eclipse.gef.examples.text.model;

/**
 * @since 3.1
 */
public class ModelLocation {

	public final ModelElement model;

	public final int offset;

	public ModelLocation(ModelElement model, int offset) {
		this.model = model;
		this.offset = offset;
	}

}
