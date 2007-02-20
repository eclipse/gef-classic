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
package org.eclipse.mylar.zest.core.internal.graphviewer.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;
import org.eclipse.mylar.zest.core.internal.viewers.figures.GraphLabel;
import org.eclipse.mylar.zest.core.internal.viewers.figures.RoundedChopboxAnchor;
import org.eclipse.mylar.zest.core.internal.viewers.trackers.SingleSelectionTracker;


/**
 * The edit part for the LayoutNode model.
 * @author Chris Callendar
 */
public class GraphNodeEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener, NodeEditPart {
	private IFigure toolTip;

	protected ConnectionAnchor anchor;
//	@tag zest(bug(152180-SelfLoops)) : New anchor required so that the loops aren't hidden by the node.
	protected ConnectionAnchor loopAnchor;
	/**
	 * GraphNodeEditPart constructor.
	 */
	public GraphNodeEditPart() {
		super();
	}

	/**
	 * Upon activation, attach to the model element as a property change
	 * listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate();
			((IGraphItem) getModel()).addPropertyChangeListener(this);
		}
	}

	/**
	 * Upon deactivation, detach from the model element as a property change
	 * listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((IGraphItem) getModel()).removePropertyChangeListener(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		// allow removal of the associated model element
		// installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeComponentEditPolicy());
		
		// allow the creation of connections and the reconnection of connections between nodes
		//installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GraphNodeEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		IFigure f = createFigureForModel();
		f.addLayoutListener(LayoutAnimator.getDefault());
		return f;
	}

	/**
	 * Return a IFigure depending on the instance of the current model element.
	 * This allows this EditPart to be used for both sublasses of Shape.
	 */
	protected IFigure createFigureForModel() {
		IFigure figure;
		if (getModel() instanceof IGraphModelNode) {
			IGraphModelNode node = getCastedModel();
			boolean cacheLabel = ((IGraphModelNode)getModel()).cacheLabel();
			GraphLabel label = new GraphLabel(node.getText(), node.getImage(), cacheLabel);
			label.setForegroundColor(node.getForegroundColor());
			label.setBackgroundColor(node.getBackgroundColor());
			label.setFont(node.getFont());
			Dimension d = label.getSize();
			node.setSizeInLayout(d.width, d.height);			
			figure = label;
			if ( getCastedModel().getTooltip() == null ) {
				toolTip = new Label();
				((Label)toolTip).setText(node.getText());
			}
			else {
				toolTip= getCastedModel().getTooltip();
			}
			figure.setToolTip(toolTip);
		} else {
			// if Shapes gets extended the conditions above must be updated
			throw new IllegalArgumentException("Unexpected model when creating a figure");
		}
		return figure;
	}

	/**
	 * Gets a drag tracker which only allows one node to be selected at a time.
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		return new SingleSelectionTracker(this);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request req) {
		if (REQ_OPEN.equals(req.getType())) {
			// TODO handle double-click
		}
		super.performRequest(req);
	}

//	@tag zest(bug(152180-SelfLoops)) : New anchor required so that the loops aren't hidden by the node.
	protected ConnectionAnchor getLoopAnchor() {
		if (loopAnchor == null) {
			loopAnchor = new LoopAnchor(getFigure());
		}
		return loopAnchor;
	}
	
//	@tag zest(bug(152180-SelfLoops)) : New anchor required so that the loops aren't hidden by the node.
	protected ConnectionAnchor getDefaultConnectionAnchor() {
		if (anchor == null) {
			if (getModel() instanceof IGraphModelNode) {
				IFigure figure = getFigure();
				// use a rounded chopbox anchor for graph label figures (rounded corners)
				if (figure instanceof GraphLabel) {
					GraphLabel graphFigure = (GraphLabel) figure;
					anchor = new RoundedChopboxAnchor(graphFigure, graphFigure.getArcWidth() / 2);
				} else {
					anchor = new ChopboxAnchor(figure);
				}
			} else {
				throw new IllegalArgumentException("Unexpected model when creating an anchor");
			}
		}
		return anchor;
	}
	
		
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		
		
		String prop = evt.getPropertyName();
		if ( IGraphModelNode.FORCE_REDRAW.equals(prop)) {
			refreshVisuals();
			refreshChildren();
			refreshColors();
			getCastedModel().highlight();
			getCastedModel().unhighlight();
		}
		else if (IGraphModelNode.LOCATION_PROP.equals(prop) || IGraphModelNode.SIZE_PROP.equals(prop)) {
			refreshVisuals();
		} else if (IGraphModelNode.SOURCE_CONNECTIONS_PROP.equals(prop)) {
			refreshSourceConnections();
		} else if (IGraphModelNode.TARGET_CONNECTIONS_PROP.equals(prop)) {
			refreshTargetConnections();
		} else if (IGraphModelNode.HIGHLIGHT_PROP.equals(prop)) {
			refreshColors();
		} else if (IGraphModelNode.COLOR_BG_PROP.equals(prop)) {
			refreshColors();
		} else if (IGraphModelNode.COLOR_FG_PROP.equals(prop)) {
			refreshColors();
		}
		else if (IGraphModelNode.COLOR_BD_PROP.equals(prop)) {
			refreshColors();
		}
		else if ( IGraphModelNode.TOOLTIP_PROP.equals(prop)) {
			IFigure tooltip = (IFigure) evt.getNewValue();
			if ( tooltip == null ) {
				this.toolTip = new Label();
				((Label)this.toolTip).setText(getCastedModel().getText());
			}
			else {
				this.toolTip = tooltip;
			}
			figure.setToolTip(tooltip);
			refreshVisuals();
		}
		else if ( IGraphModelNode.BRING_TO_FRONT.equals(prop) ) {
			IFigure figure = getFigure();
			IFigure parent = figure.getParent();
			parent.remove(figure);
			parent.add(figure);
			
		} else if (IGraphItem.VISIBLE_PROP.equals(prop)) {
			getFigure().setVisible(((Boolean)evt.getNewValue()).booleanValue());
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		IGraphModelNode node = getCastedModel();
		Point loc = node.getLocation();
		Dimension size = node.getSize();
		Rectangle bounds = new Rectangle(loc, size);
		((GraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), bounds);
		refreshColors(); 
	}

	/**
	 * Refreshes the figures colors from the model.  This includes the border color and width.
	 */
	protected void refreshColors() {
		IFigure figure = getFigure();
		IGraphModelNode model = getCastedModel();
		figure.setForegroundColor(model.getForegroundColor());
		figure.setBackgroundColor(model.getBackgroundColor());
		
		if (figure instanceof GraphLabel) {
			GraphLabel label = (GraphLabel) figure;
			label.setBorderColor(model.getBorderColor());
			label.setBorderWidth(model.getBorderWidth());
		}	
	}
	
	/**
	 * Returns the model casted into a GraphModelNode.
	 * @return the casted GraphModelNode model
	 */
	private IGraphModelNode getCastedModel() {
		return (IGraphModelNode)getModel();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected List getModelSourceConnections() {
		if (getCastedModel() == null) return Collections.EMPTY_LIST;
		return getCastedModel().getSourceConnections();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	protected List getModelTargetConnections() {
		if (getCastedModel() == null) return Collections.EMPTY_LIST;
		return getCastedModel().getTargetConnections();
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
//		@tag zest(bug(152180-SelfLoops)) : New anchor required so that the loops aren't hidden by the node.
		if (connection.getSource() == connection.getTarget()) {
			return getLoopAnchor();
		}
		return getDefaultConnectionAnchor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
//		@tag zest(bug(152180-SelfLoops)) : New anchor required so that the loops aren't hidden by the node.
		if (connection.getSource() == connection.getTarget()) {
			return getLoopAnchor();
		}
		return getDefaultConnectionAnchor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getDefaultConnectionAnchor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getDefaultConnectionAnchor();
	}
}
