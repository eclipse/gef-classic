/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;


public class LabelDirectEditManager 
	extends DirectEditManager 
{

Font scaledFont;

public LabelDirectEditManager(
		GraphicalEditPart source,
		Class editorType,
		CellEditorLocator locator)
{
	super(source, editorType, locator);
}

/**
 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
 */
protected void bringDown() {
	//This method might be re-entered when super.bringDown() is called.
	Font disposeFont = scaledFont;
	scaledFont = null;
	super.bringDown();
	if (disposeFont != null)
		disposeFont.dispose();	
}

protected void initCellEditor() {
	Text text = (Text)getCellEditor().getControl();

	getCellEditor().setValue(getInitialText());
	IFigure figure = getEditPart().getFigure();
	scaledFont = figure.getFont();
	FontData data = scaledFont.getFontData()[0];
	Dimension fontSize = new Dimension(0, data.getHeight());
	getDirectEditFigure().translateToAbsolute(fontSize);
	data.setHeight(fontSize.height);
	scaledFont = new Font(null, data);
	
	text.setFont(scaledFont);
	text.selectAll();
}

/**
 * Creates the cell editor on the given composite.  The cell editor is created by 
 * instantiating the cell editor type passed into this DirectEditManager's constuctor.
 * @param composite the composite to create the cell editor on
 * @return the newly created cell editor
 */
protected CellEditor createCellEditorOn(Composite composite) {
	return new TextCellEditor(composite, SWT.MULTI | SWT.WRAP);
}

/**
 * @return the initial value of the text shown in the cell editor for direct-editing;
 * cannot return <code>null</code>
 */
protected String getInitialText() {
	return ((BaseEditPart)getEditPart()).getDirectEditText();
}

/**
 * Used to determine the initial text of the cell editor and to determine the font size
 * of the text.
 * @return the figure being edited
 */
protected IFigure getDirectEditFigure() {
	return ((BaseEditPart)getEditPart()).getDirectEditFigure();
}

}