/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Matt Scarpino - Bug 88678
 *******************************************************************************/

package org.eclipse.draw2d.test;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.text.CaretInfo;
import org.eclipse.swt.widgets.Shell;

/**
 * @since 3.1
 */
public class CaretTests extends SimpleTextTest {

	int trail[] = new int[1];
	int offset;
	Point offsetPoint;

	protected void setUp() throws Exception {
		super.setUp();
		flowpage.setFont(TAHOMA);

		// Necessary to call getNextOffset()
		sentence.setText(PHRASE + "\n" + PHRASE);

		// Necessary to create figure with size > 0
		Shell shell = new Shell();
		FigureCanvas fc = new FigureCanvas(shell);
		fc.setContents(flowpage);
		fc.pack();
	}

	public void testNothing() {
		/* Remove when Bugzilla 88678 is fixed. */
	}
	
	public void off_testOffset() {
		offsetPoint = new Point(118, 7);
		offset = sentence.getOffset(offsetPoint, trail, null);
		assertEquals(offset, 23);
		assertEquals(trail[0], 0);
	}

	public void off_testNextOffset() {
		offsetPoint = new Point(129, 0);
		offset = sentence.getNextOffset(offsetPoint, true, null);
		assertEquals(offset, 72);
		assertEquals(trail[0], 0);

		offsetPoint = new Point(77, 18);
		offset = sentence.getNextOffset(offsetPoint, false, null);
		assertEquals(offset, 14);
		assertEquals(trail[0], 0);
	}

	public void off_testCaretPlacement() {
		offsetPoint = new Point(11, 21);
		offset = sentence.getOffset(offsetPoint, trail, null);
		CaretInfo info = sentence.getCaretPlacement(offset, trail[0] != 0);
		if (info.getBaseline() == 24) {
			// Windows XP
			assertEquals(info.getBaseline(), 24);
			assertEquals(info.getHeight(), 13);
			assertEquals(info.getLineHeight(), 13);
			assertEquals(info.getLineY(), 13);
			assertEquals(info.getX(), 12);
			assertEquals(info.getY(), 13);
		} else {
			// Red Hat Linux
			assertEquals(info.getBaseline(), 25);
			assertEquals(info.getHeight(), 14);
			assertEquals(info.getLineHeight(), 14);
			assertEquals(info.getLineY(), 14);
			assertEquals(info.getX(), 13);
			assertEquals(info.getY(), 14);
		}
	}
}
