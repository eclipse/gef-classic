package org.eclipse.graph;

import java.util.Collections;
import java.util.Comparator;

/**
 * @author hudsonr
 * @since 2.1
 */
public class MinCross extends GraphVisitor {

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

private DirectedGraph g;
private Comparator comparator = new MedianComparator();

static final int MAX = 40;

void solve() {
	Rank rank;
	Node node;
	
	double rankSize, prevRankSize, nextRankSize = 0;
	
	for (int loop = 0; loop < MAX; loop++) {
		for (int row = 1; row < g.ranks.size(); row++) {
			rank = g.ranks.getRank(row);
			rankSize = rank.size() - 1;
			prevRankSize = g.ranks.getRank(row - 1).size() - 1;
			if (row < g.ranks.size() - 1)
				nextRankSize = g.ranks.getRank(row + 1).size() - 1;
			
			for (int n = 0; n<rank.size(); n++) {
				node = rank.getNode(n);
				node.sortValue = node.medianIncoming();
				if (loop == 0 && !(node instanceof VirtualNode))
					node.sortValue = -1;
				if (node.sortValue < 0)
					node.sortValue = node.index * prevRankSize / rankSize;
				if (loop > (MAX/2) && row < g.ranks.size()-1) {
					node.sortValue *= 1.01;
					double value = node.medianOutgoing();
					if (value < 0)
						node.sortValue += node.index * nextRankSize / rankSize;
					else
						node.sortValue += value;
				}
//				if (loop < MAX/4)
//					node.sortValue = node.sortValue + Math.random() * rank.size() / (loop/3+2);
			}
			sortRank(rank);
		}

		if (loop == MAX - 1)
			continue;
		for (int row = g.ranks.size()-2; row >= 0; row--) {
			rank = g.ranks.getRank(row);
			
			//Update rank sizes
			rankSize = rank.size() - 1;
			prevRankSize = g.ranks.getRank(row + 1).size() - 1;
			if (row > 1)
				nextRankSize = g.ranks.getRank(row - 1).size() - 1;
			
			for (int n = 0; n < rank.size(); n++) {
				node = rank.getNode(n);
				node.sortValue = node.medianOutgoing();
				if (loop == 0 && !(node instanceof VirtualNode))
					node.sortValue = -1;
				if (node.sortValue < 0)
					node.sortValue = node.index * prevRankSize / rankSize;
				if (loop > MAX/2 && row > 1) {
					node.sortValue *= 1.01;
					double value = node.medianIncoming();
					if (value < 0)
						node.sortValue += node.index * nextRankSize / rankSize;
					else
						node.sortValue += value;// / (g.ranks.getRank(r-1).size() - 1);
				}
//				if (loop < MAX/4)
//					node.sortValue = node.sortValue + Math.random() * rank.size() / (loop/3+2);
			}
			sortRank(rank);
		}
	}
}

void sortRank(Rank rank) {
	Collections.sort(rank,comparator);
	int mag, n = 0;
	Node node;
	for (int i=0; i<rank.size(); i++) {
		node = rank.getNode(i);
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

/**
 * @see org.eclipse.graph.GraphVisitor#visit(DirectedGraph)
 */
public void visit(DirectedGraph g) {
	this.g = g;
	solve();
}

}
