package org.eclipse.gef.examples.flow.parts;

import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.graph.*;

import org.eclipse.gef.EditPart;

import org.eclipse.gef.examples.flow.figures.SequentialActivityFigure;

/**
 * @author hudsonr
 * Created on Jul 18, 2003
 */
public class SequentialActivityPart
	extends StructuredActivityPart
{

/**
 * @see org.eclipse.gef.examples.flow.parts.StructuredActivityPart#createFigure()
 */
protected IFigure createFigure() {
	return new SequentialActivityFigure();
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
			e.weight = 50;
			graph.edges.add(e);
		}
		prev = node;
	}
}

/**
 * @see org.eclipse.gef.examples.flow.parts.StructuredActivityPart#getAnchorOffset()
 */
int getAnchorOffset() {
	return 15;
}

}
