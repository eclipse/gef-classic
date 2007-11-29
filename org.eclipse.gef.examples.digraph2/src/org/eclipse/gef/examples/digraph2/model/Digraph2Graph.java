/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.gef.examples.digraph2.model;

import org.eclipse.gef.examples.digraph1.model.Digraph1Graph;

/**
 * The graph model object which describes the list of nodes in the directed
 * graph.
 * 
 * @author Anthony Hunter
 */
public class Digraph2Graph extends Digraph1Graph {

	/*
	 * @see org.eclipse.gef.examples.digraph1.model.Digraph1Graph#createNodes()
	 */
	@Override
	protected void createNodes() {
		for (int i = 0; i < this.COUNT; i++) {
			Digraph2Node node = new Digraph2Node(i);
			getNodes().add(node);
			if (i != 0) {
				Digraph2Edge edge = new Digraph2Edge();
				edge.setSource((Digraph2Node)getNodes().get(i - 1));
				edge.setTarget(node);
			}
		}
	}
}
