package org.eclipse.draw2d.examples;

import org.eclipse.draw2d.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * A baseclass for draw2d examples.
 * @author hudsonr
 */
public abstract class AbstractExample {

protected void run(){
	Display d = new Display();
	Shell shell = new Shell(d);
	String appName = getClass().getName();
	appName = appName.substring(appName.lastIndexOf('.')+1);
	shell.setText(appName);
	shell.setLayout(new FillLayout());
	FigureCanvas canvas = new FigureCanvas(shell);
	canvas.setContents(getContents());
	shell.setSize(300,260);
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

protected abstract IFigure getContents();

}
