package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import com.ibm.etools.draw2d.geometry.*;

/**
 * Layout manager that provides toolbar-like behavior.
 * When horizontally oriented, children will be placed
 * horizontally adjacent to each other. 
 * When vertically oriented, children will be placed
 * vertically adjacent to each other. In each case, children
 * will be separated by the number of pixels specified with
 * the setSpacing(int) method. 
 * 
 * If the field matchWidth is set to true, horizontally oriented
 * children will stretch themselves vertically to fit available
 * height. Vertically oriented children will stretch themselves
 * horizontally to fit available width.
 * 
 * Children will be stretched (or shrunk) in both horizontal and
 * vertical directions upon  resize until maximum
 * or minimum sizes are reached.
 * 
 */
public class ToolbarLayout
	extends AbstractLayout
{

private int spacing;
private Dimension minimumSize;
private boolean matchWidth;
protected boolean horizontal=false;
protected int minorAlignment;	

public static final int
	ALIGN_CENTER = 0,
	ALIGN_TOPLEFT = 1,
	ALIGN_BOTTOMRIGHT = 2;

public static final boolean
	HORIZONTAL = true,
	VERTICAL = false;
	
protected Transposer transposer; {
	transposer = new Transposer();
	transposer.setEnabled(horizontal);
}	
	
/**
 * Constructs a vertically oriented ToolbarLayout with 
 * child spacing of 0 pixels, matchWidth <code>true</code>, and 
 * <code>ALIGN_TOPLEFT</code> alignment.
 * 
 * @since 2.0
 */		
public ToolbarLayout(){
	spacing = 0;
	matchWidth = true;
	minorAlignment = ALIGN_TOPLEFT;
	horizontal = false;
}

/**
 * Constructs a ToolbarLayout with a specified orientation.
 * Default values are: child spacing 0 pixels, matchWidth <code>false</code>,
 * and <code>ALIGN_TOPLEFT</code> alignment.
 * 
 * @param isHorizonal  false(VERTICAL) will orient children
 *                      vertically
 *				
 *				        true(HORIZONTAL) will orient children
 *				        horizontally.
 * @since 2.0
 */
public ToolbarLayout(boolean isHorizontal){
		horizontal = isHorizontal; 
		transposer.setEnabled( horizontal );
		spacing = 0;
		matchWidth = false;
		minorAlignment = ALIGN_TOPLEFT;
}


/**
 * Calculates and returns the minimum size of the container 
 * given as input. In the case of the vertically oriented
 * ToolbarLayout, this is the width of the largest minimum 
 * width of figure's children and the height of the sum of 
 * the minimum heights of figure's children. Values are 
 * transposed to calculate minimum size of a horizontally
 * oriented ToolbarLayout.
 * 
 * @param figure  Figure whose preferred size is required.
 * @return  The minimum size of the figure input.
 * @since 2.0
 */
public Dimension calculateMinimumSize(IFigure parent){
	Insets insets = transposer.t(parent.getInsets());
	int height = 0, width = 0;
	Dimension childSize;
	
	List children = parent.getChildren();
	IFigure child;
	for (int i=0; i < children.size(); i++){
		child = (IFigure)children.get(i);
		childSize = transposer.t(child.getMinimumSize());
		height += childSize.height;
		width = Math.max (width,childSize.width);
	}

	Dimension d = new Dimension(width, height);
	d.height += insets.getHeight() + (children.size()-1)*spacing;
	d.width  += insets.getWidth();
	return transposer.t(d);
}

public Dimension getMinimumSize(IFigure container){
	if (minimumSize == null)
		minimumSize = calculateMinimumSize(container);
	return minimumSize;
}

public void invalidate(){
	minimumSize = null;
	super.invalidate();
}
	
/**
 * Calculates and returns the preferred size of the container 
 * given as input. In the case of the vertically oriented
 * ToolbarLayout, this is the width of the largest preferred 
 * width of figure's children and the height of the sum of 
 * the preferred heights of figure's children. Values are 
 * transposed to calculate preferred size of a horizontally
 * oriented ToolbarLayout.
 * 
 * @param figure  Figure whose preferred size is required.
 * @return  The minimum size of the figure input.
 * @since 2.0
 */
protected Dimension calculatePreferredSize(IFigure parent){
	Dimension preferred;
	{
		int height=0;
		int width=0;

		List children = parent.getChildren();
		Dimension childSize;
		IFigure child;
		for (int i=0; i < children.size(); i++){
			child = (IFigure)children.get(i);
			childSize = transposer.t(child.getPreferredSize());
			height += childSize.height;
			width = Math.max(width, childSize.width);	
		}
		height += (children.size()-1) * spacing;
		preferred = transposer.t(new Dimension(width, height));
		//Transpose has been applied, height and width no longer valid
	}

	Insets insets = parent.getInsets();
	preferred.height += insets.getHeight();
	preferred.width += insets.getWidth();
	preferred.union(getBorderPreferredSize(parent));
	return preferred;
}

/**
 * Returns whether the orientation of the layout is
 * horizontal or not.
 *
 * @return  Orientation of the layout.
 * @since 2.0
 */
public boolean isHorizontal() {return horizontal;}
	
public void layout(IFigure parent) {
	List children = parent.getChildren();
	int numChildren = children.size();
	Rectangle clientArea = transposer.t(parent.getClientArea());
	int x=clientArea.x;
	int y=clientArea.y;
	int availableHeight = clientArea.height;

	Dimension prefSizes [] = new Dimension[numChildren];
	Dimension minSizes [] = new Dimension[numChildren];
	
	/*		
	 * Calculate sum of preferred heights of all children(totalHeight). 
	 * Calculate sum of minimum heights of all children(minHeight).
	 * Cache Preferred Sizes and Minimum Sizes of all children.
	 *
	 * totalHeight is the sum of the preferred heights of all children
	 * totalMinHeight is the sum of the minimum heights of all children
	 * prefMinSumHeight is the sum of the difference between all children's
	 * preferred heights and minimum heights. (This is used as a ratio to 
	 * calculate how much each child will shrink). 
	 */
	IFigure child; 
	int totalHeight=0;
	int totalMinHeight=0;
	int prefMinSumHeight=0;

	for(int i=0; i < numChildren; i++){
		child = (IFigure)children.get(i);
		
		prefSizes[i]=transposer.t(child.getPreferredSize());
		minSizes[i]=transposer.t(child.getMinimumSize());		
		
		totalHeight += prefSizes[i].height;
		totalMinHeight += minSizes[i].height;
		prefMinSumHeight+=prefSizes[i].height-minSizes[i].height;		
	}
	totalHeight += (numChildren-1)*spacing;
	totalMinHeight += (numChildren-1)*spacing;
    /* 
	 * The total amount that the children must be shrunk is the 
	 * sum of the preferred Heights of the children minus  
	 * Max(the available area and the sum of the minimum heights of the children).
	 *
	 * amntShrinkHeight is the combined amount that the children must shrink
	 * amntShrinkCurrentHeight is the amount each child will shrink respectively  
	 */	
	int amntShrinkHeight = totalHeight - Math.max(availableHeight,totalMinHeight);
	
	if(amntShrinkHeight < 0){
		amntShrinkHeight = 0;
	}

	for(int i=0; i < numChildren; i++){
		int amntShrinkCurrentHeight=0;
		int prefHeight = prefSizes[i].height;
		int minHeight = minSizes[i].height;
		int prefWidth = prefSizes[i].width;
		int minWidth = minSizes[i].width;
		Rectangle newBounds = new Rectangle(x, y, prefWidth, prefHeight);

		child = (IFigure)children.get(i);
	    if( prefMinSumHeight != 0)
			amntShrinkCurrentHeight = (prefHeight - minHeight) * amntShrinkHeight / (prefMinSumHeight);

		int width = Math.min(prefWidth,child.getMaximumSize().width);
	      if (matchWidth)
			width = transposer.t(child.getMaximumSize()).width;
		width = Math.max(minWidth, Math.min(clientArea.width, width));
		newBounds.width = width;

	      int adjust = clientArea.width - width;
		switch (minorAlignment) {
		case ALIGN_TOPLEFT: 
			adjust = 0;
			break;
		case ALIGN_CENTER:
			adjust /= 2;
			break;
		case ALIGN_BOTTOMRIGHT:
			break;
		}
		newBounds.x += adjust;
		newBounds.height -= amntShrinkCurrentHeight;
		child.setBounds(transposer.t(newBounds));

		amntShrinkHeight -= amntShrinkCurrentHeight;
		prefMinSumHeight -= prefHeight - minHeight;
		y += newBounds.height + spacing;
	}
}				

/**
 * Sets the alignment of the children contained in the layout.
 * 
 * @param   align 0 (ALIGN_CENTER), 1 (ALIGN_TOPLEFT)
 *		       2 (ALIGN_BOTTOMRIGHT)
 * @since 2.0
 */
public void setMinorAlignment(int align){
	minorAlignment = align;
}

/**
 * Sets the amount of space between children
 * 
 * @param   space The amount of space between children.
 * @since 2.0
 */
public void setSpacing(int space){
	spacing = space;
}

/**
 * Sets children's width (if vertically oriented) or height
 * (if horizontally oriented) to stretch with their container
 * 
 * @param   match <code>true</code> will stretch the children, 
 *           <code>false</code> will not
 * @since 2.0
 */
public void setMatchWidth(boolean match){
	matchWidth = match;
}

/**
 * Sets the orientation of the children in the 
 * ToolbarLayout.
 * 
 * @param flag <code>true</code> sets orientation to vertical
 *              <code>false</code> sets oreintation to horizontal
 * @since 2.0
 */
public void setVertical(boolean flag){
	if (horizontal != flag) return;
	invalidate();
	horizontal = !flag;
	transposer.setEnabled(horizontal);
}

}