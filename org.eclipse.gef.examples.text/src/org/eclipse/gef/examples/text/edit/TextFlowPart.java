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
import com.ibm.icu.text.BreakIterator;

import org.eclipse.swt.graphics.Font;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.CaretInfo;
import org.eclipse.draw2d.text.SimpleTextLayout;
import org.eclipse.draw2d.text.TextFlow;

import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.model.Style;
import org.eclipse.gef.examples.text.model.TextRun;
import org.eclipse.gef.examples.text.requests.CaretRequest;
import org.eclipse.gef.examples.text.requests.SearchResult;

/**
 * @since 3.1
 */
public class TextFlowPart 
	extends AbstractTextPart 
{

private static BreakIterator wordIterator = null;
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
	if (trailing)
		if (offset > 0)
			offset--;
		else {
			// @TODO:Pratik this should never happen
			trailing = false;
			new RuntimeException("unexpected condition").printStackTrace();
		}
	return getTextFlow().getCaretPlacement(offset, trailing);
}

public int getLength() {
	return getTextFlow().getText().length();
}

TextFlow getTextFlow() {
	return (TextFlow)getFigure();
}

public void getTextLocation(CaretRequest search, SearchResult result) {
	if (search.getType() == CaretRequest.LINE_BOUNDARY)
		searchLineBoundary(search, result);
	else if (search.getType() == CaretRequest.COLUMN)
		searchColumn(search, result);
	else if (search.getType() == CaretRequest.ROW)
		searchRow(search, result);
	else if (search.getType() == CaretRequest.WORD_BOUNDARY)
		searchWordBoundary(search, result);
	else if (search.getType() == CaretRequest.LOCATION)
		searchLocation(search, result);		
	else if (getParent() instanceof TextEditPart)
		getTextParent().getTextLocation(search, result);
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

protected void searchColumn(CaretRequest search, SearchResult result) {
	TextFlow flow = getTextFlow();
	result.trailing = search.isForward;
	if (search.isRecursive || !(getParent() instanceof TextEditPart)) {
		if (search.isInto) {
			result.trailing = !search.isForward;
			if (search.isForward)
				result.location = 
						new TextLocation(this, flow.getNextVisibleOffset(-1));
			else
				result.location = 
						new TextLocation(this, flow.getPreviousVisibleOffset(-1));
		} else {
			if (getLength() > 0) {
				if (search.isForward)
					result.location = 
							new TextLocation(this, flow.getNextVisibleOffset(0));
				else
					result.location = new TextLocation(this, flow
							.getPreviousVisibleOffset(flow
									.getPreviousVisibleOffset(-1)));
			}
		}
	} else if (search.isForward && search.where.offset < getLength())
		result.location = new TextLocation(this, 
				flow.getNextVisibleOffset(search.where.offset));
	else if (!search.isForward && search.where.offset > 0)
		result.location = new TextLocation(this, 
				flow.getPreviousVisibleOffset(search.where.offset));
	else
		getTextParent().getTextLocation(search, result);
}

protected void searchLineBoundary(CaretRequest search, SearchResult result) {
	if (search.isRecursive || !(getParent() instanceof TextEditPart)) {
		Point where = search.getLocation().getCopy();
		TextFlow flow = getTextFlow();
		flow.translateToRelative(where);
		int offset;
		result.trailing = search.isForward;
		if (search.isForward) {
			offset = flow.getLastOffsetForLine(where.y);
			if (offset != -1)
				offset++;
		} else
			offset = flow.getFirstOffsetForLine(where.y);
		if (offset != -1)
			result.location = new TextLocation(this, offset);
	} else
		getTextParent().getTextLocation(search, result);
}

// also used for PGUP and PGDN
protected void searchLocation(CaretRequest search, SearchResult result) {
	Point pt = search.getLocation().getCopy();
	getTextFlow().translateToRelative(pt);
	
	// This detects the case where you've gone past page up or down
	if (result.location != null && vDistanceBetween(getTextFlow().getBounds(), pt.y) 
				> result.proximity.height) {
		result.bestMatchFound = true;
		return;
	}
	
	int[] trailing = new int[1];
	int offset = getTextFlow().getOffset(pt, trailing, result.proximity) + trailing[0];
	if (offset != -1) {
		result.trailing = trailing[0] == 1;
		result.location = new TextLocation(this, offset);
		result.bestMatchFound = result.proximity.width == 0 
				&& result.proximity.height == 0;
		if (result.bestMatchFound)
			return;
	}
	if (!search.isRecursive && getParent() instanceof TextEditPart) {
		search.setReferenceTextLocation(this, search.isForward ? getLength() : 0);
		getTextParent().getTextLocation(search, result);
	}
}

protected void searchRow(CaretRequest search, SearchResult result) {
	if (search.isRecursive || !(getParent() instanceof TextEditPart)) {
		Point where = search.getLocation().getCopy();
		TextFlow flow = getTextFlow();
		flow.translateToRelative(where);
		int[] trailing = new int[1];
		int offset = flow.getNextOffset(where, search.isForward, trailing) 
				+ trailing[0];
		if (offset != -1) {
			CaretInfo info = getCaretPlacement(offset, trailing[0] == 1);
			int vDistance = Math.abs(info.getBaseline() - search.getLocation().y);
			if (vDistance > result.proximity.height)
				result.bestMatchFound = true;
			else {
				int hDistance = Math.abs(info.getX() - search.getLocation().x);
				if (vDistance < result.proximity.height 
						|| (vDistance == result.proximity.height 
						&& hDistance < result.proximity.width)) {
					result.trailing = trailing[0] == 1;
					result.location = new TextLocation(this, offset);
					result.proximity.width = hDistance;
					result.proximity.height = vDistance;
				}
			}
		} else {
			// @TODO:Pratik should go to the end of the current line if offset == -1 (as is 
			// the case when on the last line and going down, or the first line and going up)
		}
	} else
		getTextParent().getTextLocation(search, result);
}

protected void searchWordBoundary(CaretRequest search, SearchResult result) {
	String text = getTextFlow().getText();
	if (text.trim().length() > 0) {
		int length = text.length();
		int offset = BreakIterator.DONE;
		if (wordIterator == null)
			wordIterator = BreakIterator.getWordInstance();
		wordIterator.setText(text);

		if (search.isRecursive)
			offset = search.isForward ? 0 : length;
		else {
			if (search.isForward)
				offset = search.where.offset == length ? BreakIterator.DONE
						: wordIterator.following(search.where.offset);
			else
				offset = wordIterator.preceding(Math.min(search.where.offset, length - 1));
		}
		int index = Math.min(offset, length - 1);
		if (offset != BreakIterator.DONE 
				&& Character.isWhitespace(text.charAt(index)))
			offset = search.isForward ? wordIterator.following(index) 
					: wordIterator.preceding(index);
		if (offset != BreakIterator.DONE) {
			result.location = new TextLocation(this, offset);
			result.trailing = offset == length;
			// this is the case where you're at the beginning or the end of the text flow
			result.bestMatchFound = !Character.isWhitespace(
					text.charAt(Math.min(offset, length - 1)));
		}
	}
	
	if (!result.bestMatchFound && !search.isRecursive 
			&& getParent() instanceof TextEditPart)
		getTextParent().getTextLocation(search, result);
}

public void setSelection(int start, int end) {
	if (start == end)
		getTextFlow().setSelection(-1, -1);
	else
		getTextFlow().setSelection(start, end);
}

private int vDistanceBetween(Rectangle rect, int y) {
	if (y < rect.y)
		return rect.y - y;
	return Math.max(0, y - rect.bottom());
}

}