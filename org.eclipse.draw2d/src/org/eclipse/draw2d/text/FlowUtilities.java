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

import java.text.BreakIterator;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

/**
 * Utility class for FlowFigures
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
 * @param s the String * @param f the Font used for measuring * @param availableWidth the available width in pixels * @param avg 0.0, or an avg character width to use during calculation * @return int */
public static int getTextForSpace(TextFragmentBox frag, String string, Font font, int availableWidth, float avg, int WRAPPING) {
	if (string.length() == 0)
		throw new IllegalArgumentException(
			"String must have length greater than 0"); //$NON-NLS-1$
	frag.truncated = false;
	
	FontMetrics metrics = getFontMetrics(font);
	BreakIterator breakItr = BreakIterator.getLineInstance();
	breakItr.setText(string);
	int MIN, min, max;
	if (avg == 0.0)
		avg = metrics.getAverageCharWidth();

	int firstBreak = breakItr.next();
	firstBreak = Math.min(string.length()-1, firstBreak);
	MIN = min = (WRAPPING != ParagraphTextLayout.WORD_WRAP_SOFT) ?  firstBreak : 1;
	max = string.length() + 1;

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
		guessSize = getTextExtents(string.substring(0, guess), font).width;

		if (guessSize < availableWidth)
			//We did not use the available width
			min = guess;
		else
			//We exceeded the available width
			max = guess;
	}
	
	int result = string.length();
	switch (WRAPPING) {
		case ParagraphTextLayout.WORD_WRAP_HARD :
			if (min == string.length())
				result = min;
			else
				result = Math.max(MIN, breakItr.preceding(min));
			frag.length = result;
			break;

		case ParagraphTextLayout.WORD_WRAP_SOFT :
			if (min == string.length())
				result = min;
			else
				result = breakItr.preceding(min);
			if (result <= 0)
				result = min;
			frag.length = result;
			break;
		case ParagraphTextLayout.WORD_WRAP_TRUNCATE :
			if (min == string.length()) {
				result = frag.length = min;
				setupFragment(frag, font, string);
				if (frag.getWidth() <= availableWidth)
					return result;
			} else
				result = breakItr.preceding(min);
			if (result <= 0) {
				String ELLIPSIS = "..."; //$NON-NLS-1$
				ELLIPSIS_SIZE = FigureUtilities.getStringExtents(ELLIPSIS, font);
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
	Dimension d = getStringExtents(s.substring(0, frag.length), f);
	FontMetrics fm = getFontMetrics(f);
	frag.setHeight(fm.getHeight() + fm.getLeading());
	frag.setAscent(fm.getAscent() + fm.getLeading());
	if (frag.truncated)
		d.width += ELLIPSIS_SIZE.width;
	frag.setWidth (d.width);
}

}