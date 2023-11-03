/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and others.
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

import java.util.ArrayList;

/**
 * A list of <code>Edge</code>s.
 *
 * @author hudsonr
 * @since 2.1.2
 */
public class EdgeList extends ArrayList<Edge> {

	/**
	 * Returns the edge for the given index.
	 *
	 * @param index the index of the requested edge
	 * @return the edge at the given index
	 *
	 * @deprecated use Use {@link #get(int)} instead.
	 */
	@Deprecated(since = "3.14", forRemoval = true)
	public Edge getEdge(int index) {
		return super.get(index);
	}

	/**
	 * For intrenal use only.
	 *
	 * @param i and index
	 * @return a value
	 */
	public int getSourceIndex(int i) {
		return get(i).source.index;
	}

	/**
	 * For internal use only.
	 *
	 * @param i an index
	 * @return a value
	 */
	public int getTargetIndex(int i) {
		return get(i).target.index;
	}

	/**
	 * For internal use only.
	 *
	 * @return the minimum slack for this edge list
	 */
	public int getSlack() {
		return stream().mapToInt(Edge::getSlack).min().orElse(Integer.MAX_VALUE);
	}

	/**
	 * For internal use only.
	 *
	 * @return the total weight of all edges
	 */
	public int getWeight() {
		return stream().mapToInt(e -> e.weight).sum();
	}

	/**
	 * For internal use only
	 *
	 * @return <code>true</code> if completely flagged
	 */
	public boolean isCompletelyFlagged() {
		return stream().allMatch(e -> e.flag);
	}

	/**
	 * For internal use only. Resets all flags.
	 *
	 * @param resetTree internal
	 */
	public void resetFlags(boolean resetTree) {
		forEach(e -> {
			e.flag = false;
			if (resetTree) {
				e.tree = false;
			}
		});
	}

	/**
	 * For internal use only.
	 *
	 * @param value value
	 */
	public void setFlags(boolean value) {
		forEach(e -> e.flag = value);
	}

}
