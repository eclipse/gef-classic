package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import org.eclipse.draw2d.geometry.*;

/**
 * Lays our children in rows or columns, wrapping when the current row/column is filled.
 * The aligment and spacing of rows in the parent can be configured.  The aligment and
 * spacing of children within a row can be configured.
 */
public class FlowLayout
	extends AbstractHintLayout
{

/*
 * Constants defining the alignment of the components
 */
public static final int
	ALIGN_CENTER = 0,
	ALIGN_LEFTTOP = 1,
	ALIGN_RIGHTBOTTOM = 2;

public static final boolean
	HORIZONTAL = true,
	VERTICAL = false;

protected boolean horizontal = true;
protected boolean fill = false;

protected Transposer transposer; {
	transposer = new Transposer();
	transposer.setEnabled(!horizontal);
}

/*
 * Internal state
 */
protected int majorAlignment = ALIGN_LEFTTOP;
protected int minorAlignment = ALIGN_LEFTTOP;
private int minorSpacing = 5, majorSpacing = 5;
private WorkingData data = null;

/**
 * Holds the necessary information for layout calculations.
 */
class WorkingData {
	int rowHeight, rowWidth, rowCount, rowX, rowY, maxWidth;
	Rectangle bounds[], area;
//	Insets insets;
	IFigure row[];
	Dimension spacing;
}

/**
 * Constructs a FlowLayout with horizontal orientation.
 * 
 * @see #FlowLayout(boolean)
 * @since 2.0
 */
public FlowLayout(){}

/**
 * Constructs a FlowLayout whose orientation is
 * given in the input.
 * @param isHorizontal  Whether the layout should be horizontal.
 * @since 2.0
 */
public FlowLayout(boolean isHorizontal){
	setHorizontal(isHorizontal);
}

/**
 * Minimum size is not well defined for FlowLayout.
 * For now, it just returns the the size required to display the first child, if it exists.
 * @param	parent	The IFigure whose minimum size has to be calculated.
 * @return	The minimum size for the layout.
 */
public Dimension calculateMinimumSize(IFigure parent){
	Dimension minSize = new Dimension(0, 0);
	List children = parent.getChildren();
	for (int i = 0; i < children.size(); i++) {
		minSize.union(((IFigure)children.get(i)).getPreferredSize());		
	}
	minSize.width += parent.getInsets().getWidth();
	minSize.height += parent.getInsets().getHeight();
	return minSize;
}

/**
 * Calculates the preferred size of the figure constrainted to the 
 * given hints.  Based on the orientation of this flow layout, one of the
 * given hints will be ignored (wHint in vertical flow layout; hHint in 
 * horizontal flow layout).  If the interesting hint is less than or equal to
 * zero, the hints are ignored, and calculatePreferredSize(IFigure) is called.
 * 
 * @param   parent  The IFigure whose preferred size has to be calculated.  
 * @param   arg_wHint   The width-hint
 * @param   arg_hHint   The height-hint
 * @return  The preferred size given the constraints
 * @see     #getPreferredSize(IFigure, int, int)
 * @since   2.0
 */
protected Dimension calculatePreferredSize(
	IFigure parent,
	int arg_wHint,
	int arg_hHint)
{
	// Remove insets
	int wHint = -1;
	if (arg_wHint > -1)
		wHint = Math.max(0, arg_wHint - parent.getInsets().getWidth());
	int hHint = -1;
	if (arg_hHint > -1)
		hHint = Math.max(0, arg_hHint - parent.getInsets().getHeight());
	
	// Figure out the new hint that we are interested in based on the orientation
	// Ignore the other hint (by setting it to -1).  NOTE: The children of the
	// parent figure will then be asked to ignore that hint as well.  
	int max_row_width;
	if (isHorizontal()){
		max_row_width = wHint;
		hHint = -1;
	} else {
		max_row_width = hHint;
		wHint = -1;
	}
	if (max_row_width == -1)
		max_row_width = Integer.MAX_VALUE;
	
	// If the hint that we are intersted in (wHint in case of horizontal layout;
	// hHint in case of vertical) is zero or less, ignore all hints
	if (max_row_width <= 0){
		return calculatePreferredSize(parent);
	}

	// The preferred dimension that is to be calculated and returned
	Dimension prefSize = new Dimension(0,0);

	List children = parent.getChildren();
	int row_width = 0;
	int row_height = 0;
	IFigure child;
	Dimension childSize;
	
	//Build the sizes for each row, and update prefSize accordingly
	for(int i = 0; i < children.size(); i++){
		child = (IFigure)children.get(i);
		childSize = transposer.t(child.getPreferredSize(wHint,hHint));
		if (i == 0){
			row_width = childSize.width;
			row_height = childSize.height;
		} else if (row_width + childSize.width + minorSpacing > max_row_width) {
			// The current row is full, start a new row.
			prefSize.height += row_height + majorSpacing;
			prefSize.width = Math.max(prefSize.width, row_width);
			row_width = childSize.width;
			row_height = childSize.height;
		} else {
			// The current row can fit another child.
			row_width += childSize.width + minorSpacing;
			row_height = Math.max(row_height, childSize.height);
		}
	}

	// Flush out the last row's data
	prefSize.height += row_height;
	prefSize.width = Math.max(prefSize.width, row_width);

	// Transpose the dimension back, and compensate for the border.
	prefSize = transposer.t(prefSize);
	prefSize.width += parent.getInsets().getWidth();
	prefSize.height += parent.getInsets().getHeight();
	prefSize.union(getBorderPreferredSize(parent));

	return prefSize;
}

/**
 * Returns the alignment used for an entire row/column.
 * Possible values are :
 * <ul>
 *		<li>ALIGN_CENTER = 0;
 * 	<li>ALIGN_LEFTTOP = 1;
 * 	<li>ALIGN_RIGHTBOTTOM = 2;
 * </ul>
 *
 * @return  Major alignment of the children.
 * @see  #setMajorAlignment(int)
 * @since 2.0
 */
public int getMajorAlignment() {return majorAlignment;}

/** 
 * Returns the alignment used for children within a row/column.
 * Possible values are :
 * <ul>
 *		<li>ALIGN_CENTER = 0;
 * 	<li>ALIGN_LEFTTOP = 1;
 * 	<li>ALIGN_RIGHTBOTTOM = 2;
 * </ul>
 *
 * @return  Minor alignment of the children.
 * @see  #setMinorAlignment(int)
 * @since 2.0
 */
public int getMinorAlignment() {return minorAlignment;}

/**
 * Initializes the state of row data, which is internal
 * to the layout process. 
 */
private void initRow(){
	data.rowX = 0;
	data.rowHeight = 0;
	data.rowWidth = 0;
	data.rowCount = 0;
}

/**
 * Initializes state data for laying out children, based
 * on the Figure given as input.
 *
 * @param parent  Figure for which the children are to 
 *                 be arranged.
 * @since 2.0 
 */
private void initVariables(IFigure parent){
	data.row = new IFigure[parent.getChildren().size()] ;
	data.bounds=new Rectangle[data.row.length];
	data.maxWidth = data.area.width;
}

/**
 * Returns whether the orientation of the layout is
 * horizontal or not.
 *
 * @return  Orientation of the layout.
 * @since 2.0
 */
public boolean isHorizontal() {return horizontal;}

/*
 * Arranges the children for the Figure given in as input,
 * based on the orientation set, and the flow layout
 * algorithm.
 */
public void layout(IFigure parent) {
	data = new WorkingData();
	Rectangle relativeArea = parent.getClientArea();
	data.area = transposer.t(relativeArea);
	data.spacing = new Dimension (minorSpacing, majorSpacing);

	Iterator iterator= parent.getChildren().iterator();
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
		Dimension pref = transposer.t(f.getPreferredSize(wHint,hHint));
		Rectangle r = new Rectangle(0,0,pref.width,pref.height);

		if (data.rowCount > 0){
			if (data.rowWidth + pref.width > data.maxWidth)
				layoutRow(parent);
		}
		r.x = data.rowX;
		r.y = data.rowY;
		dx = r.width + data.spacing.width;
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
 * Layouts one row of components. This is done based on
 * the layout's orientation, minor alignment and major alignment.
 *
 * @param parent  Figure whose children are to be placed.
 * @since 2.0
 */
protected void layoutRow(IFigure parent) {
	int majorAdjustment = 0;
	int minorAdjustment = 0;
	int correctMajorAlignment = majorAlignment;
	int correctMinorAlignment = minorAlignment;

	majorAdjustment = data.area.width - data.rowWidth;
	if(!isHorizontal()){
		correctMajorAlignment=minorAlignment;
		correctMinorAlignment=majorAlignment;
	}
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

	for (int j = 0; j < data.rowCount; j++){
		if (fill) {
			data.bounds[ j ].height = data.rowHeight;	
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
	data.rowY += data.spacing.height + data.rowHeight;
	initRow();
}

/**
 * Sets the given bounds for the child figure input.
 *
 * @param parent  Parent Figure which holds the child.
 * @param child   Child Figure whose bounds are to be set.
 * @param bounds  The size of the child to be set.
 * @since 2.0
 */
protected void setBoundsOfChild(IFigure parent, IFigure child, Rectangle bounds) {
	parent.getClientArea(Rectangle.SINGLETON);
	bounds.translate(Rectangle.SINGLETON.x, Rectangle.SINGLETON.y);
	child.setBounds(bounds);
}

/**
 * Sets flag based on layout orientation.
 * If in Horizontal orientation, all Figures will have the same height.
 * If in vertical orientation, all Figures will have the same width.
 *
 * @param value  Fill state desired.
 * @since 2.0
 */
public void setFill(boolean value) {
	fill = value;
}

/**
 * Sets the orientation of the layout.
 *
 * @param flag  Orientation of the layout.
 * @since 2.0
 */
public void setHorizontal(boolean flag){
	if (horizontal == flag) return;
	invalidate();
	horizontal = flag;
	transposer.setEnabled(!horizontal);
}

/**
 * Sets the alignment for an entire row/column within the parent figure.
 *
 * Possible values are :
 * <ul>
 *		<li>ALIGN_CENTER = 0;
 * 	<li>ALIGN_LEFTTOP = 1;
 * 	<li>ALIGN_RIGHTBOTTOM = 2;
 * </ul>
 *
 * @param align  Major alignment required.
 * @see  #getMajorAlignment()
 * @since 2.0
 */
public void setMajorAlignment(int align){
	majorAlignment = align;
}

/**
 * Sets the spacing in pixels to be used between children in 
 * the direction parallel to the layout's orientation.
 *
 * @param n  Amount of major space.
 * @see  #setMinorSpacing(int)
 * @since 2.0
 */
public void setMajorSpacing(int n){
	majorSpacing = n;
}

/**
 * Sets the alignment to be used within a row/column.
 * Possible values are :
 * <ul>
 *		<li>ALIGN_CENTER = 0;
 * 	<li>ALIGN_LEFTTOP = 1;
 * 	<li>ALIGN_RIGHTBOTTOM = 2;
 * </ul>
 *
 * @param align  Minor alignment required.
 * @see  #getMinorAlignment()
 * @since 2.0
 */
public void setMinorAlignment(int align){
	minorAlignment = align;
}

/**
 * Sets the spacing to be used between children within a row/column.
 *
 * @param n  Amount of minor space.
 * @see  #setMajorSpacing(int)
 * @since 2.0
 */
public void setMinorSpacing(int n){
	minorSpacing = n;
}

}