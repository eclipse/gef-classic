/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.pde;

import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;

/**
 * @author hudsonr
 * @since 2.1
 */
public class PluginEdge extends Edge {

public boolean exported;

public PluginEdge (Node source, Node target, boolean exported) {
	super(source, target);
//	if (source.data.equals("Eclipse UI") && Math.random() > 0.4)
//		delta = 2;
	this.exported = exported;
}

}
