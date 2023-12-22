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

package org.eclipse.gef.examples.text.edit;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import org.eclipse.gef.examples.text.model.ModelElement;

/**
 * @since 3.1
 */
public abstract class ExampleTreePart extends AbstractTreeEditPart implements PropertyChangeListener {

	@Override
	public void activate() {
		super.activate();
		ModelElement model = (ModelElement) getModel();
		model.addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		ModelElement model = (ModelElement) getModel();
		model.removePropertyChangeListener(this);
		super.deactivate();
	}

}
