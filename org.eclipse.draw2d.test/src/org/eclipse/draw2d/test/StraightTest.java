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

import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Straight;
import org.eclipse.draw2d.geometry.Vector;

/**
 * @author Alexander Nyssen
 * 
 */
public class StraightTest extends TestCase {

	public void test_getIntersection() {
		// test integer precision
		Vector p = new Vector(1, 1);
		Vector a = new Vector(2, 1);
		Vector q = new Vector(1, 4);
		Vector b = new Vector(1, -1);

		Vector intersection = new Straight(p, a).getIntersection(new Straight(
				q, b));

		assertTrue(intersection.equals(new Vector(3, 2)));

		// test double precision
		p = new Vector(0, 0);
		a = new Vector(new PrecisionPoint(0, 0), new PrecisionPoint(5, 5));
		q = new Vector(0, 5);
		b = new Vector(new PrecisionPoint(0, 5), new PrecisionPoint(5, 0));

		intersection = new Straight(p, a).getIntersection(new Straight(q, b));
		assertTrue(intersection.equals(new Vector(2.5, 2.5)));

		// check straight does not intersect itself
		assertNull(new Straight(p, a).getIntersection(new Straight(p, a)));
	}
	
	public void test_isParallelTo(){
		Straight s1 = new Straight(new Vector(0,0), new Vector(3,3));
		Straight s2 = new Straight(new Vector(0,4), new Vector(2, 2));
		assertTrue(s1.isParallelTo(s2));
	}
	
	public void test_equals(){
		Straight s1 = new Straight(new Vector(0,0), new Vector(3,3));
		Straight s2 = new Straight(new Vector(4,4), new Vector(2, 2));
		assertTrue(s1.equals(s2));
	}
}
