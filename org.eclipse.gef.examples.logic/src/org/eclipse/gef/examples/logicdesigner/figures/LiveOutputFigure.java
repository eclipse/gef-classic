package org.eclipse.gef.examples.logicdesigner.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author danlee
 */
public class LiveOutputFigure extends OutputFigure {
private static final Dimension SIZE = new Dimension(15, 15);

/**
 * Constructor for LiveOutputFigure.
 */
public LiveOutputFigure() {
	super();
}

/**
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	return SIZE;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics g) {
	Rectangle r = getBounds().getCopy();
	g.setBackgroundColor(ColorConstants.yellow);
	
	g.fillOval(r);
	r.height--;
	r.width--;
	g.drawOval(r);
	g.translate(r.getLocation());

	// Draw the "V"
	g.drawLine(3, 4, 5, 9);
	g.drawLine(5, 9, 7, 4);
	
	// Draw the "+"
	g.drawLine(9, 7, 9, 11);
	g.drawLine(7, 9, 11, 9);
}

}