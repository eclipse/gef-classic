package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
import java.awt.geom.*;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

/**
 * Used to place IFigures along the endpoint or starting point 
 * of a {@link Connection Connection}. 
 * uDistance represents the distance from the Connection's owner to the 
 * IFigure. vDistance represents the distance from the IFigure 
 * to the Connection itself.
 */
public class ConnectionEndpointLocator 
	implements Locator
{
private boolean end;
private Connection conn;
private int uDistance;
private int vDistance;
private static Rectangle figureBounds;
protected Transposer transposer; {
	transposer = new Transposer();
}

/** 
 * Constructs an ConnectionEndpointLocator
 *
 * @param c      Connection used to calculate location.
 * @param isEnd  If true, location is based from the Connection's endpoint.
 *                If false, location is based from the connection's starting point.
 * @since 2.0
 */
public ConnectionEndpointLocator(Connection c, boolean isEnd) {
	end = isEnd;
	conn = c;
	uDistance = 14;
	vDistance = 4;
	figureBounds = new Rectangle();
}

/**
 * Returns an integer representing the side of the passed Rectangle
 * that a point lies on. 
 * 1 == Top
 * 2 == Right
 * 3 == Bottom
 * 4 == Left
 *
 * @param loc The point that is to be located
 * @param bounds The rectangle on which the point lies
 * @since 2.0
 */
private int calculateConnectionLocation(Point loc, Point topLeft, Point center){
	double m1,m2=0;	
	m1 = (double)(topLeft.y - center.y) /
		 (double)(topLeft.x - center.x);
	
	if(loc.x - center.x != 0)	
			m2 = (double)(loc.y - center.y) /
				 (double)(loc.x - center.x);
	
	// Case where m2 is vertical.
	if(loc.x == center.x){
		if(loc.y < center.y)
			return 3;		
		else
			return 1;
	}
	// Connection start point along left or right side
	else if(Math.abs(m2) <= Math.abs(m1)){			
		if(loc.x < center.x) 
			return 4;
		else
			return 2;
	}
	// Connection start point along top or bottom
	else{				
		if(loc.y < center.y)
			return 3;
		else
			return 1;
	}
}

/**
 * This method is used to calculate the "quadrant" value of a 
 * connection that does not have an owner on its starting point. 
 * 
 * 1 == Top
 * 2 == Right
 * 3 == Bottom
 * 4 == Left
 *
 * @param startPoint The starting point of the connection.
 * @param endPoint The end point of the connection.
 * @since 2.0
 */
private int calculateConnectionLocation(Point startPoint, Point endPoint){
	if( Math.abs(endPoint.x - startPoint.x) > Math.abs(endPoint.x - startPoint.x) ){
		if(endPoint.x > startPoint.x)
			return 2;
		else
			return 4;
	}			
	else {
		if(endPoint.y > startPoint.y)
			return 1;
		else
			return 3;
	}				
}	

/**
 * Calculates 'tan' which is used as a factor for y adjustment
 * when placing the connection label. 'tan' is capped at 1.0 in
 * the positive direction and -1.0 in the negative direction.
 * 
 * @param startPoint The starting point of the connection.
 * @param endPoint The end point of the connection.
 * @since 2.0
 */
private double calculateTan(Point startPoint, Point endPoint){
	double tan = 0;
	if(endPoint.x == startPoint.x)
		tan = 1.0;
	else
		tan = (double)(endPoint.y - startPoint.y) / 
			  (double)(endPoint.x - startPoint.x);	      		      
	if(tan > 1)
		tan = 1.0;
	else if(tan < -1)
		tan = -1.0;
	
	return tan;
}

private int calculateYShift(int figureWidth, int figureHeight){
	int yShift = 0;
	if( vDistance < 0)
		yShift = -figureHeight;
	else if( vDistance == 0 )
		yShift = -figureHeight/2;
	return yShift;
}

private Connection getConnection(){
	return conn;
}

private IFigure getConnectionOwner(){
	IFigure connOwner;
	if(isEnd())
		connOwner = conn.getTargetAnchor().getOwner();
	else
		connOwner = conn.getSourceAnchor().getOwner();
		
	return connOwner;	
}

private int getUDistance(){
	return uDistance;
}

private int getVDistance(){
	return vDistance;
}

private boolean isEnd(){
	return end;
}

public void relocate(IFigure figure){
	Connection conn = getConnection();
	Point startPoint = Point.SINGLETON;
	Point endPoint = new Point();
	
	int startPointPosition = 0;
	int endPointPosition = 1;
	if(isEnd()){
		startPointPosition = conn.getPoints().size() - 1;
		endPointPosition = startPointPosition - 1;
	}
	
	conn.getPoints().getPoint(startPoint,startPointPosition);
	conn.getPoints().getPoint(endPoint,endPointPosition);
	
	IFigure connOwner = getConnectionOwner();
	
	int quadrant;
	if(connOwner != null){
		Rectangle connOwnerBounds = connOwner.getBounds();
		Point connOwnerCenter = connOwnerBounds.getCenter();
		Point connOwnerTL = connOwnerBounds.getTopLeft();
		quadrant = calculateConnectionLocation(startPoint,connOwnerTL,connOwnerCenter);
	}
	else
		quadrant = calculateConnectionLocation(startPoint,endPoint);

	int cos = 1;
	transposer.setEnabled(false);
	
	/*
	 * Label placement calculations are done as if the
	 * connection point is along the left or right side of the 
	 * figure. If the connection point is along the top or bottom,
	 * values are transposed.
	 */
	if(quadrant == 1 || quadrant == 3)
		transposer.setEnabled(true);
	
	if(quadrant == 3 || quadrant == 4)
		cos = -1;		

	Dimension figureSize = transposer.t(figure.getPreferredSize());
	startPoint = transposer.t(startPoint);
	endPoint = transposer.t(endPoint);

	double tan = calculateTan(startPoint,endPoint);

	int figureWidth = figureSize.width;
	int figureHeight = figureSize.height;
	int yShift = calculateYShift(figureWidth,figureHeight);
	
	Point figurePoint = 
		new Point((int)(startPoint.x + (uDistance*cos) + figureWidth*((cos-1)/2)),
				   (int)(startPoint.y + cos*uDistance*tan + vDistance + yShift));

	figureBounds.setSize(transposer.t(figureSize));
	figureBounds.setLocation(transposer.t(figurePoint));
	figure.setBounds(figureBounds);
}

/**
 * Sets the distance in pixels from the Connection's owner.
 * 
 * @param distance Number of pixels to place the 
 *                  ConnectionEndpointLocator from its owner.
 * @since 2.0
 */
public void setUDistance(int distance){
	uDistance = distance;
}

/**
 * Sets the distance in pixels from the Connection.
 * 
 * @param distance Number of pixels to place the 
 *                  ConnectionEndpointLocator from its Connection.
 * @since 2.0
 */
public void setVDistance(int distance){
	vDistance = distance;
}

}