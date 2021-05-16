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

/**
 * calculates the height and y-coordinates for nodes and subgraphs in a compound
 * directed graph.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
class CompoundVerticalPlacement extends org.eclipse.draw2dl.graph.VerticalPlacement {

	/**
	 * @see org.eclipse.draw2dl.graph.GraphVisitor#visit(org.eclipse.draw2dl.graph.DirectedGraph) Extended to set subgraph values.
	 */
	void visit(DirectedGraph dg) {
		org.eclipse.draw2dl.graph.CompoundDirectedGraph g = (CompoundDirectedGraph) dg;
		super.visit(g);
		for (int i = 0; i < g.subgraphs.size(); i++) {
			org.eclipse.draw2dl.graph.Subgraph s = (Subgraph) g.subgraphs.get(i);
			s.y = s.head.y;
			s.height = s.tail.height + s.tail.y - s.y;
		}
	}

}
