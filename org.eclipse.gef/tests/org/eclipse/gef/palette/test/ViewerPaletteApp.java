package org.eclipse.gef.palette.test;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import javax.swing.JFrame;
import org.eclipse.gef.palette.*;
import java.util.*;

public class ViewerPaletteApp {

Shell shell = null;

public static void main(String args[]){
	ViewerPaletteApp p = new ViewerPaletteApp();
	p.doit();
}

public void doit(){
		Display display= new Display();
		shell = new Shell(display);
		shell.setLayout(new org.eclipse.swt.layout.RowLayout());
		
		org.eclipse.gef.ui.palette.PaletteViewer pv = 
			new org.eclipse.gef.ui.palette.PaletteViewerImpl();
		pv.createControl(shell);
		pv.setPaletteRoot(getExamplePaletteRoot());
		
		shell.pack();
		shell.open();
		shell.setSize(500,500);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
}

protected PaletteRoot getExamplePaletteRoot(){
	List groups = new ArrayList();
	List entries = new ArrayList();
	DefaultPaletteEntry entry = new DefaultPaletteEntry("G1entry1"); entries.add(entry);
	entry = new DefaultPaletteEntry("G1entry2"); entries.add(entry);
	entry = new DefaultPaletteEntry("G1entry3"); entries.add(entry);
	DefaultPaletteGroup group = new DefaultPaletteGroup("C1Group1",entries);
	groups.add(group);
	entries = new ArrayList();
	entry = new DefaultPaletteEntry("G2entry1"); entries.add(entry);
	entry = new DefaultPaletteEntry("G2entry2"); entries.add(entry);
	group = new DefaultPaletteGroup("C1Group2",entries);
	groups.add(group);
	DefaultPaletteCategory category = new DefaultPaletteCategory("Category1",groups);
	
	List categories = new ArrayList();
	categories.add(category);
	
	DefaultPaletteRoot paletteRoot = new DefaultPaletteRoot(categories);
	return paletteRoot;
}

}


