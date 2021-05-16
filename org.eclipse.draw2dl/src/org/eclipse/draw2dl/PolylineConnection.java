/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * An implementation of {@link org.eclipse.draw2dl.Connection} based on Polyline. PolylineConnection
 * adds the following additional features:
 * <UL>
 * <LI>
 * A {@link org.eclipse.draw2dl.ConnectionRouter} may be provided which will be used to determine
 * the connections points.
 * <LI>
 * Children may be added. The bounds calculation is extended such that the
 * bounds is the smallest Rectangle which is large enough to display the
 * Polyline and all of its children figures.
 * <LI>
 * A {@link org.eclipse.draw2dl.DelegatingLayout} is set as the default layout. A delegating layout
 * allows children to position themselves via {@link Locator Locators}.
 * </UL>
 * <P>
 */
public class PolylineConnection extends Polyline implements org.eclipse.draw2dl.Connection,
    AnchorListener {

	private org.eclipse.draw2dl.ConnectionAnchor startAnchor, endAnchor;
	private org.eclipse.draw2dl.ConnectionRouter connectionRouter = org.eclipse.draw2dl.ConnectionRouter.NULL;
	private org.eclipse.draw2dl.RotatableDecoration startArrow, endArrow;

	{
		setLayoutManager(new DelegatingLayout());
		addPoint(new Point(0, 0));
		addPoint(new Point(100, 100));
	}

	/**
	 * Hooks the source and target anchors.
	 * 
	 * @see Figure#addNotify()
	 */
	public void addNotify() {
		super.addNotify();
		hookSourceAnchor();
		hookTargetAnchor();
	}

	/**
	 * Appends the given routing listener to the list of listeners.
	 * 
	 * @param listener
	 *            the routing listener
	 * @since 3.2
	 */
	public void addRoutingListener(org.eclipse.draw2dl.RoutingListener listener) {
		if (connectionRouter instanceof RoutingNotifier) {
			RoutingNotifier notifier = (RoutingNotifier) connectionRouter;
			notifier.listeners.add(listener);
		} else
			connectionRouter = new RoutingNotifier(connectionRouter, listener);
	}

	/**
	 * Called by the anchors of this connection when they have moved,
	 * revalidating this polyline connection.
	 * 
	 * @param anchor
	 *            the anchor that moved
	 */
	public void anchorMoved(org.eclipse.draw2dl.ConnectionAnchor anchor) {
		revalidate();
	}

	/**
	 * Returns the bounds which holds all the points in this polyline
	 * connection. Returns any previously existing bounds, else calculates by
	 * unioning all the children's dimensions.
	 * 
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		if (bounds == null) {
			super.getBounds();
			for (int i = 0; i < getChildren().size(); i++) {
				org.eclipse.draw2dl.IFigure child = (org.eclipse.draw2dl.IFigure) getChildren().get(i);
				bounds.union(child.getBounds());
			}
		}
		return bounds;
	}

	/**
	 * Returns the <code>ConnectionRouter</code> used to layout this connection.
	 * Will not return <code>null</code>.
	 * 
	 * @return this connection's router
	 */
	public org.eclipse.draw2dl.ConnectionRouter getConnectionRouter() {
		if (connectionRouter instanceof RoutingNotifier)
			return ((RoutingNotifier) connectionRouter).realRouter;
		return connectionRouter;
	}

	/**
	 * Returns this connection's routing constraint from its connection router.
	 * May return <code>null</code>.
	 * 
	 * @return the connection's routing constraint
	 */
	public Object getRoutingConstraint() {
		if (getConnectionRouter() != null)
			return getConnectionRouter().getConstraint(this);
		else
			return null;
	}

	/**
	 * @return the anchor at the start of this polyline connection (may be null)
	 */
	public org.eclipse.draw2dl.ConnectionAnchor getSourceAnchor() {
		return startAnchor;
	}

	/**
	 * @return the source decoration (may be null)
	 */
	protected org.eclipse.draw2dl.RotatableDecoration getSourceDecoration() {
		return startArrow;
	}

	/**
	 * @return the anchor at the end of this polyline connection (may be null)
	 */
	public org.eclipse.draw2dl.ConnectionAnchor getTargetAnchor() {
		return endAnchor;
	}

	/**
	 * @return the target decoration (may be null)
	 * 
	 * @since 2.0
	 */
	protected org.eclipse.draw2dl.RotatableDecoration getTargetDecoration() {
		return endArrow;
	}

	private void hookSourceAnchor() {
		if (getSourceAnchor() != null)
			getSourceAnchor().addAnchorListener(this);
	}

	private void hookTargetAnchor() {
		if (getTargetAnchor() != null)
			getTargetAnchor().addAnchorListener(this);
	}

	/**
	 * Layouts this polyline. If the start and end anchors are present, the
	 * connection router is used to route this, after which it is laid out. It
	 * also fires a moved method.
	 */
	public void layout() {
		if (getSourceAnchor() != null && getTargetAnchor() != null)
			connectionRouter.route(this);

		Rectangle oldBounds = bounds;
		super.layout();
		bounds = null;

		if (!getBounds().contains(oldBounds)) {
			getParent().translateToParent(oldBounds);
			getUpdateManager().addDirtyRegion(getParent(), oldBounds);
		}

		repaint();
		fireFigureMoved();
	}

	/**
	 * Called just before the receiver is being removed from its parent. Results
	 * in removing itself from the connection router.
	 * 
	 * @since 2.0
	 */
	public void removeNotify() {
		unhookSourceAnchor();
		unhookTargetAnchor();
		connectionRouter.remove(this);
		super.removeNotify();
	}

	/**
	 * Removes the first occurence of the given listener.
	 * 
	 * @param listener
	 *            the listener being removed
	 * @since 3.2
	 */
	public void removeRoutingListener(org.eclipse.draw2dl.RoutingListener listener) {
		if (connectionRouter instanceof RoutingNotifier) {
			RoutingNotifier notifier = (RoutingNotifier) connectionRouter;
			notifier.listeners.remove(listener);
			if (notifier.listeners.isEmpty())
				connectionRouter = notifier.realRouter;
		}
	}

	/**
	 * @see IFigure#revalidate()
	 */
	public void revalidate() {
		super.revalidate();
		connectionRouter.invalidate(this);
	}

	/**
	 * Sets the connection router which handles the layout of this polyline.
	 * Generally set by the parent handling the polyline connection.
	 * 
	 * @param cr
	 *            the connection router
	 */
	public void setConnectionRouter(org.eclipse.draw2dl.ConnectionRouter cr) {
		if (cr == null)
			cr = org.eclipse.draw2dl.ConnectionRouter.NULL;
		org.eclipse.draw2dl.ConnectionRouter oldRouter = getConnectionRouter();
		if (oldRouter != cr) {
			connectionRouter.remove(this);
			if (connectionRouter instanceof RoutingNotifier)
				((RoutingNotifier) connectionRouter).realRouter = cr;
			else
				connectionRouter = cr;
			firePropertyChange(org.eclipse.draw2dl.Connection.PROPERTY_CONNECTION_ROUTER,
					oldRouter, cr);
			revalidate();
		}
	}

	/**
	 * Sets the routing constraint for this connection.
	 * 
	 * @param cons
	 *            the constraint
	 */
	public void setRoutingConstraint(Object cons) {
		if (connectionRouter != null)
			connectionRouter.setConstraint(this, cons);
		revalidate();
	}

	/**
	 * Sets the anchor to be used at the start of this polyline connection.
	 * 
	 * @param anchor
	 *            the new source anchor
	 */
	public void setSourceAnchor(org.eclipse.draw2dl.ConnectionAnchor anchor) {
		if (anchor == startAnchor)
			return;
		unhookSourceAnchor();
		// No longer needed, revalidate does this.
		// getConnectionRouter().invalidate(this);
		startAnchor = anchor;
		if (getParent() != null)
			hookSourceAnchor();
		revalidate();
	}

	/**
	 * Sets the decoration to be used at the start of the {@link org.eclipse.draw2dl.Connection}.
	 * 
	 * @param dec
	 *            the new source decoration
	 * @since 2.0
	 */
	public void setSourceDecoration(org.eclipse.draw2dl.RotatableDecoration dec) {
		if (startArrow == dec)
			return;
		if (startArrow != null)
			remove(startArrow);
		startArrow = dec;
		if (startArrow != null)
			add(startArrow, new org.eclipse.draw2dl.ArrowLocator(this, org.eclipse.draw2dl.ConnectionLocator.SOURCE));
	}

	/**
	 * Sets the anchor to be used at the end of the polyline connection. Removes
	 * this listener from the old anchor and adds it to the new anchor.
	 * 
	 * @param anchor
	 *            the new target anchor
	 */
	public void setTargetAnchor(ConnectionAnchor anchor) {
		if (anchor == endAnchor)
			return;
		unhookTargetAnchor();
		// No longer needed, revalidate does this.
		// getConnectionRouter().invalidate(this);
		endAnchor = anchor;
		if (getParent() != null)
			hookTargetAnchor();
		revalidate();
	}

	/**
	 * Sets the decoration to be used at the end of the {@link org.eclipse.draw2dl.Connection}.
	 * 
	 * @param dec
	 *            the new target decoration
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

	private void unhookSourceAnchor() {
		if (getSourceAnchor() != null)
			getSourceAnchor().removeAnchorListener(this);
	}

	private void unhookTargetAnchor() {
		if (getTargetAnchor() != null)
			getTargetAnchor().removeAnchorListener(this);
	}

	final class RoutingNotifier implements org.eclipse.draw2dl.ConnectionRouter {

		org.eclipse.draw2dl.ConnectionRouter realRouter;
		List listeners = new ArrayList(1);

		RoutingNotifier(ConnectionRouter router, org.eclipse.draw2dl.RoutingListener listener) {
			realRouter = router;
			listeners.add(listener);
		}

		public Object getConstraint(org.eclipse.draw2dl.Connection connection) {
			return realRouter.getConstraint(connection);
		}

		public void invalidate(org.eclipse.draw2dl.Connection connection) {
			for (int i = 0; i < listeners.size(); i++)
				((org.eclipse.draw2dl.RoutingListener) listeners.get(i)).invalidate(connection);

			realRouter.invalidate(connection);
		}

		public void route(org.eclipse.draw2dl.Connection connection) {
			boolean consumed = false;
			for (int i = 0; i < listeners.size(); i++)
				consumed |= ((org.eclipse.draw2dl.RoutingListener) listeners.get(i))
						.route(connection);

			if (!consumed)
				realRouter.route(connection);

			for (int i = 0; i < listeners.size(); i++)
				((org.eclipse.draw2dl.RoutingListener) listeners.get(i)).postRoute(connection);
		}

		public void remove(org.eclipse.draw2dl.Connection connection) {
			for (int i = 0; i < listeners.size(); i++)
				((org.eclipse.draw2dl.RoutingListener) listeners.get(i)).remove(connection);
			realRouter.remove(connection);
		}

		public void setConstraint(Connection connection, Object constraint) {
			for (int i = 0; i < listeners.size(); i++)
				((RoutingListener) listeners.get(i)).setConstraint(connection,
						constraint);
			realRouter.setConstraint(connection, constraint);
		}

	}

}
