/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.gef.examples.digraph2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The graph model object which describes the list of nodes in the directed
 * graph.
 * 
 * @author Anthony Hunter
 */
public class Digraph2Graph {

	/**
	 * A fixed number of nodes in this directed graph.
	 */
	private int COUNT = 5;

	/**
	 * The list of nodes in the graph.
	 */
	private List<Digraph2Node> nodes = new ArrayList<Digraph2Node>();

	/**
	 * Constructor for a Digraph2Graph.
	 */
	public Digraph2Graph() {
		for (int i = 0; i < this.COUNT; i++) {
			Digraph2Node node = new Digraph2Node(i);
			this.nodes.add(node);
			if (i != 0) {
				Digraph2Edge edge = new Digraph2Edge();
				edge.setSource(this.nodes.get(i - 1));
				edge.setTarget(node);
			}
		}
	}

	/**
	 * Get the list of nodes.
	 * 
	 * @return the list of nodes.
	 */
	public List<Digraph2Node> getNodes() {
		return this.nodes;
	}
}
