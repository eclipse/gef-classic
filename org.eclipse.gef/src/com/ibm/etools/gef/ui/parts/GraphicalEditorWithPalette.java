package com.ibm.etools.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.ibm.etools.draw2d.ColorConstants;
import com.ibm.etools.draw2d.FigureUtilities;

import com.ibm.etools.gef.palette.PaletteRoot;
import com.ibm.etools.gef.ui.palette.*;

public abstract class GraphicalEditorWithPalette 
	extends GraphicalEditor 
{

private PaletteViewer paletteViewer;

protected void configurePaletteViewer(){
	getPaletteViewer().
		getControl().
		setBackground(ColorConstants.buttonLightest);
}

private void createPaletteViewer(Composite parent) {
	PaletteViewer viewer = new PaletteViewerImpl();
	viewer.createControl(parent);
	setPaletteViewer(viewer);
	configurePaletteViewer();
	hookPaletteViewer();
	initializePaletteViewer();
}

public void createPartControl(Composite parent) {
	Splitter splitter = new Splitter(parent, SWT.HORIZONTAL);
	createPaletteViewer(splitter);
	createGraphicalViewer(splitter);
	splitter.setWeights(new int [] {1,6});
}

public void dispose(){
	getPaletteViewer().dispose();
	super.dispose();
}

/**
 * Returns the model that is used in the PaletteViewer.
 */
abstract protected PaletteRoot getPaletteRoot();

/**
 * Returns the PaletteViewer.
 */
protected PaletteViewer getPaletteViewer(){
	return paletteViewer;
}

protected void hookPaletteViewer(){
	getEditDomain().setPaletteViewer(paletteViewer);
}

protected void initializePaletteViewer(){
	getEditDomain().setPaletteRoot(getPaletteRoot());
}

protected void setPaletteViewer(PaletteViewer paletteViewer){
	this.paletteViewer = paletteViewer;
}

}
