/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

import java.util.List;

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

/**
 * Given the Bidi levels of the given text, this method breaks the given text up by
 * its level runs.
 * @param text the String that needs to be broken up into its level runs
 * @param levels the Bidi levels
 * @return	an array of Strings where each String is in the same level run and the
 * concatenation of all Strings would equal the given text
 */
protected String[] breakText(String text, int[] levels) {
	if (levels == null || levels.length == 2)
		return new String[] {text};
	String[] results = new String[levels.length / 2];
	for (int i = 0; i < results.length; i++) {
		int start = i * 2 + 1;
		int end = start + 2;
		if (end < levels.length)
			results[i] = text.substring(levels[start], levels[end]);
		else
			results[i] = text.substring(levels[start]);
	}
	return results;
}

/**
 * Returns the average character width of given TextFragmentbox
 * @param fragment the TextFragmentBox
 * @return the average character width 
 */
protected float getAverageCharWidth(TextFragmentBox fragment) {
	if (fragment.width != 0 && fragment.length != 0)
		return fragment.width / (float)fragment.length;
	return 0.0f;
}

/** * @see org.eclipse.draw2d.text.FlowFigureLayout#layout() */
protected void layout() {
	TextFlow flowFigure = (TextFlow)getFlowFigure();

	List fragments = flowFigure.getFragments();//Reuse the previous List of fragments
	Font font = flowFigure.getFont();
	int i = 0; //The index of the current fragment;
	int offset = 0; //The current offset in the ORIGINAL text, not s
	int length = 0; //The length of the current fragment
	float prevAvgCharWidth;
	LineBox currentLine;
	TextFragmentBox fragment;
	int[] bidiValues = flowFigure.getBidiValues();
	String[] strings = breakText(flowFigure.getText(), bidiValues);
	strings[0] = flowFigure.prependJoiner(strings[0]);
	strings[strings.length - 1] = flowFigure.appendJoiner(strings[strings.length - 1]);
	
	for (int j = 0; j < strings.length; j++) {
		String string = strings[j];
		do {
			fragment = null;
			prevAvgCharWidth = 0f;
			fragment = getFragment(i, fragments);
			prevAvgCharWidth = getAverageCharWidth(fragment);
			
			fragment.offset = offset;
			fragment.setBidiLevel(bidiValues == null ? -1 : bidiValues[j * 2]);
			
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
				
				if (fragment.width <= currentLine.getAvailableWidth()
				  || !context.isCurrentLineOccupied())
					break;
				context.endLine();
			}
			context.addToCurrentLine(fragment);
			string = string.substring(length);
			offset += length;
			if (string.length() > 0 || fragment.truncated)
				context.endLine();
			i++;
		} while (string.length() > 0);
	}

	//Remove the remaining unused fragments.
	while (i < fragments.size())
		fragments.remove(fragments.size() - 1);
}

}