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
package org.eclipse.mylar.zest.core.internal.viewers;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.swt.widgets.Composite;


/**
 * The Graph class is a Canvas used for displaying graph items 
 * such as nodes and edges.
 * 
 * @author Chris Callendar
 */
public class Graph extends FigureCanvas {

	/**
	 * @param parent
	 */
	public Graph(Composite parent) {
		super(parent);
		setScrollBarVisibility(FigureCanvas.NEVER);
	}

	

}
