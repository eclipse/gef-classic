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
package org.eclipse.draw2d.examples.zoom;
	
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ButtonBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.parts.Thumbnail;

/**
 * This class demonstrates Draw2d's zoom capabilities.
 * @author delee
 */

public class ZoomExample {

private static Figure contents;
//private static Shell overviewShell;

public static void main(String args[]){
	Display d = new Display();
	final Shell shell = new Shell(d);
	shell.setSize(800, 800);
	LightweightSystem lws = new LightweightSystem(shell);
	
	Figure fig = new Figure();
	fig.setLayoutManager(new ToolbarLayout());

	final ScrollBar bar = new ScrollBar();
	final Label l = new Label("«Zoom»");

	l.setBorder(new SchemeBorder(ButtonBorder.SCHEMES.BUTTON_SCROLLBAR));
	bar.setThumb(l);
	bar.setHorizontal(true);
	bar.setMaximum(200);
	bar.setMinimum(0);
	bar.setExtent(25);

	final ZoomContainer panel = new ZoomContainer();
	panel.setBorder(new GroupBoxBorder("Zooming figure"));
	panel.setMinimumSize(new Dimension(5,5));
	panel.setPreferredSize(500, 500);
	fig.add(bar);
	fig.add(panel);

	bar.addPropertyChangeListener("value", new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			float z = (bar.getValue()+10) * 0.02f;
			panel.setZoom(z);
		}
	});
	
	panel.add(getContents());
	bar.setValue(40);

	lws.setContents(fig);

//	overviewShell = new Shell(shell, SWT.TITLE| SWT.RESIZE | SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
//	overviewShell.setText("Overview Shell");
//	overviewShell.setLayout(new FillLayout());
//	LightweightSystem overviewLWS = new LightweightSystem(overviewShell);
//	overviewLWS.setContents(createThumbnail(getContents()));
//	overviewShell.setSize(200, 200);
	
	shell.open();
//	overviewShell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
//	overviewShell.dispose();
}

protected static Figure createThumbnail(Figure source) {
	Thumbnail thumbnail = new Thumbnail();
	thumbnail.setBorder(new GroupBoxBorder("Overview Figure"));
	thumbnail.setSource(source);
	return thumbnail;
}

protected static Figure getContents() {
	if (contents == null)
		contents = createContents();
	return contents;
}

private static Figure createContents() {	
	Figure contents = new Figure();
	XYLayout layout = new XYLayout();
	contents.setLayoutManager(layout);
	
	Font classFont = new Font(null, "Arial", 12, SWT.BOLD);
	Label classLabel1 = new Label("Table", new Image(null, 
		ZoomExample.class.getResourceAsStream("images/class_obj.gif")));
	classLabel1.setFont(classFont);
	
	Label classLabel2 = new Label("Column", new Image(null, 
			ZoomExample.class.getResourceAsStream("images/class_obj.gif")));
	classLabel2.setFont(classFont);
	
	final UMLClassFigure classFigure = new UMLClassFigure(classLabel1);
	final UMLClassFigure classFigure2 = new UMLClassFigure(classLabel2);
	
	Label attribute1 = new Label("columns: Column[]", new Image(null, 
		UMLClassFigure.class.getResourceAsStream("images/field_private_obj.gif")));
	Label attribute2 = new Label("rows: Row[]", new Image(null, 
		UMLClassFigure.class.getResourceAsStream("images/field_private_obj.gif")));
	Label attribute3 = new Label("columnID: int", new Image(null, 
		UMLClassFigure.class.getResourceAsStream("images/field_private_obj.gif")));
	Label attribute4 = new Label("items: List", new Image(null, 
		UMLClassFigure.class.getResourceAsStream("images/field_private_obj.gif")));

	classFigure.getAttributesCompartment().add(attribute1);
	classFigure.getAttributesCompartment().add(attribute2);
	classFigure2.getAttributesCompartment().add(attribute3);
	classFigure2.getAttributesCompartment().add(attribute4);

	Label method1 = new Label("getColumns(): Column[]", new Image(null, 
		UMLClassFigure.class.getResourceAsStream("images/methpub_obj.gif")));
	Label method2 = new Label("getRows(): Row[]", new Image(null, 
		UMLClassFigure.class.getResourceAsStream("images/methpub_obj.gif")));
	Label method3 = new Label("getColumnID(): int", new Image(null, 
		UMLClassFigure.class.getResourceAsStream("images/methpub_obj.gif")));
	Label method4 = new Label("getItems(): List", new Image(null, 
		UMLClassFigure.class.getResourceAsStream("images/methpub_obj.gif")));

	classFigure.getMethodsCompartment().add(method1);
	classFigure.getMethodsCompartment().add(method2);
	classFigure2.getMethodsCompartment().add(method3);
	classFigure2.getMethodsCompartment().add(method4);

	layout.setConstraint(classFigure, new Rectangle(10,10,-1,-1));
	layout.setConstraint(classFigure2, new Rectangle(150, 150, -1, -1));
	
	contents.add(classFigure);
	contents.add(classFigure2);
	
	return contents;
}

}
