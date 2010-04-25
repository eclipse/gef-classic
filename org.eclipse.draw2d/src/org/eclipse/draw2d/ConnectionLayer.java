/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Research Group Software Construction,
 *     RWTH Aachen University, Germany - Contribution for Bugzilla 195527
 *******************************************************************************/
package org.eclipse.draw2d;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Layer designed specifically to handle the presence of connections. This is
 * done due to the necessity of having a router for the connections added.
 */
public class ConnectionLayer extends FreeformLayer {

	/**
	 * Clipping strategy for connection layer, which takes into account nested
	 * view ports and truncates those parts of connections which reach outside
	 * and are thus not visible.
	 * 
	 * @author Alexander Nyssen
	 * @author Philip Ritzkopf
	 * 
	 * @since 3.6
	 */
	public static class ConnectionLayerClippingStrategy implements
			IClippingStrategy {

		private ConnectionLayer connectionLayer = null;

		protected ConnectionLayerClippingStrategy(
				ConnectionLayer connectionLayer) {
			this.connectionLayer = connectionLayer;
		}

		/**
		 * @see org.eclipse.draw2d.IClippingStrategy#getClip(org.eclipse.draw2d.IFigure)
		 */
		public Rectangle[] getClip(IFigure figure) {
			Rectangle[] clipRect = null;
			if (figure instanceof Connection) {
				clipRect = getEdgeClippingRectangle((Connection) figure);
			} else {
				clipRect = new Rectangle[] { getNodeClippingRectangle(figure) };
			}
			// translate clipping rectangles (which are in absolute coordinates)
			// to be relative to the parent figure's (i.e. the connection
			// layer's) client area
			for (int i = 0; i < clipRect.length; i++) {
				figure.translateToRelative(clipRect[i]);
			}
			return clipRect;
		}

		/**
		 * Computes clipping rectangle(s) for a given connection. Will consider
		 * all enclosing viewports, excluding the root viewport.
		 */
		protected Rectangle[] getEdgeClippingRectangle(Connection connection) {
			// start with clipping the connection at its original bounds
			Rectangle clipRect = getAbsoluteBoundsAsCopy(connection);

			// in case we cannot infer source and target of the connection (e.g.
			// if XYAnchors are used), returning the bounds is all we can do
			ConnectionAnchor sourceAnchor = connection.getSourceAnchor();
			ConnectionAnchor targetAnchor = connection.getTargetAnchor();
			if (sourceAnchor == null || sourceAnchor.getOwner() == null
					|| targetAnchor == null || targetAnchor.getOwner() == null) {
				return new Rectangle[] { clipRect };
			}

			// source and target figure are known, see if there is common
			// viewport
			// the connection has to be clipped at.
			IFigure sourceFigure = sourceAnchor.getOwner();
			IFigure targetFigure = targetAnchor.getOwner();
			Viewport nearestEnclosingCommonViewport = ViewportUtilities
					.getNearestCommonViewport(sourceFigure, targetFigure);
			if (nearestEnclosingCommonViewport == null) {
				return new Rectangle[] { clipRect };
			}

			// if the nearest common viewport is not the root viewport, we may
			// start with clipping the connection at this viewport.
			if (nearestEnclosingCommonViewport != getRootViewport()) {
				clipRect.intersect(getNodeClippingRectangle(nearestEnclosingCommonViewport
						.getParent()));
			}

			// if the nearest common viewport of source and target is not simultaneously
			// the nearest enclosing viewport of source and target respectively, the
			// connection has to be further clipped (the connection may even not be
			// visible at all)
			Viewport nearestEnclosingSourceViewport = ViewportUtilities
					.getNearestEnclosingViewport(sourceFigure);
			Viewport nearestEnclosingTargetViewport = ViewportUtilities
					.getNearestEnclosingViewport(targetFigure);
			if (nearestEnclosingSourceViewport != nearestEnclosingTargetViewport) {
				// compute if source and target anchor are visible
				// within the nearest common enclosing viewport (which may
				// itself be nested in other viewports).
				Rectangle sourceClipRect = getAbsoluteBoundsAsCopy(sourceFigure);
				if (nearestEnclosingSourceViewport != nearestEnclosingCommonViewport) {
					clipAtViewports(sourceClipRect, ViewportUtilities
							.getViewportsPath(nearestEnclosingSourceViewport,
									nearestEnclosingCommonViewport, false));
				}
				Rectangle targetClipRect = getAbsoluteBoundsAsCopy(targetFigure);
				if (nearestEnclosingTargetViewport != nearestEnclosingCommonViewport) {
					clipAtViewports(targetClipRect, ViewportUtilities
							.getViewportsPath(nearestEnclosingTargetViewport,
									nearestEnclosingCommonViewport, false));
				}
				PointList absolutePointsAsCopy = getAbsolutePointsAsCopy(connection);
				boolean sourceAnchorVisible = sourceClipRect
						.contains(absolutePointsAsCopy.getFirstPoint());
				boolean targetAnchorVisible = targetClipRect
						.contains(absolutePointsAsCopy.getLastPoint());

				if (!sourceAnchorVisible || !targetAnchorVisible) {
					// one (or both) of source or target anchor is invisible
					// within the nearest common viewport, so up to now
					// we regard the edge as invisible.
					return new Rectangle[] {};
					// TODO: We could come up with a more decent strategy here,
					// which also computes clipping fragments in those cases
					// where source/target are not visible but the edge
					// intersects with the enclosing source/target viewport's
					// parents bounds.

				} else {
					// both ends are visible, so just return what we have
					// computed before
					// (clipping at nearest enclosing viewport)
					return new Rectangle[] { clipRect };
				}
			} else {
				// source and target share the same enclosing viewport, so just
				// return what we have computed before (clipping at nearest
				// enclosing viewport)
				return new Rectangle[] { clipRect };
			}
		}

		/**
		 * Computes clipping rectangle for a given (node) figure. Will consider
		 * all enclosing viewports, excluding the root viewport.
		 */
		protected Rectangle getNodeClippingRectangle(IFigure figure) {
			// start with the bounds of the edit part's figure
			Rectangle clipRect = getAbsoluteBoundsAsCopy(figure);

			// now traverse the viewport path of the figure (and reduce clipRect
			// to what is actually visible); process all viewports up to the
			// root viewport
			List enclosingViewportsPath = ViewportUtilities.getViewportsPath(
					ViewportUtilities.getNearestEnclosingViewport(figure),
					getRootViewport(), false);
			clipAtViewports(clipRect, enclosingViewportsPath);
			return clipRect;
		}

		/**
		 * Clips the given clipRect at all given viewports.
		 */
		protected void clipAtViewports(Rectangle clipRect,
				List enclosingViewportsPath) {
			for (Iterator iterator = enclosingViewportsPath.iterator(); iterator
					.hasNext();) {
				clipRect.intersect(getAbsoluteViewportClientAreaAsCopy((Viewport) iterator
						.next()));
			}
		}

		/**
		 * Returns the root viewport, i.e. the nearest enclosing viewport of the
		 * connection layer, which corresponds to the nearest enclosing common
		 * viewport of primary and connection layer.
		 */
		protected Viewport getRootViewport() {
			return ViewportUtilities
					.getNearestEnclosingViewport(connectionLayer);
		}

		/**
		 * Returns the connection's points in absolute coordinates.
		 */
		protected PointList getAbsolutePointsAsCopy(Connection connection) {
			PointList points = connection.getPoints().getCopy();
			connection.translateToAbsolute(points);
			return points;
		}

		/**
		 * Returns the viewport's client area in absolute coordinates.
		 */
		protected Rectangle getAbsoluteViewportClientAreaAsCopy(
				Viewport viewport) {
			Rectangle absoluteClientArea = viewport.getClientArea();
			viewport.translateToParent(absoluteClientArea);
			viewport.translateToAbsolute(absoluteClientArea);
			return absoluteClientArea;
		}

		/**
		 * Returns the figure's bounds in absolute coordinates.
		 */
		protected Rectangle getAbsoluteBoundsAsCopy(IFigure figure) {
			Rectangle absoluteFigureBounds = figure.getBounds().getCopy();
			figure.translateToAbsolute(absoluteFigureBounds);
			return absoluteFigureBounds;
		}
	}

	int antialias = SWT.DEFAULT;

	/**
	 * The ConnectionRouter used to route all connections on this layer.
	 */
	protected ConnectionRouter connectionRouter;

	public ConnectionLayer() {
		// set the default clipping strategy for connection layer
		setClippingStrategy(new ConnectionLayerClippingStrategy(this));
	}

	/**
	 * Adds the given figure with the given contraint at the given index. If the
	 * figure is a {@link Connection}, its {@link ConnectionRouter} is set.
	 * 
	 * @param figure
	 *            Figure being added
	 * @param constraint
	 *            Constraint of the figure being added
	 * @param index
	 *            Index where the figure is to be added
	 * @since 2.0
	 */
	public void add(IFigure figure, Object constraint, int index) {
		super.add(figure, constraint, index);

		// If the connection layout manager is set, then every
		// figure added should use this layout manager.
		if (figure instanceof Connection && getConnectionRouter() != null)
			((Connection) figure).setConnectionRouter(getConnectionRouter());
	}

	/**
	 * Returns the ConnectionRouter being used by this layer.
	 * 
	 * @return ConnectionRouter being used by this layer
	 * @since 2.0
	 */
	public ConnectionRouter getConnectionRouter() {
		return connectionRouter;
	}

	/**
	 * @see IFigure#paint(Graphics)
	 */
	public void paint(Graphics graphics) {
		if (antialias != SWT.DEFAULT)
			graphics.setAntialias(antialias);
		super.paint(graphics);
	}

	/**
	 * Removes the figure from this Layer. If the figure is a {@link Connection}
	 * , that Connection's {@link ConnectionRouter} is set to <code>null</code>.
	 * 
	 * @param figure
	 *            The figure to remove
	 */
	public void remove(IFigure figure) {
		if (figure instanceof Connection)
			((Connection) figure).setConnectionRouter(null);
		super.remove(figure);
	}

	/**
	 * Sets the ConnectionRouter for this layer. This router is set as the
	 * ConnectionRouter for all the child connections of this Layer.
	 * 
	 * @param router
	 *            The ConnectionRouter to set for this Layer
	 * @since 2.0
	 */
	public void setConnectionRouter(ConnectionRouter router) {
		connectionRouter = router;
		FigureIterator iter = new FigureIterator(this);
		IFigure figure;
		while (iter.hasNext()) {
			figure = iter.nextFigure();
			if (figure instanceof Connection)
				((Connection) figure).setConnectionRouter(router);
		}
	}

	/**
	 * Sets whether antialiasing should be enabled for the connection layer. If
	 * this value is set to something other than {@link SWT#DEFAULT},
	 * {@link Graphics#setAntialias(int)} will be called with the given value
	 * when painting this layer.
	 * 
	 * @param antialias
	 *            the antialias setting
	 * @since 3.1
	 */
	public void setAntialias(int antialias) {
		this.antialias = antialias;
	}

}
