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
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.TextLayout;

/**
 * A helper class for a BlockFlow that does Bidi evaluation of all the text in that block.
 * <p>
 * WARNING: This class is not intended to be subclassed by clients. This class is for
 * INTERNAL use only.
 * @author Pratik Shah
 * @since 3.1
 */
public final class BidiProcessor {

/*
 * $TODO Workaround for Carbon.  AWT DLL cannot start properly on carbon.
 * Waiting for bug 82104
 */
private static final boolean isMacOS = SWT.getPlatform().equals("carbon"); 

/**
 * A helper class to hold information about contributions made to this processor.
 * 
 * @author Pratik Shah
 * @since 3.1
 */
private static class BidiEntry {
	int begin, end;
	FlowFigure fig;
	BidiEntry(FlowFigure fig, int offset, int length) {
		this.fig = fig;
		this.begin = offset;
		this.end = offset + length;
	}
}

/**
 * A singleton instance.
 */
public static final BidiProcessor INSTANCE = new BidiProcessor();

private StringBuffer bidiText;
private List list = new ArrayList();
private int orientation = SWT.LEFT_TO_RIGHT;

private BidiProcessor() { }

/**
 * FlowFigures can make textual contributions for its block.  All contributions
 * are concatenated (in the order that they were contributed) to make the final
 * String which will determine the Bidi levels.
 * @param fig the figure that is contributing the given text
 * @param str the text contributed by the given figure
 * @see #addControlText(String)
 */
public void add(FlowFigure fig, String str) {
//	if (str.length() == 0)
//		return;
	list.add(new BidiEntry(fig, bidiText.length(), str.length()));
	bidiText.append(str);
}

/**
 * This methods allows FlowFigures to contribute text that may effect the bidi evaluation,
 * but is not text that is visible on the screen.  The bidi level of such text is
 * reported back to the contributing figure.
 * 
 * @param str the contributed text
 */
public void addControlText(String str) {
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
	BidiEntry prevEntry = null, entry = null;
	BidiInfo prevInfo = null, info = null;
	int end = 2, start = 0;
	for (int i = 0; i < list.size(); i++) {
		entry = (BidiEntry)list.get(i);
		info = new BidiInfo();
		
		while (levels[end] < entry.end)
			end += 2;
		
		int levelInfo[];
		if (end == start) {
			levelInfo = new int[1];
			if (prevInfo != null)
				levelInfo[0] = prevInfo.levelInfo[prevInfo.levelInfo.length - 1];
			else
				levelInfo[0] = (orientation == SWT.LEFT_TO_RIGHT) ? 0 : 1;
		} else {
			levelInfo = new int[end - start - 1];
			System.arraycopy(levels, start + 1, levelInfo, 0, levelInfo.length);
		}
		for (int j = 1; j < levelInfo.length; j += 2)
			levelInfo[j] -= entry.begin;
		info.levelInfo = levelInfo;

		// Compare current and previous for joiners, and commit previous BidiInfo.
		if (prevEntry != null) {
			if (// if we started in the middle of a level run
					levels[start] < entry.begin
					// and the level run is odd
					&& levels[start + 1] % 2 == 1
					// and the first character of this figure is Arabic
					&& isJoiner(entry.begin)
					// and the last character of the previous figure was Arabic
					&& isPreceedingJoiner(entry.begin))
				prevInfo.trailingJoiner = info.leadingJoiner = true;
			prevEntry.fig.setBidiInfo(prevInfo);
		}
		prevEntry = entry;
		prevInfo = info;
		if (entry.end == levels[end])
			start = end;
		else
			start = end - 2;
	}
	if (entry != null)
		entry.fig.setBidiInfo(info);
}

/**
 * @param begin
 * @return
 * @since 3.1
 */
private boolean isJoiner(int begin) {
	return begin < bidiText.length() && isJoiningCharacter(bidiText.charAt(begin));
}

/**
 * @param the character to be evaluated
 * @return <code>true</code> if the given character is Arabic or ZWJ
 */
private boolean isJoiningCharacter(char c) {
	return Character.getDirectionality(c) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
			|| c == BidiChars.ZWJ;
}

/**
 * @param begin
 * @return
 * @since 3.1
 */
private boolean isPreceedingJoiner(int begin) {
	return begin > 0 && isJoiningCharacter(bidiText.charAt(begin - 1));
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
 * instance, {1, 0, 2, 5, 1, 9} would mean that characters at index 0-4 are of level 1, 
 * 5-8 of level 2 and the remaining characters (starting at offset 9) are of level 1.
 */
public void process() {
	char[] chars = new char[bidiText.length()];
	bidiText.getChars(0, bidiText.length(), chars, 0);

	if (!isMacOS && !Bidi.requiresBidi(chars, 0, chars.length - 1))
		return;

	int[] levels = new int[15];
	TextLayout layout = FlowUtilities.getTextLayout();
	
	layout.setOrientation(orientation);
	layout.setText(bidiText.toString());
	int j = 0, offset, prevLevel = -1;
	for (offset = 0; offset < chars.length; offset++) {
		int newLevel = layout.getLevel(offset);
		if (newLevel != prevLevel) {
			if (j + 3 > levels.length) {
				int temp[] = levels;
				levels = new int[levels.length * 2 + 1];
				System.arraycopy(temp, 0, levels, 0, temp.length);
			}
			levels[j++] = offset;
			levels[j++] = newLevel;
			prevLevel = newLevel;
		}
	}
	levels[j++] = offset;

	if (j != levels.length) {
		int[] newLevels = new int[j];
		System.arraycopy(levels, 0, newLevels, 0, j);
		levels = newLevels;
	}
	assignResults(levels);
	
	// reset the orientation of the layout, in case it was set to RTL
	layout.setOrientation(SWT.LEFT_TO_RIGHT);
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
	bidiText = new StringBuffer();
	list.clear();
	orientation = newOrientation;
}

}