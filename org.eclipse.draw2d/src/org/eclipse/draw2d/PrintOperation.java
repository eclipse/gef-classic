package org.eclipse.draw2d;

/**
 * @author danlee
 */
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.printing.Printer;

public abstract class PrintOperation {

private GC printerGC;  // Note: Only one GC instance should be created per print job
private Insets printMargin = new Insets(0,0,0,0);
private Printer printer;
private PrinterGraphics printerGraphics;
private SWTGraphics g;

public PrintOperation() {
}

public PrintOperation(Printer p) {
	setPrinter(p);
}

protected void cleanup() {
	if (g != null) {
		printerGraphics.dispose();
		g.dispose();
	}
	if (printerGC != null)
		printerGC.dispose();
}		

/**
 * Returns a Rectangle that represents the 
 * region that can be printed to. The 
 * x, y, height, and width quantities are in
 * dots.
 */
public Rectangle getPrintRegion() {
	org.eclipse.swt.graphics.Rectangle trim = printer.computeTrim(0,0,0,0);
	org.eclipse.swt.graphics.Rectangle clientArea = printer.getClientArea();
	org.eclipse.swt.graphics.Point printerDPI = printer.getDPI();
	
	Rectangle printRegion = new Rectangle();
	printRegion.x = Math.max((printMargin.left * printerDPI.x)/72 - trim.width,clientArea.x);
	printRegion.y = Math.max((printMargin.top * printerDPI.y)/72 - trim.height,clientArea.y);
	printRegion.width = (clientArea.x + clientArea.width) - printRegion.x - 
							Math.max(0,(printMargin.right * printerDPI.x)/72-trim.width);
 	printRegion.height = (clientArea.y + clientArea.height) - printRegion.y - 
 							Math.max(0,(printMargin.bottom * printerDPI.y)/72-trim.height);
	return printRegion;
}

/**
 * This method contains all operations performed to
 * sourceFigure prior to being printed
 */
protected void preparePrintSource() {
}

/**
 * This method contains all operations performed to
 * sourceFigure after being printed
 */
protected void restorePrintSource() {
}


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

protected abstract void printPages();

public void run(String jobName) {
	preparePrintSource();
	if(printer.startJob(jobName)) {
		printerGC = new GC(getPrinter());
		printPages();
		printer.endJob();
	}
	restorePrintSource();
	cleanup();
}

protected void setupGraphicsForPage(PrinterGraphics pg) {
	Rectangle printRegion = getPrintRegion();
	pg.clipRect(printRegion);
	pg.translate(printRegion.getTopLeft());
}

/**
 * Sets the page margin in pels (logical pixels) to the passed
 * Insets.
 * (72 pels == 1 inch)
 * 
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