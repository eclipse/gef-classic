/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.examples.text;

import org.eclipse.draw2dl.*;
import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2dl.text.FlowAdapter;
import org.eclipse.draw2dl.text.FlowPage;
import org.eclipse.draw2dl.text.TextFlow;

public class TextSurroundingFigureExample
{
	
public static void main(String[] args) {
	Display d = Display.getDefault();
	Shell shell = new Shell(d);
	StackLayout layout = new StackLayout();
	shell.setLayout(layout);

	org.eclipse.draw2dl.FigureCanvas canvas = new FigureCanvas(shell);
	layout.topControl = canvas;
	canvas.getViewport().setContentsTracksWidth(true);

	FlowPage page = new FlowPage();
	populatePage(page);
	canvas.setContents(page);

	shell.setSize(300, 300);
	shell.open();
	while (!shell.isDisposed())
		if (!d.readAndDispatch())
			d.sleep();
}

private static void populatePage(FlowPage page) {
	page.setOrientation(SWT.LEFT_TO_RIGHT);
	
	TextFlow textFlow1 = new TextFlow();
	textFlow1.setText("This is the text on the left side, and \u0634\u0637\u0635.  There has to be a lot of text here to see the first proxy wrap.  So, here we go.");
	page.add(textFlow1);

	FlowAdapter proxy1 = new FlowAdapter();
	proxy1.setBorder(new LineBorder(org.eclipse.draw2dl.ColorConstants.orange, 3));
	proxy1.setLayoutManager(new ToolbarLayout());
	org.eclipse.draw2dl.RoundedRectangle rect = new RoundedRectangle();
	rect.setCornerDimensions(new Dimension(25,25));
	rect.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.green);
	rect.setForegroundColor(org.eclipse.draw2dl.ColorConstants.black);
	rect.setPreferredSize(100, 40);
	proxy1.add(rect);
	Shape shape = new Triangle();
	shape.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.red);
	shape.setForegroundColor(org.eclipse.draw2dl.ColorConstants.black);
	shape.setPreferredSize(80, 80);
	proxy1.add(shape);
	shape = new Ellipse();
	shape.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.blue);
	shape.setForegroundColor(ColorConstants.black);
	shape.setPreferredSize(70, 70);	
	proxy1.add(shape);
	page.add(proxy1);
	
	TextFlow textFlow2 = new TextFlow();
	textFlow2.setText("This is the right side text.");
	page.add(textFlow2);
	
	FlowAdapter proxy2 = new FlowAdapter();
	proxy2.setBorder(proxy1.getBorder());
	proxy2.setLayoutManager(new org.eclipse.draw2dl.StackLayout());
	TextFlow flow = new TextFlow();
	flow.setText("Text inside a proxy figure with BiDi: \u0634\u0637\u0635 \u0639\u0633\u0640 \u0632\u0638\u0635.");
	FlowPage innerPage = new FlowPage();
	innerPage.setOrientation(SWT.RIGHT_TO_LEFT);
	innerPage.add(flow);
	proxy2.add(innerPage);
	page.add(proxy2);
}

}