package org.eclipse.gef.examples.flow.parts;

import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.gef.EditPart;

/**
 * @author hudsonr
 * Created on Jul 18, 2003
 */
public class SequentialActivityPart
	extends StructuredActivityPart
{

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
//			e.weight = 1000;
			graph.edges.add(e);
		}
		prev = node;
	}
}

}
