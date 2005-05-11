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

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.CaretInfo;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.InlineFlow;

import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.figures.CommentPage;
import org.eclipse.gef.examples.text.model.Container;

/**
 * @since 3.1
 */
public abstract class CompoundTextualPart 
	extends AbstractTextualPart 
{

public CompoundTextualPart(Object model) {
	setModel(model);
}

public boolean acceptsCaret() {
	for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
		Object part = iter.next();
		if (part instanceof TextualEditPart && ((TextualEditPart)part).acceptsCaret())
			return true;
	}
	return false;
}

public void activate() {
	super.activate();
	getContainer().getStyle().addPropertyChangeListener(this);
}

protected void createEditPolicies() {}

protected IFigure createFigure() {
	Figure figure = null;
	switch (getContainer().getType()) {
		case Container.TYPE_INLINE:
			figure = new InlineFlow();
			break;
		case Container.TYPE_COMMENT:
			figure = new CommentPage();
			break;
		case Container.TYPE_PARAGRAPH:
			figure = new BlockFlow();
			figure.setBorder(new MarginBorder(4, 2, 4, 0));
			break;
		case Container.TYPE_ROOT:
			figure = new FlowPage();
			figure.setBorder(new MarginBorder(4));
			break;
		default:
			System.out.println("unexpected");
	}
	return figure;
}

public void deactivate() {
	getContainer().getStyle().removePropertyChangeListener(this);
	super.deactivate();
}

public CaretInfo getCaretPlacement(int offset, boolean trailing) {
	throw new RuntimeException("This part cannot place the caret");
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

public TextLocation getLocation(Point absolute, int[] trailing) {
	TextLocation result = null;
	int dx = Integer.MAX_VALUE, dy = Integer.MAX_VALUE;
	int[] isAfter = new int[1];
	for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
		Object part = iter.next();
		if (part instanceof TextualEditPart) {
			TextLocation location = ((TextualEditPart)part).getLocation(absolute, isAfter);
			if (location != null) {
				CaretInfo caretInfo = location.part
						.getCaretPlacement(location.offset, isAfter[0] == 1);
				int yDistance = vDistanceBetween(caretInfo, absolute.y);
				int xDistance = Math.abs(caretInfo.getX() - absolute.x);
				if (yDistance < dy || (yDistance == dy && xDistance < dx) ) {
					result = location;
					trailing[0] = isAfter[0];
					dx = xDistance;
					dy = yDistance;
				}
			}
		}
	}
	return result;
}

protected List getModelChildren() {
	return getContainer().getChildren();
}

/**
 * @see TextualEditPart#getNextLocation(int, TextLocation)
 */
public TextLocation getNextLocation(CaretSearch search) {
	switch (search.type) {
		case CaretSearch.LINE_BOUNDARY:
			if (search.isForward)
				return searchLineEnd(search);
			return searchLineBegin(search);

		case CaretSearch.ROW:
			if (search.isForward)
				return searchLineBelow(search);
			return searchLineAbove(search);

		case CaretSearch.WORD_BOUNDARY:
		case CaretSearch.COLUMN:
			if (search.isForward)
				return searchForward(search);
			return searchBackwards(search);

		default:
			break;
	}

	if (getParent() instanceof TextualEditPart)
		return getTextParent().getNextLocation(search);
	return null;
}

public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("children"))
		refreshChildren();
}

TextLocation searchBackwards(CaretSearch search) {
	TextLocation location = search.where;
	int childIndex = (location == null) ? getChildren().size() - 1
			: getChildren().indexOf(location.part) - 1;
	TextualEditPart part;
	while (childIndex >= 0) {
		part = (TextualEditPart)getChildren().get(childIndex--);
		location = part.getNextLocation(search.recurseSearch());
		if (location != null)
			return location;
	}
	if (search.isRecursive)
		return null;
	if (getParent() instanceof TextualEditPart)
		return getTextParent().getNextLocation(search.continueSearch(this, 0));
	return null;
}

TextLocation searchForward(CaretSearch search) {
	TextLocation location = search.where;
	
	int childIndex = (location == null) ? 0
			: getChildren().indexOf(location.part) + 1;
	int childCount = getChildren().size();
	TextualEditPart part;
	CaretSearch recurse = search.recurseSearch();
	while (childIndex < childCount) {
		part = (TextualEditPart)getChildren().get(childIndex++);
		location = part.getNextLocation(recurse);
		if (location != null)
			return location;
	}
	if (search.isRecursive)
		return null;
	if (this instanceof BlockTextualPart)
		search.isInto = true;
	if (getParent() instanceof TextualEditPart)
		return getTextParent().getNextLocation(
				search.continueSearch(this, getLength()));
	return null;
}

protected TextLocation searchLineAbove(CaretSearch search) {
	//The bottom of this figure must be above the top of the caret
	//if (getFigure().getBounds().bottom() > caret.y)
	//	return null;
	
	int childIndex;
	TextualEditPart part;
	TextLocation location = search.where;
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
		location = part.getNextLocation(search.recurseSearch());
		if (location != null) {
			CaretInfo caretInfo = location.part.getCaretPlacement(location.offset, false);
			if (lineBounds == null)
				lineBounds = new Rectangle(caretInfo.getX(), caretInfo.top(), 0, caretInfo.getHeight());
			else if (lineBounds.y > caretInfo.getBaseline())
				break;
			else
				lineBounds.union(caretInfo.getX(), caretInfo.top(), 0, caretInfo.getHeight());
			
			int distance = Math.abs(caretInfo.getX() - search.x);
			if (distance < dx) {
				result = location;
				dx = distance;
			}
		}
		childIndex--;
	}
	return result;
}

protected TextLocation searchLineBegin(CaretSearch search) {
	int childIndex = 0;
	int childCount = getChildren().size();
	TextualEditPart newPart;
	TextLocation result;
	while (childIndex < childCount) {
		newPart = (TextualEditPart)getChildren().get(childIndex);
		result = newPart.getNextLocation(search.recurseSearch());
		if (result != null)
			return result;
		childIndex++;
	}
	return null;
}

protected TextLocation searchLineBelow(CaretSearch search) {
	//The top of this figure must be below the bottom of the caret
//	if (getFigure().getBounds().y < caret.bottom())
		//return null;

	TextLocation location = search.where;
	
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
	search = search.recurseSearch();
	while (childIndex < childCount) {
		part = (TextualEditPart)getChildren().get(childIndex);
		location = part.getNextLocation(search);
		if (location != null) {
			//$TODO need to set trailing on getNextLocation
			CaretInfo caretInfo = location.part.getCaretPlacement(location.offset, false);
			if (lineBounds == null)
				lineBounds = new Rectangle(caretInfo.getX(), caretInfo.top(), 0, caretInfo.getHeight());
			else if (lineBounds.bottom() < caretInfo.getBaseline())
				break;
			else
				lineBounds.union(caretInfo.getX(), caretInfo.top(), 0, caretInfo.getHeight());
			
			int distance = Math.abs(caretInfo.getX() - search.x);
			if (distance < dx) {
				result = location;
				dx = distance;
			}
		}
		childIndex++;
	}
	return result;
}

protected TextLocation searchLineEnd(CaretSearch search) {
	int childIndex = getChildren().size() - 1;
	TextualEditPart child;
	TextLocation result;
	while (childIndex >= 0) {
		child = (TextualEditPart)getChildren().get(childIndex);
		result = child.getNextLocation(search.recurseSearch());
		if (result != null)
			return result;
		childIndex--;
	}
	return null;
}

private int vDistanceBetween(CaretInfo info, int y) {
	if (y < info.lineTop())
		return info.lineTop() - y;
	return Math.max(0, y - (info.lineTop() + info.getLineHeight()));
}

}
