package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.*;

/**
 * Repositions a {@link Figure Figure} attached to a 
 * {@link Connection Connection} when the Connection is moved. 
 * Provides for alignment at the start, middle, or end of the
 * Connection.
 */
public class ConnectionLocator
	extends AbstractLocator {

public static final int START      = 2;
public static final int END        = 3;
/**
 * @deprecated Use MIDDLE instead, since the location is not
 * the midpoint of a line-segment, but the middle of a polyline.
 */
public static final int MIDPOINT   = 4;
public static final int MIDDLE     = 4;
private Connection connection;
private int alignment;

/**
 * Constructs a ConnectionLocator with the passed connection
 * and MIDDLE alignment.
 * 
 * @since 2.0
 */
public ConnectionLocator(Connection connection) {
	this(connection, MIDDLE);
}

/**
 * Constructs a ConnectionLocator with the passed Connection
 * and alignment.
 * 
 * @param connection Connection that ConnectionLocator is
 *                    associated with.
 * @param align Alignment of ConnectionLocator. 
 *               Valid values are integer constants START, MIDDLE, or END
 * @since 2.0
 */
public ConnectionLocator(Connection connection, int align) {
	setConnection(connection);
	setAlignment(align);
}

/**
 * Returns alignment of ConnectionLocator.
 * 
 * @since 2.0
 */
public int getAlignment() {
	return alignment;
}

/**
 * Returns connection associated with ConnectionLocator.
 * 
 * @since 2.0
 */
protected Connection getConnection(){
	return connection;
}

/**
 * Returns ConnectionLocator's reference point.
 * 
 * @since 2.0
 */
protected Point getReferencePoint() {
	return getLocation(getConnection().getPoints());
}

/**
 * Returns a point from the passed PointList
 * dependent on ConnectionLocator's alignment.
 * If START, returns first point in points
 * If END, returns last point in points
 * If MIDDLE, returns middle of line represented by points.
 * 
 * @since 2.0
 */
protected Point getLocation(PointList points) {
	switch (getAlignment()) {
		case START:
			return points.getPoint(Point.SINGLETON, 0);
		case END:
			return points.getPoint(Point.SINGLETON, points.size()-1);
		case MIDPOINT: {
			if (points.size()%2 == 0) {
				int i = points.size()/2;
				int j = i - 1;
				Point p1 = points.getPoint(j);
				Point p2 = points.getPoint(i);
				Dimension d = p2.getDifference(p1);
				return Point.SINGLETON.setLocation(p1.x + d.width/2, p1.y + d.height/2);
			}
			int i = (points.size()-1)/2;
			return points.getPoint(Point.SINGLETON, i);
		}
		default:
			return new Point();
	}
}

/**
 * Sets alignment to passed value.
 * 
 * @since 2.0
 */
protected void setAlignment(int align) {
	alignment = align;
}

/**
 * Sets Connection associated with ConnectionLocator to 
 * passed Connection.
 * 
 * @since 2.0
 */
protected void setConnection(Connection connection){
	this.connection = connection;
}

}