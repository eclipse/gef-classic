package org.eclipse.gef.examples.flow.parts;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.examples.flow.policies.*;
import org.eclipse.graph.*;

/**
 * @author hudsonr
 * Created on Jul 18, 2003
 */
public class SequentialActivityPart
	extends StructuredActivityPart
{

/**
 * @see org.eclipse.gef.examples.flow.parts.StructuredActivityPart#createEditPolicies()
 */
protected void createEditPolicies() {
	installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new NonResizableEditPolicy());
	installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ActivityNodeEditPolicy());
	installEditPolicy(EditPolicy.COMPONENT_ROLE, new ActivityEditPolicy());
	installEditPolicy(
		EditPolicy.SELECTION_FEEDBACK_ROLE,
		new ActivityContainerHighlightEditPolicy());
	installEditPolicy(EditPolicy.LAYOUT_ROLE, new SequentialActivityLayoutEditPolicy());
}

/**
 * @see ActivityPart#contributeEdgesToGraph(org.eclipse.graph.CompoundDirectedGraph, 
 * 											java.util.Map)
 */
public void contributeEdgesToGraph(CompoundDirectedGraph graph, Map map) {
	super.contributeEdgesToGraph(graph, map);
	Node node, prev = null;
	EditPart a;
	List members = getChildren();
	for (int n = 0; n < members.size(); n++) {
		a = (EditPart)members.get(n);
		node = (Node)map.get(a);
		if (prev != null) {
			Edge e = new Edge(prev, node);
			graph.edges.add(e);
		}
		prev = node;
	}
}

}
