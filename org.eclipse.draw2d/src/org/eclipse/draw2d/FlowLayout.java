/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transposer;

/**
 * Lays out children in rows or columns, wrapping when the current row/column is filled.
 * The aligment and spacing of rows in the parent can be configured.  The aligment and
 * spacing of children within a row can be configured.
 */
public class FlowLayout
	extends AbstractHintLayout
{

/** Constant to specify components to be aligned in the center */
public static final int ALIGN_CENTER = 0;
/** Constant to specify components to be aligned on the left/top */
public static final int ALIGN_LEFTTOP = 1;
/** Constant to specify components to be aligned on the right/bottom */
public static final int ALIGN_RIGHTBOTTOM = 2;

/** Constant to specify components should be layed out horizontally */
public static final boolean HORIZONTAL = true;
/** Constant to specify components should be layed out vertically */
public static final boolean VERTICAL = false;

/** The horizontal property. */
protected boolean horizontal = true;
/** 
 * The property that determines whether leftover space at the end of a row/column should
 * be filled by the last item in that row/column. 
 */
protected boolean fill = false;

/** The transposer used in converting horizontal layout to vertical. */
protected Transposer transposer; {
	transposer = new Transposer();
	transposer.setEnabled(!horizontal);
}

/** The alignment along the major axis. */
protected int majorAlignment = ALIGN_LEFTTOP;
/** The alignment along the minor axis. */
protected int minorAlignment = ALIGN_LEFTTOP;
/** The spacing along the minor axis. */
protected int minorSpacing = 5;
/** The spacing along the major axis. */
protected int majorSpacing = 5;
protected WorkingData data = null;

/**
 * Holds the necessary information for layout calculations.
 */
protected class WorkingData {
	public int rowHeight, rowWidth, rowCount, rowX, rowY, maxWidth;
	public Rectangle bounds[], area;
	public IFigure row[];
}

/**
 * Constructs a FlowLayout with horizontal orientation.
 * @since 2.0
 */
public FlowLayout() { }

/**
 * Constructs a FlowLayout whose orientation is given in the input.
 * @param isHorizontal <code>true</code> if the layout should be horizontal
 * @since 2.0
 */
public FlowLayout(boolean isHorizontal) {
	setHorizontal(isHorizontal);
}

/**
 * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(IFigure, int, int)
 */
protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
	// Subtract out the insets from the hints
	if (wHint > -1)
		wHint = Math.max(0, wHint - container.getInsets().getWidth());
	if (hHint > -1)
		hHint = Math.max(0, hHint - container.getInsets().getHeight());
	
	// Figure out the new hint that we are interested in based on the orientation
	// Ignore the other hint (by setting it to -1).  NOTE: The children of the
	// parent figure will then be asked to ignore that hint as well.  
	int maxWidth;
	if (isHorizontal()) {
		maxWidth = wHint;
		hHint = -1;
	} else {
		maxWidth = hHint;
		wHint = -1;
	}
	if (maxWidth < 0) {
		maxWidth = Integer.MAX_VALUE;
	}

	// The preferred dimension that is to be calculated and returned
	Dimension prefSize = new Dimension();

	List children = container.getChildren();
	int width = 0;
	int height = 0;
	IFigure child;
	Dimension childSize;
	
	//Build the sizes for each row, and update prefSize accordingly
	for (int i = 0; i < children.size(); i++) {
		child = (IFigure)children.get(i);
		childSize = transposer.t(getChildSize(child, wHint, hHint));
		if (i == 0) {
			width = childSize.width;
			height = childSize.height;
		} else if (width + childSize.width + getMinorSpacing() > maxWidth) {
			// The current row is full, start a new row.
			prefSize.height += height + getMajorSpacing();
			prefSize.width = Math.max(prefSize.width, width);
			width = childSize.width;
			height = childSize.height;
		} else {
			// The current row can fit another child.
			width += childSize.width + getMinorSpacing();
			height = Math.max(height, childSize.height);
		}
	}

	// Flush out the last row's data
	prefSize.height += height;
	prefSize.width = Math.max(prefSize.width, width);

	// Transpose the dimension back, and compensate for the border.
	prefSize = transposer.t(prefSize);
	prefSize.width += container.getInsets().getWidth();
	prefSize.height += container.getInsets().getHeight();
	prefSize.union(getBorderPreferredSize(container));

	return prefSize;
}

/**
 * Provides the given child's preferred size.
 * 
 * @param child the Figure whose preferred size needs to be calculated
 * @param wHint the width hint
 * @param hHint the height hint
 * @return the child's preferred size
 */
protected Dimension getChildSize(IFigure child, int wHint, int hHint) {
	return child.getPreferredSize(wHint, hHint);
}


/**
 * Returns the alignment used for an entire row/column.
 * <P>
 * Possible values are :
 * <ul>
 *   <li>{@link #ALIGN_CENTER}
 * 	 <li>{@link #ALIGN_LEFTTOP}
 * 	 <li>{@link #ALIGN_RIGHTBOTTOM}
 * </ul>
 *
 * @return the major alignment
 * @since 2.0
 */
public int getMajorAlignment() {
	return majorAlignment;
}

/**
 * Returns the spacing in pixels to be used between children in the direction parallel to
 * the layout's orientation.
 * @return the major spacing
 */
public int getMajorSpacing() {
	return majorSpacing;
}

/** 
 * Returns the alignment used for children within a row/column.
 * <P>
 * Possible values are :
 * <ul>
 *   <li>{@link #ALIGN_CENTER}
 * 	 <li>{@link #ALIGN_LEFTTOP}
 * 	 <li>{@link #ALIGN_RIGHTBOTTOM}
 * </ul>
 *
 * @return the minor alignment
 * @since 2.0
 */
public int getMinorAlignment() {
	return minorAlignment;
}

/**
 * Returns the spacing to be used between children within a row/column.
 * @return the minor spacing
 */
public int getMinorSpacing() {
	return minorSpacing;
}

/**
 * Initializes the state of row data, which is internal to the layout process. 
 */
protected void initRow() {
	data.rowX = 0;
	data.rowHeight = 0;
	data.rowWidth = 0;
	data.rowCount = 0;
}

/**
 * Initializes state data for laying out children, based on the Figure given as input.
 *
 * @param parent the parent figure
 * @since 2.0 
 */
protected void initVariables(IFigure parent) {
	data.row = new IFigure[parent.getChildren().size()];
	data.bounds = new Rectangle[data.row.length];
	data.maxWidth = data.area.width;
}

/**
 * Returns <code>true</code> if the orientation of the layout is horizontal.
 *
 * @return <code>true</code> if the orientation of the layout is horizontal
 * @since 2.0
 */
public boolean isHorizontal() {
	return horizontal;
}

/**
 * @see org.eclipse.draw2d.AbstractHintLayout#isSensitiveHorizontally(IFigure)
 */
protected boolean isSensitiveHorizontally(IFigure parent) {
	return isHorizontal();
}

/**
 * @see org.eclipse.draw2d.AbstractHintLayout#isSensitiveVertically(IFigure)
 */
protected boolean isSensitiveVertically(IFigure parent) {
	return !isHorizontal();
}

/**
 * @see org.eclipse.draw2d.LayoutManager#layout(IFigure)
 */
public void layout(IFigure parent) {
	data = new WorkingData();
	Rectangle relativeArea = parent.getClientArea();
	data.area = transposer.t(relativeArea);

	Iterator iterator = parent.getChildren().iterator();
	int dx;

	//Calculate the hints to be passed to children
	int wHint = -1;
	int hHint = -1;
	if (isHorizontal())
		wHint = parent.getClientArea().width;
	else
		hHint = parent.getClientArea().height;

	initVariables(parent);
	initRow();
	int i = 0; 
	while (iterator.hasNext()) {
		IFigure f = (IFigure)iterator.next();
		Dimension pref = transposer.t(getChildSize(f, wHint, hHint));
		Rectangle r = new Rectangle(0, 0, pref.width, pref.height);

		if (data.rowCount > 0) {
			if (data.rowWidth + pref.width > data.maxWidth)
				layoutRow(parent);
		}
		r.x = data.rowX;
		r.y = data.rowY;
		dx = r.width + getMinorSpacing();
		data.rowX += dx;
		data.rowWidth += dx;
		data.rowHeight = Math.max(data.rowHeight, r.height);
		data.row [data.rowCount] = f;
		data.bounds[data.rowCount] = r;
		data.rowCount++;
		i++;
	}
	if (data.rowCount != 0)
		layoutRow(parent);
	data = null;
}

/**
 * Layouts one row of components. This is done based on the layout's orientation, minor 
 * alignment and major alignment.
 *
 * @param parent the parent figure
 * @since 2.0
 */
protected void layoutRow(IFigure parent) {
	int majorAdjustment = 0;
	int minorAdjustment = 0;
	int correctMajorAlignment = majorAlignment;
	int correctMinorAlignment = minorAlignment;

	majorAdjustment = data.area.width - data.rowWidth + getMinorSpacing();
	
	switch (correctMajorAlignment) {
		case ALIGN_LEFTTOP: 
			majorAdjustment = 0;
			break;
		case ALIGN_CENTER:
			majorAdjustment /= 2;
			break;
		case ALIGN_RIGHTBOTTOM:
			break;
	}

	for (int j = 0; j < data.rowCount; j++) {
		if (fill) {
			data.bounds[j].height = data.rowHeight;	
		} else {
			minorAdjustment = data.rowHeight - data.bounds[j].height;
			switch (correctMinorAlignment) {
				case ALIGN_LEFTTOP: 
					minorAdjustment = 0;
					break;
				case ALIGN_CENTER:
					minorAdjustment /= 2;
				break;
				case ALIGN_RIGHTBOTTOM:
					break;
			}
			data.bounds[j].y += minorAdjustment;
		}
		data.bounds[j].x += majorAdjustment;
		
		setBoundsOfChild(parent, data.row[j], transposer.t(data.bounds[j]));
	}
	data.rowY += getMajorSpacing() + data.rowHeight;
	initRow();
}

/**
 * Sets the given bounds for the child figure input.
 *
 * @param parent the parent figure
 * @param child the child figure
 * @param bounds the size of the child to be set
 * @since 2.0
 */
protected void setBoundsOfChild(IFigure parent, IFigure child, Rectangle bounds) {
	parent.getClientArea(Rectangle.SINGLETON);
	bounds.translate(Rectangle.SINGLETON.x, Rectangle.SINGLETON.y);
	child.setBounds(bounds);
}

/**
 * Sets flag based on layout orientation. If in horizontal orientation, all figures will 
 * have the same height. If in vertical orientation, all figures will have the same width.
 *
 * @param value fill state desired
 * @since 2.0
 */
public void setStretchMinorAxis(boolean value) {
	fill = value;
}

/**
 * Sets the orientation of the layout. 
 *
 * @param flag <code>true</code> if this layout should be horizontal
 * @since 2.0
 */
public void setHorizontal(boolean flag) {
	if (horizontal == flag) return;
	invalidate();
	horizontal = flag;
	transposer.setEnabled(!horizontal);
}

/**
 * Sets the alignment for an entire row/column within the parent figure.
 * <P>
 * Possible values are :
 * <ul>
 *   <li>{@link #ALIGN_CENTER}
 * 	 <li>{@link #ALIGN_LEFTTOP}
 * 	 <li>{@link #ALIGN_RIGHTBOTTOM}
 * </ul>
 *
 * @param align the major alignment
 * @since 2.0
 */
public void setMajorAlignment(int align) {
	majorAlignment = align;
}

/**
 * Sets the spacing in pixels to be used between children in the direction parallel to the
 * layout's orientation.
 *
 * @param n the major spacing
 * @since 2.0
 */
public void setMajorSpacing(int n) {
	majorSpacing = n;
}

/**
 * Sets the alignment to be used within a row/column.
 * <P>
 * Possible values are :
 * <ul>
 *   <li>{@link #ALIGN_CENTER}
 * 	 <li>{@link #ALIGN_LEFTTOP}
 * 	 <li>{@link #ALIGN_RIGHTBOTTOM}
 * </ul>
 *
 * @param align the minor alignment
 * @since 2.0
 */
public void setMinorAlignment(int align) {
	minorAlignment = align;
}

/**
 * Sets the spacing to be used between children within a row/column.
 *
 * @param n the minor spacing
 * @since 2.0
 */
public void setMinorSpacing(int n) {
	minorSpacing = n;
}

}
