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
 * Implements the ILayoutManager interface using the Flow Layout
 * algorithm. This displays the components of the container figure 
 * in an ordered list. The components can also be aligned in different 
 * ways within the available space.
 */
public class FlowLayout
	extends AbstractLayout
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
protected boolean fFill = false;

protected Transposer transposer; {
	transposer = new Transposer();
	transposer.setEnabled(!horizontal);
}

/*
 * Internal state
 */
protected int majorAlignment = ALIGN_LEFTTOP;
protected int minorAlignment = ALIGN_LEFTTOP;
private int hSpacing = 5, vSpacing = 5;
private WorkingData data = null;

/**
 * Holds the necessary information for layout calculations.
 */
class WorkingData {
	int rowHeight, rowWidth, rowCount, rowX, rowY, maxWidth;
	Rectangle bounds[], area;
	Insets insets;
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
 *
 * @param isHorizontal  Whether the layout should be horizontal.
 * @since 2.0
 */
public FlowLayout(boolean isHorizontal){setHorizontal(isHorizontal);}

/**
 * Calculates and returns the preferred size of the container 
 * given as input.
 * 
 * @param figure  Figure whose preferred size is required.
 * @return  The preferred size of the passed Figure.
 * @since 2.0
 */
protected Dimension calculatePreferredSize(IFigure parent){
	List children = parent.getChildren();
	Dimension
		preferred = new Dimension(),
		childSize;
	IFigure child;
	for (int i=0; i < children.size(); i++){
		child = (IFigure)children.get(i);
		childSize = transposer.t(child.getPreferredSize());
		preferred.width += childSize.width;
		preferred.height = Math.max(preferred.height, childSize.height);
	}
	if (children.size() > 0)
		preferred.width += hSpacing * (children.size() - 1);
	preferred = transposer.t(preferred);
	preferred.width += parent.getInsets().getWidth();
	preferred.height+= parent.getInsets().getHeight();
	preferred.union(getBorderPreferredSize(parent));
	return preferred;
}

/**
 * Returns the alignment along the major orientation of
 * the layout.
 * Possible values are :
 * <ul>
 *		<li>ALIGN_CENTER 		= 0;
 * 		<li>ALIGN_LEFTTOP 	= 1;
 * 		<li>ALIGN_RIGHTBOTTOM 	= 2;
 * </ul>
 *
 * @return  Major alignment of the children.
 * @see  #setMajorAlignment(int)
 * @see  #setMinorAlignment(int)
 * @see  #getMinorAlignment()
 * @since 2.0
 */
public int getMajorAlignment() {return majorAlignment;}

/** 
 * Returns the alignment of the layout perpendicular to 
 * the orientation of the layout.
 * Possible values are :
 * <ul>
 *		<li>ALIGN_CENTER 		= 0;
 * 		<li>ALIGN_LEFTTOP 	= 1;
 * 		<li>ALIGN_RIGHTBOTTOM 	= 2;
 * </ul>
 *
 * @return  Minor alignment of the children.
 * @see  #setMajorAlignment(int)
 * @see  #setMinorAlignment(int)
 * @see  #getMajorAlignment()
 * @since 2.0
 */
public int getMinorAlignment() {return minorAlignment;}

/**
 * Initializes the state of row data, which is internal
 * to the layout process. 
 */
private void initRow(){
	data.rowX = data.insets.left;
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
	data.rowY = data.insets.top;
	data.maxWidth = data.area.width - data.insets.getWidth();
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
	Rectangle relativeArea = parent.getBounds().getCopy();
	parent.translateFromParent(relativeArea);
	data.area = transposer.t(relativeArea);
	data.insets  = transposer.t(parent.getInsets());
	data.spacing = new Dimension (hSpacing, vSpacing);

	Iterator iterator= parent.getChildren().iterator();
	int dx;

	initVariables(parent);
	initRow();
	int i= 0; 
	while (iterator.hasNext()) {
		IFigure f = (IFigure)iterator.next();
		Dimension pref = transposer.t(f.getPreferredSize());
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

	majorAdjustment = data.area.width - data.insets.getWidth() - data.rowWidth;
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
		if( fFill ) {
			data.bounds[ j ].height = data.rowHeight;	
		}
		else {
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
	bounds.translate(parent.getBounds().getLocation());
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
	fFill = value;
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
 * Sets the alignment required along the orientation 
 * direction set. 
 *
 * Possible values are :
 * <ul>
 *		<li>ALIGN_CENTER 		= 0;
 * 		<li>ALIGN_LEFTTOP 	= 1;
 * 		<li>ALIGN_RIGHTBOTTOM 	= 2;
 * </ul>
 *
 * @param align  Major alignment required.
 * @see  #setMinorAlignment(int)
 * @see  #getMinorAlignment()
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
	vSpacing = n;
}

/**
 * Sets the alignment to be used between children
 * perpendicular to the orientation of the layout.
 *
 * Possible values are :
 * <ul>
 *		<li>ALIGN_CENTER 		= 0;
 * 		<li>ALIGN_LEFTTOP 	= 1;
 * 		<li>ALIGN_RIGHTBOTTOM 	= 2;
 * </ul>
 *
 * @param align  Minor alignment required.
 * @see  #setMajorAlignment(int)
 * @see  #getMinorAlignment()
 * @see  #getMajorAlignment()
 * @since 2.0
 */
public void setMinorAlignment(int align){
	minorAlignment = align;
}

/**
 * Sets the spacing to be used between children
 * perpendicular to the orientation of the layout.
 *
 * @param n  Amount of minor space.
 * @see  #setMajorSpacing(int)
 * @since 2.0
 */
public void setMinorSpacing(int n){
	hSpacing = n;
}

/**
 * Sets the alignment to be used between children
 * when laying out the components.
 *
 * @deprecated  Use specific major, minor alignment approaches.
 * @see  #setMajorAlignment(int)
 * @see  #setMinorAlignment(int)
 * @see  #getMinorAlignment()
 * @see  #getMajorAlignment()
 * @since 2.0
 */
public void setAlignment(int align){
	minorAlignment = align;
}

}