package org.eclipse.gef.examples.flow.parts;

import java.util.*;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.PolylineConnection;
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

protected void applyGraphResults(CompoundDirectedGraph graph, Map map) {
	Edge e = (Edge)map.get(this);
	NodeList nodes = e.vNodes;
	PolylineConnection conn = (PolylineConnection)getConnectionFigure();
	if (nodes != null) {
		List bends = new ArrayList();
		conn.setConnectionRouter(new BendpointConnectionRouter());
		for (int i = 0; i < nodes.size(); i++) {
			Node vn = nodes.getNode(i);
			int x = vn.x;
			int y = vn.y;
			bends.add(new AbsoluteBendpoint(x, y));
			bends.add(new AbsoluteBendpoint(x, y + vn.height));
		}
		conn.setRoutingConstraint(bends);
	} else
		conn.setConnectionRouter(null);
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
 */
protected void createEditPolicies() {
	installEditPolicy(
		EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
	installEditPolicy(EditPolicy.CONNECTION_ROLE, new TransitionEditPolicy());
}

/**
 * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#createFigure()
 */
protected IFigure createFigure() {
	PolylineConnection conn =(PolylineConnection)super.createFigure();
	conn.setTargetDecoration(new PolygonDecoration());
	return conn;
}


public void contributeToGraph(CompoundDirectedGraph graph, Map map) {
	Node source = (Node)map.get(getSource());
	Node target = (Node)map.get(getTarget());
	Edge e = new Edge(this, source, target);
	graph.edges.add(e);
	map.put(this, e);
}

}
