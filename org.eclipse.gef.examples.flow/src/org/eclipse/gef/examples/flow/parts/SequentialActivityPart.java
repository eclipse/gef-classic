package org.eclipse.gef.examples.flow.parts;

import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.*;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.graph.*;

/**
 * @author hudsonr
 * Created on Jul 18, 2003
 */
public class SequentialActivityPart
	extends StructuredActivityPart
	implements NodeEditPart 
{

/**
 * @see org.eclipse.gef.examples.flow.parts.StructuredActivityPart#createFigure()
 */
protected IFigure createFigure() {
	IFigure f = super.createFigure();
	f.setOpaque(true);
	return f;
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

/**
 * @see NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
 */
public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
	return new ChopboxAnchor(getFigure());
}

/**
 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
 */
public ConnectionAnchor getSourceConnectionAnchor(Request request) {
	return new ChopboxAnchor(getFigure());
}

/**
 * @see NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
 */
public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
	return new ChopboxAnchor(getFigure());
}

/**
 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
 */
public ConnectionAnchor getTargetConnectionAnchor(Request request) {
	return new ChopboxAnchor(getFigure());
}

}
