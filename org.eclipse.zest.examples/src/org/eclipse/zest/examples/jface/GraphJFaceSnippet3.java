/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC,
 *                      Canada.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.examples.jface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IGraphContentProvider;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;

/**
 * This snippet uses a very simple file format to read a graph. Edges are listed
 * on a new line in a file as such: a calls b b calls c c calld d
 *
 * The content provider creates an edge for each line in the file and names the
 * sources and destination from the line.
 *
 *
 * @author Ian Bull
 *
 */
public class GraphJFaceSnippet3 {

	public static final String GRAPH = """
			a calls b
			a calls c
			b calld d
			b calls e
			c calls f
			c calls g
			d calls h
			d calls i
			e calls j
			e calls k
			f calls l
			f calls m
			"""; //$NON-NLS-1$

	static class SimpleGraphContentProvider implements IGraphContentProvider {

		private StringTokenizer graph;

		@Override
		public Object getDestination(Object rel) {
			String string = (String) rel;
			String[] parts = string.split(" "); //$NON-NLS-1$
			return parts[2];
		}

		@Override
		public Object[] getElements(Object input) {
			List<String> listOfEdges = new ArrayList<>();
			while (graph.hasMoreTokens()) {
				listOfEdges.add(graph.nextToken());
			}
			return listOfEdges.toArray();
		}

		@Override
		public Object getSource(Object rel) {
			String string = (String) rel;
			String[] parts = string.split(" "); //$NON-NLS-1$
			return parts[0];
		}

		public double getWeight(Object connection) {
			return 0;
		}

		@Override
		public void dispose() {

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput != null) {
				graph = new StringTokenizer((String) newInput, "\n"); //$NON-NLS-1$
			}
		}

	}

	public static void main(String[] args) throws IOException {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Simple Graph File Format"); //$NON-NLS-1$

		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setFilterNames(new String[] { "Simple Graph Files (*.sgf)", "All Files (*.*)" }); //$NON-NLS-1$ //$NON-NLS-2$
		dialog.setFilterExtensions(new String[] { "*.sgf", "*.*" }); // Windows wild cards //$NON-NLS-1$ //$NON-NLS-2$

		String directory = System.getProperty("user.dir") + "/src/org/eclipse/zest/tests/jface/SimpleGraph.sgf"; // eclipse/zest/examples/jface/"; //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println(directory);
		dialog.setFilterPath(directory);
		// dialog.setFilterPath(System.getProperty("user.dir") +
		// "src/org/eclipse/zest/examples/jface/"); //Windows path

		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setSize(400, 400);
		GraphViewer viewer = null;

		viewer = new GraphViewer(shell, SWT.NONE);
		viewer.setContentProvider(new SimpleGraphContentProvider());
		viewer.setLabelProvider(new LabelProvider());
		viewer.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));

		shell.open();
		String fileName = dialog.open();

		if (fileName == null) {
			// use the sample graph
			viewer.setInput(GRAPH);
		} else {
			FileReader fileReader = new FileReader(new File(fileName));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuilder stringBuffer = new StringBuilder();
			while (bufferedReader.ready()) {
				stringBuffer.append(bufferedReader.readLine() + "\n"); //$NON-NLS-1$
			}
			viewer.setInput(stringBuffer.toString());
		}

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
