package org.eclipse.graph;

import java.util.Collections;
import java.util.Comparator;

/**
 * @author hudsonr
 * @since 2.1
 */
public class Rank extends NodeList {

private Comparator comparator = new MedianComparator();

static class MedianComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		double diff = ((Node)o1).sortValue - ((Node)o2).sortValue;
		if (diff < 0.0)
			return -1;
		if (diff > 0.0)
			return 1;
		return 0;
	}
}

public boolean add(Node n) {
	n.index = size();
	return super.add(n);
}

void sort() {
	Collections.sort(this,comparator);
}

void assignIndices() {
	int mag, n = 0;
	Node node;
	for (int i=0; i<size(); i++) {
		node = getNode(i);
		if (node instanceof VirtualNode)
			mag = 1;
		else
			mag = node.incoming.size() + node.outgoing.size();
		n += mag;
		node.index = i;
//		node.index = n;
		n += mag;
	}
}

}
