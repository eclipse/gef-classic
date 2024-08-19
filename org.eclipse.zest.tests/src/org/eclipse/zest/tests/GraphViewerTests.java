/*******************************************************************************
 * Copyright (c) 2011, 2024 Fabian Steeg and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Fabian Steeg - initial tests
 *******************************************************************************/
package org.eclipse.zest.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.util.DelegatingDragAdapter;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IGraphContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link GraphViewer} class.
 *
 * @author Fabian Steeg (fsteeg)
 *
 */
public class GraphViewerTests extends Assert {

	private GraphViewer viewer;
	private Shell shell;

	/**
	 * Set up the shell and viewer to use in the tests.
	 */
	@Before
	public void setUp() {
		shell = new Shell();
		viewer = new GraphViewer(shell, SWT.NONE);
	}

	/**
	 * Create a drop target on a viewer's control and check disposal (see
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=200732)
	 */
	@Test
	public void testDisposalWithDropTarget() {
		new DropTarget(viewer.getGraphControl(), DND.DROP_MOVE | DND.DROP_COPY);
		shell.dispose();
		Assert.assertTrue("The viewer's graph control should be disposed", viewer.getControl().isDisposed()); //$NON-NLS-1$
	}

	/**
	 * Create a drag source on a viewer and check disposal (see
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=334009)
	 */
	@Test
	public void testDisposalWithDragSource() {
		viewer.addDragSupport(DND.DROP_MOVE, new Transfer[] { TextTransfer.getInstance() },
				new DelegatingDragAdapter());
		shell.dispose();
		Assert.assertTrue("The viewer's graph control should be disposed", viewer.getControl().isDisposed()); //$NON-NLS-1$
	}

	/**
	 * Test creation of a graph viewer from a graph widget.
	 */
	@Test
	public void testCreateFromGraph() {
		Graph g = new Graph(shell, SWT.NONE);
		new GraphConnection(g, SWT.NONE, new GraphNode(g, SWT.NONE), new GraphNode(g, SWT.NONE));
		GraphViewer v = new GraphViewer(g);
		Assert.assertEquals(2, v.getGraphControl().getNodes().size());
		Assert.assertEquals(1, v.getGraphControl().getConnections().size());
	}

	/**
	 * Try to find an item that cannot be found (see
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=237489)
	 */
	@Test
	public void testFindGraphItem() {
		Assert.assertNull("If an item cannot be found, the viewer should return null",
				viewer.findGraphItem(Integer.valueOf(5)));
	}

	/**
	 * Assert that no invalid selections with null data are produced by the viewer
	 * (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=356449)
	 */
	@Test
	public void testValidSelection() {
		Graph graph = new Graph(shell, SWT.NONE);
		GraphNode n1 = new GraphNode(graph, SWT.NONE);
		GraphNode n2 = new GraphNode(graph, SWT.NONE);
		GraphConnection c = new GraphConnection(graph, SWT.NONE, n1, n2);
		graph.setSelection(new GraphItem[] { n1, n2, c });
		GraphViewer viewer = new GraphViewer(shell, SWT.NONE);
		viewer.setControl(graph);
		assertEquals("No null data should be in the selection", 0, //$NON-NLS-1$
				((StructuredSelection) viewer.getSelection()).size());
		n1.setData("1"); //$NON-NLS-1$
		n2.setData("2"); //$NON-NLS-1$
		assertEquals("Other data should be in the selection", 2, ((StructuredSelection) viewer.getSelection()).size()); //$NON-NLS-1$
	}

	/**
	 * Assert that listeners for post selection events are properly notified by the
	 * viewer (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=366916)
	 */
	@Test
	public void testPostSelectionListener() {
		final List<SelectionChangedEvent> selected = new ArrayList<>();
		viewer.addPostSelectionChangedListener(selected::add);
		viewer.getControl().notifyListeners(SWT.Selection, new Event());
		assertFalse("Post selection listeners should be notified", selected.isEmpty()); //$NON-NLS-1$
	}

	/**
	 * Assert that a ViewerFilter filters both nodes and connections when using an
	 * IGraphEntityContentProvider (see
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=381852)
	 */
	@Test
	public void testViewerFilterWithGraphEntityContentProvider() {
		testViewerFilter(new SampleGraphEntityContentProvider());
	}

	/**
	 * Assert that a ViewerFilter filters both nodes and connections when using an
	 * IGraphContentProvider.
	 */
	public void testViewerFilterWithGraphContentProvider() {
		testViewerFilter(new SampleGraphContentProvider());
	}

	public void testViewerFilter(IContentProvider contentProvider) {
		viewer.setContentProvider(contentProvider);
		viewer.setInput(new Object());
		assertNodesAndConnections(3, 3);
		viewer.setFilters(new SampleBooleanFilter(false));
		assertNodesAndConnections(0, 0);
		viewer.setFilters(new SampleBooleanFilter(true));
		assertNodesAndConnections(3, 3);
	}

	private void assertNodesAndConnections(int nodes, int connections) {
		assertEquals(nodes, viewer.getGraphControl().getNodes().size());
		assertEquals(connections, viewer.getGraphControl().getConnections().size());
	}

	static class SampleBooleanFilter extends ViewerFilter {
		private final boolean filter;

		SampleBooleanFilter(boolean filter) {
			this.filter = filter;
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			return filter;
		}
	}

	static class SampleGraphContentProvider implements IGraphContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		}

		@Override
		public Object getDestination(Object r) {
			if (r.equals("1to2")) {
				return "2";
			}
			if (r.equals("2to3")) {
				return "3";
			}
			if (r.equals("3to1")) {
				return "1";
			}
			return null;
		}

		@Override
		public Object[] getElements(Object arg0) {
			return new String[] { "1to2", "2to3", "3to1" };
		}

		@Override
		public Object getSource(Object r) {
			if (r.equals("1to2")) {
				return "1";
			}
			if (r.equals("2to3")) {
				return "2";
			}
			if (r.equals("3to1")) {
				return "3";
			}
			return null;
		}

	}

	static class SampleGraphEntityContentProvider implements IGraphEntityContentProvider {

		@Override
		public Object[] getConnectedTo(Object entity) {
			if (entity.equals("1")) {
				return new Object[] { "2" };
			}
			if (entity.equals("2")) {
				return new Object[] { "3" };
			}
			if (entity.equals("3")) {
				return new Object[] { "2" };
			}
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return new String[] { "1", "2", "3" };
		}

		public double getWeight(Object entity1, Object entity2) {
			return 0;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

}
