/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowContext;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ImageFlow;
import org.eclipse.draw2d.text.InlineFlow;
import org.eclipse.draw2d.text.TextFlow;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class LookAheadTest
	extends TestCase
{

protected static final Font TAHOMA = new Font(null, "Tahoma", 8, 0);//$NON-NLS-1$

private int getWidth(String s) {
	return FigureUtilities.getStringExtents(s, TAHOMA).width;
}

/*
 * Tests TextFlow#getWidthUntilLineBreak()
 */
public void testAddLeadingWordRequirements() {
	TextFlow flow = new TextFlow();
	flow.setFont(TAHOMA);
	flow.setText("a");
	int[] width = new int[1];
	assertTrue("test18 failed", !flow.addLeadingWordRequirements(width));
	assertTrue("test19 failed", width[0] == getWidth("a"));
	
	flow.setText(" ");
	width[0] = 0;
	assertTrue("test20 failed", flow.addLeadingWordRequirements(width));
	assertTrue("test21 failed", width[0] == 0);
	
	flow.setText("\u7325");
	width[0] = 0;
	assertTrue("test22 failed", flow.addLeadingWordRequirements(width));
	assertTrue("test23 failed", width[0] == 0);
	
	flow.setText("-");
	width[0] = 0;
	assertTrue("test24 failed", flow.addLeadingWordRequirements(width));
	assertTrue("test25 failed", width[0] == getWidth("-"));
	
	flow.setText("ombudsman");
	width[0] = 0;
	assertTrue("test26 failed", !flow.addLeadingWordRequirements(width));
	assertTrue("test27 failed", width[0] == getWidth("ombudsman"));
	
	flow.setText("space bar");
	width[0] = 0;
	assertTrue("test28 failed", flow.addLeadingWordRequirements(width));
	assertTrue("test29 failed", width[0] == getWidth("space"));
	
	flow.setText("endsInSpace ");
	width[0] = 0;
	assertTrue("test30 failed", flow.addLeadingWordRequirements(width));
	assertTrue("test31 failed", width[0] == getWidth("endsInSpace"));
	
	flow.setText("endsInHyphen-");
	width[0] = 0;
	assertTrue("test32 failed", flow.addLeadingWordRequirements(width));
	assertTrue("test33 failed", width[0] == getWidth("endsInHyphen-"));
	
	flow.setText("co-operate");
	width[0] = 0;
	assertTrue("test34 failed", flow.addLeadingWordRequirements(width));
	assertTrue("test35 failed", width[0] == getWidth("co-"));
	
	flow.setText("ab\u7325");
	width[0] = 0;
	assertTrue("test36 failed", flow.addLeadingWordRequirements(width));
	assertTrue("test37 failed", width[0] == getWidth("ab"));
	
	flow.setText("hey, man.");
	width[0] = 0;
	assertTrue("test38 failed", flow.addLeadingWordRequirements(width));
	assertTrue("test39 failed", width[0] == getWidth("hey,"));
}

public void testLookAhead() {
	FlowPage root = new FlowPage();
	root.setFont(TAHOMA);

	TextFlow a1Flow = new TextFlow();
	root.add(a1Flow);
	
	BlockFlow a3Flow = new BlockFlow();
	root.add(a3Flow);
	
	TextFlow a4Flow = new TextFlow();
	root.add(a4Flow);
	
	TextFlow a5Flow = new TextFlow();
	root.add(a5Flow);
	
	BlockFlow a6Flow = new BlockFlow();
	root.add(a6Flow);
	
	InlineFlow a3b1Flow = new InlineFlow();
	a3Flow.add(a3b1Flow);
	
	TextFlow a3b2Flow = new TextFlow();
	a3Flow.add(a3b2Flow);
	
	InlineFlow a3b3Flow = new InlineFlow();
	a3Flow.add(a3b3Flow);
	
	TextFlow a3b4Flow = new TextFlow();
	a3Flow.add(a3b4Flow);
	
	ImageFlow a3b1c1Flow = new ImageFlow();
	a3b1c1Flow.setImage(new Image(null, 60, 60));
	a3b1Flow.add(a3b1c1Flow);
	
	TextFlow a3b1c3Flow = new TextFlow();
	a3b1Flow.add(a3b1c3Flow);
	
	TextFlow a3b3c1Flow = new TextFlow();
	a3b3Flow.add(a3b3c1Flow);
	
	a1Flow.setText("<alpha>");
	a3b1c3Flow.setText("charlie");
	a3b2Flow.setText("");
	a3b3c1Flow.setText("delta");
	a3b4Flow.setText(" ");
	a4Flow.setText("</alpha> ");
	a5Flow.setText("echo");
	
	int[] width = new int[1];
	// test that look ahead properly iterates over its children
	// test that look ahead doesn't go inside or past a block
	assertTrue("test1 failed", 
			((FlowContext)root.getLayoutManager()).getWordWidthFollowing(null, width));
	assertTrue("test2 failed", width[0] == getWidth("<alpha>"));
	
	// test that look ahead, given a child, starts searching at the correct index
	// test that look ahead finds the space at the end of some text
	width[0] = 0;
	assertTrue("test3 failed", 
			((FlowContext)root.getLayoutManager()).getWordWidthFollowing(a3Flow, width));
	assertTrue("test4 failed", width[0] == getWidth("</alpha>"));
	
	// test that inline flows go into their children
	// test that images are recognized as line-breaks
	width[0] = 0;
	assertTrue("test5 failed", 
			((FlowContext)a3Flow.getLayoutManager()).getWordWidthFollowing(null, width));
	assertTrue("test6 failed", width[0] == 0);
	
	// test that inline flows will properly query their parent when needed
	// test that TextFlows with empty Strings are handled properly
	// test that a space by itself in a TextFlow is recognized as a line-break
	width[0] = 0;
	assertTrue("test7 failed", ((FlowContext)a3b1Flow.getLayoutManager())
			.getWordWidthFollowing(a3b1c1Flow, width));
	assertTrue("test8 failed", width[0] == getWidth("charliedelta"));
	
	// test that blocks are treated as line-breaks
	width[0] = 0;
	assertTrue("test9 failed", 
			((FlowContext)root.getLayoutManager()).getWordWidthFollowing(a5Flow, width));
	assertTrue("test10 failed", width[0] == 0);
	
	// test that chinese characters are treated as line-breaks
	width[0] = 0;
	a3b4Flow.setText("\u7325abc");
	assertTrue("test11 failed", ((FlowContext)a3Flow.getLayoutManager())
			.getWordWidthFollowing(a3b3Flow, width));
	assertTrue("test12 failed", width[0] == 0);

	// test that hyphens are treated as line-breaks
	width[0] = 0;
	a3b4Flow.setText("-abc");
	assertTrue("test13 failed", ((FlowContext)a3Flow.getLayoutManager())
			.getWordWidthFollowing(a3b3Flow, width));
	assertTrue("test14 failed", width[0] == getWidth("-"));

	// test that look ahead doesn't fail when there is no parent
	// test that if a break does not exist, foundBreak is false
//	a3Flow.remove(a3b3Flow);
	width[0] = 0;
	InlineFlow parent = new InlineFlow();
	TextFlow child = new TextFlow();
	child.setFont(TAHOMA);
	child.setText("delta");
	parent.add(child);
	assertTrue("test15 failed", !((FlowContext)parent.getLayoutManager())
			.getWordWidthFollowing(null, width));
	assertTrue("test16 failed", width[0] == getWidth("delta"));
}

}