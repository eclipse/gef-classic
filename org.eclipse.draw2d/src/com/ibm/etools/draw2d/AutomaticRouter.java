package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ArrayList;
import com.ibm.etools.draw2d.geometry.*;
import com.ibm.etools.draw2d.util.*;

/**
 * The AutomaticRouter provides the facility to prevent 
 * {@link Connection Connections} from overlapping. Subclasses
 * implement the abstract method handleCollision(PointList list, int index)
 * to determine how to handle Connection collisions.
 * 
 * Also provides access to its 'next' router so that manual routing in
 * subclasses is possible.
 */
abstract public class AutomaticRouter
	extends AbstractRouter 
{

private ConnectionRouter nextRouter;
private MultiValueMap connections = new MultiValueMap();

private class HashKey {
    
	private ConnectionAnchor anchor1, anchor2;
	
	HashKey(Connection conn) {
		anchor1 = conn.getSourceAnchor();
		anchor2 = conn.getTargetAnchor();
	}
	
	public boolean equals(Object object) {
		boolean isEqual = false;
		HashKey hashKey;
		
		if(object instanceof HashKey) {
			hashKey = (HashKey)object;
			ConnectionAnchor hkA1 = hashKey.getFirstAnchor();
			ConnectionAnchor hkA2 = hashKey.getSecondAnchor();
			
			isEqual = 
				(hkA1.equals(anchor1) && hkA2.equals(anchor2)) ||
				(hkA1.equals(anchor2) && hkA2.equals(anchor1));
		}
		return isEqual;
	}
			
	public ConnectionAnchor getFirstAnchor() {
		return anchor1;
	}
	
	public ConnectionAnchor getSecondAnchor() {
		return anchor2;	
	}
	
	public int hashCode() {
		return anchor1.hashCode() ^ anchor2.hashCode();
	}
}

public Object getConstraint(Connection connection){
	if (next() != null)
		return next().getConstraint(connection);
	return null;
}

abstract protected void handleCollision(PointList list, int index);

public void invalidate(Connection conn) {
	if (conn.getSourceAnchor() == null || conn.getTargetAnchor() == null)
		return;
	HashKey connectionKey = new HashKey(conn);
	ArrayList connectionList = connections.get(connectionKey);
	if (connectionList != null) {
		int index = connectionList.indexOf(conn);
		if (index == -1)
			return;
		connections.remove(connectionKey, conn);
		for(int i = index; i < connectionList.size(); i++)
			((Connection)connectionList.get(i)).revalidate();
	}
}

/**
 * Returns the next router.
 * 
 * @since 2.0
 */
protected ConnectionRouter next() {
	return nextRouter;
}

public void remove(Connection conn) {
	if (conn.getSourceAnchor() == null || conn.getTargetAnchor() == null)
		return;
	HashKey connectionKey = new HashKey(conn);
	ArrayList connectionList = connections.get(connectionKey);
	if (connectionList != null) {
		int index = connectionList.indexOf(conn);
		connections.remove(connectionKey, conn);
		for(int i = index + 1; i < connectionList.size(); i++)
			((Connection)connectionList.get(i)).revalidate();
	}
	if (next() != null) 
		next().remove(conn);
}

public void route(Connection conn) {
	conn.getPoints().removeAllPoints();
	if (next() != null) 
		next().route(conn);
	else
		setEndPoints(conn);

	if (conn.getPoints().size() == 2) {
		PointList points = conn.getPoints();
		HashKey connectionKey = new HashKey(conn);
		ArrayList connectionList = connections.get(connectionKey);
		
		if(connectionList != null) {
			
			int index;
			
			if (connectionList.contains(conn)) {
				index = connectionList.indexOf(conn) + 1;	
			}
			else {
				index = connectionList.size() + 1;
				connections.put(connectionKey, conn);
			}
			
			handleCollision(points, index);
			conn.setPoints(points);
		}
		else {
			connections.put(connectionKey, conn);
		}
	}
}

public void setConstraint(Connection connection, Object constraint) {
	invalidate(connection);
	if (next() != null)
		next().setConstraint(connection, constraint);
}

protected void setEndPoints(Connection conn) {
	PointList points = new PointList();
	points.addPoint(getStartPoint(conn));
	points.addPoint(getEndPoint(conn));
	conn.setPoints(points);
}

/**
 * Sets the next router to the passed value.
 * 
 * @since 2.0
 */
public void setNextRouter(ConnectionRouter router) {
	nextRouter = router;
}

}