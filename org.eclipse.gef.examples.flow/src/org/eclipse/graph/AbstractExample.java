package org.eclipse.graph;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * A baseclass for draw2d examples.
 * @author hudsonr
 */
public abstract class AbstractExample {

protected static final Font COURIER = new Font(null, "Courier", 9, 0);//$NON-NLS-1$
protected static final Font BOLD = new Font(null, "Helvetica", 10, SWT.BOLD);//$NON-NLS-1$
protected static final Font ITALICS = new Font(null, "Helvetica", 10, SWT.ITALIC);//$NON-NLS-1$
protected static final Font HEADING_1 = new Font(null, "Helvetica", 15, SWT.BOLD);//$NON-NLS-1$
private FigureCanvas fc;
protected Shell shell;
protected IFigure contents;

/**
 * Runs the demo.
 */
protected void run() {
	Display d = Display.getDefault();
	shell = new Shell(d);
	String appName = getClass().getName();
	appName = appName.substring(appName.lastIndexOf('.') + 1);
	hookShell();
	shell.setText(appName);
	shell.setLayout(new GridLayout(2, false));
	setFigureCanvas(new FigureCanvas(shell));
	getFigureCanvas().setContents(contents = getContents());
	getFigureCanvas().getViewport().setContentsTracksHeight(true);
	getFigureCanvas().getViewport().setContentsTracksWidth(true);
	getFigureCanvas().setLayoutData(new GridData(GridData.FILL_BOTH));
	shell.setSize(1100, 700);
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

protected abstract IFigure getContents();

protected FigureCanvas getFigureCanvas(){
	return fc;
}

protected void hookShell(){}

protected void setFigureCanvas(FigureCanvas canvas){
	this.fc = canvas;
}

}
