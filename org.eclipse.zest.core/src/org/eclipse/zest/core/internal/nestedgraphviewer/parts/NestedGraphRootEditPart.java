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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.gefx.GraphRootEditPart;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFigure;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFreeformLayer;
import org.eclipse.mylar.zest.core.internal.viewers.figures.OutlineFigure;

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

	private int zoomStyle = ZestStyles.ZOOM_EXPAND;
	
	/**
	 * Initializes the root edit part with the given zoom style.
	 * This can be real zooming, fake zooming, or expand/collapse zooming.
	 * @param zoomStyle
	 * @see ZestStyles#ZOOM_REAL
	 * @see ZestStyles#ZOOM_FAKE
	 * @see ZestStyles#ZOOM_EXPAND
	 */
	public NestedGraphRootEditPart(int zoomStyle) {
		super();
		this.zoomStyle = zoomStyle;
	}
	
	/**
	 * Gets the maximum bounds for zooming.  This will be the size
	 * of the root NestedFigure.
	 * @return Rectangle
	 */
	protected Rectangle getMaxBounds() {
		Rectangle maxBounds = getRootNestedFigure().getBounds().getCopy();
		return maxBounds;
	}

	/**
	 * Gets the root NestedFigure.
	 * @return IFigure
	 */
	protected IFigure getRootNestedFigure() {
		IFigure fig = getFigure();
		if (getChildren().size() > 0) {
			fig = ((GraphicalEditPart)this.getChildren().get(0)).getFigure();
			if (fig instanceof NestedFreeformLayer) {
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
		
		switch (zoomStyle) {
			case ZestStyles.ZOOM_REAL :
				//doRealZoomIn(editPart, startBounds, maxBounds, 10);				
				//break;
			case ZestStyles.ZOOM_FAKE :
				//doFakeZoom(startBounds, maxBounds, 8);
				//break;
			case ZestStyles.ZOOM_EXPAND :
			default:
				doExpandZoom(startBounds, maxBounds, 5, (NestedFigure)editPart.getFigure());
				break;
		}
		
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
		getRootNestedFigure().translateToRelative(startBounds);
		NestedFigure nestedFig = (NestedFigure)editPart.getFigure();
		double scale = nestedFig.calculateTotalScale();
		startBounds.setSize(startBounds.getSize().scale(scale / nestedFig.getScale()));
		
		switch (zoomStyle) {
			case ZestStyles.ZOOM_REAL :
				// TODO: it is not very smooth zooming out
				//doRealZoomOut(editPart, maxBounds, startBounds, 10);
				//break;
			case ZestStyles.ZOOM_FAKE :
				doFakeZoom(maxBounds, startBounds, 8);
				//break;
			case ZestStyles.ZOOM_EXPAND :
			default :
				//doCollapseZoom(maxBounds, startBounds, 5, (NestedFigure)editPart.getFigure());
				break;
		}		
	}

	/**
	 * Draws an expanding dotted rectangle figure around the node to give the impression
	 * of zooming in.  The dotted rectangle starts at the center of the node.
	 */
	protected void doExpandZoom(Rectangle startBounds, Rectangle endBounds, final int STEPS, NestedFigure fig) {
		final int SLEEP = 35;
		if (STEPS > 0) {
			double xleft = startBounds.x - endBounds.x;
			double ytop = startBounds.y - endBounds.y;
			double xright = endBounds.right() - startBounds.right();
			double ybottom = endBounds.bottom() - startBounds.bottom();
			double xLeftScale = xleft / (double)STEPS, 
				xRightScale = xright / (double)STEPS;
			double yTopScale = ytop / (double)STEPS, 
				yBottomScale = ybottom / (double)STEPS;
			double scaleScale = (1 - fig.getScaledFigure().getScale()) / (double)STEPS;
			double initialScale = fig.getScaledFigure().getScale();
			
			IFigure parent = fig.getParent();
			parent.remove(fig);
			getFigure().add(fig);
			
			for (int i = 0; i <= STEPS; i++) {
				int x = (int)(startBounds.x - (i * xLeftScale));
				int y = (int)(startBounds.y - (i * yTopScale));
				int w = (int)(startBounds.width + (i * xLeftScale) + (i * xRightScale));
				int h = (int)(startBounds.height + (i * yTopScale) + (i * yBottomScale));
				fig.setScale(initialScale + (i * scaleScale));
				fig.setBounds(new Rectangle(x, y, w, h));				
				getViewer().flush();
				sleep(SLEEP);
			}
			
			fig.setScale(1);
			fig.setBounds(endBounds);
			getViewer().flush();
			sleep(SLEEP);
			
			getFigure().remove(fig);
		}
	}
	
	/**
	 * Draws an expanding dotted rectangle figure around the node to give the impression
	 * of zooming in.  The dotted rectangle starts at the center of the node.
	 */
	protected void doCollapseZoom(Rectangle startBounds, Rectangle endBounds, final int STEPS, NestedFigure fig) {
		final int SLEEP = 35;
		if (STEPS > 0) {
			double xleft = startBounds.x - endBounds.x;
			double ytop = startBounds.y - endBounds.y;
			double xright = endBounds.right() - startBounds.right();
			double ybottom = endBounds.bottom() - startBounds.bottom();
			double xLeftScale = xleft / (double)STEPS, 
				xRightScale = xright / (double)STEPS;
			double yTopScale = ytop / (double)STEPS, 
				yBottomScale = ybottom / (double)STEPS;
			double scaleScale = (1 - fig.getScaledFigure().getScale()) / (double)STEPS;
			double finalScale = fig.getScaledFigure().getScale();
			IFigure parent = fig.getParent();
			parent.remove(fig);
			getFigure().add(fig);
			
			for (int i = 0; i <= STEPS; i++) {
				int x = (int)(startBounds.x - (i * xLeftScale));
				int y = (int)(startBounds.y - (i * yTopScale));
				int w = (int)(startBounds.width + (i * xLeftScale) + (i * xRightScale));
				int h = (int)(startBounds.height + (i * yTopScale) + (i * yBottomScale));
				fig.setScale(1 - (i * scaleScale));
				fig.setBounds(new Rectangle(x, y, w, h));		
				getViewer().flush();
				sleep(SLEEP);
			}
			
			fig.setScale(finalScale);
			fig.setBounds(endBounds);
			getViewer().flush();
			sleep(SLEEP);

			getFigure().remove(fig);
			parent.add(fig);
			
			getRootNestedFigure().translateToAbsolute(endBounds);
			fig.translateToRelative(endBounds);
			fig.setBounds(endBounds);
		}
	}
	
	/**
	 * Draws an expanding dotted rectangle figure around the node to give the impression
	 * of zooming in.  The dotted rectangle starts at the center of the node.
	 */
	protected void doFakeZoom(Rectangle startBounds, Rectangle endBounds, final int STEPS) {
		final int BORDER = 2;
		final int SLEEP = 25;
		if (STEPS > 0) {
			OutlineFigure outline = new OutlineFigure();
			outline.setForegroundColor(ColorConstants.gray);
			outline.setLineWidth(BORDER);
			outline.setBounds(startBounds);
			
			double xleft = startBounds.x - endBounds.x;
			double ytop = startBounds.y - endBounds.y;
			double xright = endBounds.right() - startBounds.right();
			double ybottom = endBounds.bottom() - startBounds.bottom();
			double xLeftScale = xleft / (double)STEPS, 
				xRightScale = xright / (double)STEPS;
			double yTopScale = ytop / (double)STEPS, 
				yBottomScale = ybottom / (double)STEPS;

			//DebugPrint.println(startBounds + " -> " + endBounds + "\n" + xLeftScale + ", " + xRightScale + ", " + yTopScale + ", " + yBottomScale);
			IFigure fig = getFigure();
			fig.add(outline);
			
			for (int i = 0; i <= STEPS; i++) {
				int x = (int)(startBounds.x - (i * xLeftScale));
				int y = (int)(startBounds.y - (i * yTopScale));
				int w = (int)(startBounds.width + (i * xLeftScale) + (i * xRightScale));
				int h = (int)(startBounds.height + (i * yTopScale) + (i * yBottomScale));
				outline.setBounds(new Rectangle(x, y, w, h));
				getViewer().flush();
				sleep(SLEEP);
			}
			
			// outline the whole area
			outline.setBounds(endBounds);
			getViewer().flush();
			sleep(SLEEP);
			
			fig.remove(outline);
		}
	}
	
	/**
	 * Zooms in on the given node.  
	 * @param editPart
	 * @param startBounds
	 * @param endBounds
	 * @param STEPS
	 */
	protected void doRealZoomIn(NestedGraphNodeEditPart editPart, Rectangle startBounds, Rectangle endBounds, final int STEPS) {
		final int SLEEP = 35;
		if ((startBounds.width > 0) && (startBounds.height > 0) && (STEPS > 0)) {
			double maxZoom = Math.max(0.0D, Math.min((endBounds.width/startBounds.width), (endBounds.height/startBounds.height)) - 1.0D);
			double step = maxZoom / STEPS;
			double[] zoomLevels = new double[STEPS];
			for (int i = 0; i < zoomLevels.length; i++) {
				zoomLevels[i] = 1.0D + (i * step);
			}
			getZoomManager().setZoomLevels(zoomLevels);
			Point c = startBounds.getCenter();
			//Point p = getZoomManager().getScalableFigure().getBounds().getCenter();
			//Dimension initialDiff = p.getDifference(c);
			while (getZoomManager().canZoomIn()) {
				getZoomManager().zoomIn();
				Point c2 = editPart.getScreenBounds().getCenter();
				Dimension diff = c2.getDifference(c);
				//Point p2 = getZoomManager().getScalableFigure().getBounds().getCenter();
				//Dimension diff2 = p2.getDifference(p);
				//System.out.println(diff + "\t" + diff2 + "\t" + p2 + "\t" + c2);
				Point old = getZoomManager().getViewport().getViewLocation();
				Point loc = new Point(old.x + diff.width, old.y + diff.height);
				getZoomManager().setViewLocation(loc);
				getViewer().flush();
				sleep(SLEEP);
			}
		}
	}

	/**
	 * Zooms out on the given node.
	 * Buggy - doesn't do a very smooth job of zooming out.  
	 * @param editPart
	 * @param startBounds
	 * @param endBounds
	 * @param STEPS
	 */
	protected void doRealZoomOut(NestedGraphNodeEditPart editPart, Rectangle startBounds, Rectangle endBounds, final int STEPS) {
		final int SLEEP = 35;
		if ((endBounds.width > 0) && (endBounds.height > 0) && (STEPS > 0)) {
			double maxZoom = Math.max(0.0D, Math.min((startBounds.width/endBounds.width), (startBounds.height/endBounds.height)) - 1.0D);
			double step = maxZoom / STEPS;
			double[] zoomLevels = new double[STEPS];
			for (int i = 0; i < zoomLevels.length; i++) {
				zoomLevels[i] = 1.0D + (i * step);
			}
			getZoomManager().setZoomLevels(zoomLevels);
			getZoomManager().setZoom(zoomLevels[zoomLevels.length-1]);
			Point c = startBounds.getCenter();
			//Point p = getZoomManager().getScalableFigure().getBounds().getCenter();
			//Dimension initialDiff = p.getDifference(c);
			while (getZoomManager().canZoomOut()) {
				getZoomManager().zoomOut();
				Point c2 = editPart.getScreenBounds().getCenter();
				Dimension diff = c2.getDifference(c);
				//Point p2 = getZoomManager().getScalableFigure().getBounds().getCenter();
				//Dimension diff2 = p2.getDifference(p);
				//System.out.println(diff + "\t" + diff2 + "\t" + p2 + "\t" + c2);
				Point old = getZoomManager().getViewport().getViewLocation();
				Point loc = new Point(old.x + diff.width, old.y + diff.height);
				getZoomManager().setViewLocation(loc);
				getViewer().flush();
				sleep(SLEEP);
			}
		}
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
