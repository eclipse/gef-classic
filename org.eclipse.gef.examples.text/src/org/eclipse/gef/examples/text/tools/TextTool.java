/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.Cursors;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.gef.tools.ToolUtilities;

import org.eclipse.gef.examples.text.GraphicalTextViewer;
import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.TextCommand;
import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.edit.TextualEditPart;

/**
 * @since 3.1
 */
public class TextTool extends SelectionTool {

static final boolean IS_CARBON = "carbon".equals(SWT.getPlatform()); //$NON-NLS-1$

private static final int MODE_BS = 2;

private static final int MODE_DEL = 3;

private static final int MODE_TYPING = 1;

private int textInputMode;

private TextCommand unfinished;

/**
 * @since 3.1
 */
public TextTool() {}

protected Cursor calculateCursor() {
	EditPart target = getTargetEditPart();
	if (target instanceof TextualEditPart) {
		TextualEditPart textTarget = (TextualEditPart)target;
		if (textTarget.acceptsCaret())
			return Cursors.IBEAM;
	}
	return super.calculateCursor();
}

/**
 * Extended to set unfinished command to <code>null</code>.
 * @see org.eclipse.gef.tools.TargetingTool#deactivate()
 */
public void deactivate() {
	setTextInputMode(0);
	super.deactivate();
}

/**
 * @since 3.1
 * @param action
 * @param event
 */
private void doAction(int action, KeyEvent event) {
	switch (action) {
		case ST.DELETE_PREVIOUS:
			doBackspace();
			break;
		case ST.DELETE_NEXT:
			doDelete();
			break;
		case ST.SELECT_COLUMN_PREVIOUS:
			doSelectLeft(true);
			break;
		case ST.COLUMN_PREVIOUS:
			doSelectLeft(false);
			break;
		case ST.SELECT_COLUMN_NEXT:
			doSelectRight(true);
			break;
		case ST.COLUMN_NEXT:
			doSelectRight(false);
			break;
		case SWT.TAB | SWT.SHIFT:
			doUnindent();
			break;
		case SWT.TAB:
			doIndent();
			break;
		case SWT.CR:
			doNewline();
			break;
		default:
			break;
	}
}

/**
 * @since 3.1
 * @param e
 */
private boolean doBackspace() {
	setTextInputMode(MODE_BS);
	SelectionRange range = getTextualViewer().getSelectionRange();
	TextRequest edit;
	if (range.isEmpty())
		edit = new TextRequest(TextRequest.REQ_BACKSPACE, range, unfinished);
	else
		edit = new TextRequest(range);
	return handleTextEdit(edit);
}

private boolean doDelete() {
	setTextInputMode(MODE_DEL);
	SelectionRange range = getTextualViewer().getSelectionRange();
	TextRequest edit;
	if (range.isEmpty())
		edit = new TextRequest(TextRequest.REQ_DELETE, range, unfinished);
	else
		edit = new TextRequest(range);
	return handleTextEdit(edit);
}

/**
 * @since 3.1
 *  
 */
private void doIndent() {
	setTextInputMode(0);
}

/**
 * @since 3.1
 * @param e
 */
private void doKeyDown(KeyEvent event) {
	int action = 0;
	if (event.keyCode != 0) {
		action = lookupAction(event.keyCode | event.stateMask);
	} else {
		action = lookupAction(event.character | event.stateMask);
		if (action == 0) {
			// see if we have a control character
			if ((event.stateMask & SWT.CTRL) != 0 && (event.character >= 0)
					&& event.character <= 31) {
				// get the character from the CTRL+char sequence, the control
				// key subtracts 64 from the value of the key that it modifies
				int c = event.character + 64;
				action = lookupAction(c | event.stateMask);
			}
		}
	}

	if (action == 0) {
		boolean ignore = false;

		if (IS_CARBON) {
			// Ignore accelerator key combinations (we do not want to
			// insert a character in the text in this instance). Do not
			// ignore COMMAND+ALT combinations since that key sequence
			// produces characters on the mac.
			ignore = (event.stateMask ^ SWT.COMMAND) == 0
					|| (event.stateMask ^ (SWT.COMMAND | SWT.SHIFT)) == 0;
		} else {
			// Ignore accelerator key combinations (we do not want to
			// insert a character in the text in this instance). Don't
			// ignore CTRL+ALT combinations since that is the Alt Gr
			// key on some keyboards.
			ignore = (event.stateMask ^ SWT.ALT) == 0
					|| (event.stateMask ^ SWT.CTRL) == 0
					|| (event.stateMask ^ (SWT.ALT | SWT.SHIFT)) == 0
					|| (event.stateMask ^ (SWT.CTRL | SWT.SHIFT)) == 0;
		}
		// -ignore anything below SPACE except for line delimiter keys and tab.
		// -ignore DEL
		if (!ignore && event.character > 31 && event.character != SWT.DEL
				|| event.character == SWT.CR || event.character == SWT.LF
				|| event.character == '\t') {
			insertContent(event.character);
		}
	} else {
		doAction(action, event);
	}
}

/**
 * @since 3.1
 */
private boolean doNewline() {
	setTextInputMode(MODE_BS);
	SelectionRange range = getTextualViewer().getSelectionRange();
	TextRequest edit;
	Assert.isTrue(range.isEmpty());
	edit = new TextRequest(TextRequest.REQ_NEWLINE, range, unfinished);
	return handleTextEdit(edit);
}

private void doSelectLeft(boolean appendSelection) {
	TextLocation caretLocation = getTextualViewer().getCaretLocation();

	TextLocation newLoc = caretLocation.part.getNextLocation(SWT.ARROW_LEFT,
			caretLocation);

	if (appendSelection) {
		SelectionRange range = getTextualViewer().getSelectionRange();
		if (range.begin.equals(caretLocation))
			range = new SelectionRange(newLoc, range.end, false);
		else
			range = new SelectionRange(range.begin, newLoc);
		getTextualViewer().setSelectionRange(range);
	} else {
		getTextualViewer().setSelectionRange(new SelectionRange(newLoc));
	}
}

/**
 * @since 3.1
 * @param b
 */
private void doSelectRight(boolean appendSelection) {
	TextLocation caretLocation = getTextualViewer().getCaretLocation();

	TextLocation newLoc = caretLocation.part.getNextLocation(SWT.ARROW_RIGHT,
			caretLocation);

	if (appendSelection) {
		SelectionRange range = getTextualViewer().getSelectionRange();
		if (caretLocation.equals(range.end))
			range = new SelectionRange(range.begin, newLoc);
		else
			range = new SelectionRange(newLoc, range.end, false);
		getTextualViewer().setSelectionRange(range);
	} else {
		getTextualViewer().setSelectionRange(new SelectionRange(newLoc));
	}
}

private void doUnindent() {}

protected String getDebugName() {
	return "TextTool";
}

GraphicalTextViewer getTextualViewer() {
	return (GraphicalTextViewer)getCurrentViewer();
}

protected boolean handleCommandStackChanged() {
	setTextInputMode(0);
	return super.handleCommandStackChanged();
}

protected boolean handleKeyDown(KeyEvent e) {
	if (isInState(STATE_INITIAL))
		doKeyDown(e);

	return super.handleKeyDown(e);
}

protected void handleKeyTraversed(TraverseEvent event) {
	if ((event.detail == SWT.TRAVERSE_TAB_PREVIOUS || event.detail == SWT.TRAVERSE_TAB_NEXT)
			&& (event.stateMask & SWT.CTRL) == 0)
		event.doit = false;
}

protected boolean handleMove() {
	super.handleMove();
	refreshCursor();
	return true;
}

private boolean handleTextEdit(TextRequest edit) {
	GraphicalTextViewer viewer = getTextualViewer();
	SelectionRange range = viewer.getSelectionRange();
	EditPart target, candidate = ToolUtilities.findCommonAncestor(range.begin.part,
			range.end.part);

	target = candidate.getTargetEditPart(edit);
	while (target == null && candidate != null) {
		candidate = candidate.getParent();
		target = candidate.getTargetEditPart(edit);
	}

	Command insert = null;
	if (target != null)
		insert = target.getCommand(edit);

	if (insert == null || !insert.canExecute())
		return false;

	if (unfinished == null || insert != unfinished) {
		executeCommand(insert);
		unfinished = (TextCommand)insert;
	} else
		unfinished.executeMore();

	viewer.setSelectionRange(new SelectionRange(unfinished
			.getExecuteSelectionRange(viewer).end));
	return true;
}

/**
 * @since 3.1
 * @param e
 */
private boolean insertContent(char c) {
	setTextInputMode(MODE_TYPING);
	TextRequest edit = new TextRequest(getTextualViewer().getSelectionRange(), Character
			.toString(c), unfinished);
	return handleTextEdit(edit);
}

/**
 * @since 3.1
 * @param i
 * @return
 */
private int lookupAction(int i) {
	switch (i) {
		case SWT.ARROW_LEFT:
			return ST.COLUMN_PREVIOUS;
		case SWT.ARROW_RIGHT:
			return ST.COLUMN_NEXT;
		case SWT.ARROW_RIGHT | SWT.SHIFT:
			return ST.SELECT_COLUMN_NEXT;
		case SWT.ARROW_LEFT | SWT.SHIFT:
			return ST.SELECT_COLUMN_PREVIOUS;
		case SWT.ARROW_UP:
			return ST.LINE_UP;
		case SWT.DEL:
			return ST.DELETE_NEXT;
		case SWT.BS:
			return ST.DELETE_PREVIOUS;
		case SWT.TAB | SWT.SHIFT:
			return SWT.TAB | SWT.SHIFT;
		case SWT.TAB:
			return SWT.TAB;
		case SWT.LF:
		case SWT.CR:
			return SWT.CR;
		default:
			break;
	}
	return 0;
}

public void setDragTracker(DragTracker newDragTracker) {
	super.setDragTracker(newDragTracker);
	setTextInputMode(0);
}

/**
 * @since 3.1
 * @param mode_typing2
 */
private void setTextInputMode(int mode) {
	if (textInputMode != mode)
		unfinished = null;
	textInputMode = mode;
}

}