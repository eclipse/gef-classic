package org.eclipse.draw2dl.test;

import junit.framework.TestCase;

import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Point;

public class PointTests extends TestCase {

	public void testMin() {
		assertTrue(org.eclipse.draw2dl.geometry.Point.min(new org.eclipse.draw2dl.geometry.Point(1, 3), new org.eclipse.draw2dl.geometry.Point(2, 6)).equals(
				new org.eclipse.draw2dl.geometry.Point(1, 3)));
		assertTrue(org.eclipse.draw2dl.geometry.Point.min(new org.eclipse.draw2dl.geometry.Point(4, 8), new org.eclipse.draw2dl.geometry.Point(2, 6)).equals(
				new org.eclipse.draw2dl.geometry.Point(2, 6)));
		assertTrue(org.eclipse.draw2dl.geometry.Point.min(new org.eclipse.draw2dl.geometry.Point(4, 8), new org.eclipse.draw2dl.geometry.Point(2, 10)).equals(
				new org.eclipse.draw2dl.geometry.Point(2, 8)));
		assertTrue(org.eclipse.draw2dl.geometry.Point.min(new org.eclipse.draw2dl.geometry.Point(4, 12), new org.eclipse.draw2dl.geometry.Point(6, 10)).equals(
				new org.eclipse.draw2dl.geometry.Point(4, 10)));
	}

	public void testMax() {
		assertTrue(org.eclipse.draw2dl.geometry.Point.max(new org.eclipse.draw2dl.geometry.Point(1, 3), new org.eclipse.draw2dl.geometry.Point(2, 6)).equals(
				new org.eclipse.draw2dl.geometry.Point(2, 6)));
		assertTrue(org.eclipse.draw2dl.geometry.Point.max(new org.eclipse.draw2dl.geometry.Point(4, 8), new org.eclipse.draw2dl.geometry.Point(2, 6)).equals(
				new org.eclipse.draw2dl.geometry.Point(4, 8)));
		assertTrue(org.eclipse.draw2dl.geometry.Point.max(new org.eclipse.draw2dl.geometry.Point(4, 8), new org.eclipse.draw2dl.geometry.Point(2, 10)).equals(
				new org.eclipse.draw2dl.geometry.Point(4, 10)));
		assertTrue(org.eclipse.draw2dl.geometry.Point.max(new org.eclipse.draw2dl.geometry.Point(4, 12), new org.eclipse.draw2dl.geometry.Point(6, 10)).equals(
				new org.eclipse.draw2dl.geometry.Point(6, 12)));
	}

	public void testEquals() {
		assertTrue(new org.eclipse.draw2dl.geometry.Point(4, 7).equals(4, 7));
		assertFalse(new org.eclipse.draw2dl.geometry.Point(3, 6).equals(3, 7));
		assertFalse(new org.eclipse.draw2dl.geometry.Point(3, 6).equals(4, 6));
	}

	public void testDifference() {
		org.eclipse.draw2dl.geometry.Point p1 = new org.eclipse.draw2dl.geometry.Point(4, 7);
		org.eclipse.draw2dl.geometry.Point p2 = new org.eclipse.draw2dl.geometry.Point(2, 4);
		// check returns expected result
		assertTrue(p1.getDifference(p2).equals(2, 3));
		// check is side-effect free
		assertTrue(p1.equals(4, 7));
		assertTrue(p2.equals(2, 4));
	}

	public void testTranslate() {
		org.eclipse.draw2dl.geometry.Point p1 = new org.eclipse.draw2dl.geometry.Point(3, 6);
		p1.translate(new Dimension(4711, 567));
		assertTrue(p1.equals(4714, 573));

		p1.translate(0.4, 0.7);
		assertTrue(p1.equals(4714, 573));
	}

	public void testSetX() {
		assertTrue(new org.eclipse.draw2dl.geometry.Point(4711, 678).setX(3).equals(3, 678));
	}

	public void testSetY() {
		assertTrue(new org.eclipse.draw2dl.geometry.Point(4711, 678).setY(3).equals(4711, 3));
	}

	public void testSetLocation() {
		assertTrue(new org.eclipse.draw2dl.geometry.Point().setLocation(4711, 678).equals(4711, 678));
		assertTrue(new org.eclipse.draw2dl.geometry.Point().setLocation(new Point(4711, 678)).equals(4711,
				678));
	}
}
