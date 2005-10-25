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
import java.text.BreakIterator;

import org.eclipse.swt.graphics.Font;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.text.CaretInfo;
import org.eclipse.draw2d.text.SimpleTextLayout;
import org.eclipse.draw2d.text.TextFlow;

import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.model.Style;
import org.eclipse.gef.examples.text.model.TextRun;

/**
 * @since 3.1
 */
public class TextFlowPart 
	extends AbstractTextualPart 
{
	
private Font localFont;

public TextFlowPart(Object model) {
	setModel(model);
}

protected void createEditPolicies() {}

protected IFigure createFigure() {
	TextFlow flow = new TextFlow();
	if (((TextRun)getModel()).getType() == TextRun.TYPE_CODE)
		flow.setLayoutManager(new SimpleTextLayout(flow));
	return flow;
}

public void deactivate() {
	super.deactivate();
	if (localFont != null)
		FontCache.checkIn(localFont);
}

public CaretInfo getCaretPlacement(int offset, boolean trailing) {
	Assert.isTrue(offset <= getLength());

	return getTextFlow().getCaretPlacement(offset, trailing);
}

public int getLength() {
	return getTextFlow().getText().length();
}

public TextLocation getLocation(Point absolute, int trailing[]) {
	Point pt = absolute.getCopy();
	getTextFlow().translateToRelative(pt);
	int offset = getTextFlow().getOffset(pt, trailing, null);
	if (offset == -1)
		offset = getTextFlow().getText().length();
	return new TextLocation(this, offset);
}

public TextLocation getNextLocation(CaretSearch search) {
	Point where = new Point();
	int offset;
	switch (search.type) {
		case CaretSearch.LINE_BOUNDARY:
			if (!search.isRecursive)
				break;
			where.y = search.baseline;
			getTextFlow().translateToRelative(where);
			if (search.isForward)
				offset = getTextFlow().getLastOffsetForLine(where.y) + 1;
			else
				offset = getTextFlow().getFirstOffsetForLine(where.y);
			if (offset == -1)
				return null;
			return new TextLocation(this, offset);

		case CaretSearch.COLUMN:
			TextFlow flow = getTextFlow();
			if (search.isRecursive) {
				if (search.isInto) {
					if (search.isForward)
						return new TextLocation(this, flow.getNextVisibleOffset(-1));
					else
						return new TextLocation(this, flow.getPreviousVisibleOffset(-1));
				} else {
					if (getLength() > 0)
						if (search.isForward)
							return new TextLocation(this, flow.getNextVisibleOffset(0));
						else
							return new TextLocation(this, flow
									.getPreviousVisibleOffset(flow
											.getPreviousVisibleOffset(-1)));
					//In the rare case that this is an empty element, skip it.
					return null;
				}
			}

			if (search.isForward && search.where.offset < getLength())
				return new TextLocation(this, flow.getNextVisibleOffset(search.where.offset));
			if (!search.isForward && search.where.offset > 0)
				return new TextLocation(this, flow.getPreviousVisibleOffset(search.where.offset));
			break;

		case CaretSearch.ROW:
			if (!search.isRecursive)
				break;
			where.x = search.x;
			where.y = search.baseline;
			getTextFlow().translateToRelative(where);

			int[] trailing = new int[1];
			offset = getTextFlow().getNextOffset(where, search.isForward, trailing);
			if (offset == -1)
				return null;
			return new TextLocation(this, offset + trailing[0]);
		case CaretSearch.WORD_BOUNDARY:
			String text = getTextFlow().getText();
			int length = text.length();
			int referenceOffset = (search.where == null) ? 0 : search.where.offset; 
			if (referenceOffset <= length) {
				BreakIterator iter = BreakIterator.getWordInstance();
				iter.setText(text);
				if (search.isForward) {
					if (referenceOffset < length)
						offset = iter.following(referenceOffset);
					else
						offset = BreakIterator.DONE;
				} else {
					if (referenceOffset == text.length()) {
						iter.last();
						offset = iter.previous();
					} else
						offset = iter.preceding(referenceOffset);
				}
				if (offset != BreakIterator.DONE)
					return new TextLocation(this, offset);
			}
			if (search.isRecursive)
				return null;
			return getTextParent().getNextLocation(search);
	}
	return ((TextualEditPart)getParent()).getNextLocation(search);
}

TextFlow getTextFlow() {
	return (TextFlow)getFigure();
}

public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("text"))
		refreshVisuals();
}

protected void refreshVisuals() {
	TextRun textRun = (TextRun)getModel();
	Style style = textRun.getContainer().getStyle();
	Font font = FontCache.checkOut(style.getFontFamily(), style.getFontHeight(), 
			style.isBold(), style.isItalic());
	if (font != localFont) {
		if (localFont != null)
			FontCache.checkIn(localFont);
		localFont = font;
		getFigure().setFont(font);
	} else
		FontCache.checkIn(font);
	getTextFlow().setText(textRun.getText());
}

public void setSelection(int start, int end) {
	if (start == end)
		getTextFlow().setSelection(-1, -1);
	else
		getTextFlow().setSelection(start, end);
}

}