/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.figures;

import org.eclipse.draw2d.StackLayout;
import org.eclipse.gef.examples.logicdesigner.model.SimpleOutput;

public class OutputFigure 
	extends NodeFigure 
{
		
public OutputFigure() {
	FixedConnectionAnchor outputConnectionAnchor = new FixedConnectionAnchor(this);
	outputConnectionAnchor.topDown = false;
	outputConnectionAnchor.offsetH = 7;
	outputConnectionAnchors.addElement(outputConnectionAnchor);
	connectionAnchors.put(SimpleOutput.TERMINAL_OUT, outputConnectionAnchor);
	setLayoutManager(new StackLayout());
}

}