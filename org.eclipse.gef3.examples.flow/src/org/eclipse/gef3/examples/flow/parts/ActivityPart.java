/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.examples.flow.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2dl.ConnectionAnchor;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.draw2dl.graph.CompoundDirectedGraph;
import org.eclipse.draw2dl.graph.Node;
import org.eclipse.draw2dl.graph.Subgraph;
import org.eclipse.gef3.ConnectionEditPart;
import org.eclipse.gef3.EditPolicy;
import org.eclipse.gef3.GraphicalEditPart;
import org.eclipse.gef3.NodeEditPart;
import org.eclipse.gef3.Request;
import org.eclipse.gef3.RequestConstants;
import org.eclipse.gef3.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef3.examples.flow.model.Activity;
import org.eclipse.gef3.examples.flow.model.FlowElement;
import org.eclipse.gef3.examples.flow.policies.ActivityDirectEditPolicy;
import org.eclipse.gef3.examples.flow.policies.ActivityEditPolicy;
import org.eclipse.gef3.examples.flow.policies.ActivityNodeEditPolicy;
import org.eclipse.gef3.examples.flow.policies.ActivitySourceEditPolicy;
import org.eclipse.gef3.tools.DirectEditManager;
import org.eclipse.gef3.editparts.AbstractEditPart;

/**
 * @author hudsonr Created on Jun 30, 2003
 */
public abstract class ActivityPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener, NodeEditPart {

	protected DirectEditManager manager;

	/**
	 * @see org.eclipse.gef3.EditPart#activate()
	 */
	public void activate() {
		super.activate();
		getActivity().addPropertyChangeListener(this);
	}

	protected void applyGraphResults(CompoundDirectedGraph graph, Map map) {
		Node n = (Node) map.get(this);
		getFigure().setBounds(new Rectangle(n.x, n.y, n.width, n.height));

		for (int i = 0; i < getSourceConnections().size(); i++) {
			TransitionPart trans = (TransitionPart) getSourceConnections().get(
					i);
			trans.applyGraphResults(graph, map);
		}
	}

	public void contributeEdgesToGraph(CompoundDirectedGraph graph, Map map) {
		List outgoing = getSourceConnections();
		for (int i = 0; i < outgoing.size(); i++) {
			TransitionPart part = (TransitionPart) getSourceConnections()
					.get(i);
			part.contributeToGraph(graph, map);
		}
		for (int i = 0; i < getChildren().size(); i++) {
			ActivityPart child = (ActivityPart) children.get(i);
			child.contributeEdgesToGraph(graph, map);
		}
	}

	public abstract void contributeNodesToGraph(CompoundDirectedGraph graph,
			Subgraph s, Map map);

	/**
	 * @see AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new ActivityNodeEditPolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE,
				new ActivitySourceEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ActivityEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new ActivityDirectEditPolicy());
	}

	/**
	 * @see org.eclipse.gef3.EditPart#deactivate()
	 */
	public void deactivate() {
		super.deactivate();
		getActivity().removePropertyChangeListener(this);
	}

	/**
	 * Returns the Activity model associated with this EditPart
	 * 
	 * @return the Activity model
	 */
	protected Activity getActivity() {
		return (Activity) getModel();
	}

	/**
	 * @see AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected List getModelSourceConnections() {
		return getActivity().getOutgoingTransitions();
	}

	/**
	 * @see AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	protected List getModelTargetConnections() {
		return getActivity().getIncomingTransitions();
	}

	abstract int getAnchorOffset();

	/**
	 * @see NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef3.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return new BottomAnchor(getFigure(), getAnchorOffset());
	}

	/**
	 * @see org.eclipse.gef3.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef3.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new BottomAnchor(getFigure(), getAnchorOffset());
	}

	/**
	 * @see NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef3.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return new TopAnchor(getFigure(), getAnchorOffset());
	}

	/**
	 * @see org.eclipse.gef3.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef3.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new TopAnchor(getFigure(), getAnchorOffset());
	}

	protected void performDirectEdit() {
	}

	/**
	 * @see org.eclipse.gef3.EditPart#performRequest(org.eclipse.gef3.Request)
	 */
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
			performDirectEdit();
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (FlowElement.CHILDREN.equals(prop))
			refreshChildren();
		else if (FlowElement.INPUTS.equals(prop))
			refreshTargetConnections();
		else if (FlowElement.OUTPUTS.equals(prop))
			refreshSourceConnections();
		else if (Activity.NAME.equals(prop))
			refreshVisuals();

		// Causes Graph to re-layout
		((GraphicalEditPart) (getViewer().getContents())).getFigure()
				.revalidate();
	}

	/**
	 * @see AbstractGraphicalEditPart#setFigure(org.eclipse.draw2dl.IFigure)
	 */
	protected void setFigure(IFigure figure) {
		figure.getBounds().setSize(0, 0);
		super.setFigure(figure);
	}

	/**
	 * @see AbstractEditPart#toString()
	 */
	public String toString() {
		return getModel().toString();
	}

}
