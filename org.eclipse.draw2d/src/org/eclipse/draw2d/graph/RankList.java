package org.eclipse.draw2d.graph;

import java.util.*;

/**
 * For internal use only.  A list of lists.
 * @author hudsonr
 * @since 2.1
 */
public class RankList {

ArrayList ranks = new ArrayList();

/**
 * Returns the specified rank.
 * @param rank the row
 * @return the rank
 */
public Rank getRank(int rank) {
	while (ranks.size() <= rank)
		ranks.add(new Rank());
	return (Rank)ranks.get(rank);
}

/**
 * Returns the total number or ranks.
 * @return the total number of ranks
 */
public int size() {
	return ranks.size();
}

}
