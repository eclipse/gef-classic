package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.*;

/**
 * Draws a polygon which encloses all of its {@link Point Points}.
 */
public class Polygon
	extends Polyline
{

/**
 * Fill the Polygon with the background color
 * set by <i>g</i>.
 * 
 * @since 2.0
 */
protected void fillShape(Graphics g){
	g.fillPolygon(getPoints());
}

/**
 * Draw the outline of the Polygon.
 * 
 * @since 2.0
 */
protected void outlineShape(Graphics g){
	g.drawPolygon(getPoints());
}

}