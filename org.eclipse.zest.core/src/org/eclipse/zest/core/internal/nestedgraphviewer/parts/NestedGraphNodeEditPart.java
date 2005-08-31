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

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.NestedGraphViewerImpl;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.policies.GraphXYLayoutEditPolicy;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFigure;
import org.eclipse.mylar.zest.core.internal.viewers.figures.PlusMinusFigure;


/**
 * Extends GraphNodeEditPart to add functionality specific to nested graph nodes.
 * 
 * @author ccallendar
 */
public class NestedGraphNodeEditPart extends GraphNodeEditPart implements ActionListener {

	private boolean allowOverlap;
	private boolean enforceBounds;
	
	public NestedGraphNodeEditPart(boolean allowOverlap, boolean enforceBounds) {
		super();
		this.allowOverlap = allowOverlap;
		this.enforceBounds = enforceBounds;
	}
	
	/**
	 * Gets the bounds of the figure and translates it to absolute coordinates.
	 * @return Rectangle in absolute coordinates
	 */
	public Rectangle getScreenBounds() {
		Rectangle bounds = getFigure().getBounds().getCopy();
		Point p = bounds.getLocation();
		getFigure().translateToAbsolute(p);
		bounds.setLocation(p);
		return bounds;
	}
	
	protected void createEditPolicies() {
		super.createEditPolicies();

		installEditPolicy(EditPolicy.LAYOUT_ROLE,  new GraphXYLayoutEditPolicy(allowOverlap, enforceBounds));
	}
	
	//TODO rename to getNestedGraphModel() ?
	public NestedGraphModelNode getCastedModel() {
		return (NestedGraphModelNode)getModel();
	}
	
	protected IFigure createFigureForModel() {
		IFigure figure;
		if (getCastedModel() != null) {
			NestedGraphModelNode node = getCastedModel();
			Label label = new Label(node.getText(), node.getImage());
			label.setFont(node.getFont());
			label.setForegroundColor(node.getForegroundColor());
			PlusMinusFigure plusMinus = null;
			if (node.hasChildren()) {
				plusMinus = new PlusMinusFigure(NestedGraphModelNode.PLUS_SIZE);
				plusMinus.setBorder(new MarginBorder(2));
				plusMinus.addActionListener(this);
			}
			NestedFigure nestedFigure = new NestedFigure(label, plusMinus);
			nestedFigure.setBackgroundColor(node.getBackgroundColor());
			nestedFigure.setNestedFiguresVisible(node.getChildrenVisible());
			nestedFigure.setScale(node.getScale());
			figure = nestedFigure;
		} else {
			figure = super.createFigureForModel();
		}

		return figure;
	}
	
	/**
	 * Returns a list of the children GraphModelNode object.
	 * @see org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return getCastedModel().getChildren();
	}

	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.springgraphviewer.parts.GraphNodeEditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request req) {
		if (REQ_OPEN.equals(req.getType())) {
			//DebugPrint.println("performRequest() Bounds: " + getFigure().getBounds() + "\t " + getCastedModel().getLocation() + ", " + getCastedModel().getSize());
			//DebugPrint.println("performRequest() Scale: " + getCastedModel().getScale() + ", " + ((NestedFigure)getFigure()).getScale());
			//if(true) return;
			
			// If there are children, then go down into that layer
			NestedGraphModel model = getGraphModel(); 
			if ((model != null) && (getCastedModel().getChildren().size() > 0)) {
				model.setCurrentNode(getCastedModel());
				if (getViewer() instanceof NestedGraphViewerImpl) {
					((NestedGraphViewerImpl)getViewer()).fireModelUpdate();
				}				
			}
		} else {	
			super.performRequest(req);
		}
	}
	
	/**
	 * Handles the {@link org.eclipse.mylar.zest.core.internal.viewers.figures.PlusMinusFigure} click events.
	 * Toggles whether the children are shown.
	 * @see org.eclipse.draw2d.ActionListener#actionPerformed(org.eclipse.draw2d.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event) {
		NestedFigure fig = (NestedFigure)getFigure();
		boolean vis = !fig.getScaledFigure().isVisible();
		fig.setNestedFiguresVisible(vis);
		NestedGraphModelNode node = getCastedModel();
		node.setChildrenVisible(vis);
		
		// check connections
		if (getViewer() instanceof NestedGraphViewerImpl) {
			NestedGraphViewerImpl viewer = (NestedGraphViewerImpl)getViewer();
			viewer.hideConnections();
			viewer.expandTreeItem(getCastedModel().getData(), vis);
			if (vis) {
				// do a grid layout on the children using the full area and then scale
				Rectangle bounds = getGraphModel().getMainArea();
				viewer.doGridLayout(node, bounds.width, bounds.height);
				viewer.checkScaling(node);
				if (node.getScale() != fig.getScale()) {
					fig.setScale(node.getScale());
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.springgraphviewer.parts.GraphNodeEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
	}
	
	/**
	 * Gets the NestedGraphModel from the NestedGraphModelNode 
	 * @return NestedGraphModel
	 */
	private NestedGraphModel getGraphModel() {
		NestedGraphModel model = (NestedGraphModel)getCastedModel().getGraphModel();
		return model;
	}
	
}
