package org.eclipse.draw2d.text;

import org.eclipse.draw2d.*;

/**
 * An inline flow that renders a sting of text across one or more lines. A TextFlow must
 * not have any children. It does not provide a {@link FlowContext}.
 * @author hudsonr
 * @since 2.1 */
public class TextFlow
	extends InlineFlow
{

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

/** * @see org.eclipse.draw2d.Figure#paintFigure(Graphics) */
protected void paintFigure(Graphics g) {
//	super.paintFigure(g);
	TextFragmentBox frag;
	for (int i = 0; i < fragments.size(); i++) {
		frag = (TextFragmentBox)fragments.get(i);
//		if (!g.getClip(Rectangle.SINGLETON).intersects(frag))
//			continue;
		g.drawString(
			text.substring(frag.offset, frag.offset + frag.length),
			frag.x, frag.y);
//		g.drawRectangle(frag);
	}
//	g.drawRectangle(getBounds().getResized(-1, -1));
}

/**
 * Sets the string being displayed. Causes a <code>revalidate()</code> to occur.
 * @param s the new String */
public void setText(String s) {
	text = s;
	revalidate();
	repaint();
}

}
