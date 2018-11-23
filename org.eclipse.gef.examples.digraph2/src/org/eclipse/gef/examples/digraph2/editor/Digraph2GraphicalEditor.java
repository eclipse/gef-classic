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

package org.eclipse.gef.examples.digraph2.editor;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.examples.digraph1.editor.Digraph1GraphicalEditor;
import org.eclipse.gef.examples.digraph2.factory.Digraph2EditPartFactory;
import org.eclipse.gef.examples.digraph2.model.Digraph2Graph;

/**
 * The graphical editor for the directed graph.
 * 
 * @author Anthony Hunter
 */
public class Digraph2GraphicalEditor extends Digraph1GraphicalEditor {

	/*
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();

		viewer.setEditPartFactory(new Digraph2EditPartFactory());
	}

	/*
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	@Override
	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setContents(new Digraph2Graph());
	}

}
