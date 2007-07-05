/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylyn.zest.core.widgets.internal;

import org.eclipse.mylyn.zest.core.widgets.Graph;

/**
 * This interface describes all Zest components that are Containers. This is an internal interface
 * and thus should not be used outside of Zest.  Implementors of this interface must include the 
 * following two methods
 *   o addNode(GraphNode)
 *   o addNode(GraphContainer)
 *   
 * These are not actually listed here because Java does not allow protected methods in
 * interfaces.
 * 
 * @author Ian Bull
 */
public interface IContainer {

	public Graph getGraph();

	// All implementers must include this method
	/* protected void addNode(GraphNode node); */

	// All implementers must include this method
	/* public void addNode(GraphContainer container); */

	public int getItemType();

} // end of IContainer
