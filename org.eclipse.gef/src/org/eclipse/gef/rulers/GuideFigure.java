/*
 * Created on Oct 24, 2003
 */
package org.eclipse.gef.rulers;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * @author Pratik Shah
 */
public class GuideFigure 
	extends Figure 
{
	
public GuideFigure() {
	setOpaque(true);
	setBackgroundColor(ColorConstants.blue);
}
	
/* (non-Javadoc)
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	return new Dimension(5, 5);
}
	
/* (non-Javadoc)
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics graphics) {
	super.paintFigure(graphics);
}

}
