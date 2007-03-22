/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.mylar.zest.core.widgets;

/**
 * Simple base interface for graph items to allow property change support. The
 * properties that change are dependant on the particular graph item.
 * 
 * @author Del Myers
 * 
 */
//@tag bug(154259-Abstraction(fix))
public interface IGraphItem {

	// These constants define what type of item this is
	public static final int GRAPH = 1;
	public static final int NODE = 2;
	public static final int CONNECTION = 3;

	/**
	 * The data for this item. Normally the user's model object.
	 * 
	 * @return
	 */
	public Object getData();

	/**
	 * Set the visibility of this item.
	 * 
	 * @param visible
	 *            whether or not this item is visible.
	 */
	public void setVisible(boolean visible);

	/**
	 * Get the visibility of this item.
	 * 
	 * @return the visibility of this item.
	 */
	public boolean isVisible();

	/**
	 * Gets the graph that this item is rooted on. If this item is itself a
	 * graph, then this is returned.
	 * 
	 * @return the parent graph.
	 */
	public Graph getGraphModel();

	/**
	 * Removes the item from its parent
	 */
	public void dispose();

	/**
	 * Checks if this item is disposed
	 * 
	 * @return
	 */
	public boolean isDisposed();

	/**
	 * Sets the data associated with this node
	 * 
	 * @param data
	 */
	public void setData(Object data);

	/**
	 * Gets the graph item type. The item type is one of: GRAPH, NODE or
	 * CONNECTION
	 * 
	 * @return
	 */
	public abstract int getItemType();

}
