package org.eclipse.draw2d.internal.graph;

import java.util.*;

import org.eclipse.draw2d.graph.*;

/**
 * @author hudsonr
 * @since 2.1
 */
public class HorizontalPlacement extends SpanningTreeVisitor {

class NodeCluster extends NodeList {

	final int hash = new Object().hashCode();
	Set incoming = new HashSet();
	int leftFreedom;
	
	int modified;
	Set outgoing = new HashSet();
	int pull;
	int rightFreedom;
	
	void build() {
		incoming.clear();
		outgoing.clear();
		for (int i = 0; i < size(); i++) {
			Node node = getNode(i);
			for (int j = 0; j < node.incoming.size(); j++) {
				Edge e = node.incoming.getEdge(j);
				if (!contains(e.source))
					incoming.add(e);
			}
			for (int j = 0; j < node.outgoing.size(); j++) {
				Edge e = node.outgoing.getEdge(j);
				if (!contains(e.target))
					outgoing.add(e);
			}
		}
	}

	public boolean equals(Object o) {
		return o == this;
	}

	int getPull() {
		return pull;
	}
	
	/**
	 * @see java.util.AbstractList#hashCode()
	 */
	public int hashCode() {
		return hash;
	}

	/**
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		updateValues();
		StringBuffer buffer = new StringBuffer("-----Cluster-------");//$NON-NLS-1$
		buffer.append("\n pull:" + pull);//$NON-NLS-1$
		buffer.append("\n left:" + leftFreedom);//$NON-NLS-1$
		buffer.append("\n right:" + rightFreedom);//$NON-NLS-1$
		buffer.append("\n modified:" + modified);//$NON-NLS-1$
		for (int i = 0; i < this.size(); i++) {
			Node node = (Node)this.get(i);
			buffer.append("\n\t" + node);//$NON-NLS-1$
		}
		return buffer.toString();
	}

	void union(NodeCluster other) {
		addAll(other);
		incoming.addAll(other.incoming);
		outgoing.addAll(other.outgoing);

		for (Iterator iter = incoming.iterator(); iter.hasNext();) {
			Object edge = iter.next();
			if (outgoing.remove(edge))
				iter.remove();
		}
	}

	void updateValues() {
		pull = 0;
		int pullCount = 0;
		int unweighted = 0;
		leftFreedom = rightFreedom = Integer.MAX_VALUE;
		for (Iterator iter = incoming.iterator(); iter.hasNext();) {
			Edge e = (Edge)iter.next();
			pull -= e.getSlack() * e.weight;
			unweighted -= e.getSlack();
			pullCount += e.weight;
			leftFreedom = Math.min(e.getSlack(), leftFreedom);
		}
		for (Iterator iter = outgoing.iterator(); iter.hasNext();) {
			Edge e = (Edge)iter.next();
			pull += e.getSlack() * e.weight;
			unweighted += e.getSlack();
			pullCount += e.weight;
			rightFreedom = Math.min(e.getSlack(), rightFreedom);
		}
		if (pullCount != 0)
			pull /= pullCount;
		else {
			if (outgoing.size() + incoming.size() == 0)
				pull = 0;
			else
				pull = unweighted / (outgoing.size() + incoming.size());
		}
	}
}

private List allClusters;
private Map clusterMap = new HashMap();
private Map clusterSetCache = new HashMap();
public DirectedGraph graph;

Map map = new HashMap();
public DirectedGraph prime;

void addEdges(Node n, Node nPrime) {
	if (n instanceof VirtualNode) {
		addEdges ((VirtualNode)n, nPrime);
		return;
	}
	for (int i = 0; i < n.incoming.size(); i++) {
		Edge e = n.incoming.getEdge(i);
		if (e.vNodes != null) {
			Node nSource = e.vNodes.getNode(e.vNodes.size() - 1);
			Node nPrimeSource = get(nSource);
			Node ne = new Node (new NodePair(n, nSource));//$NON-NLS-1$
			ne.y = (n.y + n.height + nSource.y) / 2;
			prime.nodes.add(ne);
			Edge eu = new Edge(ne, nPrimeSource);
			Edge ev = new Edge(ne, nPrime);
			eu.delta = e.getTargetOffset();
			ev.delta = 0;
			eu.weight = ev.weight = e.weight * 2;
			prime.edges.add(eu);
			prime.edges.add(ev);
		} else {
			Node nSource = e.source;
			Node nPrimeSource = get(e.source);
			Node ne = new Node(new NodePair(n, nSource));//$NON-NLS-1$
			ne.y = (n.y + n.height + nSource.y) / 2;
			prime.nodes.add(ne);
			Edge eu = new Edge(ne, nPrimeSource);
			int dw = e.getSourceOffset() - e.getTargetOffset();
			eu.delta = 0;
			eu.weight = e.weight;
			Edge ev = new Edge(ne, nPrime);
			ev.delta = 0;
			if (dw < 0)
				eu.delta = -dw;
			else
				ev.delta = dw;
			ev.weight = e.weight;
			prime.edges.add(eu);
			prime.edges.add(ev);
		}
	}
}

void addEdges(VirtualNode vn, Node nPrime) {
	Node prevPrime = get(vn.prev);
	Node vnSource = vn.prev;
	Node ne = new Node(new NodePair(vn, vnSource));
	ne.y = (vn.y + vn.height + vnSource.y) / 2;

	prime.nodes.add(ne);
	Edge eu = new Edge(ne, prevPrime);
	eu.delta = 0;
	eu.weight = vn.omega();
	Edge ev = new Edge(ne, nPrime, 0, eu.weight);
	if (!(vn.prev instanceof VirtualNode))
		ev.delta = ((Edge)vn.data).getSourceOffset();
	prime.edges.add(eu);
	prime.edges.add(ev);
}

void applyGPrime() {
	Node node;
	for (int n = 0; n < prime.nodes.size(); n++) {
		node = prime.nodes.getNode(n);
		if (node.data instanceof Node)
			((Node)node.data).x = node.rank;
	}
}

private void balanceClusters() {
	findAllClusters();
	boolean condition;
	do {
		condition = false;
		for (int i = 0; i < allClusters.size(); i++) {
			NodeCluster c = (NodeCluster)allClusters.get(i);
			c.updateValues();

			if (c.pull < 0 && c.leftFreedom > 0) {
				c.adjustRank(Math.max(c.pull, -c.leftFreedom));
				condition = true;
				allClusters.remove(c);
				allClusters.add(0, c);
				i = 0;
				c.modified++;
			} else if (c.pull > 0 && c.rightFreedom > 0) {
				c.adjustRank(Math.min(c.pull, c.rightFreedom));
				condition = true;
				allClusters.remove(c);
				allClusters.add(0, c);
				i = 0;
				c.modified++;
			}
		}
		if (!condition)
			condition = balanceClusterSets();
	} while (condition);
	
//	for (int i = 0; i < allClusters.size(); i++) {
//		NodeCluster c = (NodeCluster)allClusters.get(i);
//		System.out.println("Cluster:\n\t" + c + "\n pull = :" + c.getPull());
//	}
}

private boolean balanceClusterSets() {
	NodeCluster cluster, seed;
	for (int i = 0; i < allClusters.size(); i++) {
		seed = (NodeCluster)allClusters.get(i);
		if (seed.pull < 0 && seed.leftFreedom == 0) {
			Set set = new HashSet();
			set.add(seed);
			cluster = seed;
			boolean condition;
			do {
				condition = false;
				for (Iterator iter = cluster.incoming.iterator(); iter.hasNext();) {
					Edge e = (Edge) iter.next();
					if (e.getSlack() == 0) {
						condition = true;
						set.add((NodeCluster)clusterMap.get(e.source));
					}
				}
				cluster = getCachedClusterSet(set);
				cluster.updateValues();
			} while (cluster.leftFreedom == 0 && cluster.pull < 0 && condition);
			if (cluster.pull < 0) {
				cluster.adjustRank(Math.max(cluster.pull, -cluster.leftFreedom));
				allClusters.remove(seed);
				allClusters.add(0, seed);
				return true;
			}
		} else if (seed.pull > 0 && seed.rightFreedom > 0) {
			Set set = new HashSet();
			set.add(seed);
			cluster = seed;
			boolean condition;
			do {
				condition = false;
				for (Iterator iter = cluster.outgoing.iterator(); iter.hasNext();) {
					Edge e = (Edge) iter.next();
					if (e.getSlack() == 0) {
						condition = true;
						set.add((NodeCluster)clusterMap.get(e.target));
					}
				}
				cluster = getCachedClusterSet(set);
				cluster.updateValues();
			} while (cluster.rightFreedom == 0 && cluster.pull > 0 && condition);
			if (cluster.pull > 0) {
				cluster.adjustRank(Math.min(cluster.pull, cluster.rightFreedom));
				allClusters.remove(seed);
				allClusters.add(0, seed);
				return true;
			}
		}
	}
	return false;
}

private NodeCluster getCachedClusterSet(Set set) {
	NodeCluster cluster = (NodeCluster)clusterSetCache.get(set);
	if (cluster != null)
		return cluster;
	cluster = new NodeCluster();
	Iterator iter = set.iterator();

	while (iter.hasNext())
		cluster.union((NodeCluster)iter.next());

	cluster.updateValues();
//	System.out.println("built SUPERCLUSTER:" + cluster);
	
	clusterSetCache.put(set, cluster);
	return cluster;
}

void buildGPrime() {
	RankList ranks = graph.ranks;
	buildRankSeparators(ranks);

	Rank rank;
	Node n, nPrime;
	for (int r = 1; r < ranks.size(); r++) {
		rank = ranks.getRank(r);
		for (int i = 0; i < rank.count(); i++) {
			n = rank.getNode(i);
			nPrime = get(n);
			addEdges(n, nPrime);
		}
	}
}

void buildRankSeparators(RankList ranks) {
	Rank rank;
	Node n, nPrime, prevNPrime;
	for (int r = 0; r < ranks.size(); r++) {
		rank = ranks.getRank(r);
		prevNPrime = null;
		for (int i = 0; i < rank.count(); i++) {
			n = rank.getNode(i);
			nPrime = new Node(n);
			if (prevNPrime != null) {
				Edge e = new Edge(prevNPrime, nPrime);
				e.weight = 0;
				prime.edges.add(e);
				rowSeparation(e);
			}
			prevNPrime = nPrime;
			prime.nodes.add(nPrime);
			map(n, nPrime);
		}
	}
}

Node get(Node key) {
	return (Node)map.get(key);
}

void growCluster(Node root, NodeCluster cluster, List allClusters) {
	cluster.add(root);
	clusterMap.put(root, cluster);
	EdgeList treeChildren = getSpanningTreeChildren(root);
	for (int i = 0; i < treeChildren.size(); i++) {
		Edge e = treeChildren.getEdge(i);
		if (e.cut != 0)
			growCluster(getTreeTail(e), cluster, allClusters);
		else {
			NodeCluster newCluster = new NodeCluster();
			allClusters.add(newCluster);
			growCluster(getTreeTail(e), newCluster, allClusters);
		}
	}
}

private void findAllClusters() {
	Node root = prime.nodes.getNode(0);
	NodeCluster cluster = new NodeCluster();
	allClusters = new ArrayList();
	allClusters.add(cluster);
	growCluster(root, cluster, allClusters);
	for (int i = 0; i < allClusters.size(); i++) {
		NodeCluster c = (NodeCluster)allClusters.get(i);
		c.build();
	}
}

void map(Node key, Node value) {
	map.put(key, value);
}

void rowSeparation(Edge e) {
	Node source = (Node)e.source.data;
	e.delta = source.width + 15;
}

public void visit(DirectedGraph g) {
	graph = g;
	prime = new DirectedGraph();
	g.gPrime = prime;
	buildGPrime();
	new InitialRankSolver()
		.visit(prime);
	new TightSpanningTreeSolver()
		.visit(prime);

	RankAssigmentSolver solver = new RankAssigmentSolver();
//	solver.maxcount = 3;
	solver.visit(prime);
	
	balanceClusters();
	
	prime.nodes.normalizeRanks();
	applyGPrime();
}

}
