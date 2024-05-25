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

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphContainer;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.internal.ContainerFigure;
import org.eclipse.zest.examples.uml.UMLClassFigure;
import org.eclipse.zest.examples.uml.UMLExample;
import org.eclipse.zest.tests.utils.Snippet;

import org.junit.Test;

/**
 * This class instantiates the UML-based Zest examples and tests the correctness
 * of the functionality they are supposed to show.
 */
@SuppressWarnings("nls")
public class GraphUMLTests extends AbstractGraphTest {

	@Override
	protected boolean hasGraph(Lookup lookup, Snippet snippet) throws ReflectiveOperationException {
		try {
			lookup.findStaticVarHandle(snippet.type(), snippet.field(), Graph.class);
			return true;
		} catch (NoSuchFieldException ignore) {
			return false;
		}
	}

	@Override
	protected Graph getGraph(Lookup lookup, Snippet snippet) throws ReflectiveOperationException {
		VarHandle varHandle = lookup.findStaticVarHandle(snippet.type(), snippet.field(), Graph.class); // $NON-NLS-1$
		return (Graph) varHandle.get();
	}

	/**
	 * Tests whether the UML nodes are created properly and added to the correct
	 * container.
	 */
	@Test
	@Snippet(type = UMLExample.class)
	public void testUMLExample() {
		GraphNode node1 = graph.getNodes().get(0);
		GraphNode node2 = graph.getNodes().get(1);
		GraphNode node3 = graph.getNodes().get(2);
		assertEquals(graph.getNodes().size(), 3);

		assertNode(node1, "A UML Container");
		assertNode(node2, "");
		assertNode(node3, "");

		assertInstanceOf(node1.getNodeFigure(), ContainerFigure.class);
		assertInstanceOf(node2.getNodeFigure(), UMLClassFigure.class);
		assertInstanceOf(node3.getNodeFigure(), UMLClassFigure.class);

		GraphContainer graphContainer = (GraphContainer) node1;
		GraphNode node4 = graphContainer.getNodes().get(0);
		assertEquals(graphContainer.getNodes().size(), 1);

		assertNode(node4, "");
		assertInstanceOf(node4.getNodeFigure(), UMLClassFigure.class);
	}
}
