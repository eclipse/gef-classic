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
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts;

import java.util.List;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.gefx.GraphRootEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.NestedGraphViewerImpl;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFigure;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFreeformLayer;

/**
 * Extends GraphRootEditPart to add zooming support.
 * Currently there are three methods of zoom: the first is to use the 
 * ZoomMananger and do "real" zooming.  The second is to fake the zooming by drawing an
 * expanding or collapsing rectangle around the object to give the impression of zooming.  The 
 * third way is to expand or collapse the current rectangle.
 * 
 * @author Chris Callendar
 */
public class NestedGraphRootEditPart extends GraphRootEditPart {
	
	private ZoomManager zoomManager;

	
	/**
	 * Initializes the root edit part with the given zoom style.
	 * This can be real zooming, fake zooming, or expand/collapse zooming.
	 * @param zoomStyle
	 * @see ZestStyles#ZOOM_REAL
	 * @see ZestStyles#ZOOM_FAKE
	 * @see ZestStyles#ZOOM_EXPAND
	 */
	public NestedGraphRootEditPart( ) {
		super();
	}
	
	/**
	 * Gets the maximum bounds for zooming.  This will be the size
	 * of the root NestedFigure.
	 * @return Rectangle
	 */
	protected Rectangle getMaxBounds() {
		Rectangle bounds = getRootNestedFigure().getBounds();
		Rectangle maxBounds = getFigure().getBounds().getCopy();
		getFigure().translateToParent(maxBounds);
		return bounds;
	}
	

	/**
	 * Gets the root NestedFigure.
	 * @return IFigure
	 */
	
	protected IFigure getRootNestedFigure() {
		// The structure is really Figure -> FreeFormLayer -> NestedFreeformLayer
		IFigure fig = getFigure();
		if (getChildren().size() > 0) {
			fig = ((GraphicalEditPart)this.getChildren().get(0)).getFigure();
			if ( fig instanceof FreeformLayer ) {
				
				fig = (IFigure)fig.getChildren().get(0);
			}
			else if (fig instanceof NestedFreeformLayer) {
				NestedFreeformLayer layer = (NestedFreeformLayer)fig;
				fig = layer.getNestedFigure();
			}
		}
		return fig;
	}
	
	
	
	/**
	 * Zooms in on the given node.
	 * The type of zooming (real, fake, expand) depends on the zoomStyle.
	 * @param editPart
	 */
	public void zoomInOnNode(NestedGraphNodeEditPart editPart) {
		if (editPart == null)
			return;
		
		Rectangle maxBounds = getMaxBounds();
		Rectangle startBounds = editPart.getScreenBounds();
		getRootNestedFigure().translateToRelative(startBounds);

		doExpandZoom(startBounds, maxBounds, 10, (NestedFigure)editPart.getFigure());
	}
	
	/**
	 * Zooms out on the given node.  
	 * The type of zooming (real, fake, collapse) depends on the zoomStyle.
	 * @param editPart
	 */
	
	public void zoomOutOnNode( NestedGraphNodeEditPart editPart) {
		if (editPart == null)
			return;
		
		Rectangle maxBounds = getMaxBounds();
		Rectangle startBounds = editPart.getScreenBounds();
		
		doCollapseZoom(maxBounds, startBounds, 10, (NestedFigure)editPart.getFigure());
	}
	

	/**
	 * Draws an expanding dotted rectangle figure around the node to give the impression
	 * of zooming in.  The dotted rectangle starts at the center of the node.
	 */
	private void doExpandZoom(Rectangle startBounds, Rectangle endBounds, final int STEPS, NestedFigure fig) {
		if (STEPS > 0) {
			double xleft = startBounds.x - endBounds.x;
			double ytop = startBounds.y - endBounds.y;
			double xright = endBounds.right() - startBounds.right();
			double ybottom = endBounds.bottom() - startBounds.bottom();
			double xLeftScale = xleft / (double)STEPS, 
				xRightScale = xright / (double)STEPS;
			double yTopScale = ytop / (double)STEPS, 
				yBottomScale = ybottom / (double)STEPS;			
			
			IFigure parent = fig.getParent();
			parent.remove(fig);
			getFigure().add(fig);
			fig.setOpaque(true);
			for (int i = 0; i <= STEPS; i++) {
				int x = (int)(startBounds.x - (i * xLeftScale));
				int y = (int)(startBounds.y - (i * yTopScale));
				int w = (int)(startBounds.width + (i * xLeftScale) + (i * xRightScale));
				int h = (int)(startBounds.height + (i * yTopScale) + (i * yBottomScale));
				fig.setBounds(new Rectangle(x, y, w, h));		
				getViewer().flush();
				this.sleep(25);
			}
			fig.setOpaque(false);
			
			fig.setBounds(endBounds);
			getViewer().flush();
			
			getFigure().remove(fig);
		}
		
	}
	
	
	public void resize(int width, int height) {
		getFigure().setSize(width, height);
		List l = getChildren();
		((NestedGraphEditPart)l.get(0)).resize(width, height);
	}
	
	
	/**
	 * Draws an expanding dotted rectangle figure around the node to give the impression
	 * of zooming in.  The dotted rectangle starts at the center of the node.
	 */
	private void doCollapseZoom(Rectangle startBounds, Rectangle endBounds, final int STEPS, NestedFigure fig) {
		
		if (STEPS > 0) {
			double xleft = startBounds.x - endBounds.x;
			double ytop = startBounds.y - endBounds.y;
			double xright = endBounds.right() - startBounds.right();
			double ybottom = endBounds.bottom() - startBounds.bottom();
			double xLeftScale = xleft / (double)STEPS, 
				xRightScale = xright / (double)STEPS;
			double yTopScale = ytop / (double)STEPS, 
				yBottomScale = ybottom / (double)STEPS;

			
			IFigure parent = fig.getParent();
			parent.remove(fig);
			getFigure().add(fig);
			fig.setOpaque(true);
			for (int i = 0; i <= STEPS; i++) {
				int x = (int)(startBounds.x - (i * xLeftScale));
				int y = (int)(startBounds.y - (i * yTopScale));
				int w = (int)(startBounds.width + (i * xLeftScale) + (i * xRightScale));
				int h = (int)(startBounds.height + (i * yTopScale) + (i * yBottomScale));
				fig.setBounds(new Rectangle(x, y, w, h));		
				((NestedGraphViewerImpl)getViewer()).flush();
				this.sleep(25);
			}
			fig.setOpaque(false);
			
			fig.setBounds(endBounds);
			getViewer().flush();

			getFigure().remove(fig);
			parent.add(fig);
			
			getRootNestedFigure().translateToAbsolute(endBounds);
			fig.translateToRelative(endBounds);
			fig.setBounds(endBounds);
			
		}
		
	}
	

	
	/**
	 * Gets the zoom manager for the root edit part
	 */
	public ZoomManager getZoomManager() {
		return this.zoomManager;
	}
	
	/**
	 * Convenience method for calling Thread.sleep
	 * and catching the InterruptedException.
	 * @param millis the number of milliseconds to sleep
	 */
	protected void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignore) {}
	}

}
