package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Point;

/**
 * A class which implements automatic horizontal and/or
 * vertical scrolling for a single IFigure child.
 * 
 * ScrollBar visibilites are represented by integer class
 * constants:
 * NEVER: Never show the ScrollBar
 * AUTOMATIC: Show as needed, when the ScrollPane can no longer
 *            contain its view.
 * ALWAYS: Always show the ScrollBar.
 * 
 * To use, instantiate a ScrollPane object and call its
 * setView(IFigure) method passing the IFigure that is desired
 * to have scrolling ability.
 */
public class ScrollPane
	extends Figure
{

public static final int
	NEVER = 0,
	AUTOMATIC = 1,
	ALWAYS = 2;

protected Viewport viewport;
protected ScrollBar hBar, vBar;
private int
	hVisibility = AUTOMATIC,
	vVisibility = AUTOMATIC;

/**
 * Constructs a new ScrollPane with a ScrollPaneLayout.
 * 
 * @since 2.0
 */
public ScrollPane() {
	setLayoutManager(new ScrollPaneLayout());
}

/**
 * Creates a new horizontally oriented ScrollBar
 * and adds it to this ScrollPane.
 * 
 * @since 2.0
 */
protected void createHorizontalScrollBar(){
	ScrollBar bar = new ScrollBar();
	bar.setHorizontal(true);
	setHorizontalScrollBar(bar);
}

/**
 * Creates a new Viewport and adds it to this
 * ScrollPane.
 * 
 * @since 2.0
 */
protected void createViewport(){
	setViewport(new Viewport());
}

/**
 * Creates a new vertically oriented ScrollBar
 * and adds it to this ScrollPane.
 * 
 * @since 2.0
 */
protected void createVerticalScrollBar(){
	ScrollBar bar = new ScrollBar();
	setVerticalScrollBar(bar);
}

/**
 * Returns the ScrollPane's horizontal ScrollBar.
 * 
 * @since 2.0
 */
public ScrollBar getHorizontalScrollBar(){
	if (hBar == null)
		createHorizontalScrollBar();
	return hBar;
}

/**
 * Returns the visibility of the ScrollPane's 
 * horizontal ScrollBar. These are represented by
 * the integer class constants NEVER, AUTOMATIC, and ALWAYS.
 * Default is AUTOMATIC.
 * 
 * @since 2.0
 */
public int getHorizontalScrollBarVisibility(){
	return hVisibility;
}

/**
 * Returns the ScrollPane's vertical ScrollBar.
 * 
 * @since 2.0
 */
public ScrollBar getVerticalScrollBar(){
	if (vBar == null)
		createVerticalScrollBar();
	return vBar;
}

/**
 * Returns the visibility of the ScrollPane's 
 * vertical ScrollBar. These are represented by
 * the integer class constants NEVER, AUTOMATIC, and ALWAYS.
 * Default is AUTOMATIC.
 * 
 * @since 2.0
 */
public int getVerticalScrollBarVisibility(){
	return vVisibility;
}

/**
 * Returns the ScrollPane's view. The view is the IFigure 
 * that is the contents of the ScrollPane.
 * 
 * @since 2.0
 */ 
public IFigure getView() {
	return getViewport().getContents();
}

/**
 * Returns the ScrollPane's {@link Viewport}.
 * 
 * @since 2.0
 */ 
public Viewport getViewport(){
	if (viewport == null)
		createViewport();
	return viewport;
}

/*
 * ScrollPane's are always opaque
 */
public boolean isOpaque(){
	return true;
}

/**
 * Scrolls the Scrollpane horizontally x pixels
 * from its left-most position.
 * 
 * @since 2.0
 */
public void scrollHorizontalTo(int x){
	getViewport().setHorizontalLocation(x);
}

/**
 * Scrolls the Scrollpane horizontally from its left-most position
 * by location.x pixels and vertically from its top-most position 
 * by location.y pixels.
 * 
 * @since 2.0
 */ 
public void scrollTo(Point location){
	scrollHorizontalTo(location.x);
	scrollVerticalTo(location.y);
}

/**
 * Scrolls the Scrollpane vertically y pixels
 * from its top-most position. 
 * 
 * @since 2.0
 */
public void scrollVerticalTo(int y){
	getViewport().setVerticalLocation(y);
}

/**
 * Sets the ScrollPane's horizontal ScrollBar to the passed
 * ScrollBar.
 * 
 * @since 2.0
 */
public void setHorizontalScrollBar(ScrollBar bar) {
	if (hBar != null){
		remove(hBar);
		hBar.getRangeModel().removePropertyChangeListener(hBar);
	}
	hBar = bar;
	if (hBar != null){
		add(hBar);
		hBar.setRangeModel(getViewport().getHorizontalRangeModel());
	}
}

/**
 * Sets the horizontal ScrollBar visibility of the ScrollPane 
 * to the passed value. Valid visiblities are represented by
 * the integer class constants NEVER, AUTOMATIC, and ALWAYS.
 * Default is AUTOMATIC.
 * 
 * @since 2.0
 */
public void setHorizontalScrollBarVisibility(int v){
	if (hVisibility == v) return;
	hVisibility = v;
	revalidate();
}

/**
 * Sets both the horizontal and vertical ScrollBar visibilities of 
 * the ScrollPane to the passed value. Valid visiblities are 
 * represented by the integer class constants NEVER, AUTOMATIC, 
 * and ALWAYS. Default is AUTOMATIC.
 * 
 * @since 2.0
 */
public void setScrollBarVisibility(int v){
	setHorizontalScrollBarVisibility(v);
	setVerticalScrollBarVisibility(v);
}

/**
 * Sets the ScrollPane's vertical ScrollBar to the passed
 * Scrollbar.
 * 
 * @since 2.0
 */
public void setVerticalScrollBar(ScrollBar bar) {
	if (vBar != null){
		remove(vBar);
		vBar.getRangeModel().removePropertyChangeListener(vBar);
	}
	vBar = bar;
	if (vBar != null){
		add(vBar);
		vBar.setRangeModel(getViewport().getVerticalRangeModel());
	}
}

/**
 * Sets the vertical ScrollBar visibility of the ScrollPane 
 * to the passed value. Valid visiblities are represented by
 * the integer class constants NEVER, AUTOMATIC, and ALWAYS.
 * Default is AUTOMATIC.
 * 
 * @since 2.0
 */
public void setVerticalScrollBarVisibility(int v){
	if (vVisibility == v) return;
	vVisibility = v;
	revalidate();
}

/**
 * Sets the ScrollPane's view to the passed IFigure.
 * The view is the top-level IFigure which represents the contents
 * of the ScrollPane.
 * 
 * @since 2.0
 */
public void setView(IFigure figure){
	getViewport().setContents(figure);
}

/**
 * Sets the ScrollPane's Viewport to the passed value.
 * 
 * @since 2.0
 */
public void setViewport(Viewport vp){
	if (viewport != null)
		remove(viewport);
	viewport = vp;
	if (vp != null)
		add(vp,0);
}

public void validate(){
	super.validate();
	getHorizontalScrollBar().validate();
	getVerticalScrollBar().validate();
}

}