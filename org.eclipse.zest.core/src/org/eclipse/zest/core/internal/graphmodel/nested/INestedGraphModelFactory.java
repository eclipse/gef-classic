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

import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;

/**
 * @author Ian Bull
 */
public interface INestedGraphModelFactory {

	/**
	 * Creates a new graph model
	 * @return
	 */
	public abstract NestedGraphModel createModel();

	/**
	 * Creates the model using the content provider. Uses the default given node style and connection style.
	 * @param inputElement
	 * @param nodeStyle
	 * @param connectionStyle
	 * @return
	 */
	//@tag zest(bug(153348-NestedStyle(fix))) : add node style and connection style
	//	@tag zest(bug(154412-ClearStatic(fix))) : made more generic so that AbstractStylingModelFactory can do some processing in this method.
	public abstract GraphModel createModelFromContentProvider(Object inputElement, int nodeStyle, int connectionStyle);

	/**
	 * Creates a new relationship using the content provider to get the source and destination.
	 * @param model	The graph model.
	 * @param data	The data object for the new connection.
	 * @param contentProvider	The content provider which will get the source and destination nodes.
	 * @return NestedGraphModelConnection
	 */
	public abstract NestedGraphModelConnection createRelationship(NestedGraphModel model,
			Object data);

	/**
	 * Creates a new relationship and adds it to the model
	 * @param model
	 * @param source
	 * @param dest
	 * @return NestedGraphModelConnection
	 */
	public abstract NestedGraphModelConnection createRelationship(NestedGraphModel model,
			Object data, Object source, Object dest);

	/**
	 * Creates a new node and adds it to the model.  If the node already exists it is just returned.
	 * @param model	The graph model
	 * @param data	The new node's data object
	 * @return NestedGraphModelNode	the new or existing node
	 */
	public abstract NestedGraphModelNode createNode(NestedGraphModel model, Object data);

}