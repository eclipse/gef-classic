package org.eclipse.draw2d.graph;

import java.util.ArrayList;

/**
 * A list of <code>Edge</code>s.
 * @author hudsonr
 * @since 2.1
 */
public class EdgeList extends ArrayList {

/**
 * Returns the edge for the given index.
 * @param index the index of the requested edge
 * @return the edge at the given index
 */
public Edge getEdge(int index) {
	return (Edge)super.get(index);
}

/**
 * For intrenal use only.
 * @param i and index
 * @return a value
 */
public int getSourceIndex(int i) {
	return getEdge(i).source.index;
}

/**
 * For internal use only.
 * @param i an index
 * @return a value
 */
public int getTargetIndex(int i) {
	return getEdge(i).target.index;
}

/**
 * For internal use only.
 * @return the minimum slack for this edge list
 */
public int getSlack() {
	int slack = Integer.MAX_VALUE;
	for (int i = 0; i < this.size(); i++)
		slack = Math.min(slack, getEdge(i).getSlack());
	return slack;
}

/**
 * For internal use only.
 * @return the total weight of all edges
 */
public int getWeight() {
	int w = 0;
	for (int i = 0; i < this.size(); i++)
		w += getEdge(i).weight;
	return w;
}

/**
 * For internal use only.
 * @return a rank
 */
public int calculateRank() {
	int rank = 0;
	Edge e;
	for (int i = 0; i < size(); i++) {
		e = getEdge(i);
		rank = Math.max(rank, e.delta + e.source.rank);
	}
	return rank;
}

/**
 * For internal use only
 * @return <code>true</code> if completely flagged
 */
public boolean isCompletelyFlagged() {
	for (int i = 0; i < size(); i++) {
		if (!getEdge(i).flag)
			return false;
	}
	return true;
}

/**
 * For internal use only.  Resets all flags.
 */
public void resetFlags() {
	for (int i = 0; i < size(); i++) {
		getEdge(i).flag = false;
	}
}

/**
 * For internal use only.
 * @param value value
 */
public void setFlags(boolean value) {
	for (int i = 0; i < size(); i++)
		getEdge(i).flag = value;
}

}
