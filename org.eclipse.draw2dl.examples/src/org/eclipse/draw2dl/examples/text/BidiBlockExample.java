/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.draw2dl.examples.text;

import org.eclipse.draw2dl.FigureCanvas;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.PositionConstants;
import org.eclipse.draw2dl.examples.AbstractExample;
import org.eclipse.draw2dl.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2dl.text.BlockFlow;
import org.eclipse.draw2dl.text.FlowPage;

public class BidiBlockExample extends AbstractExample {

public static void main(String[] args) {
	new BidiBlockExample().run();
}

// The backwards figure canvas for bidi
protected org.eclipse.draw2dl.FigureCanvas cf;
protected String s = "\u0634\u0635\u062c\u062d \u0630\u0628\u063a and some english text.";

protected IFigure getContents() {
	FlowPage page = new FlowPage();

	BlockFlow para = new BlockFlow();
	page.add(para);
	para.setOrientation(SWT.LEFT_TO_RIGHT);
	para.setHorizontalAligment(org.eclipse.draw2dl.PositionConstants.LEFT);
	para.add(new org.eclipse.draw2dl.text.TextFlow(s));
	
	para = new BlockFlow();
	page.add(para);
	para.setOrientation(SWT.LEFT_TO_RIGHT);
	para.setHorizontalAligment(org.eclipse.draw2dl.PositionConstants.RIGHT);
	para.add(new org.eclipse.draw2dl.text.TextFlow(s));
	
	para = new BlockFlow();
	page.add(para);
	para.setOrientation(SWT.LEFT_TO_RIGHT);
	para.setHorizontalAligment(org.eclipse.draw2dl.PositionConstants.ALWAYS_LEFT);
	para.add(new org.eclipse.draw2dl.text.TextFlow(s));

	para = new BlockFlow();
	page.add(para);
	para.setOrientation(SWT.LEFT_TO_RIGHT);
	para.setHorizontalAligment(org.eclipse.draw2dl.PositionConstants.ALWAYS_RIGHT);
	para.add(new org.eclipse.draw2dl.text.TextFlow(s));

	para = new BlockFlow();
	page.add(para);
	para.setOrientation(SWT.LEFT_TO_RIGHT);
	para.add(new org.eclipse.draw2dl.text.TextFlow(s));

	para = new BlockFlow();
	page.add(para);
	para.setOrientation(SWT.RIGHT_TO_LEFT);
	para.setHorizontalAligment(org.eclipse.draw2dl.PositionConstants.LEFT);
	para.add(new org.eclipse.draw2dl.text.TextFlow(s));

	para = new BlockFlow();
	page.add(para);
	para.setOrientation(SWT.RIGHT_TO_LEFT);
	para.setHorizontalAligment(org.eclipse.draw2dl.PositionConstants.RIGHT);
	para.add(new org.eclipse.draw2dl.text.TextFlow(s));

	para = new BlockFlow();
	page.add(para);
	para.setOrientation(SWT.RIGHT_TO_LEFT);
	para.setHorizontalAligment(org.eclipse.draw2dl.PositionConstants.ALWAYS_LEFT);
	para.add(new org.eclipse.draw2dl.text.TextFlow(s));

	para = new BlockFlow();
	page.add(para);
	para.setOrientation(SWT.RIGHT_TO_LEFT);
	para.setHorizontalAligment(PositionConstants.ALWAYS_RIGHT);
	para.add(new org.eclipse.draw2dl.text.TextFlow(s));

	para = new BlockFlow();
	page.add(para);
	para.setOrientation(SWT.RIGHT_TO_LEFT);
	para.add(new TextFlow(s));

	return page;
}

protected void run() {
	Display d = Display.getDefault();
	shell = new Shell(d);
	String appName = getClass().getName();
	appName = appName.substring(appName.lastIndexOf('.') + 1);
	shell.setText(appName);
	shell.setLayout(new GridLayout(2, true));

	fc = new org.eclipse.draw2dl.FigureCanvas(shell);
	fc.setContents(getContents());
	cf = new FigureCanvas(shell, SWT.RIGHT_TO_LEFT);
	cf.setContents(getContents());
	
	fc.getViewport().setContentsTracksWidth(true);
	cf.getViewport().setContentsTracksWidth(true);

	fc.setLayoutData(new GridData(GridData.FILL_BOTH));
	cf.setLayoutData(new GridData(GridData.FILL_BOTH));
	
	cf.getViewport().setVerticalRangeModel(fc.getViewport().getVerticalRangeModel());

	shell.pack();
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

}