/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import org.eclipse.jface.viewers.CellEditor;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.tools.CellEditorLocator;

import org.eclipse.gef.examples.logicdesigner.figures.StickyNoteFigure;

final public class LabelCellEditorLocator
	implements CellEditorLocator
{

private StickyNoteFigure stickyNote;

private static int WIN_X_OFFSET = -4;
private static int WIN_W_OFFSET = 5;
private static int GTK_X_OFFSET = 0;
private static int GTK_W_OFFSET = 0;

public LabelCellEditorLocator(StickyNoteFigure stickyNote) {
	setLabel(stickyNote);
}

public void relocate(CellEditor celleditor) {
	Text text = (Text)celleditor.getControl();

	Rectangle rect = stickyNote.getClientArea().getCopy();
	stickyNote.translateToAbsolute(rect);
	
	int xOffset;
	int wOffset;
		
	if (SWT.getPlatform().equalsIgnoreCase("gtk")) {
		xOffset = GTK_X_OFFSET;
		wOffset = GTK_W_OFFSET;
	} else {
		xOffset = WIN_X_OFFSET;
		wOffset = WIN_W_OFFSET;
	}

	text.setBounds(rect.x + xOffset, rect.y, rect.width + wOffset, rect.height);	
}

/**
 * Returns the stickyNote figure.
 */
protected StickyNoteFigure getLabel() {
	return stickyNote;
}

/**
 * Sets the Sticky note figure.
 * @param stickyNote The stickyNote to set
 */
protected void setLabel(StickyNoteFigure stickyNote) {
	this.stickyNote = stickyNote;
}

}
