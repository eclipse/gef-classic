/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.examples.uml;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public class UMLClassFigure extends Figure {

	static final Color BG = new Color(null, 242, 240, 255);

	static Image classImage = new Image(null, UMLClassFigure.class.getResourceAsStream("class_obj.gif")); //$NON-NLS-1$

	static Font BOLD = new Font(null, "", 10, SWT.BOLD); //$NON-NLS-1$

	public UMLClassFigure() {
		class SeparatorBorder extends MarginBorder {
			SeparatorBorder() {
				super(3, 5, 3, 5);
			}

			@Override
			public void paint(IFigure figure, Graphics graphics, Insets insets) {
				Rectangle where = getPaintRectangle(figure, insets);
				graphics.drawLine(where.getTopLeft(), where.getTopRight());
			}
		}

		Label header = new Label("ClassName", classImage); //$NON-NLS-1$
		header.setFont(BOLD);
		header.setBorder(new MarginBorder(3, 5, 3, 5));

		Figure attributes = new Figure();
		ToolbarLayout layout;
		attributes.setLayoutManager(layout = new ToolbarLayout());
		layout.setStretchMinorAxis(false);
		attributes.add(new Label("name : String")); //$NON-NLS-1$
		attributes.add(new Label("ID : String")); //$NON-NLS-1$
		attributes.setBorder(new SeparatorBorder());

		Figure methods = new Figure();
		methods.setLayoutManager(layout = new ToolbarLayout());
		layout.setStretchMinorAxis(false);
		methods.add(new Label("foo() : int")); //$NON-NLS-1$
		methods.add(new Label("bar() : char")); //$NON-NLS-1$
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
