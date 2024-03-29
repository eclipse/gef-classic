/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.examples.zoom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ButtonBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.parts.Thumbnail;

/**
 * This class demonstrates Draw2d's zoom capabilities.
 *
 * @author delee
 */

public class ZoomExample {

	private static Figure contents;

	public static void main(String[] args) {
		Display d = new Display();
		final Shell shell = new Shell(d);
		shell.setSize(800, 800);
		LightweightSystem lws = new LightweightSystem(shell);

		Figure fig = new Figure();
		fig.setLayoutManager(new ToolbarLayout());

		final ScrollBar bar = new ScrollBar();
		final Label l = new Label("< Zoom >"); //$NON-NLS-1$

		l.setBorder(new SchemeBorder(ButtonBorder.SCHEMES.BUTTON_SCROLLBAR));
		bar.setThumb(l);
		bar.setHorizontal(true);
		bar.setMaximum(200);
		bar.setMinimum(0);
		bar.setExtent(25);

		final ZoomContainer panel = new ZoomContainer();
		panel.setBorder(new GroupBoxBorder("Zooming figure")); //$NON-NLS-1$
		panel.setMinimumSize(new Dimension(5, 5));
		panel.setPreferredSize(500, 500);
		fig.add(bar);
		fig.add(panel);

		bar.addPropertyChangeListener("value", evt -> { //$NON-NLS-1$
			float z = (bar.getValue() + 10) * 0.02f;
			panel.setZoom(z);
		});

		panel.add(getContents());
		bar.setValue(40);

		lws.setContents(fig);

		// overviewShell = new Shell(shell, SWT.TITLE| SWT.RESIZE | SWT.NO_REDRAW_RESIZE
		// | SWT.NO_BACKGROUND);
		// overviewShell.setText("Overview Shell");
		// overviewShell.setLayout(new FillLayout());
		// LightweightSystem overviewLWS = new LightweightSystem(overviewShell);
		// overviewLWS.setContents(createThumbnail(getContents()));
		// overviewShell.setSize(200, 200);

		shell.open();
		// overviewShell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
				// overviewShell.dispose();
			}
		}
	}

	protected static Figure createThumbnail(Figure source) {
		Thumbnail thumbnail = new Thumbnail();
		thumbnail.setBorder(new GroupBoxBorder("Overview Figure")); //$NON-NLS-1$
		thumbnail.setSource(source);
		return thumbnail;
	}

	protected static Figure getContents() {
		if (contents == null) {
			contents = createContents();
		}
		return contents;
	}

	private static Figure createContents() {
		Figure contents = new Figure();
		XYLayout layout = new XYLayout();
		contents.setLayoutManager(layout);

		Font classFont = new Font(null, "Arial", 12, SWT.BOLD); //$NON-NLS-1$
		Label classLabel1 = new Label("Table", //$NON-NLS-1$
				new Image(null, ZoomExample.class.getResourceAsStream("images/class_obj.gif"))); //$NON-NLS-1$
		classLabel1.setFont(classFont);

		Label classLabel2 = new Label("Column", //$NON-NLS-1$
				new Image(null, ZoomExample.class.getResourceAsStream("images/class_obj.gif"))); //$NON-NLS-1$
		classLabel2.setFont(classFont);

		final UMLClassFigure classFigure = new UMLClassFigure(classLabel1);
		final UMLClassFigure classFigure2 = new UMLClassFigure(classLabel2);

		Label attribute1 = new Label("columns: Column[]", //$NON-NLS-1$
				new Image(null, UMLClassFigure.class.getResourceAsStream("images/field_private_obj.gif"))); //$NON-NLS-1$
		Label attribute2 = new Label("rows: Row[]", //$NON-NLS-1$
				new Image(null, UMLClassFigure.class.getResourceAsStream("images/field_private_obj.gif"))); //$NON-NLS-1$
		Label attribute3 = new Label("columnID: int", //$NON-NLS-1$
				new Image(null, UMLClassFigure.class.getResourceAsStream("images/field_private_obj.gif"))); //$NON-NLS-1$
		Label attribute4 = new Label("items: List", //$NON-NLS-1$
				new Image(null, UMLClassFigure.class.getResourceAsStream("images/field_private_obj.gif"))); //$NON-NLS-1$

		classFigure.getAttributesCompartment().add(attribute1);
		classFigure.getAttributesCompartment().add(attribute2);
		classFigure2.getAttributesCompartment().add(attribute3);
		classFigure2.getAttributesCompartment().add(attribute4);

		Label method1 = new Label("getColumns(): Column[]", //$NON-NLS-1$
				new Image(null, UMLClassFigure.class.getResourceAsStream("images/methpub_obj.gif"))); //$NON-NLS-1$
		Label method2 = new Label("getRows(): Row[]", //$NON-NLS-1$
				new Image(null, UMLClassFigure.class.getResourceAsStream("images/methpub_obj.gif"))); //$NON-NLS-1$
		Label method3 = new Label("getColumnID(): int", //$NON-NLS-1$
				new Image(null, UMLClassFigure.class.getResourceAsStream("images/methpub_obj.gif"))); //$NON-NLS-1$
		Label method4 = new Label("getItems(): List", //$NON-NLS-1$
				new Image(null, UMLClassFigure.class.getResourceAsStream("images/methpub_obj.gif"))); //$NON-NLS-1$

		classFigure.getMethodsCompartment().add(method1);
		classFigure.getMethodsCompartment().add(method2);
		classFigure2.getMethodsCompartment().add(method3);
		classFigure2.getMethodsCompartment().add(method4);

		layout.setConstraint(classFigure, new Rectangle(10, 10, -1, -1));
		layout.setConstraint(classFigure2, new Rectangle(150, 150, -1, -1));

		contents.add(classFigure);
		contents.add(classFigure2);

		return contents;
	}

}
