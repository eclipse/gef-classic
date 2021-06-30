/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.test;

import java.util.List;

import org.eclipse.draw2dl.text.FlowPage;
import org.eclipse.draw2dl.text.InlineFlow;
import org.eclipse.draw2dl.text.TextFlow;
import org.eclipse.draw2dl.text.TextFragmentBox;
import org.eclipse.swt.graphics.Font;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class TextualTests extends BaseTestCase {

	public void testLineRootBidiCommit() {
		org.eclipse.draw2dl.text.FlowPage block = new org.eclipse.draw2dl.text.FlowPage();
		org.eclipse.draw2dl.text.InlineFlow bold = new org.eclipse.draw2dl.text.InlineFlow();
		org.eclipse.draw2dl.text.InlineFlow italics = new InlineFlow();
		org.eclipse.draw2dl.text.TextFlow boldTextStart = new org.eclipse.draw2dl.text.TextFlow();
		org.eclipse.draw2dl.text.TextFlow boldTextEnd = new org.eclipse.draw2dl.text.TextFlow();
		org.eclipse.draw2dl.text.TextFlow italicText = new org.eclipse.draw2dl.text.TextFlow();

		block.add(bold);
		bold.add(boldTextStart);
		bold.add(italics);
		italics.add(italicText);
		bold.add(boldTextEnd);

		boldTextStart.setText("abc ");
		italicText.setText("xyz \u0634\u0637\u0635");
		boldTextEnd.setText(" \u0639\u0633\u0640 hum");

		block.setSize(-1, -1);
		block.setFont(TAHOMA);
		block.validate();

		// TODO enhance verification
		List boldFragments = bold.getFragments();
		assertTrue(boldFragments.size() == 3);
		List italicFragments = italics.getFragments();
		assertTrue(italicFragments.size() == 2);
	}

	// test for bug 113700
	public void testGetFirstAndLastOffsetForLine() {
		Font xlFont = new Font(null, "Tahoma", 28, 0);
		org.eclipse.draw2dl.text.FlowPage block = new FlowPage();
		org.eclipse.draw2dl.text.TextFlow smallText = new org.eclipse.draw2dl.text.TextFlow("wwww");
		org.eclipse.draw2dl.text.TextFlow largeText = new TextFlow("ABCD");

		block.setFont(TAHOMA);
		largeText.setFont(xlFont);
		block.add(smallText);
		block.add(largeText);
		block.setSize(-1, -1);
		block.validate();

		org.eclipse.draw2dl.text.TextFragmentBox box = (TextFragmentBox) largeText.getFragments().get(0);
		int y = box.getBaseline();
		try {
			assertTrue(smallText.getFirstOffsetForLine(y) == 0);
			assertTrue(smallText.getLastOffsetForLine(y) == 3);
			assertTrue(smallText.getFirstOffsetForLine(y - 1) == -1);
			assertTrue(smallText.getLastOffsetForLine(y + 1) == -1);
		} finally {
			xlFont.dispose();
		}
	}

}