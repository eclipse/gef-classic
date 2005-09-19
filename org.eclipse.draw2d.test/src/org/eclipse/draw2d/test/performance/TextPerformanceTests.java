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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import org.eclipse.test.performance.Dimension;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.InlineFlow;
import org.eclipse.draw2d.text.TextFlow;


public class TextPerformanceTests 
	extends BasePerformanceTestCase
{
	
private FlowPage page;
private TextFlow text1, text2, text3, text4, text5;

private void layoutAndPaint() {
	Image img = new Image(null, 750, 1500);
	GC gc = new GC(img);
	Graphics graphics = new SWTGraphics(gc);
	int[] widths = new int[] {100, 200, 250, 300, 400, 500, 600, 700, 750, 800, 900, 1000, 800, 900, 1000};
	
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

protected void setUp() throws Exception {
	super.setUp();
	page = new FlowPage();
	page.setFont(TAHOMA);
	text1 = new TextFlow();
	InlineFlow flow = new InlineFlow();
	text2 = new TextFlow();
	text3 = new TextFlow();
	InlineFlow flow2 = new InlineFlow();
	text4 = new TextFlow();
	text5 = new TextFlow();
	page.add(text1);
	page.add(flow);
	flow.add(text2);
	flow.add(text3);
	flow.add(flow2);
	flow2.add(text4);
	flow.add(text5);
}

protected void tearDown() throws Exception {
	text1 = text2 = text3 = text4 = null;
	page = null;
	super.tearDown();
}

//public void testBidiLayout() {
//	tagAsGlobalSummary("Laying out Bidi text", new Dimension[] {Dimension.CPU_TIME, Dimension.WORKING_SET});
//	
//	text1.setText("This test mainly checks to see how long does ParagraphTextLayout" +
//			" takes to lay out a text flow.  It will also test the performance of " +
//			"FlowUtilities.wrapFragmentInContext().  BiDi will be enabled for " +
//			"this test.  Following is some Arabic text.  \u0637\u0638\u0639");
//	text2.setText("\u0634 \u0637\u0638\u0639\u0640 \"it is 123, 456, ok.\"  ");
//	text3.setText("\u0634\u0637 \u0635\u0639\u0633\u0640 \"it is a car!x\" " +
//			"\u0634\u0637\u0635 \u0639\u0633\u0640.  ");
//	text4.setText("Since there are multiple TextFlows involved, it should test a " +
//			"few more code-paths in the above-mentioned methods.  Let's throw in " +
//			"some Chinese characters too: u7325\u7334\u7329u7325\u7334\u7329u7325" +
//			"\u7334\u7329u7325\u7334\u7329u7325\u7334\u7329u7325\u7334\u7329.");
//	
//	layoutAndPaint();
//}

public void testTextLayoutAndPainting() {
	tagAsGlobalSummary("Text Layout and Painting", Dimension.CPU_TIME);
	
	text1.setText("Following are some extracts from Douglas Adams' \"The Hitchhiker's Guide to " +
			"the Galaxy\" series of books.\n\nIn the beginning the Universe was created." +
			"  This has made a lot of people very angry and been widely regarded as a bad " +
			"move.\n\nThe knack of flying is learning how to throw yourself at the " +
			"ground and miss.\n\nMy doctor says that I have a malformed public-duty " +
			"gland and a natural deficiency in moral fibre and that I am therefore " +
			"excused from saving uni");
	text2.setText("verses.  \n\nHe hoped and prayed that there wasn't an after-life.  Then he " +
			"realized there was a contradiction involved here and merely hoped that " +
			"there wasn't an afterlife.\n\nAnyone who is capable of getting themselves " +
			"made President should on no account be allowed to do the job." +
			"\n\nThere is a theory which states that if ever anybody " +
			"discovers exactly what the Universe is for and why it is here, it will " +
			"instantly disappear and be replaced by something even more bizarre and " +
			"inexplicable. There is another theory which states that this has already " +
			"happened.  Far o");
	text3.setText("ut in the un-charted backwaters of the un-fashionable end of the" +
			" Western Spiral arm of the Galaxy lies a small unregarded yellow sun.\n\n" +
			"In those days spirits were brave, the stakes were high, men were real men, " +
			"women were real women and small furry creatures from Alpha Centauri were " +
			"real small furry creatures from Alpha Centauri.");
	text4.setText("The ships hung in the sky in much the same way that bricks don't.\n" +
			"\nLet's throw in some Chinese characters too: u7325\u7334\u7329u7325" +
			"\u7334\u7329u7325\u7334\u7329\u7325\u7334\u7329u7325\u7334\u7329u7325" +
			"\u7334\u7329u7325\u7334\u7329\u7325\u7334\u7329u7325\u7334\u7329u7325");
	text5.setText("\u7334\u7329. \n\nFor thousands more years, the mighty ships tore " +
			"across the empty wastes of space and finally dived screaming onto the " +
			"first planet they came across -- which happened to be the Earth -- where " +
			"due to a terrible miscalculation of scale the entire battle fleet was " +
			"accidentally swallowed by a small dog.");

	layoutAndPaint();
}

}