/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class RectangleTest extends TestCase {
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

	public void test_creationSymmetry() {
		Point topLeft = new Point(0, 0);
		Point topRight = new Point(10, 0);
		Point bottomLeft = new Point(0, 10);
		Point bottomRight = new Point(10, 10);
		Rectangle rect1 = new Rectangle(topLeft, bottomRight);
		Rectangle rect2 = new Rectangle(topRight, bottomLeft);

		assertTrue(rect1.equals(rect2));
	}

	public void test_creationValues() {

		final Rectangle testRect = new Rectangle(10, 10, 10, 10);

		Point topLeft = new Point(10, 10);
		Point topRight = new Point(19, 10);
		Point bottomLeft = new Point(10, 19);
		Point bottomRight = new Point(19, 19);
		Rectangle rect1 = new Rectangle(topLeft, bottomRight);
		Rectangle rect2 = new Rectangle(topRight, bottomLeft);

		assertTrue(rect1.equals(testRect));
		assertTrue(rect2.equals(testRect));
	}

	public void test_sameBehavior() {

		Point p1 = new Point(0, 0);
		Point p2 = new Point(10, 10);
		Rectangle origRect = new Rectangle();
		origRect.setLocation(p1);
		origRect.union(p2);

		Rectangle newRect = new Rectangle(p1, p2);

		assertTrue(origRect.equals(newRect));
	}

}
