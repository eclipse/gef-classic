/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.rulers;


import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;

import org.eclipse.gef.examples.logicdesigner.model.Ruler;

/**
 * @author Pratik Shah
 */
public class RulerFigure
	extends Figure
{
	
/**
 * These fields allow the client to customize the look of the ruler.
 */
public int smallMarkWidth = 2;
public int mediumMarkWidth = 4;
public int textMargin = 3;
public int minPixelsBetweenMarks = 4;
public int minPixelsBetweenMajorMarks = 35;

protected Transposer transposer = new Transposer();
protected ZoomManager zoomManager;

/*
 * This is an artificial border.  When asked for the preferred size, the figure adds 
 * this width to its preferred width.  However, when painting, it doesn't touch
 * these pixels (which will be to the right in a vertical ruler).  Hence, those pixels
 * will have the parent's background color filled.  That is the intended "border."
 */
private static final int BORDER_WIDTH = 2;

private boolean horizontal;
private int unit, interval, divisions;
private double dpu = -1.0;

private ZoomListener zoomListener = new ZoomListener() {
	public void zoomChanged(double newZoomValue) {
		handleZoomChanged();
	}
};

public RulerFigure(boolean isHorizontal, int measurementUnit) {
	setHorizontal(isHorizontal);
	setUnit(measurementUnit);
	setBackgroundColor(ColorConstants.white);
	setOpaque(true);
	setLayoutManager(new RulerLayout());
}

protected double getDPU() {
	if (dpu <= 0) {
		if (getUnit() == Ruler.UNIT_PIXELS) {
			dpu = 1.0;
		} else {
			dpu = transposer.t(new Dimension(Display.getCurrent().getDPI())).height;
			if (getUnit() == Ruler.UNIT_CENTIMETERS) {
				dpu = dpu / 2.54;
			}
		}
		if (zoomManager != null) {
			dpu = dpu * zoomManager.getZoom() / zoomManager.getUIMultiplier();
		}
	}
	return dpu;
}

public Dimension getPreferredSize(int wHint, int hHint) {
	Dimension prefSize = new Dimension();
	if (isHorizontal()) {
		// the plus 2 is to draw the two lines of border at the right
		prefSize.height = (textMargin * 2) + BORDER_WIDTH
				+ FigureUtilities.getFontMetrics(getFont()).getAscent();
	} else {
		prefSize.width = (textMargin * 2) + BORDER_WIDTH
				+ FigureUtilities.getFontMetrics(getFont()).getAscent();
	}
	return prefSize;
}

public int getUnit() {
	return unit;
}

protected void handleZoomChanged() {
	dpu = -1.0;
	repaint();
	layout();
}

/* (non-Javadoc)
 * @see org.eclipse.draw2d.Figure#invalidate()
 */
public void invalidate() {
	super.invalidate();
	dpu = -1.0;
}

public boolean isHorizontal() {
	return horizontal;
}

/*
 * @TODO:Pratik    re-comment this algorithm and the setInterval method
 */

/*
 * (non-Javadoc)
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics graphics) {
	double dotsPerUnit = getDPU();
	Rectangle clip = transposer.t(graphics.getClip(Rectangle.SINGLETON));
	Rectangle figClientArea = transposer.t(getClientArea());
	// Use the x and width of the client area, but the y and height of the clip as the 
	// bounds of the area which is to be repainted.  This will increase performance as the
	// entire ruler will not be repainted everytime.
	Rectangle clippedBounds = clip;
	clippedBounds.x = figClientArea.x;
	clippedBounds.width = figClientArea.width - BORDER_WIDTH;
	
	// Paint the background
	if (isOpaque()) {
		graphics.fillRectangle(transposer.t(clippedBounds));
	}
	
	/* 
	 * A major mark is one that goes all the way from the left edge to the right edge of
	 * a ruler and for which a number is displayed.  Determine the minimum number of
	 * pixels that are to be left between major marks.  This will, in turn, help
	 * determine how many units are to be displayed per major mark.  A major mark should
	 * have at least enough pixels to display the text and its padding.  We take into the
	 * consideration the max of text's width and height so that for horizontal and
	 * vertical rulers that are of the same height, the number of units per major mark is
	 * the same.
	 */
	int unitsPerMajorMark = (int)(minPixelsBetweenMajorMarks / dotsPerUnit);
	if (minPixelsBetweenMajorMarks % dotsPerUnit != 0.0) {
		unitsPerMajorMark++;
	}
	if (interval > 0) {
		/*
		 * If the client specified how many units are to be displayed per major mark, use
		 * that.  If, however, showing that many units wouldn't leave enough room for the
		 * text, than take its smallest multiple that would leave enough room.
		 */
		int intervalMultiple = interval;
		while (intervalMultiple < unitsPerMajorMark) {
			intervalMultiple += interval;
		}
		unitsPerMajorMark = intervalMultiple;
	} else if (unitsPerMajorMark != 1 && unitsPerMajorMark % 2 != 0) {
		// if the number of units per major mark is calculated dynamically, ensure that
		// it is an even number.
		unitsPerMajorMark++;
	}

	/*
	 * divsPerMajorMark indicates the number of divisions that a major mark should be
	 * divided into.  for eg., a value of 2 would mean that a major mark would be shown
	 * as having two parts.  that means that there would be a marker showing the beginning
	 * and end of the major marker and another right in the middle.
	 */
	int divsPerMajorMark;
	if (divisions > 0 
				&& dotsPerUnit * unitsPerMajorMark / divisions >= minPixelsBetweenMarks) {
		/*
		 * If the client has specified the number of divisions per major mark, use that
		 * unless it would cause the minimum space between marks to be less than 
		 * minPixelsBetweenMarks
		 */
		divsPerMajorMark = divisions;
	} else {
		/*
		 * If the client hasn't specified the number of divisions per major mark or the
		 * one that the client has specified is invalid, then calculate it dynamically.
		 * This algorithm will try to display 10 divisions per CM, and 16 per INCH. 
		 * However, if that puts the marks too close together (i.e., the space between
		 * them is less than minPixelsBetweenMarks), then it keeps decreasing the number
		 * of divisions by a factor of 2 until there is enough space between them.
		 */
		divsPerMajorMark = 2;
		if (getUnit() == Ruler.UNIT_CENTIMETERS) {
			divsPerMajorMark = 10;
		} else if (getUnit() == Ruler.UNIT_INCHES) {
			divsPerMajorMark = 8;
		}
		while (dotsPerUnit * unitsPerMajorMark / divsPerMajorMark < minPixelsBetweenMarks) {
			divsPerMajorMark /= 2;
			if (divsPerMajorMark == 0) {
				break;
			}
		}
		// This should never happen unless the client has specified a 
		// minPixelsBetweenMarks that is larger than minPixelsBetweenMajorMarks (which
		// is calculated using the text's size -- size of the largest number to be 
		// displayed).
		if (divsPerMajorMark == 0) {
			divsPerMajorMark = 1;
		}
	}
	
	/*
	 * mediumMarkerDivNum is used to determine which mark (line drawn to indicate a
	 * point on the ruler) in a major mark will be of medium size.  If its value is 1 then
	 * every mark will be of medium size.  If its value is 5, then every 5th mark will
	 * be of medium size (the rest being of small size).
	 */
	int mediumMarkerDivNum = 1;
	switch (divsPerMajorMark) {
		case 20:
		case 10:
		case 5:
			mediumMarkerDivNum = 5;
			break;
		case 16:
		case 8:
			mediumMarkerDivNum = 4;
			break;
		case 4:
			mediumMarkerDivNum = 2;
			break;
		case 2:
			mediumMarkerDivNum = 1;
	}
	
	/*
	 * dotsPerDivision = number of pixels between each mark = number of pixels in a
	 * division
	 */
	double dotsPerDivision = dotsPerUnit * unitsPerMajorMark / divsPerMajorMark;
	/*
	 * startMark is the division/mark from which we are going to start painting.  It
	 * should be the last major mark (one for which a number is displayed) that is before
	 * the top of the clip rectangle.
	 */
	int startMark = (int)(clippedBounds.y / (dotsPerUnit * unitsPerMajorMark)) 
			* divsPerMajorMark;
	if (clippedBounds.y < 0) {
		// -2 / 10 = 0, not -1.  so, if the top of the clip is negative, we need to move
		// the startMark back by a whole major mark.
		startMark -= divsPerMajorMark;
	}
	// endMark is the last visible mark (doesn't have to be a major mark) that is to be painted
	int endMark = (int)(((clippedBounds.y + clippedBounds.height) / dotsPerDivision));
	int leading = FigureUtilities.getFontMetrics(getFont()).getLeading();
	Rectangle forbiddenZone = new Rectangle();
	for (int div = startMark; div <= endMark; div++) {
		// y is the vertical position of the mark 
		int y = (int)(div * dotsPerDivision);
		if (div % divsPerMajorMark == 0) {
			String num = "" + (div / divsPerMajorMark) * unitsPerMajorMark; //$NON-NLS-1$
			if (isHorizontal()) {
				Dimension numSize = FigureUtilities.getStringExtents(num, getFont());
				numSize.width--;
				Point textLocation = new Point(y - (numSize.width / 2), 
						clippedBounds.x + textMargin - leading); 
				forbiddenZone.setLocation(textLocation);
				forbiddenZone.setSize(numSize);
				forbiddenZone.expand(1, 1);
				graphics.fillRectangle(forbiddenZone);
				graphics.drawText(num, textLocation);
			} else {
				Image numImage = ImageUtilities.createRotatedImageOfString(num, getFont(), 
						getForegroundColor(), getBackgroundColor());
				Point textLocation = new Point(clippedBounds.x + textMargin,
						y - (numImage.getBounds().height / 2));
				forbiddenZone.setLocation(textLocation);
				forbiddenZone.setSize(numImage.getBounds().width, 
						numImage.getBounds().height);
				forbiddenZone.expand(1, 1);
				graphics.fillRectangle(forbiddenZone);
				graphics.drawImage(numImage, textLocation);
				numImage.dispose();
			}
		} else if ((div % divsPerMajorMark) % mediumMarkerDivNum == 0) {
			// this is a medium mark, so its length should be longer than the small marks  
			Point start = transposer.t(
					new Point((clippedBounds.getRight().x - mediumMarkWidth) / 2, y));
			Point end = transposer.t(new Point(((clippedBounds.getRight().x 
					- mediumMarkWidth) / 2) + mediumMarkWidth, y));
			if (!forbiddenZone.contains(start)) {
				graphics.drawLine(start, end);				
			}
		} else {
			// small mark
			Point start = transposer.t(
					new Point((clippedBounds.getRight().x - smallMarkWidth) / 2, y));
			Point end = transposer.t(new Point(((clippedBounds.getRight().x 
					- smallMarkWidth) / 2) + smallMarkWidth, y));
			if (!forbiddenZone.contains(start)) {
				graphics.drawLine(start, end);				
			}
		}
	}
}

public void setHorizontal(boolean isHorizontal) {
	horizontal = isHorizontal;
	transposer.setEnabled(isHorizontal);
}

/**
 * Allows the client to set the number of units to be displayed per major mark, and the
 * number of divisions to be shown per major mark.
 * 
 * A major mark is a mark on the ruler that goes all the way across and whose
 * corresponding number is displayed on the ruler.
 * 
 * @param	unitsPerMajorMark	if less than 1, it will be ignored; if there is not enough
 * space to display that many units per major mark, its smallest multiple that leaves
 * enough room will be used.
 * @param	divisionsPerMajorMark		if less than 1, it will be ignored; if displaying 
 * that many divisions does not leave enough room between marks, it will be ignored.
 * 
 */
public void setInterval(int unitsPerMajorMark, int divisionsPerMajorMark) {
	interval = unitsPerMajorMark;
	divisions = divisionsPerMajorMark;
	repaint();
}

public void setUnit(int newUnit) {
	if (unit != newUnit) {
		unit = newUnit;
		dpu = -1.0;
		repaint();
	}
}

public void setZoomManager(ZoomManager manager) {
	if (zoomManager != manager) {
		if (zoomManager != null) {
			zoomManager.removeZoomListener(zoomListener);
		}
		zoomManager = manager;
		if (zoomManager != null) {
			zoomManager.addZoomListener(zoomListener);
		}
	}
}

}