/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text;
  
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.UpdateListener;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.ToolUtilities;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

import org.eclipse.gef.examples.text.edit.TextualEditPart;

/**
 * @since 3.1
 */
public class GraphicalTextViewer extends ScrollingGraphicalViewer {

class CaretRefresh implements Runnable {
	public void run() {
		refreshCaret();
		caretRefresh = null;
	}
}

private Caret caret;
private Runnable caretRefresh;
private SelectionRange selectionRange;

/**
 * @since 3.1
 * @param part
 * @param result
 */
private void depthFirstTraversal(EditPart part, ArrayList result) {
	if (part.getChildren().isEmpty())
		result.add(part);
	else
		for (int i = 0; i < part.getChildren().size(); i++)
			depthFirstTraversal((EditPart)part.getChildren().get(i), result);
}

/**
 * @since 3.1
 * @param start
 * @param end
 * @return
 */
private List findLeavesBetweenInclusive(EditPart ll, EditPart rr) {
	if (ll == rr) return Collections.singletonList(ll);
	EditPart commonAncestor = ToolUtilities.findCommonAncestor(ll, rr);

	EditPart l = ll.getParent();
	List list;

	ArrayList result = new ArrayList();
	while (l != commonAncestor) {
		list = l.getChildren();
		int start = list.indexOf(ll);
		for (int i = start; i < list.size(); i++)
			depthFirstTraversal((EditPart)list.get(i), result);

		ll = l;
		l = l.getParent();
	}

	ArrayList rightSide = new ArrayList();
	EditPart r = rr.getParent();
	while (r != commonAncestor) {
		list = r.getChildren();
		int end = list.indexOf(rr);
		for (int i = 0; i <= end; i++)
			depthFirstTraversal((EditPart)list.get(i), rightSide);

		rr = r;
		r = r.getParent();
	}

	list = commonAncestor.getChildren();
	int start = list.indexOf(ll) + 1;
	int end = list.indexOf(rr);
	for (int i = start; i < end; i++)
		depthFirstTraversal((EditPart)list.get(i), result);

	result.addAll(rightSide);
	return result;
}

private Caret getCaret() {
	if (caret == null && getControl() != null)
			caret = new Caret((Canvas)getControl(), 0);
	return caret;
}

public Rectangle getCaretBounds() {
	return new Rectangle(getCaret().getBounds());
}

public TextLocation getCaretLocation() {
	if (selectionRange.isForward) return selectionRange.end;
	return selectionRange.begin;
}

public TextualEditPart getCaretOwner() {
	if (selectionRange == null) return null;
	if (selectionRange.isForward) return selectionRange.end.part;
	return selectionRange.begin.part;
}

/**
 * Returns the viewers selection range by <em>reference</em>.  The range should not be
 * modified directly.
 * @since 3.1
 * @return the current selection by reference
 */
public SelectionRange getSelectionRange() {
	return selectionRange;
}

private UpdateManager getUpdateManager() {
	return getLightweightSystem().getUpdateManager();
}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#hookControl()
 */
protected void hookControl() {
	super.hookControl();
	getUpdateManager().addUpdateListener(new UpdateListener() {
		public void notifyPainting(Rectangle damage, Map dirtyRegions) { }
		public void notifyValidating() {
			queueCaretRefresh();
		}
	});
}

/**
 * @since 3.1
 */
void queueCaretRefresh() {
	if (caretRefresh == null) {
		caretRefresh = new CaretRefresh();
		getUpdateManager().runWithUpdate(caretRefresh);
	}
}

void refreshCaret() {
	if (getCaretOwner() == null)
		return;
	Rectangle r = getCaretOwner().getCaretPlacement(getCaretLocation().offset);
	getCaret().setBounds(r.x, r.y, r.width, r.height);
}

public void setCaretVisible(boolean value) {
	Assert.isNotNull(getControl(), "The control has not been created");
	getCaret().setVisible(value);
}

/**
 * Sets the selection range to the given value.  Updates any editparts which had or will
 * have textual selection.  Fires selection changed.  Place the caret in the appropriate
 * location.
 * 
 * @since 3.1
 * @param newRange the new selection range
 */
public void setSelectionRange(SelectionRange newRange) {
	List currentSelection;
	if (selectionRange != null) {
		currentSelection = findLeavesBetweenInclusive(selectionRange.begin.part,
				selectionRange.end.part);
		for (int i = 0; i < currentSelection.size(); i++)
			((TextualEditPart)currentSelection.get(i)).setSelection(-1, -1);
		selectionRange.begin.part.setSelection(-1, -1);
		selectionRange.end.part.setSelection(-1, -1);
	}
	selectionRange = newRange;
	if (selectionRange != null) {
		currentSelection = findLeavesBetweenInclusive(selectionRange.begin.part,
				selectionRange.end.part);
		for (int i = 0; i < currentSelection.size(); i++) {
			TextualEditPart textpart = (TextualEditPart)currentSelection.get(i);
			textpart.setSelection(0, textpart.getLength());
		}

		if (selectionRange.begin.part == selectionRange.end.part)
			selectionRange.begin.part.setSelection(selectionRange.begin.offset,
					selectionRange.end.offset);
		else {
			selectionRange.begin.part.setSelection(selectionRange.begin.offset,
					selectionRange.begin.part.getLength());
			selectionRange.end.part.setSelection(0, selectionRange.end.offset);
		}
	}

	queueCaretRefresh();
	fireSelectionChanged();
}

}