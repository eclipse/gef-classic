/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import org.eclipse.swt.widgets.Text;

import org.eclipse.jface.viewers.CellEditor;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.tools.CellEditorLocator;

import org.eclipse.gef.examples.logicdesigner.figures.StickyNoteFigure;

final public class LabelCellEditorLocator
	implements CellEditorLocator
{

private StickyNoteFigure stickyNote;

public LabelCellEditorLocator(StickyNoteFigure stickyNote) {
	setLabel(stickyNote);
}

public void relocate(CellEditor celleditor) {
	Text text = (Text)celleditor.getControl();
	Rectangle rect = stickyNote.getClientArea();
	stickyNote.translateToAbsolute(rect);
	org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
	rect.translate(trim.x, trim.y);
	rect.width += trim.width;
	rect.height += trim.height;
	text.setBounds(rect.x, rect.y, rect.width, rect.height);
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