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

import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.handles.HandleBounds;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

public class CircuitFigure
	extends NodeFigure
	implements HandleBounds
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
	scrollpane.setContents(pane);

	createConnectionAnchors();
	setBackgroundColor(ColorConstants.listBackground);
	setOpaque(true);
}

protected void createConnectionAnchors() {
	FixedConnectionAnchor in,out;
	for (int i=0; i<8; i++){
		in = new FixedConnectionAnchor(this);
		out= new FixedConnectionAnchor(this);
		if (i > 3){
			in.topDown = false;
			in.offsetV = 5;
			out.topDown= false;
		} else {
			out.offsetV = 5;
		}
		setOutputConnectionAnchor(i,out);
		setInputConnectionAnchor(i,in);
		outputConnectionAnchors.addElement(out);
		inputConnectionAnchors.addElement(in);
	}
}

public IFigure getContentsPane(){
	return pane;
}

protected FixedConnectionAnchor getInputConnectionAnchor(int i) {
	return (FixedConnectionAnchor) connectionAnchors.get(Circuit.TERMINALS_IN[i]);
}

/**
 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
 */
public Rectangle getHandleBounds() {
	return getBounds().getCropped(new Insets(2,0,2,0));
}

protected FixedConnectionAnchor getOutputConnectionAnchor(int i) {
	return (FixedConnectionAnchor) connectionAnchors.get(Circuit.TERMINALS_OUT[i]);
}

public Dimension getPreferredSize(int w, int h) {
	Dimension prefSize = super.getPreferredSize(w, h);
	Dimension defaultSize = new Dimension(100,100);
	prefSize.union(defaultSize);
	return prefSize;
}

protected void layoutConnectionAnchors() {
	int x;
	for (int i = 0; i < 4; i++){
		x = (2*i+1) * getSize().width / 8;
		getOutputConnectionAnchor(i+4).setOffsetH(x-1);
		getInputConnectionAnchor(i).setOffsetH(x-1);
		getInputConnectionAnchor(i+4).setOffsetH(x);
		getOutputConnectionAnchor(i).setOffsetH(x);
	}
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics graphics) {
	Rectangle rect = getBounds().getCopy();
	rect.crop(new Insets(2,0,2,0));
	graphics.fillRectangle(rect);
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