package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.util.UITimer;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Point;

/**
 * This class is used by SWTEventDispatcher as support to 
 * display Figure tooltips on a mouse hover event. Tooltips 
 * are drawn directly below the cursor unless the display 
 * does not allow, in which case the tooltip will be drawn 
 * directly above the cursor. Tooltips will be displayed
 * with a LineBorder. The background of the tooltips will
 * be the standard SWT tooltipBackground color unless 
 * the Figure's tooltip has set its own background.
 */
 
public class ToolTipHelper 
	extends PopUpHelper
{

private UITimer timer;
private IFigure currentTipSource;

/**
 * Constructs a ToolTipHelper to be associated with Control <i>c</i>.
 * 
 * @since 2.0
 */
public ToolTipHelper(org.eclipse.swt.widgets.Control c){
	super(c);
	getLightweightSystem().getRootFigure().setBorder(new LineBorder());
	getShell().setBackground(ColorConstants.tooltipBackground);
	getShell().setForeground(ColorConstants.tooltipForeground);
}

/**
 * Calculates the location where the tooltip will be
 * painted. Returns this as a Point. Tooltip will 
 * be painted directly below the cursor if possible,
 * otherwise it will be painted directly above cursor.
 *
 * @param tip  The tool tip to be displayed.
 * @param eventX X coordinate of the hover event
 * @param eventY Y coodrinate of the hover event
 * @since 2.0
 */
private Point computeWindowLocation(IFigure tip, int eventX, int eventY){
	org.eclipse.swt.graphics.Rectangle clientArea = control.getDisplay().getClientArea();
	Point preferredLocation = new Point(eventX,eventY + 26);
	
	Dimension tipSize = getLightweightSystem().getRootFigure().getPreferredSize();
	tip.setSize(tipSize.width,tipSize.height);
	
	// Adjust location if tip is going to fall outside display
	if( preferredLocation.y + tipSize.height > clientArea.height )  
		preferredLocation.y = eventY - tipSize.height;
	
	if( preferredLocation.x + tip.getSize().width > clientArea.width )
		preferredLocation.x -= (preferredLocation.x + tipSize.width) - clientArea.width;
	
	return preferredLocation; 
}

/**
 * Sets the LightWeightSystem object's contents  
 * to the passed tooltip, and displays the tip.
 * The tip will be displayed only if the tip 
 * source is different than the previously viewed
 * tip source. I.E. The cursor has moved off of the 
 * previous tooltip source Figure. 
 *
 * Tooltip will  be painted directly below the cursor if possible,
 * otherwise it will be painted directly above cursor.
 *
 * @param hoverSource The Figure overwhich the hover event was fired. 
 * @param tip  The tool tip to be displayed.
 * @param eventX X coordinate of the hover event
 * @param eventY Y coordinate of the hover event
 * @since 2.0
 */
public void displayToolTipNear(IFigure hoverSource, IFigure tip, int eventX, int eventY){
	if(tip != null && hoverSource != currentTipSource){
		getLightweightSystem().setContents(tip);
		Point displayPoint = computeWindowLocation(tip, eventX, eventY);
		Dimension tipSize = tip.getSize();
		setShellBounds(displayPoint.x, displayPoint.y, tipSize.width, tipSize.height);
		show();
		currentTipSource = hoverSource;
		timer = new UITimer();
		timer.scheduleRepeatedly(new Runnable(){
			public void run(){
				hide();
				timer.cancel();
			}
		}, 5000, 5000);
	}
}

/* Dispose of the tooltip's shell and kill the timer */
public void dispose(){
	if(isShowing()){
		timer.cancel();
		hide();
	}	
	getShell().dispose();
}

protected void hookShellListeners(){
	
	// Close the tooltip window if the mouse enters the tooltip
	getShell().addMouseTrackListener(new MouseTrackAdapter(){
		public void mouseEnter(org.eclipse.swt.events.MouseEvent e){
				hide();
				currentTipSource = null;
				timer.cancel();
		}
	});
}

/**
 * Displays the hoverSource's tooltip if a tooltip of another  
 * source is currently being displayed. 
 *
 * @param figureUnderMouse The Figure overwhich the cursor was when called 
 * @param tip  The tool tip to be displayed.
 * @param eventX X coordinate of the cursor
 * @param eventY Y coordinate of the cursor
 * @since 2.0
 */
public void updateToolTip(IFigure figureUnderMouse, IFigure tip, int eventX, int eventY){
	/* If the cursor is not on any Figures, it has been moved
	   off of the control. Hide the tool tip. */
	if(figureUnderMouse == null){ 
		if(isShowing()){
			hide();
			timer.cancel();
		}
	}
	/* Makes tooltip appear without a hover event if a tip
	   is currently being displayed. */	
	if(isShowing() && figureUnderMouse != currentTipSource){ 
		hide();
		timer.cancel();
		displayToolTipNear(figureUnderMouse, tip, eventX, eventY);
	}
	/* If the tooltip is not being displayed, there is no current tip source */
	else if(!isShowing() && figureUnderMouse != currentTipSource)		
		currentTipSource = null;			
}

}