/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.draw2d.examples.text;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.examples.AbstractExample;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;

public class BidiBlockExample extends AbstractExample {

public static void main(String[] args) {
	new BidiBlockExample().run();
}

// The backwards figure canvas for bidi
FigureCanvas cf;

protected IFigure getContents() {
	FlowPage page = new FlowPage();

	BlockFlow p1 = new BlockFlow();
	page.add(p1);
	p1.add(new TextFlow("This is some text."));

	return page;
}

protected void run() {
	Display d = Display.getDefault();
	shell = new Shell(d);
	String appName = getClass().getName();
	appName = appName.substring(appName.lastIndexOf('.') + 1);
	shell.setText(appName);
	shell.setLayout(new GridLayout(2, true));

	fc = new FigureCanvas(shell);
	fc.setContents(getContents());
	cf = new FigureCanvas(shell, SWT.RIGHT_TO_LEFT);
	cf.setContents(getContents());

	
	fc.getViewport().setContentsTracksWidth(true);
	cf.getViewport().setContentsTracksWidth(true);

	fc.setLayoutData(new GridData(GridData.FILL_BOTH));
	cf.setLayoutData(new GridData(GridData.FILL_BOTH));

	shell.pack();
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

}
