package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.widgets.Text;

import com.ibm.etools.draw2d.Label;
import com.ibm.etools.gef.GraphicalEditPart;
import com.ibm.etools.gef.tools.CellEditorLocator;
import com.ibm.etools.gef.tools.DirectEditManager;

public class LogicLabelEditManager 
	extends DirectEditManager {

public LogicLabelEditManager(
		GraphicalEditPart source,
		Class editorType,
		CellEditorLocator locator)
{
	super(source, editorType, locator);
}

protected void initCellEditor() {
	String initialLabelText = ((Label)((GraphicalEditPart)getEditPart()).getFigure()).getText();
	getCellEditor().setValue(initialLabelText);
	Text text = (Text)getCellEditor().getControl();
	text.setFont(((GraphicalEditPart)getEditPart()).getFigure().getFont());
	text.selectAll();
}

}