/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Research Group Software Construction,
 *     RWTH Aachen University, Germany - Contribution for Bugzilla 245182
 */
package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.Ray;
import org.eclipse.draw2d.geometry.Straight;

/**
 * @author Alexander Nyssen
 * 
 */
public class StraightTest extends TestCase {

	public void test_getIntersection() {
		Ray p = new Ray(1, 1);
		Ray a = new Ray(2, 1);
		Ray q = new Ray(1, 4);
		Ray b = new Ray(1, -1);

		Ray intersection = new Straight(p, a)
				.getIntersection(new Straight(q, b));

		assertTrue(intersection.equals(new Ray(3, 2)));

		assertNull(new Straight(p, a).getIntersection(new Straight(p, a)));
	}
}
