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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.VerifyListener;
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

import org.eclipse.gef.examples.logicdesigner.figures.StickyNoteFigure;

public class LogicLabelEditManager 
	extends DirectEditManager 
{

Font scaledFont;
private VerifyListener verifyListener;

public LogicLabelEditManager(
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

	
	StickyNoteFigure stickyNote = (StickyNoteFigure)((GraphicalEditPart)getEditPart()).getFigure();
	String initialLabelText = stickyNote.getText();
	getCellEditor().setValue(initialLabelText);
	IFigure figure = ((GraphicalEditPart)getEditPart()).getFigure();
	scaledFont = figure.getFont();
	FontData data = scaledFont.getFontData()[0];
	Dimension fontSize = new Dimension(0, data.getHeight());
	stickyNote.translateToAbsolute(fontSize);
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

class MultiTextCellEditor extends TextCellEditor {
	
	public MultiTextCellEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		if (keyEvent.character == '\r') {
			if ((keyEvent.stateMask & SWT.CTRL) != 0) {
				keyEvent.doit = false;
				int index = text.getText().lastIndexOf("\r\n");
				if (index == -1)
					index = text.getText().lastIndexOf('\r');
				if (index == -1)
					index = text.getText().lastIndexOf('\n');
				if (index != -1)
					text.setText(text.getText().substring(0, index));
				
				fireApplyEditorValue();
				deactivate();
			}
		} else
			super.keyReleaseOccured(keyEvent);
	}
}


}
 