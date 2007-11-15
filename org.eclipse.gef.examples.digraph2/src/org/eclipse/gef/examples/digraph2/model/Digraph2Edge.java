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

/**
 * The edge model object which describes an edge in the directed graph.
 * 
 * @author Anthony Hunter
 */
public class Digraph2Edge {

	/**
	 * The source of the edge is connected to this node.
	 */
	private Digraph2Node source;

	/**
	 * The target of the edge is connected to this node.
	 */
	private Digraph2Node target;

	/**
	 * Get the source node for this edge.
	 * 
	 * @return the source node for this edge.
	 */
	public Digraph2Node getSource() {
		return this.source;
	}

	/**
	 * Get the target node for this edge.
	 * 
	 * @return the target node for this edge.
	 */
	public Digraph2Node getTarget() {
		return this.target;
	}

	/**
	 * Set the source node for this edge.
	 * 
	 * @param newSource
	 *            the source node.
	 */
	public void setSource(Digraph2Node newSource) {
		if (this.source != null) {
			this.source.removeSourceEdge(this);
		}
		this.source = newSource;
		if (this.source != null) {
			this.source.addSourceEdge(this);
		}
	}

	/**
	 * Set the target node for this edge.
	 * 
	 * @param newTarget
	 *            the target node.
	 */
	public void setTarget(Digraph2Node newTarget) {
		if (this.target != null) {
			this.target.removeTargetEdge(this);
		}
		this.target = newTarget;
		if (this.target != null) {
			this.target.addTargetEdge(this);
		}
	}
}
