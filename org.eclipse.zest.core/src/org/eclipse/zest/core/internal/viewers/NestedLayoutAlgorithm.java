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
package org.eclipse.mylar.zest.core.internal.viewers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelNode;



/**
 * A simple layout algorithm that attempts to 
 * properly size a list of nodes as well as the children
 * under those nodes.
 * 
 * @author ccallendar
 */
public class NestedLayoutAlgorithm {

	//TODO no longer used.  Can delete...  ***
	
	private NoOverlapLayout noOverlapLayout;
	
	public NestedLayoutAlgorithm() {
		this.noOverlapLayout = new NoOverlapLayout();
	}
	
	/**
	 * Recursively lays out the nodes and their children.  Starts with the nodes that
	 * have no children and then traverses up the parent hierarchy laying out each node 
	 * based on the size of its children.
	 * The nodes should not overlap.
	 * @param rootNodes
	 */
	public void layout(List rootNodes) {
		if (rootNodes != null) {

			ArrayList boundsList = new ArrayList(rootNodes.size());
			for (Iterator iter = rootNodes.iterator(); iter.hasNext(); ) {
				NestedGraphModelNode node = (NestedGraphModelNode)iter.next();
				Dimension dim = node.calculateMinimumLabelSize();
				node.setSizeInLayout(dim.width, dim.height);
				
				// layout the children
				layout(node.getChildren());
				
				Dimension childSize = node.calculateMinimumSize();
				Rectangle bounds = new Rectangle(node.getLocation(), childSize);
				
				noOverlapLayout.addRectangle(boundsList, bounds);

				node.setLocationInLayout(bounds.x, bounds.y);
				// initially hide the children
				node.setSizeInLayout(bounds.width, bounds.height);
			}

		}
		
	}

}
