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

package org.eclipse.gef.examples.text.model.commands;

import org.eclipse.gef.examples.text.model.ModelLocation;

/**
 * @since 3.1
 */
public abstract class MiniEdit {

	public abstract void apply();

	public abstract boolean canApply();

	public abstract ModelLocation getResultingLocation();

	public void reapply() {
		apply();
	}

	public abstract void rollback();

}
