package org.eclipse.graph;

import java.util.Collections;
import java.util.Comparator;

/**
 * @author hudsonr
 * @since 2.1
 */
public class Rank extends NodeList {

private Comparator comparator = new MedianComparator();
final int hash = new Object().hashCode();
public int total;

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

public int count() {
	return super.size();
}

public boolean equals(Object o) {
	return o == this;
}

/**
 * @see java.util.AbstractList#hashCode()
 */
public int hashCode() {
	return hash;
}

/**
 * @deprecated
 */
public int size() {
	return super.size();
}

void sort() {
	Collections.sort(this,comparator);
}

void assignIndices() {
	total = 0;
	Node node;
//	for (int i=0; i<size(); i++) {
//		node = getNode(i);
//		node.index = i;
//	}
//	total = size() - 1;

	int mag;
	for (int i=0; i<size(); i++) {
		node = getNode(i);
		mag = node.incoming.size() + node.outgoing.size();
//		if (node instanceof SubgraphBoundary)
//			mag = 4;
		total += mag;
		node.index = total;
		total += mag;
	}
}

}
