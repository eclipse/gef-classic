/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.internal.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Rank;
import org.eclipse.draw2d.graph.RankList;

/**
 * Assigns the X and width values for nodes in a directed graph.
 * @author Randy Hudson
 * @since 2.1.2
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

/**
 * Adds all of the incoming edges to the graph.
 * @param n the original node
 * @param nPrime its corresponding node in the auxilary graph
 */
void addEdges(Node n) {
	for (int i = 0; i < n.incoming.size(); i++) {
		Edge e = n.incoming.getEdge(i);
		addEdge(e.source, n, e, 1);
	}
}

/**
 * Inset the corresponding parts for the given 2 nodes along an edge E.  The weight value
 * is a value by which to scale the edges specified weighting factor.
 * @param u the source
 * @param v the target
 * @param e the edge along which u and v exist
 * @param weight a scaling for the weight
 */
void addEdge(Node u, Node v, Edge e, int weight) {
	Node ne = new Node(new NodePair(u, v));
	prime.nodes.add(ne);

	ne.y = (u.y + u.height + v.y) / 2;
	Node uPrime = get(u);
	Node vPrime = get(v);

	int uOffset = e.getSourceOffset();
	
	int vOffset = e.getTargetOffset();
	
	Edge eu = new Edge(ne, uPrime, 0, e.weight * weight);
	Edge ev = new Edge(ne, vPrime, 0, e.weight * weight);
	
	int dw = uOffset - vOffset;
	if (dw < 0)
		eu.delta = -dw;
	else
		ev.delta = dw;
	
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
	
	for (Iterator itr = clusterSetCache.values().iterator(); itr.hasNext();) {
		seed = (NodeCluster)itr.next();
		seed.updateValues();
		if (seed.pull < 0 && seed.leftFreedom > 0) {
			seed.adjustRank(Math.max(seed.pull, -seed.leftFreedom));
			return true;
		} else if (seed.pull > 0 && seed.rightFreedom > 0) {
			seed.adjustRank(Math.min(seed.pull, seed.rightFreedom));
			return true;
		}
	}
	
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
						set.add(clusterMap.get(e.source));
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
						set.add(clusterMap.get(e.target));
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
	Node n;
	for (int r = 1; r < ranks.size(); r++) {
		rank = ranks.getRank(r);
		for (int i = 0; i < rank.count(); i++) {
			n = rank.getNode(i);
			addEdges(n);
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
