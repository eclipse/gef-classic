package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * A decorative Figure intended to be placed on a 
 * {@link Polyline}.
 * It has the default shape of right-pointing triangle.
 */
public class PolylineDecoration
	extends Polyline
	implements RotatableDecoration
{

public static final PointList
	TRIANGLE_TIP = new PointList();

static {
	TRIANGLE_TIP.addPoint(-1,1);
	TRIANGLE_TIP.addPoint(0,0);
	TRIANGLE_TIP.addPoint(-1,-1);
}

private Point location = new Point();
private PointList template = TRIANGLE_TIP;
private Transform transform = new Transform();

/**
 * Constructs a PolylineDecoration. 
 * Defaults the PolylineDecoration to fill its region with black.
 * 
 * @since 2.0 
 */
public PolylineDecoration(){
	setBackgroundColor(ColorConstants.black);
	setScale(7,3);
}

/**
 * Returns the points in the PolylineDecoration as a PointList.
 * 
 * @since 2.0
 */
public PointList getPoints(){
	if (points == null){
		points = new PointList();
		for (int i=0; i < template.size(); i++)
			points.addPoint(transform.getTransformed(template.getPoint(i)));
	}
	return points;
}

public void setLocation(Point p){
	points = null;
	bounds = null;
	location.setLocation(p);
	transform.setTranslation(p.x,p.y);
}

/**
 * Sets the PolylineDecorations point template to
 * the passed PointList. This template is an outline
 * of the PolylineDecoration's region. (The default value
 * is TRIANGLE_TIP which is a triangle whose tip is pointing 
 * to the right).
 * 
 * @param pl The PointList outline to use as the PolylineDecoration's
 *            region.
 * @since 2.0
 */
public void setTemplate(PointList pl){
	erase();
	template = pl;
	points = null;
	bounds = null;
	repaint();
}

/**
 * Sets the amount of scaling to be done along X and Y
 * axes on the PolylineDecoration's template.
 *
 * @param x X scaling
 * @param y Y scaling
 * @since 2.0
 */
public void setScale(double x, double y){
	points = null;
	bounds = null;
	transform.setScale(x,y);
}

public void setReferencePoint(Point ref){
	Point pt = Point.SINGLETON;
	pt.setLocation(ref);
	pt.negate().translate(location);
	setRotation(Math.atan2(pt.y, pt.x));
}

/**
 * Sets the angle by which rotation is to be done on the 
 * PolylineDecoration.
 * 
 * @param angle Angle of rotation.
 * @since 2.0
 */
public void setRotation(double angle){
	points = null;
	bounds = null;
	transform.setRotation(angle);
}

}