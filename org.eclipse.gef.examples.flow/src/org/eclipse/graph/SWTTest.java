/*
 * Created on Feb 17, 2003
 */
package org.eclipse.graph;

import org.eclipse.swt.events.*;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.*;

/**
 * @author hudsonr
 */
public class SWTTest {

public static void main(String[] args) {
	Display d = Display.getDefault();
	final Shell shell = new Shell(d);
	shell.setSize(600,500);
	shell.open();
	
	final Button b = new Button(shell, 0);
	b.setBounds(20,20,200,100);
	b.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			b.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					System.out.println("secondary listener notified");
				}
			});
		}
	});
	
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();

}

}
