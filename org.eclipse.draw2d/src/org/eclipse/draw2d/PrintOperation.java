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

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.printing.Printer;

/**
 * Implementation of draw2d's printing capabilities. 
 *  
 * @author danlee
 * */
public abstract class PrintOperation {

private GC printerGC;  // Note: Only one GC instance should be created per print job
private Insets printMargin = new Insets(0, 0, 0, 0);
private Printer printer;
private PrinterGraphics printerGraphics;
private SWTGraphics g;

/**
 * Creates a new PrintOperation
 */
public PrintOperation() {
}

/**
 * Creates a new PrintOperation on Printer p
 * @param p The printer to print on */
public PrintOperation(Printer p) {
	setPrinter(p);
}

/**
 * Disposes the PrinterGraphics and GC objects associated with this PrintOperation. 
 */
protected void cleanup() {
	if (g != null) {
		printerGraphics.dispose();
		g.dispose();
	}
	if (printerGC != null)
		printerGC.dispose();
}		

/**
 * Returns a Rectangle that represents the region that can be printed to. The x, y,
 * height, and width values are using the printers coordinates.
 * @return the print region
 */
public Rectangle getPrintRegion() {
	org.eclipse.swt.graphics.Rectangle trim = printer.computeTrim(0, 0, 0, 0);
	org.eclipse.swt.graphics.Rectangle clientArea = printer.getClientArea();
	org.eclipse.swt.graphics.Point printerDPI = printer.getDPI();
	
	Rectangle printRegion = new Rectangle();
	printRegion.x =
		Math.max(
			(printMargin.left * printerDPI.x) / 72 - trim.width,
			clientArea.x);
	printRegion.y =
		Math.max(
			(printMargin.top * printerDPI.y) / 72 - trim.height,
			clientArea.y);
	printRegion.width =
		(clientArea.x + clientArea.width)
			- printRegion.x
			- Math.max(0, (printMargin.right * printerDPI.x) / 72 - trim.width);
	printRegion.height =
		(clientArea.y + clientArea.height)
			- printRegion.y
			- Math.max(0, (printMargin.bottom * printerDPI.y) / 72 - trim.height);
	return printRegion;
}

/**
 * This method contains all operations performed to sourceFigure prior to being printed.
 */
protected void preparePrintSource() {
}

/**
 * This method contains all operations performed to
 * sourceFigure after being printed.
 */
protected void restorePrintSource() {
}

/**
 * Returns a new PrinterGraphics setup for the Printer associated with this
 * PrintOperation.
 * 
 * @return PrinterGraphics The new PrinterGraphics */
protected PrinterGraphics getFreshPrinterGraphics() {
	if (printerGraphics != null) {
		printerGraphics.dispose();
		g.dispose();
		printerGraphics = null;
		g = null;
	}
	g = new SWTGraphics(printerGC);
	printerGraphics = new PrinterGraphics(g, printer);
	setupGraphicsForPage(printerGraphics);
	return printerGraphics;
}

/**
 * This method is responsible for printing pages. (A page is printed by calling
 * Printer.startPage(), followed by painting to the PrinterGraphics object, and then
 * calling Printer.endPage()).
 */
protected abstract void printPages();

/**
 * Sets the print job into motion. 
 * 
 * @param jobName A String representing the name of the print job */
public void run(String jobName) {
	preparePrintSource();
	if (printer.startJob(jobName)) {
		printerGC = new GC(getPrinter());
		printPages();
		printer.endJob();
	}
	restorePrintSource();
	cleanup();
}

/**
 * Manipulates the PrinterGraphics to position it to paint in the desired region of the
 * page. (Default is the top left corner of the page).
 * 
 * @param pg The PrinterGraphics to setup */
protected void setupGraphicsForPage(PrinterGraphics pg) {
	Rectangle printRegion = getPrintRegion();
	pg.clipRect(printRegion);
	pg.translate(printRegion.getTopLeft());
}

/**
 * Sets the page margin in pels (logical pixels) to the passed Insets.(72 pels == 1 inch)
 * 
 * @param margin The margin to set on the page 
 */
public void setPrintMargin(Insets margin) {
	printMargin = margin;
}

/**
 * Returns the printer.
 * @return Printer
 */
public Printer getPrinter() {
	return printer;
}

/**
 * Sets the printer.
 * @param printer The printer to set
 */
public void setPrinter(Printer printer) {
	this.printer = printer;
}

}