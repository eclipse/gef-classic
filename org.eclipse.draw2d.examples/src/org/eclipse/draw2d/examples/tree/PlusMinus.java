package org.eclipse.draw2d.examples.tree;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Toggle;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 * @author hudsonr
 * Created on Apr 22, 2003
 */
public class PlusMinus extends Toggle {

{setPreferredSize(9, 9);}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics g) {
	super.paintFigure(g);
	Rectangle r = Rectangle.SINGLETON;
	r.setBounds(getBounds()).resize(-1, -1);
	g.drawRectangle(r);
	int xMid = r.x + r.width / 2;
	int yMid = r.y + r.height / 2;
	g.drawLine(r.x + 2, yMid, r.right() - 2, yMid);
	if (!isSelected())
		g.drawLine(xMid, r.y + 2, xMid, r.bottom() - 2);
}

}
