package org.eclipse.gef.examples.flow.parts;

import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.*;

import org.eclipse.gef.*;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;

import org.eclipse.gef.examples.flow.figures.StartTag;
import org.eclipse.gef.examples.flow.figures.SubgraphFigure;
import org.eclipse.gef.examples.flow.model.StructuredActivity;
import org.eclipse.gef.examples.flow.policies.*;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Subgraph;

/**
 * @author hudsonr
 * Created on Jun 30, 2003
 */
public class StructuredActivityPart extends ActivityPart 
	implements NodeEditPart {

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
	installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new NonResizableEditPolicy());
	installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ActivityNodeEditPolicy());
	installEditPolicy(EditPolicy.COMPONENT_ROLE, new ActivityEditPolicy());
	installEditPolicy(
		EditPolicy.SELECTION_FEEDBACK_ROLE,
		new ActivityContainerHighlightEditPolicy());
	installEditPolicy(EditPolicy.CONTAINER_ROLE, new ActivityContainerEditPolicy());	
}

public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, Map map) {
	Subgraph me = new Subgraph(this, s);
	me.outgoingOffset = 5;
	me.incomingOffset = 5;
	map.put(this, me);
	graph.nodes.add(me);
	for (int i = 0; i < getChildren().size(); i++) {
		ActivityPart activity = (ActivityPart)getChildren().get(i);
		activity.contributeNodesToGraph(graph, me, map);
	}
}

protected IFigure createFigure() {
	Figure f = new SubgraphFigure(new StartTag(), new StartTag());
	f.setOpaque(true);
	f.setBorder(new LineBorder());
	return f;
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

protected void refreshVisuals() {}

}
