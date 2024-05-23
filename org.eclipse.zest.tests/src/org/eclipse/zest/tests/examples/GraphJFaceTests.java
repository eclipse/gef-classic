/*******************************************************************************
 * Copyright (c) 2024 Patrick Ziegler and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Ziegler - initial API and implementation
 *******************************************************************************/

package org.eclipse.zest.tests.examples;

import static org.junit.Assert.assertEquals;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.VarHandle;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.widgets.Button;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IGraphContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.viewers.INestedContentProvider;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphContainer;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.internal.ContainerFigure;
import org.eclipse.zest.examples.jface.GraphJFaceSnippet1;
import org.eclipse.zest.examples.jface.GraphJFaceSnippet2;
import org.eclipse.zest.examples.jface.GraphJFaceSnippet3;
import org.eclipse.zest.examples.jface.GraphJFaceSnippet4;
import org.eclipse.zest.examples.jface.GraphJFaceSnippet5;
import org.eclipse.zest.examples.jface.GraphJFaceSnippet6;
import org.eclipse.zest.examples.jface.GraphJFaceSnippet7;
import org.eclipse.zest.examples.jface.GraphJFaceSnippet8;
import org.eclipse.zest.examples.uml.UMLClassFigure;
import org.eclipse.zest.tests.utils.Snippet;

import org.junit.Test;

/**
 * This class instantiates the {@link GraphViewer}-based Zest examples and tests
 * the correctness of the functionality they are supposed to show.
 */
@SuppressWarnings("nls")
public class GraphJFaceTests extends AbstractGraphTest {
	protected GraphViewer viewer;

	@Override
	protected Graph getGraph(Lookup lookup, Snippet snippet) throws ReflectiveOperationException {
		VarHandle varHandle = lookup.findStaticVarHandle(snippet.type(), "viewer", GraphViewer.class); //$NON-NLS-1$
		viewer = (GraphViewer) varHandle.get();
		return viewer.getGraphControl();
	}

	@Override
	protected boolean hasGraph(Lookup lookup, Snippet snippet) throws ReflectiveOperationException {
		try {
			lookup.findStaticVarHandle(snippet.type(), "viewer", GraphViewer.class); //$NON-NLS-1$
			return true;
		} catch (NoSuchFieldException ignore) {
			return false;
		}
	}

	/**
	 * Test using the {@link IGraphEntityContentProvider} when building a graph.
	 */
	@Test
	@Snippet(type = GraphJFaceSnippet1.class)
	public void testGraphJFaceSnippet1() {
		assertNode(graph, 0, "First");
		assertNode(graph, 1, "Second");
		assertNode(graph, 2, "Third");
		assertEquals(graph.getNodes().size(), 3);

		assertConnection(0, "Second", "", "Third");
		assertConnection(1, "Third", "", "First");
		assertConnection(2, "First", "", "Second");
		assertEquals(graph.getConnections().size(), 3);
	}

	/**
	 * Test using the {@link IGraphContentProvider} and {@link LabelProvider} when
	 * building a graph.
	 */
	@Test
	@Snippet(type = GraphJFaceSnippet2.class)
	public void testGraphJFaceSnippet2() {
		assertNode(graph, 0, "Paper");
		assertNode(graph, 1, "Rock");
		assertNode(graph, 2, "Scissors");
		assertEquals(graph.getNodes().size(), 3);

		assertConnection(0, "Paper", "Rock2Paper", "Rock");
		assertConnection(1, "Scissors", "Paper2Scissors", "Paper");
		assertConnection(2, "Rock", "Scissors2Rock", "Scissors");
		assertEquals(graph.getConnections().size(), 3);
	}

	/**
	 * Test reading the graph content from the file system.
	 */
	@Test
	@Snippet(type = GraphJFaceSnippet3.class)
	public void testGraphJFaceSnippet3() {
		// Explicitly checking 30 nodes and 29 connections is just busy work...
		assertEquals(graph.getNodes().size(), 30);
		assertEquals(graph.getConnections().size(), 29);
		assertNoOverlap(graph);
	}

	/**
	 * Test proper handling of selection events.
	 */
	@Test
	@Snippet(type = GraphJFaceSnippet4.class)
	public void testGraphJFaceSnippet4() {
		assertNode(graph, 0, "Paper");
		assertNode(graph, 1, "Rock");
		assertNode(graph, 2, "Scissors");
		assertEquals(graph.getNodes().size(), 3);

		assertConnection(0, "Paper", "Rock2Paper", "Rock");
		assertConnection(1, "Scissors", "Paper2Scissors", "Paper");
		assertConnection(2, "Rock", "Scissors2Rock", "Scissors");
		assertEquals(graph.getConnections().size(), 3);

		AtomicReference<Object> selection = new AtomicReference<>();
		viewer.addSelectionChangedListener(event -> {
			IStructuredSelection structuredSelection = event.getStructuredSelection();
			assertEquals(structuredSelection.size(), 1);
			Object data = structuredSelection.getFirstElement();
			selection.set(data);
		});

		for (GraphNode node : graph.getNodes()) {
			robot.select(node);
			assertEquals(node.getData(), selection.get());
		}
	}

	/**
	 * Test refreshing the graph.
	 */
	@Test
	@Snippet(type = GraphJFaceSnippet5.class)
	public void testGraphJFaceSnippet5() {
		assertConnection(0, "Paper", "Rock2Paper", "Rock");
		assertConnection(1, "Scissors", "Paper2Scissors", "Paper");
		assertConnection(2, "Rock", "Scissors2Rock", "Scissors");
		assertEquals(graph.getConnections().size(), 3);
		assertEquals(graph.getNodes().size(), 3);

		Button refresh = findButtonByName(graph.getShell(), "Refresh");
		robot.select(refresh);

		assertConnection(0, "Paper", "Rock2Paper", "Rock");
		assertConnection(1, "Scissors", "Scissors2Paper", "Paper");
		assertConnection(2, "Rock", "Scissors2Rock", "Scissors");
		assertEquals(graph.getConnections().size(), 3);
		assertEquals(graph.getNodes().size(), 3);
	}

	/**
	 * The using the {@link INestedContentProvider} when building a nested graph.
	 */
	@Test
	@Snippet(type = GraphJFaceSnippet6.class)
	public void testGraphJFaceSnippet6() {
		assertNode(graph, 0, "First");
		assertNode(graph, 1, "Second");
		assertNode(graph, 2, "Third");
		assertEquals(graph.getNodes().size(), 3);

		GraphContainer container = (GraphContainer) graph.getNodes().get(0);
		assertNode(container, 0, "rock");
		assertNode(container, 1, "paper");
		assertNode(container, 2, "scissors");
		assertEquals(container.getNodes().size(), 3);

		assertConnection(0, "rock", "", "paper");
		assertConnection(1, "Second", "", "Third");
		assertConnection(2, "Second", "", "rock");
		assertConnection(3, "Third", "", "First");
		assertConnection(4, "First", "", "Second");
		assertEquals(graph.getConnections().size(), 5);
	}

	/**
	 * Test building a graph with custom figures.
	 */
	@Test
	@Snippet(type = GraphJFaceSnippet7.class)
	public void testGraphJFaceSnippet7() {
		assertFigure(graph, 0, ContainerFigure.class);
		assertFigure(graph, 1, UMLClassFigure.class);
		assertFigure(graph, 2, UMLClassFigure.class);
	}

	/**
	 * Test building a graph with curved edges.
	 */
	@Test
	@Snippet(type = GraphJFaceSnippet8.class)
	public void testGraphJFaceSnippet8() {
		assertNode(graph, 0, "First");
		assertNode(graph, 1, "Second");
		assertNode(graph, 2, "Third");
		assertEquals(graph.getNodes().size(), 3);

		assertConnection(0, "Third", 34 /* ° */, "Second");
		assertConnection(1, "Second", 34 /* ° */, "Third");
		assertConnection(2, "First", Double.NaN, "First");
		assertConnection(3, "First", 0 /* ° */, "Second");
		assertEquals(graph.getConnections().size(), 4);

		assertNoOverlap(graph);
	}
}
