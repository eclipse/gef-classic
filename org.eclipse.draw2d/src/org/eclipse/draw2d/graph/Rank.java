/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.graph;

/**
 * @author hudsonr
 * @since 2.1.2
 */
public class Rank extends NodeList {

final int hash = new Object().hashCode();

/**
 * For internal use only.  The total "size" of this rank, where size may be weighted per
 * node.
 */
public int total;

/**
 * Adds a node to this rank.
 * @param n the node
 */
public void add(Node n) {
	super.add(n);
}

/**
 * $TODO move this to RankSorter
 * @deprecated
 */
public void assignIndices() {
	total = 0;
	Node node;

	int mag;
	for (int i = 0; i < size(); i++) {
		node = getNode(i);
		mag = Math.max(1, node.incoming.size() + node.outgoing.size());
		mag = Math.min(mag, 5);
		if (node instanceof SubgraphBoundary)
			mag = 4;
		total += mag;
		node.index = total;
		total += mag;
	}
}

/**
 * Returns the number of nodes in this rank.
 * @return the number of nodes
 */
public int count() {
	return super.size();
}

/**
 * @see Object#equals(Object)
 */
public boolean equals(Object o) {
	return o == this;
}

/**
 * @see Object#hashCode()
 * Overridden for speed based on absolute equality.
 */
public int hashCode() {
	return hash;
}

/**
 * @deprecated Do not call
 */
public void sort() { }

}
