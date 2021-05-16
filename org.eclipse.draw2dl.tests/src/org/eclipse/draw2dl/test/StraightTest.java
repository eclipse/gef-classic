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
package org.eclipse.draw2dl.test;

import junit.framework.TestCase;

import org.eclipse.draw2dl.geometry.PrecisionPoint;
import org.eclipse.draw2dl.geometry.Straight;
import org.eclipse.draw2dl.geometry.Vector;

/**
 * @author Alexander Nyssen
 * 
 */
public class StraightTest extends TestCase {

	public void test_getIntersection() {
		// test integer precision
		org.eclipse.draw2dl.geometry.Vector p = new org.eclipse.draw2dl.geometry.Vector(1, 1);
		org.eclipse.draw2dl.geometry.Vector a = new org.eclipse.draw2dl.geometry.Vector(2, 1);
		org.eclipse.draw2dl.geometry.Vector q = new org.eclipse.draw2dl.geometry.Vector(1, 4);
		org.eclipse.draw2dl.geometry.Vector b = new org.eclipse.draw2dl.geometry.Vector(1, -1);
		org.eclipse.draw2dl.geometry.Straight s1 = new org.eclipse.draw2dl.geometry.Straight(p, a);
		org.eclipse.draw2dl.geometry.Straight s2 = new org.eclipse.draw2dl.geometry.Straight(q, b);
		org.eclipse.draw2dl.geometry.Vector intersection = s1.getIntersection(s2);
		assertTrue(intersection.equals(new org.eclipse.draw2dl.geometry.Vector(3, 2)));
		assertTrue(s1.contains(intersection));
		assertTrue(s2.contains(intersection));

		// check straight does not intersect itself
		assertNull(new org.eclipse.draw2dl.geometry.Straight(p, a).getIntersection(new org.eclipse.draw2dl.geometry.Straight(p, a)));

		// test double precision
		p = new org.eclipse.draw2dl.geometry.Vector(0, 0);
		a = new org.eclipse.draw2dl.geometry.Vector(new org.eclipse.draw2dl.geometry.PrecisionPoint(0, 0), new org.eclipse.draw2dl.geometry.PrecisionPoint(5, 5));
		q = new org.eclipse.draw2dl.geometry.Vector(0, 5);
		b = new org.eclipse.draw2dl.geometry.Vector(new org.eclipse.draw2dl.geometry.PrecisionPoint(0, 5), new org.eclipse.draw2dl.geometry.PrecisionPoint(5, 0));
		s1 = new org.eclipse.draw2dl.geometry.Straight(p, a);
		s2 = new org.eclipse.draw2dl.geometry.Straight(q, b);
		intersection = s1.getIntersection(s2);
		assertTrue(intersection.equals(new org.eclipse.draw2dl.geometry.Vector(2.5, 2.5)));
		assertTrue(s1.contains(intersection));
		assertTrue(s2.contains(intersection));

		org.eclipse.draw2dl.geometry.PrecisionPoint p1 = new org.eclipse.draw2dl.geometry.PrecisionPoint(-2, 1);
		org.eclipse.draw2dl.geometry.PrecisionPoint p2 = new org.eclipse.draw2dl.geometry.PrecisionPoint(1, 1);
		org.eclipse.draw2dl.geometry.PrecisionPoint p3 = new org.eclipse.draw2dl.geometry.PrecisionPoint(0, 0);
		org.eclipse.draw2dl.geometry.PrecisionPoint p4 = new org.eclipse.draw2dl.geometry.PrecisionPoint(0, 3);
		s1 = new org.eclipse.draw2dl.geometry.Straight(p1, p2);
		s2 = new org.eclipse.draw2dl.geometry.Straight(p3, p4);
		intersection = s1.getIntersection(s2);
		assertTrue(intersection.equals(new org.eclipse.draw2dl.geometry.Vector(0, 1)));
		assertTrue(s1.contains(intersection));
		assertTrue(s2.contains(intersection));

		// check four rounding effects
		p1 = new org.eclipse.draw2dl.geometry.PrecisionPoint(-50, 5);
		p2 = new org.eclipse.draw2dl.geometry.PrecisionPoint(7, 104);
		p3 = new org.eclipse.draw2dl.geometry.PrecisionPoint(0, 0);
		p4 = new PrecisionPoint(0, 3);

		s1 = new org.eclipse.draw2dl.geometry.Straight(p1, p2);
		s2 = new org.eclipse.draw2dl.geometry.Straight(p3, p4);
		assertTrue(s1.intersects(s2));
		intersection = s1.getIntersection(s2);
		assertNotNull(intersection);
		assertTrue(s1.contains(intersection));
		assertTrue(s2.contains(intersection));
	}

	public void test_isParallelTo() {
		org.eclipse.draw2dl.geometry.Straight s1 = new org.eclipse.draw2dl.geometry.Straight(new org.eclipse.draw2dl.geometry.Vector(0, 0), new org.eclipse.draw2dl.geometry.Vector(3, 3));
		org.eclipse.draw2dl.geometry.Straight s2 = new org.eclipse.draw2dl.geometry.Straight(new org.eclipse.draw2dl.geometry.Vector(0, 4), new org.eclipse.draw2dl.geometry.Vector(2, 2));
		assertTrue(s1.isParallelTo(s2));

		s1 = new org.eclipse.draw2dl.geometry.Straight(new org.eclipse.draw2dl.geometry.Vector(0, 0), new org.eclipse.draw2dl.geometry.Vector(5, 5));
		s2 = new org.eclipse.draw2dl.geometry.Straight(new org.eclipse.draw2dl.geometry.Vector(0, 5), new org.eclipse.draw2dl.geometry.Vector(0, 5));
		assertFalse(s1.isParallelTo(s2));
	}

	public void test_getAngle() {
		org.eclipse.draw2dl.geometry.Straight s1 = new org.eclipse.draw2dl.geometry.Straight(new org.eclipse.draw2dl.geometry.Vector(0, 0), new org.eclipse.draw2dl.geometry.Vector(3, 3));
		org.eclipse.draw2dl.geometry.Straight s2 = new org.eclipse.draw2dl.geometry.Straight(new org.eclipse.draw2dl.geometry.Vector(0, 4), new org.eclipse.draw2dl.geometry.Vector(2, 2));
		assertTrue(s1.getAngle(s2) == 0.0);

		s1 = new org.eclipse.draw2dl.geometry.Straight(new org.eclipse.draw2dl.geometry.Vector(0, 0), new org.eclipse.draw2dl.geometry.Vector(5, 5));
		s2 = new org.eclipse.draw2dl.geometry.Straight(new org.eclipse.draw2dl.geometry.Vector(0, 5), new org.eclipse.draw2dl.geometry.Vector(0, 5));
		assertTrue((float) s1.getAngle(s2) == 45.0); // rounding effects
	}

	public void test_equals() {
		org.eclipse.draw2dl.geometry.Straight s1 = new org.eclipse.draw2dl.geometry.Straight(new org.eclipse.draw2dl.geometry.Vector(0, 0), new org.eclipse.draw2dl.geometry.Vector(3, 3));
		org.eclipse.draw2dl.geometry.Straight s2 = new Straight(new org.eclipse.draw2dl.geometry.Vector(4, 4), new Vector(2, 2));
		assertTrue(s1.equals(s2));
	}
}
