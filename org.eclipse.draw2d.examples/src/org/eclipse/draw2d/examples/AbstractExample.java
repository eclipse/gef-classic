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
package org.eclipse.draw2d.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;

/**
 * A baseclass for draw2d examples.
 *
 * @author hudsonr
 */
public abstract class AbstractExample {

	private static final String COURIER2 = "Courier"; //$NON-NLS-1$
	private static final String HELVETICA = "Helvetica"; //$NON-NLS-1$

	protected static final Font COURIER = new Font(null, COURIER2, 9, 0);
	protected static final Font BOLD = new Font(null, HELVETICA, 10, SWT.BOLD);
	protected static final Font ITALICS = new Font(null, HELVETICA, 10, SWT.ITALIC);
	protected static final Font HEADING_1 = new Font(null, HELVETICA, 15, SWT.BOLD);

	private FigureCanvas fc;
	private IFigure contents;

	private Shell shell;

	protected void run() {
		Display d = Display.getDefault();
		shell = new Shell(d, getShellStyle());
		String appName = getClass().getName();
		appName = appName.substring(appName.lastIndexOf('.') + 1);
		shell.setText(appName);
		shell.setLayout(new GridLayout(2, false));
		setFigureCanvas(new FigureCanvas(shell));
		contents = createContents();
		getFigureCanvas().setContents(contents);
		getFigureCanvas().getViewport().setContentsTracksHeight(true);
		getFigureCanvas().getViewport().setContentsTracksWidth(true);
		getFigureCanvas().setLayoutData(new GridData(GridData.FILL_BOTH));
		hookShell(shell);
		sizeShell();
		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}

	@SuppressWarnings("static-method") // allow children to provide their own shell style
	protected int getShellStyle() {
		return SWT.SHELL_TRIM;
	}

	protected void sizeShell() {
		shell.pack();
	}

	protected abstract IFigure createContents();

	protected FigureCanvas getFigureCanvas() {
		return fc;
	}

	protected IFigure getContents() {
		return contents;
	}

	public Shell getShell() {
		return shell;
	}

	protected void hookShell(Shell shell) {
	}

	protected void setFigureCanvas(FigureCanvas canvas) {
		this.fc = canvas;
	}

}
