package org.eclipse.gef.ui.palette;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Border that draws a single white lines on top and left, and a dark line on bottom.
 * 
 * @author Pratik Shah
 */
public class PaletteContainerBorder 
	extends AbstractBorder 
{

private Insets insets = new Insets(1, 1, 1, 0);

/**
 * @see org.eclipse.draw2d.Border#getInsets(IFigure)
 */
public Insets getInsets(IFigure figure) {
	return insets;
}

/**
 * @see org.eclipse.draw2d.Border#isOpaque()
 */
public boolean isOpaque() {
	return true;
}

/**
 * @see org.eclipse.draw2d.Border#paint(IFigure, Graphics, Insets)
 */
public void paint(IFigure figure, Graphics graphics, Insets insets) {
	graphics.setLineStyle(Graphics.LINE_SOLID);
	graphics.setLineWidth(1);
	graphics.setForegroundColor(ColorConstants.buttonLightest);
	PointList points = new PointList();
	Rectangle r = getPaintRectangle(figure, insets);
	points.addPoint(r.x, r.y + r.height - 1);
	points.addPoint(r.x, r.y);
	points.addPoint(r.x + r.width - 1, r.y);
	graphics.drawPolyline(points);
	graphics.setForegroundColor(ColorConstants.buttonDarker);
	graphics.drawLine(r.x, r.y + r.height - 1, r.x + r.width - 1, r.y + r.height - 1);
}

}
