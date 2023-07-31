/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.graph;

import org.eclipse.draw2d.geometry.Insets;

/**
 * Converts a compound directed graph into a simple directed graph.
 *
 * @author Randy Hudson
 * @since 2.1.2
 */
class ConvertCompoundGraph extends GraphVisitor {

	private static void addContainmentEdges(CompoundDirectedGraph graph) {
		// For all nested nodes, connect to head and/or tail of containing
		// subgraph if present
		for (Node node : graph.nodes) {
			Subgraph parent = node.getParent();
			if (parent == null) {
				continue;
			}
			if (node instanceof Subgraph sub) {
				connectHead(graph, sub.head, parent);
				connectTail(graph, sub.tail, parent);
			} else {
				connectHead(graph, node, parent);
				connectTail(graph, node, parent);
			}
		}
	}

	int buildNestingTreeIndices(NodeList nodes, int base) {
		for (Node node : nodes) {
			if (node instanceof Subgraph s) {
				s.nestingTreeMin = base;
				base = buildNestingTreeIndices(s.members, base);
			}
			node.nestingIndex = base;
			base++;
		}
		return base++;
	}

	private static void connectHead(CompoundDirectedGraph graph, Node node, Subgraph parent) {
		boolean connectHead = true;
		for (int j = 0; connectHead && j < node.incoming.size(); j++) {
			Node ancestor = node.incoming.get(j).source;
			if (parent.isNested(ancestor)) {
				connectHead = false;
			}
		}
		if (connectHead) {
			Edge e = new Edge(parent.head, node);
			e.weight = 0;
			graph.edges.add(e);
			graph.containment.add(e);
		}
	}

	private static void connectTail(CompoundDirectedGraph graph, Node node, Subgraph parent) {
		boolean connectTail = true;
		for (int j = 0; connectTail && j < node.outgoing.size(); j++) {
			Node ancestor = node.outgoing.get(j).target;
			if (parent.isNested(ancestor)) {
				connectTail = false;
			}
		}
		if (connectTail) {
			Edge e = new Edge(node, parent.tail);
			e.weight = 0;
			graph.edges.add(e);
			graph.containment.add(e);
		}
	}

	private static void convertSubgraphEndpoints(CompoundDirectedGraph graph) {
		for (int i = 0; i < graph.edges.size(); i++) {
			Edge edge = graph.edges.get(i);
			if (edge.source instanceof Subgraph s) {
				Node newSource;
				if (s.isNested(edge.target)) {
					newSource = s.head;
				} else {
					newSource = s.tail;
				}
				// s.outgoing.remove(edge);
				edge.source = newSource;
				newSource.outgoing.add(edge);
			}
			if (edge.target instanceof Subgraph s) {
				Node newTarget;
				if (s.isNested(edge.source)) {
					newTarget = s.tail;
				} else {
					newTarget = s.head;
				}

				// s.incoming.remove(edge);
				edge.target = newTarget;
				newTarget.incoming.add(edge);
			}
		}
	}

	private static void replaceSubgraphsWithBoundaries(CompoundDirectedGraph graph) {
		for (int i = 0; i < graph.subgraphs.size(); i++) {
			Subgraph s = (Subgraph) graph.subgraphs.get(i);
			graph.nodes.add(s.head);
			graph.nodes.add(s.tail);
			graph.nodes.remove(s);
		}
	}

	@Override
	void revisit(DirectedGraph g) {
		for (Edge e : g.edges) {
			if (e.source instanceof SubgraphBoundary) {
				e.source.outgoing.remove(e);
				e.source = e.source.getParent();
			}
			if (e.target instanceof SubgraphBoundary) {
				e.target.incoming.remove(e);
				e.target = e.target.getParent();
			}
		}
	}

	/**
	 * @see GraphVisitor#visit(org.eclipse.draw2d.graph.DirectedGraph)
	 */
	@Override
	public void visit(DirectedGraph dg) {
		CompoundDirectedGraph graph = (CompoundDirectedGraph) dg;

		NodeList roots = new NodeList();
		// Find all subgraphs and root subgraphs
		for (Node node : graph.nodes) {
			if (node instanceof Subgraph s) {
				Insets padding = dg.getPadding(s);
				s.head = new SubgraphBoundary(s, padding, 0);
				s.tail = new SubgraphBoundary(s, padding, 2);
				Edge headToTail = new Edge(s.head, s.tail);
				headToTail.weight = 10;
				graph.edges.add(headToTail);
				graph.containment.add(headToTail);

				graph.subgraphs.add(s);
				if (s.getParent() == null) {
					roots.add(s);
				}
				if (s.members.size() == 2) { // The 2 being the head and tail only
					graph.edges.add(new Edge(s.head, s.tail));
				}
			}
		}

		buildNestingTreeIndices(roots, 0);
		convertSubgraphEndpoints(graph);
		addContainmentEdges(graph);
		replaceSubgraphsWithBoundaries(graph);
	}

}
