package org.eclipse.draw2d.internal.graph;

import java.util.*;

import org.eclipse.draw2d.graph.*;

/**
 * @author hudsonr
 */
public class CompoundRankSorter extends RankSorter {

static class RowEntry {
	double contribution;
	int count;
	void reset() {
		count = 0;
		contribution = 0;
	}
}

static class RowKey {
	int rank;
	Subgraph s;
	RowKey() { }
	RowKey(Subgraph s, int rank) {
		this.s = s;
		this.rank = rank;
	}

	public boolean equals(Object obj) {
		RowKey rp = (RowKey)obj;
		return rp.s == s && rp.rank == rank;
	}

	public int hashCode() {
		return s.hashCode() ^ (rank * 31);
	}
}

boolean init;
RowKey key = new RowKey();

Map map = new HashMap();

void addRowEntry(Subgraph s, int row) {
	key.s = s;
	key.rank = row;
	if (!map.containsKey(key))
		map.put(new RowKey(s, row), new RowEntry());
}

protected void assignIncomingSortValues() {
	super.assignIncomingSortValues();
	pullTogetherSubgraphs();
}

/**
 * @see org.eclipse.draw2d.internal.graph.RankSorter#assignOutgoingSortValues()
 */
protected void assignOutgoingSortValues() {
	super.assignOutgoingSortValues();
	pullTogetherSubgraphs();
}

private void pullTogetherSubgraphs() {
	if (true)
		return;
	for (int j = 0; j < rank.count(); j++) {
		Node n = rank.getNode(j);
		Subgraph s = n.getParent();
		while (s != null) {
			getRowEntry(s, currentRow).reset();
			s = s.getParent();
		}
	}
	for (int j = 0; j < rank.count(); j++) {
		Node n = rank.getNode(j);
		Subgraph s = n.getParent();
		while (s != null) {
			RowEntry entry = getRowEntry(s, currentRow);
			entry.count++;
			entry.contribution += n.sortValue;
			s = s.getParent();
		}
	}
	
	double weight = 0.5;// * (1.0 - progress) * 3;
	
	for (int j = 0; j < rank.count(); j++) {
		Node n = rank.getNode(j);
		Subgraph s = n.getParent();
		if (s != null) {
			RowEntry entry = getRowEntry(s, currentRow);
			n.sortValue =
				n.sortValue * (1.0 - weight) + weight * entry.contribution / entry.count;
		}
	}
}

double evaluateNodeOutgoing() {
	double result = super.evaluateNodeOutgoing();
//	result += Math.random() * rankSize * (1.0 - progress) / 3.0;
	if (progress > 0.2) {
		Subgraph s = node.getParent();
		double connectivity = mergeConnectivity(s, node.rank + 1, result, progress);
		result = connectivity;
	}
	return result;
}

double evaluateNodeIncoming() {
	double result = super.evaluateNodeIncoming();
//	result += Math.random() * rankSize * (1.0 - progress) / 3.0;
	if (progress > 0.2) {
		Subgraph s = node.getParent();
		double connectivity = mergeConnectivity(s, node.rank - 1, result, progress);
		result = connectivity;
	}
	return result;
}

double mergeConnectivity(Subgraph s, int row, double result, double scaleFactor) {
	while (s != null && getRowEntry(s, row) == null)
		s = s.getParent();
	if (s != null) {
		RowEntry entry = getRowEntry(s, row);
		double connectivity = entry.contribution / entry.count;
		result = connectivity * 0.3 + (0.7) * result;
		s = s.getParent();
	}
	return result;
}

RowEntry getRowEntry(Subgraph s, int row) {
	key.s = s;
	key.rank = row;
	return (RowEntry)map.get(key);
}

public void init(DirectedGraph g) {
	super.init(g);
	init = true;
	for (int row = 0; row < g.ranks.size(); row++) {
		Rank rank = g.ranks.getRank(row);
		for (int j = 0; j < rank.count(); j++) {
			Node n = rank.getNode(j);
			Subgraph s = n.getParent();
			while (s != null) {
				addRowEntry(s, row);
				s = s.getParent();
			}
		}
	}
}

protected void postSort() {
	super.postSort();
	updateRank(rank);
}

void updateRank(Rank rank) {
	for (int j = 0; j < rank.count(); j++) {
		Node n = rank.getNode(j);
		Subgraph s = n.getParent();
		while (s != null) {
			getRowEntry(s, currentRow).reset();
			s = s.getParent();
		}
	}
	for (int j = 0; j < rank.count(); j++) {
		Node n = rank.getNode(j);
		Subgraph s = n.getParent();
		while (s != null) {
			RowEntry entry = getRowEntry(s, currentRow);
			entry.count++;
			entry.contribution += n.index;
			s = s.getParent();
		}
	}
}

}






