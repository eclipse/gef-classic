package org.eclipse.graph;

import java.util.Random;


class RankSorter {

Random flipflop = new Random(314159);
Node node;
double rankSize, prevRankSize, nextRankSize;

double evaluateIncoming(double progress) {
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
	if (progress > 0.24 && progress < 0.75) {
		if (flipflop.nextBoolean())
			return (l + l + r)/3.0;
		else
			return (r + r + l)/3.0;
	}
	return (l + r) / 2.0;
}

double evaluateOutgoing(double progress) {
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
	if (progress > 0.24 && progress < 0.75) {
		if (flipflop.nextBoolean())
			return (l + l + r)/3.0;
		else
			return (r + r + l)/3.0;
	}
	return (l + r) / 2.0;
}

public void sortRankOutgoing(DirectedGraph g, Rank rank, int row, double progress) {
	//Update rank sizes
	rankSize = rank.total;
	prevRankSize = g.ranks.getRank(row + 1).total;
	if (row > 1)
		nextRankSize = g.ranks.getRank(row - 1).total;
	
	for (int n = 0; n < rank.count(); n++) {
		node = rank.getNode(n);
		sortValueOutgoing(row, progress);
	}
	rank.sort();
	rank.assignIndices();
}

public void sortRankIncoming(DirectedGraph g, Rank rank, int row, double progress) {
	rankSize = rank.total;
	prevRankSize = g.ranks.getRank(row - 1).total;
	if (row < g.ranks.size() - 1)
		nextRankSize = g.ranks.getRank(row + 1).total;
	for (int n = 0; n<rank.count(); n++) {
		node = rank.getNode(n);
		sortValueIncoming(g, row, progress);
	}
	rank.sort();
	rank.assignIndices();
}

void sortValueIncoming(DirectedGraph g, int row, double progress) {
	node.sortValue = evaluateIncoming(progress);
	if (progress == 0.0 && !(node instanceof VirtualNode))
		node.sortValue = -1;
	double value = evaluateOutgoing(progress);
	if (value < 0)
		value = node.index * nextRankSize / rankSize;
	node.sortValue += value * progress;
}

void sortValueOutgoing(int row, double progress) {
	node.sortValue = evaluateOutgoing(progress);
	if (progress == 0.0 && !(node instanceof VirtualNode))
		node.sortValue = -1;
	double value = evaluateIncoming(progress);
	if (value < 0)
		value = node.index * nextRankSize / rankSize;
	node.sortValue += value * progress;
}

}