/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.text.FlowFigure;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.model.ModelElement;
import org.eclipse.gef.examples.text.tools.SelectionRangeDragTracker;

/**
 * @since 3.1
 */
public abstract class AbstractTextualPart extends AbstractGraphicalEditPart
		implements TextualEditPart, PropertyChangeListener {

public boolean acceptsCaret() {
	return true;
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

public DragTracker getDragTracker(Request request) {
	return new SelectionRangeDragTracker(this);
}

public TextLocation getLocation(Point absolute, int trailing[]) {
	return null;
}

protected TextualEditPart getTextParent() {
	return (TextualEditPart)getParent();
}

public void setSelection(int start, int end) {
	FlowFigure ff = (FlowFigure)getFigure();
	ff.setSelection(start, end);
}

}
