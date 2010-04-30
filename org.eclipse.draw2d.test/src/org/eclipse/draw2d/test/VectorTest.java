/*******************************************************************************
 * Copyright (c) 2008, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.Vector;

/**
 * Vector's tests
 * 
 * @author aboyko
 *
 */
public class VectorTest extends TestCase {

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void test_getLength() {
		testLengthValues(3, 4, 5);
		testLengthValues(0, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
		
	public void test_getOrthoComplement() {
		Vector a = new Vector(3, -5);
		assertTrue(a.getOrthogonalComplement().equals(new Vector(5, 3)));
	}

	public void test_getDotProduct() {
		Vector a = new Vector(3, 2);
		Vector b = new Vector(2, -2);
		assertTrue(a.getDotProduct(b) == 2);
	}
	
	private void testLengthValues(int x, int y, double expectedLength) {
		Vector Vector = new Vector(x, y);
		assertEquals(expectedLength, Vector.getLength(), 0);
	}
}
