/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
package org.eclipse.draw2d.examples;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Label;

public class HelloWorld {

	public static void main(String[] args) {

		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setLayout(new FillLayout());

		FigureCanvas canvas = new FigureCanvas(shell);
		canvas.setContents(new Label("Hello World")); //$NON-NLS-1$

		shell.setText("Draw2d"); //$NON-NLS-1$
		shell.setSize(200, 100);
		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}

	}

}
