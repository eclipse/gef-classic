package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.graph.Node;

/**
 * @author hudsonr
 * @since 2.1
 */
public class NodePair {

public Node n1;
public Node n2;

public NodePair() { }

public NodePair(Node n1, Node n2) {
	this.n1 = n1;
	this.n2 = n2;
}

public boolean equals(Object obj) {
	if (obj instanceof NodePair) {
		NodePair np = (NodePair)obj;
		return np.n1 == n1 && np.n2 == n2;
	}
	return false;
}

public int hashCode() {
	return n1.hashCode() ^ n2.hashCode();
}

/**
 * @see java.lang.Object#toString()
 */
public String toString() {
	return "[" + n1 + ", " + n2 + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}

}
