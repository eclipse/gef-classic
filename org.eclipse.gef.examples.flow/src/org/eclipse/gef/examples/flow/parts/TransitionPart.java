package org.eclipse.gef.examples.flow.parts;

import java.util.Map;

import org.eclipse.graph.*;
import org.eclipse.graph.CompoundDirectedGraph;
import org.eclipse.graph.Node;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.examples.flow.policies.TransitionEditPolicy;

/**
 * @author hudsonr
 * Created on Jul 16, 2003
 */
public class TransitionPart extends AbstractConnectionEditPart {

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
 */
protected void createEditPolicies() {
	installEditPolicy(
		EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
	installEditPolicy(EditPolicy.CONNECTION_ROLE, new TransitionEditPolicy());
}

public void contributeToGraph(CompoundDirectedGraph graph, Map map) {
	Node source = (Node)map.get(getSource());
	Node target = (Node)map.get(getTarget());
	Edge e = new Edge(this, source, target);
	graph.edges.add(e);
}

}
