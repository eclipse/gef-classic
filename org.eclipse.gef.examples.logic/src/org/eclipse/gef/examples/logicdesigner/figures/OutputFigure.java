package org.eclipse.gef.examples.logicdesigner.figures;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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