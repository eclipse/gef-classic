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
package org.eclipse.mylar.zest.core.internal.graphviewer.parts.tools;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;

/**
 * A tool that is used in the graph overview viewer that allows the user to select an area
 * of the graph to zoom on.
 * @author Del Myers
 *
 */
public class OverviewRectangleTool extends AbstractTool implements DragTracker, ControlListener, ZoomListener, PropertyChangeListener, FigureListener {
	private RectangleFigure focusFigure;
	private int dragState;
	private Rectangle startBounds;
	private TargetShape target;
	private FigureListener focusFigureListener;
	private Rectangle referenceBounds;
	private ZoomManager currentManager;
	
	private static final int OUTSIDE = 0;
	private static final int INSIDE = 1<<1;
	private static final int NORTH = INSIDE | 1<<2;
	private static final int EAST = INSIDE |1<<3;
	private static final int SOUTH = INSIDE | 1<<4;
	private static final int WEST = INSIDE | 1<<5;
	
	private static final int NORTHWEST = INSIDE | NORTH | WEST;
	private static final int NORTHEAST = INSIDE | NORTH | EAST;
	private static final int SOUTHWEST = INSIDE | SOUTH | WEST;
	private static final int SOUTHEAST = INSIDE | SOUTH | EAST;
	
	private class FocusMoveListener implements FigureListener {

		/* (non-Javadoc)
		 * @see org.eclipse.draw2d.FigureListener#figureMoved(org.eclipse.draw2d.IFigure)
		 */
		public void figureMoved(IFigure source) {
			//move the target to the center of the figure.
			setTargetCenter(source.getBounds().getCenter());
		}
		
	}
	
	/**
	 * 
	 */
	public OverviewRectangleTool() {
		setUnloadWhenFinished(false);
		referenceBounds = new Rectangle();
		//setEditDomain(new EditDomain());
	}
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#handleDragStarted()
	 */
	protected boolean handleDragStarted() {
		setState(STATE_DRAG);
		setDragState(getRelativeCursorLocation(getStartLocation()));
		Rectangle bounds = getFeedBackFigure().getBounds().getCopy();
		getFeedBackFigure().translateToAbsolute(bounds);
		setDragStartBounds(bounds);
		return true;
	}
	
	/**
	 * @param bounds
	 */
	private void setDragStartBounds(Rectangle bounds) {
		this.startBounds = bounds.getCopy();
		
	}

	private Rectangle getDragStartBounds() {
		return startBounds;
	}
	
	private void setDragState(int state) {
		this.dragState = state;
	}
	
	/**
	 * @return the dragState
	 */
	private int getDragState() {
		return dragState;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#handleDragInProgress()
	 */
	protected boolean handleDragInProgress() {
		setState(STATE_DRAG_IN_PROGRESS);
		Dimension delta = getDragMoveDelta();
		Point start = getStartLocation();
		RectangleFigure feedBack = getFeedBackFigure();
		boolean moving = false;
		int state = getDragState();
		Rectangle oldBounds = getDragStartBounds();
		Rectangle newBounds = oldBounds.getCopy();
		if (isNorth(state)) {
			newBounds.y = oldBounds.y+delta.height;
			newBounds.height = oldBounds.height-delta.height;
		} else if (isSouth(state)) {
			newBounds.height = oldBounds.height+delta.height;
		}
		if (isWest(state)) {
			newBounds.x = oldBounds.x+delta.width;
			newBounds.width = oldBounds.width-delta.width;
		} else if (isEast(state)) {
			newBounds.width = oldBounds.width+delta.width;
		}
		if (!(isWest(state) || isEast(state) || isNorth(state) || isSouth(state))) {
			if (isInside(state)) {
				moving = true;
				newBounds.x = oldBounds.x+delta.width;
				newBounds.y = oldBounds.y+delta.height;
			} else {
				//a new rectangle is drawn.
				newBounds = new Rectangle(start, delta);
			}
		}
		//adjust the bounds for negative widths and heights
		if (newBounds.width < 0) {
			newBounds.x += newBounds.width;
			newBounds.width = -newBounds.width;
		}
		if (newBounds.height < 0) {
			newBounds.y += newBounds.height;
			newBounds.height = -newBounds.height;
		}
		int minX = getCurrentViewer().getControl().getBounds().x;
		int minY = getCurrentViewer().getControl().getBounds().y;
		int maxWidth = getCurrentViewer().getControl().getBounds().width;
		int maxHeight = getCurrentViewer().getControl().getBounds().height;
		if (newBounds.x < minX || newBounds.y < minY || 
			newBounds.x + newBounds.width > minX + maxWidth ||
			newBounds.y + newBounds.height > minY + maxHeight) {
			if (moving) {
				if (newBounds.x < minX) {
					newBounds.x = minX;
				} 
				if (newBounds.y < minY) {
					newBounds.y = minY;
				}
				if (newBounds.x+newBounds.width > minX+maxWidth) {
					newBounds.x = maxWidth-newBounds.width;;
				}
				if (newBounds.y +newBounds.height > minY+maxHeight) {
					newBounds.y = maxHeight-newBounds.height;
				}
			} else {
				if (newBounds.x < minX) {
					newBounds.width = newBounds.x+newBounds.width-minX;
					newBounds.x = minX;
				}
				if (newBounds.y < minY) {
					newBounds.height = newBounds.y+newBounds.height-minY;
					newBounds.y = minY;
				}
				if (newBounds.x+newBounds.width > minX+maxWidth) {
					newBounds.width = maxWidth-newBounds.x;
				}
				if (newBounds.y +newBounds.height > minY+maxHeight) {
					newBounds.height = maxHeight-newBounds.y;
				}
			}
		}
		feedBack.translateToRelative(newBounds);
		feedBack.setBounds(newBounds);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#handleMove()
	 */
	protected boolean handleMove() {
		Point location = getLocation();
		switch (getRelativeCursorLocation(location)) {
		case NORTH:
		case SOUTH:
			setCursor(Cursors.SIZENS);
			break;
		case EAST:
		case WEST:
			setCursor(Cursors.SIZEWE);
			break;
		case NORTHWEST:
		case SOUTHEAST:
			setCursor(Cursors.SIZENWSE);
			break;
		case NORTHEAST:
		case SOUTHWEST:
			setCursor(Cursors.SIZENESW);
			break;
		case INSIDE:
			setCursor(Cursors.SIZEALL);
			break;
		default:
			setCursor(getDefaultCursor());

		}
		return true;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
	 */
	protected boolean handleButtonUp(int button) {
		handleFinished();
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#handleFinished()
	 */
	protected void handleFinished() {
		if (isInState(STATE_DRAG_IN_PROGRESS) || isInState(STATE_DRAG)) {
			
		} else {
			Point location = getLocation();
			getFeedBackFigure().setBounds(new Rectangle(location.x, location.y, 0,0));
		}
		setState(STATE_TERMINAL);
		resetFlags();
		zoomTo(getFeedBackFigure().getBounds().getCopy());
		
		//super.handleFinished();
	}
	
	/**
	 * @param copy
	 */
	private void zoomTo(Rectangle rect) {
		referenceBounds = rect.getCopy();
		GraphicalEditPart part = (GraphicalEditPart)getCurrentViewer().getContents();
		if (part != null) {
			part.getFigure().translateFromParent(referenceBounds);
			if (rect.isEmpty()) {
				//make sure that it didn't scale to a size greater than 0.
				referenceBounds.setSize(0,0);
			}
			if (this.currentManager != null)
				this.currentManager.zoomTo(referenceBounds);
			if (referenceBounds.isEmpty()) {
				//reset the figure so as to not jar the user.
				getFeedBackFigure().setBounds(rect);
			}
		}
	}
	boolean isNorth(int loc) {
		loc = (isInside(loc)) ? loc ^ INSIDE : loc;
		return (loc & NORTH) != 0;
	}
	
	boolean isEast(int loc) {
		loc = (isInside(loc)) ? loc ^ INSIDE : loc;
		return (loc & EAST)!= 0;
	}
	
	boolean isSouth(int loc) {
		loc = (isInside(loc)) ? loc ^ INSIDE : loc;
		return (loc & SOUTH) != 0;
	}
	
	boolean isWest(int loc) {
		loc = (isInside(loc)) ? loc ^ INSIDE : loc;
		return (loc & WEST) != 0;
	}
	
	boolean isInside(int loc) {
		return (loc & INSIDE) != 0;
	}
	
	private int getRelativeCursorLocation(Point location) {
		RectangleFigure feedBack = getFeedBackFigure();
		Rectangle bounds = feedBack.getBounds();
		location = location.getCopy();
		feedBack.translateToRelative(location);
		int result = OUTSIDE;
		if (feedBack.containsPoint(location)) {
			result |= INSIDE;
			//check to see the bounds to find out which 
			//cursor to use
			if (Math.abs(location.y - bounds.y)<=2) {
				result |= NORTH;
			} else if (Math.abs(location.y -(bounds.y+bounds.height-1))<=2) {
				result |= SOUTH;
			} 
			if (Math.abs(location.x - bounds.x)<=2) {
				result |= WEST;
			} else if (Math.abs(location.x - (bounds.x+bounds.width-1))<=2) {
				result |= EAST;
			}
		}
		return result;
	}
	
	private RectangleFigure getFeedBackFigure() {
		if (focusFigure == null) {
			focusFigure = new RectangleFigure();
			focusFigure.setForegroundColor(ColorConstants.red);
			focusFigure.setFill(false);
			focusFigure.setLineWidth(2);
			focusFigureListener = new FocusMoveListener();
			focusFigure.addFigureListener(focusFigureListener);
			focusFigure.setBounds(new Rectangle());
			addFeedback(focusFigure);
//			referenceBounds = focusFigure.getParent().getBounds().getCopy();
//			focusFigure.getParent().addFigureListener(this);
		}
		return focusFigure;
	}
	
	private TargetShape getTargetFigure() {
		if (target == null) {
			target = new TargetShape();
			target.setForegroundColor(ColorConstants.red);
			target.setFill(false);
			target.setBounds(new Rectangle(-10,-10,20,20));
			target.setLineWidth(2);
			addFeedback(target);
		}
		return target;
	}
	
	private void setTargetCenter(Point center) {
		Point newCenter = center.getCopy();
		newCenter.translate(-getTargetFigure().getSize().width/2, -getTargetFigure().getSize().height/2);
		getTargetFigure().setLocation(newCenter);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#deactivate()
	 */
	public void deactivate() {
		if (focusFigure != null) {
			focusFigure.removeFigureListener(focusFigureListener);
			removeFeedback(focusFigure);
			focusFigure = null;
		}
		if (target != null) {
			removeFeedback(target);
			target = null;
		}
		if (getCurrentViewer() != null) {
			getCurrentViewer().getControl().removeControlListener(this);
			if (getCurrentViewer().getContents() instanceof GraphicalEditPart) {
				((GraphicalEditPart)getCurrentViewer().getContents()).getFigure().removeFigureListener(this);
			}
		}
		if (this.currentManager != null) {
			this.currentManager.removeZoomListener(this);
			this.currentManager.getViewport().getHorizontalRangeModel().removePropertyChangeListener(this);
			this.currentManager.getViewport().getVerticalRangeModel().removePropertyChangeListener(this);
		}
		super.deactivate();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#activate()
	 */
	public void activate() {
	
		super.activate();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
	 */
	protected String getCommandName() {
		return RequestConstants.REQ_SELECTION;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#setViewer(org.eclipse.gef.EditPartViewer)
	 */
	public void setViewer(EditPartViewer viewer) {
		if (getCurrentViewer() != null) {
			getCurrentViewer().getControl().removeControlListener(this);
			GraphicalEditPart part = (GraphicalEditPart) getCurrentViewer().getContents();
			if (part != null) {
				IFigure referenceFigure = part.getContentPane();
				referenceFigure.removeFigureListener(this);
			}
		}
		super.setViewer(viewer);
				
		if (getCurrentViewer() != null) {
			getCurrentViewer().getControl().addControlListener(this);
			GraphicalEditPart part = (GraphicalEditPart) getCurrentViewer().getContents();
			if (part != null) {
				IFigure referenceFigure = part.getContentPane();
				referenceFigure.addFigureListener(this);
			}
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
	 */
	public void controlMoved(ControlEvent e) {}
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.ControlEvent)
	 */
	public void controlResized(ControlEvent e) {}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#viewerExited(org.eclipse.swt.events.MouseEvent, org.eclipse.gef.EditPartViewer)
	 */
	public void viewerExited(MouseEvent me, EditPartViewer viewer) {
//		//don't want to reset the viewer to null
//		if (viewer == getCurrentViewer()) {
//			handleViewerExited();
//			getCurrentInput().setInput(me);
//		}
		super.viewerExited(me, viewer);
	}
	/**
	 * @param zoomManager
	 */
	public void setZoomManager(ZoomManager zoomManager) {
		if (this.currentManager != null) {
			this.currentManager.removeZoomListener(this);
			this.currentManager.getViewport().getHorizontalRangeModel().removePropertyChangeListener(this);
			this.currentManager.getViewport().getVerticalRangeModel().removePropertyChangeListener(this);
		}
		this.currentManager = zoomManager;
		if (this.currentManager != null) {
			this.currentManager.addZoomListener(this);
			this.currentManager.getViewport().getHorizontalRangeModel().addPropertyChangeListener(this);
			this.currentManager.getViewport().getVerticalRangeModel().addPropertyChangeListener(this);
			//make sure that everthing is up-to-date.
			IFigure referenceFigure = getReferenceFigure();
			if (referenceFigure != null)
				getCurrentViewer().flush();
		}
		updateViewPort();
	}
	
	private IFigure getReferenceFigure() {
		GraphicalEditPart part = getReferenceEditPart();
		if (part != null)
			return part.getFigure();
		return null;
	}
	
	
	/**
	 * @return
	 */
	private GraphicalEditPart getReferenceEditPart() {
		if (getCurrentViewer() == null) return null;
		if (getCurrentViewer().getContents() instanceof GraphicalEditPart) {
			return (GraphicalEditPart) getCurrentViewer().getContents();
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.ZoomListener#zoomChanged(double)
	 */
	public void zoomChanged(double zoom) {
//		Viewport viewport = this.currentManager.getViewport();
//		if (Math.abs(1-zoom) < .1) {
//			//set the bounds of the rectangle to zeros
//			getFeedBackFigure().setBounds(new Rectangle());
//		}
//		//adjust the feedback according to the zoom.
//		int x = viewport.getHorizontalRangeModel().getValue();
//		int width = viewport.getHorizontalRangeModel().getExtent();
//		int y = viewport.getVerticalRangeModel().getValue();
//		int height = viewport.getVerticalRangeModel().getExtent();
//		Rectangle newBounds = new Rectangle(x,y,width,height);
//		//translate it.
		
		
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		updateViewPort();		
	}
	/**
	 * 
	 */
	private void updateViewPort() {
		if (this.currentManager == null) return;
		if (getCurrentViewer() == null) return;
		Viewport viewport = this.currentManager.getViewport();
		double zoom = this.currentManager.getZoom();
//		adjust the feedback according to the zoom.
		int x = viewport.getHorizontalRangeModel().getValue();
		int width = viewport.getHorizontalRangeModel().getExtent();
		int y = viewport.getVerticalRangeModel().getValue();
		int height = viewport.getVerticalRangeModel().getExtent();
		if (Math.abs(1-zoom) < .1) {
			//set the bounds of the rectangle to zeros
			x+= width/2;
			y+= height/2;
			width = 0;
			height = 0;
		}
		Rectangle newBounds = new Rectangle(x,y,width,height);
		GraphicalEditPart part = (GraphicalEditPart) getCurrentViewer().getContents();
		if (part != null) {
			IFigure referenceFigure = part.getContentPane();
			newBounds.scale(1/this.currentManager.getZoom());
			referenceFigure.translateToParent(newBounds);
			//adjust for the insets on the figure
			//newBounds.crop(getFeedBackFigure().getInsets());
			newBounds.shrink(1,1);//getFeedBackFigure().getLineWidth(), getFeedBackFigure().getLineWidth());
			getFeedBackFigure().setBounds(newBounds);
		}
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.FigureListener#figureMoved(org.eclipse.draw2d.IFigure)
	 */
	public void figureMoved(IFigure source) {
		updateViewPort();
		
	}
	

}
