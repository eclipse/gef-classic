/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.gefx;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.AnchorListener;
import org.eclipse.draw2d.ArrowLocator;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * 
 * @author Del Myers
 * 
 * A connection between two anchor points. This connection is drawn as an arc,
 * defined as the circular arc with the chord (ax, ay) - (bx, by) (where a and b
 * are the anchors) and a depth d defined as the maximum distance from any point on
 * the chord (i.e. a vector normal to the chord with magnitude d).
 */
public class ArcConnection extends Arc implements Connection, AnchorListener {

	/**
	 * Routes Connections directly from the source anchor to the target anchor with no
	 * bendpoints in between.
	 */
	static class NullConnectionRouter
		extends AbstractRouter
	{
		
		/**
		 * Constructs a new NullConnectionRouter.
		 */
		NullConnectionRouter() { }

		/**
		 * Routes the given Connection directly between the source and target anchors.
		 * @param conn the connection to be routed
		 */
		public void route(Connection conn) {
			PointList points = conn.getPoints();
			points.removeAllPoints();
			Point p;
			p = getStartPoint(conn);
			conn.translateToRelative(p = getStartPoint(conn));
			points.addPoint(p);
			p = getEndPoint(conn);
			conn.translateToRelative(p = getEndPoint(conn));
			points.addPoint(p);
			conn.setPoints(points);
		}

	}
	
	/** The depth of the arc */
	private int depth;
	
	//just for reference purposes: a point on the radius of the arc, halfway
	//between the endpoints.
	private double px;
	private double py;
	//decorations for the start and end of the arc.
	private RotatableDecoration endArrow;
	private RotatableDecoration startArrow;
	
	//list of points in the arc. This will include just the end points.
	private PointList pointList;
	
	//method for routing connections.
	private ConnectionRouter connectionRouter;
	
	//source and destination anchors.
	private ConnectionAnchor source;
	private ConnectionAnchor dest;
	
	//default connection router.
	private static ConnectionRouter NullConnectionRouter = new NullConnectionRouter();
	
	
	public ArcConnection() {
		super();
		this.pointList = new PointList();
		connectionRouter = NullConnectionRouter;
		this.depth = 0;
		pointList.addPoint(new Point(0,0));
		pointList.addPoint(new Point(100,100));
		setLayoutManager(new DelegatingLayout());
	}
	
	public ConnectionRouter getConnectionRouter() {
		return connectionRouter;
	}
	
	/**
	 * Hooks the source and target anchors.
	 * @see Figure#addNotify()
	 */
	public void addNotify() {
		super.addNotify();
		hookSourceAnchor();
		hookTargetAnchor();
	}

	/**
	 * Returns the list of points in this arc. Note: the points can't be changed using
	 * setPoints, nor by changing the PointList. The PointList is fixed within this connection.
	 */
	public final PointList getPoints() {
		return pointList.getCopy();
	}

	public Object getRoutingConstraint() {
		if (this.connectionRouter != null)
			return connectionRouter.getConstraint(this);
		return null;
	}

	public ConnectionAnchor getSourceAnchor() {
		return source;
	}

	public ConnectionAnchor getTargetAnchor() {
		return dest;
	}

	public void setConnectionRouter(ConnectionRouter router) {
		if (this.connectionRouter == router) return;
		ConnectionRouter oldRouter = connectionRouter;
		if (router == null) router = NullConnectionRouter;
		this.connectionRouter = router;
		firePropertyChange(Connection.PROPERTY_CONNECTION_ROUTER, oldRouter, this.connectionRouter);
	}

	
	
	/**
	 * Updates the arc for when point or depth changes occur.
	 */
	protected void updateArc(Point src, Point dest, int depth) {
		double workingDepth = depth;
		this.depth = depth;
		//get cartesian coordinates: they are easier to work with.
		//cartesian coordinates are easier to use for translations.
		double x1 = src.x;
		double y1 = -src.y;
		double x2 = dest.x;
		double y2 = -dest.y;
		//	the center of the chord
		double cartChordX = (x2 + x1)/2;
		double cartChordY = (y2 + y1)/2;
		
		//special cases
		if (depth == 0) {
			doStraightLine(src, dest);
			return;
		} else if (src.equals(dest)) {
			doCircle(src);
			return;
		}
		
		
		if (x1 >= x2) {
			workingDepth = -workingDepth;
		}
		
		double chordLength = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		if (Math.abs(workingDepth) >= chordLength/2) {
			workingDepth = (chordLength/3)*(workingDepth/Math.abs(workingDepth));
		}
		double r = (((chordLength/2)*(chordLength/2)) + ((double)workingDepth*workingDepth))/((double)2*workingDepth);
		
		//assume that the chord is perpendicular to the origin
		//to find the angle of the chord.
		double cartCenterX = 0;
		double cartCenterY = 0;
		
		//Find a vector normal to the chord. This will be used for translating the
		//circle back to screen coordinates.
		double chordNormal = 0;

		if (Math.abs(x1 - x2) <= .001) {
			//slope of 0. NaN is easier to detect than 0.
			chordNormal = Double.NaN;
		} else if (Math.abs(y1-y2) <= 0.001) {
			//infinite slope.
			chordNormal = Double.POSITIVE_INFINITY;
		} else {
			chordNormal = -(double)(y2 - y1)/(x2 - x1);
		}
		
		double th1;
		if (Double.isNaN(chordNormal)) {
			cartCenterX = (y1 > y2) ? (cartChordX-r+(workingDepth)) : (cartChordX+r-(workingDepth));
			cartCenterY = cartChordY;
			this.px = cartChordX + ((y1 > y2) ? (workingDepth) : (-workingDepth));
			this.py = (-cartChordY);
			th1 = Math.PI/2;
		} else if (Double.isInfinite(chordNormal)) {
			cartCenterX = cartChordX;
			cartCenterY = cartChordY+r-(workingDepth);
			this.px = cartChordX;
			this.py = (-cartChordY)+workingDepth;
			th1 = 0.0;
		} else {
			//assume that the center of the chord is on the origin.
			th1 = Math.atan(chordNormal);
			cartCenterX = (r-(double)(workingDepth))*Math.sin(th1)+cartChordX;//cartChordX+r -depth;
			cartCenterY = (r-(double)(workingDepth))*Math.cos(th1)+cartChordY;//cartChordY+r-depth;
			this.px = (-workingDepth)*Math.sin(th1)+cartChordX;
			this.py = -((-workingDepth)*Math.cos(th1)+cartChordY);
		}
		
		//update the points and depth
		pointList.removeAllPoints();
		pointList.addPoint(src);
		pointList.addPoint(new Point(Math.round(px), Math.round(py)));
		pointList.addPoint(dest);
		
		//figure out the new angles
		//translate the points to the center of the circle
		double cartArcX1 = x1 - cartCenterX;
		double cartArcY1 = y1 - cartCenterY;
		double cartArcX2 = x2 - cartCenterX;
		double cartArcY2 = y2 - cartCenterY;
		
		double arcStart = angle360(cartArcX1, cartArcY1);
		double arcEnd = angle360(cartArcX2, cartArcY2);
		double  arcLength = findAngleDegrees(arcStart, arcEnd);
		int padding = (chordLength > 100) ? 4 : 0;
		if (depth < 0) padding = -padding; 
		setOffset((int)(Math.round(arcStart - padding)));
		setLength((int)(Math.round(arcLength+padding*(chordLength/100))));
		
		//update the bounds
		double br = Math.abs(r);
		double topx = cartCenterX - br;
		double topy = cartCenterY + br;
		double width = br*2;
			
		Rectangle b = new Rectangle((int)x1, -(int)y1, (int)(px-x1), (int)(py+y1));
		if (b.width < 0) {
			b.width = -b.width;
			b.x = b.x - b.width;
		}
		if (b.height < 0){
			b.height = -b.height;
			b.y = b.y-b.height;
		}
		Rectangle b2 = new Rectangle((int)x2, (int)-y2, (int)(px-x2), (int)(py+y2));
		if (b2.width < 0) {
			b2.width = -b2.width;
			b2.x = b2.x - b2.width;
		}
		if (b2.height < 0){
			b2.height = -b2.height;
			b2.y = b2.y-b2.height;
		}
		int b3x = Math.min(b2.x, b.x);
		int b3y = Math.min(b.y, b2.y);
		Rectangle b3 = new Rectangle(b3x-5, (b3y-5), b.width+b2.width+10, b.height+ b2.height+10);
		setBounds(b3);
		setArcBounds(new Rectangle((int)topx, -(int)topy, (int)width, (int)width));
		
		
		//this.bounds = null;
		
	}
	
	//source and destination are the same. create a circle that passes through the point.
	
	private void doCircle(Point src) {
		setOffset(0);
		setLength(360);
		Rectangle circleBounds = new Rectangle(src.x - depth/2, src.y - depth, depth, depth);
		setArcBounds(circleBounds);
		setBounds(circleBounds);
		pointList.removeAllPoints();
		pointList.addPoint(src.getCopy());
		pointList.addPoint(new Point(src.x, src.y-depth));
		pointList.addPoint(src.getCopy());
	}

	private void doStraightLine(Point src, Point dest) {
		int x1 = src.x;
		int y1 = src.y;
		int x2 = dest.x;
		int y2 = dest.y;
		//just a straight line.
		if (x1 > x2) {
			int t = x2;
			x2 = x1;
			x1 = t;
		}
		if (y1 > y2) {
			int t = y2;
			y2 = y1;
			y1 = t;
		}
		Rectangle bounds = new Rectangle((int)x1-2, (int)y1-2, (int)(x2-x1)+4, (int)(y2-y1)+4);
		setBounds(bounds);
		setArcBounds(bounds);
		
		pointList.removeAllPoints();
		pointList.addPoint(src.getCopy());
		pointList.addPoint(new Point((x1+x2)/2, (y1+y2)/2));
		pointList.addPoint(dest.getCopy());
		
	}

	/**
	 * This implementation only pays attention to the first and the last points in the
	 * list because Arcs don't allow for bendpoints. The new point list will consist of
	 * the two end-points and a point that describes the depth of the arc.
	 */
	public final void setPoints(PointList list) {
		Point src = list.getFirstPoint();
		Point dest = list.getLastPoint();
		//create the new arc.
		updateArc(src, dest, getDepth());
		
		firePropertyChange(Connection.PROPERTY_POINTS, null, pointList);
		repaint();
		
	}
	
	public int getDepth() {
		return depth;
	}
	
	/**
	 * Sets the depth of the arc. This is defined as the maximum distance from the
	 * line that connects the source and target of this connection. The sign of the
	 * depth (+ve or -ve) will define which side of the line the arc will appear on.
	 * @param depth the depth of the arc.
	 */
	public void setDepth(int depth) {
		updateArc(getPoints().getFirstPoint(), getPoints().getLastPoint(), depth);
		repaint();
	}
	
	/**
	 * Takes the polar coordinates x1, y1 and returns their angle between 0 and 360 degrees.
	 * @param x1 polar coordinate.
	 * @param y1 polar coordinate
	 * @return polar angle in 360 degrees.
	 */
	protected double angle360(double x1, double y1) {
		double theta = Math.toDegrees(Math.atan(y1/x1));
		switch (findQuadrant(x1,y1)) {
			case 1: return theta;
			case 2: 
			case 3: return theta+180; 
			case 4: return theta+360;
			default: return theta;
		}
	}
	
	
	/**
	 * 
	 * finds the shortest angle between the two given angles. Assumes that the angles
	 * are between 0-360 degrees.
	 *
	 */
	protected double findAngleDegrees(double a1, double a2) {
		double diff = a2-a1;
		double diff2 = diff-360;
		if (diff < 0) {
			diff2 = 360+diff;
		}
		if (Math.abs(diff) < Math.abs(diff2)) {
			return diff;
		}
		return diff2;
	}
	
	
	//find the quadrant, assume points are centered at 0,0
	protected int findQuadrant (double x, double y) {
		if (y > 0) {
			if (x > 0) {
				return 1;
			} else {
				return 2;
			}
		} else {
			if (x > 0) {
				return 4;
			} else {
				return 3;
			}
		}
	}

	
	public void setRoutingConstraint(Object cons) {
		if (this.connectionRouter != null) connectionRouter.setConstraint(this, cons);
	}

	public void setSourceAnchor(ConnectionAnchor anchor) {
		if (anchor == this.source) return;
		unhookSourceAnchor();
		this.source = anchor;
		if (getParent()!=null) hookSourceAnchor();
		revalidate();
	}

	public void setTargetAnchor(ConnectionAnchor anchor) {
		if (anchor == this.dest) return;
		unhookTargetAnchor();
		this.dest = anchor;
		if (getParent()!=null) hookTargetAnchor();
		revalidate();
	}
	
	private void unhookSourceAnchor() {
		if (getSourceAnchor() != null)
			getSourceAnchor().removeAnchorListener(this);
	}

	private void unhookTargetAnchor() {
		if (getTargetAnchor() != null)
			getTargetAnchor().removeAnchorListener(this);
	}
	
	public void revalidate() {
		super.revalidate();
		if (connectionRouter != null)
			connectionRouter.invalidate(this);
	}
	
	
	protected final void fillShape(Graphics graphics) {
		
	}
	
	protected void outlineShape(Graphics graphics) {
		Color c = graphics.getForegroundColor();
		graphics.setForegroundColor(c.getDevice().getSystemColor(SWT.COLOR_GRAY));
		//graphics.drawOval(getBounds());
		graphics.setForegroundColor(c);
		Point source =getPoints().getFirstPoint();
		Point target = getPoints().getLastPoint();
		Rectangle bounds = getBounds();
		if (depth == 0) {
			graphics.drawLine(source, target);
			return;
		}
		graphics.setClip(bounds);
		super.outlineShape(graphics);
	}
	
	/*public boolean containsPoint(int x, int y) {
		if (depth == 0) {
			//calculate for a straight line.
			Point source = getPoints().getFirstPoint();
			Point target = getPoints().getLastPoint();
			double m = ((double)target.y - (double)source.y)/((double)target.x-(double)source.x);
			double dy = m*(source.x-x) + y;
			return (Math.abs(dy) <= .01);
		}
		if (!super.containsPoint(x, y))
			return false;
		
		Rectangle r = getArcBounds();
		double ellw = r.width;
		double normx = (x - r.x) / ellw - 0.5;
		double ellh = r.height;
		double normy = (y - r.y) / ellh - 0.5;
		double distSq = (normx * normx + normy * normy);
		return (Math.abs(1-distSq) <= .001);
		
	}*/
	
	public boolean containsPoint(int x, int y) {
		return false;
	}

	private void hookSourceAnchor() {
		if (getSourceAnchor() != null)
			getSourceAnchor().addAnchorListener(this);
	}

	private void hookTargetAnchor() {
		if (getTargetAnchor() != null)
			getTargetAnchor().addAnchorListener(this);
	}
	

	public void anchorMoved(ConnectionAnchor anchor) {
		revalidate();
	}
	
	/**
	 * Layouts this polyline. If the start and end anchors are present, the connection router 
	 * is used to route this, after which it is laid out. It also fires a moved method.
	 */
	public void layout() {
		if (getSourceAnchor() != null && getTargetAnchor() != null)
			connectionRouter.route(this);
		Rectangle oldBounds = bounds;
		super.layout();
		
		if (!getBounds().contains(oldBounds)) {
			getParent().translateToParent(oldBounds);
			getUpdateManager().addDirtyRegion(getParent(), oldBounds);
		}
		
		repaint();
		fireFigureMoved();
	}
	
	/**
	 * Sets the decoration to be used at the end of the {@link Connection}.
	 * @param dec the new target decoration
	 */
	public void setTargetDecoration(RotatableDecoration dec) {
		if (endArrow == dec)
			return;
		if (endArrow != null)
			remove(endArrow);
		endArrow = dec;
		if (endArrow != null) {
			if (depth != 0)
				add(endArrow, new ArcLocator(this, ConnectionLocator.TARGET));
			else 
				add(endArrow, new ArrowLocator(this,ConnectionLocator.TARGET));
		}
	}
	
	/**
	 * Sets the decoration to be used at the start of the {@link Connection}.
	 * @param dec the new source decoration
	 * @since 2.0
	 */
	public void setSourceDecoration(RotatableDecoration dec) {
		if (startArrow == dec)
			return;
		if (startArrow != null)
			remove(startArrow);
		startArrow = dec;
		if (startArrow != null) {
			if (depth != 0)
				add(startArrow, new ArcLocator(this, ConnectionLocator.SOURCE));
			else 
				add(startArrow, new ArrowLocator(this,ConnectionLocator.SOURCE));
		}
	}

}
