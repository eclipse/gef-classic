package org.eclipse.graph.pde;

import org.eclipse.graph.*;

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
