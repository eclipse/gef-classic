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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.figures.TextLayoutFigure;
import org.eclipse.gef.examples.text.model.TextRun;
import org.eclipse.gef.examples.text.tools.SelectionRangeDragTracker;

/**
 * @since 3.1
 */
public class TextFlowPart extends AbstractTextualEditPart {

private TextLayout textLayout = new TextLayout(Display.getCurrent());

/**
 * @since 3.1
 */
public TextFlowPart(Object model) {
	setModel(model);
}

public boolean acceptsCaret() {
	return true;
}

protected void createEditPolicies() {}

/**
 * @see AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	textLayout.setText(((TextRun)getModel()).getText());
	Figure f = new TextLayoutFigure(textLayout);
	f.setBorder(new LineBorder(3));
	return f;
}

/**
 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
 */
public DragTracker getDragTracker(Request request) {
	return new SelectionRangeDragTracker(this);
}

/**
 * @see TextualEditPart#getCaretPlacement(int)
 */
public Rectangle getCaretPlacement(int offset) {
	Point pt = new Point(textLayout.getLocation(offset, false));
	pt.translate(getFigure().getClientArea().getLocation());
	Rectangle result = new Rectangle(pt.x, pt.y, 1, 12);
	getFigure().translateToAbsolute(result);
	return result;
}

/**
 * @see TextualEditPart#getLength()
 */
public int getLength() {
	return textLayout.getText().length();
}

/**
 * @see org.eclipse.gef.examples.text.edit.TextualEditPart#getNextLocation(int,
 *      org.eclipse.gef.examples.text.TextLocation)
 */
public TextLocation getNextLocation(int identifier, TextLocation current) {
	switch (identifier) {
		case SWT.ARROW_RIGHT:
			if (current.offset < getLength())
					return new TextLocation(current.part, current.offset + 1);
			break;
		case SWT.ARROW_LEFT:
			if (current.offset > 0)
					return new TextLocation(current.part, current.offset - 1);
	}
	return ((TextualEditPart)getParent()).getNextLocation(identifier, current);
}

public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("text")) refreshVisuals();
}

public TextLocation getLocation(Point absolute) {
	Point pt = Point.SINGLETON.setLocation(absolute);
	IFigure f = getFigure();
	f.translateToRelative(pt);
	pt.translate(f.getClientArea().getLocation().negate());
	int offset = textLayout.getOffset(pt.x, pt.y, new int[1]);
	return new TextLocation(this, offset);
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	textLayout.setText(((TextRun)getModel()).getText());
	getFigure().repaint();
	getFigure().revalidate();
}

/**
 * @see TextualEditPart#setSelection(int, int)
 */
public void setSelection(int start, int end) {
	((TextLayoutFigure)getFigure()).setSelectionRange(start, end - 1);
}

}