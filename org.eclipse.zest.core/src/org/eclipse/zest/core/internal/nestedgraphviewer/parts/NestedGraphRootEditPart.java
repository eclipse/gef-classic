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

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformFigure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.GuideLayer;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.SimpleRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.gefx.ZestRootEditPart;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.NestedGraphViewerImpl;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFigure;

/**
 * Extends GraphRootEditPart to add zooming support.
 * Currently there are three methods of zoom: the first is to use the 
 * ZoomMananger and do "real" zooming.  The second is to fake the zooming by drawing an
 * expanding or collapsing rectangle around the object to give the impression of zooming.  The 
 * third way is to expand or collapse the current rectangle.
 * 
 * @author Chris Callendar
 */
public class NestedGraphRootEditPart extends SimpleRootEditPart
		implements LayerConstants, ZestRootEditPart, LayerManager {
	
	private ZoomManager zoomManager;
	protected GraphEditPart graphEditPart = null;
	private LayeredPane innerLayers;
	private LayeredPane printableLayers;

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
	 * The contents' Figure will be added to the PRIMARY_LAYER.
	 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane() {
		return getLayer(PRIMARY_LAYER);
	}

	/**
	 * The root editpart does not have a real model.  The LayerManager ID is returned so that
	 * this editpart gets registered using that key.
	 * @see org.eclipse.gef.EditPart#getModel()
	 */
	public Object getModel() {
		return LayerManager.ID;
	}
	
	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Viewport viewport = new FreeformViewport() {
			/**
			 * Readjusts the scrollbars.  In doing so, it gets the freeform extent of the contents and
			 * unions this rectangle with this viewport's client area, then sets the contents freeform
			 * bounds to be this unioned rectangle.  Then proceeds to set the scrollbar values based
			 * on this new information.
			 * @see Viewport#readjustScrollBars()
			 */
			protected void readjustScrollBars() {
				if (getContents() == null)
					return;
				if (!(getContents() instanceof FreeformFigure))
					return;
				FreeformFigure ff = (FreeformFigure)getContents();
				Rectangle clientArea = getClientArea();
				Rectangle bounds = ff.getFreeformExtent().getCopy();
				bounds.union(0, 0, clientArea.width, clientArea.height);
				ff.setFreeformBounds(bounds);
			}
		};
		innerLayers = new FreeformLayeredPane();
		createLayers(innerLayers);
		viewport.setContents(innerLayers);
		return viewport;
	}
	
	/**
	 * Creates the top-most set of layers on the given layered pane.
	 * @param layeredPane the parent for the created layers
	 */
	protected void createLayers(LayeredPane layeredPane) {
		layeredPane.add(getPrintableLayers(), PRINTABLE_LAYERS);
		layeredPane.add(new FreeformLayer(), HANDLE_LAYER);
		layeredPane.add(new FeedbackLayer(), FEEDBACK_LAYER);
		layeredPane.add(new GuideLayer(), GUIDE_LAYER);
	}
	
	/**
	 * Returns the LayeredPane that should be used during printing. This layer will be
	 * identified using {@link LayerConstants#PRINTABLE_LAYERS}.
	 * @return the layered pane containing all printable content
	 */
	protected LayeredPane getPrintableLayers() {
		if (printableLayers == null)
			printableLayers = createPrintableLayers();
		return printableLayers;
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.gefx.GraphRootEditPart#createPrintableLayers()
	 */
	protected LayeredPane createPrintableLayers() {
		FreeformLayeredPane layeredPane = new FreeformLayeredPane();
		//override to put the connection layers on the top
		layeredPane.add(new FreeformLayer(), PRIMARY_LAYER);
		layeredPane.add(new ConnectionLayer(), CONNECTION_LAYER);
		layeredPane.add(new ConnectionLayer(), CONNECTION_FEEDBACK_LAYER);
		
		return layeredPane;
	}

	/**
	 * Sets the main edit part for the model. You should be able to 
	 * fire changes off here and see the effect
	 */
	public void setModelRootEditPart(Object modelRootEditPart) {
		this.graphEditPart = (GraphEditPart) modelRootEditPart;
	}

	/**
	 * Returns the layer indicated by the key. Searches all layered panes.
	 * @see LayerManager#getLayer(Object)
	 */
	public IFigure getLayer(Object key) {
		if (innerLayers == null)
			return null;
		IFigure layer = innerLayers.getLayer(key);
		if (layer != null)
			return layer;
		if (printableLayers == null)
			return null;
		return printableLayers.getLayer(key);
	}
	
	class FeedbackLayer
	extends FreeformLayer
{
	FeedbackLayer() {
		setEnabled(false);
	}
}

	
}
