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

import java.util.Iterator;

import junit.framework.TestCase;

import org.eclipse.swt.graphics.Font;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.draw2d.text.TextFragmentBox;

public class TextFlowWrapTest 
	extends TestCase 
{

protected static final Font TAHOMA = new Font(null, "Tahoma", 8, 0);//$NON-NLS-1$

// used to ensure that there are no extra fragments
protected String TERMINATE = "#@$FREGWW@@#!";
protected FlowPage figure;
protected TextFlow textFlow;
protected String failMsg;
protected boolean failed;

protected void setUp() throws Exception {
	super.setUp();
	failMsg = "\n";
	failed = false;
}

protected void doTest(String stringToTest, String widthString, String[] answers) {
	figure.setSize(FigureUtilities.getStringExtents(widthString, TAHOMA).width,500);
	textFlow.setText(stringToTest);
	figure.getLayoutManager().layout(null);
	Iterator frags = textFlow.getFragments().iterator();
	
	int index = 0;
	for (; index < answers.length; index++) {
		String answer = answers[index];
		if (answer == TERMINATE) {
			if (frags.hasNext()) {
				TextFragmentBox box = (TextFragmentBox)frags.next();
				failMsg += "Failed on: " + stringToTest + " Found extra fragment: -" + stringToTest.substring(box.offset, box.offset + box.length) + "-\n";
				failed = true;
			}
			return;
		}
		TextFragmentBox frag = null;
		if (frags.hasNext())
			frag = (TextFragmentBox) frags.next();
		else
			break;
		if (!stringToTest.substring(frag.offset, frag.offset + frag.length).equals(answer)) {
			failMsg += "Failed on: " + stringToTest + " Frag expected: -" + answer + "- Got: -" + stringToTest.substring(frag.offset, frag.offset + frag.length) + "-\n";
			failed = true;
			return;
		}
	}
	if (index < answers.length) {
		failMsg += "Failed on: " + stringToTest + " Frag expected: -" + answers[index] + "- No corresponding fragment\n";
		failed = true;
	}
}

public void runGenericTests() {
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
	doTest( "tester abc\n def", "tester", new String[] {"tester", "abc" });
	doTest( "tester abc\r\n def", "tester", new String[] {"tester", "abc" });
	doTest( "tester abc\r def", "tester", new String[] {"tester", "abc" });
	doTest( "tester abc def\r\n", "tester", new String[] {"tester", "abc"} );
	doTest( "tester abc def\r", "tester", new String[] {"tester", "abc" });
	doTest( "tester abc def\n", "tester", new String[] {"tester", "abc"} );
	doTest( "blah blah blah", "blah blah", new String[] {"blah blah", "blah", TERMINATE});
	doTest( "blah blah blah", "blah", new String[] {"blah", "blah"});
	doTest( "h hh h", "h hh", new String[] {"h hh", "h", TERMINATE});
	doTest( "h hh h", "h hh ", new String[] {"h hh", "h"} );
	doTest( "x x x  x ", "x x x ", new String[] {"x x x", "x", TERMINATE});
	doTest( "\n\nbreak", "break", new String[] {"", ""});
	doTest( "\r\rbreak", "break", new String[] {"", ""});
	doTest( "\r\n\r\nbreak", "break", new String[] {"",""});
	
	doTest("testers testers testers ab c", "testers testers test", new String[] {"testers testers", "testers ab c", TERMINATE});
	doTest("testers\r ab c", "testers", new String[] {"testers", " ab c", TERMINATE});
	doTest("ab\tcd", "ab", new String[] {"ab", "cd", TERMINATE});
	doTest("trailingSpace  \n  ", "trailingSpace", new String[] {"trailingSpace", "", TERMINATE});
	doTest("test \r b", "test", new String[] {"test", " b", TERMINATE});
	doTest("   \n   \n   \n   ", "wwwwww", new String[] {"", "", "", "", TERMINATE});
	doTest("\r\r\n", "wwwwwww", new String[] {"", "", TERMINATE});
	doTest("", "www", new String[] {"", TERMINATE});
	// empty string means availableWidth == 1
	doTest("", "", new String[] {"", TERMINATE});
	doTest("a cow\naha", "a cow", new String[] {"a cow", "aha", TERMINATE});
	// chinese characters
	doTest("\u7325\u7334\u7329", "\u7325\u7334", new String[]{"\u7325\u7334", "\u7329", TERMINATE});
	doTest("\u7325\u7334\u7329", "\u7325", new String[]{"\u7325", "\u7334", "\u7329", TERMINATE});
}

public void testHardWrapping() {
	figure = new FlowPage();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(
			new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_HARD));
	textFlow.setFont(TAHOMA);
	figure.add(textFlow);
	
	runGenericTests();
	doTest("Flow    Container  ", "F", new String[] {"Flow", "Container", TERMINATE});
	doTest("aha \nb \r c ", "", new String[] {"aha", "b", "", "c", TERMINATE});
	doTest("Flowing", "flow", new String[] {"Flowing", TERMINATE});
	
//	assertFalse(failMsg, failed);
}

public void testSoftWrapping() {
	figure = new FlowPage();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(
			new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_SOFT));
	textFlow.setFont(TAHOMA);
	figure.add(textFlow);
	
	runGenericTests();
	doTest( "tester ab", "teste", new String[] {"teste", "r ab", TERMINATE} );
	doTest("aha \nb \r c ", "", new String[] {"a", "h", "a", "b", "", "c", TERMINATE});

//	assertFalse(failMsg, failed);
}

public void testTruncatedWrapping() {
	figure = new FlowPage();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(
			new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_TRUNCATE));
	textFlow.setFont(TAHOMA);
	figure.add(textFlow);
	
	runGenericTests();
	doTest("Flowing          Container", "Container", new String[] {"Flowing", "Container", TERMINATE});
	doTest("Flowing          C", "Flo...", new String[] {"Flo", "C", TERMINATE});
	doTest("         Foo", "Foo", new String[] {"", "Foo", TERMINATE});
	doTest("aha \nb \r c ", "", new String[] {"a", "b", "", "c", TERMINATE});

//	assertFalse(failMsg, failed);
}

public void testZeroWidth() {
	/*
	 * @TODO:Pratik    somehow need to test such that availableWidth == 0
	 */
	// FlowUtilities is not visible
//	FlowUtilities.getTextForSpace(new TextFragmentBox(), "whatever", TAHOMA, 0, 0.0, ParagraphTextLayout.WORD_WRAP_TRUNCATE);
}

}