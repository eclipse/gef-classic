/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.tools;

import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.Cursors;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.SimpleDragTracker;
import org.eclipse.gef.tools.ToolUtilities;

import org.eclipse.gef.examples.text.GraphicalTextViewer;
import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.edit.CaretSearch;
import org.eclipse.gef.examples.text.edit.TextualEditPart;

/**
 * @since 3.1
 */
public class SelectionRangeDragTracker extends SimpleDragTracker {

private static final int STATE_START = SimpleDragTracker.MAX_STATE << 1;

private static final int STATE_SWIPE = SimpleDragTracker.MAX_STATE << 2;

private TextLocation beginDrag;

private TextLocation endDrag;

/**
 * This flag is set to true during a double-click-swipe.
 */
private boolean isWordSelection;

private final TextualEditPart textSource;

/**
 * @since 3.1
 */
public SelectionRangeDragTracker(TextualEditPart part) {
	this.textSource = part;
}

protected Cursor calculateCursor() {
	return Cursors.IBEAM;
}

/**
 * 
 * @since 3.1
 */
private void doNormalSwipe() {
	endDrag = getCurrentTextLocation();
	if (endDrag != null) {
		
		//Previous 
		EditPart end = endDrag.part;
		EditPart begin = beginDrag.part;
		boolean inverted = false;
		if (end == begin)
			inverted = endDrag.offset < beginDrag.offset;
		else {
			EditPart ancestor = ToolUtilities.findCommonAncestor(end, begin);
			while (end.getParent() != ancestor)
				end = end.getParent();
			while (begin.getParent() != ancestor)
				begin = begin.getParent();
			inverted = ancestor.getChildren().indexOf(end) < ancestor.getChildren().indexOf(begin);
		}
		GraphicalTextViewer viewer = (GraphicalTextViewer)getCurrentViewer();
		if (!inverted)
			viewer.setSelectionRange(new SelectionRange(beginDrag, endDrag));
		else
			viewer.setSelectionRange(new SelectionRange(endDrag, beginDrag, false));
	}
}

/**
 * Selects the word at the current mouse location
 * @since 3.1
 */
private void doWordSelect() {
	int trailing[] = new int[1];
	TextLocation exact = getSource().getLocation(getLocation(), trailing);
	
	CaretSearch nextWord = new CaretSearch();
	nextWord.type = CaretSearch.WORD_BOUNDARY;
	nextWord.isForward = true;
	nextWord.where = exact;
	TextLocation wordEnd = getSource().getNextLocation(nextWord);
	nextWord.where = wordEnd;
	nextWord.isForward = false;
	TextLocation wordBegin = getSource().getNextLocation(nextWord);
	GraphicalTextViewer viewer = (GraphicalTextViewer)getCurrentViewer();
	viewer.setSelectionRange(new SelectionRange(wordBegin, wordEnd));
}

/**
 * @since 3.1
 */
private void doWordSwipe() {
}

protected String getCommandName() {
	return "Drop Text Request";
}

private TextLocation getCurrentTextLocation() {
	EditPart part = getCurrentViewer().findObjectAt(getLocation());
	if (part instanceof TextualEditPart) {
		TextualEditPart textPart = (TextualEditPart)part;
		int trailing[] = new int[1];
		if (textPart.acceptsCaret())
			return textPart.getLocation(getLocation(), trailing);
	}
	return null;
}

/**
 * 
 * @since 3.1
 */
private TextualEditPart getSource() {
	return textSource;
}

protected boolean handleButtonDown(int button) {
	if (button == 1) {
		beginDrag = getCurrentTextLocation();
		((GraphicalTextViewer)getCurrentViewer()).setSelectionRange(new SelectionRange(
				beginDrag));
		stateTransition(STATE_INITIAL, STATE_START);
	}
	return super.handleButtonDown(button);
}

protected boolean handleDoubleClick(int button) {
	if (button == 1) {
		doWordSelect();
		isWordSelection = true;
		return true;
	}
	return super.handleDoubleClick(button);
}

protected boolean handleDragInProgress() {
	//$TODO during a swipe, the viewer should not be firing selection changes the whole time.
	if (isInState(STATE_SWIPE)) {
		if (isWordSelection)
			doWordSwipe();
		else
			doNormalSwipe();
	}
	return super.handleDragInProgress();
}

protected boolean handleDragStarted() {
	return stateTransition(STATE_START, STATE_SWIPE);
}

}