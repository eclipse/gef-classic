/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import java.util.List;

import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.InlineFlow;
import org.eclipse.draw2d.text.TextFlow;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class TextualTests 
	extends BaseTestCase
{
	
public void testLineRootBidiCommit() {
	FlowPage block = new FlowPage();
	InlineFlow bold = new InlineFlow();
	InlineFlow italics = new InlineFlow();
	TextFlow boldTextStart = new TextFlow();
	TextFlow boldTextEnd = new TextFlow();
	TextFlow italicText = new TextFlow();
	
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
	
	List boldFragments = bold.getFragments();
	assertTrue(boldFragments.size() == 3);
	List italicFragments = italics.getFragments();
	assertTrue(italicFragments.size() == 2);
}

}
