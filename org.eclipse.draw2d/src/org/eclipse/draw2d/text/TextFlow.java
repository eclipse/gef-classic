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
 * @since 2.1 */
public class TextFlow
	extends InlineFlow
{

static final String ELLIPSIS = "..."; //$NON-NLS-1$

static final int SELECT_ALL = 1;
static final int SELECT_PARTIAL = 2;
private int selectBegin = -1;
private int selectEnd = -1;
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
 * A TextFlow contributes its text.
 * @see org.eclipse.draw2d.text.FlowFigure#contributeBidi(org.eclipse.draw2d.text.BidiProcessor)
 */
protected void contributeBidi(BidiProcessor proc) {
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
	for (int i = 0; i < fragments.size(); i++) {
		box = (TextFragmentBox)fragments.get(i);
		if (box.getBaseline() > p.y)
			break;
		box = null;
	}
	if (box == null)
		return -1;
	TextLayout layout = FlowUtilities.getTextLayout();
	layout.setOrientation(SWT.LEFT_TO_RIGHT);
	layout.setFont(getFont());
	layout.setText(addJoiners(text).substring(box.offset, box.offset + box.length));
	int trailing[] = new int[1];
	int layoutOffset = layout.getOffset(p.x - box.x, p.y - box.y, trailing) + trailing[0];
	layout.setText(""); //$NON-NLS-1$
	return box.offset + layoutOffset;
}

private int findPreviousLineOffset(Point p) {
	if (getBounds().y > p.y)
		return -1;

	TextFragmentBox box = null;
	for (int i = fragments.size() - 1; i >= 0; i--) {
		box = (TextFragmentBox)fragments.get(i);
		if (box.getBaseline() < p.y)
			break;
	}
	if (box == null)
		return -1;
	TextLayout layout = FlowUtilities.getTextLayout();
	layout.setOrientation(SWT.LEFT_TO_RIGHT);
	layout.setFont(getFont());
	layout.setText(addJoiners(text).substring(box.offset, box.offset + box.length));
	int trailing[] = new int[1];
	int layoutOffset = layout.getOffset(p.x - box.x, p.y - box.y, trailing) + trailing[0];
	layout.setText(""); //$NON-NLS-1$
	return box.offset + layoutOffset; 
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
	
	offset -= box.offset;
	offset = Math.min(box.length, offset);
	
	TextLayout layout = FlowUtilities.getTextLayout();
	layout.setOrientation(SWT.LEFT_TO_RIGHT);
	layout.setFont(getFont());
	String substring = addJoiners(text).substring(box.offset, box.offset + box.length);
	layout.setText(substring);
	Point where = new Point(layout.getLocation(offset, trailing));
	layout.setText(""); //$NON-NLS-1$
	
	FontMetrics fm = FigureUtilities.getFontMetrics(getFont());
	return new Rectangle(
			where.x + box.x,
			where.y + box.y,
			1,
			fm.getAscent() + fm.getLeading() + fm.getDescent());
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

/**
 * Returns the textual offset nearest the specified point. The must be relative to this
 * figure. If the point is not inside any fragment, <code>-1</code> is returned. 
 * Otherwise the offset will be between 0 and <code>getText().length()</code> inclusively.
 * 
 * @since 3.1
 * @param p a point relative to this figure
 * @return the offset in the string or <code>-1</code>
 */
public int getOffset(Point p, int trailing[]) {
	if (!getBounds().contains(p))
		return -1;
	for (int i = fragments.size() - 1; i >= 0; i--) {
		TextFragmentBox box = (TextFragmentBox)fragments.get(i);
		if (!box.containsPoint(p.x, p.y))
			continue;
		String substring = addJoiners(text).substring(box.offset, box.offset + box.length);
		TextLayout layout = FlowUtilities.getTextLayout();
		layout.setOrientation(SWT.LEFT_TO_RIGHT);
		layout.setFont(getFont());
		layout.setText(substring);
		int result = layout.getOffset(p.x - box.x, p.y - box.y, trailing);
		layout.setText(""); //$NON-NLS-1$
		return result + trailing[0] + box.offset;
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
	String text = addJoiners(getText());
	
	int selectType;
	for (int i = 0; i < fragments.size(); i++) {
		frag = (TextFragmentBox)fragments.get(i);
		if (yStart > frag.y + frag.getHeight() + 1)//The + 1 is for disabled text
			continue;
		if (yEnd < frag.y)
			break;
		String draw;
		
		if (selectBegin == -1)
			selectType = 0;
		else if (selectEnd < frag.offset)
			selectType = 0;
		else if (selectBegin > frag.offset + frag.length)
			selectType = 0;
		else {
			if (selectBegin <= frag.offset && selectEnd >= frag.offset + frag.length)
				selectType = SELECT_ALL;
			else
				selectType = SELECT_PARTIAL;
		}

		if (frag.truncated)
			draw = text.substring(frag.offset, frag.offset + frag.length) + ELLIPSIS;
		else
			draw = text.substring(frag.offset, frag.offset + frag.length);

		// Insert RLE if this fragment has RTL text.
		if (frag.isBidi())
			draw = "\u202b" + draw; //$NON-NLS-1$

		if (!isEnabled()) {
			Color fgColor = g.getForegroundColor();
			g.setForegroundColor(ColorConstants.buttonLightest);
			paintText(g, draw, frag.x + 1, frag.y + 1, -1, -1, 0, frag.isBidi());
			g.setForegroundColor(ColorConstants.buttonDarker);
			paintText(g, draw, frag.x, frag.y, -1, -1, 0, frag.isBidi());
			g.setForegroundColor(fgColor);
		} else {
			int start = -1, end = -1;
			if (selectType == SELECT_ALL) {
				start = 0;
				end = frag.length - 1;
			} else if (selectType == SELECT_PARTIAL) {
				start = selectBegin - frag.offset;
				end = selectEnd - frag.offset;
			}
			paintText(g, draw, frag.x, frag.y, start, end, selectType, frag.isBidi());
		}
	}
}

// Paints the text based on selection and Bidi level.  Uses TextLayout as needed.
private void paintText(Graphics g, String text, int x, int y, int selectionStart, 
		int selectionEnd, int selectionType, boolean isBidi) {

	if (isBidi || selectionType == SELECT_PARTIAL) {
		// Case of RTL text and/or partial selection
		TextLayout layout = FlowUtilities.getTextLayout();
		layout.setOrientation(SWT.LEFT_TO_RIGHT);
		layout.setFont(getFont());
		layout.setText(text);
		g.drawTextLayout(layout, x, y, selectionStart, selectionEnd, null, null);
		// set the text to an empty string so that the current string is not held in memory
		layout.setText(""); //$NON-NLS-1$
	} else if (selectionType == SELECT_ALL) {
		// Case of complete selection (and no RTL text)
		Color fg = g.getForegroundColor();
		Color bg = g.getBackgroundColor();
		g.setForegroundColor(ColorConstants.menuForegroundSelected);
		g.setBackgroundColor(ColorConstants.menuBackgroundSelected);
		g.fillString(text, x, y);
		g.setForegroundColor(fg);
		g.setBackgroundColor(bg);
	} else
		// Case of no selection
		g.drawString(text, x, y);
}

/**
 * Sets the extent of selection.  The selection range is inclusive.  For example, the
 * range [0, 0] indicates that the first character is selected.
 * @param begin the begin offset
 * @param end the end offset
 * @since 3.1
 */
public void setSelection(int begin, int end) {
	boolean repaint = false;
	end--;
	if (selectBegin == begin) {
		if (selectEnd == end)
			return;
		repaint = true;
	} else
		repaint = selectBegin != selectEnd || begin != end;

	selectBegin = begin;
	selectEnd = end;
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