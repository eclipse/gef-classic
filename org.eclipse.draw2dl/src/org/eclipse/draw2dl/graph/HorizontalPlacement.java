/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Assigns the X and width values for nodes in a directed graph.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
class HorizontalPlacement extends org.eclipse.draw2dl.graph.SpanningTreeVisitor {

	class ClusterSet {
		int freedom = Integer.MAX_VALUE;
		boolean isRight;
		public List members = new ArrayList();
		int pullWeight = 0;
		int rawPull = 0;

		boolean addCluster(org.eclipse.draw2dl.graph.NodeCluster seed) {
			members.add(seed);
			seed.isSetMember = true;

			rawPull += seed.weightedTotal;
			pullWeight += seed.weightedDivisor;
			if (isRight) {
				freedom = Math.min(freedom, seed.rightNonzero);
				if (freedom == 0 || rawPull <= 0)
					return true;
				addIncomingClusters(seed);
				if (addOutgoingClusters(seed))
					return true;
			} else {
				freedom = Math.min(freedom, seed.leftNonzero);
				if (freedom == 0 || rawPull >= 0)
					return true;
				addOutgoingClusters(seed);
				if (addIncomingClusters(seed))
					return true;
			}
			return false;
		}

		boolean addIncomingClusters(org.eclipse.draw2dl.graph.NodeCluster seed) {
			for (int i = 0; i < seed.leftCount; i++) {
				org.eclipse.draw2dl.graph.NodeCluster neighbor = seed.leftNeighbors[i];
				if (neighbor.isSetMember)
					continue;
				org.eclipse.draw2dl.graph.CollapsedEdges edges = seed.leftLinks[i];
				if (!edges.isTight())
					continue;
				if ((!isRight || neighbor.getPull() > 0)
						&& addCluster(neighbor))
					return true;
			}
			return false;
		}

		boolean addOutgoingClusters(org.eclipse.draw2dl.graph.NodeCluster seed) {
			for (int i = 0; i < seed.rightCount; i++) {
				org.eclipse.draw2dl.graph.NodeCluster neighbor = seed.rightNeighbors[i];
				if (neighbor.isSetMember)
					continue;
				org.eclipse.draw2dl.graph.CollapsedEdges edges = seed.rightLinks[i];
				if (!edges.isTight())
					continue;
				if ((isRight || neighbor.getPull() < 0) && addCluster(neighbor))
					return true;
			}
			return false;
		}

		boolean build(org.eclipse.draw2dl.graph.NodeCluster seed) {
			isRight = seed.weightedTotal > 0;
			if (!addCluster(seed)) {
				int delta = rawPull / pullWeight;
				if (delta < 0)
					delta = Math.max(delta, -freedom);
				else
					delta = Math.min(delta, freedom);
				if (delta != 0) {
					for (int i = 0; i < members.size(); i++) {
						org.eclipse.draw2dl.graph.NodeCluster c = (org.eclipse.draw2dl.graph.NodeCluster) members.get(i);
						c.adjustRank(delta, dirtyClusters);
					}
					refreshDirtyClusters();
					reset();
					return true;
				}
			}
			reset();
			return false;
		}

		void reset() {
			rawPull = pullWeight = 0;
			for (int i = 0; i < members.size(); i++)
				((org.eclipse.draw2dl.graph.NodeCluster) members.get(i)).isSetMember = false;
			members.clear();
			freedom = Integer.MAX_VALUE;
		}
	}

	static int step;
	private List allClusters;
	private Map clusterMap = new HashMap();
	ClusterSet clusterset = new ClusterSet();
	Collection dirtyClusters = new HashSet();
	org.eclipse.draw2dl.graph.DirectedGraph graph;
	Map map = new HashMap();
	org.eclipse.draw2dl.graph.DirectedGraph prime;
	org.eclipse.draw2dl.graph.Node graphRight;
	org.eclipse.draw2dl.graph.Node graphLeft;

	/**
	 * Inset the corresponding parts for the given 2 nodes along an edge E. The
	 * weight value is a value by which to scale the edges specified weighting
	 * factor.
	 * 
	 * @param u
	 *            the source
	 * @param v
	 *            the target
	 * @param e
	 *            the edge along which u and v exist
	 * @param weight
	 *            a scaling for the weight
	 */
	void addEdge(org.eclipse.draw2dl.graph.Node u, org.eclipse.draw2dl.graph.Node v, org.eclipse.draw2dl.graph.Edge e, int weight) {
		org.eclipse.draw2dl.graph.Node ne = new org.eclipse.draw2dl.graph.Node(new org.eclipse.draw2dl.graph.NodePair(u, v));
		prime.nodes.add(ne);

		ne.y = (u.y + u.height + v.y) / 2;
		org.eclipse.draw2dl.graph.Node uPrime = get(u);
		org.eclipse.draw2dl.graph.Node vPrime = get(v);

		int uOffset = e.getSourceOffset();

		int vOffset = e.getTargetOffset();

		org.eclipse.draw2dl.graph.Edge eu = new org.eclipse.draw2dl.graph.Edge(ne, uPrime, 0, e.weight * weight);
		org.eclipse.draw2dl.graph.Edge ev = new org.eclipse.draw2dl.graph.Edge(ne, vPrime, 0, e.weight * weight);

		int dw = uOffset - vOffset;
		if (dw < 0)
			eu.delta = -dw;
		else
			ev.delta = dw;

		prime.edges.add(eu);
		prime.edges.add(ev);
	}

	/**
	 * Adds all of the incoming edges to the graph.
	 * 
	 * @param n
	 *            the original node
	 * @param nPrime
	 *            its corresponding node in the auxilary graph
	 */
	void addEdges(org.eclipse.draw2dl.graph.Node n) {
		for (int i = 0; i < n.incoming.size(); i++) {
			org.eclipse.draw2dl.graph.Edge e = n.incoming.getEdge(i);
			addEdge(e.source, n, e, 1);
		}
	}

	void applyGPrime() {
		org.eclipse.draw2dl.graph.Node node;
		for (int n = 0; n < prime.nodes.size(); n++) {
			node = prime.nodes.getNode(n);
			if (node.data instanceof org.eclipse.draw2dl.graph.Node)
				((org.eclipse.draw2dl.graph.Node) node.data).x = node.rank;
		}
	}

	private void balanceClusters() {
		findAllClusters();

		step = 0;
		boolean somethingMoved = false;

		for (int i = 0; i < allClusters.size();) {

			org.eclipse.draw2dl.graph.NodeCluster c = (org.eclipse.draw2dl.graph.NodeCluster) allClusters.get(i);
			int delta = c.getPull();
			if (delta < 0) {
				if (c.leftFreedom > 0) {
					c.adjustRank(Math.max(delta, -c.leftFreedom), dirtyClusters);
					refreshDirtyClusters();
					moveClusterForward(i, c);
					somethingMoved = true;
					step++;
				} else if (clusterset.build(c)) {
					step++;
					moveClusterForward(i, c);
					somethingMoved = true;
				}
			} else if (delta > 0) {
				if (c.rightFreedom > 0) {
					c.adjustRank(Math.min(delta, c.rightFreedom), dirtyClusters);
					refreshDirtyClusters();
					moveClusterForward(i, c);
					somethingMoved = true;
					step++;
				} else if (clusterset.build(c)) {
					step++;
					moveClusterForward(i, c);
					somethingMoved = true;
				}
			}
			i++;
			if (i == allClusters.size() && somethingMoved) {
				i = 0;
				somethingMoved = false;
			}
		}
	}

	// boolean balanceClusterSets() {
	// for (int i = 0; i < allClusters.size(); i++) {
	// NodeCluster c = (NodeCluster)allClusters.get(i);
	// if (c.weightedTotal < 0 && c.leftFreedom == 0) {
	// if (clusterset.build(c)) {
	// moveClusterForward(i, c);
	// return true;
	// }
	// } else if (c.weightedTotal > 0 && c.rightFreedom == 0) {
	// if (clusterset.build(c)) {
	// moveClusterForward(i, c);
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	void buildGPrime() {
		org.eclipse.draw2dl.graph.RankList ranks = graph.ranks;
		buildRankSeparators(ranks);

		org.eclipse.draw2dl.graph.Rank rank;
		org.eclipse.draw2dl.graph.Node n;
		for (int r = 1; r < ranks.size(); r++) {
			rank = ranks.getRank(r);
			for (int i = 0; i < rank.count(); i++) {
				n = rank.getNode(i);
				addEdges(n);
			}
		}
	}

	void buildRankSeparators(RankList ranks) {
		org.eclipse.draw2dl.graph.Rank rank;
		org.eclipse.draw2dl.graph.Node n, nPrime, prevNPrime;
		org.eclipse.draw2dl.graph.Edge e;
		for (int r = 0; r < ranks.size(); r++) {
			rank = ranks.getRank(r);
			prevNPrime = null;
			for (int i = 0; i < rank.count(); i++) {
				n = rank.getNode(i);
				nPrime = new org.eclipse.draw2dl.graph.Node(n);
				if (i == 0) {
					e = new org.eclipse.draw2dl.graph.Edge(graphLeft, nPrime, 0, 0);
					prime.edges.add(e);
					e.delta = graph.getPadding(n).left + graph.getMargin().left;
				} else {
					e = new org.eclipse.draw2dl.graph.Edge(prevNPrime, nPrime);
					e.weight = 0;
					prime.edges.add(e);
					rowSeparation(e);
				}
				prevNPrime = nPrime;
				prime.nodes.add(nPrime);
				map(n, nPrime);
				if (i == rank.count() - 1) {
					e = new org.eclipse.draw2dl.graph.Edge(nPrime, graphRight, 0, 0);
					e.delta = n.width + graph.getPadding(n).right
							+ graph.getMargin().right;
					prime.edges.add(e);
				}
			}
		}
	}

	private void calculateCellLocations() {
		graph.cellLocations = new int[graph.ranks.size() + 1][];
		for (int row = 0; row < graph.ranks.size(); row++) {
			Rank rank = graph.ranks.getRank(row);
			int locations[] = graph.cellLocations[row] = new int[rank.size() + 1];
			int cell;
			org.eclipse.draw2dl.graph.Node node = null;
			for (cell = 0; cell < rank.size(); cell++) {
				node = rank.getNode(cell);
				locations[cell] = node.x - graph.getPadding(node).left;
			}
			locations[cell] = node.x + node.width
					+ graph.getPadding(node).right;
		}
	}

	private void findAllClusters() {
		org.eclipse.draw2dl.graph.Node root = prime.nodes.getNode(0);
		org.eclipse.draw2dl.graph.NodeCluster cluster = new org.eclipse.draw2dl.graph.NodeCluster();
		allClusters = new ArrayList();
		allClusters.add(cluster);
		growCluster(root, cluster);

		for (int i = 0; i < prime.edges.size(); i++) {
			org.eclipse.draw2dl.graph.Edge e = prime.edges.getEdge(i);
			org.eclipse.draw2dl.graph.NodeCluster sourceCluster = (org.eclipse.draw2dl.graph.NodeCluster) clusterMap.get(e.source);
			org.eclipse.draw2dl.graph.NodeCluster targetCluster = (org.eclipse.draw2dl.graph.NodeCluster) clusterMap.get(e.target);

			// Ignore cluster internal edges
			if (targetCluster == sourceCluster)
				continue;

			org.eclipse.draw2dl.graph.CollapsedEdges link = sourceCluster.getRightNeighbor(targetCluster);
			if (link == null) {
				link = new org.eclipse.draw2dl.graph.CollapsedEdges(e);
				sourceCluster.addRightNeighbor(targetCluster, link);
				targetCluster.addLeftNeighbor(sourceCluster, link);
			} else {
				prime.removeEdge(link.processEdge(e));
				i--;
			}
		}
		for (int i = 0; i < allClusters.size(); i++)
			((org.eclipse.draw2dl.graph.NodeCluster) allClusters.get(i)).initValues();
	}

	org.eclipse.draw2dl.graph.Node get(org.eclipse.draw2dl.graph.Node key) {
		return (org.eclipse.draw2dl.graph.Node) map.get(key);
	}

	void growCluster(org.eclipse.draw2dl.graph.Node root, org.eclipse.draw2dl.graph.NodeCluster cluster) {
		cluster.add(root);
		clusterMap.put(root, cluster);
		EdgeList treeChildren = getSpanningTreeChildren(root);
		for (int i = 0; i < treeChildren.size(); i++) {
			org.eclipse.draw2dl.graph.Edge e = treeChildren.getEdge(i);
			if (e.cut != 0)
				growCluster(getTreeTail(e), cluster);
			else {
				org.eclipse.draw2dl.graph.NodeCluster newCluster = new org.eclipse.draw2dl.graph.NodeCluster();
				allClusters.add(newCluster);
				growCluster(getTreeTail(e), newCluster);
			}
		}
	}

	void map(org.eclipse.draw2dl.graph.Node key, org.eclipse.draw2dl.graph.Node value) {
		map.put(key, value);
	}

	private void moveClusterForward(int i, org.eclipse.draw2dl.graph.NodeCluster c) {
		if (i == 0)
			return;
		int swapIndex = i / 2;
		Object temp = allClusters.get(swapIndex);
		allClusters.set(swapIndex, c);
		allClusters.set(i, temp);
	}

	void refreshDirtyClusters() {
		for (Iterator iter = dirtyClusters.iterator(); iter.hasNext();)
			((org.eclipse.draw2dl.graph.NodeCluster) iter.next()).refreshValues();
		dirtyClusters.clear();
	}

	void rowSeparation(org.eclipse.draw2dl.graph.Edge e) {
		org.eclipse.draw2dl.graph.Node source = (org.eclipse.draw2dl.graph.Node) e.source.data;
		org.eclipse.draw2dl.graph.Node target = (org.eclipse.draw2dl.graph.Node) e.target.data;
		e.delta = source.width + graph.getPadding(source).right
				+ graph.getPadding(target).left;
	}

	public void visit(org.eclipse.draw2dl.graph.DirectedGraph g) {
		graph = g;
		prime = new DirectedGraph();
		prime.nodes.add(graphLeft = new org.eclipse.draw2dl.graph.Node(null));
		prime.nodes.add(graphRight = new Node(null));
		if (g.tensorStrength != 0)
			prime.edges.add(new Edge(graphLeft, graphRight, g.tensorSize,
					g.tensorStrength));
		buildGPrime();
		new org.eclipse.draw2dl.graph.InitialRankSolver().visit(prime);
		new org.eclipse.draw2dl.graph.TightSpanningTreeSolver().visit(prime);

		org.eclipse.draw2dl.graph.RankAssignmentSolver solver = new org.eclipse.draw2dl.graph.RankAssignmentSolver();
		solver.visit(prime);
		graph.size.width = graphRight.rank;
		balanceClusters();

		prime.nodes.adjustRank(-graphLeft.rank);
		applyGPrime();
		calculateCellLocations();
	}

}
