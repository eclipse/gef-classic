package org.eclipse.graph;

import java.util.ArrayList;

/**
 * @author hudsonr
 * @since 2.1
 */
public class EdgeList extends ArrayList {

public Edge getEdge(int index) {
	return (Edge)super.get(index);
}

public int getSourceIndex(int i) {
	return getEdge(i).source.index;
}

public int getTargetIndex(int i) {
	return getEdge(i).target.index;
}

public int getSlack() {
	int slack = Integer.MAX_VALUE;
	for (int i = 0; i < this.size(); i++)
		slack = Math.min(slack, getEdge(i).getSlack());
	return slack;
}

public int getWeight() {
	int w = 0;
	for (int i = 0; i < this.size(); i++)
		w+=getEdge(i).weight;
	return w;
}

public int calculateRank() {
	int rank = 0;
	Edge e;
	for (int i=0; i<size(); i++){
		e = getEdge(i);
		rank = Math.max(rank, e.delta + e.source.rank);
	}
	return rank;
}

public boolean isCompletelyFlagged() {
	for (int i = 0; i < size(); i++) {
		if (getEdge(i).flag == false)
			return false;
	}
	return true;
}

public void resetFlags() {
	for (int i = 0; i < size(); i++) {
		getEdge(i).flag = false;
	}
}

public void setFlags(boolean value) {
	for (int i=0; i<size(); i++)
		getEdge(i).flag = value;
}

}
