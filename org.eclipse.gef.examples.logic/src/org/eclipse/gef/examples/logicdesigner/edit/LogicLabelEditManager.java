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
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Text;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;

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
	verifyListener = new VerifyListener() {
		public void verifyText(VerifyEvent event) {
			Text text = (Text)getCellEditor().getControl();
			String oldText = text.getText();
			String leftText = oldText.substring(0, event.start);
			String rightText = oldText.substring(event.end, oldText	.length());
			GC gc = new GC(text);
			String s = leftText + event.text + rightText;
			Point size = gc.textExtent(leftText + event.text + rightText);
			gc.dispose();
			if (size.x != 0)
				size = text.computeSize(size.x, SWT.DEFAULT);
			getCellEditor().getControl().setSize(size.x, size.y);
		}
	};
	text.addVerifyListener(verifyListener);

	Label label = (Label)((GraphicalEditPart)getEditPart()).getFigure();
	String initialLabelText = label.getText();
	getCellEditor().setValue(initialLabelText);
	IFigure figure = ((GraphicalEditPart)getEditPart()).getFigure();
	scaledFont = figure.getFont();
	FontData data = scaledFont.getFontData()[0];
	Dimension fontSize = new Dimension(0, data.getHeight());
	label.translateToAbsolute(fontSize);
	data.setHeight(fontSize.height);
	scaledFont = new Font(null, data);
	
	text.setFont(scaledFont);
	text.selectAll();
}

protected void unhookListeners() {
	super.unhookListeners();
	Text text = (Text)getCellEditor().getControl();
	text.removeVerifyListener(verifyListener);
	verifyListener = null;
}

}