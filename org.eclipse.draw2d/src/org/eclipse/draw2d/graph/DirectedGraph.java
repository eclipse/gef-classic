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
package org.eclipse.draw2d.graph;

import org.eclipse.draw2d.geometry.Insets;

/**
 * A graph consisting of nodes and directed edges.  A DirectedGraph serves as the input to
 * a graph layout algorithm.  The algorithm will place the graph's nodes and edges
 * according to certain goals, such as short, non-crossing edges, and readability.
 * 
 * @author hudsonr
 * @since 2.1.2
 */
public class DirectedGraph {

/**
 * For internal use only.
 */
public EdgeList spanningTree;

/**
 * The default padding to be used for nodes which don't specify any padding.  Padding is
 * the amount of empty space to be left around a node.  The default value is undefined.
 */
private Insets defaultPadding = new Insets(16);

/**
 * All of the edges in the graph. This list should be initialized with all edges that are
 * reachable from the graph's {@link #nodes}.
 */
public EdgeList edges = new EdgeList();

/**
 * For internal use only.
 */
public DirectedGraph gPrime;

/**
 * All of the nodes in the graph.
 */
public NodeList nodes = new NodeList();

/**
 * For internal use only.  The list of rows which makeup the final graph layout.
 */
public RankList ranks;

/**
 * Returns the effective padding for the given node.  If the node has a specified padding,
 * it will be used, otherwise, the graph's defaultPadding is returned.  The
 * returned value must not be modified.
 * @param node the node
 * @return the effective padding for that node
 */
public Insets getPadding(Node node) {
	Insets pad = node.getPadding();
	if (pad == null)
		return defaultPadding;
	return pad;
}

/**
 * Removes the given edge from the graph.
 * @param edge the edge to be removed
 */
public void removeEdge(Edge edge) {
	edges.remove(edge);
	edge.source.outgoing.remove(edge);
	edge.target.incoming.remove(edge);
	if (edge.vNodes != null)
		for (int j = 0; j < edge.vNodes.size(); j++)
			removeNode(edge.vNodes.getNode(j));
}

/**
 * Removes the given node from the graph.  Does not remove the node's edges.
 * @param node the node to remove
 */
public void removeNode(Node node) {
	nodes.remove(node);
	if (ranks != null)
		ranks.getRank(node.rank).remove(node);
}

/**
 * Sets the default padding for all nodes in the graph.  Padding is the empty space left
 * around the <em>outside</em> of each node.  The default padding is used for all nodes
 * which do not specify a specific amount of padding (i.e., their padding is
 * <code>null</code>).
 * @param insets the padding
 */
public void setDefaultPadding(Insets insets) {
	defaultPadding = insets;
}

}
