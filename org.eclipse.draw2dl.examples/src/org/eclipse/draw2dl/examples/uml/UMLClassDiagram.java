/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.examples.uml;

import org.eclipse.draw2dl.PolygonDecoration;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2dl.Figure;
import org.eclipse.draw2dl.FigureCanvas;
import org.eclipse.draw2dl.geometry.PointList;
import org.eclipse.draw2dl.geometry.Rectangle;

public class UMLClassDiagram {

public static void main(String[] args) {

	Display d = new Display();
	Shell shell = new Shell(d);
	shell.setLayout(new FillLayout());
	
	FigureCanvas canvas = new FigureCanvas(shell);
	canvas.setBackground(org.eclipse.draw2dl.ColorConstants.white);

	Figure diagram = new Figure();
	diagram.setLayoutManager(new org.eclipse.draw2dl.XYLayout());
	canvas.setContents(diagram);
	
	org.eclipse.draw2dl.IFigure c1, c2;
	
	diagram.add(c1 = new UMLClassFigure(), new Rectangle( 20,20, -1, -1));
	diagram.add(c2 = new UMLClassFigure(), new Rectangle(230, 102, -1, -1));
	
	org.eclipse.draw2dl.PolylineConnection assoc = new org.eclipse.draw2dl.PolylineConnection();
	assoc.setTargetAnchor(new org.eclipse.draw2dl.ChopboxAnchor(c1));
	assoc.setSourceAnchor(new org.eclipse.draw2dl.ChopboxAnchor(c2));
	org.eclipse.draw2dl.PolygonDecoration containment = new PolygonDecoration();
	containment.setTemplate(new PointList(new int[]{-2, 0, -1, 1, 0, 0, -1, -1}));
	assoc.setTargetDecoration(containment);
	diagram.add(assoc);
	
	org.eclipse.draw2dl.Label ref = new org.eclipse.draw2dl.Label("end1");
	org.eclipse.draw2dl.ConnectionEndpointLocator locator = new org.eclipse.draw2dl.ConnectionEndpointLocator(assoc, false);
	locator.setUDistance(8);
	assoc.add(ref, locator);
	
	org.eclipse.draw2dl.Label connLabel = new org.eclipse.draw2dl.Label("connection");
	connLabel.setBorder(new org.eclipse.draw2dl.LineBorder());
	connLabel.setOpaque(true);
	connLabel.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.tooltipBackground);
	assoc.add(connLabel, new org.eclipse.draw2dl.ConnectionLocator(assoc, org.eclipse.draw2dl.ConnectionLocator.MIDDLE));
	
	diagram.add(new StickyNote(), new Rectangle(180, 10, 90, - 1));
	
	shell.setSize(500, 300);
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();

}


}
