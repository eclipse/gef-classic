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
package org.eclipse.mylar.zest.core.internal.graphmodel;

/**
 * @author Ian Bull
 */
public interface IGraphModelFactory {

	/**
	 * Creates a new graph model
	 * @return
	 */
	public abstract GraphModel createModel();

	public abstract GraphModel createModelFromContentProvider(
			Object inputElement);

	/**
	 * Creates a new relationship using the content provider to get the source and destination.
	 * @param model	The graph model.
	 * @param data	The data object for the new connection.
	 * @param contentProvider	The content provider which will get the source and destination nodes.
	 * @return GraphModelConnection
	 */
	public abstract GraphModelConnection createRelationship(GraphModel model,
			Object data);

	/**
	 * Creates a new relationship and adds it to the model
	 * @param model
	 * @param source
	 * @param dest
	 * @return GraphModelConnection
	 */
	public abstract GraphModelConnection createRelationship(GraphModel model,
			Object data, Object source, Object dest);

	/**
	 * Creates a new node and adds it to the model.  If the node already exists it is just returned.
	 * @param model	The graph model
	 * @param data	The new node's data object
	 * @return GraphModelNode	the new or existing node
	 */
	public abstract GraphModelNode createNode(GraphModel model, Object data);

}