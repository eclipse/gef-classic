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
package org.eclipse.draw2dl.test;

import junit.framework.TestCase;
import org.eclipse.draw2dl.geometry.Vector;

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
		org.eclipse.draw2dl.geometry.Vector a = new org.eclipse.draw2dl.geometry.Vector(3, -5);
		assertTrue(a.getOrthogonalComplement().equals(new org.eclipse.draw2dl.geometry.Vector(5, 3)));
	}

	public void test_getDotProduct() {
		org.eclipse.draw2dl.geometry.Vector a = new org.eclipse.draw2dl.geometry.Vector(3, 2);
		org.eclipse.draw2dl.geometry.Vector b = new org.eclipse.draw2dl.geometry.Vector(2, -2);
		assertTrue(a.getDotProduct(b) == 2);
	}

	public void test_getAngle() {
		org.eclipse.draw2dl.geometry.Vector a = new org.eclipse.draw2dl.geometry.Vector(24.03809869102058, -6.868028197434448);
		org.eclipse.draw2dl.geometry.Vector b = new org.eclipse.draw2dl.geometry.Vector(-24.038098691020593, 6.868028197434448);
		assertTrue(a.getAngle(b) == 180.0);
	}

	private void testLengthValues(int x, int y, double expectedLength) {
		org.eclipse.draw2dl.geometry.Vector Vector = new Vector(x, y);
		assertEquals(expectedLength, Vector.getLength(), 0);
	}
}
