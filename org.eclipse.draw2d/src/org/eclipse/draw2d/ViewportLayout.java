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
	extends AbstractHintLayout
{

/* 
 * Returns the minimum size required by the input viewport
 * figure. Since viewport is flexible, the minimum size
 * required would be the just the size of the borders.
 */
public Dimension calculateMinimumSize(IFigure figure){
	Viewport viewport = (Viewport)figure;
	Dimension min = new Dimension();
	Insets insets = viewport.getInsets();
	return min.getExpanded(insets.getWidth(), insets.getHeight());
}

/**
 * Calculates and returns the preferred size of the figure based on the 
 * given hints.  The given wHint is ignored unless the viewport (parent) is
 * tracking width.  The same is true for the height hint.
 * 
 * @param	parent	The Viewport whose preferred size is to be calculated
 * @param	wHint	The width hint
 * @param	hHint	The height hint
 * @return	Preferred size of the given Viewport
 * @since	2.0
 */
protected Dimension calculatePreferredSize(IFigure parent, int wHint, int hHint){
	Viewport viewport = (Viewport)parent;
	Insets insets = viewport.getInsets();
	IFigure contents = viewport.getContents();
	int widthHint = -1;
	int heightHint = -1;

	if (viewport.getContentsTracksWidth() && wHint > -1)
		widthHint = Math.max(0, wHint - insets.getWidth());
	if (viewport.getContentsTracksHeight() && hHint > -1)
		heightHint = Math.max(0, hHint - insets.getHeight());
	
	if (contents == null){
		return new Dimension(insets.getWidth(), insets.getHeight());
	} else {
		return contents
			.getPreferredSize(widthHint, heightHint)
			.getExpanded(insets.getWidth(), insets.getHeight());
	}
	
	//Layout currently does not union border's preferred size.
}	

public void layout(IFigure figure){
	Viewport viewport = (Viewport)figure;
	IFigure contents = viewport.getContents();
	
	if (contents == null) return;
	Point p = viewport.getClientArea().getLocation();

	p.translate(viewport.getViewLocation().getNegated());
	
	// Calculate the hints
	Rectangle hints = viewport.getClientArea().getCropped(viewport.getInsets());
	int wHint = viewport.getContentsTracksWidth() ? hints.width : -1;
	int hHint = viewport.getContentsTracksHeight() ? hints.height : -1;
	
	Dimension avail = viewport.getClientArea().getSize();
	Dimension size = avail.getUnioned(contents.getPreferredSize(wHint,hHint));
	Dimension min = contents.getMinimumSize();
	
	if (viewport.getContentsTracksHeight())
		size.height = Math.max(min.height, avail.height);
	if (viewport.getContentsTracksWidth())
		size.width = Math.max(min.width, avail.width);

	contents.setBounds(new Rectangle(p,size));
}

}