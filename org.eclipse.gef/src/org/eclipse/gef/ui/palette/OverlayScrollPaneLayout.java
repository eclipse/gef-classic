package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.*;

class OverlayScrollPaneLayout
	extends ScrollPaneLayout
{

public void layout(IFigure parent){
	ScrollPane scrollpane = (ScrollPane)parent;
	Rectangle clientArea = parent.getClientArea();

	ScrollBar hBar = scrollpane.getHorizontalScrollBar(),
		    vBar = scrollpane.getVerticalScrollBar();
	Viewport viewport = scrollpane.getViewport();

	Insets insets = new Insets();
	insets.bottom = hBar.getPreferredSize().height;
	insets.right  = vBar.getPreferredSize().width;

	int hVis = scrollpane.getHorizontalScrollBarVisibility(),
	    vVis = scrollpane.getVerticalScrollBarVisibility();

	Dimension preferred = viewport.getPreferredSize(),
		    available = clientArea.getSize(),
		    guaranteed = new Dimension(available).shrink(
		    	(vVis == NEVER ? 0 : insets.right),
		    	(hVis == NEVER ? 0 : insets.bottom) );

	boolean none = available.contains(preferred),
		  both = !none && vVis != NEVER && hVis != NEVER && preferred.contains(guaranteed),
		  showV= both || preferred.height > available.height,
		  showH= both || preferred.width  > available.width;

	//Adjust for visibility override flags
	showV = !(vVis == NEVER) && (showV || vVis == ALWAYS);
	showH = !(hVis == NEVER) && (showH || hVis == ALWAYS);

	if (!showV) insets.right = 0;
	if (!showH) insets.bottom = 0;
	Rectangle bounds, viewportArea = clientArea;

	if (showV){
		bounds = new Rectangle(
			viewportArea.right()-insets.right,
			viewportArea.y, insets.right, viewportArea.height);
		vBar.setBounds(bounds);
		//vBar.setMaximum(preferred.height);
	}
	if (showH){
		bounds = new Rectangle(viewportArea.x, 
			viewportArea.bottom()-insets.bottom,
			viewportArea.width, insets.bottom);
		hBar.setBounds(bounds);
		//hBar.setMaximum(preferred.width);
	}
	vBar.setVisible(showV);
	hBar.setVisible(showH);
	viewport.setBounds(viewportArea);
}

}