package org.eclipse.gef.examples.flow.parts;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.TextCellEditor;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Subgraph;

import org.eclipse.gef.*;
import org.eclipse.gef.requests.DirectEditRequest;

import org.eclipse.gef.examples.flow.figures.SubgraphFigure;
import org.eclipse.gef.examples.flow.model.StructuredActivity;
import org.eclipse.gef.examples.flow.policies.*;

/**
 * @author hudsonr
 * Created on Jun 30, 2003
 */
public class StructuredActivityPart extends ActivityPart 
	implements NodeEditPart {

static final Insets PADDING = new Insets(8, 6, 8, 6);
static final Insets INNER_PADDING = new Insets(0);

protected void applyChildrenResults(CompoundDirectedGraph graph, Map map) {
	for (int i = 0; i < getChildren().size(); i++) {
		ActivityPart part = (ActivityPart)getChildren().get(i);
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
 * @see org.eclipse.gef.examples.flow.parts.ActivityPart#createEditPolicies()
 */
protected void createEditPolicies() {
	installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ActivityNodeEditPolicy());
	installEditPolicy(EditPolicy.COMPONENT_ROLE, new ActivityEditPolicy());
	installEditPolicy(
		EditPolicy.SELECTION_FEEDBACK_ROLE,
		new ActivityContainerHighlightEditPolicy());
	installEditPolicy(EditPolicy.CONTAINER_ROLE, new ActivityContainerEditPolicy());
	installEditPolicy(EditPolicy.LAYOUT_ROLE, new StructuredActivityLayoutEditPolicy());
	installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new StructuredActivityDirectEditPolicy());
}

public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, Map map) {
	GraphAnimation.recordInitialState(getContentPane());
	Subgraph me = new Subgraph(this, s);
	me.outgoingOffset = 5;
	me.incomingOffset = 5;
	IFigure fig = getFigure();
	if (fig instanceof SubgraphFigure) {
		me.width = fig.getPreferredSize(me.width, me.height).width;
		int tagHeight = ((SubgraphFigure)fig).getHeader().getPreferredSize().height;
		me.insets.top = tagHeight;
		me.insets.left = 0;
		me.insets.bottom = tagHeight;
	}
	me.innerPadding = INNER_PADDING;
	me.setPadding(PADDING);
	map.put(this, me);
	graph.nodes.add(me);
	for (int i = 0; i < getChildren().size(); i++) {
		ActivityPart activity = (ActivityPart)getChildren().get(i);
		activity.contributeNodesToGraph(graph, me, map);
	}
}

protected IFigure createFigure() {
	Figure f = new SubgraphFigure(new Label(""), new Label("")) {
		/**
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		protected void paintFigure(Graphics g) {
			super.paintFigure(g);
			Rectangle r = getBounds();
			g.setBackgroundColor(ColorConstants.button);
			if (getSelected() != EditPart.SELECTED_NONE) {
				g.setBackgroundColor(ColorConstants.menuBackgroundSelected);
				g.setForegroundColor(ColorConstants.menuForegroundSelected);
			}
			
			g.fillRectangle(r.x, r.y, 3, r.height);
			g.fillRectangle(r.right() - 3, r.y, 3, r.height);
			g.fillRectangle(r.x, r.bottom() - 18, r.width, 18);
			g.fillRectangle(r.x, r.y, r.width, 18);
		}
	};
	f.setBorder(new MarginBorder(3,5,3,0));
	f.setOpaque(true);
	return f;
}


private boolean directEditHitTest(Point requestLoc) {
	IFigure header = ((SubgraphFigure)getFigure()).getHeader();
	header.translateToRelative(requestLoc);
	if (header.containsPoint(requestLoc))
		return true;
	return false;
}

/**
 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
 */
public IFigure getContentPane() {
	if (getFigure() instanceof SubgraphFigure)
		return ((SubgraphFigure)getFigure()).getContents();
	return getFigure();
}

protected List getModelChildren() {
	return getStructuredActivity().getChildren();
}

/**
 * @see NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
 */
public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
	return new SimpleActivityPart.BottomAnchor(getFigure());
}

/**
 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
 */
public ConnectionAnchor getSourceConnectionAnchor(Request request) {
	return new ChopboxAnchor(getFigure());
}

StructuredActivity getStructuredActivity() {
	return (StructuredActivity)getModel();
}

/**
 * @see NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
 */
public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
	return new SimpleActivityPart.TopAnchor(getFigure());
}

/**
 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
 */
public ConnectionAnchor getTargetConnectionAnchor(Request request) {
	return new ChopboxAnchor(getFigure());
}

/**
 * @see org.eclipse.gef.examples.flow.parts.ActivityPart#performDirectEdit()
 */
protected void performDirectEdit() {
	if (manager == null) {
		Label l = ((Label)((SubgraphFigure) getFigure()).getHeader());
		manager =
			new ActivityDirectEditManager(
				this,
				TextCellEditor.class,
				new ActivityCellEditorLocator(l),l);
	}
	manager.show();
}

/**
 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
 */
public void performRequest(Request request) {
	if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
		if (request instanceof DirectEditRequest
			&& !directEditHitTest(((DirectEditRequest) request)
				.getLocation()
				.getCopy()))
			return;
		performDirectEdit();
	}
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	((Label)((SubgraphFigure)getFigure()).getHeader()).setText(getActivity().getName());
	((Label)((SubgraphFigure)getFigure()).getFooter())
		.setText("/" + getActivity().getName()); //$NON-NLS-1$
}

}
