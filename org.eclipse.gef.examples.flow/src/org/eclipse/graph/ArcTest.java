package org.eclipse.graph;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;

/**
 * @author hudsonr
 * @since 2.1
 */
public class ArcTest {

public static void main(String args[]){
	Display d = new Display();
	final Color red = new Color(d, 255,10,10);
	final Color yellow = new Color(d, 255, 255, 10);
	final Shell shell = new Shell(d);
	shell.setSize(400, 290);

	shell.addPaintListener(new PaintListener() {
		/**
		 * @see org.eclipse.swt.events.PaintListener#paintControl(PaintEvent)
		 */
		public void paintControl(PaintEvent e) {
			e.gc.setBackground(red);
			e.gc.fillArc(30,0,91,91,180,180);
			e.gc.drawArc(30,0,90,90,180,180);
			
			e.gc.setBackground(yellow);
			e.gc.fillArc(150,20,90,90,0,180);
			e.gc.drawArc(150,20,89,89,0,180);
		}
	});

	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

}
