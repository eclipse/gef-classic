package org.eclipse.graph;

import java.util.*;

/**
 * @author hudsonr
 * @since 2.1
 */
public class RankList {

ArrayList ranks = new ArrayList();

public Rank getRank(int rank) {
	while (ranks.size() <= rank)
		ranks.add(new Rank());
	return (Rank)ranks.get(rank);
}

public int size() {
	return ranks.size();
}

}
