package org.eclipse.gef.examples.flow.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.examples.flow.FlowImages;

/**
 * @author hudsonr
 * Created on Jul 21, 2003
 */
public class StartTag extends Label {

/**
 * Creates a new StartTag
 * @param name the text to display in this StartTag
 */
public StartTag(String name) {
	setIconTextGap(8);
	setText(name);
	setIcon(FlowImages.gear);
}

protected void paintFigure(Graphics g) {
	super.paintFigure(g);
	Rectangle r = getTextBounds();

	r.resize(-1, -1);
	g.drawLine(r.x, r.y, r.right(), r.y); //Top line
	g.drawLine(r.x, r.bottom(), r.right(), r.bottom()); //Bottom line
	g.drawLine(r.right(), r.bottom(), r.right(), r.y); //Right line

	r.x -= 4;
	g.drawLine(r.x, r.y + r.height / 2, r.x + 3, r.y);
	g.drawLine(r.x, r.y + r.height / 2, r.x + 3, r.bottom());
}

}
