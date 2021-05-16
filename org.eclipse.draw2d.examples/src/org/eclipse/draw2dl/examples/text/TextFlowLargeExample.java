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
package org.eclipse.draw2dl.examples.text;

import org.eclipse.draw2dl.PositionConstants;
import org.eclipse.draw2dl.text.FlowPage;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;

/**
 * The example is used to build large draw2d.text flow documents to benchmark the layout
 * performance as the document changes.  In this case, we allow fake typing of text.
 * @author hudsonr
 */

public class TextFlowLargeExample {

static int KEYS_TYPED = 0;
static boolean PERFORMANCE = false;
static org.eclipse.draw2dl.FigureCanvas canvas;
static org.eclipse.draw2dl.text.TextFlow target;
static org.eclipse.draw2dl.text.FlowPage page;
static Font
	regularFont = new Font(Display.getDefault(), "Arial", 15, SWT.NORMAL),
	boldFont = new Font(Display.getDefault(), "Comic Sans MS", 16, SWT.BOLD);

public static void main(String[] args) {
	Display d = Display.getDefault();
	Shell shell = new Shell(d);
	shell.setLayout(new GridLayout());

	canvas = new org.eclipse.draw2dl.FigureCanvas(shell);
	canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
	canvas.setVerticalScrollBarVisibility(canvas.ALWAYS);
	canvas.getViewport().setContentsTracksWidth(true);
	shell.open();

	canvas.addKeyListener(new KeyAdapter(){
		public void keyPressed(KeyEvent e){
			addText(e.character);
		}
	});

	page = new FlowPage();
	populatePage();
	canvas.setContents(page);

	while (!shell.isDisposed())
		if (!d.readAndDispatch())
			d.sleep();
}

static protected void addText(char c){
	if ((c <= 'Z' && c >='A') ||
	    (c <= 'z' && c >='a') ||
	    (c == ' '))
	if (target != null){
		target.setText(target.getText()+c);
		if (PERFORMANCE && (KEYS_TYPED % 10 == 0)){
//			System.out.println(KEYS_TYPED + " keys typed " + FlowPage.VALIDATIONS + " paints and layouts");
		}
		KEYS_TYPED++;
	}
}

static public void populatePage(){

	target = new org.eclipse.draw2dl.text.TextFlow("Normal text.");
	target.setToolTip(new org.eclipse.draw2dl.Label("This is a Tooltip"));
	page.add(target);
	page.setOpaque(true);
	page.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.white);

	for (int i=0; i< 20; i++){
		org.eclipse.draw2dl.text.BlockFlow bf = new org.eclipse.draw2dl.text.BlockFlow();
		page.add(bf);
		if (i == 0)
			bf.add(target);
		
		org.eclipse.draw2dl.text.FlowFigure ff = new org.eclipse.draw2dl.text.InlineFlow();
		ff.add(new org.eclipse.draw2dl.text.TextFlow("This is the first small sentence. "));
		bf.add(ff);
		
		org.eclipse.draw2dl.text.FlowFigure inline = new org.eclipse.draw2dl.text.InlineFlow();
		TextFlowFactory.addSentences(inline,4);
		ff.add(inline);
		
		org.eclipse.draw2dl.text.BlockFlow block = new org.eclipse.draw2dl.text.BlockFlow();
		block.setHorizontalAligment(PositionConstants.CENTER);
		TextFlowFactory.addFontSizes(block, 5, 25);
		page.add(block);
	}
}

}