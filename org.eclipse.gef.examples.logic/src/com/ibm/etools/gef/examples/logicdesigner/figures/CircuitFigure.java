package com.ibm.etools.gef.examples.logicdesigner.figures;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.gef.examples.logicdesigner.model.*;
import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import org.eclipse.swt.graphics.Color;

public class CircuitFigure 
	extends NodeFigure 
{

private IFigure pane;

public CircuitFigure() {
	setBorder(new CircuitBorder());
	ScrollPane scrollpane = new ScrollPane();
	pane = new FreeformLayer();
	pane.setLayoutManager(new FreeformLayout());
	setLayoutManager(new StackLayout());
	add(scrollpane);
	scrollpane.setViewport(new FreeformViewport());
	scrollpane.setView(pane);

	createConnectionAnchors();
	setBackgroundColor(ColorConstants.buttonLightest);
	setOpaque(true);
}

protected void createConnectionAnchors() {
	FixedConnectionAnchor in,out;
	for (int i=0; i<8; i++){
		in = new FixedConnectionAnchor(this);
		out= new FixedConnectionAnchor(this);
		if (i > 3){
			in.topDown = false;
			in.offsetV = getInsets().top;
			out.topDown= false;
		} else {
			out.offsetV = getInsets().top;
		}
		setOutputConnectionAnchor(i,out);
		setInputConnectionAnchor(i,in);
		outputConnectionAnchors.addElement(out);
		inputConnectionAnchors.addElement(in);
	}
}

public Color getBorderColor(){
	return ((CircuitBorder)getBorder()).getBorderColor();
}

public IFigure getContentsPane(){
	return pane;
}

protected FixedConnectionAnchor getInputConnectionAnchor(int i) {
	return (FixedConnectionAnchor) connectionAnchors.get(Circuit.TERMINALS_IN[i]);
}

protected FixedConnectionAnchor getOutputConnectionAnchor(int i) {
	return (FixedConnectionAnchor) connectionAnchors.get(Circuit.TERMINALS_OUT[i]);
}

public Dimension getPreferredSize() {
	Dimension prefSize = super.getPreferredSize();
	int connBuffer = 10;
	//add a buffer to each side to account for connections being drawn
	prefSize = new Dimension(prefSize.width + connBuffer, prefSize.height + connBuffer);
	Dimension defaultSize = new Dimension(100,100);
	prefSize.union(defaultSize);
	return prefSize;
}

protected void layoutConnectionAnchors() {
	int innerwidth = getBounds().width - getInsets().right*2;
	int x;
	for (int i = 0; i < 4; i++){
		x = (2*i+1) * getSize().width / 8;
		getOutputConnectionAnchor(i+4).offsetH = x;
		getInputConnectionAnchor(i).offsetH = x;
		x = getInsets().right + (2*i+1) * innerwidth / 8;
		getInputConnectionAnchor(i+4).offsetH = x;
		getOutputConnectionAnchor(i).offsetH = x;
	}
}

public void setBorderColor(Color c){
	((CircuitBorder)getBorder()).setBorderColor(c);
	repaint();
}

public void setInputConnectionAnchor(int i, ConnectionAnchor c) {
	connectionAnchors.put(Circuit.TERMINALS_IN[i],c);
}

public void setOutputConnectionAnchor(int i, ConnectionAnchor c) {
	connectionAnchors.put(Circuit.TERMINALS_OUT[i],c);
}

public String toString() {
	return "CircuitBoardFigure"; //$NON-NLS-1$
}

public void validate() {
	if(isValid()) return;
	layoutConnectionAnchors();
	super.validate();
}

protected boolean useLocalCoordinates(){return true;}

}