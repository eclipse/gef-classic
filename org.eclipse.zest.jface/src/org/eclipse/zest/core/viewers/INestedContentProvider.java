/*******************************************************************************
 * Copyright (c) 2009-2010, 2023 EclipseSource and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.zest.core.viewers;

/**
 * A content provider for nested graphs. Any entity based content provider
 * (IGraphEntityContentProvider or IGraphEntityRelationshipContentProvider) can
 * also implement this interface. Any node that "hasChildren" will be rendered
 * as a container.
 *
 * Note: Containers cannot contain other containers.
 *
 * @author irbull
 */
public interface INestedContentProvider {

	/**
	 * Does the current node have children? If so, it will be rendered as a
	 * container.
	 *
	 * @param element The current node
	 * @return True if it has children, false otherwise
	 */
	public boolean hasChildren(Object element);

	/**
	 * Gets the children of this node. This method will not be called if hasChildren
	 * returns false.
	 *
	 * @param element The current node
	 * @return The list of children for this node.
	 */
	public Object[] getChildren(Object element);
}
