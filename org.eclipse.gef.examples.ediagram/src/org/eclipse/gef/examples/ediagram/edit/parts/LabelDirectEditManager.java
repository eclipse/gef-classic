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
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;


public class LabelDirectEditManager 
	extends DirectEditManager 
{

private double cachedZoom = -1.0;
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
	super.bringDown();
	disposeScaledFont();
}

/**
 * Creates the cell editor on the given composite.  The cell editor is created by 
 * instantiating the cell editor type passed into this DirectEditManager's constuctor.
 * @param composite the composite to create the cell editor on
 * @return the newly created cell editor
 */
protected CellEditor createCellEditorOn(Composite composite) {
	return new TextCellEditor(composite, SWT.MULTI | SWT.WRAP) {
		public boolean isPasteEnabled() {
			boolean result = false;
			if (text != null && !text.isDisposed()) {
				Clipboard cb = new Clipboard(Display.getDefault());
				TransferData[] transferTypes = cb.getAvailableTypes();
				for (int i = 0; i < transferTypes.length; i++) {
					if (TextTransfer.getInstance().isSupportedType(transferTypes[i])) {
						result = true;
						break;
					}
				}
				cb.dispose();
			}
			return result; 
		}
	};
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
	Text text = (Text)getCellEditor().getControl();
	text.setText(getInitialText());
	ZoomManager zoomMgr = (ZoomManager)getEditPart().getViewer()
			.getProperty(ZoomManager.class.toString());
	cachedZoom = -1.0;
	updateScaledFont(zoomMgr.getZoom());
	zoomMgr.addZoomListener(zoomListener);
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
