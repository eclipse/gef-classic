/*******************************************************************************
 * Copyright (c) 2012 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander Ny§en (itemis AG) - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.Dimension;

public class DimensionTests extends TestCase {

	public void testGetExpanded() {
		Dimension d = new Dimension(3, 5);
		assertEquals(new Dimension(6, 7), d.getExpanded(3, 2));
		assertEquals(new Dimension(6, 7), d.getExpanded(3.4, 2.7));
	}

	public void testGetShrinked() {
		Dimension d = new Dimension(6, 7);
		assertEquals(new Dimension(3, 5), d.getShrinked(3, 2));
		assertEquals(new Dimension(3, 5), d.getShrinked(3.4, 2.7));
	}
}
