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
package org.eclipse.draw2d.test.performance;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import org.eclipse.test.performance.Dimension;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowFigure;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.InlineFlow;
import org.eclipse.draw2d.text.TextFlow;


public class TextPerformanceTests 
	extends BasePerformanceTestCase
{
	
private Font[] fonts;
private FlowPage page;

private void addFontSizes(Figure parent){
	for (int i = 0; i < fonts.length; i++){
		TextFlow tf = new TextFlow(" " + Integer.toString(i) + " pt Font.");
		tf.setForegroundColor(ColorConstants.red);
		tf.setFont(fonts[i]);
		parent.add(tf);
	}
}

private void addSentences(IFigure parent, int count, boolean addBidi){
	for (int i = 0; i < count; i++) {
		if (addBidi) {
			parent.add(new TextFlow("-10% \u0634\u0635\u0636. "));
			parent.add(new TextFlow("\u0634\u0635\u0636\u0637 ~~~23%%% \u0637\u0638\u0639\u0640 abc. "));
			parent.add(new TextFlow("\u0634 \u0637\u0638\u0639\u0640 \"it is 123, 456, ok.\" "));
			parent.add(new TextFlow("\u0634\u0637\u0638 1*5 1-5 1/5 1+5 "));
			parent.add(new TextFlow("\u0634 \u0637 \u2029 abcd. "));
			parent.add(new TextFlow("\u0634\u0637\u0635 \u0639\u0633\u0640 \u0632\u0638\u0635 'he said \"car \u0640\u0637\u0633\u0639\u0635 \u0635\u0632\u0636\"'? \u0634\u0637\u0635"));
			parent.add(new TextFlow("\u0639\u0633\u0640. "));
			parent.add(new TextFlow("car is \u0634\u0637\u0635 \u0639\u0633\u0640 in arabic. "));
			parent.add(new TextFlow("\u0634\u0637\u0635 \u0635\u0639 the car \u0633\u0640 \u0638\u0635\u0634\u0637\u0635\u0639\u0633. "));
			parent.add(new TextFlow("he said \"\u0634\u0637 \u0635\u0639 (123,456), \u0632\u0638.\" "));
			parent.add(new TextFlow("<\u0634123>shalom</\u0634123>. "));
			parent.add(new TextFlow("<h123>\u0637\u0635\u0639\u0633\u0640\u0632</h123>. "));
			parent.add(new TextFlow("\u0634\u0637 \u0635\u0639\u0633\u0640 \"it is a car!\" \u0634\u0637\u0635 \u0639\u0633\u0640. "));
			parent.add(new TextFlow("\u202d\u0634\u0637\u0635\u0639\u0633. "));
			parent.add(new TextFlow("\u202efirst character is RLO. "));
			parent.add(new TextFlow("\u200ffirst character is RLM. "));
		} else {
			parent.add(new TextFlow(" One two three four five six seven -- eight nine ten -- eleven twelve thirteen, \"fourteen fifteen sixteen.\" Seventeen eighteen nineteen twenty twenty-one twenty-two twenty-three twenty-four twenty-five twenty-six twenty-seven twenty-eight twenty-nine thirty thirty-one thirty-two thirty-three thirty-four thirty-five thirty-six thirty-seven thirty-eight thirty-nine forty forty-one forty-two forty-three forty-four forty-five forty-six forty-seven forty-eight forty-nine fifty."));
			parent.add(new TextFlow(" Chinese characters: \u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325"));
			parent.add(new TextFlow(" This text has been cut mid-sen"));
			parent.add(new TextFlow("tence: supercalifraglistic"));
			parent.add(new TextFlow("expialidocious."));
		}
	}
}

private void layoutAndPaint() {
	Image img = new Image(null, 500, 2500);
	GC gc = new GC(img);
	Graphics graphics = new SWTGraphics(gc);
	int[] widths = new int[] {400, 450, 500, 400, 450, 500};
	
	int warmupRuns = getWarmupRuns();
	int measuredRuns = getMeasuredRuns();	
	for (int i = 0; i < warmupRuns + measuredRuns; i++) {
		if (i >= warmupRuns)
			startMeasuring();
		
		for (int j = 0; j < widths.length; j++) {
			page.setSize(page.getPreferredSize(widths[j], -1));
			page.validate();
			page.paint(graphics);
		}
		
		if (i >= warmupRuns)
			stopMeasuring();
	}
	commitMeasurements();
	graphics.dispose();
	gc.dispose();
	img.dispose();
	assertPerformance();
}

private void populatePage(boolean addBidi, int numOfBlocks){
	for (int i = 0; i < numOfBlocks; i++){
		BlockFlow bf = new BlockFlow();
		page.add(bf);
		
		FlowFigure ff = new InlineFlow();
		ff.add(new TextFlow("This is the first small sentence."));
		bf.add(ff);
		
		FlowFigure inline = new InlineFlow();
		addSentences(inline, 3, addBidi);
		ff.add(inline);
		
		if (!addBidi) {
			BlockFlow block = new BlockFlow();
			block.setHorizontalAligment(PositionConstants.CENTER);
			addFontSizes(block);
			page.add(block);
		}
	}
}

protected void setUp() throws Exception {
	super.setUp();
	page = new FlowPage();
	page.setFont(TAHOMA);
}

protected void tearDown() throws Exception {
	page = null;
	super.tearDown();
}

public void testBidiLayout() {
	tagAsGlobalSummary("Bidi", Dimension.CPU_TIME);
	populatePage(true, 1);	
	layoutAndPaint();
}

public void testTextLayoutAndPainting() {
	tagAsGlobalSummary("Text Layout and Painting", Dimension.CPU_TIME);
	
	fonts = new Font[20];
	for (int i = 0; i < 20; i++) {
		fonts[i] = new Font(null, "Tahoma", i * 2 + 6, SWT.NORMAL);
	}
	
	populatePage(false, 20);
	layoutAndPaint();
	
	for (int i = 0; i < fonts.length; i++) {
		fonts[i].dispose();
	}
	fonts = null;
}

}