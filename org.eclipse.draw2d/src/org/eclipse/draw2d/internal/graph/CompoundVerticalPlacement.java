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

import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Subgraph;

/**
 * calculates the height and y-coordinates for nodes and subgraphs in a compound directed
 * graph.
 * @author Randy Hudson
 * @since 2.1.2
 */
public class CompoundVerticalPlacement extends VerticalPlacement {

/**
 * @see GraphVisitor#visit(DirectedGraph)
 * Extended to set subgraph values.
 */
public void visit(DirectedGraph dg) {
	CompoundDirectedGraph g = (CompoundDirectedGraph)dg;
	super.visit(g);
	for (int i = 0; i < g.subgraphs.size(); i++) {
		Subgraph s = (Subgraph)g.subgraphs.get(i);
		s.y = s.head.y;
		s.height = s.tail.height + s.tail.y - s.y;
	}
}

}
