package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Text;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;

public class LogicLabelEditManager 
	extends DirectEditManager {

Font scaledFont;

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
	Label label = (Label)((GraphicalEditPart)getEditPart()).getFigure();
	String initialLabelText = label.getText();
	getCellEditor().setValue(initialLabelText);
	Text text = (Text)getCellEditor().getControl();
	IFigure figure = ((GraphicalEditPart)getEditPart()).getFigure();
	scaledFont = figure.getFont();
	FontData data = scaledFont.getFontData()[0];
	Dimension fontSize = new Dimension(0, data.height);
	label.translateToAbsolute(fontSize);
	data.height = fontSize.height;
	scaledFont = new Font(null, data);
	
	text.setFont(scaledFont);
	text.selectAll();
}

}