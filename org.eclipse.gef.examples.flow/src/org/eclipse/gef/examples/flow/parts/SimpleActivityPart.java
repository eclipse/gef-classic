package org.eclipse.gef.examples.flow.parts;

import java.util.Map;

import org.eclipse.graph.*;

import org.eclipse.draw2d.*;

import org.eclipse.gef.*;

/**
 * @author hudsonr
 * Created on Jul 17, 2003
 */
public class SimpleActivityPart extends ActivityPart implements NodeEditPart {

public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, Map map) {
	Node n = new Node(this, s);
	n.width = Math.max(getFigure().getPreferredSize().width, 80);
	n.height = getFigure().getPreferredSize().height;
	map.put(this, n);
	graph.nodes.add(n);
}

protected IFigure createFigure() {
	Label l = new Label();
	l.setBorder(new LineBorder());
	return l;
}

public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
	return new ChopboxAnchor(getFigure());
}

public ConnectionAnchor getSourceConnectionAnchor(Request request) {
	return new ChopboxAnchor(getFigure());
}

public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
	return new ChopboxAnchor(getFigure());
}

public ConnectionAnchor getTargetConnectionAnchor(Request request) {
	return new ChopboxAnchor(getFigure());
}


protected void refreshVisuals() {
	((Label)getFigure()).setText(getActivity().getName());
}

}
