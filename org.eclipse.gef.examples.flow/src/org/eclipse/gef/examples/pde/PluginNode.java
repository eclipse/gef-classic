package org.eclipse.gef.examples.pde;

import java.util.*;
import org.eclipse.draw2d.graph.*;


/**
 * @author hudsonr
 * @since 2.1
 */
public class PluginNode extends Node {

NodeList closure;

public PluginNode(Object data) {
	super(data);
}

public NodeList getClosure() {
	if (closure == null) {
		closure = new NodeList();
		for (int i = 0; i < incoming.size(); i++) {
			PluginEdge e = (PluginEdge)incoming.getEdge(i);
			if (e.exported || true) {
				closure.addAll(((PluginNode)e.source).getClosure());
				closure.add(e.source);
			}
		}
	}
	return closure;
}

public void prune(DirectedGraph g) {
	Collections.sort(incoming, new Comparator() {
		public int compare(Object o1, Object o2) {
			PluginNode n1 = (PluginNode)((PluginEdge)o1).source;
			PluginNode n2 = (PluginNode)((PluginEdge)o2).source;
			return n1.getClosure().size() - n2.getClosure().size();
		}
		public boolean equals(Object obj) {
			return false;
		}
	});
	for (int i = 0; i < incoming.size();) {
		boolean remove = false;
		PluginEdge e = (PluginEdge)incoming.get(i);
		PluginNode n1 = (PluginNode)e.source;
		for (int j = i + 1; j < incoming.size(); j++) {
			PluginNode n2 = (PluginNode)incoming.getEdge(j).source;
			if (n2.getClosure().contains(n1))
				remove = true;
		}
		if (remove) {
			g.edges.remove(e);
			incoming.remove(e);
			e.source.outgoing.remove(e);
		}
		else
			i++;
	}
}

}
