/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.InlineFlow;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.draw2d.text.TextFragmentBox;

public class TextFlowWrapTest
	extends BaseTestCase 
{
	
// used to ensure that there are no extra fragments
static final String TERMINATE = "#@terminate@#!";
// used to ensure that two consecutive fragments are on the same line
static final String SAMELINE = "#@sameline@#";
// used to ensure that two consecutive fragments are on different lines
static final String NEWLINE = "#@newline@#";
// used to ensure that a fragment is truncated (this mark is placed after the fragment
// that is supposed to be truncated)
static final String TRUNCATED = "#@truncated@#";
//used to ensure that a fragment is not truncated (this mark is placed after the fragment
//that is not supposed to be truncated)
static final String NON_TRUNCATED = "#@non-truncated@#";

FlowPage figure;
TextFlow textFlow, textFlow2;

protected void doTest(String stringToTest, String widthString, String[] answers) {
	doTest2(stringToTest, "", widthString, answers);
}

protected void doTest2(String string1, String string2, String widthString, String[] answers) {
	int width = -1;
	if (widthString != null)
		width = FigureUtilities.getStringExtents(widthString, TAHOMA).width;
	figure.setSize(width, 1000);
	textFlow.setText(string1);
	textFlow2.setText(string2);
	figure.validate();
	ArrayList list = new ArrayList(textFlow.getFragments());
	list.addAll(textFlow2.getFragments());
	Iterator frags = list.iterator();
	
	int index = 0;
	TextFragmentBox previousFrag = null;
	for (; index < answers.length; index++) {
		String answer = answers[index];
		if (answer == TERMINATE) {
			if (frags.hasNext()) {
				TextFragmentBox box = (TextFragmentBox)frags.next();
				assertTrue("Failed on: " + string1 + string2 + " Found extra fragment: -" 
						+ string1.substring(box.offset, box.offset + box.length) + "-\n",
						false);
			}
			return;
		} else if (answer == TRUNCATED) {
			assertTrue("Failed on: " + string1 + string2 + "Fragment is not truncated\n",
					callBooleanMethod(previousFrag, "isTruncated"));
			continue;
		} else if (answer == NON_TRUNCATED) {
			assertFalse("Failed on: " + string1 + string2 + "Fragment is truncated\n",
					callBooleanMethod(previousFrag, "isTruncated"));
			continue;
		}

		if (!frags.hasNext())
			break;

		TextFragmentBox frag = (TextFragmentBox) frags.next();
		
		if (answer == SAMELINE) {
			assertTrue("Failed on: " + string1 + string2 + " Fragments are not on the same line\n",
					previousFrag.getBaseline() == frag.getBaseline());
			index++;
			answer = answers[index];
		} else if (answer == NEWLINE) {
			assertTrue("Failed on: " + string1 + string2 + " Fragments are on the same line\n",
					previousFrag.getBaseline() != frag.getBaseline());
			index++;
			answer = answers[index];
		}
		previousFrag = frag;
		
		if (textFlow.getFragments().contains(frag)) {
			assertEquals("Failed on: \"" + string1 +"\" + \"" + string2
					+ "\" Fragment expected: \"" + answer + "\" Got: \""
					+ string1.substring(frag.offset, frag.offset + frag.length) + "\"\n",
					answer, string1.substring(frag.offset, frag.offset + frag.length));
			return;
		} else {
			assertEquals("Failed on: \"" + string1 +"\" + \"" + string2
					+ "\" Fragment expected: \"" + answer + "\" Got: \""
					+ string2.substring(frag.offset, frag.offset + frag.length) + "\"\n",
					answer, string2.substring(frag.offset, frag.offset + frag.length));
			return;
		}
	}
	
	assertFalse("Failed on: \"" + string1 +"\" + \"" + string2 + "\" Fragment expected: -"
			+ answers[index] + "- No corresponding fragment\n",
			index < answers.length);
}

protected void runGenericTests() {
	doTest( "tester abc", "tester", new String[] {"tester", "abc", TERMINATE});
	doTest( "tester abc", "tester a", new String[] {"tester", "abc", TERMINATE});
	doTest( "tester abc", "tester ab", new String[] {"tester", "abc", TERMINATE});
	doTest( "tester ab", "tester", new String[] {"tester", "ab"} );
	doTest( "tester ab c", "tester", new String[] {"tester", "ab c"} );			
	doTest( "test\r ab c", "test ab c", new String[] {"test"," ab c"} );
	doTest( "test\n ab c", "test ab c", new String[] {"test"," ab c"} );
	doTest( "test\r\n abc def", "test abc def", new String[] {"test", " abc def", TERMINATE});
	doTest( "\rtester abc def", "tester", new String[] {"", "tester" });
	doTest( "\r\ntester abc def", "tester", new String[] {"", "tester" });
	doTest( "\ntester abc def", "tester", new String[] {"", "tester"} );
	doTest( "tester abc\n def", "tester", new String[] {"tester", "abc", " def", TERMINATE });
	doTest( "tester abc\r\n def", "tester", new String[] {"tester", "abc", " def", TERMINATE });
	doTest( "tester abc\r def", "tester", new String[] {"tester", "abc", " def", TERMINATE });
	doTest( "tester abc def\r\n", "tester", new String[] {"tester", "abc", "def", TERMINATE} );
	doTest( "tester abc def\r", "tester", new String[] {"tester", "abc", "def", TERMINATE });
	doTest( "tester abc def\n", "tester", new String[] {"tester", "abc", "def", TERMINATE} );
	doTest( "blah blah blah", "blah blah", new String[] {"blah blah", "blah", TERMINATE});
	doTest( "blah blah blah", "blah", new String[] {"blah", "blah"});
	doTest( "h hh h", "h hh", new String[] {"h hh", "h", TERMINATE});
	doTest( "h hh h", "h hh ", new String[] {"h hh", "h"} );
	doTest( "x x x  x ", "x x x ", new String[] {"x x x ", "x ", TERMINATE});
	doTest( "x x x  x", "x x x", new String[] {"x x x", " x", TERMINATE});
	doTest( "\n\nbreak", "break", new String[] {"", ""});
	doTest( "\r\rbreak", "break", new String[] {"", ""});
	doTest( "\r\n\r\nbreak", "break", new String[] {"", "", "break", TERMINATE});
	doTest("crow ", "crow", new String[] {"crow", TERMINATE});
	
	doTest("abc - -moreango", "abc", new String[] {"abc", NEWLINE, "- -", NEWLINE});
	doTest("abc def ghi", "abc def g", new String[] {"abc def", "ghi", TERMINATE});
	doTest("blah blah ", "blah blah", new String[] {"blah blah", TERMINATE});
	doTest("testers testers testers ab c", "testers testers test", new String[] {"testers testers", "testers ab c", TERMINATE});
	doTest("testers\r ab c", "testers", new String[] {"testers", " ab c", TERMINATE});
	doTest("trailingSpace  \n  ", "trailingSpace", new String[] {"trailingSpace", " ", "  ", TERMINATE});
	doTest("test \r b", "test", new String[] {"test", "", " b", TERMINATE});
	doTest("   \n   \n   \n   ", "wwwwww", new String[] {"   ", "   ", "   ", "   ", TERMINATE});
	doTest("   \n  \n   ", " ", new String[] {" ", " ", " ", "", " ", " ", TERMINATE});
	doTest("\r\r\n", "wwwwwww", new String[] {"", "", TERMINATE});
	doTest("", "www", new String[] {"", TERMINATE});
	// empty string means availableWidth == 1
	doTest("", "", new String[] {"", TERMINATE});
	doTest("a cow\naha", "a cow", new String[] {"a cow", "aha", TERMINATE});
//	doTest(".  a", ". ", new String[] {".", NEWLINE, "a"});
	doTest("more.  Fun", "more.", new String[] {"more.", NEWLINE, "Fun", TERMINATE});
	doTest("a one. two", "a one", new String[] {"a", NEWLINE, "one.", NEWLINE, "two"});
	doTest("a one-two", "a one", new String[] {"a", NEWLINE, "one-", NEWLINE, "two"});
	// chinese characters
	doTest("\u7325\u7334\u7329", "\u7325\u7334", new String[]{"\u7325\u7334", "\u7329", TERMINATE});
	doTest("\u7325\u7334\u7329", "\u7325", new String[]{"\u7325", "\u7334", "\u7329", TERMINATE});
	
	// BiDi
	doTest2("\u0634", "foo-bar", "\u0634foo-", new String[] {"\u0634", SAMELINE, "foo-", NEWLINE, "bar"});
	
	// testing with multiple TextFlows will bring lookAhead into action
	doTest2("foo1", " bar1", null, new String[] {"foo1", SAMELINE, " bar1", TERMINATE});
	doTest2("foo2", " bar2", "foo2 ", new String[] {"foo2", "", "bar2", TERMINATE});
	doTest2("foo3", " ba3", "foo3", new String[] {"foo3", "", NEWLINE, "ba3", TERMINATE});
	doTest2("foo4 ", " bar4", "foo4 ", new String[] {"foo4 ", "", "bar4", TERMINATE});
	doTest2("wwww ", " bar", "wwww", new String[] {"wwww", "", " bar", TERMINATE});
	doTest2("foo5 ", "bar5", "foo5 ", new String[] {"foo5", NEWLINE, "", "bar5", TERMINATE});
	doTest2("foot bar", "xyz", "barxyz", new String[] {"foot", "bar", SAMELINE, "xyz", TERMINATE});
	doTest2("foo\n", " bar6", null, new String[] {"foo", NEWLINE, "", SAMELINE, " bar6", TERMINATE});
	doTest2("foo7-bar7", "mo", "foo7-ba", new String[] {"foo7-", NEWLINE, "bar7", SAMELINE, "mo", TERMINATE});
	doTest2("foo-bar", "abc", "foo-barab", new String[] {"foo-", NEWLINE, "bar", SAMELINE, "abc", TERMINATE});
	doTest2(" foobar", "abc", " foobarab", new String[] {"", NEWLINE, "foobar", SAMELINE});
	doTest2("foo  bar", "abc", "foo  barab", new String[] {"foo ", NEWLINE, "bar", SAMELINE, "abc", TERMINATE});
	doTest2("abd", "\u7325", "abd", new String[] {"abd", NEWLINE, "\u7325"});

	doTest2("a abc", "-def", "a abc", new String[] {"a", NEWLINE, "abc", SAMELINE, "-", NEWLINE, "def", TERMINATE});
	
	doTest2("alpha\n", "bravo", null, new String[] {"alpha", NEWLINE, "", SAMELINE, "bravo", TERMINATE});
}

protected void runHardWrappingTests() {
	doTest("ab\tcd", "ab", new String[] {"ab", NEWLINE, "cd", TERMINATE});
	doTest("ahahahah", "aha", new String[] {"ahahahah", TERMINATE});
	doTest("Flow    Container  ", " ", new String[] {"Flow", " ", "", "Container", " ", TERMINATE});
	doTest("aha \nb \r c ", "", new String[] {"aha", "", "b", "", "", "c", TERMINATE});
	doTest2("one", "two", "onet", new String[] {"one", SAMELINE, "two", TERMINATE});
	doTest2("one", "t ", "one", new String[] {"one", SAMELINE, "t", TERMINATE});
	doTest("Flowing", "flow", new String[] {"Flowing", TERMINATE});
	doTest2("foobar", "foobar", "foo", new String[] {"foobar", SAMELINE, "foobar"});
	doTest2("home ", "alone", "home al", new String[] {"home", NEWLINE, "", SAMELINE, "alone", TERMINATE});
	doTest2("more willing in t", "hemorning", "more willing in themorni", 
			new String[] {"more willing in", NEWLINE, "t", SAMELINE, "hemorning", TERMINATE});
}

protected void runSoftWrappingTests() {
	doTest("ab\tcd", "ab", new String[] {"ab", NEWLINE, "cd", TERMINATE});
	doTest( "tester ab", "teste", new String[] {"teste", NEWLINE, "r ab", TERMINATE} );
	doTest("aha \nb \r c ", "", new String[] {"a", "h", "a", "", "b", "", "", "c", TERMINATE});
	doTest("\u0634abcd", "\u0634abc", new String[] {"\u0634", SAMELINE, "abc", NEWLINE, "d", TERMINATE});
	doTest2("foofoo", "foo", "foo", new String[] {"foo", NEWLINE, "foo", NEWLINE, "foo", TERMINATE});
	doTest2("foofo", "ofoo", "foo", new String[] {"foo", NEWLINE, "fo", SAMELINE, "o", NEWLINE, "foo", TERMINATE});
}

protected void runTruncatedWrappingTests() {
	doTest("ab\tcd", "ab", new String[] {"", TRUNCATED, NEWLINE, "", TRUNCATED, TERMINATE});
	doTest("Flowing  Container", "Flo...", new String[] {"Flo", "", "Co", TERMINATE});
	doTest("Flowing C", "Flo...", new String[] {"Flo", "C", TERMINATE});
	doTest("Fooooooo", "...", new String[] {"", TRUNCATED, TERMINATE});
	doTest("WWW", "|...", new String[] {"", TRUNCATED, TERMINATE});
	doTest(" Foo", "Foo", new String[] {"", "Foo", TERMINATE});
//	doTest("aha \nb \r c ", "", new String[] {"", TRUNCATED, "", "", TRUNCATED, "", "", "", TRUNCATED, TERMINATE});
	doTest("aha \nb \r c ", "", new String[] {"", TRUNCATED, "", NON_TRUNCATED, "b", "", NON_TRUNCATED, "", "c", TERMINATE});
	doTest("aha \nb \r c", "", new String[] {"", TRUNCATED, "", NON_TRUNCATED, "b", "", NON_TRUNCATED, "", "c", TERMINATE});
	doTest("aha \nb \r w ", "..", new String[] {"", TRUNCATED, "", NON_TRUNCATED, "b", "", NON_TRUNCATED, "", "w", TERMINATE});
	// truncation is not supported with BiDi and across figures (with look-ahead), so we're
	// not testing it here
//	doTest2("foobar", "foobar", "foobar...", new String[] {"foobar", SAMELINE, "", TERMINATE});
//	doTest2("foobar", "foobar", "fooba...", new String[] {"fooba", TERMINATE});
//	doTest2("foobar", "foobar", "f...", new String[] {"f", TERMINATE});
}

public void testHardWrapping() {
	figure = new FlowPage();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(
			new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_HARD));
	textFlow.setFont(TAHOMA);
	figure.add(textFlow);
	textFlow2 = new TextFlow();
	textFlow2.setLayoutManager(
			new ParagraphTextLayout(textFlow2, ParagraphTextLayout.WORD_WRAP_HARD));
	textFlow2.setFont(TAHOMA);
	figure.add(textFlow2);
	
	runGenericTests();
	runHardWrappingTests();
}

public void testSoftWrapping() {
	figure = new FlowPage();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(
			new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_SOFT));
	textFlow.setFont(TAHOMA);
	figure.add(textFlow);
	textFlow2 = new TextFlow();
	textFlow2.setLayoutManager(
			new ParagraphTextLayout(textFlow2, ParagraphTextLayout.WORD_WRAP_SOFT));
	textFlow2.setFont(TAHOMA);
	figure.add(textFlow2);
	
	runGenericTests();
	runSoftWrappingTests();
}

public void testTruncatedWrapping() {
	figure = new FlowPage();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(
			new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_TRUNCATE));
	textFlow.setFont(TAHOMA);
	figure.add(textFlow);
	textFlow2 = new TextFlow();
	textFlow2.setLayoutManager(
			new ParagraphTextLayout(textFlow2, ParagraphTextLayout.WORD_WRAP_TRUNCATE));
	textFlow2.setFont(TAHOMA);
	figure.add(textFlow2);
	
	runGenericTests();
	runTruncatedWrappingTests();
}

public void testInlineFlow() {
	figure = new FlowPage();
	InlineFlow inline = new InlineFlow();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(
			new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_SOFT));
	textFlow.setFont(TAHOMA);
	inline.add(textFlow);
	figure.add(inline);
	textFlow2 = new TextFlow();
	textFlow2.setLayoutManager(
			new ParagraphTextLayout(textFlow2, ParagraphTextLayout.WORD_WRAP_SOFT));
	textFlow2.setFont(TAHOMA);
	figure.add(textFlow2);
	runGenericTests();
	runSoftWrappingTests();
	
	figure = new FlowPage();
	inline = new InlineFlow();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(
			new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_HARD));
	textFlow.setFont(TAHOMA);
	inline.add(textFlow);
	figure.add(inline);
	textFlow2 = new TextFlow();
	textFlow2.setLayoutManager(
			new ParagraphTextLayout(textFlow2, ParagraphTextLayout.WORD_WRAP_HARD));
	textFlow2.setFont(TAHOMA);
	figure.add(textFlow2);
	runGenericTests();
	runHardWrappingTests();
	doTest2("def", "def", "defde", new String[] {"def", SAMELINE, "def", TERMINATE});
}

public void testNestedInlineFlows() {
	figure = new FlowPage();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(
			new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_SOFT));
	textFlow.setFont(TAHOMA);
	figure.add(textFlow);
	InlineFlow inline = new InlineFlow();
	figure.add(inline);
	InlineFlow inline2 = new InlineFlow();
	inline.add(inline2);
	textFlow2 = new TextFlow();
	textFlow2.setLayoutManager(
			new ParagraphTextLayout(textFlow2, ParagraphTextLayout.WORD_WRAP_SOFT));
	textFlow2.setFont(TAHOMA);
	inline2.add(textFlow2);
	runGenericTests();
	runSoftWrappingTests();
	
	figure = new FlowPage();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(
			new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_HARD));
	textFlow.setFont(TAHOMA);
	figure.add(textFlow);
	inline = new InlineFlow();
	figure.add(inline);
	inline2 = new InlineFlow();
	inline.add(inline2);
	textFlow2 = new TextFlow();
	textFlow2.setLayoutManager(
			new ParagraphTextLayout(textFlow2, ParagraphTextLayout.WORD_WRAP_HARD));
	textFlow2.setFont(TAHOMA);
	inline2.add(textFlow2);
	runGenericTests();
	runHardWrappingTests();
	doTest2("def", "def", "defde", new String[] {"def", SAMELINE, "def", TERMINATE});
}

}