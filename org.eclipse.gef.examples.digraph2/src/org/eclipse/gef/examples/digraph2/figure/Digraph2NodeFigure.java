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

package org.eclipse.gef.examples.digraph2.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.examples.digraph1.figure.Digraph1NodeFigure;

/**
 * The figure for a node in the directed graph.
 * 
 * @author Anthony Hunter
 */
public class Digraph2NodeFigure extends Digraph1NodeFigure {

	/**
	 * Constructor for a Digraph2NodeFigure.
	 * 
	 * @param number
	 *            the node number in the directed graph.
	 */
	public Digraph2NodeFigure(int number) {
		super(number);
		getRectangleFigure().setBackgroundColor(ColorConstants.lightGreen);
	}

}