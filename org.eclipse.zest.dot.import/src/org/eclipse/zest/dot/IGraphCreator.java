/*******************************************************************************
 * Copyright (c) 2009, 2010 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.dot;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;

/**
 * Common interface for different ways to create a Zest Graph instance from DOT.
 * 
 * @author Fabian Steeg (fsteeg)
 */
interface IGraphCreator {

	/**
	 * @param parent
	 *            The parent for the graph
	 * @param style
	 *            The style bits for the graph
	 * @param dot
	 *            The parsed DOT AST to import
	 * @return The new graph
	 */
	Graph create(Composite parent, int style, DotAst dot);

}
