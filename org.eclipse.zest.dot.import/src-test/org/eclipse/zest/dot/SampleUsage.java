/*******************************************************************************
 * Copyright (c) 2009, 2010 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.dot;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.Graph;
import org.junit.Test;

/**
 * Overall API sample usage.
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class SampleUsage {
	@Test
	public void sampleUsage() {

		/******************************************************************
		 * Graph
		 ******************************************************************/
		Shell shell = new Shell();
		/* A Dot graph is a Zest graph that can be created from DOT: */
		Dot graph = new Dot("digraph{1->2}", shell, SWT.NONE); //$NON-NLS-1$
		/* The Dot graph can be modified using DOT snippets: */
		graph.add("2->3; 2->4"); //$NON-NLS-1$
		/* The snippets can contain DOT node and edge attributes: */
		graph.add("node[label=zested]; edge[style=dashed]; 3->5; 4->6"); //$NON-NLS-1$
		// TestGraphInstanceDotImport.open(shell);

		/******************************************************************
		 * Import
		 ******************************************************************/
		shell = new Shell();
		/* The DOT input, can be given as a String, File or IFile: */
		DotImport importer = new DotImport("digraph Simple { 1;2; 1->2 }"); //$NON-NLS-1$
		/* Compile the DOT input to a Zest graph subclass: */
		File file = importer.newGraphSubclass();
		/* Or create a Zest graph instance in a parent, with a style: */
		Graph importedGraph = importer.newGraphInstance(shell, SWT.NONE);
		// TestGraphInstanceDotImport.open(shell);

		/******************************************************************
		 * Export
		 ******************************************************************/
		/* For some Zest graph, we create the exporter: */
		DotExport exporter = new DotExport(graph);
		/* Export the Zest graph to DOT: */
		String dot = exporter.toDotString();
		/* Or to an image file, via a given Graph installation: */
		// File image = exporter.toImage(TestImageExport.dotBinDir(), "pdf");
		// File image = exporter.toImage("/opt/local/bin", "pdf"); // set dir
		// and uncomment
		// System.out.println(image);
		/*****************************************************************/

		// TestGraphInstanceDotImport.open(shell); // sets title, layout, and
		// size, opens the shell
		System.out.println(importedGraph);
		System.out.println(file);
		System.out.println(dot);
	}
}
