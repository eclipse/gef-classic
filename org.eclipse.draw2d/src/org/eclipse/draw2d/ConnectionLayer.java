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

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Layer designed specifically to handle the presence of connections. This is done due to
 * the necessity of having a router for the connections added.
 */
public class ConnectionLayer
	extends FreeformLayer
{
	
	/**
	 * Clipping strategy for connection layer, which takes into account nested
	 * view ports and truncates those parts of connections which reach outside.
	 * 
	 * @author Alexander Nyssen
	 * @author Philip Ritzkopf
	 * 
	 * @since 3.6
	 */
	protected class ClippingStrategy implements IClippingStrategy {

		/**
		 * @see org.eclipse.draw2d.IClippingStrategy#getClip(org.eclipse.draw2d.IFigure)
		 */
		public Rectangle[] getClip(IFigure figure) {
			Rectangle clipRect = null;
			if (figure instanceof Connection) {
				// clip rect is in absolute coordinates
				clipRect = getEdgeClippingRectangle((Connection) figure);
			} else {
				clipRect = getNodeClippingRectangle(figure);
			}
			// translate clipping rectangle relative to the parent figure's
			// (i.e. the connection layer's) client area
			if (clipRect != null) {
				figure.translateToRelative(clipRect);
			}
			return new Rectangle[] { clipRect };
		}

		protected Rectangle getEdgeClippingRectangle(Connection connection) {
			// start with clipping the connection at its bounds
			Rectangle clipRect = getAbsoluteBoundsCopy(connection);

			// in case we cannot infer source and target of the connection (e.g.
			// if XYAnchors are used), this is all we can do
			if (connection.getSourceAnchor() == null
					|| connection.getSourceAnchor().getOwner() == null
					|| connection.getTargetAnchor() == null
					|| connection.getTargetAnchor().getOwner() == null) {
				return clipRect;
			}

			// clip edge at the nearest enclosing viewport of its source and
			// target (i.e. at the scroll pane enclosing this viewport)
			Viewport nearestEnclosingSourceTargetViewport = ViewportUtilities
					.getNearestCommonViewport(connection.getSourceAnchor()
							.getOwner(), connection.getTargetAnchor()
							.getOwner());
			if (nearestEnclosingSourceTargetViewport != null) {				
				clipRect.intersect(getNodeClippingRectangle(nearestEnclosingSourceTargetViewport
							.getParent()));
			}

			// if source and target do not share a common viewport, this has to be further restricted
			if (ViewportUtilities.getNearestEnclosingViewport(connection
					.getSourceAnchor().getOwner()) != ViewportUtilities.getNearestEnclosingViewport(connection
							.getTargetAnchor().getOwner()) ) {
				Rectangle sourceClipRect = getNodeClippingRectangle(connection
						.getSourceAnchor().getOwner());
				Rectangle targetClipRect = getNodeClippingRectangle(connection
						.getTargetAnchor().getOwner());
				clipRect = clipRect.intersect(sourceClipRect
						.getUnion(targetClipRect));
			}
			return clipRect;
		}

		protected Rectangle getNodeClippingRectangle(IFigure figure) {
			// start with the bounds of the edit part's figure
			Rectangle clipRect = getAbsoluteBoundsCopy(figure);

			// now traverse the viewport path of the figure (and reduce clipRect
			// to what is actually visible)
			List enclosingViewportsPath = ViewportUtilities
					.getEnclosingViewportsPath(figure);
			for (Iterator iterator = enclosingViewportsPath.iterator(); iterator
					.hasNext();) {
				Viewport viewport = (Viewport) iterator.next();
				clipRect.intersect(getAbsoluteBoundsCopy(viewport.getParent()));
			}
			return clipRect;
		}

		private Rectangle getAbsoluteBoundsCopy(IFigure figure) {
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

public ConnectionLayer(){
	// replate the original clipping strategy
	setClippingStrategy(new ClippingStrategy());
}

/**
 * Adds the given figure with the given contraint at the given index. If the figure is a
 * {@link Connection}, its {@link ConnectionRouter} is set.
 *
 * @param figure  Figure being added
 * @param constraint  Constraint of the figure being added
 * @param index  Index where the figure is to be added
 * @since 2.0 
 */
public void add(IFigure figure, Object constraint, int index) {
	super.add(figure, constraint, index);
	
	// If the connection layout manager is set, then every
	// figure added should use this layout manager.
	if (figure instanceof Connection && getConnectionRouter() != null)
		((Connection)figure).setConnectionRouter(getConnectionRouter());
}

/**
 * Returns the ConnectionRouter being used by this layer.
 *
 * @return  ConnectionRouter being used by this layer
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
 * Removes the figure from this Layer.  If the figure is a {@link Connection}, that
 * Connection's {@link ConnectionRouter} is set to <code>null</code>.
 * 
 * @param figure The figure to remove
 */
public void remove(IFigure figure) {
	if (figure instanceof Connection)
		((Connection)figure).setConnectionRouter(null);
	super.remove(figure);
}

/**
 * Sets the ConnectionRouter for this layer. This router is set as the ConnectionRouter
 * for all the child connections of this Layer.
 *
 * @param router  The ConnectionRouter to set for this Layer
 * @since 2.0
 */
public void setConnectionRouter(ConnectionRouter router) {
	connectionRouter = router;
	FigureIterator iter = new FigureIterator(this);
	IFigure figure;
	while (iter.hasNext()) {
		figure = iter.nextFigure();
		if (figure instanceof Connection)
			((Connection)figure).setConnectionRouter(router);
	}
}

/**
 * Sets whether antialiasing should be enabled for the connection layer. If this value is
 * set to something other than {@link SWT#DEFAULT}, {@link Graphics#setAntialias(int)}
 * will be called with the given value when painting this layer.
 * @param antialias the antialias setting
 * @since 3.1
 */
public void setAntialias(int antialias) {
	this.antialias = antialias;
}

}
