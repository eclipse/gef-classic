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

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.text.*;

import org.eclipse.swt.graphics.Font;

import java.util.Iterator;

import junit.framework.TestCase;

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

	figure = new FlowPage();
	textFlow = new TextFlow();
	textFlow.setLayoutManager(new ParagraphTextLayout(textFlow, ParagraphTextLayout.WORD_WRAP_SOFT));
	textFlow.setFont(TAHOMA);
	figure.add(textFlow);
	failMsg = "\r\n";
	failed = false;
}


public void testWordWrap() {
	doTest( "tester abc", "tester", "tester", "abc");
	doTest( "tester abc", "tester a", "tester", "abc");
	doTest( "tester abc", "tester ab", "tester", "abc");
	doTest( "tester ab", "tester", "tester", "ab" );
	doTest( "tester ab c", "tester", "tester", "ab c" );			
	doTest( "tester ab", "teste", "teste", "r ab" );
	doTest( "test\r ab c", "test ab c", "test"," ab c" );
	doTest( "test\n ab c", "test ab c", "test"," ab c" );
	doTest( "test\r\n abc def", "test abc def", "test", " abc def" );
	doTest( "\rtester abc def", "tester", "", "tester" );
	doTest( "\r\ntester abc def", "tester", "", "tester" );
	doTest( "\ntester abc def", "tester", "", "tester" );
	doTest( "tester abc\n def", "tester", "tester", "abc" );
	doTest( "tester abc\r\n def", "tester", "tester", "abc" );
	doTest( "tester abc\r def", "tester", "tester", "abc" );
	doTest( "tester abc def\r\n", "tester", "tester", "abc" );
	doTest( "tester abc def\r", "tester", "tester", "abc" );
	doTest( "tester abc def\n", "tester", "tester", "abc" );
	doTest( "blah blah blah", "blah blah", "blah blah", "blah" );
	doTest( "blah blah blah", "blah", "blah", "blah");
	doTest( "h hh h", "h hh", "h hh", "h" );
	doTest( "h hh h", "h hh ", "h hh", "h" );
	doTest( "x x x  x ", "x x x ", "x x x", "x");
	doTest( "\n\nbreak", "break", "", "");
	doTest( "\r\rbreak", "break", "", "");
	doTest( "\r\n\r\nbreak", "break", "","");
	
	//do test with two \n 
	assertFalse(failMsg, failed);
}

protected void doTest(String stringToTest, String widthString, String firstFrag, String secondFrag) {
	figure.setSize(FigureUtilities.getStringExtents(widthString, TAHOMA).width,500);
	textFlow.setText(stringToTest);
	figure.getLayoutManager().layout(null);
	Iterator frags = textFlow.getFragments().iterator();
	TextFragmentBox frag = null;
	
	frag = (TextFragmentBox) frags.next();

//		while (frags.hasNext()) {
//			frag = (TextFragmentBox) frags.next();
//		}

	if (!stringToTest.substring(frag.offset, frag.offset + frag.length).equals(firstFrag)) {
		failMsg += "Failed on: " + stringToTest + " Frag expected: -" + firstFrag + "- Got: -" + stringToTest.substring(frag.offset, frag.offset + frag.length) + "-\r\n";
		failed = true;
		return;
	}

	frag = (TextFragmentBox) frags.next();

	if (!stringToTest.substring(frag.offset, frag.offset + frag.length).equals(secondFrag)) {
		failMsg += "Failed on: " + stringToTest + " Frag expected: -" + secondFrag + "- Got: -" + stringToTest.substring(frag.offset, frag.offset + frag.length) + "-\r\n";
		failed = true;
	}
}
/*
 * @see TestCase#tearDown()
 */
protected void tearDown() throws Exception {
	super.tearDown();
}

}
