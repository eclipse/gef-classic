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

package org.eclipse.gef.examples.digraph2.editpart;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.examples.digraph1.editpart.Digraph1GraphEditPart;

/**
 * The edit part for the directed graph.
 * 
 * @author Anthony Hunter
 */
public class Digraph2GraphEditPart extends Digraph1GraphEditPart {

	/*
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		ConnectionLayer connectionLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
		connectionLayer.setConnectionRouter(new ManhattanConnectionRouter());
	}

}
