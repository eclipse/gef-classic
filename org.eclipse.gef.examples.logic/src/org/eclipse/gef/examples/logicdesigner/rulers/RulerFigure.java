/*
 * Created on Oct 3, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;

/**
 * @author Pratik Shah
 */
public class RulerFigure
	extends Figure
{

/**
 * These fields allow the client to customize the look of the ruler.
 */
public int smallMarkWidth = 5;
public int mediumMarkWidth = 10;
public int textLeftMargin = 2;
public int textRightMargin = 2;
public int textTopMargin = 2;
public int textBottomMargin = 4;
public int minPixelsBetweenMarks = 3;

private boolean horizontal;
private int unit, interval, divisions, cachedHeight, cachedY, cachedLength = 0;
private double dpu = -1.0;
private Dimension textSize, cachedPrefSize, cachedTextSize;

protected Transposer transposer = new Transposer();
protected FigureCanvas editor;
protected ZoomManager zoom;

private ZoomListener zoomListener = new ZoomListener() {
	public void zoomChanged(double zoom) {
		layout();
	}
};

public RulerFigure(FigureCanvas editor, boolean isHorizontal, int unit) {
	this.editor = editor;
	setHorizontal(isHorizontal);
	setUnit(unit);
	setBackgroundColor(ColorConstants.white);
	setBorder(new MarginBorder(transposer.t(new Insets(0, 3, 0, 4))));
	setOpaque(true);
	setLayoutManager(new RulerLayout());
}

private void calculateTextSize(Rectangle transposedBounds) {
	int numLength = ("" //$NON-NLS-1$
			+ (int)((transposedBounds.height + transposedBounds.y) / getDPU())).length();
	if (transposedBounds.y < 0) {
		numLength = Math.max(numLength, 
				("" + (int)(transposedBounds.y / getDPU())).length()); //$NON-NLS-1$
	}
	numLength = Math.max(2, numLength);
	textSize = getSizeOfText(numLength);
}

/*
 * @TODO:Pratik
 * 
 * this is really a hack.  is this acceptable?  the overview ruler should not really
 * know about the rulercomposite.  maybe it can be passed in as a parameter to the 
 * constructor of this class.  or maybe you can get rid of this method completely.  it 
 * then becomes the client's responsibility to force a layout when necessary (say, when 
 * the units change).
 */
protected void forceSWTLayoutIfNecessary() {
	Composite parent = editor.getParent();
	if (parent instanceof RulerComposite) {
		((RulerComposite)parent).layoutIfNecessary();
	} else {
		parent.layout(true);
	}
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
		if (zoom != null) {
			dpu = dpu * zoom.getZoom() / zoom.getUIMultiplier();
		}
	}
	return dpu;
}

// @TODO:Pratik    you should change this so that the preferred size is being calculated
// based on the range model, and not the size of the editor.  this way, the editor
// won't have to be passed around.
public Dimension getPreferredSize(int wHint, int hHint) {
	Rectangle viewBounds = transposer.t(editor.getContents().getBounds().getCopy());
	if (cachedHeight != viewBounds.height || cachedY != viewBounds.y 
			|| cachedPrefSize == null) {
		cachedHeight = viewBounds.height;
		cachedY = viewBounds.y;
		calculateTextSize(viewBounds);
		cachedPrefSize = transposer.t(new Dimension(transposer.t(textSize).width 
				+ textLeftMargin + textRightMargin + mediumMarkWidth, 
				viewBounds.height));
		if (getBorder() != null) {
			cachedPrefSize.expand(getBorder().getInsets(this).getWidth(), 
					getBorder().getInsets(this).getHeight());
		}
	}
	return cachedPrefSize.getCopy();
}

// length must be greater than or equal to 1
private Dimension getSizeOfText(int length) {
	if (length != cachedLength) {
		String number = "8"; //$NON-NLS-1$
		for (int i = 1; i < length; i++) {
			number += "8"; //$NON-NLS-1$
		}
		cachedTextSize = FigureUtilities.getStringExtents(number, getFont());
		cachedLength = length;
	}
	return cachedTextSize;
}

public int getUnit() {
	return unit;
}

/* (non-Javadoc)
 * @see org.eclipse.draw2d.Figure#invalidate()
 */
public void invalidate() {
	if (isValid()) {
		super.invalidate();
		// we have to invalidate the cachedLength and the cachedTextSize because the
		// font could have changed
		cachedLength = 0;
		cachedTextSize = null;
	}
	cachedPrefSize = null;
	dpu = -1.0;
}

public boolean isHorizontal() {
	return horizontal;
}

/*
 * (non-Javadoc)
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics graphics) {
	if (isOpaque()) {
		graphics.fillRectangle(getClientArea());
	}
	double dotsPerUnit = getDPU();
	Rectangle clip = transposer.t(graphics.getClip(Rectangle.SINGLETON));
	Rectangle figClientArea = transposer.t(getClientArea()).translate(-1, -1);
	// Use the x and width of the client area, but the y and height of the clip as the 
	// bounds of the area which is to be repainted.  This will increase performance as the
	// entire ruler will not be repainted everytime.
	Rectangle clippedBounds = clip;
	clippedBounds.x = figClientArea.x;
	clippedBounds.width = figClientArea.width;

	/* 
	 * A major mark is one that goes all the way from the left edge to the right edge of
	 * a ruler and for which a number is displayed.  Determine the minimum number of
	 * pixels that are to be left between major marks.  This will, in turn, help
	 * determine how many units are to be displayed per major mark.  A major should have
	 * at least enough pixels to display the text and its padding.  We take into the
	 * consideration the max of text's width and height so that for horizontal and
	 * vertical rulers that are of the same height, the number of units per major mark is
	 * the same.
	 */
	int minPixelsBetweenMajorMarks = Math.max(textSize.width, textSize.height) 
			+ textTopMargin + textBottomMargin;
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
			divsPerMajorMark = 16;
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
			mediumMarkerDivNum = 5;
			break;
		case 16:
		case 8:
			mediumMarkerDivNum = 2;
			break;
		case 4:
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
	for (int div = startMark; div <= endMark; div++) {
		// y is the vertical position of the mark 
		int y = (int)(div * dotsPerDivision);
		Point start = new Point(clippedBounds.getRight().x - smallMarkWidth, y);
		Point end = new Point(clippedBounds.getRight().x, y);
		if (div % divsPerMajorMark == 0) {
			// this is a major mark.  so it should go all the way across, and the number
			// should be displayed 
			start.x = clippedBounds.x;
			graphics.drawText("" + (div / divsPerMajorMark) * unitsPerMajorMark,  //$NON-NLS-1$
					transposer.t(start.getTranslated(textLeftMargin, textTopMargin)));
		} else if ((div % divsPerMajorMark) % mediumMarkerDivNum == 0) {
			// this is a medium mark, so its length should be longer than the small marks  
			start.x = clippedBounds.getRight().x - mediumMarkWidth;
		}
		graphics.drawLine(transposer.t(start), transposer.t(end));
	}
	// draw the line on the right of the ruler 
//	graphics.drawLine(transposer.t(clippedBounds.getTopRight()), 
//			transposer.t(clippedBounds.getBottomRight()));
}

/* (non-Javadoc)
 * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
 */
public void setBounds(Rectangle rect) {
	// TODO Auto-generated method stub
	super.setBounds(rect);
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

public void setUnit(int unit) {
	if (this.unit != unit) {
		this.unit = unit;
		invalidate();
		forceSWTLayoutIfNecessary();
	}
}

public void setZoomManager(ZoomManager manager) {
	if (zoom != manager) {
		if (zoom != null) {
			zoom.removeZoomListener(zoomListener);
		}
		zoom = manager;
		if (zoom != null) {
			zoom.addZoomListener(zoomListener);
		}
	}
	if (getLayoutManager() instanceof RulerLayout) {
		((RulerLayout)getLayoutManager()).setZoomManager(zoom);		
	}
}

}