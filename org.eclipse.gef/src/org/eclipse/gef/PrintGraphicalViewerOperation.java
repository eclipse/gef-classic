package org.eclipse.gef;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PrintOperation;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Display;

/**
 * @author danlee
 */
public class PrintGraphicalViewerOperation extends PrintOperation {

private GraphicalViewer viewer;
private List selectedEditParts;

/**
 * Constructor for PrintGraphicalViewerOperation.
 */
public PrintGraphicalViewerOperation() {
	super();
}

/**
 * Constructor for PrintGraphicalViewerOperation.
 * 
 * @param p The Printer to print to
 */
public PrintGraphicalViewerOperation(Printer p) {
	super(p);
}

/**
 * Constructor for PrintGraphicalViewerOperation
 * 
 * @param p The Printer to print to * @param g The viewer containing what is to be printed */
public PrintGraphicalViewerOperation(Printer p, GraphicalViewer g) {
	super(p);
	viewer = g;
}	
	
/**
 * Returns the viewer.
 * @return GraphicalViewer
 */
public GraphicalViewer getViewer() {
	return viewer;
}

/**
 * @see org.eclipse.draw2d.PrintOperation#preparePrintSource()
 */
protected void preparePrintSource() {
	super.preparePrintSource();
	selectedEditParts = new ArrayList(viewer.getSelectedEditParts());
	viewer.deselectAll();
}

/**
 * @see org.eclipse.draw2d.PrintOperation#printPages()
 */
protected void printPages() {
	getPrinter().startPage();

	Graphics g = getFreshPrinterGraphics();
	
	LayerManager lm = (LayerManager)viewer.getEditPartRegistry().get(LayerManager.ID);
	IFigure f = lm.getLayer(LayerConstants.PRINTABLE_LAYERS);
	setupPrinterGraphicsFor(g, f);
	f.paint(g);
	getPrinter().endPage();
}

/**
 * @see org.eclipse.draw2d.PrintOperation#restorePrintSource()
 */
protected void restorePrintSource() {
	super.restorePrintSource();
	viewer.setSelection(new StructuredSelection(selectedEditParts));
}

/**
 * Sets up Graphics object g for IFigure f.
 * 
 * @param g The Graphics to setup
 * @param f The IFigure used to setup g
 */
protected void setupPrinterGraphicsFor(Graphics g, IFigure f) {
	g.setForegroundColor(f.getForegroundColor());
	g.setBackgroundColor(f.getBackgroundColor());
	g.setFont(f.getFont());

	g.scale((double)getPrinter().getDPI().x / Display.getDefault().getDPI().x);
	g.translate(f.getBounds().getCopy().getLocation().negate());
	g.clipRect(f.getBounds());
}

/**
 * Sets the viewer.
 * @param viewer The viewer to set
 */
public void setViewer(GraphicalViewer viewer) {
	this.viewer = viewer;
}

}