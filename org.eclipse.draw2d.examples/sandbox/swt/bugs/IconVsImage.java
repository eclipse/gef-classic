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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class IconVsImage {

public static void main(String[] args) {
	Display display = Display.getDefault();
	Shell shell = new Shell(display);
	
	Menu menu = new Menu(shell, SWT.BAR);
	shell.setMenuBar(menu);
	MenuItem file = new MenuItem(menu, SWT.CASCADE);
	file.setText("File");
	Menu fileMenu = new Menu(file);
	file.setMenu(fileMenu);
	MenuItem item1 = new MenuItem(fileMenu, 0);
	item1.setText("item 1");
	MenuItem item2 = new MenuItem(fileMenu, 0);
	item2.setText("item 2");
	
	Image image1 = new Image(null, IconVsImage.class.getResourceAsStream("and16.gif"));
	Image image2 = new Image(null, image1.getImageData(), image1.getImageData().getTransparencyMask());
	
	item1.setImage(image1);
	item2.setImage(image2);
	
	shell.setSize(600, 500);
	shell.open();
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}
