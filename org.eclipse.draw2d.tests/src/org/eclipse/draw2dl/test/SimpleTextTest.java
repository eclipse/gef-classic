/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2dl.test;

import org.eclipse.draw2dl.FigureUtilities;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.draw2dl.text.FlowPage;
import org.eclipse.draw2dl.text.InlineFlow;
import org.eclipse.draw2dl.text.TextFlow;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * @since 3.1
 */
public class SimpleTextTest extends AbstractTextTest {

	public static final String PHRASE = "The quick brown fox jumped over the lazy dog. ";
	public org.eclipse.draw2dl.text.FlowPage flowpage = new FlowPage();
	public org.eclipse.draw2dl.text.TextFlow sentence = new org.eclipse.draw2dl.text.TextFlow(PHRASE);
	public org.eclipse.draw2dl.text.InlineFlow inline = new InlineFlow();
	public org.eclipse.draw2dl.text.TextFlow child1 = new org.eclipse.draw2dl.text.TextFlow(PHRASE);
	public org.eclipse.draw2dl.text.TextFlow child2 = new TextFlow(PHRASE);
	public Font font = Display.getDefault().getSystemFont();

	protected void setUp() throws Exception {
		flowpage.setFont(font);
		flowpage.add(sentence);
		flowpage.add(inline);
		inline.add(child1);
		inline.add(child2);
	}

	protected void makePageWidth(String string, int extra) {
		int width = FigureUtilities.getStringExtents(string, font).width
				+ extra;
		flowpage.setBounds(new Rectangle(0, 0, width, 300));
		flowpage.validate();
	}

}
