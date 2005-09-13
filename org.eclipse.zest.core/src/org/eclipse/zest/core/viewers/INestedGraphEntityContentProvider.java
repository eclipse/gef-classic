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
package org.eclipse.mylar.zest.core.viewers;

import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * Extends {@link org.eclipse.mylar.zest.core.viewers.IGraphEntityContentProvider} to provide methods
 * for getting the parent and children nodes.
 * 
 * @author Chris Callendar
 */
public interface INestedGraphEntityContentProvider extends IGraphEntityContentProvider, ITreeContentProvider {

	/**
	 * Gets the parent for the given node.
	 * @param node	The node object.
	 * @return The parent object.
	 */
	public Object getParent(Object node);
	
	/**
	 * Gets the children for the given node.
	 * @param node	The node object.
	 * @return An array of the children objects.
	 */
	public Object[] getChildren(Object node);
	
	/**
	 * Returns if there are children for the given node.
	 * @param node the node in question
	 * @return boolean if the node has children
	 */
	public boolean hasChildren(Object node);
	
}
