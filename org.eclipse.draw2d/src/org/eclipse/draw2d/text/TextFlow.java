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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;

/**
 * An inline flow that renders a sting of text across one or more lines. A TextFlow must
 * not have any children. It does not provide a {@link FlowContext}.
 * 
 * <P>WARNING: This class is not intended to be subclassed by clients.
 * @author hudsonr
 * @since 2.1 */
public class TextFlow
	extends InlineFlow
{

static final String ELLIPSIS = "..."; //$NON-NLS-1$
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
 * @see org.eclipse.draw2d.text.InlineFlow#createDefaultFlowLayout()
 */
protected FlowFigureLayout createDefaultFlowLayout() {
	return new ParagraphTextLayout(this);
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
//	super.paintFigure(g);
	TextFragmentBox frag;
	for (int i = 0; i < fragments.size(); i++) {
		frag = (TextFragmentBox)fragments.get(i);
//		if (!g.getClip(Rectangle.SINGLETON).intersects(frag))
//			continue;
		String draw;
		if (!frag.truncated)
			draw = text.substring(frag.offset, frag.offset + frag.length);
		else
			draw = text.substring(frag.offset, frag.offset + frag.length) + ELLIPSIS;
		if (!isEnabled()) {
			g.setForegroundColor(ColorConstants.buttonLightest);
			g.drawString(draw, frag.x + 1, frag.y + 1);
			g.setForegroundColor(ColorConstants.buttonDarker);
			g.drawString(draw, frag.x, frag.y);
			g.restoreState();
		} else
			g.drawString(draw, frag.x, frag.y);
	}
}

/**
 * Sets the string being displayed. Causes a <code>revalidate()</code> to occur.
 * @param s The new String.  It cannot be <code>null</code>. */
public void setText(String s) {
	text = s;
	revalidate();
	repaint();
}

}
