/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2dl.examples.uml;

import org.eclipse.draw2dl.ToolbarLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2dl.geometry.Insets;
import org.eclipse.draw2dl.geometry.Rectangle;

public class UMLClassFigure extends org.eclipse.draw2dl.Figure {

static final Color BG = new Color(null, 242, 240, 255);

static Image classImage =
	new Image(null, UMLClassFigure.class.getResourceAsStream("class_obj.gif"));

static Font BOLD = new Font(null, "", 10, SWT.BOLD);

public UMLClassFigure() {
	class SeparatorBorder extends org.eclipse.draw2dl.MarginBorder {
		SeparatorBorder () {
			super(3, 5, 3, 5);
		}
		public void paint(org.eclipse.draw2dl.IFigure figure, org.eclipse.draw2dl.Graphics graphics, Insets insets) {
			Rectangle where = getPaintRectangle(figure, insets);
			graphics.drawLine(where.getTopLeft(), where.getTopRight());
		}
	}
	
	org.eclipse.draw2dl.Label header = new org.eclipse.draw2dl.Label("ClassName", classImage);
	header.setFont(BOLD);
	header.setBorder(new org.eclipse.draw2dl.MarginBorder(3, 5, 3, 5));
	
	org.eclipse.draw2dl.Figure attributes = new org.eclipse.draw2dl.Figure();
	org.eclipse.draw2dl.ToolbarLayout layout;
	attributes.setLayoutManager(layout = new org.eclipse.draw2dl.ToolbarLayout());
	layout.setStretchMinorAxis(false);
	attributes.add(new org.eclipse.draw2dl.Label("name : String"));
	attributes.add(new org.eclipse.draw2dl.Label("ID : String"));
	attributes.setBorder(new SeparatorBorder());
	
	org.eclipse.draw2dl.Figure methods = new org.eclipse.draw2dl.Figure();
	methods.setLayoutManager(layout = new org.eclipse.draw2dl.ToolbarLayout());
	layout.setStretchMinorAxis(false);
	methods.add(new org.eclipse.draw2dl.Label("foo() : int"));
	methods.add(new org.eclipse.draw2dl.Label("bar() : char"));
	methods.setBorder(new SeparatorBorder());
	
	setBorder(new org.eclipse.draw2dl.LineBorder());
	setLayoutManager(new ToolbarLayout());
	
	add(header);
	add(attributes);
	add(methods);
	setOpaque(true);
	setBackgroundColor(BG);
}

}
