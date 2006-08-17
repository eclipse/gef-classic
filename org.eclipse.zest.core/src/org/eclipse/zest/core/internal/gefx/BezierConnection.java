/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
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
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;



/**
 * A connection that uses Bezier curves.
 * 
 * Bezier curves are defined by a set of four points: two point in the layout
 * (start and end), and two related control points (also start and end). The
 * control points are defined relative to their corresponding layout point.
 * This definition includes an angle between the layout point and the line
 * between the two layout points, as well as a ratio distance from the corresponding
 * layout point. The ratio distance is defined as a fraction between 0 and 1
 * of the distance between the two layout points. Using this definition
 * allows bezier curves to have a consistant look regardless of the actual
 * positions of the nodes in the layouts. 
 * @author Del Myers
 *
 */
//@tag bug(152530-Bezier(fix))
public class BezierConnection extends Bezier implements Connection, AnchorListener {

	private double startAngle;
	private double startLength;
	private double endAngle;
	private double endLength;
	private RotatableDecoration startArrow, endArrow;


	/**
	 * @param startAngle the angle from the line that the first control point is on.
	 * @param startLength the distance from the start point that the first control point is  as a percentage of the distance between the endpoints.
	 * @param endAngle the angle from the line that the second control point is on.
	 * @param endLength the distance from the endpoint that the last control point is as a percentage of the distance between the endpoints.
	 */
	public BezierConnection(double startAngle, double startLength, double endAngle, double endLength) {
		super(new Point(0,0), new Point(0,0), new Point(0,0), new Point(0,0));
		this.startAngle = startAngle;
		this.startLength = startLength;
		this.endAngle = endAngle;
		this.endLength = endLength;
		setConnectionRouter(null);
		setLayoutManager(new DelegatingLayout());
		resetControls();
	}

	private static ConnectionRouter NullConnectionRouter = new NullConnectionRouter();
	
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

	private ConnectionRouter connectionRouter;
	private ConnectionAnchor source;
	private ConnectionAnchor dest;
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Connection#getConnectionRouter()
	 */
	public ConnectionRouter getConnectionRouter() {
		return connectionRouter;
	}

	private void unhookSourceAnchor() {
		if (getSourceAnchor() != null)
			getSourceAnchor().removeAnchorListener(this);
	}

	private void unhookTargetAnchor() {
		if (getTargetAnchor() != null)
			getTargetAnchor().removeAnchorListener(this);
	}
	
	private void hookSourceAnchor() {
		if (getSourceAnchor() != null)
			getSourceAnchor().addAnchorListener(this);
	}

	private void hookTargetAnchor() {
		if (getTargetAnchor() != null)
			getTargetAnchor().addAnchorListener(this);
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
		if (startArrow != null)
			add(startArrow, new ArrowLocator(this, ConnectionLocator.SOURCE));
	}
	
	/**
	 * @return the source decoration (may be null)
	 */
	protected RotatableDecoration getSourceDecoration() {
		return startArrow;
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
		if (endArrow != null)
			add(endArrow, new ArrowLocator(this, ConnectionLocator.TARGET));
	}
	
	/**
	 * @return the target decoration (may be null)
	 * 
	 * @since 2.0
	 */
	protected RotatableDecoration getTargetDecoration() {
		return endArrow;
	}

	

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.AnchorListener#anchorMoved(org.eclipse.draw2d.ConnectionAnchor)
	 */
	public void anchorMoved(ConnectionAnchor anchor) {
		revalidate();
	}
	
	/**
	 * Returns the list of points in this arc. Note: the points can't be changed using
	 * setPoints, nor by changing the PointList. The PointList is fixed within this connection.
	 */
	public final PointList getPoints() {
		return super.getPoints().getCopy();
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
		if (router == null) router = NullConnectionRouter;
		if (this.connectionRouter == router) return;
		ConnectionRouter oldRouter = connectionRouter;
		this.connectionRouter = router;
		firePropertyChange(Connection.PROPERTY_CONNECTION_ROUTER, oldRouter, this.connectionRouter);
	}
	
	public void revalidate() {
		super.revalidate();
		if (connectionRouter != null)
			connectionRouter.invalidate(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Polyline#setPoints(org.eclipse.draw2d.geometry.PointList)
	 */
	public void setPoints(PointList points) {
		setStart(points.getFirstPoint());
		setEnd(points.getLastPoint());
		resetControls();
		repaint();
	}

	/**
	 * @param firstPoint
	 * @param lastPoint
	 */
	private void resetControls() {
		Point firstPoint = getStart();
		Point lastPoint = getEnd();
		Point startControl;
		Point endControl;
		double distance = Math.sqrt((lastPoint.x-firstPoint.x)*(lastPoint.x-firstPoint.x)+(firstPoint.y-lastPoint.y)*(firstPoint.y-lastPoint.y));
		double m = (firstPoint.y - lastPoint.y)/(double)(lastPoint.x - firstPoint.x);
		double theta = Math.atan(m);
		if (firstPoint.x > lastPoint.x) {theta = theta + Math.PI;}
		double sar = (Math.toRadians(getStartAngle()) + theta);
		double ear = (theta-Math.toRadians(getEndAngle()));
		
		double length = distance*getStartLength();
		double controlX = length*Math.cos(sar);
		double controlY = length*Math.sin(sar);
		startControl = new Point(firstPoint.x + Math.round(controlX), firstPoint.y - Math.round(controlY));
		
		length = distance*getEndLength();
		controlX =length*Math.cos(ear);
		controlY = length*Math.sin(ear);
		endControl = new Point(lastPoint.x - Math.round(controlX), lastPoint.y + Math.round(controlY));
		
		setStartControl(startControl);
		setEndControl(endControl);
		
	}
	
	/**
	 * @return the startLength
	 */
	public double getStartLength() {
		return startLength;
	}
	
	/**
	 * @return the endLength
	 */
	public double getEndLength() {
		return endLength;
	}
	
	/**
	 * @param startLength the startLength to set
	 */
	public void setStartLength(double startLength) {
		this.startLength = startLength;
	}
	
	/**
	 * @param endLength the endLength to set
	 */
	public void setEndLength(double endLength) {
		this.endLength = endLength;
	}
	
	/**
	 * @return the startAngle
	 */
	public double getStartAngle() {
		return startAngle;
	}
	
	/**
	 * @return the endAngle
	 */
	public double getEndAngle() {
		return endAngle;
	}
	
	/**
	 * @param startAngle the startAngle to set
	 */
	public void setStartAngle(int startAngle) {
		this.startAngle = startAngle;
		resetControls();
	}
	
	/**
	 * @param endAngle the endAngle to set
	 */
	public void setEndAngle(int endAngle) {
		this.endAngle = endAngle;
		resetControls();
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

}
