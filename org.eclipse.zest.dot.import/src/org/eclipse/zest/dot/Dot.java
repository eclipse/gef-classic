/*******************************************************************************
 * Copyright (c) 2010 Fabian Steeg. All rights reserved. This program and
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
 * A Zest Graph that can be be built using Graphviz DOT.
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class Dot extends Graph {

	/**
	 * @param dot
	 *            The DOT graph (e.g. "graph{1--2}") or snippet (e.g. "1->2")
	 * @param parent
	 *            The parent to create the graph in
	 * @param style
	 *            The style bits
	 */
	public Dot(String dot, Composite parent, int style) {
		super(parent, style);
		new GraphCreatorInterpreter().create(new DotImport(dot).getDotAst(),
				this);
	}

	/**
	 * @param dot
	 *            The DOT snippet (e.g. "1->2") to add to this graph
	 */
	public void add(String dot) {
		new DotImport(dot).into(this);
	}

}
