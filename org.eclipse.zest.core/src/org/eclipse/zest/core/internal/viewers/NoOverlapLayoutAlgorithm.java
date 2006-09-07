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
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;
import org.eclipse.mylar.zest.layouts.NestedLayoutEntity;


/**
 * A simple layout algorithm that attempts to properly size a 
 * list of nodes as well as any children under those nodes.
 * 
 * @author Chris Callendar
 */
public class NoOverlapLayoutAlgorithm {

	private NoOverlapLayout noOverlapLayout;
	
	/**
	 * Initializes this algorithm.
	 */
	public NoOverlapLayoutAlgorithm() {
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
				IGraphModelNode node = (IGraphModelNode)iter.next();
				Dimension dim = node.calculateMinimumLabelSize();
				node.setSizeInLayout(dim.width, dim.height);
				
				if (node instanceof NestedLayoutEntity) {
					// layout the children
					layout(((NestedLayoutEntity)node).getChildren());
				}
				
				Dimension childSize = node.calculateMinimumSize();
				Rectangle rect = new Rectangle(node.getLocation(), childSize);
				
				noOverlapLayout.addRectangle(boundsList, rect);

				// ensure that the new position is inside the bounds
				// TODO causes overlapping, so not very useful
//				if (enforeBounds && (bounds != null) && !bounds.isEmpty()) {
//					if (rect.right() > bounds.right()) {
//						rect.x = bounds.right() - rect.width;
//					}
//					if (rect.bottom() > bounds.bottom()) {
//						rect.y = bounds.bottom() - rect.height;
//					}
//					if (rect.x < bounds.x) {
//						rect.x = bounds.x;
//					}
//					if (rect.y < bounds.y) {
//						rect.y = bounds.y;
//					}
//				}
				
				node.setLocationInLayout(rect.x, rect.y);
				node.setSizeInLayout(rect.width, rect.height);
			}

		}
		
	}

}
