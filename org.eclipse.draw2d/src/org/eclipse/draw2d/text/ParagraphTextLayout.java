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
 * doesn't fit.  Note that truncation is not supported across multiple figures and
 * with BiDi.  Undesired effects may result if that is the case.
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
 * @param levelInfo the Bidi levels
 * @return the requested segment
 */
private String[] getSegments(String text, int levelInfo[]) {
	if (levelInfo.length == 1)
		return new String[] {text};
 	
	String result[] = new String[levelInfo.length / 2 + 1];
	
	int i;
	int endOffset;
	int beginOffset = 0;

	for (i = 0; i < result.length - 1; i++) {
		endOffset = levelInfo[i * 2 + 1];
		result[i] = text.substring(beginOffset, endOffset);
		beginOffset = endOffset;
	}
	endOffset = text.length();
	result[i] = text.substring(beginOffset, endOffset);
	return result;
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
	/*
	 * Changes to this algorithm should be tested using TextFlowWrapTest.
	 */
	TextFlow textFlow = (TextFlow)getFlowFigure();
	int offset = 0;
	
	List fragments = textFlow.getFragments();
	Font font = textFlow.getFont();
	int fragIndex = 0;
	int fragLength = 0;
	float prevAvgCharWidth;
	LineBox currentLine;
	TextFragmentBox fragment;
	BidiInfo bidi = textFlow.getBidiInfo();
	int levelInfo[] = (bidi == null) ? new int[] {-1} : bidi.levelInfo;
	
	String segment, segments[] = getSegments(textFlow.getText(), levelInfo);

	for (int seg = 0; seg < segments.length; seg++) {
		segment = segments[seg];
		while (segment.length() > 0) {
			fragment = getFragment(fragIndex, fragments);
			prevAvgCharWidth = getAverageCharWidth(fragment);
			fragment.width = 0;
			fragment.length = 0;
			fragment.offset = offset;
			fragment.truncated = false;
			fragment.setBidiLevel(levelInfo[seg * 2]);
			
			//This loop is done at most twice.
			//The second time through, a context.endLine()
			//was requested, and the loop will break.
			while (true) {
				currentLine = context.getCurrentLine();
				if (!currentLine.isOccupied()
						&& context.getConsumeSpaceOnNewLine() 
						&& Character.isWhitespace(segment.charAt(0))) {
					segment = segment.substring(1);
					offset++;
					fragment.offset++;
				}
				fragLength = FlowUtilities.getTextForSpace(
						fragment, segment, font,
						currentLine.getAvailableWidth(),
						prevAvgCharWidth, wrappingStyle);
				
				if (fragment.width <= currentLine.getAvailableWidth()
						|| !currentLine.isOccupied()
						|| context.getContinueOnSameLine())
					break;
				context.endLine();
			}
			if (fragLength != segment.length()) {
				// all the given text did not fit on this line and we might have to wrap
				/*
				 * $TODO:Pratik change to String.regionMatches, or 2 charAt calls.  Note
				 * that this case is a sub-case of the next test which is for whitespace.
				 */
				if (segment.substring(fragLength).startsWith("\r\n")) //$NON-NLS-1$
					// special-case the WinNL character
					fragLength += 2;
				else if (Character.isWhitespace(segment.charAt(fragLength)))
					// if the first character that doesn't fit is a whitespace,
					// we consume it
					fragLength++;
				else if (Character.isWhitespace(segment.charAt(fragLength - 1))) {
					// otherwise, if the last character that fit is a whitespace, we
					// simply don't paint it
					// if the fragment is truncated, this will never be true
					fragment.length--;
					// update the width of the fragment
					FlowUtilities.setupFragment(fragment, font, segment);
				}
				context.addToCurrentLine(fragment);
			} else {
				// all of string fit on the current line
				int lookAheadWidth = -1;
				int availableWidth = 
					context.getCurrentLine().getAvailableWidth() - fragment.width;
				
				if (!FlowUtilities.isLineBreakingMark(segment.charAt(fragLength - 1))
						&& !context.getContinueOnSameLine()) {
					lookAheadWidth = lookAhead(segments, seg + 1);
					if (lookAheadWidth > availableWidth) {
						BreakIterator breakFinder = BreakIterator.getLineInstance();
						breakFinder.setText(segment); //$NON-NLS-1$
						int index = breakFinder.preceding(fragLength - 1);
						if (index > 0) {
							// case: "foo barABC", where "foo bar" is in one figure and "ABC"
							// is in another and there is only enough room to display 
							// "foo barAB" on the current line
							fragLength = index;
							fragment.length = index;
							int oldWidth = fragment.width;
							FlowUtilities.setupFragment(fragment, font, segment);
							lookAheadWidth += oldWidth - fragment.width;
						}
					}
				}
				
				/*
				 *  If the fragment is not going to fit in the available width left on
				 *  this line and it does not start with a whitespace character and if
				 *  this fragment ends in a whitespace character, we need to not paint
				 *  that last character. Note that it is important to use
				 *  string.charAt(length - 1) and not string.charAt(string.length() - 1)
				 *  because we might not be putting the entire string on this line (as in
				 *  the case "foo barABC", as explained in the if statement above).
				 */
				if (Character.isWhitespace(segment.charAt(fragLength - 1))) {
					if (lookAheadWidth == -1)
						lookAheadWidth = lookAhead(segments, seg + 1);
					if (lookAheadWidth > availableWidth) {
						fragment.length--;
						FlowUtilities.setupFragment(fragment, font, segment);
					}
				}

				context.addToCurrentLine(fragment);
				/*
				 * set continueOnSameLine to true if hard-wrapping is being employed, the
				 * last character is not a line-break, and the first character of  the
				 * next fragment is not a line-break either.
				 */
				if (wrappingStyle == WORD_WRAP_HARD 
						&& !FlowUtilities.isLineBreakingMark(segment.charAt(fragLength - 1))) {
					if (lookAheadWidth == -1)
						lookAheadWidth = lookAhead(segments, seg + 1);
					context.setContinueOnSameLine(lookAheadWidth != 0);
				}
				// Since all of the given string fit on this line, we consume any initial
				// space on the new line (regardless of whether this line ended in
				// whitespace or not)
				context.setConsumeSpaceOnNewLine(true);
			}
			
			segment = segment.substring(fragLength);
			offset += fragLength;
			if (segment.length() > 0 || fragment.truncated
					|| (currentLine.getAvailableWidth() <= 0
					&& !context.getContinueOnSameLine()))
				context.endLine();
			fragIndex++;
		}
	}
	//Remove the remaining unused fragments.
	while (fragIndex < fragments.size())
		fragments.remove(fragments.size() - 1);
}

private int lookAhead(String[] strings, int startingIndex) {
	int[] width = new int[1];
	TextFlow textFlow = (TextFlow)getFlowFigure();
	if (startingIndex == strings.length)
		context.getWordWidthFollowing(textFlow, width);
	else {
		String rest = strings[startingIndex];
		for (int k = startingIndex + 1; k < strings.length; k++)
			rest += strings[k];
		if (!textFlow.addLeadingWordWidth(rest, width))
			context.getWordWidthFollowing(textFlow, width);
	}
	return width[0];
}

}