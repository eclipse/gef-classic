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

import java.text.BreakIterator;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * Utility class for FlowFigures.
 * @author hudsonr
 * @since 2.1 */
class FlowUtilities
	extends FigureUtilities
{

private static Dimension ELLIPSIS_SIZE = new Dimension();

/**
 * Returns the number of characters from the specified String that will fit in the
 * available amount of space. An average character width can be provided as a hint for
 * faster calculation.
 * 
 * @param frag the TextFragmentBox
 * @param string the String * @param font the Font used for measuring * @param availableWidth the available width in pixels * @param avg 0.0, or an avg character width to use during calculation
 * @param wrapping the word wrap style * @return the number of characters that will fit in the given space */
public static int getTextForSpace(TextFragmentBox frag, String string, Font font, 
										int availableWidth, float avg, int wrapping) {
	/*
	 * Changes to this method should be tested with 
	 * org.eclipse.draw2d.test.TextFlowWrapTest
	 */
	frag.truncated = false;
	if (string.length() == 0) {
		frag.length = 0;
		setupFragment(frag, font, string);
		return 0;
	}
	
	FontMetrics metrics = getFontMetrics(font);
	BreakIterator breakItr = BreakIterator.getLineInstance();
	breakItr.setText(string);
	// min is the maximum no. of characters that can fit in the available width.  To get
	// to that last character that fits you'd do string.charAt(min - 1).  max is the 
	// smallest possible number of characters that will not fit in the available space.
	int MIN, min, max;

	MIN = min = (wrapping == ParagraphTextLayout.WORD_WRAP_HARD) ?  breakItr.next() : 1;
	max = string.length() + 1;
	if (avg == 0.0)
		avg = metrics.getAverageCharWidth();

	int winNL = string.indexOf("\r\n"); //$NON-NLS-1$
	int macNL = string.indexOf('\r');
	int unixNL = string.indexOf('\n');
	//	 If the Mac newline is just the prefix to the win NL, ignore it	
	if (macNL == winNL)
		macNL = -1;
	// max points to the character after the first instance of a NL character
	if (winNL != -1) {
		winNL += 2;
		max = Math.min(max, winNL + 1);
	}
	if (unixNL != -1) {
		unixNL++;
		max = Math.min(max, unixNL + 1);
	}
	if (macNL != -1) {
		macNL++;
		max = Math.min(max, macNL + 1);
	}

	//The size of the current guess
	int guess = 0, guessSize = 0;
	while ((max - min) > 1) {
		// Pick a new guess size
		// New guess is the last guess plus the missing width in pixels
		// divided by the average character size in pixels
		guess = guess + (int)((availableWidth - guessSize) / avg);

		if (guess >= max) guess = max - 1;
		if (guess <= min) guess = min + 1;

		//Measure the current guess
		guessSize = getStringExtents(string.substring(0, guess), font).width;

		if (guessSize <= availableWidth)
			//We did not use the available width
			min = guess;
		else
			//We exceeded the available width
			max = guess;
	}
		
	// Skip forward (thus consuming all whitespace) until max is a non-whitespace 
	// character (or end of the given string), unless we have encountered a newline
	while (max <= string.length() && Character.isWhitespace(string.charAt(max - 1))
			&& min != winNL && min != macNL && min != unixNL) {
		max++;
		min++;
	}

	/*
	 * @TODO:Pratik
	 * Remove any offset the given String might have.  This is because 
	 * BreakIterator.isBoundary() has a bug where it doesn't take the offset into account.
	 * This workaround should be removed once BreakIterator is fixed.
	 */
	string = new String(string.toString());
	
	int result;
	boolean needToSetLength = true;
	if (min == string.length() || min == winNL || min == unixNL || min == macNL
			|| breakItr.isBoundary(max - 1))
		// min = last letter in the given string, max = past the last letter OR
		// min = NewLine OR
		// max = boundary (includes the cases of japanese characters
		//                 and min = whitespace, max = non-whitespace character
		result = min;
	else {
		// min = non-whitespace character, max = non-whitespace character
		result = breakItr.preceding(max - 1);
		if (result == 0) {
			switch (wrapping) {
				case ParagraphTextLayout.WORD_WRAP_HARD :
					result = MIN;
					break;
				case ParagraphTextLayout.WORD_WRAP_SOFT :
					result = min;
					break;
				case ParagraphTextLayout.WORD_WRAP_TRUNCATE :
					// FigureUtilities increases the necessary width by 1 pixel, but that
					// causes boundary problems where there is just enough space to show
					// a letter and the ellipsis.  So, we reduce that one pixel here.
					ELLIPSIS_SIZE = FigureUtilities
							.getStringExtents(TextFlow.ELLIPSIS, font).shrink(1, 0);
					// This recursive invocation will set the fragment's length appropriately
					getTextForSpace(frag, 
							string, 
							font, 
							availableWidth - ELLIPSIS_SIZE.width, 
							avg, 
							ParagraphTextLayout.WORD_WRAP_SOFT);
					needToSetLength = false;
					frag.truncated = true;
					result = breakItr.following(max - 1);
					if (result == BreakIterator.DONE)
						result = string.length();
			}
		}
	}
	
	if (needToSetLength)
		frag.length = result;
	setupFragment(frag, font, string);
	return result;
}

static void setupFragment(TextFragmentBox frag, Font f, String s) {
	while (frag.length > 0 && Character.isWhitespace(s.charAt(frag.length - 1)))
		frag.length--;
	Dimension d = getStringExtents(s.substring(0, frag.length), f);
	FontMetrics fm = getFontMetrics(f);
	frag.setHeight(fm.getHeight());
	frag.setAscent(fm.getAscent());
	if (frag.truncated)
		d.width += ELLIPSIS_SIZE.width;
	frag.setWidth (d.width);
}

}