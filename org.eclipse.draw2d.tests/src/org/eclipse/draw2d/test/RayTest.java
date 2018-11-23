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

import org.eclipse.draw2d.geometry.Ray;

/**
 * Ray's tests
 * 
 * @author aboyko
 * 
 */
public class RayTest extends TestCase {

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

	public void test_length() {
		testLengthValues(3, 4, 5);
		testLengthValues(0, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	public void test_getScalarProduct() {
		Ray a = new Ray(3, 2);
		Ray b = new Ray(2, -2);
		assertTrue(a.dotProduct(b) == 2);
	}

	private void testLengthValues(int x, int y, double expectedLength) {
		Ray ray = new Ray(x, y);
		assertEquals(expectedLength, ray.length(), 0);
	}

}
