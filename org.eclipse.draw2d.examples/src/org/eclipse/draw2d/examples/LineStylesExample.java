package org.eclipse.draw2d.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class LineStylesExample {

protected void run(){
	Display d = Display.getDefault();
	Shell shell = new Shell(d);
	
	shell.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event e) {
			GC rgc = e.gc;
			Image a = new Image(Display.getDefault(), 400, 400);
			GC gc = new GC(a);
						
			gc.setLineStyle(SWT.LINE_DASH);
			gc.drawLine(10, 10, 300, 10);
			gc.setLineStyle(SWT.LINE_DOT);
			gc.drawLine(10, 20, 300, 20);
			gc.setLineStyle(SWT.LINE_DASHDOT);
			gc.drawLine(10, 30, 300, 30);
			gc.setLineStyle(SWT.LINE_DASHDOTDOT);
			gc.drawLine(10, 40, 300, 40);
			
			rgc.drawImage(a, 0, 0);
			rgc.dispose();
			gc.dispose();
		}
	});
	
	shell.pack();
	shell.open();

	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

public static void main(String[] args) {
	new LineStylesExample().run();	
}

}