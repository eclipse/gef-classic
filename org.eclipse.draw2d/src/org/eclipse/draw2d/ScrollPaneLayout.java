package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;


/**
 * The ScrollPaneLayout is responsible for laying out the
 * {@link Viewport Viewport} and {@link ScrollBar ScrollBars}
 * of a {@link ScrollPane ScrollPane}.
 */
public class ScrollPaneLayout
	extends AbstractHintLayout
{

protected static final int
	NEVER = ScrollPane.NEVER,
	AUTO  = ScrollPane.AUTOMATIC,
	ALWAYS= ScrollPane.ALWAYS;

/**
 * Calculates and returns the minimum size of the container 
 * given as input. In the case of the ScrollPaneLayout
 * this is the minimum size of the passed Figure's 
 * {@link Viewport Viewport} plus its {@link Insets Insets}.
 * 
 * @param figure  Figure whose preferred size is required.
 * @return  The minimum size of the Figure input.
 * @since 2.0
 */
public Dimension calculateMinimumSize(IFigure figure){
	ScrollPane scrollpane = (ScrollPane)figure;
	Insets insets = scrollpane.getInsets();
	Dimension d = scrollpane.getViewport().getMinimumSize();
	return d.getExpanded(insets.getWidth(),insets.getHeight());
}

/**
 * Calculates and returns the preferred size of the container based on the 
 * given hints.  If the given ScrollPane's (container's) horizontal and 
 * vertical scroll bar visibility is not <code>ScrollPane.NEVER</code>, then 
 * space for those bars is always deducted from the hints (whether or not we
 * actually need the scroll bars).
 * 
 * @param	container	The ScrollPane whose preferred size needs to be 
 * 						calculated
 * @param	wHint		The width hint
 * @param	hHint		The height hint
 * @return	The preferred size of the given container
 * @since	2.0
 */
protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint){
	ScrollPane scrollpane = (ScrollPane)container;
	ScrollBar hBar = scrollpane.getHorizontalScrollBar();
	ScrollBar vBar = scrollpane.getVerticalScrollBar();
	Insets insets = scrollpane.getInsets();

	int reservedWidth = insets.getWidth();
	int reservedHeight = insets.getHeight();

	if (scrollpane.getVerticalScrollBarVisibility() != ScrollPane.NEVER)
		reservedWidth += vBar.getPreferredSize().width;
	if (scrollpane.getHorizontalScrollBarVisibility() != ScrollPane.NEVER)
		reservedHeight += hBar.getPreferredSize().height;

	if (wHint > -1)
		wHint = Math.max(0, wHint - reservedWidth);
	if (hHint > -1)
		hHint = Math.max(0, hHint - reservedHeight);

	return scrollpane
		.getViewport()
		.getPreferredSize(wHint, hHint)
		.getExpanded(reservedWidth, reservedHeight);
}

public void layout(IFigure parent) {
	ScrollPane scrollpane = (ScrollPane)parent;
	Viewport viewport = scrollpane.getViewport();
	ScrollBar hBar = scrollpane.getHorizontalScrollBar(),
		      vBar = scrollpane.getVerticalScrollBar();
	
	ScrollPaneSolver.Result result = ScrollPaneSolver.solve(
					parent.getClientArea(), 
					viewport, 
					scrollpane.getHorizontalScrollBarVisibility(),
					scrollpane.getVerticalScrollBarVisibility(), 
					vBar.getPreferredSize().width, 
					hBar.getPreferredSize().height);
	
	if (result.showV){
		Rectangle bounds = new Rectangle(result.viewportArea.right(), 
		                                  result.viewportArea.y,
		                                  result.insets.right, 
		                                  result.viewportArea.height);
		vBar.setBounds(bounds);
		//vBar.setMaximum(preferred.height);
	}
	if (result.showH){
		Rectangle bounds = new Rectangle(result.viewportArea.x, 
		                                  result.viewportArea.bottom(),
		                                  result.viewportArea.width, 
		                                  result.insets.bottom);
		hBar.setBounds(bounds);
		//hBar.setMaximum(preferred.width);
	}
	vBar.setVisible(result.showV);
	hBar.setVisible(result.showH);
	hBar.setPageIncrement(hBar.getRangeModel().getExtent());
	vBar.setPageIncrement(vBar.getRangeModel().getExtent());
}

}