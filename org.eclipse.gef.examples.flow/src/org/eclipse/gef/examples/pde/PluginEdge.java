package org.eclipse.gef.examples.pde;

import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;

/**
 * @author hudsonr
 * @since 2.1
 */
public class PluginEdge extends Edge {

public boolean exported;

public PluginEdge (Node source, Node target, boolean exported) {
	super(source, target);
//	if (source.data.equals("Eclipse UI") && Math.random() > 0.4)
//		delta = 2;
	this.exported = exported;
}

}
