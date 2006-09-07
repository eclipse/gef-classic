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
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;


/**
 * Extends GraphModelConnection.  The only real purpose is to change the colors
 * and the line widths.
 * @deprecated by Del Myers. This connection is no longer needed. Use regular GraphModelConnections.
 * @author Chris Callendar
 */
public class NestedGraphModelConnection extends GraphModelConnection {

	/**
	 * @param graphModel
	 * @param data
	 * @param source
	 * @param destination
	 */
	public NestedGraphModelConnection(GraphModel graphModel, Object data, IGraphModelNode source,
			IGraphModelNode destination) {
		super(graphModel, data, source, destination);
	}

	/**
	 * @param graphModel
	 * @param data
	 * @param source
	 * @param destination
	 * @param bidirection
	 * @param weight
	 */
	public NestedGraphModelConnection(GraphModel graphModel, Object data, IGraphModelNode source,
			IGraphModelNode destination, boolean bidirection, double weight) {
		super(graphModel, data, source, destination, bidirection, weight);
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getColorFromWeight()
	 *
	protected Color getColorFromWeight() {
		return ColorConstants.darkBlue;
	}*/
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getLineWidthFromWeight()
	 *
	protected int getLineWidthFromWeight() {
		return 1;
	}*/

}
