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

package org.eclipse.gef.examples.text;
  
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.UpdateListener;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.CaretInfo;

import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

import org.eclipse.gef.examples.text.edit.TextualEditPart;

/**
 * @since 3.1
 */
public class GraphicalTextViewer extends ScrollingGraphicalViewer {

class CaretRefresh implements Runnable {
	private boolean reveal;
	public CaretRefresh(boolean reveal) {
		setReveal(reveal);
	}
	public void run() {
		refreshCaret();
		caretRefresh = null;
		if (reveal)
			revealCaret();
	}
	public void setReveal(boolean newVal) {
		reveal |= newVal;
	}
}

private Caret caret;
private CaretRefresh caretRefresh;
private SelectionRange selectionRange;

private Caret getCaret() {
	if (caret == null && getControl() != null)
			caret = new Caret((Canvas)getControl(), 0);
	return caret;
}

public Rectangle getCaretBounds() {
	return new Rectangle(getCaret().getBounds());
}

public CaretInfo getCaretInfo() {
	TextLocation location = getCaretLocation();
	if (getSelectionRange().isForward && location.offset > 0)
		return getCaretOwner().getCaretPlacement(location.offset - 1, true);
	else
		return getCaretOwner().getCaretPlacement(location.offset, false);
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
		public void notifyPainting(Rectangle damage, Map dirtyRegions) {
			queueCaretRefresh(false);
		}
		public void notifyValidating() {
		}
	});
}

/**
 * @since 3.1
 */
void queueCaretRefresh(boolean revealAfterwards) {
	if (caretRefresh == null) {
		caretRefresh = new CaretRefresh(revealAfterwards);
		getUpdateManager().runWithUpdate(caretRefresh);
	} else
		caretRefresh.setReveal(revealAfterwards);
}

void refreshCaret() {
	if (getCaretOwner() == null)
		return;
	CaretInfo info = getCaretInfo();
	getCaret().setBounds(info.getX(), info.getY(), 1, info.getHeight());
}

public void revealCaret() {
	// @TODO:Pratik  you should expose the text location first (it might not be visible)
	Viewport port = getFigureCanvas().getViewport();
	Rectangle view = new Rectangle(port.getViewLocation(), port.getClientArea().getSize());
	Rectangle exposeRegion = new Rectangle(getCaretBounds());
	port.getContents().translateToRelative(exposeRegion);
	if (!view.contains(exposeRegion)) {
		int x = view.x, y = view.y;
		if (exposeRegion.x < view.x)
			x = exposeRegion.x;
		else if (exposeRegion.right() > view.right())
			x = view.x + exposeRegion.right() - view.right();
		if (exposeRegion.y < view.y)
			y = exposeRegion.y;
		else if (exposeRegion.bottom() > view.bottom())
			y = view.y + exposeRegion.bottom() - view.bottom();
		getFigureCanvas().scrollTo(x, y);		
	}
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
		currentSelection = selectionRange.getSelectedParts();
		for (int i = 0; i < currentSelection.size(); i++)
			((TextualEditPart)currentSelection.get(i)).setSelection(-1, -1);
	}
	selectionRange = newRange;
	if (selectionRange != null) {
		currentSelection = selectionRange.getSelectedParts();
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

	queueCaretRefresh(true);
	fireSelectionChanged();
}

}
