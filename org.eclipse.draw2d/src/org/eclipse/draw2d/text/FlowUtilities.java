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

/**
 * Returns the number of characters from the specified String that will fit in the
 * available amount of space. An average character width can be provided as a hint for
 * faster calculation.
 * @param s the String * @param f the Font used for measuring * @param availableWidth the available width in pixels * @param avg 0.0, or an avg character width to use during calculation * @return int */
public static int getTextForSpace(String s, Font f, int availableWidth, float avg) {
	if (s.equals("")) //$NON-NLS-1$
		return 0;
	
	FontMetrics metrics = getFontMetrics(f);
	BreakIterator breakItr = BreakIterator.getLineInstance();
	breakItr.setText(s);
	int MIN, min, max;
	if (avg == 0.0)
		avg = metrics.getAverageCharWidth();
	MIN = min = breakItr.next();
	max = s.length() + 1;

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
		guessSize = getTextExtents(s.substring(0, guess), f).width;

		if (guessSize < availableWidth)
			//We did not use the available width
			min = guess;
		else
			//We exceeded the available width
			max = guess;
	}
	
	if (min == s.length()) {
		return min;
	} else {
		return Math.max(MIN, breakItr.preceding(min - 1));
	}
}

static void setupFragment(TextFragmentBox frag, Font f, String s) {
	Dimension d = getStringExtents(s.substring(0, frag.length), f);
	FontMetrics fm = getFontMetrics(f);
	frag.setHeight(fm.getHeight() + fm.getLeading());
	frag.setAscent(fm.getAscent() + fm.getLeading());
	frag.setWidth (d.width);
}

}