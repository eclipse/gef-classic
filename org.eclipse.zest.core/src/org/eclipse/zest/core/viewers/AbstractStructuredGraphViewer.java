/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.viewers;

import org.eclipse.mylar.zest.core.ZestException;
import org.eclipse.mylar.zest.core.ZestPlugin;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.graphmodel.IZestGraphDefaults;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;

/**
 * Abstraction of graph viewers to implement functionality used by all of them.
 * Not intended to be implemented by clients. Use one of the provided children
 * instead.
 * @author Del Myers
 *
 */
public abstract class AbstractStructuredGraphViewer extends AbstractZoomableViewer {
	/**
	 * Contains top-level styles for the entire graph. Set in the constructor.	 * 
	 */
	private int graphStyle;
	
	/**
	 * Contains node-level styles for the graph. Set in setNodeStyle(). Defaults
	 * are used in the constructor.
	 */
	private int nodeStyle;
	
	/**
	 * Contains arc-level styles for the graph. Set in setConnectionStyle(). Defaults
	 * are used in the constructor.
	 */
	private int connectionStyle;
	
	
	AbstractStructuredGraphViewer(int graphStyle) {
		this.graphStyle = graphStyle;
		this.connectionStyle = IZestGraphDefaults.CONNECTION_STYLE;
		this.nodeStyle = IZestGraphDefaults.NODE_STYLE;
	}
	
	/**
	 * Sets the default style for nodes in this graph. Note: if an input
	 * is set on the viewer, a ZestException will be thrown.
	 * @param nodeStyle the style for the nodes.
	 * @see #ZestStyles
	 */
	public void setNodeStyle(int nodeStyle) {
		if (getInput() != null) ZestPlugin.error(ZestException.ERROR_CANNOT_SET_STYLE);
		this.nodeStyle = nodeStyle;
	}
	
	/**
	 * Sets the default style for connections in this graph. Note: if an input
	 * is set on the viewer, a ZestException will be thrown.
	 * @param connectionStyle the style for the connections.
	 * @see #ZestStyles
	 */
	public void setConnectionStyle(int connectionStyle) {
		if (getInput() != null) ZestPlugin.error(ZestException.ERROR_CANNOT_SET_STYLE);
		if (!ZestStyles.validateConnectionStyle(connectionStyle)) ZestPlugin.error(ZestException.ERROR_INVALID_STYLE);
		this.connectionStyle = connectionStyle;
	}
	
	
	/**
	 * Returns the style set for the graph
	 * @return The style set of the graph
	 */ 
	public int getGraphStyle() {
		return graphStyle;
	}
	
	/**
	 * Returns the style set for the nodes.
	 * @return the style set for the nodes.
	 */
	public int getNodeStyle() {
		return nodeStyle;
	}
	
	/**
	 * @return the connection style.
	 */
	public int getConnectionStyle() {
		return connectionStyle;
	}
	
	/**
	 * Sets the layout algorithm for this viewer. Subclasses may place restrictions on the
	 * algorithms that it accepts.
	 * @param algorithm the layout algorithm
	 * @param run true if the layout algorithm should be run immediately. This is a hint.
	 */
	public abstract void setLayoutAlgorithm(LayoutAlgorithm algorithm, boolean run);
	
	/**
	 * Equivalent to setLayoutAlgorithm(algorithm, false).
	 * @param algorithm
	 */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm) {
		setLayoutAlgorithm(algorithm, false);
	}
	
	

}
