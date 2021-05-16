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

import java.util.List;
import java.util.Map;

import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.Label;
import org.eclipse.draw2dl.geometry.Insets;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.graph.CompoundDirectedGraph;
import org.eclipse.draw2dl.graph.Subgraph;
import org.eclipse.gef3.EditPolicy;
import org.eclipse.gef3.NodeEditPart;
import org.eclipse.gef3.Request;
import org.eclipse.gef3.RequestConstants;
import org.eclipse.gef3.examples.flow.figures.SubgraphFigure;
import org.eclipse.gef3.examples.flow.model.StructuredActivity;
import org.eclipse.gef3.examples.flow.policies.ActivityContainerEditPolicy;
import org.eclipse.gef3.examples.flow.policies.ActivityContainerHighlightEditPolicy;
import org.eclipse.gef3.examples.flow.policies.ActivityEditPolicy;
import org.eclipse.gef3.examples.flow.policies.ActivityNodeEditPolicy;
import org.eclipse.gef3.examples.flow.policies.StructuredActivityDirectEditPolicy;
import org.eclipse.gef3.examples.flow.policies.StructuredActivityLayoutEditPolicy;
import org.eclipse.gef3.requests.DirectEditRequest;
import org.eclipse.gef3.editparts.AbstractEditPart;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * @author hudsonr Created on Jun 30, 2003
 */
public abstract class StructuredActivityPart extends ActivityPart implements
		NodeEditPart {

	static final Insets PADDING = new Insets(8, 6, 8, 6);
	static final Insets INNER_PADDING = new Insets(0);

	protected void applyChildrenResults(CompoundDirectedGraph graph, Map map) {
		for (int i = 0; i < getChildren().size(); i++) {
			ActivityPart part = (ActivityPart) getChildren().get(i);
			part.applyGraphResults(graph, map);
		}
	}

	protected void applyGraphResults(CompoundDirectedGraph graph, Map map) {
		applyOwnResults(graph, map);
		applyChildrenResults(graph, map);
	}

	protected void applyOwnResults(CompoundDirectedGraph graph, Map map) {
		super.applyGraphResults(graph, map);
	}

	/**
	 * @see org.eclipse.gef3.examples.flow.parts.ActivityPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new ActivityNodeEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ActivityEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE,
				new ActivityContainerHighlightEditPolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE,
				new ActivityContainerEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new StructuredActivityLayoutEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new StructuredActivityDirectEditPolicy());
	}

	public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s,
			Map map) {
		GraphAnimation.recordInitialState(getContentPane());
		Subgraph me = new Subgraph(this, s);
		// me.rowOrder = getActivity().getSortIndex();
		me.outgoingOffset = 5;
		me.incomingOffset = 5;
		IFigure fig = getFigure();
		if (fig instanceof SubgraphFigure) {
			me.width = fig.getPreferredSize(me.width, me.height).width;
			int tagHeight = ((SubgraphFigure) fig).getHeader()
					.getPreferredSize().height;
			me.insets.top = tagHeight;
			me.insets.left = 0;
			me.insets.bottom = tagHeight;
		}
		me.innerPadding = INNER_PADDING;
		me.setPadding(PADDING);
		map.put(this, me);
		graph.nodes.add(me);
		for (int i = 0; i < getChildren().size(); i++) {
			ActivityPart activity = (ActivityPart) getChildren().get(i);
			activity.contributeNodesToGraph(graph, me, map);
		}
	}

	private boolean directEditHitTest(Point requestLoc) {
		IFigure header = ((SubgraphFigure) getFigure()).getHeader();
		header.translateToRelative(requestLoc);
		if (header.containsPoint(requestLoc))
			return true;
		return false;
	}

	/**
	 * @see org.eclipse.gef3.EditPart#performRequest(org.eclipse.gef3.Request)
	 */
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			if (request instanceof DirectEditRequest
					&& !directEditHitTest(((DirectEditRequest) request)
							.getLocation().getCopy()))
				return;
			performDirectEdit();
		}
	}

	int getAnchorOffset() {
		return -1;
	}

	public IFigure getContentPane() {
		if (getFigure() instanceof SubgraphFigure)
			return ((SubgraphFigure) getFigure()).getContents();
		return getFigure();
	}

	protected List getModelChildren() {
		return getStructuredActivity().getChildren();
	}

	StructuredActivity getStructuredActivity() {
		return (StructuredActivity) getModel();
	}

	/**
	 * @see org.eclipse.gef3.examples.flow.parts.ActivityPart#performDirectEdit()
	 */
	protected void performDirectEdit() {
		if (manager == null) {
			Label l = ((Label) ((SubgraphFigure) getFigure()).getHeader());
			manager = new ActivityDirectEditManager(this, TextCellEditor.class,
					new ActivityCellEditorLocator(l), l);
		}
		manager.show();
	}

	/**
	 * @see AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		((Label) ((SubgraphFigure) getFigure()).getHeader())
				.setText(getActivity().getName());
		((Label) ((SubgraphFigure) getFigure()).getFooter())
				.setText("/" + getActivity().getName()); //$NON-NLS-1$
	}

}
