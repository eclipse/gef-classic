package org.eclipse.gef.ui.palette.editparts;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Border that draws a single white lines on top and left, and a dark line on bottom.
 * 
 * @author Pratik Shah
 */
public class RaisedBorder extends MarginBorder {

private static final Insets DEFAULT_INSETS = new Insets(1, 1, 1, 1);

/**
 * @see org.eclipse.draw2d.Border#getInsets(IFigure)
 */
public Insets getInsets(IFigure figure) {
	return insets;
}

public RaisedBorder() {
	this(DEFAULT_INSETS);
}

public RaisedBorder(Insets insets) {
	super(insets);
}

public RaisedBorder(int t, int l, int b, int r) {
	super(t, l, b, r);
}

public boolean isOpaque() {
	return true;
}

/**
 * @see org.eclipse.draw2d.Border#paint(IFigure, Graphics, Insets)
 */
public void paint(IFigure figure, Graphics g, Insets insets) {
	g.setLineStyle(Graphics.LINE_SOLID);
	g.setLineWidth(1);
	g.setForegroundColor(ColorConstants.buttonLightest);
	Rectangle r = getPaintRectangle(figure, insets);
	r.resize(-1, -1);
	g.drawLine(r.x, r.y, r.right(), r.y);
	g.drawLine(r.x, r.y, r.x, r.bottom());
	g.setForegroundColor(ColorConstants.buttonDarker);
	g.drawLine(r.x, r.bottom(), r.right(), r.bottom());
	g.drawLine(r.right(), r.y, r.right(), r.bottom());
}

}
