/*******************************************************************************
 * Copyright (c) 2008, 2023 IBM Corporation and others.
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
package org.eclipse.draw2d.examples.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Label;

public class InheritBackgroundExample {

	static Shell shell;
	static Image bg;

	static {
		bg = new Image(null, 10, 800);
		GC gc = new GC(bg);
		gc.setForeground(ColorConstants.white);
		gc.setBackground(ColorConstants.lightGreen);
		gc.fillGradientRectangle(0, 0, 300, 400, true);
		gc.setForeground(ColorConstants.lightGreen);
		gc.setBackground(ColorConstants.white);
		gc.fillGradientRectangle(0, 400, 300, 400, true);
		gc.dispose();
	}

	public static void main(String[] args) {
		Display d = Display.getDefault();
		shell = new Shell(d, SWT.SHELL_TRIM);
		shell.setText("Figure Canvas inheriting background from parent Composite"); //$NON-NLS-1$
		shell.setLayout(new GridLayout(2, false));
		shell.setBackgroundImage(bg);
		shell.setBackgroundMode(SWT.INHERIT_FORCE);

		FigureCanvas canvas;

		canvas = createCanvas(shell,
				SWT.DOUBLE_BUFFERED | SWT.BORDER | SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL,
				"Paints Background"); //$NON-NLS-1$
		canvas.setBackground(ColorConstants.orange);

		canvas = createCanvas(shell,
				SWT.DOUBLE_BUFFERED | SWT.BORDER | SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL,
				"Inherits Background"); //$NON-NLS-1$
		canvas.getLightweightSystem().getRootFigure().setOpaque(false);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}

	private static FigureCanvas createCanvas(Composite parent, int style, String text) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setText(text);
		parent = group;
		group.setLayout(new FillLayout());

		FigureCanvas canvas = new FigureCanvas(style, parent);
		canvas.setContents(new Label("""
				This is a figure canvas
				with a label.  The background
				is either painted by the root
				figure, or by the operating system.
				Note that vertical scrolling may not be
				as fast when the background from the
				parent is inherited.""")); //$NON-NLS-1$
		return canvas;
	}

}
