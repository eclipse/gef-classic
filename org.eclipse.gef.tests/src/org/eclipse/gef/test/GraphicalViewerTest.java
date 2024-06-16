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

package org.eclipse.gef.test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;

import org.junit.Before;
import org.junit.Test;

public class GraphicalViewerTest {
	private GraphicalViewer viewer;

	@Before
	public void setUp() {
		viewer = PlatformUI.getWorkbench().getDisplay().syncCall(GraphicalViewerImpl::new);
	}

	/**
	 * Appending a {@code null} edit part shouldn't leave the viewer in an
	 * inconsistent state.
	 */
	@Test
	public void testAppendNullSelection() {
		assertThrows(NullPointerException.class, () -> viewer.appendSelection(null));
		assertTrue(viewer.getSelectedEditParts().isEmpty());
	}

	/**
	 * Setting a {@code null} edit part shouldn't leave the viewer in an
	 * inconsistent state.
	 */
	@Test
	public void testSetNullSelection() {
		IStructuredSelection selection = new StructuredSelection(new Object[] { null });
		assertThrows(NullPointerException.class, () -> viewer.setSelection(selection));
		assertTrue(viewer.getSelectedEditParts().isEmpty());
	}
}
