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
	extends AbstractLayout
{

private Dimension minimumSize;
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
 * Calculates and returns the preferred size of the container 
 * given as input. In the case of the ScrollPaneLayout
 * this is the preferred size of the passed Figure's 
 * {@link Viewport Viewport} plus its  {@link Insets Insets}.
 * 
 * @param container  Figure whose preferred size is required.
 * @return  The preferred size of the figure input.
 * @since 2.0
 */
protected Dimension calculatePreferredSize(IFigure container){
	ScrollPane scrollpane = (ScrollPane)container;
	Dimension pref = new Dimension();
	pref.width += container.getInsets().getWidth();
	pref.height += container.getInsets().getHeight();
	pref.expand(scrollpane.getViewport().getPreferredSize());
	return pref;
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

public void layout(IFigure parent) {
	ScrollPane scrollpane = (ScrollPane)parent;

	/****** CLIENT AREA MAY BE BY REFERENCE, DO NOT MODIFY ******/
	Rectangle $REFclientArea = parent.getClientArea();

	ScrollBar hBar = scrollpane.getHorizontalScrollBar(),
		    vBar = scrollpane.getVerticalScrollBar();
	Viewport viewport = scrollpane.getViewport();

	Insets insets = new Insets();
	insets.bottom = hBar.getPreferredSize().height;
	insets.right  = vBar.getPreferredSize().width;

	int hVis = scrollpane.getHorizontalScrollBarVisibility(),
	    vVis = scrollpane.getVerticalScrollBarVisibility();
	
	Dimension preferred  = viewport.getPreferredSize().getCopy();
	Dimension available  = $REFclientArea.getSize();
	Dimension guaranteed = new Dimension(available).shrink(
		    	(vVis == NEVER ? 0 : insets.right),
		    	(hVis == NEVER ? 0 : insets.bottom));
	
	//Adjust preferred size if tracking flags set
	Dimension viewportMinSize = viewport.getMinimumSize();

	if(viewport.getContentsTracksHeight()){
		preferred.height = viewportMinSize.height;
	}
	if(viewport.getContentsTracksWidth()){
		preferred.width = viewportMinSize.width;
	}

	boolean none = available.contains(preferred),
	        both = !none && preferred.containsProper(guaranteed),
		  showV= both || preferred.height > available.height,
		  showH= both || preferred.width  > available.width;
	
	//Adjust for visibility override flags
	showV = !(vVis == NEVER) && (showV  || vVis == ALWAYS);
	showH = !(hVis == NEVER) && (showH  || hVis == ALWAYS);
	
	if (!showV) insets.right = 0;
	if (!showH) insets.bottom = 0;
	Rectangle bounds, viewportArea = $REFclientArea.getCropped(insets);

	if (showV){
		bounds = new Rectangle(viewportArea.right(), viewportArea.y,
			insets.right, viewportArea.height);
		vBar.setBounds(bounds);
		//vBar.setMaximum(preferred.height);
	}
	if (showH){
		bounds = new Rectangle(viewportArea.x, viewportArea.bottom(),
			viewportArea.width, insets.bottom);
		hBar.setBounds(bounds);
		//hBar.setMaximum(preferred.width);
	}
	vBar.setVisible(showV);
	hBar.setVisible(showH);
	viewport.setBounds(viewportArea);
	hBar.setPageIncrement(hBar.getRangeModel().getExtent());
	vBar.setPageIncrement(vBar.getRangeModel().getExtent());
}

}