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

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

final public class LabelCellEditorLocator
	implements CellEditorLocator
{

private Label label;

public LabelCellEditorLocator(Label label) {
	setLabel(label);
}

public void relocate(CellEditor celleditor) {
	Text text = (Text)celleditor.getControl();
	Point pref = text.computeSize(-1, -1);
	Rectangle rect = label.getTextBounds().getCopy();
	label.translateToAbsolute(rect);
	text.setBounds(rect.x-4, rect.y-1, pref.x+1, pref.y+1);	
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
