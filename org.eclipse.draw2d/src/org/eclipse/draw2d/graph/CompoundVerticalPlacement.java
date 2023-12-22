/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.graph;

/**
 * calculates the height and y-coordinates for nodes and subgraphs in a compound
 * directed graph.
 *
 * @author Randy Hudson
 * @since 2.1.2
 */
class CompoundVerticalPlacement extends VerticalPlacement {

	/**
	 * @see GraphVisitor#visit(DirectedGraph) Extended to set subgraph values.
	 */
	@Override
	void visit(DirectedGraph dg) {
		CompoundDirectedGraph g = (CompoundDirectedGraph) dg;
		super.visit(g);
		for (Node element : g.subgraphs) {
			Subgraph s = (Subgraph) element;
			s.y = s.head.y;
			s.height = s.tail.height + s.tail.y - s.y;
		}
	}

}
