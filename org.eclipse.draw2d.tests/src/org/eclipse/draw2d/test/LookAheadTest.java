/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
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
package org.eclipse.draw2d.test;

import org.eclipse.swt.graphics.Font;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowContext;
import org.eclipse.draw2d.text.FlowFigure;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.InlineFlow;
import org.eclipse.draw2d.text.TextFlow;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Pratik Shah
 * @author Randy Hudson
 * @since 3.1
 */
public class LookAheadTest extends BaseTestCase {

	static final Font TIMES_ROMAN = new Font(null, "Times", 14, 0); //$NON-NLS-1$
	TextFlow simpleText;

	private int[] width;
	private BlockFlow paragraph1;
	private TextFlow p1text1;
	private InlineFlow p1inline;
	private TextFlow p1text2;
	private TextFlow p2text1;
	private TextFlow p1text3;

	static void assertLineBreakFound(boolean b) {
		assertTrue("Line break should have been found", b); //$NON-NLS-1$
	}

	static void assertLineBreakNotFound(boolean b) {
		assertFalse("Line break should not be found.", b); //$NON-NLS-1$
	}

	static void assertLookaheadMatchesString(int[] width, String expected) {
		assertEquals("Lookahead width did not match expected string:\"" + expected + "\"", getWidth(expected), //$NON-NLS-1$ //$NON-NLS-2$
				width[0]);
	}

	private static int getWidth(String s) {
		return FigureUtilities.getStringExtents(s, TIMES_ROMAN).width;
	}

	@Before
	public void setUp() throws Exception {
		simpleText = new TextFlow();
		simpleText.setFont(TIMES_ROMAN);
		width = new int[1];

		FlowPage flowpage = new FlowPage();
		flowpage.setFont(TIMES_ROMAN);

		flowpage.add(new TextFlow("Heading 1")); //$NON-NLS-1$
		flowpage.add(paragraph1 = new BlockFlow());
		flowpage.add(new TextFlow("Heading 2")); //$NON-NLS-1$
		BlockFlow paragraph2 = new BlockFlow();
		flowpage.add(paragraph2);

		paragraph1.add(p1text1 = new TextFlow("The quick ")); //$NON-NLS-1$
		paragraph1.add(p1inline = new InlineFlow());
		InlineFlow p1NestedInline = new InlineFlow();
		p1inline.add(p1NestedInline);
		p1NestedInline.add(p1text2 = new TextFlow("brown fox")); //$NON-NLS-1$
		paragraph1.add(p1text3 = new TextFlow("jumped over")); //$NON-NLS-1$

		paragraph2.add(p2text1 = new TextFlow("Hel")); //$NON-NLS-1$
		paragraph2.add(new TextFlow("")); //$NON-NLS-1$
		paragraph2.add(new InlineFlow());
		paragraph2.add(new TextFlow("lo")); //$NON-NLS-1$
	}

	@Test
	public void testContainerLeadingWord() {
		p1inline.addLeadingWordRequirements(width);
		assertLookaheadMatchesString(width, "brown"); //$NON-NLS-1$
	}

	@Test
	public void testBlockLeadingWord() {
		paragraph1.addLeadingWordRequirements(width);
		assertEquals("Blocks should have no leading word", 0, width[0]); //$NON-NLS-1$
	}

	static FlowContext getContext(FlowFigure figure) {
		return (FlowContext) figure.getParent().getLayoutManager();
	}

	int getFollow(FlowFigure figure) {
		getContext(figure).getWidthLookahead(figure, width);
		return width[0];
	}

	@Test
	public void testContextLookaheadPrecedingInline() {
		assertEquals("Context lookahead into inline flow failed", getFollow(p1text1), getWidth("brown")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void testContextLookaheadFromNested() {
		assertEquals("Context lookahead from nested inline textflow failed", getFollow(p1text2), getWidth("jumped")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void testContextLookaheadAtEndOfBlock() {
		assertTrue("Last figure in a block should have no lookahead", getFollow(p1text3) == 0); //$NON-NLS-1$
	}

	@Test
	public void testContextLookaheadPastEmptyString() {
		assertEquals("Context lookahead over empty TextFlow failed", getFollow(p2text1), getWidth("lo")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void testContextChineseCharLookahead() {
		p1text2.setText("\u7325abcdef"); //$NON-NLS-1$
		assertTrue("Chinese characters should have no lookahead", getFollow(p1text1) == 0); //$NON-NLS-1$
	}

	@Test
	public void testContextHyphenLookahead() {
		p1text2.setText("-abc"); //$NON-NLS-1$
		assertEquals("Context lookahead should be hyphen character", getFollow(p1text1), getWidth("-")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void testSingleLetter() {
		simpleText.setText("a"); //$NON-NLS-1$
		assertLineBreakNotFound(simpleText.addLeadingWordRequirements(width));
		assertLookaheadMatchesString(width, "a"); //$NON-NLS-1$
	}

	@Test
	public void testSingleSpace() {
		simpleText.setText(" "); //$NON-NLS-1$
		assertTrue("Line break should have been found", simpleText.addLeadingWordRequirements(width)); //$NON-NLS-1$
		assertEquals("test21 failed", width[0], 0); //$NON-NLS-1$

		simpleText.setText("\u7325"); //$NON-NLS-1$
		width[0] = 0;
		assertTrue("test22 failed", simpleText.addLeadingWordRequirements(width)); //$NON-NLS-1$
		assertEquals("test23 failed", width[0], 0); //$NON-NLS-1$

		simpleText.setText("-"); //$NON-NLS-1$
		width[0] = 0;
		assertTrue("test24 failed", simpleText.addLeadingWordRequirements(width)); //$NON-NLS-1$
		assertEquals("test25 failed", width[0], getWidth("-")); //$NON-NLS-1$ //$NON-NLS-2$

		simpleText.setText("ombudsman"); //$NON-NLS-1$
		width[0] = 0;
		assertTrue("test26 failed", !simpleText.addLeadingWordRequirements(width)); //$NON-NLS-1$
		assertEquals("test27 failed", width[0], getWidth("ombudsman")); //$NON-NLS-1$ //$NON-NLS-2$

		simpleText.setText("space bar"); //$NON-NLS-1$
		width[0] = 0;
		assertTrue("test28 failed", simpleText.addLeadingWordRequirements(width)); //$NON-NLS-1$
		assertEquals("test29 failed", width[0], getWidth("space")); //$NON-NLS-1$ //$NON-NLS-2$

		simpleText.setText("endsInSpace "); //$NON-NLS-1$
		width[0] = 0;
		assertTrue("test30 failed", simpleText.addLeadingWordRequirements(width)); //$NON-NLS-1$
		assertEquals("test31 failed", width[0], getWidth("endsInSpace")); //$NON-NLS-1$ //$NON-NLS-2$

		simpleText.setText("endsInHyphen-"); //$NON-NLS-1$
		width[0] = 0;
		assertTrue("test32 failed", simpleText.addLeadingWordRequirements(width)); //$NON-NLS-1$
		assertEquals("test33 failed", width[0], getWidth("endsInHyphen-")); //$NON-NLS-1$ //$NON-NLS-2$

		simpleText.setText("co-operate"); //$NON-NLS-1$
		width[0] = 0;
		assertTrue("test34 failed", simpleText.addLeadingWordRequirements(width)); //$NON-NLS-1$
		assertEquals("test35 failed", width[0], getWidth("co-")); //$NON-NLS-1$ //$NON-NLS-2$

		simpleText.setText("ab\u7325"); //$NON-NLS-1$
		width[0] = 0;
		assertTrue("test36 failed", simpleText.addLeadingWordRequirements(width)); //$NON-NLS-1$
		assertEquals("test37 failed", width[0], getWidth("ab")); //$NON-NLS-1$ //$NON-NLS-2$

// FIXME: the following test does not reliably run on all platforms
//		simpleText.setText("hey, man."); //$NON-NLS-1$
//		width[0] = 0;
//		assertTrue("test38 failed", simpleText.addLeadingWordRequirements(width)); //$NON-NLS-1$
//		assertEquals("test39 failed", width[0], getWidth("hey,")); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
