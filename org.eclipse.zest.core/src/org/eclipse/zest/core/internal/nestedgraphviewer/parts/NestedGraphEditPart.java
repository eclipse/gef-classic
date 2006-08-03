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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedPane;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.NestedGraphViewerImpl;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFigure;


/**
 * Extends GraphEditPart to allow moving and resizing of nodes.
 * 
 * @author Chris Callendar
 */
public class NestedGraphEditPart extends GraphEditPart  {
	
	NestedPane supplierPane = null;
	NestedPane clientPane = null;
	NestedPane mainNestedPane = null;
	
	
	/**
	 * Initializes the edit part.
	 * @param allowOverlap If nodes are allowed to overlap
	 * @param enforceBounds If nodes can be moved outside the bounds  If this is set to false
	 * then scrollbars will appear.
	 */
	public NestedGraphEditPart( ) {
		super();
		supplierPane = new NestedPane(NestedPaneArea.SUPPLIER_PANE);
		clientPane = new NestedPane(NestedPaneArea.CLIENT_PANE);
		mainNestedPane = new NestedPane(NestedPaneArea.MAIN_PANE);
		
		
		
	}	
	
	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate();
			((GraphItem)getCastedModel().getRootNode()).addPropertyChangeListener(this);
		}
	}	
	
	/**
	 * Upon deactivation, detach from the model element as a property change listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((GraphItem)getCastedModel().getRootNode()).removePropertyChangeListener(this);
		}
	}	

	
	
	//TODO: should we change this to getNestedGraphModel?
	// and make the same method in GraphEditPart called getGraphModel?
	public NestedGraphModel getCastedModel() {
		return (NestedGraphModel)getModel();
	}	
	
	public void setModel(Object model) {

		super.setModel(model);
		supplierPane.setModel(getCastedModel());
		clientPane.setModel(getCastedModel());
		mainNestedPane.setModel(getCastedModel());
		
	}

	
	public Rectangle getMainArea() {
		return getMainFigure().getClientArea();
	}
	
	
	protected IFigure getClientFigure() {
		return (IFigure)getFigure().getChildren().get(0);
	}
	
	protected IFigure getMainFigure() {
		return (IFigure)getFigure().getChildren().get(1);
	}
	
	protected IFigure getSupplierFigure() {
		return (IFigure)getFigure().getChildren().get(2);
	}
	
	/**
	 * Creates a NestedFreeformLayer which contains the root NestedFigure.
	 * This NestedFigure will have an up button in the top left if the
	 * current node isn't the root node.
	 */
	static int layoutCount = 0;
	protected IFigure createFigure() {	
		Figure figure = new Figure();
		figure.setLayoutManager(new AbstractLayout() {
			
			protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
				// TODO Auto-generated method stub
				return null;
			}

			public void layout(IFigure container) {
				List l = container.getChildren();
				IFigure f1 = (IFigure) l.get(0);
				IFigure f2 = (IFigure) l.get(1);
				IFigure f3 = (IFigure) l.get(2);
				Rectangle parentBounds = container.getBounds();
				
				f1.setBounds(new Rectangle(0,0, parentBounds.width,parentBounds.height/4));
				f2.setBounds(new Rectangle(0,parentBounds.height/4, parentBounds.width,parentBounds.height/2));
				f3.setBounds(new Rectangle(0,3*parentBounds.height/4, parentBounds.width,parentBounds.height/4));
			}
			
		} );
		figure.setOpaque(true);
		return figure;
	}		
	
	
	protected List getModelChildren() {
		List list = new ArrayList();
		list.add( supplierPane );
		list.add( mainNestedPane );
		list.add( clientPane );
		return list;
		//return super.getModelChildren();
	}
	
	
	/**
	 * Zooms in on the given node.
	 * The type of zooming (real, fake, expand) depends on the zoomStyle.
	 * @param editPart
	 */
	public void zoomInOnNode(NestedGraphNodeEditPart editPart) {
		if (editPart == null)
			return;
		
		Rectangle maxBounds = getMainArea();
		Rectangle startBounds = editPart.getAbsoluteBounds();
		getMainFigure().translateToRelative(startBounds);

		doExpandZoom(startBounds, maxBounds, 10, editPart);
	}
	
	
	/**
	 * Zooms out on the given node.  
	 * The type of zooming (real, fake, collapse) depends on the zoomStyle.
	 * @param editPart
	 */
	
	public void zoomOutOnNode( NestedGraphNodeEditPart editPart) {
		if (editPart == null)
			return;
		
		Rectangle maxBounds = getMainArea();
		Rectangle startBounds = editPart.getAbsoluteBounds();
		doCollapseZoom(maxBounds, startBounds, 10, editPart);
	}
	
	/**
	 * Draws an expanding dotted rectangle figure around the node to give the impression
	 * of zooming in.  The dotted rectangle starts at the center of the node.
	 */
	private void doCollapseZoom(Rectangle startBounds, Rectangle endBounds, final int STEPS, NestedGraphNodeEditPart node) {
		NestedFigure fig = (NestedFigure) node.getFigure();
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
			getMainFigure().add(fig);
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
			
			getMainFigure().remove(fig);
			getMainFigure().translateToAbsolute(endBounds);
			fig.translateToRelative(endBounds);
			fig.setBounds(endBounds);
			getViewer().flush();
			
		}
		
	}
	
	/**
	 * Draws an expanding dotted rectangle figure around the node to give the impression
	 * of zooming in.  The dotted rectangle starts at the center of the node.
	 */
	private void doExpandZoom(Rectangle startBounds, Rectangle endBounds, final int STEPS, NestedGraphNodeEditPart node) {
		NestedFigure fig = (NestedFigure)node.getFigure();
		
		
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
			getMainFigure().add(fig);
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
			
			fig.setBounds(endBounds);
			((NestedGraphViewerImpl)getViewer()).flush();
			//getMainFigure().remove(fig);
			//parent.add(fig);
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
