/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.edit;

import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.model.ModelElement;

/**
 * @since 3.1
 */
public abstract class AbstractTextualPart extends AbstractGraphicalEditPart
		implements TextualEditPart, PropertyChangeListener {

public boolean acceptsCaret() {
	return false;
}

/**
 * @see org.eclipse.gef.EditPart#activate()
 */
public void activate() {
	super.activate();
	ModelElement model = (ModelElement)getModel();
	model.addPropertyChangeListener(this);
}

/**
 * @see org.eclipse.gef.EditPart#deactivate()
 */
public void deactivate() {
	ModelElement model = (ModelElement)getModel();
	model.removePropertyChangeListener(this);
	super.deactivate();
}

public TextLocation getLocation(Point absolute, int trailing[]) {
	return null;
}

protected TextualEditPart getTextParent() {
	return (TextualEditPart)getParent();
}

}