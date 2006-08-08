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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedPane;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.policies.NullLayoutEditPolicy;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFigure;
import org.eclipse.swt.widgets.Display;


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
	 * A class that takes a series of figures, and animates them to a final 
	 * position.
	 * @author Del Myers
	 *
	 */
	//@tag bug(152613-Client-Supplier(fix))
	//@tag bug(150585-TopArcs(fix))
	private class FigureKeyFrameAnimator {
		private ArrayList figures;
		private ArrayList startBounds;
		private ArrayList endBounds;
		private ArrayList figureParents;
		
		FigureKeyFrameAnimator() {
			figures = new ArrayList();
			endBounds = new ArrayList();
			figureParents = new ArrayList();
			startBounds = new ArrayList();
		}
		
		/**
		 * Adds figures to be animated. The figures should be added in the
		 * state that the animation should start at. The endBounds is where the
		 * figures should end up, in absolute coordinates.
		 * @param figures the figures to move.
		 * @param endBounds where the figures should end up, in absolute coordinates.
		 */
		public void addFigures(IFigure[] figuresArray, Rectangle[] endBounds) {
			if (figuresArray.length != endBounds.length) {
				throw new IllegalArgumentException("Arrays of unequal length");
			}

			for (int i = 0; i < figuresArray.length; i++) {
				IFigure figure = figuresArray[i];
				this.endBounds.add(endBounds[i]);
				figures.add(figure);
				figureParents.add(figure.getParent());
				startBounds.add(figure.getBounds());
			}
		}
		
		/**
		 * Starts the animation, using the given number of steps. The current thread
		 * will suspend until the animation is complete.
		 * @param steps the number of steps to take.
		 */
		public void start(final int steps) {
			Display.getCurrent().syncExec(new Runnable() {
				public void run() {
					//@tag bug(153169-OccludedArcs(fix)) : use the animation layer
					Layer feedBack = (Layer) getLayer(NestedGraphRootEditPart.ANIMATION_LAYER);
					feedBack.setLayoutManager(new XYLayout());
					//startBounds.clear();
					for (int i = 0; i < figures.size(); i++) {
						IFigure figure = (IFigure) figures.get(i);
						Rectangle figureBounds = figure.getBounds();
						figure.translateToAbsolute(figureBounds);
						feedBack.translateToRelative(figureBounds);
						//startBounds.add(figureBounds);
						figure.getParent().remove(figure);
						feedBack.add(figure);
						figure.setBounds(figureBounds);
					}
					
					for (int step = 0; step < steps; step++) {
						for (int i = 0; i < figures.size(); i++) {
							IFigure figure = (IFigure) figures.get(i);
							Rectangle bounds = (Rectangle)endBounds.get(i);
							feedBack.translateToRelative(bounds);
							Rectangle cBounds = (Rectangle) startBounds.get(i);
							int newX = (int)(((double)(step+1)*(bounds.x - cBounds.x))/((double)steps)) + cBounds.x;
							int newY = (int)(((double)(step+1)*(bounds.y - cBounds.y))/((double)steps)) + cBounds.y;
							int newHeight = (int)(((double)(step+1)*(bounds.height - cBounds.height))/((double)steps)) + cBounds.height;
							int newWidth = (int)(((double)(step+1)*(bounds.width - cBounds.width))/((double)steps)) + cBounds.width;
							Rectangle newBounds = new Rectangle(newX, newY, newWidth, newHeight);
							figure.setBounds(newBounds);
							figure.setOpaque(true);
							figure.invalidate();
							figure.revalidate();
						}
						getViewer().flush();
						sleep(25);
					}
					
				}
			});

		}
		/**
		 * Clears all data from this animator. Normally should be called after
		 * start().
		 */
		public void clear() {
			for (int i = 0; i < figures.size(); i++) {
				Layer feedBack = (Layer) getLayer(NestedGraphRootEditPart.ANIMATION_LAYER);
				IFigure figure = (IFigure) figures.get(i);
				if (figure.getParent() == feedBack) {
					feedBack.remove(figure);
					Rectangle bounds = figure.getBounds();
					IFigure parent = (IFigure) figureParents.get(i);
					if (parent != null) {
						parent.add(figure);
						figure.translateToRelative(bounds);
						figure.setBounds(bounds);
						figure.invalidateTree();
					}
				}
			}
			figures.clear();
			endBounds.clear();
			figureParents.clear();
			startBounds.clear();
		}
	}
	
	/**
	 * Initializes the edit part.
	 * @param allowOverlap If nodes are allowed to overlap
	 * @param enforceBounds If nodes can be moved outside the bounds  If this is set to false
	 * then scrollbars will appear.
	 */
	public NestedGraphEditPart( ) {
		super();
		supplierPane = new NestedPane(NestedPane.SUPPLIER_PANE);
		clientPane = new NestedPane(NestedPane.CLIENT_PANE);
		mainNestedPane = new NestedPane(NestedPane.MAIN_PANE);
	
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new NullLayoutEditPolicy());
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
		return getMainFigure().getClientArea().getCopy();
	}
	
	public Rectangle getClientPaneArea() {
		return getClientFigure().getClientArea().getCopy();
	}
	
	public Rectangle getSupplierPaneArea() {
		return getSupplierFigure().getClientArea().getCopy();
	}
	
	public NestedPaneAreaEditPart getClientPane() {
		return (NestedPaneAreaEditPart)getChildren().get(0);
	}
	
	protected IFigure getClientFigure() {
		return (IFigure)getFigure().getChildren().get(2);
	}
	
	protected IFigure getMainFigure() {
		return (IFigure)getFigure().getChildren().get(1);
	}
	
	protected IFigure getSupplierFigure() {
		return (IFigure)getFigure().getChildren().get(0);
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
		
		//Rectangle maxBounds = getMainArea();
		Rectangle startBounds = editPart.getAbsoluteBounds();
		getLayer(LayerConstants.FEEDBACK_LAYER).translateToRelative(startBounds);
		//@tag bug(153170-FilterArcs(fix))
		//@tag bug(153169-OccludedArcs(fix)) : filter the arcs before animating.
		filterConnections(editPart.getCastedModel(), true);
		doExpandZoom(startBounds, 10, editPart);
	}
	
	
	/**
	 * If <code>filter</code> is true filters out the connections that don't have 
	 * an end in a node that has <code>node</code> as an ancestor. Otherwise, 
	 * displays all nodes. 
	 * @param node the node to filter against
	 * @param filter whether or not the connections should be filtered.
	 */
	//@tag bug(153170-FilterArcs(fix)) : make sure that the arcs are filtered out if they should be inivisible.
	public void filterConnections(NestedGraphModelNode node, boolean filter) {
		List connections = node.getGraphModel().getConnections();
		for (Iterator i = connections.iterator(); i.hasNext();) {
			GraphModelConnection conn = (GraphModelConnection) i.next();
			GraphicalEditPart part = (GraphicalEditPart) conn.getEditPart();
			if (part != null) {
				if (!filter) {
					part.getFigure().setVisible(true);
					continue;
				}
				NestedGraphModelNode source = (NestedGraphModelNode) conn.getSource();
				NestedGraphModelNode dest = (NestedGraphModelNode) conn.getDestination();
				int sourceRel = node.getRelationshipBetweenNodes(source);
				int destRel = node.getRelationshipBetweenNodes(dest);
				boolean visible = (
					(sourceRel == NestedGraphModelNode.ANCESTOR) ||
					(sourceRel == NestedGraphModelNode.SAME_NODE) ||
					(destRel == NestedGraphModelNode.ANCESTOR) ||
					(destRel == NestedGraphModelNode.SAME_NODE)
				);
				part.getFigure().setVisible(visible);
			}
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
		
		//@tag bug(153169-OccludedArcs(fix)) : using the animation layer requires start bounds to be relative.
		Rectangle maxBounds = new Rectangle(0,0,500,500);
		Rectangle startBounds = editPart.getAbsoluteBounds();
		doCollapseZoom(maxBounds, startBounds, 10, editPart);
	}
	
	/**
	 * Draws an expanding dotted rectangle figure around the node to give the impression
	 * of zooming in.  The dotted rectangle starts at the center of the node.
	 */
	//@tag bug(152613-Client-Supplier(fix)) : change this to use the FigureKeyFrameAnimator
	//@tag bug(153169-OccludedArcs(fix)) : using FigureKeyFrameAnimator forces animation onto the animation layer
	private void doCollapseZoom(Rectangle startBounds, Rectangle endBounds, final int STEPS, NestedGraphNodeEditPart node) {
		NestedFigure fig = (NestedFigure) node.getFigure();
		FigureKeyFrameAnimator animator = new FigureKeyFrameAnimator();
		fig.setBounds(startBounds);
		animator.addFigures(new IFigure[] {fig}, new Rectangle[] {endBounds});
		animator.start(STEPS);
		animator.clear();
		/*if (STEPS > 0) {
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
		*/
	}
	
	/**
	 * Draws an expanding dotted rectangle figure around the node to give the impression
	 * of zooming in.  The dotted rectangle starts at the center of the node.
	 */
	private void doExpandZoom(Rectangle startBounds, final int STEPS, NestedGraphNodeEditPart node) {
		NestedFigure fig = (NestedFigure)node.getFigure();
		FigureKeyFrameAnimator animator = new FigureKeyFrameAnimator();
		addChildrenToAnimator(node, animator);
		animator.start(STEPS);
		
		FigureKeyFrameAnimator focusAnimator = new FigureKeyFrameAnimator();
		Rectangle endBounds = getMainArea();
		fig.setOpaque(true);
		
		//fig.translateToRelative(bounds);
		//getLayer(LayerConstants.FEEDBACK_LAYER).add(fig);
		//fig.setBounds(startBounds);
		focusAnimator.addFigures(new IFigure[]{fig}, new Rectangle[]{endBounds});
		focusAnimator.start(STEPS);
		animator.clear();
		focusAnimator.clear();
		/*if (STEPS > 0) {
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
		}*/
		
	}
	
	/**
	 * Animates the children of the given node to place them into the correct panes.
	 * @param node the node to get the children of.
	 */
	private void addChildrenToAnimator(final NestedGraphNodeEditPart node, FigureKeyFrameAnimator animator) {
		List connectedTo = node.getCastedModel().getNodesConnectedTo();
		GraphicalEditPart[] editPartArray = new GraphicalEditPart[connectedTo.size()];
		IFigure[] figureArray = new IFigure[editPartArray.length];
		for (int i = 0; i < connectedTo.size(); i++) {
			NestedGraphModelNode to = (NestedGraphModelNode)connectedTo.get(i);
			editPartArray[i] = ((GraphicalEditPart)to.getEditPart());
			figureArray[i] = editPartArray[i].getFigure();
		}
				
		Arrays.sort(figureArray, FigureGridLayout.SimpleComparator);
		Rectangle[] boundss = FigureGridLayout.layoutInBounds(getClientPaneArea(), figureArray);
		animator.addFigures(figureArray, boundss);
		
		List connectedFrom = node.getCastedModel().getNodesConnectedFrom();
		editPartArray = new GraphicalEditPart[connectedFrom.size()];
		figureArray = new IFigure[editPartArray.length];
		for (int i = 0; i < connectedFrom.size(); i++) {
			NestedGraphModelNode From = (NestedGraphModelNode)connectedFrom.get(i);
			editPartArray[i] = ((GraphicalEditPart)From.getEditPart());
			figureArray[i] = editPartArray[i].getFigure();
		}
				
		Arrays.sort(figureArray, FigureGridLayout.SimpleComparator);
		boundss = FigureGridLayout.layoutInBounds(getSupplierPaneArea(), figureArray);
		animator.addFigures(figureArray, boundss);
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
