package zoom.test;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr
 * @since 2.1
 */
public class AndGate extends Figure {

private static final Dimension size = new Dimension(15, 17);

/**
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	return size;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics g) {
	Rectangle r = getBounds().getCopy();
	g.setBackgroundColor(ColorConstants.yellow);
	g.fillRectangle(r);
	r.translate(2,2);
	r.setSize(11, 9);

	//Draw terminals, 2 at top
	g.drawLine(r.x + 2, r.y, r.x + 2, r.y -2);
	g.drawLine(r.right() - 3, r.y, r.right() - 3, r.y -2);

	//draw main area
	g.setBackgroundColor(new Color (null, 230, 25, 25));
	g.fillRectangle(r);
	
	//outline main area
	g.drawLine(r.x, r.y, r.right()-1, r.y);
	g.drawLine(r.right() - 1, r.y, r.right()-1, r.bottom() - 1);
	g.drawLine(r.x, r.y, r.x, r.bottom() - 1);

	//draw and outline the arc
	r.height = 9;
	r.y += 4;
	g.fillArc(r, 180, 180);
	r.width--;
	r.height--;
	g.drawArc(r, 180, 180);
	g.drawLine(r.x + r.width/2, r.bottom(), r.x + r.width/2, r.bottom() + 2);
}

}
