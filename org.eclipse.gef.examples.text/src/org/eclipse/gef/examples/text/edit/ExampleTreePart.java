/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public abstract class ExampleTreePart
	extends AbstractTreeEditPart
	implements PropertyChangeListener
{

public void activate() {
	super.activate();
	ModelElement model = (ModelElement)getModel();
	model.addPropertyChangeListener(this);
}

public void deactivate() {
	ModelElement model = (ModelElement)getModel();
	model.removePropertyChangeListener(this);
	super.deactivate();
}

}
