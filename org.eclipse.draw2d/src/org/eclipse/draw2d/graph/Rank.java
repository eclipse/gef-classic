/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.graph;

import java.util.Collections;
import java.util.Comparator;

/**
 * @author hudsonr
 * @since 2.1
 */
public class Rank extends NodeList {

private Comparator comparator = new MedianComparator();
final int hash = new Object().hashCode();
/**
 * For internal use only.  The total "size" of this rank, where size may be weighted per
 * node.
 */
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

/**
 * Adds a node to this rank.
 * @param n the node
 */
public void add(Node n) {
	super.add(n);
}

/**
 * Returns the number of nodes in this rank.
 * @return the number of nodes
 */
public int count() {
	return super.size();
}

/**
 * @see java.lang.Object#equals(java.lang.Object)
 */
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
 * $TODO move this to RankSorter
 */
public void sort() {
	Collections.sort(this, comparator);
}

/**
 * $TODO move this to RankSorter
 */
public void assignIndices() {
	total = 0;
	Node node;
//	for (int i=0; i<size(); i++) {
//		node = getNode(i);
//		node.index = i;
//	}
//	total = size() - 1;

	int mag;
	for (int i = 0; i < size(); i++) {
		node = getNode(i);
		mag = node.incoming.size() + node.outgoing.size() + 1;
//		if (node instanceof SubgraphBoundary)
//			mag = 4;
		total += mag;
		node.index = total;
		total += mag;
	}
}

}
