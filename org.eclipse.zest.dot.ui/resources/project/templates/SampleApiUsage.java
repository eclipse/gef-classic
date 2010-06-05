/*******************************************************************************
 * Copyright (c) 2009, 2010 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.dot.DotExport;
import org.eclipse.zest.dot.DotImport;

/**
 * Overall API sample usage.
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class SampleApiUsage {

	/**
	 * Run the sample usage.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		/******************************************************************
		 * Import
		 ******************************************************************/
		Shell shell = new Shell();
		/* The DOT input, can be given as a String, File or IFile: */
		DotImport importer = new DotImport("digraph Simple { 1;2; 1->2 }");
		/* Compile the DOT input to a Zest graph subclass: */
		File file = importer.newGraphSubclass();
		System.out.println(file);
		/* Or create a Zest graph instance in a parent, with a style: */
		Graph graph = importer.newGraphInstance(shell, SWT.NONE);
		System.out.println(graph);

		/******************************************************************
		 * Export
		 ******************************************************************/
		/* For some Zest graph, we create the exporter: */
		DotExport exporter = new DotExport(graph);
		/* Export the Zest graph to DOT: */
		String dot = exporter.toDotString();
		System.out.println(dot);
		/* Or to an image file, via a given Graphviz installation (TODO set): */
		File image = exporter.toImage("/opt/local/bin", "pdf");
		System.out.println(image);

		/*****************************************************************/
		shell.setText("Zest from DOT");
		shell.setLayout(new FillLayout());
		shell.setSize(200, 250);
		shell.open();
		while (!shell.isDisposed()) {
			while (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}
	}
}
