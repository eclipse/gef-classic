/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * @author hudsonr
 */
public abstract class AbstractExample {

protected static final Font COURIER = new Font(null, "Courier", 9, 0);//$NON-NLS-1$
protected static final Font BOLD = new Font(null, "Helvetica", 10, SWT.BOLD);//$NON-NLS-1$
protected static final Font ITALICS = new Font(null, "Helvetica", 10, SWT.ITALIC);//$NON-NLS-1$
protected static final Font HEADING_1 = new Font(null, "Helvetica", 15, SWT.BOLD);//$NON-NLS-1$
private FigureCanvas fc;
protected IFigure contents;

protected Shell shell;

protected void run(){
	Display d = Display.getDefault();
	shell = new Shell(d);
	String appName = getClass().getName();
	appName = appName.substring(appName.lastIndexOf('.')+1);
	shell.setText(appName);
	shell.setLayout(new GridLayout(2, false));
	setFigureCanvas(new FigureCanvas(shell));
	getFigureCanvas().setContents(contents = getContents());
	getFigureCanvas().getViewport().setContentsTracksHeight(true);
	getFigureCanvas().getViewport().setContentsTracksWidth(true);
	getFigureCanvas().setLayoutData(new GridData(GridData.FILL_BOTH));	
	hookShell();
	shell.pack();
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

protected abstract IFigure getContents();

protected FigureCanvas getFigureCanvas(){
	return fc;
}

protected void hookShell(){}

protected void setFigureCanvas(FigureCanvas canvas){
	this.fc = canvas;
}

}
