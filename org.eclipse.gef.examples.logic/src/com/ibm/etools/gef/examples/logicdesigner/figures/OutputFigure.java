package com.ibm.etools.gef.examples.logicdesigner.figures;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import org.eclipse.swt.graphics.Image;
import com.ibm.etools.gef.examples.logicdesigner.model.*;

import java.util.Vector;

public class OutputFigure 
	extends NodeFigure 
{
		
public OutputFigure() {
	FixedConnectionAnchor outputConnectionAnchor = new FixedConnectionAnchor(this);
	outputConnectionAnchor.topDown = false;
	outputConnectionAnchor.offsetH = 8;
	outputConnectionAnchors.addElement(outputConnectionAnchor);
	connectionAnchors.put(SimpleOutput.TERMINAL_OUT, outputConnectionAnchor);
	setLayoutManager(new StackLayout());
}

public void setImage (Image image) {
	if(image == null) return;
	add(new Label(image));
}

}