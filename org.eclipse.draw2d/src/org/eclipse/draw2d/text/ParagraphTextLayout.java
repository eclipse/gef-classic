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
package org.eclipse.draw2d.text;

import java.util.*;

import org.eclipse.swt.graphics.Font;

/**
 * The layout for {@link TextFlow}.
 * @author hudsonr
 * @since 2.1 */
public class ParagraphTextLayout
	extends TextLayout
{

/**
 * Consturcts a new ParagraphTextLayout on the specified TextFlow.
 * @param flow the TextFlow */
public ParagraphTextLayout(TextFlow flow) {
	super(flow);
}

protected float getAverageCharWidth(TextFragmentBox fragment){
	if (fragment.width != 0 && fragment.length != 0)
		return fragment.width/(float)fragment.length;
	return 0.0f;
}

/** * @see org.eclipse.draw2d.text.FlowFigureLayout#layout() */
protected void layout() {
	TextFlow flowFigure = (TextFlow)getFlowFigure();
	
	   List fragments = flowFigure.getFragments();//Reuse the previous List of fragments
	 String s         = flowFigure.getText();
	   Font f         = flowFigure.getFont();
	    int i         = 0; //The index of the current fragment;
	    int offset    = 0; //The current offset in the ORIGINAL text, not s
	  float prevAvgCharWidth;
	LineBox currentLine;
	TextFragmentBox fragment;

	do {
		fragment = null;
		prevAvgCharWidth = 0f;
		fragment = getFragment(i, fragments);
		fragment.offset	= offset;
		prevAvgCharWidth = getAverageCharWidth(fragment);
		
		//This loop is done at most twice.
		//The second time through, a context.endLine()
		//was requested, and the loop will break.
		while (true) {
			currentLine = context.getCurrentLine();
			fragment.length	= FlowUtilities.getTextForSpace(
				s,
				f,
				currentLine.getAvailableWidth(),
				prevAvgCharWidth);
			
			FlowUtilities.setupFragment(fragment, f, s);
			if (fragment.width < currentLine.getAvailableWidth()
			  || !context.isCurrentLineOccupied())
				break;
			context.endLine();
		}
		//fragment.x = context.getCurrentX();
		context.addToCurrentLine(fragment);
		s = s.substring(fragment.length);
		offset += fragment.length;
		if (s.length() > 0)
			context.endLine();
		i++;
	} while (s.length() > 0 && fragment.length != 0);

	//Remove the remaining unused fragments.
	while (i < fragments.size())
		fragments.remove(fragments.size()-1);
}

}


