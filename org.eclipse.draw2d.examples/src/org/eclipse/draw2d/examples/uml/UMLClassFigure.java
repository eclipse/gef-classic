/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.draw2d.examples.uml;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

public class UMLClassFigure extends Figure {

static final Color BG = new Color(null, 242, 240, 255);

static Image classImage =
	new Image(null, UMLClassFigure.class.getResourceAsStream("class_obj.gif"));

static Font BOLD = new Font(null, "", 10, SWT.BOLD);

public UMLClassFigure() {
	class SeparatorBorder extends MarginBorder {
		SeparatorBorder () {
			super(3, 5, 3, 5);
		}
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			Rectangle where = getPaintRectangle(figure, insets);
			graphics.drawLine(where.getTopLeft(), where.getTopRight());
		}
	}
	
	Label header = new Label("ClassName", classImage);
	header.setFont(BOLD);
	header.setBorder(new MarginBorder(3, 5, 3, 5));
	
	Figure attributes = new Figure();
	ToolbarLayout layout;
	attributes.setLayoutManager(layout = new ToolbarLayout());
	layout.setStretchMinorAxis(false);
	attributes.add(new Label("name : String"));
	attributes.add(new Label("ID : String"));
	attributes.setBorder(new SeparatorBorder());
	
	Figure methods = new Figure();
	methods.setLayoutManager(layout = new ToolbarLayout());
	layout.setStretchMinorAxis(false);
	methods.add(new Label("foo() : int"));
	methods.add(new Label("bar() : char"));
	methods.setBorder(new SeparatorBorder());
	
	setBorder(new LineBorder());
	setLayoutManager(new ToolbarLayout());
	
	add(header);
	add(attributes);
	add(methods);
	setOpaque(true);
	setBackgroundColor(BG);
}

}
