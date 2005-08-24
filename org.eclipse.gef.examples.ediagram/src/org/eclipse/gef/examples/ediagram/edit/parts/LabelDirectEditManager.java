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
package org.eclipse.gef.examples.ediagram.edit.parts;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.CellEditorActionHandler;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;


public class LabelDirectEditManager 
	extends DirectEditManager 
{

private IActionBars actionBars;
private CellEditorActionHandler actionHandler;
private double cachedZoom = -1.0;
private IAction copy, cut, paste, undo, redo, find, selectAll, delete;
private Font scaledFont;
private ZoomListener zoomListener = new ZoomListener() {
	public void zoomChanged(double newZoom) {
		updateScaledFont(newZoom);
	}
};

public LabelDirectEditManager(GraphicalEditPart source, CellEditorLocator locator) {
	super(source, TextCellEditor.class, locator);
}

protected void bringDown() {
	ZoomManager zoomMgr = (ZoomManager)getEditPart().getViewer()
			.getProperty(ZoomManager.class.toString());
	zoomMgr.removeZoomListener(zoomListener);
	if (actionHandler != null) {
		actionHandler.dispose();
		actionHandler = null;
	}
	if (actionBars != null) {
		restoreSavedActions(actionBars);
		actionBars.updateActionBars();
		actionBars = null;
	}
	super.bringDown();
	disposeScaledFont();
}

/**
 * Creates a multi-lined text celleditor that can wrap.
 */
protected CellEditor createCellEditorOn(Composite composite) {
	return new TextCellEditor(composite, SWT.MULTI | SWT.WRAP);
}

private void disposeScaledFont() {
	if (scaledFont != null) {
		scaledFont.dispose();
		scaledFont = null;
	}
}

/**
 * Used to determine the initial text of the cell editor and to determine the font size
 * of the text.
 * @return the figure being edited
 */
protected IFigure getDirectEditFigure() {
	return ((BaseEditPart)getEditPart()).getDirectEditFigure();
}

/**
 * @return the initial value of the text shown in the cell editor for direct-editing;
 * cannot return <code>null</code>
 */
protected String getInitialText() {
	return ((BaseEditPart)getEditPart()).getDirectEditText();
}

protected void initCellEditor() {
	getCellEditor().setValue(getInitialText());
	ZoomManager zoomMgr = (ZoomManager)getEditPart().getViewer()
			.getProperty(ZoomManager.class.toString());
	cachedZoom = -1.0;
	updateScaledFont(zoomMgr.getZoom());
	zoomMgr.addZoomListener(zoomListener);
	// Hook the cell editor's copy/paste actions to the actionBars so that they can
	// be invoked via keyboard shortcuts.
	actionBars = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.getActiveEditor().getEditorSite().getActionBars();
	saveCurrentActions(actionBars);
	actionHandler = new CellEditorActionHandler(actionBars);
	actionHandler.addCellEditor(getCellEditor());
	actionBars.updateActionBars();
}

private void restoreSavedActions(IActionBars actionBars){
	actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copy);
	actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), paste);
	actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), delete);
	actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAll);
	actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cut);
	actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), find);
	actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undo);
	actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redo);
}

private void saveCurrentActions(IActionBars actionBars) {
	copy = actionBars.getGlobalActionHandler(ActionFactory.COPY.getId());
	paste = actionBars.getGlobalActionHandler(ActionFactory.PASTE.getId());
	delete = actionBars.getGlobalActionHandler(ActionFactory.DELETE.getId());
	selectAll = actionBars.getGlobalActionHandler(ActionFactory.SELECT_ALL.getId());
	cut = actionBars.getGlobalActionHandler(ActionFactory.CUT.getId());
	find = actionBars.getGlobalActionHandler(ActionFactory.FIND.getId());
	undo = actionBars.getGlobalActionHandler(ActionFactory.UNDO.getId());
	redo = actionBars.getGlobalActionHandler(ActionFactory.REDO.getId());
}

private void updateScaledFont(double zoom) {
	if (cachedZoom == zoom)
		return;
	
	Text text = (Text)getCellEditor().getControl();
	Font font = getEditPart().getFigure().getFont();
	
	disposeScaledFont();
	cachedZoom = zoom;
	if (zoom == 1.0)
		text.setFont(font);
	else {
		FontData fd = font.getFontData()[0];
		fd.setHeight((int)(fd.getHeight() * zoom));
		text.setFont(scaledFont = new Font(null, fd));
	}
}

}
