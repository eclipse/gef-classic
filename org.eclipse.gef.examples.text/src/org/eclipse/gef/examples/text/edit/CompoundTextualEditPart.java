/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.edit;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.swt.SWT;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.model.Container;

/**
 * @since 3.1
 */
public class CompoundTextualEditPart extends AbstractTextualEditPart {

protected void createEditPolicies() {}

public CompoundTextualEditPart(Object model) {
	setModel(model);
}

/**
 * @see AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	Figure f = new Figure();
	f.setBorder(new LineBorder(ColorConstants.lightGray, 4));
	f.setLayoutManager(new ToolbarLayout(ToolbarLayout.VERTICAL));
	return f;
}

/**
 * @see TextualEditPart#getCaretPlacement(int)
 */
public Rectangle getCaretPlacement(int offset) {
	throw new RuntimeException("not supported");
}

private Container getContainer() {
	return (Container)getModel();
}

/**
 * @see TextualEditPart#getLength()
 */
public int getLength() {
	return getChildren().size();
}

protected List getModelChildren() {
	return getContainer().getChildren();
}

/**
 * @see TextualEditPart#getNextLocation(int, org.eclipse.gef.examples.text.TextLocation)
 */
public TextLocation getNextLocation(int identifier, TextLocation current) {
	int childIndex = getChildren().indexOf(current.part);
	int childCount = getChildren().size();
	switch (identifier) {
		case SWT.ARROW_RIGHT:
			if (childIndex == childCount - 1) {
				//$TODO do nothing for now, but need to create another location and pass
				// to parent
				return current;
			}
			TextualEditPart newPart = (TextualEditPart)getChildren().get(childIndex + 1);
			return new TextLocation(newPart, 0);
		case SWT.ARROW_LEFT:
			if (childIndex == 0) {
				//$TODO do nothing for now, but need to create another location and pass
				// to parent
				return current;
			}
			newPart = (TextualEditPart)getChildren().get(childIndex - 1);
			return new TextLocation(newPart, newPart.getLength());
		default:
			break;
	}
	return null;
}

public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("children"))
		refreshChildren();
}

/**
 * Selection is not rendered
 */
public void setSelection(int start, int end) {}

}