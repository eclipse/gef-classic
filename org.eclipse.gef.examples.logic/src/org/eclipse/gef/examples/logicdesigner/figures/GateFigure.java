package org.eclipse.gef.examples.logicdesigner.figures;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.examples.logicdesigner.model.Gate;

public class GateFigure  
	extends OutputFigure 
{

public GateFigure() {
	FixedConnectionAnchor inputConnectionAnchorA, inputConnectionAnchorB;
	inputConnectionAnchorA = new FixedConnectionAnchor(this);
	inputConnectionAnchorA.offsetH = 4;
	inputConnectionAnchorB = new FixedConnectionAnchor(this);
	inputConnectionAnchorB.offsetH =11;
	inputConnectionAnchors.addElement(inputConnectionAnchorA);
	inputConnectionAnchors.addElement(inputConnectionAnchorB);
	connectionAnchors.put(Gate.TERMINAL_A, inputConnectionAnchorA);
	connectionAnchors.put(Gate.TERMINAL_B, inputConnectionAnchorB);
}

public String toString(){
	return "GateFigure"; //$NON-NLS-1$
}

}