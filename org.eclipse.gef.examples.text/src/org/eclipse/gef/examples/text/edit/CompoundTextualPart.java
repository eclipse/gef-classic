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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.InlineFlow;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.figures.CommentPage;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.Style;

/**
 * @since 3.1
 */
public abstract class CompoundTextualPart extends AbstractTextualPart {

public CompoundTextualPart(Object model) {
	setModel(model);
}

protected void createEditPolicies() {}

/**
 * @see AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	Figure figure;
	switch (getContainer().getType()) {
		case Container.TYPE_INLINE:
			figure = new InlineFlow();
			break;
		case Container.TYPE_COMMENT:
			figure = new CommentPage();
			break;
		case Container.TYPE_PARAGRAPH:
			figure = new FlowPage();
			break;
		default:
			figure = new Figure();
			figure.setLayoutManager(new ToolbarLayout(ToolbarLayout.VERTICAL));
			figure.setBorder(new LineBorder(ColorConstants.lightGray, 4));
	}
	return figure;
}

/**
 * @see TextualEditPart#getCaretPlacement(int)
 */
public Rectangle getCaretPlacement(int offset) {
	throw new RuntimeException("not supported");
}

protected Container getContainer() {
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
 * @see TextualEditPart#getNextLocation(int, TextLocation)
 */
public TextLocation getNextLocation(int movement, TextLocation current, Rectangle caret) {
	switch (movement) {
		case LINE_START_QUERY:
			return searchLineBegin(caret);

		case LINE_END_QUERY:
			return searchLineEnd(caret);

		case LINE_DOWN_INTO:
			return searchLineBelow(current, caret);

		case LINE_UP_INTO:
			return searchLineAbove(current, caret);
			
		case COLUMN_NEXT:
			return searchForward(current, caret, false);
		case COLUMN_NEXT_INTO:
			return searchForward(current, caret, true);
		
		case COLUMN_PREVIOUS:
			return searchBackwards(current, caret,  false);
		case COLUMN_PREVIOUS_INTO:
			return searchBackwards(current, caret, true);

		default:
			break;
	}

	if (getParent() instanceof TextualEditPart)
		return getTextParent().getNextLocation(movement, current, caret);
	return null;
}

public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("children"))
		refreshChildren();
}

protected void refreshVisuals() {
	Style style = getContainer().getStyle();
	FontData basis = getFigure().getParent().getFont().getFontData()[0];
	if (style.getFontHeight() > 0)
		basis.setHeight(style.getFontHeight());
	if (style.getFontFamily() != null)
		basis.setName(style.getFontFamily());
	basis.setStyle((style.isBold() ? SWT.BOLD : 0) | (style.isItalic() ? SWT.ITALIC : 0));
	getFigure().setFont(new Font(null, basis));
}

TextLocation searchBackwards(TextLocation current, Rectangle caret, boolean into) {
	int childIndex = (current == null) ? getChildren().size() - 1
			: getChildren().indexOf(current.part) - 1;
	TextualEditPart part;
	while (childIndex >= 0) {
		part = (TextualEditPart)getChildren().get(childIndex--);
		return part.getNextLocation(COLUMN_PREVIOUS_INTO, null, caret);
	}
	if (into)
		return null;
	current = new TextLocation(this, childIndex);
	if (getParent() instanceof TextualEditPart)
		return getTextParent().getNextLocation(COLUMN_PREVIOUS, current, caret);
	return null;
}

TextLocation searchForward(TextLocation current, Rectangle caret, boolean into) {
	int childIndex = (current == null) ? 0
			: getChildren().indexOf(current.part) + 1;
	int childCount = getChildren().size();
	TextualEditPart part;
	while (childIndex < childCount) {
		part = (TextualEditPart)getChildren().get(childIndex++);
		return part.getNextLocation(COLUMN_NEXT_INTO, null,
				caret);
	}
	if (into)
		return null;
	current = new TextLocation(this, getChildren().size());
	if (getParent() instanceof TextualEditPart)
		return getTextParent().getNextLocation(COLUMN_NEXT, current, caret);
	return null;
}

protected TextLocation searchLineBegin(Rectangle caret) {
	int childIndex = 0;
	int childCount = getChildren().size();
	TextualEditPart newPart;
	TextLocation result;
	while (childIndex < childCount) {
		newPart = (TextualEditPart)getChildren().get(childIndex);
		result = newPart.getNextLocation(LINE_START_QUERY, null, caret);
		if (result != null)
			return result;
		childIndex++;
	}
	return null;
}

protected TextLocation searchLineEnd(Rectangle caret) {
	int childIndex = getChildren().size() - 1;
	TextualEditPart newPart;
	TextLocation result;
	while (childIndex >= 0) {
		newPart = (TextualEditPart)getChildren().get(childIndex);
		result = newPart.getNextLocation(LINE_END_QUERY, null, caret);
		if (result != null)
			return result;
		childIndex--;
	}
	return null;
}

protected TextLocation searchLineBelow(TextLocation location, Rectangle caret) {
	//The top of this figure must be below the bottom of the caret
//	if (getFigure().getBounds().y < caret.bottom())
		//return null;

	int childIndex;
	int childCount = getChildren().size();
	TextualEditPart part;
	if (location == null)
		childIndex = 0;
	else {
		childIndex = getChildren().indexOf(location.part);
		if (location.offset == location.part.getLength())
			childIndex++;
	}
	
	TextLocation result = null;
	int dx = Integer.MAX_VALUE;
	Rectangle lineBounds = null;
	while (childIndex < childCount) {
		part = (TextualEditPart)getChildren().get(childIndex);
		location = part.getNextLocation(LINE_DOWN_INTO, null, caret);
		if (location != null) {
			Rectangle newPlacement = location.part.getCaretPlacement(location.offset);
			if (lineBounds == null)
				lineBounds = new Rectangle(newPlacement);
			else if (lineBounds.y > newPlacement.bottom())
				break;
			else
				lineBounds.union(newPlacement);
			
			int distance = Math.abs(newPlacement.x - caret.x);
			if (distance < dx) {
				result = location;
				dx = distance;
			}
		}
		childIndex++;
	}
	return result;
}

protected TextLocation searchLineAbove(TextLocation location, Rectangle caret) {
	//The bottom of this figure must be above the top of the caret
	//if (getFigure().getBounds().bottom() > caret.y)
	//	return null;
	
	int childIndex;
	TextualEditPart part;
	if (location == null)
		childIndex = getChildren().size() - 1;
	else {
		childIndex = getChildren().indexOf(location.part);
		if (location.offset == 0)
			childIndex--;
	}
	
	TextLocation result = null;
	int dx = Integer.MAX_VALUE;
	Rectangle lineBounds = null;
	while (childIndex >= 0) {
		part = (TextualEditPart)getChildren().get(childIndex);
		location = part.getNextLocation(LINE_UP_INTO, null, caret);
		if (location != null) {
			Rectangle newPlacement = part.getCaretPlacement(location.offset);
			if (lineBounds == null)
				lineBounds = new Rectangle(newPlacement);
			else if (lineBounds.y > newPlacement.bottom())
				break;
			else
				lineBounds.union(newPlacement);
			
			int distance = Math.abs(newPlacement.x - caret.x);
			if (distance < dx) {
				result = location;
				dx = distance;
			}
		}
		childIndex--;
	}
	return result;
}

/**
 * Selection is not rendered
 */
public void setSelection(int start, int end) {}

}