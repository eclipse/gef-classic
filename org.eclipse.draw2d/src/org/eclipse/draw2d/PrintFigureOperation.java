package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.printing.Printer;

/**
 * @author danlee
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
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
 * @param p
 * @param srcFigure
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

protected void printPages() {
	getPrinter().startPage();

	Graphics g = getFreshPrinterGraphics();
	IFigure f = getPrintSource();
	setupPrinterGraphicsFor(g, f);

	f.paint(g);

	getPrinter().endPage();
}

protected void setupPrinterGraphicsFor(Graphics g, IFigure f) {
	g.setForegroundColor(f.getForegroundColor());
	g.setBackgroundColor(f.getBackgroundColor());
	g.setFont(f.getFont());

	g.scale((double)getPrinter().getDPI().x / 72);
	g.translate(f.getBounds().getCopy().getLocation().negate());
	g.clipRect(f.getBounds());
}

/**
 * Returns the printSource.
 * @return IFigure
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
