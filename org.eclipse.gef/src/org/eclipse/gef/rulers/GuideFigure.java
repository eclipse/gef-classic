/*
 * Created on Oct 24, 2003
 */
package org.eclipse.gef.rulers;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.SharedCursors;

/**
 * @author Pratik Shah
 */
public class GuideFigure 
	extends Figure
{
	
private boolean horizontal;
	
public GuideFigure(boolean isHorizontal) {
	horizontal = isHorizontal;
	setBackgroundColor(ColorConstants.button);
	if (horizontal) {
		setCursor(SharedCursors.SIZEN);		
	} else {
		setCursor(SharedCursors.SIZEW);
	}
}
	
/* (non-Javadoc)
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	Dimension prefSize;
	if (isHorizontal()) {
		prefSize = new Dimension(8, 9);
	} else {
		prefSize = new Dimension(9, 8);
	}
	if (getBorder() != null) {
		prefSize.expand(getBorder().getInsets(this).getWidth(), 
				getBorder().getInsets(this).getHeight());
	}
	return prefSize;
}

protected boolean isHorizontal() {
	return horizontal;
}
	
/* (non-Javadoc)
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics graphics) {
	// Since painting can occur a lot, using a transposer is not good for performance.
	// Hence, this method does not use it.
	if (isHorizontal()) {
		Rectangle clientArea = getClientArea().getCopy();
		clientArea.x = clientArea.getTopRight().x - 8;
		clientArea.width = 8;
		
		graphics.fillRectangle(clientArea.getCropped(new Insets(2, 2, 2, 1)));

		graphics.setForegroundColor(ColorConstants.buttonLightest);
		graphics.drawLine(clientArea.x, clientArea.y + 1, clientArea.x, clientArea.y + 7);
		graphics.drawLine(clientArea.x + 1, clientArea.y, clientArea.x + 4, clientArea.y);
		graphics.drawLine(clientArea.x + 1, clientArea.y + 8, clientArea.x + 4, 
				clientArea.y + 8);
		graphics.drawLine(clientArea.x + 2, clientArea.y + 2, clientArea.x + 2,
				clientArea.y + 5);
		graphics.drawLine(clientArea.x + 3, clientArea.y + 2, clientArea.x + 3,
				clientArea.y + 2);
		graphics.drawLine(clientArea.x + 6, clientArea.y + 2, clientArea.x + 6,
				clientArea.y + 2);
		graphics.drawLine(clientArea.x + 6, clientArea.y + 6, clientArea.x + 6,
				clientArea.y + 6);
		graphics.drawLine(clientArea.x + 7, clientArea.y + 3, clientArea.x + 7,
				clientArea.y + 5);

		graphics.setForegroundColor(ColorConstants.buttonDarker);
		graphics.drawLine(clientArea.x + 1, clientArea.y + 1, clientArea.x + 4, 
				clientArea.y + 1);
		graphics.drawLine(clientArea.x + 1, clientArea.y + 2, clientArea.x + 1,
				clientArea.y + 7);
		graphics.drawLine(clientArea.x + 2, clientArea.y + 7, clientArea.x + 2,
				clientArea.y + 7);
		graphics.drawLine(clientArea.x + 5, clientArea.y + 2, clientArea.x + 5, 
				clientArea.y + 2);
		graphics.drawLine(clientArea.x + 6, clientArea.y + 3, clientArea.x + 6,
				clientArea.y + 3);

		graphics.setForegroundColor(ColorConstants.buttonDarkest);
		graphics.drawLine(clientArea.x + 3, clientArea.y + 7, clientArea.x + 4, 
				clientArea.y + 7);
		graphics.drawLine(clientArea.x + 5, clientArea.y + 6, clientArea.x + 5,
				clientArea.y + 6);
		graphics.drawLine(clientArea.x + 6, clientArea.y + 5, clientArea.x + 6, 
				clientArea.y + 5);
		graphics.drawLine(clientArea.x + 7, clientArea.y + 4, clientArea.x + 7, 
				clientArea.y + 4);
	} else {
		Rectangle clientArea = getClientArea().getCopy();
		clientArea.y = clientArea.getBottomLeft().y - 8;
		clientArea.height = 8;
		
		graphics.fillRectangle(clientArea.getCropped(new Insets(2, 2, 1, 2)));

		graphics.setForegroundColor(ColorConstants.buttonLightest);
		graphics.drawLine(clientArea.x + 1, clientArea.y, clientArea.x + 7, clientArea.y);
		graphics.drawLine(clientArea.x, clientArea.y + 1, clientArea.x, clientArea.y + 4);
		graphics.drawLine(clientArea.x + 8, clientArea.y + 1, clientArea.x + 8, 
				clientArea.y + 4);
		graphics.drawLine(clientArea.x + 2, clientArea.y + 2, clientArea.x + 5,
				clientArea.y + 2);
		graphics.drawLine(clientArea.x + 2, clientArea.y + 3, clientArea.x + 2,
				clientArea.y + 3);
		graphics.drawLine(clientArea.x + 2, clientArea.y + 6, clientArea.x + 2,
				clientArea.y + 6);
		graphics.drawLine(clientArea.x + 6, clientArea.y + 6, clientArea.x + 6,
				clientArea.y + 6);
		graphics.drawLine(clientArea.x + 3, clientArea.y + 7, clientArea.x + 5,
				clientArea.y + 7);

		graphics.setForegroundColor(ColorConstants.buttonDarker);
		graphics.drawLine(clientArea.x + 1, clientArea.y + 1, clientArea.x + 1, 
				clientArea.y + 4);
		graphics.drawLine(clientArea.x + 2, clientArea.y + 1, clientArea.x + 7,
				clientArea.y + 1);
		graphics.drawLine(clientArea.x + 7, clientArea.y + 2, clientArea.x + 7,
				clientArea.y + 2);
		graphics.drawLine(clientArea.x + 2, clientArea.y + 5, clientArea.x + 2, 
				clientArea.y + 5);
		graphics.drawLine(clientArea.x + 3, clientArea.y + 6, clientArea.x + 3,
				clientArea.y + 6);

		graphics.setForegroundColor(ColorConstants.buttonDarkest);
		graphics.drawLine(clientArea.x + 7, clientArea.y + 3, clientArea.x + 7, 
				clientArea.y + 4);
		graphics.drawLine(clientArea.x + 6, clientArea.y + 5, clientArea.x + 6,
				clientArea.y + 5);
		graphics.drawLine(clientArea.x + 5, clientArea.y + 6, clientArea.x + 5, 
				clientArea.y + 6);
		graphics.drawLine(clientArea.x + 4, clientArea.y + 7, clientArea.x + 4, 
				clientArea.y + 7);
	}
}

}