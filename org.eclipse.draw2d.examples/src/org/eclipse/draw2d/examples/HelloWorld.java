package org.eclipse.draw2d.examples;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Label;

public class HelloWorld {

public static void main(String[] args) {

	Display d = new Display();
	Shell shell = new Shell(d);
	shell.setLayout(new FillLayout());
	
	FigureCanvas canvas = new FigureCanvas(shell);
	canvas.setContents(new Label("Hello World"));

	shell.setText("draw2d");
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();

}


}
