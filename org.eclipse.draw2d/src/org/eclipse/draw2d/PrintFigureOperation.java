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
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Class responsible for printing Figures.
 * 
 * @author Dan Lee
 * @author Eric Bordeau
 * @author Sven Müller
 */
public class PrintFigureOperation extends PrintOperation {

private IFigure printSource;
private Color oldBGColor;

/**
 * Constructor for PrintFigureOperation.
 * <p>
 * Note: Descendants must call setPrintSource(IFigure) to set the IFigure that is to be 
 * printed.
 * @see org.eclipse.draw2d.PrintOperation#PrintOperation(Printer) */
protected PrintFigureOperation(Printer p) {
	super(p);
}

/**
 * Constructor for PrintFigureOperation.
 * 
 * @param p Printer to print on
 * @param srcFigure Figure to print
 */
public PrintFigureOperation(Printer p, IFigure srcFigure) {
	super(p);
	setPrintSource(srcFigure);
}


/**
 * Returns the printSource.
 * 
 * @return IFigure The source IFigure
 */
protected IFigure getPrintSource() {
	return printSource;
}

/**
 * @see org.eclipse.draw2d.PrintOperation#preparePrintSource()
 */
protected void preparePrintSource() {
	oldBGColor = getPrintSource().getLocalBackgroundColor();
	getPrintSource().setBackgroundColor(ColorConstants.white);
}

/**
 * Prints the pages based on the current print mode. * @see org.eclipse.draw2d.PrintOperation#printPages() */
protected void printPages() {
	double dpiScale = getPrinter().getDPI().x / Display.getCurrent().getDPI().x;
	
	Rectangle printRegion = getPrintRegion();
	printRegion.width /= dpiScale;
	printRegion.height /= dpiScale;
	
	Rectangle bounds = printSource.getBounds();
	double xScale = (double)printRegion.width / bounds.width;
	double yScale = (double)printRegion.height / bounds.height;
	double scale = 1.0;
	if (getPrintMode() == PrintOperation.FIT_PAGE)
		scale = Math.min(xScale, yScale);
	else if (getPrintMode() == PrintOperation.FIT_WIDTH)
		scale = xScale;
	else if (getPrintMode() == PrintOperation.FIT_HEIGHT)
		scale = yScale;
	
	double x =	getPrintSource().getBounds().width  * scale / printRegion.width;
	double y =	getPrintSource().getBounds().height * scale / printRegion.height;

	int horizontalPages = (x > (int)x) ? (int)x + 1 : (int)x;
	int verticalPages = (y > (int)y) ? (int)y + 1 : (int)y;
	
	IFigure figure = getPrintSource();
	Point offset = figure.getBounds().getLocation();
	PrinterGraphics g = getFreshPrinterGraphics();
	g.scale(scale);
	g.setForegroundColor(figure.getForegroundColor());
	g.setBackgroundColor(figure.getBackgroundColor());
	g.setFont(figure.getFont());
	
	Rectangle clipRect = new Rectangle();
	for (int v = 0; v < verticalPages; v++) {
		for (int h = 0; h < horizontalPages; h++) {
			g.pushState();
			getPrinter().startPage();
			g.scale(dpiScale);
			g.translate(-offset.x, -offset.y);
			clipRect.setLocation(offset);
			clipRect.setSize((int)(printRegion.width / scale), 
							(int)(printRegion.height / scale));
			g.clipRect(clipRect);
			figure.paint(g);
			getPrinter().endPage();
			g.restoreState();
			offset.x += (int)(printRegion.width / scale);
		}
		offset.y += (int)(printRegion.height / scale);
		offset.x = figure.getBounds().getLocation().x;
	}
}

/**
 * @see org.eclipse.draw2d.PrintOperation#restorePrintSource()
 */
protected void restorePrintSource() {
	getPrintSource().setBackgroundColor(oldBGColor);
	oldBGColor = null;
}

/**
 * Sets the printSource.
 * @param printSource The printSource to set
 */
protected void setPrintSource(IFigure printSource) {
	this.printSource = printSource;
}

}
