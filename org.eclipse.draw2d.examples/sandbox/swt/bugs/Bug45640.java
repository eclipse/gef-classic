/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package swt.bugs;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class Bug45640 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell();
	shell.setLayout(new GridLayout());
	final Button b1 = new Button(shell, 0);
	b1.setText ("Leak some pens");
	
	b1.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Image image = new Image(null, 100, 100);
			GC gc = new GC(image);
			gc.setLineWidth(1);
			gc.dispose();
			image.dispose();
		}
	});
	
	shell.setSize(400,300);
	shell.open();
	
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}