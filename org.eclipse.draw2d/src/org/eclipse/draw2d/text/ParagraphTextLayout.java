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
 * Wrapping will ONLY occur at valid line breaks
 */
public static final int WORD_WRAP_HARD = 0;

/**
 * Wrapping will always occur at the end of the available space, breaking in the middle of
 * a word.
 */
public static final int WORD_WRAP_SOFT = 1;

/**
 * Wrapping will always occur at the end of available space, truncating a word if it
 * doesn't fit.
 */
public static final int WORD_WRAP_TRUNCATE = 2;

private int wrappingStyle = WORD_WRAP_HARD;

/**
 * Consturcts a new ParagraphTextLayout on the specified TextFlow.
 * @param flow the TextFlow */
public ParagraphTextLayout(TextFlow flow) {
	super(flow);
}

/**
 * Constructs the layout with the specified TextFlow and wrapping style.  The wrapping
 * style must be one of:
 * <UL>
 * 	<LI>{@link #WORD_WRAP_HARD}</LI>
 * 	<LI>{@link #WORD_WRAP_SOFT}</LI>
 * 	<LI>{@link #WORD_WRAP_TRUNCATE}</LI>
 * </UL>
 * @param flow the textflow
 * @param style the style of wrapping
 */
public ParagraphTextLayout(TextFlow flow, int style) {
	this(flow);
	wrappingStyle = style;
}

protected float getAverageCharWidth(TextFragmentBox fragment) {
	if (fragment.width != 0 && fragment.length != 0)
		return fragment.width / (float)fragment.length;
	return 0.0f;
}

/** * @see org.eclipse.draw2d.text.FlowFigureLayout#layout() */
protected void layout() {
	TextFlow flowFigure = (TextFlow)getFlowFigure();
	
	   List fragments = flowFigure.getFragments();//Reuse the previous List of fragments
	 String string    = flowFigure.getText();
	   Font font      = flowFigure.getFont();
	    int i         = 0; //The index of the current fragment;
	    int offset    = 0; //The current offset in the ORIGINAL text, not s
	    int length    = 0; //The length of the current fragment
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
			length = FlowUtilities.getTextForSpace(
				fragment,
				string,
				font,
				currentLine.getAvailableWidth(),
				prevAvgCharWidth,
				wrappingStyle);
			
			if (fragment.width < currentLine.getAvailableWidth()
			  || !context.isCurrentLineOccupied())
				break;
			context.endLine();
		}
		//fragment.x = context.getCurrentX();
		context.addToCurrentLine(fragment);
		string = string.substring(length);
		offset += length;
		if (string.length() > 0)
			context.endLine();
		i++;
	} while (string.length() > 0 && fragment.length != 0);

	//Remove the remaining unused fragments.
	while (i < fragments.size())
		fragments.remove(fragments.size() - 1);
}

}


