package org.eclipse.graph;

/**
 * @author hudsonr
 * @since 2.1
 */
public class MinCross extends GraphVisitor {

static final int MAX = 100;

private DirectedGraph g;
private RankSorter sorter = new RankSorter();

public void setRankSorter(RankSorter sorter) {
	this.sorter = sorter;
}

void solve() {
	Rank rank;
	for (int loop = 0; loop < MAX; loop++) {
		for (int row = 1; row < g.ranks.size(); row++) {			
			rank = g.ranks.getRank(row);
			sorter.sortRankIncoming(g, rank, row, (double)loop / MAX);
		}
		if (loop == MAX - 1)
			continue;
		for (int row = g.ranks.size() - 2; row >= 0; row--) {
			rank = g.ranks.getRank(row);
			sorter.sortRankOutgoing(g, rank, row, (double)loop / MAX);
		}
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
