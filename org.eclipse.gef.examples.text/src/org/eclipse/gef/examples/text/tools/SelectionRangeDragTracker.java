/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
import org.eclipse.gef.examples.text.edit.TextualEditPart;

/**
 * @since 3.1
 */
public class SelectionRangeDragTracker extends SimpleDragTracker {

private static final int STATE_START = SimpleDragTracker.MAX_STATE << 1;

private static final int STATE_SWIPE = SimpleDragTracker.MAX_STATE << 2;

private final TextualEditPart owner;

private TextLocation beginDrag;

private TextLocation endDrag;

/**
 * @since 3.1
 */
public SelectionRangeDragTracker(TextualEditPart owner) {
	this.owner = owner;
}

protected Cursor calculateCursor() {
	return Cursors.IBEAM;
}

protected String getCommandName() {
	return "Drop Text Request";
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

protected boolean handleDragStarted() {
	return stateTransition(STATE_START, STATE_SWIPE);
}

private TextLocation getCurrentTextLocation() {
	EditPart part = getCurrentViewer().findObjectAt(getLocation());
	if (part instanceof TextualEditPart) {
		TextualEditPart textPart = (TextualEditPart)part;
		if (textPart.acceptsCaret()) return textPart.getLocation(getLocation());
	}
	return null;
}

protected boolean handleDragInProgress() {
	if (isInState(STATE_SWIPE)) {
		endDrag = getCurrentTextLocation();
		if (endDrag != null) {
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
	return super.handleDragInProgress();
}

}