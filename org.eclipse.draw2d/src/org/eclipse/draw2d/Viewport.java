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
package org.eclipse.draw2d;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.geometry.*;

/**
 * A Viewport is a flexible window onto a {@link ScrollPane} and
 * represents the visible portion of the ScrollPane.
 */
public class Viewport
	extends Figure
	implements PropertyChangeListener
{

public static String PROPERTY_VIEW_LOCATION = "viewLocation";//$NON-NLS-1$
private IFigure view;

private boolean useTranslate = false;
private boolean trackWidth = false;
private boolean trackHeight = false;
private boolean ignoreScroll = false;
private RangeModel 
	horiztonalRangeModel = null,
	verticalRangeModel = null;

{
	setLayoutManager(new ViewportLayout());
	setHorizontalRangeModel(new DefaultRangeModel());
	setVerticalRangeModel(new DefaultRangeModel());
}

public Viewport(){}

public Viewport(boolean setting){
	useTranslate = setting;
}

public Rectangle getClientArea(Rectangle rect){
	super.getClientArea(rect);
	if (useGraphicsTranslate())
		rect.translate(getViewLocation());
	return rect;
}

/**
 * Returns the view, which is the contents of the 
 * {@link ScrollPane} associated with this Viewport.
 * 
 * @since 2.0
 */
public IFigure getContents(){
	return view;
}

/**
 * Returns the RangeModel associated with the horizontal 
 * motion of this Viewport.
 * 
 * @since 2.0
 */
public RangeModel getHorizontalRangeModel(){
	return horiztonalRangeModel;
}

/**
 * Returns true if the Viewport resizes itself in the
 * vertical direction when the available height of its
 * view is decreased, false otherwise. This option is
 * turned off by default, and can be activated by calling
 * setContentsTracksHeight(true).
 * 
 * @since 2.0
 */
public boolean getContentsTracksHeight(){
	return trackHeight;
}

/**
 * Returns true if the Viewport resizes itself in the
 * horizontal direction when the available width of its
 * view is decreased, false otherwise. This option is
 * turned off by default, and can be activated by calling
 * setContentsTracksWidth(true).
 * 
 * @since 2.0
 */
public boolean getContentsTracksWidth(){
	return trackWidth;
}		

/**
 * Returns the RangeModel associated with the vertical 
 * motion of the Viewport.
 * 
 * @since 2.0
 */
public RangeModel getVerticalRangeModel(){
	return verticalRangeModel;
}

/**
 * Returns the current location of this Viewport.
 * 
 * @since 2.0
 */
public Point getViewLocation(){
	return new Point(
		getHorizontalRangeModel().getValue(),
		getVerticalRangeModel().getValue());
}

private void localRevalidate(){
	invalidate();
	if (getLayoutManager() != null)
		getLayoutManager().invalidate();
	getUpdateManager().addInvalidFigure(this);
}

protected void paintClientArea(Graphics g){
	if (useGraphicsTranslate()){
		Point p = getViewLocation();
		try {
			g.translate(-p.x,-p.y);
			g.pushState();
			super.paintClientArea(g);
			g.popState();
		} finally {
			g.translate(p.x,p.y);
		}
	} else
		super.paintClientArea(g);
}

public void propertyChange(PropertyChangeEvent event){
	if(event.getSource() instanceof RangeModel){
		if(RangeModel.PROPERTY_VALUE.equals(event.getPropertyName())){
			if (!ignoreScroll) {
				localRevalidate();
				if(useGraphicsTranslate()){
					repaint();
					fireMoved(); //Must fire moved because the contents have been virtually scrolled.
				}
			}
			firePropertyChange(PROPERTY_VIEW_LOCATION, event.getOldValue(), event.getNewValue());
		}
	}
}

/**
 * Sets extents of {@link RangeModel RangeModels} to the
 * client area of this Viewport. Sets RangeModel minimums
 * to zero. Sets RangeModel maximums to this Viewports height/width.
 * 
 * @since 2.0
 */
protected void readjustScrollBars(){
	if (getContents() == null)
		return;
	getVerticalRangeModel().setAll(0, getClientArea().height, getContents().getBounds().height);		
	getHorizontalRangeModel().setAll(0, getClientArea().width, getContents().getBounds().width);
}

/**
 * Sets this Viewport to be associated with the passed
 * Figure.
 * 
 * @since 2.0
 */
public void setContents(IFigure figure){
	if (view != null) remove(view);
	view = figure;
	add(figure);
}

/**
 * Toggles the Viewport's ability to resize itself automatically 
 * when its view is decreased in size in the vertical direction.
 * This is disabled by default.
 * 
 * @since 2.0
 */
public void setContentsTracksHeight(boolean track){
	trackHeight = track;
}

/**
 * Toggles the Viewport's ability to resize itself automatically 
 * when its view is decreased in size in the horizontal direction.
 * This is disabled by default.
 * 
 * @since 2.0
 */
public void setContentsTracksWidth(boolean track){
	trackWidth = track;
}		

/**
 * Sets the horizontal location of the Viewport's view to the
 * passed value.
 * 
 * @since 2.0
 */
public void setHorizontalLocation(int value){
	setViewLocation( value, getVerticalRangeModel().getValue() );
}

/**
 * Sets the horizontal range model to the passed RangeModel.
 * 
 * @since 2.0
 */
public void setHorizontalRangeModel(RangeModel rangeModel){
	if( horiztonalRangeModel != null )
		horiztonalRangeModel.removePropertyChangeListener(this);
	horiztonalRangeModel = rangeModel;
	horiztonalRangeModel.addPropertyChangeListener(this);
}

public void setIgnoreScroll(boolean value) {
	ignoreScroll = value;
}

/**
 * Sets the vertical location of the Viewport's view to the
 * passed value.
 * 
 * @since 2.0
 */
public void setVerticalLocation(int value){
	setViewLocation( getHorizontalRangeModel().getValue(), value );
}

/**
 * Sets the vertical range model to the passed RangeModel.
 * 
 * @since 2.0
 */
public void setVerticalRangeModel(RangeModel rangeModel){
	if(verticalRangeModel != null)
		verticalRangeModel.removePropertyChangeListener(this);
	verticalRangeModel = rangeModel;
	verticalRangeModel.addPropertyChangeListener(this);
}

/**
 * Sets the location of the Viewport's view to the passed values.
 * 
 * @param x The new x coordinate of the Viewport's view.
 * @param y The new y coordinate of the Viewport's view.
 * @since 2.0
 */
public void setViewLocation(int x, int y){
	if( getHorizontalRangeModel().getValue() != x )
		getHorizontalRangeModel().setValue(x);
	if( getVerticalRangeModel().getValue() != y )
		getVerticalRangeModel().setValue(y);
}

/**
 * Sets the location of the Viewport's view to the passed Point.
 * 
 * @param p The new location of the Viewport's view.
 * @since 2.0 
 */
public void setViewLocation(Point p){
	setViewLocation(p.x, p.y);
}

public void translateFromParent(Translatable t){
	if (useTranslate)
		t.performTranslate(
			getHorizontalRangeModel().getValue(),
			getVerticalRangeModel().getValue()
		);
	super.translateFromParent(t);
}

public void translateToParent(Translatable t){
	if (useTranslate)
		t.performTranslate(
			-getHorizontalRangeModel().getValue(),
			-getVerticalRangeModel().getValue()
		);
	super.translateToParent(t);
}

public boolean useGraphicsTranslate(){
	return useTranslate;
}

public void validate(){
	super.validate();
	readjustScrollBars();
}

}