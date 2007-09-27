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

package org.eclipse.gef.examples.digraph1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The graph model object which describes the list of nodes in the directed
 * graph.
 * 
 * @author Anthony Hunter
 */
public class Digraph1Graph {

	/**
	 * A fixed number of nodes in this directed graph.
	 */
	private int COUNT = 5;

	/**
	 * The list of nodes in the graph.
	 */
	private List<Digraph1Node> nodes = new ArrayList<Digraph1Node>();

	/**
	 * Constructor for a Digraph1Graph.
	 */
	public Digraph1Graph() {
		for (int i = 0; i < this.COUNT; i++) {
			Digraph1Node node = new Digraph1Node(i);
			this.nodes.add(node);
		}
	}

	/**
	 * Get the list of nodes.
	 * 
	 * @return the list of nodes.
	 */
	public List<Digraph1Node> getNodes() {
		return this.nodes;
	}
}
