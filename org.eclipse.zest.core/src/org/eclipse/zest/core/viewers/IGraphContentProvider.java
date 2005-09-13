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

import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 * A graph content provider. 
 * 
 * @author Ian Bull
 */
public interface IGraphContentProvider extends IStructuredContentProvider {

	
	/**
	 * Returns all the connections to be displayed
	 * @return
	 */
	public Object[] getRelationships();
	
	/**
	 * Gets the source object
	 * @param rel
	 * @return
	 */
	public Object getSource( Object rel );
	
	/**
	 * Gets the target Object
	 * @param rel
	 * @return
	 */
	public Object getDestination( Object rel );
	
	/**
	 * Gets the elements
	 */
	public Object[] getElements( Object o );

	/**
	 * Gets the weight of an edge given the connection object.  The weight will be either -1 or 
	 * a double between 0.0 and 1.0
	 * @param connection	The connection data object.
	 * @return double
	 */
	public double getWeight(Object connection);

}
