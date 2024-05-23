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
		assertNode(graph, 0, "A UML Container");
		assertNode(graph, 1, "");
		assertNode(graph, 2, "");
		assertEquals(graph.getNodes().size(), 3);

		assertFigure(graph, 0, ContainerFigure.class);
		assertFigure(graph, 1, UMLClassFigure.class);
		assertFigure(graph, 2, UMLClassFigure.class);

		GraphContainer graphContainer = (GraphContainer) graph.getNodes().get(0);
		assertNode(graphContainer, 0, "");
		assertEquals(graphContainer.getNodes().size(), 1);

		assertFigure(graphContainer, 0, UMLClassFigure.class);
	}
}
