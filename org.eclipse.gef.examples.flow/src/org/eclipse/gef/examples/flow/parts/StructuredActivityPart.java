package org.eclipse.gef.examples.flow.parts;

import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.*;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.examples.flow.model.StructuredActivity;
import org.eclipse.gef.examples.flow.policies.*;
import org.eclipse.gef.examples.flow.policies.ActivityNodeEditPolicy;
import org.eclipse.gef.examples.flow.policies.StructuredActivityLayoutEditPolicy;
import org.eclipse.graph.CompoundDirectedGraph;
import org.eclipse.graph.Subgraph;

/**
 * @author hudsonr
 * Created on Jun 30, 2003
 */
public class StructuredActivityPart extends ActivityPart {

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
	installEditPolicy(EditPolicy.LAYOUT_ROLE, new StructuredActivityLayoutEditPolicy());
	installEditPolicy(EditPolicy.COMPONENT_ROLE, new ActivityEditPolicy());
	installEditPolicy(
		EditPolicy.SELECTION_FEEDBACK_ROLE,
		new ActivityContainerHighlightEditPolicy());
}

public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, Map map) {
	Subgraph me = new Subgraph(this, s);
	map.put(this, me);
	graph.nodes.add(me);
	for (int i = 0; i < getChildren().size(); i++) {
		ActivityPart activity = (ActivityPart)getChildren().get(i);
		activity.contributeNodesToGraph(graph, me, map);
	}
}

protected IFigure createFigure() {
	Figure f = new Figure();
	f.setBorder(new LineBorder());
	return f;
}

protected List getModelChildren() {
	return getStructuredActivity().getChildren();
}

StructuredActivity getStructuredActivity() {
	return (StructuredActivity)getModel();
}

protected void refreshVisuals() {}

}
