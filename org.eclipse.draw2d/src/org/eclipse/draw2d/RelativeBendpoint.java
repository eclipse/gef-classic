package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * A Bendpoint that calculates its location based on its
 * distance from the start and end points of the {@link Connection}, 
 * as well as its weight. 
 */
public class RelativeBendpoint
	implements Bendpoint
{

private Connection connection;
private float weight = 0.5f;
private Dimension d1, d2;

/**
 * Creates a new RelativeBendpoint.
 * 
 * @since 2.0
 */
public RelativeBendpoint() {}

/**
 * Creates a new RelativeBendpoint and associates it with
 * the given Connection.
 * 
 * @since 2.0
 */
public RelativeBendpoint(Connection conn) {
	setConnection(conn);
}

/**
 * Returns the Connection this bendpoint is associated with.
 * 
 * @since 2.0
 */
protected Connection getConnection() {
	return connection;
}

/**
 * Calculates and returns this bendpoint's new location.
 * 
 * @since 2.0
 */
public Point getLocation() {
	Point a1 = getConnection().getSourceAnchor().getReferencePoint();
	Point a2 = getConnection().getTargetAnchor().getReferencePoint();
	Point p = new Point();
	p.x = (int)((a1.x + d1.width )*(1f-weight) + weight*(a2.x+d2.width));
	p.y = (int)((a1.y + d1.height)*(1f-weight) + weight*(a2.y+d2.height));
	getConnection().translateToRelative(p);
	return p;
}

/**
 * Sets the Connection this bendpoint should be associated with.
 * 
 * @since 2.0
 */
public void setConnection(Connection conn) {
	connection = conn;
}

/**
 * Sets the dimensions representing the x and y distances this
 * bendpoint is from the start and end points of the connection.
 * These dimensions are generally set once and are used in 
 * calculating the bendpoint's location.
 * 
 * @since 2.0
 */
public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
	d1 = dim1;
	d2 = dim2;
}

/**
 * Sets the weight this bendpoint should use to calculate its
 * location.  The weight should be between 0.0 and 1.0.  A weight
 * of 0.0 will cause the bendpoint to follow the start point, while 
 * a weight of 1.0 will cause the bendpoint to follow the end point.
 * A weight of 0.5 (the default) will cause the bendpoint to 
 * maintain its original aspect ratio between the start and end
 * points.
 * 
 * @since 2.0
 */
public void setWeight(float w) {
	weight = w;
}

}