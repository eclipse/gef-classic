/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.gef.examples.text.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.TextFlow;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;

import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.model.TextRun;
import org.eclipse.gef.examples.text.tools.SelectionRangeDragTracker;

/**
 * @since 3.1
 */
public class TextFlowPart extends AbstractTextualPart {

public TextFlowPart(Object model) {
	setModel(model);
}

protected void createEditPolicies() {}

public boolean acceptsCaret() {
	return true;
}

protected IFigure createFigure() {
	TextFlow flow = new TextFlow();
	return flow;
}

public Rectangle getCaretPlacement(int offset) {
	Assert.isTrue(offset <= getLength());

	Rectangle result = getTextFlow().getCaretPlacement(offset);
	getFigure().translateToAbsolute(result);
	return result;
}

public DragTracker getDragTracker(Request request) {
	return new SelectionRangeDragTracker(this);
}

public int getLength() {
	return getTextFlow().getText().length();
}

public TextLocation getLocation(Point absolute) {
	Point pt = absolute.getCopy();
	getFigure().translateToRelative(pt);
	int offset = getTextFlow().getOffset(pt);
	if (offset == - 1)
		return null;
	return new TextLocation(this, offset);
}

public TextLocation getNextLocation(int movement, TextLocation current, Rectangle caret) {
	Point where = null;
	int offset;
	switch (movement) {
		case LINE_START_QUERY:
			where = caret.getCenter();
			getFigure().translateToRelative(where);
			offset = getTextFlow().getFirstOffsetForLine(where.y);
			if (offset == - 1)
				return null;
			return new TextLocation(this, offset);
		case LINE_END_QUERY:
			where = caret.getCenter(); 
			getFigure().translateToRelative(where);
			offset = getTextFlow().getLastOffsetForLine(where.y);
			if (offset == - 1)
				return null;
			return new TextLocation(this, offset);
		case COLUMN_PREVIOUS_INTO:
			return new TextLocation(this, getLength());
		
		case COLUMN_NEXT_INTO:
			return new TextLocation(this, 0);
		
		case COLUMN_NEXT:
			if (current.offset < getLength())
				return new TextLocation(this, current.offset + 1);
			break;
			
		case COLUMN_PREVIOUS:
			if (current.offset > 0)
				return new TextLocation(this, current.offset - 1);
			break;
		
		case LINE_DOWN_INTO:
			where = caret.getBottom();
		case LINE_UP_INTO:
			if (where == null)
				where = caret.getTop().translate(0, -1);
			getFigure().translateToRelative(where);
			//Rectangle area = getFigure().getClientArea();
			//where.translate(-area.x, -area.y);

			offset = getTextFlow().getNextOffset(where, movement == LINE_DOWN_INTO);
			if (offset == -1)
				return null;
			return new TextLocation(this, offset);
		
		case LINE_UP:
		case LINE_DOWN:
		case LINE_START:
		case LINE_END:
			//Allow ancestor block to perform LINE searches.
			return getTextParent().getNextLocation(movement, current, caret);
	}
	if (current == null)
		return null;
	return ((TextualEditPart)getParent())
		.getNextLocation(movement, current, caret);
}

TextFlow getTextFlow() {
	return (TextFlow)getFigure();
}

public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("text"))
		refreshVisuals();
}

protected void refreshVisuals() {
	getTextFlow().setText(((TextRun)getModel()).getText());
}

public void setSelection(int start, int end) {
	if (start == end)
		getTextFlow().setSelection(-1, -1);
	else
		getTextFlow().setSelection(start, end);
}

}
