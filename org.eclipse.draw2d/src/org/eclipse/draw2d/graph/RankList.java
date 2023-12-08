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
import java.util.Iterator;

/**
 * For internal use only. A list of lists.
 *
 * @author hudsonr
 * @since 2.1.2
 */
public final class RankList implements Iterable<Rank> {

	ArrayList<Rank> ranks = new ArrayList<>();

	/**
	 * Returns the specified rank.
	 *
	 * @param rank the row
	 * @return the rank
	 */
	public Rank getRank(int rank) {
		while (ranks.size() <= rank) {
			ranks.add(new Rank());
		}
		return ranks.get(rank);
	}

	/**
	 * Returns the total number or ranks.
	 *
	 * @return the total number of ranks
	 */
	public int size() {
		return ranks.size();
	}

	@Override
	public Iterator<Rank> iterator() {
		return ranks.iterator();
	}

}
