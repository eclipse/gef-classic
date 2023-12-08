/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
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

import org.eclipse.draw2d.PositionConstants;

/**
 * Performs transposing of subgraphics in a compound directed graph.
 *
 * @since 3.7.1
 */
class CompoundTransposeMetrics extends TransposeMetrics {

	@Override
	public void visit(DirectedGraph g) {
		if (g.getDirection() == PositionConstants.SOUTH) {
			return;
		}
		super.visit(g);
		int temp;
		CompoundDirectedGraph cg = (CompoundDirectedGraph) g;
		for (Node node : cg.subgraphs) {
			temp = node.width;
			node.width = node.height;
			node.height = temp;
			if (node.getPadding() != null) {
				node.setPadding(t.t(node.getPadding()));
			}
		}
	}

	@Override
	public void revisit(DirectedGraph g) {
		if (g.getDirection() == PositionConstants.SOUTH) {
			return;
		}
		super.revisit(g);
		int temp;
		CompoundDirectedGraph cg = (CompoundDirectedGraph) g;
		for (Node node : cg.subgraphs) {
			temp = node.width;
			node.width = node.height;
			node.height = temp;
			temp = node.y;
			node.y = node.x;
			node.x = temp;
			if (node.getPadding() != null) {
				node.setPadding(t.t(node.getPadding()));
			}
		}
	}

}