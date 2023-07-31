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

/**
 * Inverts any edges which are marked as backwards or "feedback" edges.
 *
 * @author Daniel Lee
 * @since 2.1.2
 */
class InvertEdges extends GraphVisitor {

	/**
	 *
	 * @see GraphVisitor#visit(org.eclipse.draw2d.graph.DirectedGraph)
	 */
	@Override
	public void visit(DirectedGraph g) {
		g.edges.stream().filter(Edge::isFeedback).forEach(Edge::invert);
	}

}
