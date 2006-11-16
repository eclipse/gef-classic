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
package org.eclipse.mylar.zest.core.internal.graphviewer.overview.parts;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.UpdateListener;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * An edit part that links itself to a graphical root edit part, and periodically refreshes itself
 * with the data in that edit part in order to create an overview.
 * @author Del Myers
 */

public class OverviewImageEditPart extends AbstractGraphicalEditPart implements UpdateListener {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	Image image;
	protected IFigure createFigure() {
		ImageFigure figure = new ImageFigure();
		return figure;
	}

	private GraphicalEditPart getCastedModel() {
		return (GraphicalEditPart)getModel();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		// TODO Auto-generated method stub

	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		
		IFigure clientFigure = ((GraphicalEditPart)((RootEditPart)getCastedModel()).getContents()).getFigure();
		Rectangle clientBounds = getBoundingArea();
		if (clientBounds.isEmpty()) return;
		if (image == null) {
			image = new Image(Display.getDefault(), clientBounds.width, clientBounds.height);
		}
		Rectangle imageBounds = new Rectangle(image.getBounds().x, image.getBounds().y, image.getBounds().width, image.getBounds().height);
		if (!imageBounds.getSize().equals(clientBounds.getSize())) {
			if (!image.isDisposed()) {
				image.dispose();
			}
			image = new Image(Display.getDefault(), clientBounds.width, clientBounds.height);
		}
		GC drawer = new GC(image);
		SWTGraphics graphics = new SWTGraphics(drawer);
		graphics.translate(-clientBounds.x, -clientBounds.y);
		clientFigure.paint(graphics);
		drawer.dispose();
		getFigure().setBounds(clientBounds);
		((ImageFigure)getFigure()).setImage(image);
		getFigure().invalidate();
		getFigure().repaint();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	public void activate() {
		getCastedModel().getFigure().getUpdateManager().addUpdateListener(this);
		super.activate();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	public void deactivate() {
		getCastedModel().getFigure().getUpdateManager().removeUpdateListener(this);
		super.deactivate();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#unregisterVisuals()
	 */
	protected void unregisterVisuals() {
		if (image != null && !image.isDisposed()) {
			image.dispose();
			image = null;
		}
		super.unregisterVisuals();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.UpdateListener#notifyPainting(org.eclipse.draw2d.geometry.Rectangle, java.util.Map)
	 */
	public void notifyPainting(Rectangle damage, Map dirtyRegions) {
		IFigure clientFigure = ((GraphicalEditPart)((RootEditPart)getCastedModel()).getContents()).getFigure();
		Rectangle clientBounds = getBoundingArea();
		if (clientBounds.isEmpty()) return;
		if (image == null) {
			refreshVisuals();
			return;
		}
		Rectangle imageBounds = new Rectangle(image.getBounds().x, image.getBounds().y, image.getBounds().width, image.getBounds().height);
		if (!imageBounds.getSize().equals(clientBounds.getSize())) {
			refreshVisuals();
			return;
		}
		List clientChildren = clientFigure.getChildren();
		Iterator keys = clientChildren.iterator();
		
		Rectangle region = (Rectangle) dirtyRegions.get(clientFigure);
		List dirtyFigures = new LinkedList();
		if (region != null) {
			dirtyFigures.add(clientFigure);
		}
		while (keys.hasNext()) {
			IFigure fig = (IFigure) keys.next();
			region = (Rectangle) dirtyRegions.get(fig);
			if (region != null) {
				dirtyFigures.add(fig);
			}
		}
		if (dirtyFigures.size() > clientChildren.size()/2) {
			refreshVisuals();
			return;
		}
		calculateDirtyArea(clientFigure, dirtyFigures, dirtyRegions);
	}
	
	/**
	 * @return
	 */
	private Rectangle getBoundingArea() {
		IFigure clientFigure = ((GraphicalEditPart)((RootEditPart)getCastedModel()).getContents()).getFigure();
		//Dimension logicalSize = part.getFigure().getSize();
 		Rectangle boundingArea = null;//new Rectangle(new Point(0,0), logicalSize);
		for (Iterator i = clientFigure.getChildren().iterator(); i.hasNext();) {
			IFigure child = (IFigure) i.next();
			if (boundingArea == null) {
				boundingArea = child.getBounds().getCopy();
			} else {
				boundingArea.union(child.getBounds());
			}
		}
		if (boundingArea == null) {
			boundingArea = new Rectangle();
		}
//		boundingArea.width += Math.abs(boundingArea.x);
//		boundingArea.height += Math.abs(boundingArea.y);
//		boundingArea.x = 0;
//		boundingArea.y = 0;
		return boundingArea;
	}

	/**
	 * @param dirtyFigures
	 * @param dirtyRegions
	 * @return
	 */
	private Rectangle calculateDirtyArea(IFigure clientFigure, List dirtyFigures, Map dirtyRegions) {
		if (image == null) return null;
//		if (region == null)
//			return null;
		GC drawer = new GC(image);
		Graphics graphics = new SWTGraphics(drawer);
		graphics.setBackgroundColor(clientFigure.getBackgroundColor());
		Rectangle clientbounds = getBoundingArea();
		graphics.translate(-clientbounds.x, -clientbounds.y);
		for (Iterator dirty = dirtyFigures.iterator(); dirty.hasNext();) {
			IFigure fig = (IFigure) dirty.next();
			Rectangle r = ((Rectangle) dirtyRegions.get(fig)).getCopy();
//			r.x -= fig.getInsets().left;
//			r.y -= fig.getInsets().top;
//			r.height += fig.getInsets().bottom;
//			r.width += fig.getInsets().right;
			
			//fig.getParent().translateToAbsolute(r);
			r = fig.getBounds().getCopy();
			//r.expand(fig.getInsets());
			graphics.fillRectangle(r);
			fig.paint(graphics);
			
		
//			intersectingSet.add(fig);
//			region.union(r);
//			for (Iterator intersectors = childSet.iterator(); intersectors.hasNext();) {
//				IFigure intersect = (IFigure) intersectors.next();
//				if (intersect.getBounds().intersects(region)) {
//					region.union(intersect.getBounds());
//					intersectingSet.add(intersect);
//					intersectors.remove();
//				}
//			}
		}
//		graphics.fillRectangle(region);
//		for (Iterator figs = intersectingSet.iterator(); figs.hasNext();) {
//			((IFigure)figs.next()).paint(graphics);
//		}
		drawer.dispose();
		getFigure().repaint();
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.UpdateListener#notifyValidating()
	 */
	public void notifyValidating() {
		// TODO Auto-generated method stub
		
	}
	

}
