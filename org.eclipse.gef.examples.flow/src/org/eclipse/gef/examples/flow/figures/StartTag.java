package org.eclipse.gef.examples.flow.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr
 * Created on Jul 21, 2003
 */
public class StartTag extends Label {

{
	setIconTextGap(8);
}

protected void paintFigure(Graphics g) {
	super.paintFigure(g);
	Rectangle r = getTextBounds();

	r.resize(-1, -1);
	g.drawLine(r.x, r.y, r.right(), r.y);
	g.drawLine(r.x, r.bottom(), r.right(), r.bottom());
	g.drawLine(r.right(), r.bottom(), r.right(), r.y);

	r.x -= 5;
	g.drawLine(r.x, r.y + r.height / 2, r.x + 3, r.y);
	g.drawLine(r.x, r.y + r.height / 2, r.x + 3, r.bottom());
}

}
