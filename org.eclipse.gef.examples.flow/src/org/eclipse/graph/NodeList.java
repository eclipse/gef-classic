package org.eclipse.graph;

import java.util.ArrayList;

/**
 * @author hudsonr
 * @since 2.1
 */
public class NodeList extends ArrayList {

public NodeList() { }

public NodeList(NodeList list) {
	super(list);
}

public void adjustRank (int delta) {
	if (delta == 0)
		return;
	for (int i=0; i<size(); i++)
		getNode(i).rank += delta;
}

public void resetSortValues() {
	for (int i = 0; i < size(); i++)
		getNode(i).sortValue = 0.0;
}

public void resetIndices() {
	for (int i = 0; i < size(); i++)
		getNode(i).index = 0;
}

public void normalizeRanks() {
	int minRank = Integer.MAX_VALUE;
	for (int i = 0; i < size(); i++)
		minRank = Math.min(minRank, getNode(i).rank);
	adjustRank(-minRank);
}

public int getMaxRank() {
	int max = Integer.MIN_VALUE;
	for (int i=0; i<size(); i++)
		max = Math.max(max, getNode(i).rank);
	return max;
}

/**
 * @see java.util.ArrayList#get(int)
 */
public Node getNode(int index) {
	return (Node)super.get(index);
}

public void resetFlags() {
	for (int i = 0; i < size(); i++) {
		getNode(i).flag = false;
	}
}

}
