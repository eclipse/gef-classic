/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import org.eclipse.swt.graphics.Font;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowContext;
import org.eclipse.draw2d.text.FlowFigure;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.InlineFlow;
import org.eclipse.draw2d.text.TextFlow;

/**
 * @author Pratik Shah
 * @author Randy Hudson
 * @since 3.1
 */
public class LookAheadTest
	extends BaseTestCase
{

static final Font TIMES_ROMAN = new Font(null, "Times", 14, 0);
TextFlow simpleText;

private int[] width;
private FlowPage flowpage;
private TextFlow heading1;
private BlockFlow paragraph1;
private TextFlow p1text1;
private InlineFlow p1inline;
private InlineFlow p1NestedInline;
private TextFlow p1text2;
private BlockFlow paragraph2;
private TextFlow heading2;
private TextFlow p2text1;
private TextFlow p2emptyText;
private TextFlow p2text2;
private TextFlow p1text3;

void assertLineBreakFound(boolean b) {
	assertTrue("Line break should have been found", b);
}

void assertLineBreakNotFound(boolean b) {
	assertFalse("Line break should not be found.", b);
}

void assertLookaheadMatchesString(int[] width, String expected) {
	assertEquals("Lookahead width did not match expected string:\""
			+ expected +"\"", getWidth(expected), width[0]);
}

private int getWidth(String s) {
	return FigureUtilities.getStringExtents(s, TIMES_ROMAN).width;
}

protected void setUp() throws Exception {
	simpleText = new TextFlow();
	simpleText.setFont(TIMES_ROMAN);
	width = new int[1];
	
	flowpage = new FlowPage();
	flowpage.setFont(TIMES_ROMAN);

	flowpage.add(heading1 = new TextFlow("Heading 1"));
	flowpage.add(paragraph1 = new BlockFlow());
	flowpage.add(heading2 = new TextFlow("Heading 2"));	
	flowpage.add(paragraph2 = new BlockFlow());

	paragraph1.add(p1text1 = new TextFlow("The quick "));
	paragraph1.add(p1inline = new InlineFlow());
		p1inline.add(p1NestedInline = new InlineFlow());
			p1NestedInline.add(p1text2 = new TextFlow("brown fox"));
	paragraph1.add(p1text3 = new TextFlow("jumped over"));
	
	paragraph2.add(p2text1 = new TextFlow("Hel"));
	paragraph2.add(p2emptyText = new TextFlow(""));
	paragraph2.add(new InlineFlow());
	paragraph2.add(p2text2 = new TextFlow("lo"));
}

public void textContainerLeadingWord() {
	p1inline.addLeadingWordRequirements(width);
	assertLookaheadMatchesString(width, "brown");
}

public void testBlockLeadingWord() {
	paragraph1.addLeadingWordRequirements(width);
	assertEquals("Blocks should have no leading word", 0, width[0]);
}

FlowContext getContext(FlowFigure figure) {
	return (FlowContext)figure.getParent().getLayoutManager();
}

int getFollow(FlowFigure figure) {
	getContext(figure).getWidthLookahead(figure, width);
	return width[0];
}

public void testContextLookaheadPrecedingInline() {
	assertEquals("Context lookahead into inline flow failed",
			getFollow(p1text1), getWidth("brown"));
}

public void testContextLookaheadFromNested() {
	assertEquals("Context lookahead from nested inline textflow failed",
			getFollow(p1text2), getWidth("jumped"));
}

public void testContextLookaheadAtEndOfBlock() {
	assertTrue("Last figure in a block should have no lookahead",
			getFollow(p1text3) == 0);
}

public void testContextLookaheadPastEmptyString() {
	assertEquals("Context lookahead over empty TextFlow failed",
			getFollow(p2text1), getWidth("lo"));
}

public void testContextChineseCharLookahead() {
	p1text2.setText("\u7325abcdef");
	assertTrue("Chinese characters should have no lookahead",
			getFollow(p1text1) == 0);
}

public void testContextHyphenLookahead() {
	p1text2.setText("-abc");
	assertEquals("Context lookahead should be hyphen character",
			getFollow(p1text1), getWidth("-"));
}

public void testSingleLetter() {
	simpleText.setText("a");
	assertLineBreakNotFound(simpleText.addLeadingWordRequirements(width));
	assertLookaheadMatchesString(width, "a");
}

public void testSingleSpace() {
	simpleText.setText(" ");
	assertTrue("Line break should have been found", simpleText.addLeadingWordRequirements(width));
	assertTrue("test21 failed", width[0] == 0);
	
	simpleText.setText("\u7325");
	width[0] = 0;
	assertTrue("test22 failed", simpleText.addLeadingWordRequirements(width));
	assertTrue("test23 failed", width[0] == 0);
	
	simpleText.setText("-");
	width[0] = 0;
	assertTrue("test24 failed", simpleText.addLeadingWordRequirements(width));
	assertTrue("test25 failed", width[0] == getWidth("-"));
	
	simpleText.setText("ombudsman");
	width[0] = 0;
	assertTrue("test26 failed", !simpleText.addLeadingWordRequirements(width));
	assertTrue("test27 failed", width[0] == getWidth("ombudsman"));
	
	simpleText.setText("space bar");
	width[0] = 0;
	assertTrue("test28 failed", simpleText.addLeadingWordRequirements(width));
	assertTrue("test29 failed", width[0] == getWidth("space"));
	
	simpleText.setText("endsInSpace ");
	width[0] = 0;
	assertTrue("test30 failed", simpleText.addLeadingWordRequirements(width));
	assertTrue("test31 failed", width[0] == getWidth("endsInSpace"));
	
	simpleText.setText("endsInHyphen-");
	width[0] = 0;
	assertTrue("test32 failed", simpleText.addLeadingWordRequirements(width));
	assertTrue("test33 failed", width[0] == getWidth("endsInHyphen-"));
	
	simpleText.setText("co-operate");
	width[0] = 0;
	assertTrue("test34 failed", simpleText.addLeadingWordRequirements(width));
	assertTrue("test35 failed", width[0] == getWidth("co-"));
	
	simpleText.setText("ab\u7325");
	width[0] = 0;
	assertTrue("test36 failed", simpleText.addLeadingWordRequirements(width));
	assertTrue("test37 failed", width[0] == getWidth("ab"));
	
	simpleText.setText("hey, man.");
	width[0] = 0;
	assertTrue("test38 failed", simpleText.addLeadingWordRequirements(width));
	assertTrue("test39 failed", width[0] == getWidth("hey,"));
}

}
