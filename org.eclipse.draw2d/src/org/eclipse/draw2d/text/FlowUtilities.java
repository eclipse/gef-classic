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
 * @param frag the TextFragmentBox
 * @param string the String * @param font the Font used for measuring * @param availableWidth the available width in pixels * @param avg 0.0, or an avg character width to use during calculation
 * @param wrapping the word wrap style * @return the number of characters that will fit in the space */
public static int getTextForSpace(TextFragmentBox frag, String string, Font font, 
										int availableWidth, float avg, int wrapping) {
	frag.truncated = false;
	if (string.length() == 0) {
		frag.length = 0;
		setupFragment(frag, font, string);
		return 0;
	}
	
	FontMetrics metrics = getFontMetrics(font);
	BreakIterator breakItr = BreakIterator.getLineInstance();
	breakItr.setText(string);
	int MIN, min, max;
	if (avg == 0.0)
		avg = metrics.getAverageCharWidth();

	int firstBreak = breakItr.next();

	int winNL = string.indexOf("\r\n"); //$NON-NLS-1$
	int macNL = string.indexOf('\r');
	int unixNL = string.indexOf('\n');

	MIN = min = (wrapping == ParagraphTextLayout.WORD_WRAP_HARD) ?  firstBreak : 1;
	if (macNL == winNL)
		macNL = -1; //If the Mac newline is just the prefix to the win NL, ignore it

	max = string.length() + 1;
	
	if (winNL != -1) {
		max = Math.min(max, winNL);
		min = Math.min(min, winNL);
	}
	if (unixNL != -1) {
		max = Math.min(max, unixNL);
		min = Math.min(min, unixNL);
	}
	if (macNL != -1) {
		max = Math.min(max, macNL);
		min = Math.min(min, macNL);
	}

	int origMax = max;
	//The size of the current guess
	int guess = 0,
	    guessSize = 0;

	while ((max - min) > 1) {
		//Pick a new guess size
		//	New guess is the last guess plus the missing width in pixels
		//	divided by the average character size in pixels
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

	int result = string.length();
	switch (wrapping) {
		case ParagraphTextLayout.WORD_WRAP_HARD :
			if (min == string.length() || min == winNL || min == unixNL || min == macNL)
				result = min;
			else if (max == origMax 
						&& getStringExtents(string.substring(0, max), font).width 
						<= availableWidth)
				result = max;
			else if (breakItr.isBoundary(Math.min(max, string.length() - 1)))
				result = max;
			else
				result = Math.max(MIN, breakItr.preceding(Math.min(max, 
																string.length() - 1)));
			frag.length = result;
			break;

		case ParagraphTextLayout.WORD_WRAP_SOFT :
			if (min == string.length() || min == winNL || min == unixNL || min == macNL)
				result = min;
			else if (max == origMax
						&& getStringExtents(string.substring(0, max), font).width 
						<= availableWidth)
				result = max;
			else if (breakItr.isBoundary(Math.min(max, string.length() - 1)))
				result = max;
			else 
				result = breakItr.preceding(Math.min(max, string.length() - 1));
			if (result <= 0)
				result = min;
			frag.length = result;
			break;
		case ParagraphTextLayout.WORD_WRAP_TRUNCATE :
			if (min == string.length() || min == winNL || min == unixNL || min == macNL) {
				result = frag.length = min;
				setupFragment(frag, font, string);
				if (frag.getWidth() <= availableWidth)
					return result;
				min -= 1;
			} else if (max == origMax 
						&& getStringExtents(string.substring(0, max), font).width 
						<= availableWidth) {
				result = frag.length = max;
				setupFragment(frag, font, string);
				return result;
			}
			else if (breakItr.isBoundary(Math.min(max, string.length() - 1)))
				result = max;
			else
				result = breakItr.preceding(Math.min(max, string.length() - 1));
			if (result <= 0) {
				ELLIPSIS_SIZE = FigureUtilities.getStringExtents(TextFlow.ELLIPSIS, font);
				getTextForSpace(
					frag,
					string,
					font,
					availableWidth - ELLIPSIS_SIZE.width,
					avg,
					ParagraphTextLayout.WORD_WRAP_SOFT);
				//frag.length = min;
				frag.truncated = true;
				result = breakItr.following(min);
				if (result == BreakIterator.DONE)
					result = string.length();
			} else {
				frag.length = result;
			}
	}

	setupFragment(frag, font, string);
	return result;
}

static void setupFragment(TextFragmentBox frag, Font f, String s) {
	//if (frag.length != s.length()) 
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