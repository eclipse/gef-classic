package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;
import org.eclipse.draw2d.geometry.*;

/**
 * Connection based on polyline. The layout of the connection
 * is handled by routers.
 */
public class PolylineConnection
	extends Polyline
	implements Connection, AnchorListener
{

private ConnectionAnchor startAnchor, endAnchor;
private ConnectionRouter connectionRouter = ConnectionRouter.NULL;
private RotatableDecoration startArrow, endArrow;

{
	setLayoutManager(new DelegatingLayout());
	addPoint(new Point(0,0));
	addPoint(new Point(100,100));
}

public void addNotify(){
	super.addNotify();
	hookSourceAnchor();
	hookTargetAnchor();
}

/*
 * Called by the anchors of this connection when they have moved,
 * revalidating this polyline connection.
 */
public void anchorMoved(ConnectionAnchor anchor){
	revalidate();
}

/*
 * Returns the bounds which holds all the points in this
 * polyline connection. Returns any previously existing
 * bounds, else calculates by unioning all the children's
 * dimensions.
 */
public Rectangle getBounds(){
	if (bounds == null){
		super.getBounds();
		for(int i=0; i<getChildren().size(); i++) {
			IFigure child = (IFigure)getChildren().get(i);
			bounds.union(child.getBounds());
		}
	}
	return bounds;
}

/*
 * Returns the <code>ConnectionRouter</code> used to layout this connection.
 * Will not return <code>null</code>.
 */
public ConnectionRouter getConnectionRouter(){
	return connectionRouter;
}

/**
 * @deprecated
 */
public ConnectionAnchor getEndAnchor(){
	return getTargetAnchor();
}

/**
 * @deprecated Use getTargetDecoration().
 */
protected RotatableDecoration getEndDecoration(){
	return getTargetDecoration();
}

/*
 * Returns the list of points being used by this PolylineConnection <b>by reference</b>.
 */
public PointList getPoints(){
	return super.getPoints();
}

public Object getRoutingConstraint() {
	if (getConnectionRouter() != null)
		return (List)getConnectionRouter().getConstraint(this);
	else
		return null;
}

public ConnectionAnchor getSourceAnchor(){
	return startAnchor;
}

protected RotatableDecoration getSourceDecoration(){
	return startArrow;
}

/**
 * @deprecated
 */
public ConnectionAnchor getStartAnchor(){
	return getSourceAnchor();
}

/**
 * @deprecated
 */
 protected RotatableDecoration getStartDecoration() {
	return getSourceDecoration();
}

/*
 * Returns the anchor at the end of this polyline connection.
 */
public ConnectionAnchor getTargetAnchor() {
	return endAnchor;
}

/**
 * Returns the RotatableDecoration at the target end of the 
 * {@link Connection}.
 * 
 * @since 2.0
 */
protected RotatableDecoration getTargetDecoration(){
	return endArrow;
}

private void hookSourceAnchor(){
	if (getSourceAnchor() != null)
		getSourceAnchor().addAnchorListener(this);
}

private void hookTargetAnchor(){
	if (getTargetAnchor() != null)
		getTargetAnchor().addAnchorListener(this);
}

/*
 * Layouts this polyline. If the start and end anchors are present,
 * the connection router is used to route this, after which it is
 * laid out. It also fires a moved method.
 */
public void layout() {
	if (getSourceAnchor() != null && getTargetAnchor() != null) {
		getConnectionRouter().route(this);
	}//$RANDY:ERASE
	super.layout();
	bounds = null;
	repaint();
	fireMoved();
}

/**
 * Called just before the receiver is being removed from its parent.
 * Results in removing itself from the connection router.
 * 
 * @since 2.0
 */
public void removeNotify() {
	unhookSourceAnchor();
	unhookTargetAnchor();
	getConnectionRouter().remove(this);
	super.removeNotify();
}

/*
 * Sets the connection router which handles the layout of
 * this polyline. Generally set by the parent handling the
 * polyline connection.
 */
public void setConnectionRouter(ConnectionRouter cr) {
	if (cr == null)
		cr = ConnectionRouter.NULL;
	if (connectionRouter != cr){
		connectionRouter.remove(this);
		Object old = connectionRouter;
		connectionRouter = cr;
		revalidate();
		firePropertyChange(Connection.PROPERTY_CONNECTION_ROUTER, old, cr);
	}
}

/**
 * @deprecated
 */
 public void setEndAnchor(ConnectionAnchor anchor){
	setTargetAnchor(anchor);
}

/**
 * @deprecated
 */
 public void setEndDecoration(RotatableDecoration dec){
	setTargetDecoration(dec);
}

public void setRoutingConstraint(Object cons) {
	if (getConnectionRouter() != null)
		getConnectionRouter().setConstraint(this, cons);
	revalidate();
}

/**
 * @deprecated
 */
 public void setStartDecoration(RotatableDecoration dec){
	setSourceDecoration(dec);
}

/*
 * Sets the anchor to be used at the start of this polyline
 * connection.
 */
public void setSourceAnchor(ConnectionAnchor anchor){
	unhookSourceAnchor();
	getConnectionRouter().invalidate(this);
	startAnchor = anchor;
	hookSourceAnchor();
	revalidate(); 
}

/**
 * Sets the decoration to be used at the start of the 
 * {@link Connection}.
 * 
 * @since 2.0
 */
public void setSourceDecoration(RotatableDecoration dec){
	if (getSourceDecoration() != null)
		remove(getSourceDecoration());
	startArrow = dec;
	if (dec != null)
		add(dec, new ArrowLocator(this, ConnectionLocator.START));
}

/**
 * @deprecated
 */
 public void setStartAnchor(ConnectionAnchor anchor){
	setSourceAnchor(anchor);
}

/*
 * Sets the anchor to be used at the end of the polyline connection.
 * Removes this listener from the old anchor and adds it to the new
 * anchor.
 */
public void setTargetAnchor(ConnectionAnchor anchor){
	unhookTargetAnchor();
	getConnectionRouter().invalidate(this);
	endAnchor = anchor;
	hookTargetAnchor();
	revalidate();
}

/**
 * Sets the decoration to be used at the end of the 
 * {@link Connection}.
 */
public void setTargetDecoration(RotatableDecoration dec){
	if (getTargetDecoration() != null)
		remove(getTargetDecoration());
	endArrow = dec;
	if (dec != null)
		add(dec, new ArrowLocator(this, ConnectionLocator.END));
}

private void unhookSourceAnchor(){
	if (getSourceAnchor() != null)
		getSourceAnchor().removeAnchorListener(this);
}

private void unhookTargetAnchor(){
	if (getTargetAnchor() != null)
		getTargetAnchor().removeAnchorListener(this);
}

}