/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
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

import org.eclipse.draw2d.text.FlowFigure;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.examples.text.model.ModelElement;
import org.eclipse.gef.examples.text.tools.SelectionRangeDragTracker;

/**
 * @since 3.1
 */
public abstract class AbstractTextPart extends AbstractGraphicalEditPart
		implements TextEditPart, PropertyChangeListener {

	@Override
	public boolean acceptsCaret() {
		return true;
	}

	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		getModel().addPropertyChangeListener(this);
	}

	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		getModel().removePropertyChangeListener(this);
		super.deactivate();
	}

	@Override
	public DragTracker getDragTracker(Request request) {
		return new SelectionRangeDragTracker(this);
	}

	@Override
	public ModelElement getModel() {
		return (ModelElement) super.getModel();
	}

	protected TextEditPart getTextParent() {
		return (TextEditPart) getParent();
	}

	@Override
	public void setSelection(int start, int end) {
		FlowFigure ff = (FlowFigure) getFigure();
		ff.setSelection(start, end);
	}

}
