/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
import org.eclipse.draw2d.graph.SubgraphBoundary;

/**
 * @author hudsonr
 */
public class RestoreCompoundGraph extends GraphVisitor {

public void visit(DirectedGraph g) {
	for (int i = 0; i < g.edges.size(); i++) {
		Edge e = g.edges.getEdge(i);
		if (e.source instanceof SubgraphBoundary) {
			e.source.outgoing.remove(e);
			e.source = e.source.getParent();
		}
		if (e.target instanceof SubgraphBoundary) {
			e.target.incoming.remove(e);
			e.target= e.target.getParent();
		}
	}
}

}
