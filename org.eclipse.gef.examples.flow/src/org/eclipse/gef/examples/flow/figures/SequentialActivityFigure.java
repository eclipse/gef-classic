package org.eclipse.gef.examples.flow.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr
 */
public class SequentialActivityFigure extends SubgraphFigure {

static final MarginBorder MARGIN_BORDER = new MarginBorder(0, 8, 0, 0);

static final PointList ARROW = new PointList(3); {
	ARROW.addPoint(0,0);
	ARROW.addPoint(10,0);
	ARROW.addPoint(5,5);
}

/**
 * @param header
 * @param footer
 */
public SequentialActivityFigure() {
	super(new StartTag(""), new EndTag(""));
	setBorder(MARGIN_BORDER);
	setOpaque(true);
}

protected void paintFigure(Graphics graphics) {
	super.paintFigure(graphics);
	graphics.setBackgroundColor(ColorConstants.button);
	Rectangle r = getBounds();
	graphics.fillRectangle(r.x + 13, r.y + 10, 8, r.height - 18);
//	graphics.fillPolygon(ARROW);
//	graphics.drawPolygon(ARROW);
}

}
