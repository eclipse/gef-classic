package org.eclipse.draw2d.internal.graph;

import java.util.Random;

import org.eclipse.draw2d.graph.*;

class RankSorter {

Random flipflop = new Random(3);
Node node;
double rankSize, prevRankSize, nextRankSize;
int currentRow;
Rank rank;
double progress;
DirectedGraph g;

protected void assignIncomingSortValues() {
	rankSize = rank.total;
	prevRankSize = g.ranks.getRank(currentRow - 1).total;
	if (currentRow < g.ranks.size() - 1)
		nextRankSize = g.ranks.getRank(currentRow + 1).total;
	for (int n = 0; n<rank.count(); n++) {
		node = rank.getNode(n);
		sortValueIncoming(g, currentRow, progress);
	}
}

protected void assignOutgoingSortValues() {
	rankSize = rank.total;
	prevRankSize = g.ranks.getRank(currentRow + 1).total;
	if (currentRow > 1)
		nextRankSize = g.ranks.getRank(currentRow - 1).total;
	
	for (int n = 0; n < rank.count(); n++) {
		node = rank.getNode(n);
		sortValueOutgoing(currentRow, progress);
	}
}

double evaluateNodeIncoming() {
	boolean change = false;
	EdgeList incoming = node.incoming;
	do {
		change = false;
		for (int i = 0; i < incoming.size() - 1; i++) {
			if (incoming.getSourceIndex(i) > incoming.getSourceIndex(i+1)) {
				Edge e = incoming.getEdge(i);
				incoming.set(i, incoming.get(i+1));
				incoming.set(i+1, e);
				change = true;
			}
		}
	} while (change);
	
	int n = incoming.size();
	if (n == 0) {
		return node.index * prevRankSize / rankSize;
	}
	if (n % 2 == 1)
		return incoming.getSourceIndex(n / 2);

	int l = incoming.getSourceIndex(n / 2 - 1);
	int r = incoming.getSourceIndex(n / 2);
	if (progress >= 0.8) {
		int dl = l - incoming.getSourceIndex(0);
		int dr = incoming.getSourceIndex(n - 1) - r;
		if (dl < dr)
			return l;
		if (dl > dr)
			return r;
	}
	if (progress > 0.25 && progress < 0.75) {
		if (flipflop.nextBoolean())
			return (l + l + r)/3.0;
		else
			return (r + r + l)/3.0;
	}
	return (l + r) / 2.0;
}

double evaluateNodeOutgoing() {
	boolean change = false;
	EdgeList outgoing = node.outgoing;
	do {
		change = false;
		for (int i = 0; i < outgoing.size() - 1; i++) {
			if (outgoing.getTargetIndex(i) > outgoing.getTargetIndex(i+1)) {
				Edge e = outgoing.getEdge(i);
				outgoing.set(i, outgoing.get(i+1));
				outgoing.set(i+1, e);
				change = true;
			}
		}
	} while (change);

	int n = outgoing.size();
	if (n == 0)
		return node.index * prevRankSize / rankSize;
	if (n % 2 == 1)
		return outgoing.getTargetIndex(n / 2);
	int l = outgoing.getTargetIndex(n / 2 - 1);
	int r = outgoing.getTargetIndex(n / 2);
	if (progress >= 0.8) {
		int dl = l - outgoing.getTargetIndex(0);
		int dr = outgoing.getTargetIndex(n - 1) - r;
		if (dl < dr)
			return l;
		if (dl > dr)
			return r;
	}
	if (progress > 0.25 && progress < 0.75) {
		if (flipflop.nextBoolean())
			return (l + l + r)/3.0;
		else
			return (r + r + l)/3.0;
	}
	return (l + r) / 2.0;
}

public void sortRankIncoming(DirectedGraph g, Rank rank, int row, double progress) {
	this.currentRow = row;
	this.rank = rank;
	this.progress = progress;
	assignIncomingSortValues();
	rank.sort();
	postSort();
}

public void init(DirectedGraph g) {
	this.g = g;
}

protected void postSort() {
	rank.assignIndices();
}

public void sortRankOutgoing(DirectedGraph g, Rank rank, int row, double progress) {
	this.currentRow = row;
	this.rank = rank;
	this.progress = progress;
	assignOutgoingSortValues();
	rank.sort();
	postSort();
}

void sortValueIncoming(DirectedGraph g, int row, double progress) {
	node.sortValue = evaluateNodeIncoming();
	if (progress == 0.0 && !(node instanceof VirtualNode))
		node.sortValue = -1;
	double value = evaluateNodeOutgoing();
	if (value < 0)
		value = node.index * nextRankSize / rankSize;
	node.sortValue += value * progress;
}

void sortValueOutgoing(int row, double progress) {
	node.sortValue = evaluateNodeOutgoing();
	if (progress == 0.0 && !(node instanceof VirtualNode))
		node.sortValue = -1;
	double value = evaluateNodeIncoming();
	if (value < 0)
		value = node.index * nextRankSize / rankSize;
	node.sortValue += value * progress;
}

}