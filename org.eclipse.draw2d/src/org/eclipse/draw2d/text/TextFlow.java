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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.TextLayout;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An inline flow that renders a string of text across one or more lines. A TextFlow must
 * not have any children. It does not provide a {@link FlowContext}.
 * <p>
 * The TextFlow's fragments are sized using the text plus any required joiners.  Clients
 * should add the joiners to the text before breaking it up according to fragments.
 * <P>
 * WARNING: This class is not intended to be subclassed by clients.
 * @author hudsonr
 * @since 2.1 */
public class TextFlow extends InlineFlow {

static final String ELLIPSIS = "..."; //$NON-NLS-1$

static final char LRO = '\u202d';
static final char RLO = '\u202e';

static final int SELECT_ALL = 1;
static final int SELECT_PARTIAL = 2;
static final String ZWJ = "\u200d";
private BidiInfo bidiInfo;

private int selectionEnd = -1;
private String text;

/**
 * Constructs a new TextFlow with the empty String.
 * @see java.lang.Object#Object() */
public TextFlow() {
	this(new String());
}

/**
 * Constructs a new TextFlow with the specified String.
 * @param s the string */
public TextFlow(String s) {
	text = s;
}

/**
 * Returns the width of the text until the first line-break.
 * @see org.eclipse.draw2d.text.FlowFigure#addLeadingWordRequirements(int[])
 */
public boolean addLeadingWordRequirements(int[] width) {
	return addLeadingWordWidth(getText(), width);
}

/**
 * Calculates the width taken up by the given text before a line-break is encountered.
 * 
 * @param text the text in which the break is to be found
 * @param width the width before the next line-break (if one's found; the width of all
 * the given text, otherwise) will be added on to the first int in the given array
 * @return <code>true</code> if a line-break was found
 * @since 3.1
 */
boolean addLeadingWordWidth(String text, int[] width) {
	// Changes to this algorithm should be verified with LookAheadTest
	BreakIterator spaceFinder = BreakIterator.getLineInstance();
	text = "a" + text + "a"; //$NON-NLS-1$ //$NON-NLS-2$
	spaceFinder.setText(text);
	int index = FlowUtilities.findPreviousNonWS(text, spaceFinder.next());
	boolean result = index < text.length() - 1;
	if (index == text.length() - 1)
		index--;
	// An optimization to prevent unnecessary invocation of String.substring and 
	// getStringExtents()
	if (index + 1 == 1)
		return result;
	text = text.substring(1, index + 1);
	
	if (bidiInfo == null)
		width[0] += FlowUtilities.getStringExtents(text, getFont()).width - 1;
	else {
		org.eclipse.swt.graphics.TextLayout textLayout = FlowUtilities.getTextLayout();
		textLayout.setFont(getFont());
		textLayout.setText(text);
		width[0] += textLayout.getBounds().width;
	}
	return result;
}

/**
 * A TextFlow contributes its text.
 * @see org.eclipse.draw2d.text.FlowFigure#contributeBidi(org.eclipse.draw2d.text.BidiProcessor)
 */
protected void contributeBidi(BidiProcessor proc) {
	bidiInfo = null;
	proc.add(this, getText());
}

/**
 * @see org.eclipse.draw2d.text.InlineFlow#createDefaultFlowLayout()
 */
protected FlowFigureLayout createDefaultFlowLayout() {
	return new ParagraphTextLayout(this);
}

private int findNextLineOffset(Point p) {
	if (getBounds().bottom() < p.y)
		return -1;

	TextFragmentBox box = null;

	int i;
	for (i = 0; i < fragments.size(); i++) {
		box = (TextFragmentBox)fragments.get(i);
		if (box.y > p.y)
			break;
		box = null;
	}
	if (box == null)
		return -1;
	TextLayout layout = FlowUtilities.getTextLayout();
	layout.setFont(getFont());
	
	String fragString = getBidiSubstring(box, i);
	layout.setText(fragString);
	int trailing[] = new int[1];
	int layoutOffset = layout.getOffset(p.x - box.x, p.y - box.y, trailing) + trailing[0];
	layout.setText(""); //$NON-NLS-1$
	return box.offset + layoutOffset;
}

private int findPreviousLineOffset(Point p) {
	if (getBounds().y > p.y)
		return -1;

	TextFragmentBox box = null;
	
	int i;
	for (i = fragments.size() - 1; i >= 0; i--) {
		box = (TextFragmentBox)fragments.get(i);
		if (box.getBaseline() < p.y)
			break;
		box = null;
	}
	if (box == null)
		return -1;
	TextLayout layout = FlowUtilities.getTextLayout();
	layout.setFont(getFont());
	
	String fragString;
	if (bidiInfo == null)
		fragString = text.substring(box.offset, box.offset + box.length);
	else {
		fragString = getBidiSubstring(box, i);
	}

	layout.setText(fragString);
	int trailing[] = new int[1];
	int layoutOffset = layout.getOffset(p.x - box.x, p.y - box.y, trailing) + trailing[0];
	layout.setText(""); //$NON-NLS-1$
	return box.offset + layoutOffset;
}

/**
 * Returns the BidiInfo for this figure or <code>null</code>.
 * @return <code>null</code> or the info
 * @since 3.1
 */
public BidiInfo getBidiInfo() {
	return bidiInfo;
}

/**
 * @param box which fragment
 * @param index the fragment index
 * @return the bidi string for that fragment
 * @since 3.1
 */
private String getBidiSubstring(TextFragmentBox box, int index) {
	if (bidiInfo == null)
		return text.substring(box.offset, box.offset + box.length);
	
	return (box.isRightToLeft() ? RLO : LRO) 
			+ ((index == 0 && bidiInfo.leadingJoiner) ? ZWJ : "")
			+ text.substring(box.offset, box.offset + box.length)
			+ ((index == fragments.size() - 1 && bidiInfo.trailingJoiner) ? ZWJ : "");
}

/**
 * Returns the rectangular bounds for placing a {@link org.eclipse.swt.widgets.Caret
 * Caret} at the given offset.  The offset must be between 0 and the length of the String
 * being displayed.
 * @since 3.1
 * @param offset the location in this figures text
 * @param trailing true if the caret is being placed after the offset
 * @exception IllegalArgumentException If the offset is not between <code>0</code> and the
 * length of the string inclusively
 * @return the caret bounds relative to this figure
 */
public Rectangle getCaretPlacement(int offset, boolean trailing) {
	if (offset < 0 || offset > text.length())
		throw new IllegalArgumentException("Offset: " + offset //$NON-NLS-1$
				+ " is invalid"); //$NON-NLS-1$
	
	int i = fragments.size();
	TextFragmentBox box;
	do
		box = (TextFragmentBox)fragments.get(--i);
	while (offset < box.offset && i > 0);
	if (trailing && box.offset + box.length <= offset) {
		box = (TextFragmentBox)fragments.get(++i);
		offset = box.offset;
		trailing = false;
	}
	
	offset -= box.offset;
	offset = Math.min(box.length, offset);
	
	TextLayout layout = FlowUtilities.getTextLayout();
	layout.setFont(getFont());

	String fragString;
	if (bidiInfo == null)
		fragString = text.substring(box.offset, box.offset + box.length);
	else {
		offset++;
		if (i == 0 && bidiInfo.leadingJoiner)
			offset++;
		fragString = getBidiSubstring(box, i);
	}

	layout.setText(fragString);
	Point where = new Point(layout.getLocation(offset, trailing));
	layout.setText(""); //$NON-NLS-1$
	
	FontMetrics fm = FigureUtilities.getFontMetrics(getFont());
	return new Rectangle(
			where.x + box.x,
			where.y + box.y,
			1,
			fm.getHeight());
}

/**
 * Returns the first caret position which occupies the line at the given y location. 
 * The y location is relative to this figure.  If no fragment occupies that y coordinate,
 * <code>-1</code> is returned.
 * @since 3.1
 * @param y the baseline's y coordinate
 * @return -1 of the first offset at the given baseline
 */
public int getFirstOffsetForLine(int y) {
	for (int i = 0; i < fragments.size(); i++) {
		TextFragmentBox box = (TextFragmentBox)fragments.get(i);
		if (y >= box.y && y < box.y + box.getHeight())
			return box.offset;
	}
	return -1;
}

/**
 * Returns the last caret position which occupies the line at the given y location. 
 * The y location is relative to this figure.  If no fragment occupies that y coordinate,
 * <code>-1</code> is returned.
 * @since 3.1
 * @param y the baseline's y coordinate
 * @return -1 of the last  offset at the given baseline
 */
public int getLastOffsetForLine(int y) {
	for (int i = fragments.size() - 1; i >= 0; i--) {
		TextFragmentBox box = (TextFragmentBox)fragments.get(i);
		if (y >= box.y && y < box.y + box.getHeight())
			return box.offset + box.length;
	}
	return -1;
}

/**
 * Returns the offset nearest the given point either up or down one line.  If no offset
 * is found, -1 is returned.
 * @since 3.1
 * @param p a reference point
 * @param down <code>true</code> if the search is down
 * @return the next offset or <code>-1</code>
 */
public int getNextOffset(Point p, boolean down) {
	if (down)
		return findNextLineOffset(p);
	return findPreviousLineOffset(p);
}

public int getNextVisibleOffset(int offset) {
	TextFragmentBox box;
	for (int i = 0; i < fragments.size(); i++) {
		box = (TextFragmentBox)fragments.get(i);
		if (box.offset + box.length <= offset)
			continue;
		return Math.max(box.offset, offset + 1);
	}
	return -1;
}

/**
 * Returns the textual offset nearest the specified point. The point must be relative to
 * this figure. If the point is not inside any fragment, <code>-1</code> is returned. 
 * Otherwise the offset will be between 0 and <code>getText().length()</code> inclusively.
 * <p>
 * Trailing information for bidi applications is provided by SWT's {@link TextLayout}.
 * 
 * @since 3.1
 * @param p a point relative to this figure
 * @param trailing trailing information
 * @return the offset in the string or <code>-1</code>
 */
public int getOffset(Point p, int trailing[]) {
	if (!getBounds().contains(p))
		return -1;
	for (int i = fragments.size() - 1; i >= 0; i--) {
		TextFragmentBox box = (TextFragmentBox)fragments.get(i);
		if (!box.containsPoint(p.x, p.y))
			continue;
		
		String substring = text.substring(box.offset, box.offset + box.length);
		int bidiCorrection = 0;
		if (bidiInfo != null) {
			if (i == 0 && bidiInfo.leadingJoiner)
				bidiCorrection = -2;
			else
				bidiCorrection = -1;
			substring = (box.isRightToLeft() ? RLO : LRO)
				+ (bidiCorrection == -2 ? ZWJ : "")
				+ substring
				+ ((i == fragments.size() - 1 && bidiInfo.trailingJoiner) ? ZWJ : "");
		}
		
		TextLayout layout = FlowUtilities.getTextLayout();
		layout.setFont(getFont());
		layout.setText(substring);
		int result = layout.getOffset(p.x - box.x, p.y - box.y, trailing);
		layout.setText(""); //$NON-NLS-1$
		return result + trailing[0] + box.offset + bidiCorrection;
	}
	return -1;
}

public int getPreviousVisibleOffset(int offset) {
	TextFragmentBox box;
	if (offset == - 1)
		offset = Integer.MAX_VALUE;
	for (int i = fragments.size() - 1; i >= 0; i--) {
		box = (TextFragmentBox)fragments.get(i);
		if (box.offset >= offset)
			continue;
		return Math.min(box.offset + box.length, offset - 1);
	}
	return -1;
}

/**
 * @return the String being displayed */
public String getText() {
	return text;
}

/**
 * Returns <code>true</code> if a portion if the text is truncated using ellipses ("...").
 * @return <code>true</code> if the text is truncated with ellipses
 */
public boolean isTextTruncated() {
	for (int i = 0; i < fragments.size(); i++) {
		if (((TextFragmentBox)fragments.get(i)).truncated)
			return true;
	}
	return false;
}

/** * @see org.eclipse.draw2d.Figure#paintFigure(Graphics) */
protected void paintFigure(Graphics g) {
	TextFragmentBox frag;
	g.getClip(Rectangle.SINGLETON);
	int yStart = Rectangle.SINGLETON.y;
	int yEnd = Rectangle.SINGLETON.bottom();
	
	for (int i = 0; i < fragments.size(); i++) {
		frag = (TextFragmentBox)fragments.get(i);
		//Loop until first visible fragment
		if (yStart > frag.y + frag.getHeight() + 1)//The + 1 is for disabled text
			continue;
		//Break loop at first non-visible fragment
		if (yEnd < frag.y)
			break;

		String draw = getBidiSubstring(frag, i);
			
		if (frag.truncated)
			draw += ELLIPSIS;

		if (!isEnabled()) {
			Color fgColor = g.getForegroundColor();
			g.setForegroundColor(ColorConstants.buttonLightest);
			paintText(g, draw, frag.x + 1, frag.y + 1, frag.getBidiLevel());
			g.setForegroundColor(ColorConstants.buttonDarker);
			paintText(g, draw, frag.x, frag.y, frag.getBidiLevel());
			g.setForegroundColor(fgColor);
		} else {
			paintText(g, draw, frag.x, frag.y, frag.getBidiLevel());
		}
	}
}

/**
 * @see org.eclipse.draw2d.text.InlineFlow#paintSelection(org.eclipse.draw2d.Graphics)
 */
protected void paintSelection(Graphics graphics) {
	if (selectionStart == -1)
		return;
	graphics.setXORMode(true);
	graphics.setBackgroundColor(ColorConstants.white);
	
	TextFragmentBox frag;
	for (int i = 0; i < fragments.size(); i++) {
		frag = (TextFragmentBox)fragments.get(i);
		//Loop until first visible fragment
		if (frag.offset + frag.length <= selectionStart)//The + 1 is for disabled text
			continue;
		if (frag.offset > selectionEnd)
			return;
		if (selectionStart <= frag.offset && selectionEnd >= frag.offset + frag.length)
			graphics.fillRectangle(frag.x, frag.y, frag.getWidth(), frag.getHeight());
		else if (selectionEnd > frag.offset && selectionStart < frag.offset + frag.length) {
			int prefixCorrection = 0;
			String text = getText().substring(frag.offset, frag.offset + frag.length);
			if (frag.bidiLevel != -1) {
				if (i == 0 && bidiInfo.leadingJoiner)
					prefixCorrection = 2;
				else
					prefixCorrection = 1;
				text = (frag.bidiLevel % 2 == 0 ? LRO : RLO)
						+ (prefixCorrection == 1 ? "" : ZWJ)
						+ text
						+ (i == fragments.size() - 1 && bidiInfo.trailingJoiner ? ZWJ : "");
			}

			TextLayout layout = FlowUtilities.getTextLayout();
			layout.setFont(graphics.getFont());
			layout.setText(text);
			Rectangle rect = new Rectangle();
			rect.setLocation(layout.getLocation(
					Math.max(selectionStart - frag.offset, 0) + prefixCorrection, false).x, 0);
			rect.union(layout.getLocation(Math.min(selectionEnd - frag.offset,
					frag.offset + frag.length) - 1 + prefixCorrection, true).x, 0);
			rect.width--;
			rect.height = frag.getHeight();
			rect.translate(frag.x, frag.y);
			graphics.fillRectangle(rect);
		}
	}
}

private void paintText(Graphics g, String draw, int x, int y, int bidiLevel) {
	if (bidiLevel == -1) {
		g.drawString(draw, x, y);
	} else {
		if ((bidiLevel % 2) == 1)
			draw = LRO + draw;
		else
			draw = RLO + draw;
		TextLayout tl = FlowUtilities.getTextLayout();
		tl.setFont(g.getFont());
		tl.setText(draw);
		g.drawTextLayout(tl, x, y);
		// tl.setText("");
	}
}

/**
 * @see org.eclipse.draw2d.text.FlowFigure#setBidiValues(int[])
 */
public void setBidiInfo(BidiInfo info) {
	this.bidiInfo = info;
}

/**
 * Sets the extent of selection.  The selection range is inclusive.  For example, the
 * range [0, 0] indicates that the first character is selected.
 * @param start the start offset
 * @param end the end offset
 * @since 3.1
 */
public void setSelection(int start, int end) {
	boolean repaint = false;

	if (selectionStart == start) {
		if (selectionEnd == end)
			return;
		repaint = true;
	} else
		repaint = selectionStart != selectionEnd || start != end;

	selectionStart = start;
	selectionEnd = end;
	if (repaint)
		repaint();
}

/**
 * Sets the string being displayed. Causes a <code>revalidate()</code> to occur.
 * @param s The new String.  It cannot be <code>null</code>. */
public void setText(String s) {
	if (s != null && !s.equals(text)) {
		text = s;
		revalidateBidi(this);
		revalidate();
		repaint();
	}
}

}