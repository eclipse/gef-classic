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
package org.eclipse.mylar.zest.core.internal.graphmodel;

import org.eclipse.mylar.zest.core.ZestStyles;

/**
 * Default Zest Style values for graphs, connections, and nodes.
 * @author Del Myers
 *
 */
public interface IZestGraphDefaults {
	/**
	 * Default node style.
	 */
	public static final int NODE_STYLE = ZestStyles.NODES_HIGHLIGHT_ADJACENT;
	
	/**
	 * Default connection style.
	 */
	public static final int CONNECTION_STYLE = ZestStyles.CONNECTIONS_STRAIGHT;
	
}
