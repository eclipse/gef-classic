package org.eclipse.draw2d.text;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

/**
 * Utility class for FlowFigures
 * @author hudsonr
 * @since 2.1 */
public class FlowUtilities
	extends FigureUtilities
{

private static int getNextBreakpoint(String s, int offset) {
	int index = s.indexOf(" ", offset);
	if (index == -1) return s.length();
	return index;
}

private static int getPreviousBreakpoint(String s, int offset) {
	if (offset == s.length()) return offset;
	int index = s.lastIndexOf(" ", offset - 1);
	return Math.max(0, index + 1);
}

/**
 * Returns the number of characters from the specified String that will fit in the
 * available amount of space. An average character width can be provided as a hint for
 * faster calculation.
 * @param s the String * @param f the Font used for measuring * @param availableWidth the available width in pixels * @param avg 0.0, or an avg character width to use during calculation * @return int */
public static int getTextForSpace(String s, Font f, int availableWidth, float avg) {
	FontMetrics metrics = getFontMetrics(f);
	int MIN, min, max;
	if (avg == 0.0)
		avg = metrics.getAverageCharWidth();
	MIN = min = getNextBreakpoint(s, 0);
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
	return Math.max(MIN, getPreviousBreakpoint(s, min));
}

static void setupFragment(TextFragmentBox frag, Font f, String s) {
	Dimension d = getTextExtents(s.substring(0, frag.length), f);
	FontMetrics fm = getFontMetrics(f);
	frag.setHeight(fm.getHeight() + fm.getLeading());
	frag.setAscent(fm.getAscent() + fm.getLeading());
	frag.setWidth (d.width);
}

}