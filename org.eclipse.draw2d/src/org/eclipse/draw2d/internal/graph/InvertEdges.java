/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;

/**
 * Graph visitors run prior to this visitor may have inverted edges to remove cycles
 * in the graph. This visitor flips all such edges back to their original orientation.
 * 
 * @author Daniel Lee
 */
public class InvertEdges extends GraphVisitor {

/**
 * 
 * @see GraphVisitor#visit(org.eclipse.draw2d.graph.DirectedGraph)
 */
public void visit(DirectedGraph g) {
	for (int i = 0; i < g.edges.size(); i++) {
		Edge e = g.edges.getEdge(i);
		if (e.isFeedback)
			e.invert();
	}
}

}
