package org.eclipse.draw2d.graph;

import java.util.ArrayList;

/**
 * @author hudsonr
 * @since 2.1
 */
public class NodeList extends ArrayList {

/**
 * Constructs an empty NodeList.
 */
public NodeList() { }

/**
 * Constructs a NodeList with the elements from the specified list.
 * @param list the list whose elements are to be added to this list
 */
public NodeList(NodeList list) {
	super(list);
}

/**
 * For internal use only.  Adjusts the rank value of all nodes in this list by the
 * specified amount.
 * @param delta the amount to adjust ranks.
 */
public void adjustRank (int delta) {
	if (delta == 0)
		return;
	for (int i = 0; i < size(); i++)
		getNode(i).rank += delta;
}

/**
 * For internal use only.
 */
public void resetSortValues() {
	for (int i = 0; i < size(); i++)
		getNode(i).sortValue = 0.0;
}

/**
 * For internal use only
 */
public void resetIndices() {
	for (int i = 0; i < size(); i++)
		getNode(i).index = 0;
}

/**
 * For internal use only.  Adjusts all nodes ranks' equally such that the minimum rank is
 * zero.
 */
public void normalizeRanks() {
	int minRank = Integer.MAX_VALUE;
	for (int i = 0; i < size(); i++)
		minRank = Math.min(minRank, getNode(i).rank);
	adjustRank(-minRank);
}

/**
 * Returns the Node at the given index.
 * @param index the index
 * @return the node at a given index
 */
public Node getNode(int index) {
	return (Node)super.get(index);
}

/**
 * For internal use only. Resets all nodes' flags.
 */
public void resetFlags() {
	for (int i = 0; i < size(); i++) {
		getNode(i).flag = false;
	}
}

}
