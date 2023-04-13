package org.eclipse.draw2d.test;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

public class PointTests extends BaseTestCase {

	public void testMin() {
		assertTrue(Point.min(new Point(1, 3), new Point(2, 6)).equals(new Point(1, 3)));
		assertTrue(Point.min(new Point(4, 8), new Point(2, 6)).equals(new Point(2, 6)));
		assertTrue(Point.min(new Point(4, 8), new Point(2, 10)).equals(new Point(2, 8)));
		assertTrue(Point.min(new Point(4, 12), new Point(6, 10)).equals(new Point(4, 10)));
	}

	public void testMax() {
		assertTrue(Point.max(new Point(1, 3), new Point(2, 6)).equals(new Point(2, 6)));
		assertTrue(Point.max(new Point(4, 8), new Point(2, 6)).equals(new Point(4, 8)));
		assertTrue(Point.max(new Point(4, 8), new Point(2, 10)).equals(new Point(4, 10)));
		assertTrue(Point.max(new Point(4, 12), new Point(6, 10)).equals(new Point(6, 12)));
	}

	public void testEquals() {
		assertTrue(new Point(4, 7).equals(4, 7));
		assertFalse(new Point(3, 6).equals(3, 7));
		assertFalse(new Point(3, 6).equals(4, 6));
	}

	public void testDifference() {
		Point p1 = new Point(4, 7);
		Point p2 = new Point(2, 4);
		// check returns expected result
		assertTrue(p1.getDifference(p2).equals(2, 3));
		// check is side-effect free
		assertTrue(p1.equals(4, 7));
		assertTrue(p2.equals(2, 4));
	}

	public void testTranslate() {
		Point p1 = new Point(3, 6);
		p1.translate(new Dimension(4711, 567));
		assertTrue(p1.equals(4714, 573));

		p1.translate(0.4, 0.7);
		assertTrue(p1.equals(4714, 573));
	}

	public void testSetX() {
		assertTrue(new Point(4711, 678).setX(3).equals(3, 678));
	}

	public void testSetY() {
		assertTrue(new Point(4711, 678).setY(3).equals(4711, 3));
	}

	public void testSetLocation() {
		assertTrue(new Point().setLocation(4711, 678).equals(4711, 678));
		assertTrue(new Point().setLocation(new Point(4711, 678)).equals(4711, 678));
	}

	public void testConstructor() throws Exception {
		assertEquals(0, 0, new Point());
	}

	public void testConstructorIntInt() throws Exception {
		assertEquals(-1, 2, new Point(-1, 2));
	}

	public void testConstructorDoubleDouble() throws Exception {
		assertEquals((int) Math.PI, (int) -Math.E, new Point(Math.PI, -Math.E));
	}

	public void testConstructorPoint() throws Exception {
		Point template = new Point(-1, 2);
		assertEquals(-1, 2, new Point(template));
		assertEquals(-1, 2, template); // assert read only argument point
	}

	public void testConstructorSwtPoint() throws Exception {
		org.eclipse.swt.graphics.Point template = new org.eclipse.swt.graphics.Point(-1, 2);
		assertEquals(-1, 2, new Point(template));
		// assert read only argument point
		assertEquals(-1, template.x);
		assertEquals(2, template.y);
	}

	public void testConstructorDimension() throws Exception {
		Dimension dimension = new Dimension(100, 200);
		assertEquals(100, 200, new Point(dimension));
		assertEquals(100, 200, dimension);
	}

	public void testEqualsObject() throws Exception {
		Point testPoint = new Point(-1, 2);
		assertFalse(testPoint.equals(null));
		assertFalse(testPoint.equals(new Object()));
		assertTrue(testPoint.equals(testPoint));
		assertTrue(testPoint.equals(new Point(testPoint)));
		assertFalse(testPoint.equals(new Point()));
	}

	public void testHashCodeToString() throws Exception {
		assertEquals(0, new Point().hashCode());
		assertEquals((1 * 2) ^ (1 + 2), new Point(1, 2).hashCode());
		//
		assertNotNull(new Point().toString());
		assertNotNull(new Point(1, 2).toString());
	}

	public void testGetCopy() throws Exception {
		Point template = new Point(-1, 2);
		Point testPoint = template.getCopy();
		assertNotNull(testPoint);
		assertNotSame(template, testPoint);
		assertEquals(template, testPoint);
	}

	public void testGetSwtPoint() throws Exception {
		org.eclipse.swt.graphics.Point testPoint = new Point(-1, 2).getSWTPoint();
		assertNotNull(testPoint);
		assertEquals(-1, testPoint.x);
		assertEquals(2, testPoint.y);
	}

	public void testSetLocationIntInt() throws Exception {
		Point testPoint = new Point();
		assertSame(testPoint, testPoint.setLocation(-1, 2));
		assertEquals(-1, 2, testPoint);
	}

	public void testSetLocationPoint() throws Exception {
		Point template = new Point(-1, 2);
		Point testPoint = new Point();
		assertSame(testPoint, testPoint.setLocation(template));
		assertEquals(template, testPoint);
	}

	public void testGetDifferencePoint() throws Exception {
		Dimension dimension = new Point(5, -5).getDifference(new Point(4, -4));
		assertEquals(1, dimension.width);
		assertEquals(-1, dimension.height);
	}

	public void testGetDistance2Point() throws Exception {
		assertEquals(25, new Point(4, 7).getDistance2(new Point(1, 3)));
		assertEquals(25, new Point(-1, -2).getDistance2(new Point(-5, 1)));
	}

	public void testGetDistancePoint() throws Exception {
		assertEquals(5, new Point(4, 7).getDistance(new Point(1, 3)), 0);
		assertEquals(5, new Point(-1, -2).getDistance(new Point(-5, 1)), 0);
	}

	public void testGetDistanceOrthogonal() throws Exception {
		assertEquals(53, new Point(10, 20).getDistanceOrthogonal(new Point(51, 32)));
		assertEquals(53, new Point(51, 32).getDistanceOrthogonal(new Point(10, 20)));
		//
		assertEquals(60, new Point(-10, -20).getDistanceOrthogonal(new Point(10, 20)));
		assertEquals(60, new Point(10, 20).getDistanceOrthogonal(new Point(-10, -20)));
	}

	public void testNegate() throws Exception {
		Point testPoint = new Point(1, 2);
		assertSame(testPoint, testPoint.negate());
		assertEquals(-1, -2, testPoint);
		//
		assertSame(testPoint, testPoint.negate());
		assertEquals(1, 2, testPoint);
	}

	public void testScale() throws Exception {
		// check work scal(double)
		Point testPoint = new Point(10, 20);
		assertSame(testPoint, testPoint.scale(0.5));
		assertEquals(5, 10, testPoint);
		//
		// check work scal(double, double)
		assertSame(testPoint, testPoint.scale(20, 10));
		assertEquals(100, 100, testPoint);
	}

	public void testTranspose() throws Exception {
		Point testPoint = new Point(3, 5);
		assertSame(testPoint, testPoint.transpose());
		assertEquals(5, 3, testPoint);
	}

	public void testGetNegated() throws Exception {
		Point template = new Point(1, 2);
		Point testPoint = template.getNegated();
		assertNotSame(template, testPoint);
		assertEquals(-1, -2, testPoint);
		//
		template = testPoint;
		testPoint = template.getNegated();
		assertNotSame(template, testPoint);
		assertEquals(1, 2, testPoint);
	}

	public void testGetScaled() throws Exception {
		Point template = new Point(10, 20);
		Point testPoint = template.getScaled(0.5);
		assertNotSame(template, testPoint);
		assertEquals(5, 10, testPoint);
	}

	public void testGetTranslated() throws Exception {
		// check work getTranslated(int, int)
		Point template = new Point(3, 5);
		Point testPoint = template.getTranslated(1, -1);
		assertNotSame(template, testPoint);
		assertEquals(4, 4, testPoint);
		//
		// check work getTranslated(Dimension)
		template = testPoint;
		testPoint = template.getTranslated(new Dimension(-5, -5));
		assertNotSame(template, testPoint);
		assertEquals(-1, -1, testPoint);
		//
		// check work getTranslated(Point)
		template = testPoint;
		testPoint = template.getTranslated(new Point(1, 1));
		assertNotSame(template, testPoint);
		assertEquals(0, 0, testPoint);
	}

	public void testGetTransposed() throws Exception {
		Point template = new Point(3, 5);
		Point testPoint = template.getTransposed();
		assertNotSame(template, testPoint);
		assertEquals(5, 3, testPoint);
	}
}
