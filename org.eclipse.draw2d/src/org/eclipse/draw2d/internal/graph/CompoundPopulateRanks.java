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

import org.eclipse.draw2d.graph.*;

/**
 * @author hudsonr
 */
public class CompoundPopulateRanks extends PopulateRanks {

/**
 * @see org.eclipse.graph.PopulateRanks#createVirtualNode(org.eclipse.graph.Edge, int)
 */
VirtualNode createVirtualNode(Edge e, int i) {
	VirtualNode n = super.createVirtualNode(e, i);
	Subgraph s = GraphUtilities.getCommonAncestor(e.source, e.target);
	if (s != null) {
		n.setParent(s);
		s.members.add(n);
		n.nestingIndex = s.nestingIndex;
	}
	return n;
}

}
