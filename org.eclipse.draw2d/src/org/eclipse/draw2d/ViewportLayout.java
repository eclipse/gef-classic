package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * Layout for a viewport. A viewport is a flexible window
 * onto a figure.
 */
public class ViewportLayout 
	extends AbstractLayout
{

/**
 * Calculates and returns the preferred size of the figure
 * given in the input. Since the viewport would generally
 * like to cover the contents it is windowing, it returns the
 * preferred size of the contents, with its own borders added.
 *
 * @param  Viewport for which the preferred size is required.
 * @return  Preferred size of the viewport.
 * @since 2.0
 */
protected Dimension calculatePreferredSize(IFigure parent){
	Viewport viewport = (Viewport)parent;
	IFigure contents = viewport.getContents();
	Dimension d = contents == null ? new Dimension() : contents.getPreferredSize().getCopy();
	Insets insets = viewport.getInsets();
	d.expand(insets.getWidth(), insets.getHeight());
	return d;
}

public void layout(IFigure figure){
	Viewport viewport = (Viewport)figure;
	IFigure contents = viewport.getContents();
	
	if (contents == null) return;
	Point p = viewport.getClientArea().getLocation();

	p.translate(viewport.getViewLocation().getNegated());
	
	Dimension avail = viewport.getClientArea().getSize();
	Dimension size = avail.getUnioned(contents.getPreferredSize());
	Dimension min = contents.getMinimumSize();
	
	if(viewport.getContentsTracksHeight())
		size.height = Math.max(min.height, avail.height);
	if(viewport.getContentsTracksWidth())
		size.width = Math.max(min.width, avail.width);

	contents.setBounds(new Rectangle(p,size));
}

/* 
 * Returns the minimum size required by the input viewport
 * figure. Since viewport is flexible, the minimum size
 * required would be the just the size of the borders.
 */
public Dimension getMinimumSize(IFigure figure){
	Viewport viewport = (Viewport)figure;
	Dimension min = new Dimension();
	Insets insets = viewport.getInsets();
	return min.getExpanded(insets.getWidth(), insets.getHeight());
}

}