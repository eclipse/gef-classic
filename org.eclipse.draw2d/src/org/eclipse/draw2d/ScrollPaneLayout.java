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

/** * @see AbstractHintLayout#calculateMinimumSize(IFigure, int, int) */
public Dimension calculateMinimumSize(IFigure figure, int w, int h) {
	ScrollPane scrollpane = (ScrollPane)figure;
	Insets insets = scrollpane.getInsets();
	Dimension d = scrollpane.getViewport().getMinimumSize(w, h);
	return d.getExpanded(insets.getWidth(), insets.getHeight());
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

/** * @see org.eclipse.draw2d.LayoutManager#layout(IFigure) */
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
	
	if (result.showV) {
		vBar.setBounds(new Rectangle(
			result.viewportArea.right(),
			result.viewportArea.y,
			result.insets.right,
			result.viewportArea.height));
	}
	if (result.showH) {
		hBar.setBounds(new Rectangle(
			result.viewportArea.x,
			result.viewportArea.bottom(),
			result.viewportArea.width,
			result.insets.bottom));
	}
	vBar.setVisible(result.showV);
	hBar.setVisible(result.showH);
	
	int vStepInc = vBar.getStepIncrement();
	int vPageInc = vBar.getRangeModel().getExtent() - vStepInc;
	if (vPageInc < vStepInc)
		vPageInc = vStepInc;
	vBar.setPageIncrement(vPageInc);
	
	int hStepInc = hBar.getStepIncrement();
	int hPageInc = hBar.getRangeModel().getExtent() - hStepInc;
	if (hPageInc < hStepInc)
		hPageInc = hStepInc;
	hBar.setPageIncrement(hPageInc);
}

}