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
package org.eclipse.gef.internal.ui.palette.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A ToolbarLayout-like layout for the palette.  The difference is that it stretches the 
 * Figures so that they take up all the available space on the palette. 
 * 
 * @author Pratik Shah
 */
public class PaletteToolbarLayout extends ToolbarLayout {

/**
 * Constructor
 */
public PaletteToolbarLayout() {
	super();
}

/**
 * Constructor
 * 
 * @param isHorizontal  false(VERTICAL) will orient children vertically;
 *                       true(HORIZONTAL) will orient children horizontally.
 */
public PaletteToolbarLayout(boolean isHorizontal) {
	super(isHorizontal);
}

/**
 * @see org.eclipse.draw2d.ToolbarLayout#layout(IFigure)
 */
public void layout(IFigure parent) {
	List children = parent.getChildren();
	List childrenGrabbingVertical = new ArrayList();
	int numChildren = children.size();
	Rectangle clientArea = transposer.t(parent.getClientArea());
	int x = clientArea.x;
	int y = clientArea.y;
	int availableHeight = clientArea.height;
	// Indicates whether this layout is a mock run or an actual layout.
	boolean needToSetDestination = false;
	Dimension prefSizes [] = new Dimension[numChildren];
	Dimension minSizes [] = new Dimension[numChildren];
	int totalHeight = 0, totalMinHeight = 0, heightOfNonGrowingChildren = 0, 
		heightPerChild = 0, excessHeight = 0, amntShrinkHeight = 0;
	IFigure child; 
	
	/*
	 * Determine if this is a mock run or an actual layout.  This will be a mock run to
	 * determine the destination sizes of animating figures if there are any animating
	 * figures that do not know their destination sizes.
	 */
	for (Iterator iter = children.iterator(); iter.hasNext();) {
		child = (IFigure) iter.next();
		if (child instanceof DrawerFigure) {
			DrawerFigure fig = (DrawerFigure)child;
			needToSetDestination |= fig.isAnimating() 
				&& !fig.isAnimationDestinationKnown();
		}
	}

	
	// Calculate the width and height hints.  If it's a vertically-oriented layout,
	// then ignore the height hint (set it to -1); otherwise, ignore the 
	// width hint.  These hints will be passed to the children of the parent
	// figure when getting their preferred size. 
	int wHint = -1;
	int hHint = -1;    
	if (isHorizontal()) {
		hHint = parent.getClientArea(Rectangle.SINGLETON).height;
	} else {
		wHint = parent.getClientArea(Rectangle.SINGLETON).width;
	}

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
	for (int i = 0; i < numChildren; i++) {
		child = (IFigure)children.get(i);
		
		prefSizes[i] = transposer.t(child.getPreferredSize(wHint, hHint));
		minSizes[i] = transposer.t(child.getMinimumSize(wHint, hHint));
						
		if (child instanceof DrawerFigure && ((DrawerFigure)child).isAnimating()) {
			DrawerFigure fig = (DrawerFigure)child;
			if( fig.isAnimationDestinationKnown() ){
				// This animating figure already knows its destination.  It means that
				// it is collapsing, and its destination is its minimum size.
				if( needToSetDestination ){
					// If this is just a mock run to determine destination sizes, take
					// this figure's destination size (its min size) to correctly 
					// determine other destinations.
					prefSizes[i] = fig.getAnimationDestination();
				}
				heightOfNonGrowingChildren += prefSizes[i].height;
			} else {
				childrenGrabbingVertical.add(child);
			}
		} else if (prefSizes[i].height == minSizes[i].height) {
			heightOfNonGrowingChildren += prefSizes[i].height;
		} else {
			childrenGrabbingVertical.add(child);
		}
		
		totalHeight += prefSizes[i].height;
		totalMinHeight += minSizes[i].height;
	}
	totalHeight += (numChildren - 1) * spacing;
	totalMinHeight += (numChildren - 1) * spacing;


	amntShrinkHeight = totalHeight - Math.max(availableHeight, totalMinHeight);

	// So long as there is at least one child that can be grown, figure out how much
	// height should be given to each growing child.  
	if (!childrenGrabbingVertical.isEmpty()) {
		boolean childrenDiscarded;
		// spaceToConsume is the space height available on the palette that is to be
		// shared by the growing children.
		int spaceToConsume = availableHeight - heightOfNonGrowingChildren;
		// heightPerChild is the height of each growing child
		heightPerChild = spaceToConsume / childrenGrabbingVertical.size();
		// excessHeight is the space leftover at the bottom of the palette after each
		// growing child has 
		excessHeight = spaceToConsume - (heightPerChild * childrenGrabbingVertical.size());
		do {
			childrenDiscarded = false;
			for (Iterator iter = childrenGrabbingVertical.iterator(); iter.hasNext();) {
				IFigure childFig = (IFigure) iter.next();
				int i = childFig.getParent().getChildren().indexOf(childFig);
				boolean discardChild = false;
				if (amntShrinkHeight > 0) {
					// In case of shrinking, if the child's height is smaller than
					// heightPerChild, mark that child as non-growing
					discardChild = prefSizes[i].height < heightPerChild;
				} else {
					// In case of stretching, if the child's height is greater than
					// heightPerChild, mark that child as non-growing
					discardChild = prefSizes[i].height > heightPerChild;
				}
				if (discardChild) {
					spaceToConsume -= prefSizes[i].height;
					heightOfNonGrowingChildren += prefSizes[i].height;
					childrenGrabbingVertical.remove(childFig);
					childrenDiscarded = true;
					heightPerChild = spaceToConsume / childrenGrabbingVertical.size();
					excessHeight = spaceToConsume 
									- (heightPerChild * childrenGrabbingVertical.size());
					break;
				}
			}
		} while (childrenDiscarded);
	}

	for (int i = 0; i < numChildren; i++) {
		child = (IFigure)children.get(i);

		Rectangle newBounds = new Rectangle(x, y, prefSizes[i].width, prefSizes[i].height);

		if (childrenGrabbingVertical.contains(child)) {
			// Set the height of growing children.  If this is the last one, give it
			// the excess height.
			childrenGrabbingVertical.remove(child);
			if( childrenGrabbingVertical.isEmpty() ){
				newBounds.height = heightPerChild + excessHeight;
			} else {
				newBounds.height = heightPerChild;
			}
		}

		// Set the destination of animating figures that don't know their
		// destination yet, if there are any.  If not, go ahead and do the actual layout.
		if(needToSetDestination){
			if( child instanceof DrawerFigure ){
				DrawerFigure fig = (DrawerFigure)child;
				if( fig.isAnimating() && !fig.isAnimationDestinationKnown()){
					fig.setAnimationDestination(newBounds.getSize());
				}
			}
		} else {
			int minWidth = minSizes[i].width;
			int width = Math.min(prefSizes[i].width, child.getMaximumSize().width);
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
			child.setBounds(transposer.t(newBounds));
		}
		y += newBounds.height + spacing;
	}
	// If only the destinations for animating figures were set this time, do an
	// actual layout.
	if( needToSetDestination ){
		layout(parent);
	}
}

//private Dimension[] sourceSizes, destinationSizes;
//
///* (non-Javadoc)
// * @see org.eclipse.draw2d.ToolbarLayout#layout(org.eclipse.draw2d.IFigure)
// */
//public void layout(IFigure parent) {
//	// Unless there is some figure animating, this is a straight-forward layout
//	boolean needToSetDestination = false;
//	List children = parent.getChildren();
//	for (Iterator iter = children.iterator(); iter.hasNext();) {
//		IFigure child = (IFigure) iter.next();
//		if (child instanceof DrawerFigure) {
//			DrawerFigure fig = (DrawerFigure)child;
//			needToSetDestination = needToSetDestination || (fig.isAnimating() 
//			                                            && !fig.isDestinationKnown());
//		}
//	}
//	
//	if (needToSetDestination) {
//		setup(parent);
//	}
//}
//
//private void setup(IFigure parent) {
//	List children = parent.getChildren();
//	sourceSizes = new Dimension[children.size()];
//	destinationSizes = new Dimension[children.size()];
//	Dimension[] prefSizes = new Dimension[children.size()];
//	Dimension[] minSizes = new Dimension[children.size()];
//	
//	int wHint = -1;
//	int hHint = -1;    
//	if (isHorizontal()) {
//		hHint = parent.getClientArea(Rectangle.SINGLETON).height;
//	} else {
//		wHint = parent.getClientArea(Rectangle.SINGLETON).width;
//	}
//
//	for (int i = 0; i < children.size(); i++) {
//		IFigure child = (IFigure)children.get(i);
//		sourceSizes[i] = child.getBounds().getSize();
//		prefSizes[i] = child.getPreferredSize(wHint, hHint);
//		minSizes[i] = child.getMinimumSize(wHint, hHint);
//		if( child instanceof DrawerFigure ) {
//			DrawerFigure drawer = (DrawerFigure)child;
//			if( drawer.isExpanded() ){
//				// you are here....how do you determine the destination size in this case?
//			} else {
//				destinationSizes[i] = minSizes[i];
//			}
//		} else {
//			destinationSizes[i] = sourceSizes[i];
//		}
//	}
//}

}