/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

import java.text.BreakIterator;

import org.eclipse.swt.SWT;
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
 * @author Pratik Shah
 * @since 2.1
 */
public class TextFlow 
	extends InlineFlow 
{

static final String ELLIPSIS = "..."; //$NON-NLS-1$
private BidiInfo bidiInfo;
private int selectionEnd = -1;
private String text;

/**
 * Constructs a new TextFlow with the empty String.
 * @see java.lang.Object#Object()
 */
public TextFlow() {
	this(new String());
}

/**
 * Constructs a new TextFlow with the specified String.
 * @param s the string
 */
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
	if (text.length() == 0)
		return false;
	if (Character.isWhitespace(text.charAt(0)))
		return true;

	BreakIterator lineBreaker = FlowUtilities.LINE_BREAK;
	text = 'a' + text + 'a';
	lineBreaker.setText(text);
	int index = lineBreaker.next() - 1;
	if (index == 0)
		return true;
	while (Character.isWhitespace(text.charAt(index)))
		index--;
	boolean result = index < text.length() - 1;
	// index should point to the end of the actual text (not including the 'a' that was 
	// appended), if there were no breaks
	if (index == text.length() - 1)
		index--;
	text = text.substring(1, index + 1);

	if (bidiInfo == null)
		width[0] += FlowUtilities.getStringExtents(text, getFont()).width;
	else {
		TextLayout textLayout = FlowUtilities.getTextLayout();
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

private int findNextLineOffset(Point p, int[] trailing) {
	if (getBounds().bottom() <= p.y)
		return -1;

	TextFragmentBox closestBox = null;	
	int index = 0;
	for (int i = fragments.size() - 1; i >= 0; i--) {
		TextFragmentBox box = (TextFragmentBox)fragments.get(i);
		if (box.getBaseline() - box.getLineRoot().contentAscent > p.y 
				&& (closestBox == null
				|| box.getBaseline() < closestBox.getBaseline()
				|| hDistanceBetween(box, p.x) < hDistanceBetween(closestBox, p.x))) {
			closestBox = box;
			index = i;
		}
	}
	if (closestBox != null)
		return findOffset(p, trailing, closestBox, index);
	return -1;
}

private int findOffset(Point p, int[] trailing, TextFragmentBox box, int boxIndex) {
	TextLayout layout = FlowUtilities.getTextLayout();
	layout.setFont(getFont());
	layout.setText(getBidiSubstring(box, boxIndex));
	int x = p.x - box.getX();
	if (isMirrored())
		x = box.getWidth() - x;
	int layoutOffset = layout.getOffset(x, p.y - box.getTextTop(), trailing);
	return box.offset + layoutOffset - getBidiPrefixLength(box, boxIndex);
}

private int findPreviousLineOffset(Point p, int[] trailing) {
	if (getBounds().y > p.y)
		return -1;

	TextFragmentBox closestBox = null;	
	int index = 0;
	for (int i = fragments.size() - 1; i >= 0; i--) {
		TextFragmentBox box = (TextFragmentBox)fragments.get(i);
		if (box.getBaseline() + box.getLineRoot().contentDescent < p.y
				&& (closestBox == null
				|| box.getBaseline() > closestBox.getBaseline()
//				|| (box.getX() <= p.x && box.getX() + box.getWidth() > p.x)
				|| hDistanceBetween(box, p.x) < hDistanceBetween(closestBox, p.x))) {
			closestBox = box;
			index = i;
		}
	}
	if (closestBox != null)
		return findOffset(p, trailing, closestBox, index);
	return -1;
}

int getAscent() {
	FontMetrics fm = FigureUtilities.getFontMetrics(getFont());
	return fm.getHeight() - fm.getDescent();
}

/**
 * Returns the BidiInfo for this figure or <code>null</code>.
 * @return <code>null</code> or the info
 * @since 3.1
 */
public BidiInfo getBidiInfo() {
	return bidiInfo;
}

private int getBidiPrefixLength(TextFragmentBox box, int index) {
	if (box.getBidiLevel() < 1)
		return 0;
	if (index > 0 || !bidiInfo.leadingJoiner)
		return 1;
	return 2;
}

/**
 * @param box which fragment
 * @param index the fragment index
 * @return the bidi string for that fragment
 * @since 3.1
 */
private String getBidiSubstring(TextFragmentBox box, int index) {
	if (bidiInfo == null || box.getBidiLevel() < 1)
		return getText().substring(box.offset, box.offset + box.length);
	
	StringBuffer buffer = new StringBuffer(box.length + 3);
	buffer.append(box.isRightToLeft() ? BidiChars.RLO : BidiChars.LRO);
	if (index == 0 && bidiInfo.leadingJoiner)
		buffer.append(BidiChars.ZWJ);
	buffer.append(getText().substring(box.offset, box.offset + box.length));
	if (index == fragments.size() - 1 && bidiInfo.trailingJoiner)
		buffer.append(BidiChars.ZWJ);
	return buffer.toString();
}

/**
 * Returns the CaretInfo in absolute coordinates. The offset must be between 0 and the
 * length of the String being displayed.
 * @since 3.1
 * @param offset the location in this figure's text
 * @param trailing true if the caret is being placed after the offset
 * @exception IllegalArgumentException If the offset is not between <code>0</code> and the
 * length of the string inclusively
 * @return the caret bounds relative to this figure
 */
public CaretInfo getCaretPlacement(int offset, boolean trailing) {
	if (offset < 0 || offset > getText().length())
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

	String fragString = getBidiSubstring(box, i);
	if (bidiInfo != null)
		offset += getBidiPrefixLength(box, i);

	layout.setText(fragString);
	Point where = new Point(layout.getLocation(offset, trailing));
	if (isMirrored())
		where.x = box.width - where.x;
	FontMetrics metrics = FigureUtilities.getFontMetrics(getFont());
	where.translate(box.getX(), box.getTextTop());
	CaretInfo info = new CaretInfo(where.x, where.y,
			metrics.getAscent(), metrics.getHeight() - metrics.getAscent());
	translateToAbsolute(info);
	return info;
}

int getDescent() {
	return FigureUtilities.getFontMetrics(getFont()).getDescent();
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
	TextFragmentBox box;
	for (int i = 0; i < fragments.size(); i++) {
		box = (TextFragmentBox)fragments.get(i);
		if (y >= box.getBaseline() - box.getAscentWithBorder()
				&& y < box.getBaseline() + box.getDescentWithBorder())
			return box.offset;
	}
	return -1;
}

/**
 * Returns the last caret position which occupies the line at the given y location. 
 * The y location is relative to this figure.  If no fragment occupies that y coordinate,
 * <code>-1</code> is returned.
 * @since 3.1
 * @param baseline the baseline's y coordinate
 * @return -1 of the last  offset at the given baseline
 */
public int getLastOffsetForLine(int baseline) {
	TextFragmentBox box;
	//LineRoot root;
	for (int i = fragments.size() - 1; i >= 0; i--) {
		box = (TextFragmentBox)fragments.get(i);
		//root = box.getLineRoot();
		if (baseline >= box.getBaseline() - box.getAscentWithBorder()
				&& baseline < box.getBaseline() + box.getDescentWithBorder())
			return box.offset + box.length;
	}
	return -1;
}

/**
 * Returns the offset nearest the given point either up or down one line.  If no offset
 * is found, -1 is returned.  <code>trailing[0]</code> will be set to 1 if the reference
 * point is closer to the trailing edge of the offset than it is to the leading edge.
 * @since 3.1
 * @param p a reference point
 * @param down <code>true</code> if the search is down
 * @param trailing an int array
 * @return the next offset or <code>-1</code>
 */
public int getNextOffset(Point p, boolean down, int[] trailing) {
	return down ? findNextLineOffset(p, trailing) : findPreviousLineOffset(p, trailing);
}

/**
 * Returns the next offset which is visible in at least one fragment or -1 if there is
 * not one. A visible offset means that the character or the one preceding it is
 * displayed, which implies that a caret can be positioned at such an offset.  This is
 * useful for advancing a caret past characters which resulted in a line wrap.
 * 
 * @param offset the reference offset
 * @return the next offset which is visible
 * @since 3.1
 */
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
 * this figure. The offset will be between 0 and <code>getText().length()</code> 
 * inclusively, if a fragment is found.  If the point in on the same line(s) as this
 * TextFlow, the index of the closest fragment is returned.  Otherwise, -1 will be 
 * returned.   <code>trailing[0]</code> will be set to 1 if the reference
 * point is closer to the trailing edge of the offset than it is to the leading edge.
 * <p>
 * Trailing information for bidi applications is provided by SWT's {@link TextLayout}.
 * 
 * @since 3.1
 * @param p a point relative to this figure
 * @param trailing trailing information
 * @return the offset in the string or <code>-1</code>
 */
public int getOffset(Point p, int trailing[]) {
	TextFragmentBox closestBox = null;
	int index = 0;
	for (int i = fragments.size() - 1; i >= 0; i--) {
		TextFragmentBox box = (TextFragmentBox)fragments.get(i);
		int closestDistance = closestBox == null 
				? Integer.MAX_VALUE : vDistanceBetween(closestBox, p.y);
		int vDistance = vDistanceBetween(box, p.y);
		if (vDistance < closestDistance
				|| (vDistance == closestDistance 
				&& hDistanceBetween(box, p.x) < hDistanceBetween(closestBox, p.x))) {
			closestBox = box;
			index = i;
		}
	}
	if (closestBox != null)
		return findOffset(p, trailing, closestBox, index);
	return -1;
}

/**
 * Returns the previous offset which is visible in at least one fragment or -1 if there
 * is not one. See {@link #getNextVisibleOffset(int)} for more. 
 * 
 * @param offset a reference offset
 * @return -1 or the previous offset which is visible
 * @since 3.1
 */

public int getPreviousVisibleOffset(int offset) {
	TextFragmentBox box;
	if (offset == -1)
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
 * @return the String being displayed
 */
public String getText() {
	return text;
}

int getVisibleAscent() {
	if (getBorder() instanceof FlowBorder) {
		FlowBorder border = (FlowBorder)getBorder();
		return border.getInsets(this).top + getAscent();
	}
	return getAscent();
}

int getVisibleDescent() {
	if (getBorder() instanceof FlowBorder) {
		FlowBorder border = (FlowBorder)getBorder();
		return border.getInsets(this).bottom + getDescent();
	}
	return getDescent();
}

private int hDistanceBetween(TextFragmentBox box, int x) {
	if (x < box.getX())
		return box.getX() - x;
	return Math.max(0, x - (box.getX() + box.getWidth()));
}

/**
 * Returns <code>true</code> if a portion if the text is truncated using ellipses ("...").
 * @return <code>true</code> if the text is truncated with ellipses
 */
public boolean isTextTruncated() {
	for (int i = 0; i < fragments.size(); i++) {
		if (((TextFragmentBox)fragments.get(i)).isTruncated())
			return true;
	}
	return false;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics g) {
	TextFragmentBox frag;
	g.getClip(Rectangle.SINGLETON);
	int yStart = Rectangle.SINGLETON.y;
	int yEnd = Rectangle.SINGLETON.bottom();
		
	for (int i = 0; i < fragments.size(); i++) {
		frag = (TextFragmentBox)fragments.get(i);
		g.setBackgroundColor(ColorConstants.red);
//		g.drawLine(frag.getX(), frag.getLineRoot().getVisibleTop(),
//				frag.getWidth() + frag.getX(), frag.getLineRoot().getVisibleTop());
//		g.drawLine(frag.getX(), frag.getBaseline(), frag.getWidth() + frag.getX(), frag.getBaseline());
		if (frag.offset == -1)
			continue;
		//Loop until first visible fragment
		if (yStart > frag.getLineRoot().getVisibleBottom() + 1)//The + 1 is for disabled text
			continue;
		//Break loop at first non-visible fragment
		if (yEnd < frag.getLineRoot().getVisibleTop())
			break;

		String draw = getBidiSubstring(frag, i);
		
		if (frag.isTruncated())
			draw += ELLIPSIS;

		if (!isEnabled()) {
			Color fgColor = g.getForegroundColor();
			g.setForegroundColor(ColorConstants.buttonLightest);
			paintText(g, draw,
					frag.getX() + 1,
					frag.getBaseline() - getAscent() + 1,
					frag.getBidiLevel());
			g.setForegroundColor(ColorConstants.buttonDarker);
			paintText(g, draw,
					frag.getX(),
					frag.getBaseline() - getAscent(),
					frag.getBidiLevel());
			g.setForegroundColor(fgColor);
		} else {
			paintText(g, draw,
					frag.getX(),
					frag.getBaseline() - getAscent(),
					frag.getBidiLevel());
		}
	}
}

/**
 * @see InlineFlow#paintSelection(org.eclipse.draw2d.Graphics)
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
		if (selectionStart <= frag.offset && selectionEnd >= frag.offset + frag.length) {
			int y = frag.getLineRoot().getVisibleTop();
			int height = frag.getLineRoot().getVisibleBottom() - y;
			graphics.fillRectangle(frag.getX(), y, frag.getWidth(), height);
		} else if (selectionEnd > frag.offset && selectionStart < frag.offset + frag.length) {
			int prefixCorrection = getBidiPrefixLength(frag, i);
			String text = getBidiSubstring(frag, i);

			TextLayout layout = FlowUtilities.getTextLayout();
			layout.setFont(graphics.getFont());
			layout.setText(text);
			Rectangle rect = new Rectangle();
			rect.x = layout.getLocation(
					Math.max(selectionStart - frag.offset, 0) + prefixCorrection,
					false).x;
			rect.union(layout.getLocation(Math.min(selectionEnd - frag.offset,
					frag.length) - 1 + prefixCorrection, true).x, 0);
			rect.width--;
			if (isMirrored())
				rect.x = frag.getWidth() - (rect.x + rect.width);
			rect.translate(frag.getX(), frag.getLineRoot().getVisibleTop());
			rect.height = frag.getLineRoot().getVisibleBottom() - rect.y;
			graphics.fillRectangle(rect);
		}
	}
}

private void paintText(Graphics g, String draw, int x, int y, int bidiLevel) {
	if (bidiLevel < 1) {
		g.drawText(draw, x, y);
	} else {
		TextLayout tl = FlowUtilities.getTextLayout();
		if (isMirrored())
			tl.setOrientation(SWT.RIGHT_TO_LEFT);
		tl.setFont(g.getFont());
		tl.setText(draw);
		g.drawTextLayout(tl, x, y);
	}
}

/**
 * @see org.eclipse.draw2d.text.FlowFigure#setBidiInfo(org.eclipse.draw2d.text.BidiInfo)
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
 * Sets the text being displayed. The string may not be <code>null</code>.
 * @param s The new text
 */
public void setText(String s) {
	if (s != null && !s.equals(text)) {
		text = s;
		revalidateBidi(this);
		repaint();
	}
}

private int vDistanceBetween(TextFragmentBox box, int y) {
	int top = box.getBaseline() - box.getLineRoot().contentAscent;
	if (y < top)
		return top - y;
	return Math.max(0, y - (box.getBaseline() + box.getLineRoot().contentDescent));
}

}