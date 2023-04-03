package org.eclipse.draw2d.test;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
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

	////////////////////////////////////////////////////////////////////////////
	//
	// Constructor tests
	//
	////////////////////////////////////////////////////////////////////////////
	public void test_constructor() throws Exception {
		assertEquals(0, 0, new Point());
	}

	public void test_constructor_int_int() throws Exception {
		assertEquals(-1, 2, new Point(-1, 2));
	}

	public void test_constructor_double_double() throws Exception {
		assertEquals((int) Math.PI, (int) -Math.E, new Point(Math.PI, -Math.E));
	}

	public void test_constructor_Point() throws Exception {
		Point template = new Point(-1, 2);
		assertEquals(-1, 2, new Point(template));
		assertEquals(-1, 2, template); // assert read only argument point
	}

	public void test_constructor_Swt_Point() throws Exception {
		org.eclipse.swt.graphics.Point template = new org.eclipse.swt.graphics.Point(-1, 2);
		assertEquals(-1, 2, new Point(template));
		// assert read only argument point
		assertEquals(-1, template.x);
		assertEquals(2, template.y);
	}

	public void test_constructorDimension() throws Exception {
		Dimension dimension = new Dimension(100, 200);
		assertEquals(100, 200, new Point(dimension));
		assertEquals(100, 200, dimension);
	}

	////////////////////////////////////////////////////////////////////////////
	//
	// Object test
	//
	////////////////////////////////////////////////////////////////////////////
	public void test_equals_Object() throws Exception {
		Point testPoint = new Point(-1, 2);
		assertFalse(testPoint.equals(null));
		assertFalse(testPoint.equals(new Object()));
		assertTrue(testPoint.equals(testPoint));
		assertTrue(testPoint.equals(new Point(testPoint)));
		assertFalse(testPoint.equals(new Point()));
	}

	public void test_hashCode_toString() throws Exception {
		assertEquals(0, new Point().hashCode());
		assertEquals((1 * 2) ^ (1 + 2), new Point(1, 2).hashCode());
		//
		assertNotNull(new Point().toString());
		assertNotNull(new Point(1, 2).toString());
	}

	////////////////////////////////////////////////////////////////////////////
	//
	// Copy tests
	//
	////////////////////////////////////////////////////////////////////////////
	public void test_getCopy() throws Exception {
		Point template = new Point(-1, 2);
		Point testPoint = template.getCopy();
		assertNotNull(testPoint);
		assertNotSame(template, testPoint);
		assertEquals(template, testPoint);
	}

	public void test_getSwtPoint() throws Exception {
		org.eclipse.swt.graphics.Point testPoint = new Point(-1, 2).getSWTPoint();
		assertNotNull(testPoint);
		assertEquals(-1, testPoint.x);
		assertEquals(2, testPoint.y);
	}

	////////////////////////////////////////////////////////////////////////////
	//
	// Bounds tests
	//
	////////////////////////////////////////////////////////////////////////////
	public void test_setLocation_int_int() throws Exception {
		Point testPoint = new Point();
		assertSame(testPoint, testPoint.setLocation(-1, 2));
		assertEquals(-1, 2, testPoint);
	}

	public void test_setLocation_Point() throws Exception {
		Point template = new Point(-1, 2);
		Point testPoint = new Point();
		assertSame(testPoint, testPoint.setLocation(template));
		assertEquals(template, testPoint);
	}

	public void test_setLocation_Swt_Point() throws Exception {
		Point testPoint = new Point();
		assertSame(testPoint, testPoint.setLocation(new org.eclipse.swt.graphics.Point(-1, 2)));
		assertEquals(-1, 2, testPoint);
	}

	////////////////////////////////////////////////////////////////////////////
	//
	// Oparation tests
	//
	////////////////////////////////////////////////////////////////////////////
	public void test_getDifference_Point() throws Exception {
		Dimension dimension = new Point(5, -5).getDifference(new Point(4, -4));
		assertEquals(1, dimension.width);
		assertEquals(-1, dimension.height);
	}

	public void test_getDistance2_Point() throws Exception {
		assertEquals(25, new Point(4, 7).getDistance2(new Point(1, 3)));
		assertEquals(25, new Point(-1, -2).getDistance2(new Point(-5, 1)));
	}

	public void test_getDistance_Point() throws Exception {
		assertEquals(5, new Point(4, 7).getDistance(new Point(1, 3)), 0);
		assertEquals(5, new Point(-1, -2).getDistance(new Point(-5, 1)), 0);
	}

	public void test_getDistanceOrthogonal() throws Exception {
		assertEquals(53, new Point(10, 20).getDistanceOrthogonal(new Point(51, 32)));
		assertEquals(53, new Point(51, 32).getDistanceOrthogonal(new Point(10, 20)));
		//
		assertEquals(60, new Point(-10, -20).getDistanceOrthogonal(new Point(10, 20)));
		assertEquals(60, new Point(10, 20).getDistanceOrthogonal(new Point(-10, -20)));
	}

	////////////////////////////////////////////////////////////////////////////
	//
	// Modify operation tests
	//
	////////////////////////////////////////////////////////////////////////////
	public void test_negate() throws Exception {
		Point testPoint = new Point(1, 2);
		assertSame(testPoint, testPoint.negate());
		assertEquals(-1, -2, testPoint);
		//
		assertSame(testPoint, testPoint.negate());
		assertEquals(1, 2, testPoint);
	}

	public void test_scale() throws Exception {
		// check work scal(double)
		Point testPoint = new Point(10, 20);
		assertSame(testPoint, testPoint.scale(0.5));
		assertEquals(5, 10, testPoint);
		//
		// check work scal(double, double)
		assertSame(testPoint, testPoint.scale(20, 10));
		assertEquals(100, 100, testPoint);
	}

	public void test_translate() throws Exception {
		Point testPoint = new Point(3, 5);
		//
		// check work translate(int, int)
		testPoint.translate(1, -1);
		assertEquals(4, 4, testPoint);
		//
		// check work translate(Dimension)
		Dimension dimension = new Dimension(-5, -5);
		testPoint.translate(dimension);
		assertEquals(-1, -1, testPoint);
		assertEquals(-5, -5, dimension);
		//
		// check work translate(Point)
		Point point = new Point(1, 1);
		testPoint.translate(point);
		assertEquals(0, 0, testPoint);
		assertEquals(1, 1, point);
		//
		// check work translate(Insets)
		Insets insets = new Insets(7, 5, 0, -1);
		testPoint.translate(insets);
		assertEquals(5, 7, testPoint);
		assertEquals(7, 5, 0, -1, insets);
	}

	public void test_transpose() throws Exception {
		Point testPoint = new Point(3, 5);
		assertSame(testPoint, testPoint.transpose());
		assertEquals(5, 3, testPoint);
	}

	////////////////////////////////////////////////////////////////////////////
	//
	// Copy operation tests
	//
	////////////////////////////////////////////////////////////////////////////
	public void test_getNegated() throws Exception {
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

	public void test_getScaled() throws Exception {
		Point template = new Point(10, 20);
		Point testPoint = template.getScaled(0.5);
		assertNotSame(template, testPoint);
		assertEquals(5, 10, testPoint);
	}

	public void test_getTranslated() throws Exception {
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

	public void test_getTransposed() throws Exception {
		Point template = new Point(3, 5);
		Point testPoint = template.getTransposed();
		assertNotSame(template, testPoint);
		assertEquals(5, 3, testPoint);
	}

	////////////////////////////////////////////////////////////////////////////
	//
	// Static Utils tests
	//
	////////////////////////////////////////////////////////////////////////////
	public void test_max() throws Exception {
		Point point1 = new Point(10, 20);
		Point point2 = new Point(71, 32);
		Point testPoint = Point.max(point1, point2);
		assertNotNull(testPoint);
		assertNotSame(point1, testPoint);
		assertNotSame(point2, testPoint);
		assertEquals(71, 32, testPoint);
		//
		assertEquals(14, 100, Point.max(new Point(14, 15), new Point(14, 100)));
		//
		assertEquals(89, 57, Point.max(new Point(56, 57), new Point(89, 57)));
	}

	public void test_min() throws Exception {
		Point point1 = new Point(10, 20);
		Point point2 = new Point(71, 32);
		Point testPoint = Point.min(point1, point2);
		assertNotNull(testPoint);
		assertNotSame(point1, testPoint);
		assertNotSame(point2, testPoint);
		assertEquals(10, 20, testPoint);
		//
		assertEquals(14, 15, Point.min(new Point(14, 15), new Point(14, 100)));
		//
		assertEquals(56, 57, Point.min(new Point(56, 57), new Point(89, 57)));
	}
}
