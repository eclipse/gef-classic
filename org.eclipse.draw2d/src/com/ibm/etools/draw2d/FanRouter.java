package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.*;

/**
 * Automatic router that spreads its {@link Connection Connections}
 * in a fan-like fashion upon collision.
 */
public class FanRouter
	extends AutomaticRouter
{

private int separation = 10;

/**
 * Returns the colliding {@link Connection Connection's} 
 * separation in pixels.
 * 
 * @since 2.0 
 */
public int getSeparation() {
	return separation;	
}

protected void handleCollision(PointList points, int index){
	Point start = points.getFirstPoint();
	Point end = points.getLastPoint();
	
	Point midPoint = new Point((end.x + start.x) / 2, (end.y + start.y) / 2);
	Ray ray = new Ray(start, end);
	double length = ray.length();

	double xSeparation = separation * ray.x / length;
	double ySeparation = separation * ray.y / length;
	
	Point bendPoint;
		
	if (index%2 == 0) {
		bendPoint = new Point(
			midPoint.x + (index / 2) * (-1 * ySeparation),
			midPoint.y + (index / 2) * xSeparation);
	}
	else {
		bendPoint = new Point(
			midPoint.x + (index / 2) * ySeparation,
			midPoint.y + (index / 2) * (-1 * xSeparation));
	}
	if (!bendPoint.equals(midPoint))
		points.insertPoint(bendPoint, 1);
}

/**
 * Sets the colliding {@link Connection Connection's} 
 * separation in pixels.
 * 
 * @since 2.0 
 */
public void setSeparation(int value) {
	separation = value;
}

}