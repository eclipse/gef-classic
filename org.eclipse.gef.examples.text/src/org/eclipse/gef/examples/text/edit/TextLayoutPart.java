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

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.CaretInfo;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.figures.BulletBorder;
import org.eclipse.gef.examples.text.figures.Images;
import org.eclipse.gef.examples.text.figures.ListItemBorder;
import org.eclipse.gef.examples.text.figures.TextLayoutFigure;
import org.eclipse.gef.examples.text.figures.TreeItemBorder;
import org.eclipse.gef.examples.text.model.Style;
import org.eclipse.gef.examples.text.model.TextRun;
import org.eclipse.gef.examples.text.tools.SelectionRangeDragTracker;

/**
 * @since 3.1
 */
public class TextLayoutPart 
	extends AbstractTextualPart 
{

private TextLayout textLayout = new TextLayout(Display.getCurrent());
private Font localFont;

/**
 * @since 3.1
 */
public TextLayoutPart(Object model) {
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
	TextRun run = (TextRun)getModel();
	textLayout.setText(run.getText());
	Figure f = new TextLayoutFigure(textLayout);

	switch (run.getType()) {
		case TextRun.TYPE_BULLET:
			f.setBorder(new BulletBorder());
			break;
		case TextRun.TYPE_IMPORT:
			f.setBorder(new TreeItemBorder(Images.IMPORT));
			break;
		default:
			f.setBorder(new LineBorder(ColorConstants.lightGray));
	}
	return f;
}

public void deactivate() {
	super.deactivate();
	if (localFont != null)
		FontCache.checkIn(localFont);
}

/**
 * @see TextualEditPart#getCaretPlacement(int)
 */
public CaretInfo getCaretPlacement(int offset, boolean trailing) {
	Point pt = new Point(textLayout.getLocation(offset, trailing));
	pt.translate(getFigure().getClientArea().getLocation());
	Rectangle result = new Rectangle(pt.x, pt.y, 1, 12);
	getFigure().translateToAbsolute(result);
	return new CaretInfo(result.x, result.y, 10, 2);
}

/**
 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
 */
public DragTracker getDragTracker(Request request) {
	return new SelectionRangeDragTracker(this);
}

/**
 * @see TextualEditPart#getLength()
 */
public int getLength() {
	return textLayout.getText().length();
}

public TextLocation getLocation(Point absolute, int trailing[]) {
	Point pt = Point.SINGLETON.setLocation(absolute);
	IFigure f = getFigure();
	f.translateToRelative(pt);
	pt.translate(f.getClientArea().getLocation().negate());
	
	int offset = textLayout.getOffset(pt.x, pt.y, trailing);
	return new TextLocation(this, offset + trailing[0]);
}

/**
 * @see TextualEditPart#getNextLocation(int, TextLocation)
 */
public TextLocation getNextLocation(CaretSearch search) {
	switch (search.type) {
		case CaretSearch.COLUMN:
			if (search.isForward) {
				if (search.isRecursive)
					return new TextLocation(this, 0);
				if (search.where.offset < getLength())
					return new TextLocation(this, search.where.offset + 1);
				else
					break;
			} else {
				if (search.isRecursive)
					return new TextLocation(this, getLength() - 1);
				if (search.where.offset > 0)
					return new TextLocation(this, search.where.offset - 1);
				else
					break;
			}
			
		case CaretSearch.ROW:
			int offset;
			if (search.where != null && search.where.part == this)
				offset = findNextLineOffset(search.where.offset, search.isForward);
			else {
				Point baseline = new Point(0, search.baseline); //was Top or Bottom of caret
				getFigure().translateToRelative(baseline);
				Rectangle clientArea = getFigure().getClientArea();
				baseline.translate(-clientArea.x, -clientArea.y);
				offset = findOffsetForPoint(baseline);
			}
			if (offset > - 1)
				return new TextLocation(this, offset);
			break;
	}
	return ((TextualEditPart)getParent()).getNextLocation(search);
}

/**
 * @since 3.1
 * @param offset
 * @param isDown
 * @return
 */
private int findNextLineOffset(int offset, boolean isDown) {
	int line = textLayout.getLineIndex(offset);
	Rectangle rect = new Rectangle(textLayout.getLineBounds(line));
	int currentX = textLayout.getLocation(offset, false).x;
	int trailing[] = new int[1];
	int newY;
	if (isDown)
		newY = rect.bottom() + 1;
	else newY = rect.y - 1;
	int result = textLayout.getOffset(currentX, newY, trailing);
	result += trailing[0];
	if (isDown && result > offset || !isDown && result < offset)
		return result;
	return - 1;
}

/**
 * @since 3.1
 * @param reference
 * @return
 */
private int findOffsetForPoint(Point reference) {
//	Rectangle r = new Rectangle(textLayout.getBounds());
//	if (!r.contains(reference))
//		return - 1;
	int trailing[] = new int[1];
	return textLayout.getOffset(reference.x, reference.y, trailing) + trailing[0];
}

public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("text"))
		refreshVisuals();
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	TextRun textRun = (TextRun)getModel();
	Style style = textRun.getContainer().getStyle();
	Font font = FontCache.checkOut(style.getFontFamily(), style.getFontHeight(), 
			style.isBold(), style.isItalic());
	if (font != localFont) {
		if (localFont != null)
			FontCache.checkIn(localFont);
		localFont = font;
		textLayout.setFont(font);
	} else
		FontCache.checkIn(font);
	textLayout.setText(textRun.getText());
	getFigure().repaint();
	getFigure().revalidate();
}

/**
 * @see TextualEditPart#setSelection(int, int)
 */
public void setSelection(int start, int end) {
	((TextLayoutFigure)getFigure()).setSelection(start, end - 1);
	Border border = getFigure().getBorder();
	if (border instanceof ListItemBorder) {
		ListItemBorder itemBorder = (ListItemBorder)border;
		itemBorder.setEnabled(getLength() > 0 || start >= 0);
	}
}

}