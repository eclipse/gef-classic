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

public class TextFlowWrapTest extends TestCase {

protected static final Font TAHOMA = new Font(null, "Tahoma", 8, 0);//$NON-NLS-1$

protected FlowPage figure;
protected TextFlow textFlow;
protected String failMsg;
protected boolean failed;

/*
 * @see TestCase#setUp()
 */
protected void setUp() throws Exception {
	super.setUp();

	failMsg = "\r\n";
	failed = false;
}

public void testSoftWrapping() {
	figure = new FlowPage();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_SOFT));
	textFlow.setFont(TAHOMA);
	figure.add(textFlow);

	doTest( "tester abc", "tester", new String[] {"tester", "abc"});
	doTest( "tester abc", "tester a", new String[] {"tester", "abc"});
	doTest( "tester abc", "tester ab", new String[] {"tester", "abc"});
	doTest( "tester ab", "tester", new String[] {"tester", "ab"} );
	doTest( "tester ab c", "tester", new String[] {"tester", "ab c"} );			
	doTest( "tester ab", "teste", new String[] {"teste", "r ab"} );
	doTest( "test\r ab c", "test ab c", new String[] {"test"," ab c"} );
	doTest( "test\n ab c", "test ab c", new String[] {"test"," ab c"} );
	doTest( "test\r\n abc def", "test abc def", new String[] {"test", " abc def" });
	doTest( "\rtester abc def", "tester", new String[] {"", "tester" });
	doTest( "\r\ntester abc def", "tester", new String[] {"", "tester" });
	doTest( "\ntester abc def", "tester", new String[] {"", "tester"} );
	doTest( "tester abc\n def", "tester", new String[] {"tester", "abc" });
	doTest( "tester abc\r\n def", "tester", new String[] {"tester", "abc" });
	doTest( "tester abc\r def", "tester", new String[] {"tester", "abc" });
	doTest( "tester abc def\r\n", "tester", new String[] {"tester", "abc"} );
	doTest( "tester abc def\r", "tester", new String[] {"tester", "abc" });
	doTest( "tester abc def\n", "tester", new String[] {"tester", "abc"} );
	doTest( "blah blah blah", "blah blah", new String[] {"blah blah", "blah"} );
	doTest( "blah blah blah", "blah", new String[] {"blah", "blah"});
	doTest( "h hh h", "h hh", new String[] {"h hh", "h" });
	doTest( "h hh h", "h hh ", new String[] {"h hh", "h"} );
	doTest( "x x x  x ", "x x x ", new String[] {"x x x", "x"});
	doTest( "\n\nbreak", "break", new String[] {"", ""});
	doTest( "\r\rbreak", "break", new String[] {"", ""});
	doTest( "\r\n\r\nbreak", "break", new String[] {"",""});
	
	doTest("testers\r ab c", "testers", new String[] {"testers", " ab c"});
	doTest("ab\tcd", "ab", new String[] {"ab", "cd"});
	doTest("trailingSpace  \n  ", "trailingSpace", new String[] {"trailingSpace", ""});
	
	//do test with two \n 
	assertFalse(failMsg, failed);
}

public void testHardWrapping() {
	figure = new FlowPage();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_HARD));
	textFlow.setFont(TAHOMA);
	figure.add(textFlow);
	doTest("Flow          Container", "Flow...", new String[] {"Flow...", "Container"});
	doTest("Flow Container", "F", new String[] {"Flow", "Container"});
}

public void testTruncatedWrapping() {
	figure = new FlowPage();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_TRUNCATE));
	textFlow.setFont(TAHOMA);
	figure.add(textFlow);
}

protected void doTest(String stringToTest, String widthString, String[] answers) {
	figure.setSize(FigureUtilities.getStringExtents(widthString, TAHOMA).width,500);
	textFlow.setText(stringToTest);
	figure.getLayoutManager().layout(null);
	Iterator frags = textFlow.getFragments().iterator();
	
	int index = 0;
	for (; index < answers.length; index++) {
		TextFragmentBox frag = null;
		if (frags.hasNext())
			frag = (TextFragmentBox) frags.next();
		else
			break;
		if (!stringToTest.substring(frag.offset, frag.offset + frag.length).equals(answers[index])) {
			failMsg += "Failed on: " + stringToTest + " Frag expected: -" + answers[index] + "- Got: -" + stringToTest.substring(frag.offset, frag.offset + frag.length) + "-\r\n";
			failed = true;
			return;
		}
	}
	if (index < answers.length)
		failMsg += "Failed on: " + stringToTest + " Frag expected: -" + answers[index] + "No matching fragment\r\n";
}

}
