/*******************************************************************************
 * Copyright (c) 2011 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial tests
 *******************************************************************************/
package org.eclipse.zest.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.jface.util.DelegatingDragAdapter;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

/**
 * Tests for the {@link GraphViewer} class.
 * 
 * @author Fabian Steeg (fsteeg)
 * 
 */
public class GraphViewerTests extends TestCase {

	private GraphViewer viewer;
	private Shell shell;

	/**
	 * Set up the shell and viewer to use in the tests.
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		shell = new Shell();
		viewer = new GraphViewer(shell, SWT.NONE);
	}

	/**
	 * Create a drop target on a viewer's control and check disposal (see
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=200732)
	 */
	public void testDisposalWithDropTarget() {
		new DropTarget(viewer.getGraphControl(), DND.DROP_MOVE | DND.DROP_COPY);
		shell.dispose();
		Assert.assertTrue("The viewer's graph control should be disposed",
				viewer.getControl().isDisposed());
	}

	/**
	 * Create a drag source on a viewer and check disposal (see
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=334009)
	 */
	public void testDisposalWithDragSource() {
		viewer.addDragSupport(DND.DROP_MOVE,
				new Transfer[] { TextTransfer.getInstance() },
				new DelegatingDragAdapter());
		shell.dispose();
		Assert.assertTrue("The viewer's graph control should be disposed",
				viewer.getControl().isDisposed());
	}

	/**
	 * Assert that no invalid selections with null data are produced by the
	 * viewer (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=356449)
	 */
	public void testValidSelection() {
		Graph graph = new Graph(shell, SWT.NONE);
		GraphNode n1 = new GraphNode(graph, SWT.NONE);
		GraphNode n2 = new GraphNode(graph, SWT.NONE);
		GraphConnection c = new GraphConnection(graph, SWT.NONE, n1, n2);
		graph.setSelection(new GraphItem[] { n1, n2, c });
		GraphViewer viewer = new GraphViewer(shell, SWT.NONE);
		viewer.setControl(graph);
		assertEquals("No null data should be in the selection", 0,
				((StructuredSelection) viewer.getSelection()).size());
		n1.setData("1");
		n2.setData("2");
		assertEquals("Other data should be in the selection", 2,
				((StructuredSelection) viewer.getSelection()).size());
	}

	/**
	 * Assert that listeners for post selection events are properly notified by
	 * the viewer (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=366916)
	 */
	public void testPostSelectionListener() {
		final List selected = new ArrayList();
		viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				selected.add(event);
			}
		});
		viewer.getControl().notifyListeners(SWT.Selection, new Event());
		assertFalse("Post selection listeners should be notified",
				selected.isEmpty());
	}
}
