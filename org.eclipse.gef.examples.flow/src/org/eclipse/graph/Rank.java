package org.eclipse.graph;

/**
 * @author hudsonr
 * @since 2.1
 */
public class Rank extends NodeList {

public boolean add(Node n) {
	n.index = size();
	return super.add(n);
}

}
