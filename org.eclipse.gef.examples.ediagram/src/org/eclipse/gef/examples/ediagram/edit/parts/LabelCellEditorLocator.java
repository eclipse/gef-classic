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

import org.eclipse.swt.widgets.Text;

import org.eclipse.jface.viewers.CellEditor;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.tools.CellEditorLocator;

final public class LabelCellEditorLocator
	implements CellEditorLocator
{

protected IFigure fig;

public LabelCellEditorLocator(IFigure figure) {
	fig = figure;
}

public void relocate(CellEditor celleditor) {
	Text text = (Text)celleditor.getControl();

	Rectangle rect = fig.getClientArea(Rectangle.SINGLETON);
	if (fig instanceof Label)
		rect = ((Label)fig).getTextBounds().intersect(rect);
	fig.translateToAbsolute(rect);

	org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
	rect.translate(trim.x, trim.y);
	rect.width += trim.width;
	rect.height += trim.height;
	
	text.setBounds(rect.x, rect.y, rect.width, rect.height);
}

}
