package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.CellEditorActionHandler;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GEF;
import org.eclipse.gef.tools.*;
import org.eclipse.gef.tools.CellEditorLocator;

final public class LabelCellEditorLocator
	implements CellEditorLocator
{

private Label label;

public LabelCellEditorLocator(Label label) {
	setLabel(label);
}

public void relocate(CellEditor celleditor) {
	Text text = (Text)celleditor.getControl();
	Point sel = text.getSelection();
	Point pref = text.computeSize(-1, -1);
	Rectangle rect = label.getTextBounds().getCopy();
	label.translateToAbsolute(rect);
	text.setBounds(rect.x-4, rect.y-1, pref.x+1, pref.y+1);	
	text.setSelection(0);
	text.setSelection(sel);
}

/**
 * Returns the Label figure.
 */
protected Label getLabel() {
	return label;
}

/**
 * Sets the label.
 * @param label The label to set
 */
protected void setLabel(Label label) {
	this.label = label;
}

}
