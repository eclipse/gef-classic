/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.TextLayout;

/**
 * A helper class for a BlockFlow that does Bidi evaluation of all the text in
 * that block.
 * 
 * <p>
 * WARNING: This class is not intended to be subclassed by clients.
 * </p>
 * 
 * @author Pratik Shah
 * @since 3.1
 */
public class BidiProcessor
{

/**
 * The Singleton
 */
public static final BidiProcessor INSTANCE = new BidiProcessor();
private StringBuffer bidiText = new StringBuffer();

private List list = new ArrayList();
private int orientation = SWT.LEFT_TO_RIGHT;

private BidiProcessor() {}

/**
 * FlowFigures can make textual contributions for its block.  All contributions
 * are concatenated (in the order that they were contributed) to make the final
 * String which will be evaluated to determine the Bidi levels (in the 
 * {@link #process() process} method).  This method should only be called at most 
 * once per figure per Bidi validation, i.e, a FlowFigure is not allowed to make
 * multiple text contributions at different offsets.
 * 
 * @param fig the figure that is contributing the given text
 * @param str the text contributed by the given figure
 * @see #addControlText(String)
 */
public void add(FlowFigure fig, String str) {
	if (fig == null || str == null || str.length() == 0)
		return;
	if (bidiText == null)
		bidiText = new StringBuffer();
	list.add(new StringInfoHolder(fig, bidiText.length(), str.length()));
	bidiText.append(str);
}

/**
 * This methods allows FlowFigures to contribute text that may effect the Bidi
 * evaluation, but is not text that is visible on the screen.  The bidi level of
 * such text is not saved or assigned back to the contributing figure.
 * 
 * @param str the contributed text
 */
public void addControlText(String str) {
	if (bidiText == null)
		bidiText = new StringBuffer();
	bidiText.append(str);
}

/**
 * Breaks the given int array into bidi levels for each figure based on their
 * contributed text, and assigns those levels to each figure.  Also determines
 * if shaping needs to occur between figures and sets the appendJoiner, prependJoiner
 * accordingly.
 * 
 * @param levels the calculated levels of all the text in the block
 */
private void assignResults(int[] levels) {
	int offsetIndex = 1;
	StringInfoHolder prevInfo = null, info = null;
	for (int i = 0; i < list.size(); i++) {
		info = (StringInfoHolder)list.get(i);
		while (levels[offsetIndex] <= info.offset) {
			offsetIndex += 2;
			if (offsetIndex >= levels.length)
				break;
		}
		offsetIndex -= 2;
		int startingIndex = offsetIndex - 1;
		while (levels[offsetIndex] <= info.offset + info.length) {
			offsetIndex += 2;
			if (offsetIndex >= levels.length)
				break;
		}
		offsetIndex -= 2;
		int endingIndex = offsetIndex;
		int results[] = new int[endingIndex - startingIndex + 1];
		System.arraycopy(levels, startingIndex, results, 0, results.length);
		for (int j = 1; j < results.length; j += 2)
			results[j] = Math.max(0, results[j] - info.offset);
		info.fig.setBidiValues(results);

		// Determine whether or not joiner characters need to be added
		if (prevInfo != null
				// if we started in the middle of a level run
				&& levels[startingIndex + 1] != info.offset
				// and the level run is odd
				&& levels[offsetIndex - 1] % 2 == 1
				// and the first character of this figure is arabic
				&& isArabic(bidiText.charAt(info.offset)) 
				// and the last character of the previous figure was arabic
				&& isArabic(bidiText.charAt(prevInfo.offset + prevInfo.length - 1))) {
			info.fig.setPrependJoiner(true);
			prevInfo.fig.setAppendJoiner(true);
		}
		prevInfo = info;
	}
}

/**
 * @param the character to be evaluated
 * @return <code>true</code> if the given character is Arabic or ZWJ
 */
private boolean isArabic(char c) {
	return Character.getDirectionality(c) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
			|| c == '\u200d';
}

/**
 * Processes the contributed text, determines the Bidi levels, and assigns them to
 * the FlowFigures that made thet contributions.  Shaping of visually contiguous 
 * Arabic characters that are split in different figures is also handled.  
 * This method will do nothing if the contributed text does not require Bidi 
 * evaluation.  All contributions are discarded at the end of this method.
 * <p>
 * The assigned Bidi levels are in the form on an int array that contains sets of
 * the Bidi level and the offset of the first character that that level applies to. For 
 * instance, [1, 0, 2, 5, 1, 9] would mean that characters at index 0-4 are of level 1, 
 * 5-8 of level 2 and the remaining characters (starting at offset 9) are of level 1.
 */
public void process() {
	if (bidiText != null && bidiText.length() != 0) {
		char[] chars = new char[bidiText.length()];
		bidiText.getChars(0, bidiText.length(), chars, 0);
		if (Bidi.requiresBidi(chars, 0, chars.length - 1)) {
			int[] levels = new int[16];
			TextLayout layout = FlowUtilities.getTextLayout();
			layout.setOrientation(orientation);
			layout.setText(bidiText.toString());
			int j = 0, offset = 0, prevLevel = -1;
			for (int i = 0; i < chars.length; i++) {
				int newLevel = layout.getLevel(i);
				if (newLevel != prevLevel) {
					if (j + 2 > levels.length) {
						int temp[] = levels;
						levels = new int[levels.length * 2];
						System.arraycopy(temp, 0, levels, 0, temp.length);
					}
					levels[j++] = newLevel;
					levels[j++] = offset;
					prevLevel = newLevel;
				}
				offset++;
			}
			// levels was initialized to be twice as long as bidiText.  we now truncate it.
			if (j != levels.length) {
				int[] newLevels = new int[j];
				System.arraycopy(levels, 0, newLevels, 0, j);
				levels = newLevels;
			}
			System.out.println(Arrays.toString(levels));
			assignResults(levels);
		}
	}
	// will cause the fields to be reset for the next string to be processed
	bidiText = null;
	list.clear();
}

/**
 * Sets the paragraph embedding.  The given orientation will be used on TextLayout
 * when determining the Bidi levels.
 * 
 * @param newOrientation SWT.LEFT_TO_RIGHT or SWT.RIGHT_TO_LEFT 
 */
public void setOrientation(int newOrientation) {
	orientation = newOrientation;
}

/**
 * A helper class to hold information about contributions made to this processor.
 * 
 * @author Pratik Shah
 * @since 3.1
 */
private static class StringInfoHolder {
	private FlowFigure fig;
	private int offset, length;
	private StringInfoHolder(FlowFigure fig, int offset, int length) {
		this.fig = fig;
		this.offset = offset;
		this.length = length;
	}
}

}