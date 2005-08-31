/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphmodel.nested;

import java.util.Comparator;


/**
 * Compares NestedGraphModelNode objects based on how many children a node has.
 * Nodes with the most children will be first and nodes with no children will be last.
 * It also sorts nodes with the same number of children alphabetically.
 * 
 * @author ccallendar
 */
public class NodeChildrenComparator implements Comparator {

	public NodeChildrenComparator() {
		super();
	}

	/**
	 * Compares two NestedGraphModelNode objects based on the number of
	 * children each one has.  The node with more children will be first.
	 * If the nodes have the same number of children then the nodes are sorted
	 * by name (getText()). 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		if ((o1 instanceof NestedGraphModelNode) && (o2 instanceof NestedGraphModelNode)) {
			return compare((NestedGraphModelNode)o1, (NestedGraphModelNode)o2);
		}
		return 0;
	}
	
	private int compare(NestedGraphModelNode node1, NestedGraphModelNode node2) {
		int rv = 0;
		int c1 = node1.getChildren().size();
		int c2 = node2.getChildren().size();
		if (c1 != c2) {
			rv = c2 - c1;	// put the node with more children first
		} else {
			String t1 = node1.getText();
			String t2 = node2.getText();
			if (t1 != null) {
				rv = t1.compareTo(t2);
			}
		}
		return rv;
	}

}
