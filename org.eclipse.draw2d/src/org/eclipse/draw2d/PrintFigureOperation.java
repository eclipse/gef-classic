package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Display;

/**
 * Class responsible for printing Figures.
 * 
 * @author danlee
 */
public class PrintFigureOperation extends PrintOperation {

private IFigure printSource;
private Color oldBGColor;

/**
 * Constructor for PrintFigureOperation.
 */
public PrintFigureOperation() { }

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
 * @see org.eclipse.draw2d.PrintOperation#preparePrintSource()
 */
protected void preparePrintSource() {
	oldBGColor = getPrintSource().getBackgroundColor();
	getPrintSource().setBackgroundColor(ColorConstants.white);
}

/**
 * @see org.eclipse.draw2d.PrintOperation#restorePrintSource()
 */
protected void restorePrintSource() {
	getPrintSource().setBackgroundColor(oldBGColor);
	oldBGColor = null;
}

/**
 *  * @see org.eclipse.draw2d.PrintOperation#printPages() */
protected void printPages() {
	getPrinter().startPage();

	Graphics g = getFreshPrinterGraphics();
	IFigure f = getPrintSource();
	setupPrinterGraphicsFor(g, f);

	f.paint(g);

	getPrinter().endPage();
}

/**
 * Sets up Graphics object g for IFigure f.
 * 
 * @param g The Graphics to setup * @param f The IFigure used to setup g */
protected void setupPrinterGraphicsFor(Graphics g, IFigure f) {
	g.setForegroundColor(f.getForegroundColor());
	g.setBackgroundColor(f.getBackgroundColor());
	g.setFont(f.getFont());

	g.scale((double)getPrinter().getDPI().x / Display.getDefault().getDPI().x);
	g.translate(f.getBounds().getCopy().getLocation().negate());
	g.clipRect(f.getBounds());
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
 * Sets the printSource.
 * @param printSource The printSource to set
 */
public void setPrintSource(IFigure printSource) {
	this.printSource = printSource;
}

}
