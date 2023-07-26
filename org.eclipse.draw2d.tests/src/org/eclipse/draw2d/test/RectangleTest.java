/*******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.test;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.junit.Test;

public class RectangleTest extends BaseTestCase {

	@Test
	public void test_creationSymmetry() {
		Point topLeft = new Point(0, 0);
		Point topRight = new Point(10, 0);
		Point bottomLeft = new Point(0, 10);
		Point bottomRight = new Point(10, 10);
		Rectangle rect1 = new Rectangle(topLeft, bottomRight);
		Rectangle rect2 = new Rectangle(topRight, bottomLeft);

		assertTrue(rect1.equals(rect2));
	}

	@Test
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

	@Test
	public void test_sameBehavior() {

		Point p1 = new Point(0, 0);
		Point p2 = new Point(10, 10);
		Rectangle origRect = new Rectangle();
		origRect.setLocation(p1);
		origRect.union(p2);

		Rectangle newRect = new Rectangle(p1, p2);

		assertTrue(origRect.equals(newRect));
	}

	@Test
	public void testGetExpanded() {
		Rectangle r = new Rectangle(1, 1, 3, 5);
		assertEquals(new Dimension(9, 9), r.getExpanded(3, 2).getSize());
		assertEquals(new Dimension(9, 9), r.getExpanded(3.4, 2.7).getSize());
	}

	@Test
	public void testGetShrinked() {
		Rectangle r = new Rectangle(1, 1, 6, 7);
		assertEquals(new Dimension(2, 3), r.getShrinked(2, 2).getSize());
		assertEquals(new Dimension(2, 3), r.getShrinked(2.4, 2.7).getSize());
	}

	@Test
	public void testGetTranslated() {
		Rectangle r = new Rectangle(1, 1, 6, 7);
		assertEquals(new Point(5, 5), r.getTranslated(4, 4).getLocation());
		assertEquals(new Point(5, 5), r.getTranslated(4.4, 4.8).getLocation());
	}

	@Test
	public void testContains() {
		Rectangle r = new Rectangle(1, 1, 6, 7);
		assertTrue(r.contains(6, 7));
		assertTrue(r.contains(6.9, 7.9));
	}

	@Test
	public void testConstructor() throws Exception {
		assertEquals(0, 0, 0, 0, new Rectangle());
	}

	@Test
	public void testConstructorPointDimension() throws Exception {
		Point location = new Point(1, 2);
		Dimension size = new Dimension(3, 4);
		assertEquals(1, 2, 3, 4, new Rectangle(location, size));
		assertEquals(1, 2, location);
		assertEquals(3, 4, size);
	}

	@Test
	public void testConstructorRectangle() throws Exception {
		Rectangle template = new Rectangle(1, 2, 3, 4);
		assertEquals(1, 2, 3, 4, new Rectangle(template));
		assertEquals(1, 2, 3, 4, template);
	}

	@Test
	public void testConstructorSwtRectangle() throws Exception {
		org.eclipse.swt.graphics.Rectangle template = new org.eclipse.swt.graphics.Rectangle(1, 2, 3, 4);
		assertEquals(1, 2, 3, 4, new Rectangle(template));
		assertEquals(1, template.x);
		assertEquals(2, template.y);
		assertEquals(3, template.width);
		assertEquals(4, template.height);
	}

	@Test
	public void testConstructorInts() throws Exception {
		assertEquals(1, 2, 3, 4, new Rectangle(1, 2, 3, 4));
	}

	@Test
	public void testConstructorPointPoint() throws Exception {
		Point point1 = new Point(10, 5);
		Point point2 = new Point(40, 30);
		Rectangle testRectangle = new Rectangle(point1, point2);
		assertEquals(10, 5, 31, 26, testRectangle);
		assertEquals(10, 5, point1);
		assertEquals(40, 30, point2);
		//
		point1 = new Point(20, 20);
		point2 = new Point(15, 15);
		testRectangle = new Rectangle(point1, point2);
		assertEquals(15, 15, 6, 6, testRectangle);
		assertEquals(20, 20, point1);
		assertEquals(15, 15, point2);
	}

	@Test
	public void testEqualsObject() throws Exception {
		Rectangle testRectangle = new Rectangle(10, 15, 70, 30);
		assertFalse(testRectangle.equals(null));
		assertFalse(testRectangle.equals(new Object()));
		assertTrue(testRectangle.equals(testRectangle));
		assertTrue(testRectangle.equals(new Rectangle(testRectangle)));
		assertFalse(testRectangle.equals(new Rectangle()));
	}

	@Test
	public void testToString() throws Exception {
		assertNotNull(new Rectangle().toString());
		assertNotNull(new Rectangle(1, 2, 3, 4).toString());
	}

	@Test
	public void testGetLocation() throws Exception {
		Rectangle template = new Rectangle(10, 15, 70, 30);
		assertEquals(10, 15, template.getLocation());
		assertEquals(10, 15, 70, 30, template);
	}

	@Test
	public void testGetSize() throws Exception {
		Rectangle template = new Rectangle(10, 15, 70, 30);
		assertEquals(70, 30, template.getSize());
		assertEquals(10, 15, 70, 30, template);
	}

	@Test
	public void testBottom() throws Exception {
		Rectangle template = new Rectangle(10, 15, 70, 30);
		assertEquals(45, template.bottom());
		assertEquals(10, 15, 70, 30, template);
	}

	@Test
	public void testRight() throws Exception {
		Rectangle template = new Rectangle(10, 15, 70, 30);
		assertEquals(80, template.right());
		assertEquals(10, 15, 70, 30, template);
	}

	@Test
	public void testLeft() throws Exception {
		Rectangle template = new Rectangle(10, 15, 70, 30);
		assertEquals(10, template.left());
	}

	@Test
	public void testTop() throws Exception {
		Rectangle template = new Rectangle(10, 15, 70, 30);
		assertEquals(15, template.top());
	}

	@Test
	public void testContainsIntInt() throws Exception {
		Rectangle template = new Rectangle(10, 15, 70, 30);
		//
		assertTrue(template.contains(10, 15));
		assertEquals(10, 15, 70, 30, template);
		//
		assertFalse(template.contains(-10, 15));
		assertEquals(10, 15, 70, 30, template);
		//
		assertTrue(template.contains(70, 30));
		assertFalse(template.contains(80, 45));
	}

	@Test
	public void testContainsPoint() throws Exception {
		Rectangle template = new Rectangle(10, 15, 70, 30);
		Point point = new Point(10, 15);
		//
		assertTrue(template.contains(point));
		assertEquals(10, 15, 70, 30, template);
		assertEquals(10, 15, point);
		//
		assertFalse(template.contains(new Point(-10, 15)));
		assertEquals(10, 15, 70, 30, template);
		//
		assertTrue(template.contains(new Point(70, 30)));
		assertFalse(template.contains(new Point(80, 45)));
	}

	@Test
	public void testIntersects() throws Exception {
		Rectangle rectangle1 = new Rectangle(10, 15, 70, 30);
		//
		assertTrue(rectangle1.intersects(rectangle1));
		assertEquals(10, 15, 70, 30, rectangle1);
		//
		Rectangle rectangle2 = new Rectangle(0, 30, 50, 40);
		assertTrue(rectangle1.intersects(rectangle2));
		assertEquals(0, 30, 50, 40, rectangle2);
		//
		assertFalse(rectangle1.intersects(new Rectangle(10, 15, 0, 0)));
		//
		assertTrue(rectangle1.intersects(new Rectangle(0, 30, 100, 10)));
		//
		assertFalse(rectangle1.intersects(new Rectangle(-100, -100, 10, 10)));
		//
		assertFalse(rectangle1.intersects(new Rectangle(0, 0, 5, 10)));
	}

	@Test
	public void testIsEmpty() throws Exception {
		assertTrue(new Rectangle().isEmpty());
		assertTrue(new Rectangle(10, 10, -3, 7).isEmpty());
		assertFalse(new Rectangle(-10, -10, 1, 2).isEmpty());
	}

	@Test
	public void testTouches() throws Exception {
		Rectangle rectangle1 = new Rectangle(10, 15, 70, 30);
		//
		assertTrue(rectangle1.touches(rectangle1));
		assertEquals(10, 15, 70, 30, rectangle1);
		//
		Rectangle rectangle2 = new Rectangle(0, 30, 50, 40);
		assertTrue(rectangle1.touches(rectangle2));
		assertEquals(0, 30, 50, 40, rectangle2);
		//
		assertTrue(rectangle1.touches(new Rectangle(10, 15, 0, 0)));
		//
		assertTrue(rectangle1.touches(new Rectangle(0, 30, 100, 10)));
		//
		assertFalse(rectangle1.touches(new Rectangle(-100, -100, 10, 10)));
		//
		assertFalse(rectangle1.touches(new Rectangle(0, 0, 5, 10)));
	}

	@Test
	public void testSetBoundsRectangle() throws Exception {
		Rectangle template = new Rectangle(10, 15, 70, 30);
		Rectangle testRectangle = new Rectangle();
		assertSame(testRectangle, testRectangle.setBounds(template));
		assertEquals(template, testRectangle);
		assertEquals(10, 15, 70, 30, template);
	}

	@Test
	public void testSetBoundsInts() throws Exception {
		Rectangle testRectangle = new Rectangle(10, 15, 70, 30);
		assertSame(testRectangle, testRectangle.setBounds(-100, 200, 700, 800));
		assertEquals(-100, 200, 700, 800, testRectangle);
	}

	@Test
	public void testSetLocation() throws Exception {
		// check work setLocation(Point)
		Point location = new Point(10, 30);
		Rectangle testRectangle = new Rectangle(-100, 200, 700, 800);
		assertSame(testRectangle, testRectangle.setLocation(location));
		assertEquals(10, 30, 700, 800, testRectangle);
		assertEquals(10, 30, location);
		//
		// check work setLocation(int, int)
		assertSame(testRectangle, testRectangle.setLocation(-100, 200));
		assertEquals(-100, 200, 700, 800, testRectangle);
	}

	@Test
	public void testSetSize() throws Exception {
		// check work setSize(Dimension)
		Dimension size = new Dimension(110, 15);
		Rectangle testRectangle = new Rectangle(-100, 200, 700, 800);
		assertSame(testRectangle, testRectangle.setSize(size));
		assertEquals(-100, 200, 110, 15, testRectangle);
		assertEquals(110, 15, size);
		//
		// check work setSize(int, int)
		assertSame(testRectangle, testRectangle.setSize(1024, 768));
		assertEquals(-100, 200, 1024, 768, testRectangle);
	}

	@Test
	public void testCrop() throws Exception {
		Rectangle template = new Rectangle(10, 15, 70, 30);
		assertSame(template, template.crop(null));
		assertEquals(10, 15, 70, 30, template);
		//
		Insets insets = new Insets(1, 2, 3, 4);
		assertSame(template, template.crop(insets));
		assertEquals(1, 2, 3, 4, insets);
		assertEquals(12, 16, 64, 26, template);
		//
		insets = new Insets(4, 3, 2, 1);
		assertSame(template, template.crop(insets));
		assertEquals(15, 20, 60, 20, template);
		//
		insets = new Insets(20, 0, 20, 0);
		assertSame(template, template.crop(insets));
		assertEquals(15, 40, 60, 0, template);
		//
		insets = new Insets(0, 40, 0, 40);
		assertSame(template, template.crop(insets));
		assertEquals(55, 40, 0, 0, template);
	}

	@Test
	public void testExpandIntInt() throws Exception {
		Rectangle template = new Rectangle(1, 2, 3, 4);
		assertSame(template, template.expand(2, 3));
		assertEquals(-1, -1, 7, 10, template);
	}

	@Test
	public void testExpandInsets() throws Exception {
		Rectangle template = new Rectangle(1, 2, 3, 4);
		Insets insets = new Insets(5, 4, 3, 2);
		assertSame(template, template.expand(insets));
		assertEquals(-3, -3, 9, 12, template);
		assertEquals(5, 4, 3, 2, insets);
	}

	@Test
	public void testIntersect() throws Exception {
		Rectangle rectangle1 = new Rectangle(10, 15, 70, 30);
		//
		assertSame(rectangle1, rectangle1.intersect(rectangle1));
		assertEquals(10, 15, 70, 30, rectangle1);
		//
		Rectangle rectangle2 = new Rectangle(0, 30, 50, 40);
		assertSame(rectangle1, rectangle1.intersect(rectangle2));
		assertEquals(0, 30, 50, 40, rectangle2);
		assertEquals(10, 30, 40, 15, rectangle1);
		//
		rectangle1 = new Rectangle(10, 15, 70, 30);
		assertSame(rectangle1, rectangle1.intersect(new Rectangle(0, 30, 100, 10)));
		assertEquals(10, 30, 70, 10, rectangle1);
		//
		rectangle1 = new Rectangle(10, 15, 70, 30);
		assertSame(rectangle1, rectangle1.intersect(new Rectangle(-100, -100, 10, 10)));
		assertTrue(rectangle1.isEmpty());
		//
		rectangle1 = new Rectangle(10, 15, 70, 30);
		assertSame(rectangle1, rectangle1.intersect(new Rectangle(0, 0, 5, 10)));
		assertTrue(rectangle1.isEmpty());
	}

	@Test
	public void testResize() throws Exception {
		// check work resize(Dimension)
		Rectangle template = new Rectangle(1, 2, 3, 4);
		Dimension size = new Dimension(11, -17);
		assertSame(template, template.resize(size));
		assertEquals(1, 2, 14, -13, template);
		assertEquals(11, -17, size);
		//
		// check work resize(int, int)
		assertSame(template, template.resize(-13, 14));
		assertEquals(1, 2, 1, 1, template);
	}

	@Test
	public void testScale() throws Exception {
		// check work scale(double)
		Rectangle template = new Rectangle(10, 10, 20, 20);
		assertSame(template, template.scale(0.5));
		assertEquals(5, 5, 10, 10, template);
		//
		// check work scale(double, double)
		template = new Rectangle(10, 10, 20, 20);
		assertSame(template, template.scale(0.5, 0.5));
		assertEquals(5, 5, 10, 10, template);
	}

	@Test
	public void testShrink() throws Exception {
		Rectangle template = new Rectangle(1, 2, 30, 40);
		Point center = new Point(16, 22);
		assertSame(template, template.shrink(5, 7));
		assertEquals(6, 9, 20, 26, template);
		assertEquals(center, template.getCenter());
		//
		template = new Rectangle(10, 20, 3, 4);
		center = new Point(11, 22);
		assertSame(template, template.shrink(-5, -7));
		assertEquals(5, 13, 13, 18, template);
		assertEquals(center, template.getCenter());
		//
		template = new Rectangle(10, 20, 10, 10);
		center = new Point(15, 25);
		assertSame(template, template.shrink(20, 20));
		assertEquals(15, 25, 0, 0, template);
		assertEquals(center, template.getCenter());
	}

	@Test
	public void testTranslate() throws Exception {
		// check work translate(int, int)
		Rectangle template = new Rectangle(1, 2, 3, 4);
		template.translate(15, 17);
		assertEquals(16, 19, 3, 4, template);
		//
		// check work translate(Point)
		template = new Rectangle(1, 2, 3, 4);
		Point point = new Point(-3, -4);
		template.translate(point);
		assertEquals(-2, -2, 3, 4, template);
		assertEquals(-3, -4, point);
	}

	@Test
	public void testTranspose() throws Exception {
		Rectangle template = new Rectangle(1, 2, 3, 4);
		assertSame(template, template.transpose());
		assertEquals(2, 1, 4, 3, template);
	}

	@Test
	public void testUnionDimension() throws Exception {
		Rectangle template = new Rectangle(1, 2, 3, 4);
		Dimension dimension = new Dimension(-1, 7);
		//
		assertSame(template, template.union(dimension));
		assertEquals(1, 2, 3, 7, template);
		assertEquals(-1, 7, dimension);
	}

	@Test
	public void testUnionIntInt() throws Exception {
		Rectangle template = new Rectangle(10, 20, 30, 40);
		//
		assertSame(template, template.union(15, 25));
		assertEquals(10, 20, 30, 40, template);
		//
		assertSame(template, template.union(50, 70));
		assertEquals(10, 20, 41, 51, template);
		//
		assertSame(template, template.union(55, 30));
		assertEquals(10, 20, 46, 51, template);
		//
		assertSame(template, template.union(50, 77));
		assertEquals(10, 20, 46, 58, template);
		//
		assertSame(template, template.union(6, 47));
		assertEquals(6, 20, 50, 58, template);
		//
		assertSame(template, template.union(26, 17));
		assertEquals(6, 17, 50, 61, template);
		//
		assertSame(template, template.union(0, 0));
		assertEquals(0, 0, 56, 78, template);
	}

	@Test
	public void testUnionPoint() throws Exception {
		Rectangle template = new Rectangle(10, 20, 30, 40);
		Point point = new Point(15, 25);
		//
		template.union(point);
		assertEquals(10, 20, 30, 40, template);
		assertEquals(15, 25, point);
		//
		template.union(point.setLocation(50, 70));
		assertEquals(10, 20, 41, 51, template);
		assertEquals(50, 70, point);
		//
		template.union(point.setLocation(55, 30));
		assertEquals(10, 20, 46, 51, template);
		assertEquals(55, 30, point);
		//
		template.union(point.setLocation(50, 77));
		assertEquals(10, 20, 46, 58, template);
		assertEquals(50, 77, point);
		//
		template.union(point.setLocation(6, 47));
		assertEquals(6, 20, 50, 58, template);
		assertEquals(6, 47, point);
		//
		template.union(point.setLocation(26, 17));
		assertEquals(6, 17, 50, 61, template);
		assertEquals(26, 17, point);
		//
		template.union(point.setLocation(0, 0));
		assertEquals(0, 0, 56, 78, template);
		assertEquals(0, 0, point);
	}

	@Test
	public void testUnion4int() throws Exception {
		Rectangle template = new Rectangle(10, 20, 30, 40);
		//
		assertSame(template, template.union(10, 20, 30, 40));
		assertEquals(10, 20, 30, 40, template);
		//
		assertSame(template, template.union(5, 7, 10, 10));
		assertEquals(5, 7, 35, 53, template);
		//
		assertSame(template, template.union(10, 20, 100, 200));
		assertEquals(5, 7, 105, 213, template);
	}

	@Test
	public void testUnionRectangle() throws Exception {
		Rectangle template = new Rectangle(10, 20, 30, 40);
		//
		assertSame(template, template.union(template));
		assertEquals(10, 20, 30, 40, template);
		//
		assertSame(template, template.union((Rectangle) null));
		assertEquals(10, 20, 30, 40, template);
		//
		Rectangle rectangle = new Rectangle(5, 7, 10, 10);
		assertSame(template, template.union(rectangle));
		assertEquals(5, 7, 35, 53, template);
		assertEquals(5, 7, 10, 10, rectangle);
		//
		rectangle = new Rectangle(10, 20, 100, 200);
		assertSame(template, template.union(rectangle));
		assertEquals(5, 7, 105, 213, template);
		assertEquals(10, 20, 100, 200, rectangle);
	}

	@Test
	public void testSetX() throws Exception {
		Rectangle rectangle = new Rectangle(1, 2, 4, 3);
		rectangle.setX(0);
		assertEquals(0, 2, 4, 3, rectangle);
		rectangle.setX(1);
		assertEquals(1, 2, 4, 3, rectangle);
	}

	@Test
	public void testSetY() throws Exception {
		Rectangle rectangle = new Rectangle(1, 2, 3, 4);
		rectangle.setY(1);
		assertEquals(1, 1, 3, 4, rectangle);
		rectangle.setY(3);
		assertEquals(1, 3, 3, 4, rectangle);
	}

	@Test
	public void testSetRight() throws Exception {
		Rectangle rectangle = new Rectangle(1, 2, 3, 4);
		rectangle.setRight(5);
		assertEquals(1, 2, 4, 4, rectangle);
		rectangle.setRight(2);
		assertEquals(1, 2, 1, 4, rectangle);
		rectangle.setRight(1);
		assertEquals(1, 2, 0, 4, rectangle);
		rectangle.setRight(-5);
		assertEquals(1, 2, 0, 4, rectangle);
	}

	@Test
	public void testSetBottom() throws Exception {
		Rectangle rectangle = new Rectangle(1, 2, 3, 4);
		rectangle.setBottom(7);
		assertEquals(1, 2, 3, 5, rectangle);
		rectangle.setBottom(5);
		assertEquals(1, 2, 3, 3, rectangle);
		rectangle.setBottom(1);
		assertEquals(1, 2, 3, 0, rectangle);
		rectangle.setBottom(-5);
		assertEquals(1, 2, 3, 0, rectangle);
	}

	@Test
	public void testShrinkLeft() throws Exception {
		Rectangle rectangle = new Rectangle(1, 2, 10, 4);
		rectangle.shrinkLeft(5);
		assertEquals(6, 2, 5, 4, rectangle);
		rectangle.shrinkLeft(2);
		assertEquals(8, 2, 3, 4, rectangle);
		rectangle.shrinkLeft(5);
		assertEquals(11, 2, 0, 4, rectangle);
	}

	@Test
	public void testShrinkTop() throws Exception {
		Rectangle rectangle = new Rectangle(1, 2, 3, 11);
		rectangle.shrinkTop(5);
		assertEquals(1, 7, 3, 6, rectangle);
		rectangle.shrinkTop(4);
		assertEquals(1, 11, 3, 2, rectangle);
		rectangle.shrinkTop(7);
		assertEquals(1, 13, 3, 0, rectangle);
	}

	@Test
	public void testGetCopy() throws Exception {
		Rectangle template = new Rectangle(1, 2, 3, 4);
		Rectangle testRectangle = template.getCopy();
		assertNotNull(testRectangle);
		assertNotSame(template, testRectangle);
		assertEquals(template, testRectangle);
	}

	@Test
	public void testGetBottom() throws Exception {
		Rectangle rectangle = new Rectangle(10, 10, 20, 30);
		assertEquals(20, 40, rectangle.getBottom());
		assertEquals(10, 10, 20, 30, rectangle);
	}

	@Test
	public void testGetBottomLeft() throws Exception {
		Rectangle rectangle = new Rectangle(10, 10, 20, 30);
		assertEquals(10, 40, rectangle.getBottomLeft());
		assertEquals(10, 10, 20, 30, rectangle);
	}

	@Test
	public void testGetBottomRight() throws Exception {
		Rectangle rectangle = new Rectangle(10, 10, 20, 30);
		assertEquals(30, 40, rectangle.getBottomRight());
		assertEquals(10, 10, 20, 30, rectangle);
	}

	@Test
	public void testGetCenter() throws Exception {
		Rectangle rectangle = new Rectangle(10, 10, 20, 30);
		assertEquals(20, 25, rectangle.getCenter());
		assertEquals(10, 10, 20, 30, rectangle);
	}

	@Test
	public void testGetLeft() throws Exception {
		Rectangle rectangle = new Rectangle(10, 10, 20, 30);
		assertEquals(10, 25, rectangle.getLeft());
		assertEquals(10, 10, 20, 30, rectangle);
	}

	@Test
	public void testGetRight() throws Exception {
		Rectangle rectangle = new Rectangle(10, 10, 20, 30);
		assertEquals(30, 25, rectangle.getRight());
		assertEquals(10, 10, 20, 30, rectangle);
	}

	@Test
	public void testGetTop() throws Exception {
		Rectangle rectangle = new Rectangle(10, 10, 20, 30);
		assertEquals(20, 10, rectangle.getTop());
		assertEquals(10, 10, 20, 30, rectangle);
	}

	@Test
	public void testGetTopLeft() throws Exception {
		Rectangle rectangle = new Rectangle(10, 10, 20, 30);
		assertEquals(10, 10, rectangle.getTopLeft());
		assertEquals(10, 10, 20, 30, rectangle);
	}

	@Test
	public void testGetTopRight() throws Exception {
		Rectangle rectangle = new Rectangle(10, 10, 20, 30);
		assertEquals(30, 10, rectangle.getTopRight());
		assertEquals(10, 10, 20, 30, rectangle);
	}

	@Test
	public void testGetCropped() throws Exception {
		// check work getCropped() with null Insets
		Rectangle template = new Rectangle(10, 15, 70, 30);
		Rectangle testRectangle = template.getCropped(null);
		assertNotSame(template, testRectangle);
		assertEquals(10, 15, 70, 30, template);
		assertEquals(10, 15, 70, 30, testRectangle);
		//
		// check work getCropped() with Insets
		Insets insets = new Insets(1, 2, 3, 4);
		testRectangle = template.getCropped(insets);
		assertNotSame(template, testRectangle);
//	    assertEquals(12, 16, 64, 26, testRectangle);
		assertEquals(10, 15, 70, 30, template);
		assertEquals(1, 2, 3, 4, insets);
	}

	@Test
	public void testGetExpandedIntInt() throws Exception {
		Rectangle template = new Rectangle(1, 2, 3, 4);
		Rectangle testRectangle = template.getExpanded(2, 3);
		assertNotSame(template, testRectangle);
		assertEquals(1, 2, 3, 4, template);
		assertEquals(-1, -1, 7, 10, testRectangle);
	}

	@Test
	public void testGetExpandedInsets() throws Exception {
		Rectangle template = new Rectangle(1, 2, 3, 4);
		Insets insets = new Insets(5, 4, 3, 2);
		Rectangle testRectangle = template.getExpanded(insets);
		assertNotSame(template, testRectangle);
		assertEquals(1, 2, 3, 4, template);
		assertEquals(-3, -3, 9, 12, testRectangle);
		assertEquals(5, 4, 3, 2, insets);
	}

	@Test
	public void testGetIntersection() throws Exception {
		Rectangle rectangle1 = new Rectangle(10, 15, 70, 30);
		//
		// check work getIntersection(Rectangle) with itself
		Rectangle testRectangle = rectangle1.getIntersection(rectangle1);
		assertNotSame(rectangle1, testRectangle);
		assertEquals(10, 15, 70, 30, testRectangle);
		assertEquals(10, 15, 70, 30, rectangle1);
		//
		// check work getIntersection(Rectangle)
		Rectangle rectangle2 = new Rectangle(0, 30, 50, 40);
		testRectangle = rectangle1.getIntersection(rectangle2);
		assertNotSame(rectangle1, testRectangle);
		assertNotSame(rectangle2, testRectangle);
		assertEquals(10, 30, 40, 15, testRectangle);
		assertEquals(10, 15, 70, 30, rectangle1);
		assertEquals(0, 30, 50, 40, rectangle2);
		//
		// check work getIntersection(Rectangle)
		rectangle1 = new Rectangle(10, 15, 70, 30);
		testRectangle = rectangle1.getIntersection(new Rectangle(0, 30, 100, 10));
		assertEquals(10, 30, 70, 10, testRectangle);
		assertEquals(10, 15, 70, 30, rectangle1);
		//
		// check work getIntersection(Rectangle)
		rectangle1 = new Rectangle(10, 15, 70, 30);
		testRectangle = rectangle1.getIntersection(new Rectangle(-100, -100, 10, 10));
		assertTrue(testRectangle.isEmpty());
		assertEquals(10, 15, 70, 30, rectangle1);
		//
		// check work getIntersection(Rectangle)
		rectangle1 = new Rectangle(10, 15, 70, 30);
		testRectangle = rectangle1.getIntersection(new Rectangle(0, 0, 5, 10));
		assertTrue(testRectangle.isEmpty());
		assertEquals(10, 15, 70, 30, rectangle1);
	}

	@Test
	public void testGetResized() throws Exception {
		Rectangle template = new Rectangle(1, 2, 3, 4);
		//
		// check work getResized(int, int)
		Rectangle testRectangle = template.getResized(7, 8);
		assertNotNull(testRectangle);
		assertNotSame(template, testRectangle);
		assertEquals(1, 2, 10, 12, testRectangle);
		assertEquals(1, 2, 3, 4, template);
		//
		// check work getResized(Dimension)
		Dimension size = new Dimension(11, 12);
		testRectangle = template.getResized(size);
		assertNotNull(testRectangle);
		assertNotSame(template, testRectangle);
		assertEquals(1, 2, 14, 16, testRectangle);
		assertEquals(1, 2, 3, 4, template);
		assertEquals(11, 12, size);
	}

	@Test
	public void testGetTransposed() throws Exception {
		Rectangle template = new Rectangle(1, 2, 3, 4);
		Rectangle testRectangle = template.getTransposed();
		assertNotNull(testRectangle);
		assertNotSame(template, testRectangle);
		assertEquals(2, 1, 4, 3, testRectangle);
		assertEquals(1, 2, 3, 4, template);
	}

	@Test
	public void testGetUnion() throws Exception {
		Rectangle template = new Rectangle(10, 20, 30, 40);
		//
		// check work getUnion() with itself
		Rectangle testRectangle = template.getUnion(template);
		assertNotNull(testRectangle);
		assertNotSame(template, testRectangle);
		assertEquals(10, 20, 30, 40, testRectangle);
		assertEquals(10, 20, 30, 40, template);
		//
		// check work getUnion() with null
		testRectangle = template.getUnion((Rectangle) null);
		assertNotNull(testRectangle);
		assertNotSame(template, testRectangle);
		assertEquals(10, 20, 30, 40, testRectangle);
		assertEquals(10, 20, 30, 40, template);
		//
		// check work getUnion() with empty rectangle
		testRectangle = template.getUnion(new Rectangle());
		assertNotNull(testRectangle);
		assertNotSame(template, testRectangle);
		assertEquals(10, 20, 30, 40, testRectangle);
		assertEquals(10, 20, 30, 40, template);
		//
		// check work getUnion() with rectangle
		Rectangle rectangle = new Rectangle(5, 7, 10, 10);
		testRectangle = template.getUnion(rectangle);
		assertNotNull(testRectangle);
		assertNotSame(template, testRectangle);
		assertNotSame(rectangle, testRectangle);
		assertEquals(5, 7, 35, 53, testRectangle);
		assertEquals(10, 20, 30, 40, template);
		assertEquals(5, 7, 10, 10, rectangle);
		//
		// check work getUnion() with rectangle
		template = new Rectangle(5, 7, 35, 53);
		rectangle = new Rectangle(10, 20, 100, 200);
		testRectangle = template.getUnion(rectangle);
		assertNotNull(testRectangle);
		assertNotSame(template, testRectangle);
		assertNotSame(rectangle, testRectangle);
		assertEquals(5, 7, 105, 213, testRectangle);
		assertEquals(5, 7, 35, 53, template);
		assertEquals(10, 20, 100, 200, rectangle);
	}
}
