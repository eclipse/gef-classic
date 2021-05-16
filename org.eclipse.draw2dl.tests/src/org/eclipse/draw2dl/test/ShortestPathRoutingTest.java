/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.PointList;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.draw2dl.graph.Path;
import org.eclipse.draw2dl.graph.ShortestPathRouter;

public class ShortestPathRoutingTest extends TestCase {

	private static final org.eclipse.draw2dl.geometry.Point bend = new org.eclipse.draw2dl.geometry.Point(620, 309);
	private static final org.eclipse.draw2dl.geometry.Point bendAEnd = new org.eclipse.draw2dl.geometry.Point(264, 472);
	private static final org.eclipse.draw2dl.geometry.Point bendAStart = new org.eclipse.draw2dl.geometry.Point(283, 98);
	private static final org.eclipse.draw2dl.geometry.Rectangle bendBottomRect = new org.eclipse.draw2dl.geometry.Rectangle(426, 335, 92,
			167);
	private static final org.eclipse.draw2dl.geometry.Rectangle bendRightRect = new org.eclipse.draw2dl.geometry.Rectangle(588, 192, 101,
			176);
	private static final org.eclipse.draw2dl.geometry.Rectangle bendTopRect = new org.eclipse.draw2dl.geometry.Rectangle(428, 59, 99, 205);

	private static final org.eclipse.draw2dl.geometry.Point blockAEnd = new org.eclipse.draw2dl.geometry.Point(696, 279);
	private static final org.eclipse.draw2dl.geometry.Point blockAStart = new org.eclipse.draw2dl.geometry.Point(181, 306);
	private static final org.eclipse.draw2dl.geometry.Rectangle blockRect1 = new org.eclipse.draw2dl.geometry.Rectangle(45, 163, 102, 373);
	private static final org.eclipse.draw2dl.geometry.Rectangle blockRect2 = new org.eclipse.draw2dl.geometry.Rectangle(21, 403, 412, 109);
	private static final org.eclipse.draw2dl.geometry.Rectangle blockRect3 = new org.eclipse.draw2dl.geometry.Rectangle(229, 214, 134,
			259);
	private static final org.eclipse.draw2dl.geometry.Rectangle blockRect4 = new org.eclipse.draw2dl.geometry.Rectangle(73, 174, 352, 96);

	private static final org.eclipse.draw2dl.geometry.Point corner2AEnd = new org.eclipse.draw2dl.geometry.Point(865, 600);
	private static final org.eclipse.draw2dl.geometry.Point corner2AEndCross = new org.eclipse.draw2dl.geometry.Point(829, 500);
	private static final org.eclipse.draw2dl.geometry.Point corner2AStart = new org.eclipse.draw2dl.geometry.Point(212, 193);
	private static final org.eclipse.draw2dl.geometry.Point corner2BEnd = new org.eclipse.draw2dl.geometry.Point(802, 585);
	private static final org.eclipse.draw2dl.geometry.Point corner2BStart = new org.eclipse.draw2dl.geometry.Point(276, 238);
	private static final org.eclipse.draw2dl.geometry.Point corner2CEnd = new org.eclipse.draw2dl.geometry.Point(749, 305);
	private static final org.eclipse.draw2dl.geometry.Point corner2CStart = new org.eclipse.draw2dl.geometry.Point(305, 291);

	private static final org.eclipse.draw2dl.geometry.Rectangle bl = new org.eclipse.draw2dl.geometry.Rectangle(374, 78, 189, 396);
	private static final org.eclipse.draw2dl.geometry.Rectangle br = new org.eclipse.draw2dl.geometry.Rectangle(200, 132, 309, 263);
	private static final org.eclipse.draw2dl.geometry.Rectangle tl = new org.eclipse.draw2dl.geometry.Rectangle(340, 257, 309, 263);
	private static final org.eclipse.draw2dl.geometry.Rectangle tr = new org.eclipse.draw2dl.geometry.Rectangle(426, 255, 275, 436);

	private static final org.eclipse.draw2dl.geometry.Point cornerAEnd = new org.eclipse.draw2dl.geometry.Point(605, 180);
	private static final org.eclipse.draw2dl.geometry.Point cornerAStart = new org.eclipse.draw2dl.geometry.Point(179, 468);
	private static final org.eclipse.draw2dl.geometry.Point cornerBEnd = new org.eclipse.draw2dl.geometry.Point(655, 214);
	private static final org.eclipse.draw2dl.geometry.Point cornerBStart = new org.eclipse.draw2dl.geometry.Point(261, 463);

	private static final org.eclipse.draw2dl.geometry.Rectangle deformedLeft = new org.eclipse.draw2dl.geometry.Rectangle(201, 151, 89,
			127);
	private static final org.eclipse.draw2dl.geometry.Rectangle deformedLeftMid = new org.eclipse.draw2dl.geometry.Rectangle(302, 151,
			72, 118);
	private static final org.eclipse.draw2dl.geometry.Rectangle deformedRightMid = new org.eclipse.draw2dl.geometry.Rectangle(381, 151,
			79, 137);

	private static final org.eclipse.draw2dl.geometry.Point deltaAEnd = new org.eclipse.draw2dl.geometry.Point(547, 96);
	private static final org.eclipse.draw2dl.geometry.Point deltaAStart = new org.eclipse.draw2dl.geometry.Point(194, 400);
	private static final org.eclipse.draw2dl.geometry.Point deltaBEnd = new org.eclipse.draw2dl.geometry.Point(469, 195);
	private static final org.eclipse.draw2dl.geometry.Point deltaBStart = new org.eclipse.draw2dl.geometry.Point(262, 334);
	private static final org.eclipse.draw2dl.geometry.Rectangle deltaNewRect = new org.eclipse.draw2dl.geometry.Rectangle(25, 83, 102, 74);
	private static final org.eclipse.draw2dl.geometry.Rectangle deltaNewRectIntersec = new org.eclipse.draw2dl.geometry.Rectangle(224,
			225, 87, 43);
	private static final org.eclipse.draw2dl.geometry.Rectangle deltaNewRectNewBounds = new org.eclipse.draw2dl.geometry.Rectangle(83,
			225, 56, 66);
	private static final org.eclipse.draw2dl.geometry.Rectangle deltaRect = new org.eclipse.draw2dl.geometry.Rectangle(322, 220, 149, 249);
	private static final org.eclipse.draw2dl.geometry.Rectangle deltaRectNewBounds = new org.eclipse.draw2dl.geometry.Rectangle(439, 373,
			149, 249);

	private static final org.eclipse.draw2dl.geometry.Point offsetAEnd = new org.eclipse.draw2dl.geometry.Point(755, 614);
	private static final org.eclipse.draw2dl.geometry.Point offsetAStart = new org.eclipse.draw2dl.geometry.Point(163, 107);
	private static final org.eclipse.draw2dl.geometry.Point offsetBEnd = new org.eclipse.draw2dl.geometry.Point(675, 592);
	private static final org.eclipse.draw2dl.geometry.Point offsetBStart = new org.eclipse.draw2dl.geometry.Point(195, 145);
	private static final org.eclipse.draw2dl.geometry.Point offsetCEnd = new org.eclipse.draw2dl.geometry.Point(796, 458);
	private static final org.eclipse.draw2dl.geometry.Point offsetCStart = new org.eclipse.draw2dl.geometry.Point(529, 212);
	private static final org.eclipse.draw2dl.geometry.Point offsetDEnd = new org.eclipse.draw2dl.geometry.Point(803, 419);
	private static final org.eclipse.draw2dl.geometry.Point offsetDStart = new org.eclipse.draw2dl.geometry.Point(571, 241);
	private static final org.eclipse.draw2dl.geometry.Rectangle offsetRectBottom = new org.eclipse.draw2dl.geometry.Rectangle(539, 345,
			88, 161);
	private static final org.eclipse.draw2dl.geometry.Rectangle offsetRectLeft = new org.eclipse.draw2dl.geometry.Rectangle(170, 168,
			271, 306);
	private static final org.eclipse.draw2dl.geometry.Rectangle offsetRectRight = new org.eclipse.draw2dl.geometry.Rectangle(630, 199,
			178, 145);
	private static final org.eclipse.draw2dl.geometry.Rectangle offsetRectTop = new org.eclipse.draw2dl.geometry.Rectangle(448, 102, 170,
			84);

	private static final org.eclipse.draw2dl.geometry.Point ovalAEnd = new org.eclipse.draw2dl.geometry.Point(717, 294);
	private static final org.eclipse.draw2dl.geometry.Point ovalAStart = new org.eclipse.draw2dl.geometry.Point(68, 289);
	private static final org.eclipse.draw2dl.geometry.Rectangle ovalRect1 = new org.eclipse.draw2dl.geometry.Rectangle(191, 206, 59, 146);
	private static final org.eclipse.draw2dl.geometry.Rectangle ovalRect2 = new org.eclipse.draw2dl.geometry.Rectangle(303, 203, 62, 139);
	private static final org.eclipse.draw2dl.geometry.Rectangle ovalRect3 = new org.eclipse.draw2dl.geometry.Rectangle(381, 333, 92, 103);
	private static final org.eclipse.draw2dl.geometry.Rectangle ovalRect4 = new org.eclipse.draw2dl.geometry.Rectangle(409, 136, 83, 144);
	private static final org.eclipse.draw2dl.geometry.Rectangle ovalRect5 = new org.eclipse.draw2dl.geometry.Rectangle(535, 221, 57, 174);

	private static final org.eclipse.draw2dl.geometry.Point quadBendAEnd = new org.eclipse.draw2dl.geometry.Point(701, 368);
	private static final org.eclipse.draw2dl.geometry.Point quadBendAStart = new org.eclipse.draw2dl.geometry.Point(249, 33);
	private static final org.eclipse.draw2dl.geometry.Point quadBendBEnd = new org.eclipse.draw2dl.geometry.Point(708, 397);
	private static final org.eclipse.draw2dl.geometry.Rectangle quadBendBottom = new org.eclipse.draw2dl.geometry.Rectangle(410, 294,
			432, 24);
	private static final org.eclipse.draw2dl.geometry.Point quadBendBStart = new org.eclipse.draw2dl.geometry.Point(309, 64);
	private static final org.eclipse.draw2dl.geometry.Point quadBendCEnd = new org.eclipse.draw2dl.geometry.Point(725, 421);
	private static final org.eclipse.draw2dl.geometry.Point quadBendCStart = new org.eclipse.draw2dl.geometry.Point(291, 82);
	private static final org.eclipse.draw2dl.geometry.Point quadBendDEnd = new org.eclipse.draw2dl.geometry.Point(730, 445);
	private static final org.eclipse.draw2dl.geometry.Point quadBendDStart = new org.eclipse.draw2dl.geometry.Point(255, 97);
	private static final org.eclipse.draw2dl.geometry.Rectangle quadBendMiddleHit = new org.eclipse.draw2dl.geometry.Rectangle(385, 209,
			49, 40);
	private static final org.eclipse.draw2dl.geometry.Rectangle quadBendMiddleHit2 = new org.eclipse.draw2dl.geometry.Rectangle(491, 210,
			60, 44);
	private static final org.eclipse.draw2dl.geometry.Rectangle quadBendMiddleMiss = new org.eclipse.draw2dl.geometry.Rectangle(410, 184,
			49, 40);
	private static final org.eclipse.draw2dl.geometry.Rectangle quadBendTop = new org.eclipse.draw2dl.geometry.Rectangle(84, 131, 416, 47);

	private static final org.eclipse.draw2dl.geometry.Point sideAEnd = new org.eclipse.draw2dl.geometry.Point(500, 100);
	private static final org.eclipse.draw2dl.geometry.Point sideAStart = new org.eclipse.draw2dl.geometry.Point(100, 100);
	private static final org.eclipse.draw2dl.geometry.Point sideBEnd = new org.eclipse.draw2dl.geometry.Point(500, 150);
	private static final org.eclipse.draw2dl.geometry.Rectangle sideBottom = new org.eclipse.draw2dl.geometry.Rectangle(150, 0, 50, 153);
	private static final org.eclipse.draw2dl.geometry.Point sideBStart = new org.eclipse.draw2dl.geometry.Point(100, 150);
	private static final org.eclipse.draw2dl.geometry.Rectangle sideTop = new org.eclipse.draw2dl.geometry.Rectangle(150, 25, 50, 450);

	private static final org.eclipse.draw2dl.geometry.Point subpathAEnd = new org.eclipse.draw2dl.geometry.Point(283, 237);
	private static final org.eclipse.draw2dl.geometry.Point subpathAStart = new org.eclipse.draw2dl.geometry.Point(746, 335);
	private static final org.eclipse.draw2dl.geometry.Point subpathBEnd = new org.eclipse.draw2dl.geometry.Point(426, 135);
	private static final org.eclipse.draw2dl.geometry.Point subpathBStart = new org.eclipse.draw2dl.geometry.Point(386, 357);
	private static final org.eclipse.draw2dl.geometry.Point subpathCEnd = new org.eclipse.draw2dl.geometry.Point(519, 231);
	private static final org.eclipse.draw2dl.geometry.Point subpathCStart = new org.eclipse.draw2dl.geometry.Point(308, 194);
	private static final org.eclipse.draw2dl.geometry.Point subpathDEnd = new org.eclipse.draw2dl.geometry.Point(461, 150);
	private static final org.eclipse.draw2dl.geometry.Point subpathDStart = new org.eclipse.draw2dl.geometry.Point(666, 385);
	private static final org.eclipse.draw2dl.geometry.Rectangle subpathLeftRect = new org.eclipse.draw2dl.geometry.Rectangle(328, 174,
			187, 138);
	private static final org.eclipse.draw2dl.geometry.Rectangle subpathRightRect = new org.eclipse.draw2dl.geometry.Rectangle(560, 94,
			101, 308);

	private static final org.eclipse.draw2dl.geometry.Point tangentAEnd = new org.eclipse.draw2dl.geometry.Point(778, 185);
	private static final org.eclipse.draw2dl.geometry.Point tangentAStart = new org.eclipse.draw2dl.geometry.Point(73, 185);
	private static final org.eclipse.draw2dl.geometry.Point tangentBEnd = new org.eclipse.draw2dl.geometry.Point(781, 201);
	private static final org.eclipse.draw2dl.geometry.Point tangentBStart = new org.eclipse.draw2dl.geometry.Point(66, 203);
	private static final org.eclipse.draw2dl.geometry.Point tangentCEnd = new org.eclipse.draw2dl.geometry.Point(459, 180);
	private static final org.eclipse.draw2dl.geometry.Point tangentCStart = new org.eclipse.draw2dl.geometry.Point(364, 208);
	private static final org.eclipse.draw2dl.geometry.Rectangle tangentLeft = new org.eclipse.draw2dl.geometry.Rectangle(156, 151, 200,
			168);
	private static final org.eclipse.draw2dl.geometry.Rectangle tangentMiddle = new org.eclipse.draw2dl.geometry.Rectangle(383, 151, 60,
			95);
	private static final org.eclipse.draw2dl.geometry.Rectangle tangentRight = new org.eclipse.draw2dl.geometry.Rectangle(473, 151, 251,
			138);

	// working vars
	private org.eclipse.draw2dl.geometry.PointList pathA, pathB, pathC, pathD;
	private org.eclipse.draw2dl.geometry.Rectangle rect;

	private org.eclipse.draw2dl.graph.ShortestPathRouter routing;

	private void doAssertAbove(org.eclipse.draw2dl.geometry.Point pt1, org.eclipse.draw2dl.geometry.Point pt2, org.eclipse.draw2dl.geometry.Rectangle r) {
		assertTrue("Both points " + pt1 + " " + pt2
				+ " should bend above the rectangle. " + r, pt1.y < r.y
				&& pt2.y < r.y);
	}

	private void doAssertBelow(org.eclipse.draw2dl.geometry.Point pt1, org.eclipse.draw2dl.geometry.Point pt2, org.eclipse.draw2dl.geometry.Rectangle r) {
		assertTrue("Both points " + pt1 + " " + pt2
				+ " should bend below the rectangle. " + r, pt1.y > r.bottom()
				&& pt2.y > r.bottom());
	}

	private void doAssertLeft(org.eclipse.draw2dl.geometry.Point pt1, org.eclipse.draw2dl.geometry.Point pt2, org.eclipse.draw2dl.geometry.Rectangle r) {
		assertTrue("Both points " + pt1 + " " + pt2
				+ " should bend to the left of the rectangle. " + r,
				pt1.x < r.x && pt2.x < r.x);
	}

	private void doAssertNoPathsSolved() {
		// assertTrue("No paths should have been solved.", routing.solve() ==
		// 0);
	}

	private void doAssertNumPoints(org.eclipse.draw2dl.geometry.PointList path, int expectedBends) {
		assertTrue(
				"Path should have " + expectedBends + " but had "
						+ (path.size() - 2), expectedBends == path.size() - 2);
	}

	private void doAssertOffsetShrink(org.eclipse.draw2dl.geometry.Point pt1, org.eclipse.draw2dl.geometry.Point pt2) {
		assertTrue("Point : " + pt1 + " and " + pt2
				+ " should be separated by less than the normal offset: " + 4,
				Math.abs(pt2.x - pt1.x) < 4 && Math.abs(pt2.y - pt1.y) < 4);
	}

	private void doAssertPathAbove(org.eclipse.draw2dl.geometry.PointList pathA, org.eclipse.draw2dl.geometry.PointList pathB) {
		assertEquals("Paths should have same number of bends " + pathA.size()
				+ " " + pathB.size(), pathA.size(), pathB.size());

		for (int i = 1; i < pathA.size() - 1; i++) {
			org.eclipse.draw2dl.geometry.Point a = pathA.getPoint(i);
			org.eclipse.draw2dl.geometry.Point b = pathB.getPoint(i);
			assertTrue("Point " + a + " should have been above " + b, a.y < b.y);
		}
	}

	private void doAssertPathRight(org.eclipse.draw2dl.geometry.PointList pathA, org.eclipse.draw2dl.geometry.PointList pathB) {
		assertEquals("Paths should have same number of bends " + pathA.size()
				+ " " + pathB.size(), pathA.size(), pathB.size());

		for (int i = 1; i < pathA.size() - 1; i++) {
			org.eclipse.draw2dl.geometry.Point a = pathA.getPoint(i);
			org.eclipse.draw2dl.geometry.Point b = pathB.getPoint(i);
			assertTrue("Point " + a + " should have been right of " + b,
					a.x > b.x);
		}
	}

	private void doAssertPointAbove(org.eclipse.draw2dl.geometry.Point above, org.eclipse.draw2dl.geometry.Point point) {
		assertTrue("Point : " + above + " should be below " + point,
				above.y < point.y);
	}

	private void doAssertPointBelow(org.eclipse.draw2dl.geometry.Point below, org.eclipse.draw2dl.geometry.Point point) {
		assertTrue("Point : " + below + " should be below " + point,
				below.y > point.y);
	}

	private void doAssertPointLeft(org.eclipse.draw2dl.geometry.Point left, org.eclipse.draw2dl.geometry.Point point) {
		assertTrue("Point : " + left + " should be left of " + point,
				left.x < point.x);
	}

	private void doAssertPointRight(org.eclipse.draw2dl.geometry.Point right, org.eclipse.draw2dl.geometry.Point point) {
		assertTrue("Point : " + right + " should be right of " + point,
				right.x > point.x);
	}

	private void doAssertRight(org.eclipse.draw2dl.geometry.Point pt1, org.eclipse.draw2dl.geometry.Point pt2, org.eclipse.draw2dl.geometry.Rectangle r) {
		assertTrue("Both points " + pt1 + " " + pt2
				+ " should bend to the right of the rectangle. " + r,
				pt1.x > r.right() && pt2.x > r.right());
	}

	private void doSetUp(org.eclipse.draw2dl.geometry.Point aStartStatic, org.eclipse.draw2dl.geometry.Point aEndStatic,
                         org.eclipse.draw2dl.geometry.Point bStartStatic, org.eclipse.draw2dl.geometry.Point bEndStatic, org.eclipse.draw2dl.geometry.Point cStartStatic,
                         org.eclipse.draw2dl.geometry.Point cEndStatic, org.eclipse.draw2dl.geometry.Point dStartStatic, org.eclipse.draw2dl.geometry.Point dEndStatic,
                         org.eclipse.draw2dl.geometry.Rectangle rectStatic) {
		org.eclipse.draw2dl.graph.Path d = new org.eclipse.draw2dl.graph.Path(dStartStatic.getCopy(), dEndStatic.getCopy());

		routing.addPath(d);

		doSetUp(aStartStatic, aEndStatic, bStartStatic, bEndStatic,
				cStartStatic, cEndStatic, rectStatic);

		pathD = d.getPoints();
	}

	private void doSetUp(org.eclipse.draw2dl.geometry.Point aStartStatic, org.eclipse.draw2dl.geometry.Point aEndStatic,
                         org.eclipse.draw2dl.geometry.Point bStartStatic, org.eclipse.draw2dl.geometry.Point bEndStatic, org.eclipse.draw2dl.geometry.Point cStartStatic,
                         org.eclipse.draw2dl.geometry.Point cEndStatic, org.eclipse.draw2dl.geometry.Rectangle rectStatic) {
		org.eclipse.draw2dl.graph.Path c = new org.eclipse.draw2dl.graph.Path(cStartStatic.getCopy(), cEndStatic.getCopy());

		routing.addPath(c);

		doSetUp(aStartStatic, aEndStatic, bStartStatic, bEndStatic, rectStatic);

		pathC = c.getPoints();
	}

	private void doSetUp(org.eclipse.draw2dl.geometry.Point aStartStatic, org.eclipse.draw2dl.geometry.Point aEndStatic,
                         org.eclipse.draw2dl.geometry.Point bStartStatic, org.eclipse.draw2dl.geometry.Point bEndStatic, org.eclipse.draw2dl.geometry.Rectangle rectStatic) {
		org.eclipse.draw2dl.graph.Path b = new org.eclipse.draw2dl.graph.Path(bStartStatic.getCopy(), bEndStatic.getCopy());

		routing.addPath(b);

		doSetUp(aStartStatic, aEndStatic, rectStatic);

		pathB = b.getPoints();
	}

	private void doSetUp(org.eclipse.draw2dl.geometry.Point aStartStatic, Point aEndStatic,
                         Rectangle rectStatic) {
		rect = rectStatic.getCopy();

		org.eclipse.draw2dl.graph.Path a = new org.eclipse.draw2dl.graph.Path(aStartStatic.getCopy(), aEndStatic.getCopy());

		routing.addObstacle(rect);

		routing.addPath(a);

		routing.solve();

		pathA = a.getPoints();
	}

	private void doTestBottomLeftIntersection() {
		doAssertBelow(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertLeft(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertPointBelow(pathA.getPoint(1), pathB.getPoint(1));
		doAssertPointBelow(pathB.getPoint(1), pathC.getPoint(1));
		doAssertPointLeft(pathA.getPoint(1), pathB.getPoint(1));
		doAssertPointLeft(pathB.getPoint(1), pathC.getPoint(1));
	}

	private void doTestBottomRightIntersection() {
		doAssertBelow(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertRight(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertPointBelow(pathB.getPoint(1), pathA.getPoint(1));
		doAssertPointRight(pathB.getPoint(1), pathA.getPoint(1));
	}

	private void doTestDeformed() {
		doAssertNumPoints(pathA, 8);
		doAssertNumPoints(pathB, 8);

		doAssertPathAbove(pathA, pathB);
	}

	private void doTestOffsetShrink() {
		doAssertNumPoints(pathA, 3);
		doAssertNumPoints(pathB, 3);
		doAssertNumPoints(pathC, 1);
		doAssertNumPoints(pathD, 1);

		doAssertOffsetShrink(pathA.getPoint(1), pathB.getPoint(1));
		doAssertOffsetShrink(pathA.getPoint(2), pathB.getPoint(2));
		doAssertOffsetShrink(pathA.getPoint(3), pathB.getPoint(3));
		doAssertOffsetShrink(pathC.getPoint(1), pathD.getPoint(1));
	}

	private void doTestQuadBendMiss(int numPoints) {
		doAssertNumPoints(pathA, numPoints);
		doAssertNumPoints(pathB, numPoints);
		doAssertNumPoints(pathC, numPoints);
		doAssertNumPoints(pathD, numPoints);

		doAssertAbove(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertAbove(pathC.getPoint(1), pathD.getPoint(1), rect);
		doAssertRight(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertRight(pathC.getPoint(1), pathD.getPoint(1), rect);

		doAssertBelow(pathA.getPoint(2), pathB.getPoint(2), rect);
		doAssertBelow(pathC.getPoint(2), pathD.getPoint(2), rect);
		doAssertRight(pathA.getPoint(2), pathB.getPoint(2), rect);
		doAssertRight(pathC.getPoint(2), pathD.getPoint(2), rect);

		doAssertAbove(pathA.getPoint(numPoints - 1),
				pathB.getPoint(numPoints - 1), quadBendBottom);
		doAssertAbove(pathC.getPoint(numPoints - 1),
				pathD.getPoint(numPoints - 1), quadBendBottom);
		doAssertLeft(pathA.getPoint(numPoints - 1),
				pathB.getPoint(numPoints - 1), quadBendBottom);
		doAssertLeft(pathC.getPoint(numPoints - 1),
				pathD.getPoint(numPoints - 1), quadBendBottom);

		doAssertBelow(pathA.getPoint(numPoints), pathB.getPoint(numPoints),
				quadBendBottom);
		doAssertBelow(pathC.getPoint(numPoints), pathD.getPoint(numPoints),
				quadBendBottom);
		doAssertLeft(pathA.getPoint(numPoints), pathB.getPoint(numPoints),
				quadBendBottom);
		doAssertLeft(pathC.getPoint(numPoints), pathD.getPoint(numPoints),
				quadBendBottom);

		doAssertPathRight(pathA, pathB);
		doAssertPathRight(pathB, pathC);
		doAssertPathRight(pathC, pathD);

		doAssertPointAbove(pathD.getPoint(3), pathC.getPoint(3));
		doAssertPointAbove(pathC.getPoint(3), pathB.getPoint(3));
		doAssertPointAbove(pathB.getPoint(3), pathA.getPoint(3));
	}

	private void doTestSideIntersectionBottom() {
		doAssertBelow(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertBelow(pathA.getPoint(2), pathB.getPoint(2), rect);
		doAssertPointBelow(pathB.getPoint(1), pathA.getPoint(1));
		doAssertPointBelow(pathB.getPoint(2), pathA.getPoint(2));
	}

	private void doTestSideIntersectionTop() {
		doAssertAbove(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertAbove(pathA.getPoint(2), pathB.getPoint(2), rect);
		doAssertPointBelow(pathB.getPoint(1), pathA.getPoint(1));
		doAssertPointBelow(pathB.getPoint(2), pathA.getPoint(2));
	}

	private void doTestTangent() {
		doAssertNumPoints(pathA, 6);
		doAssertNumPoints(pathB, 6);
		doAssertNumPoints(pathC, 2);

		doAssertAbove(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertPointAbove(pathB.getPoint(3), pathC.getPoint(1));
		doAssertPointAbove(pathA.getPoint(3), pathC.getPoint(1));

		doAssertPathAbove(pathA, pathB);
	}

	private void doTestTopLeftIntersection() {
		doAssertAbove(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertLeft(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertPointAbove(pathA.getPoint(1), pathB.getPoint(1));
	}

	private void doTestTopLeftIntersectionCross() {
		doAssertAbove(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertLeft(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertPointBelow(pathA.getPoint(1), pathB.getPoint(1));
	}

	private void doTestTopRightIntersection() {
		doAssertAbove(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertRight(pathA.getPoint(1), pathB.getPoint(1), rect);
		doAssertPointRight(pathA.getPoint(1), pathB.getPoint(1));
		doAssertPointAbove(pathA.getPoint(1), pathB.getPoint(1));
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		routing = new ShortestPathRouter();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		routing = null;
		pathA = null;
		pathC = null;
		pathB = null;
		pathD = null;
		rect = null;
	}

	public void testBendpoints() {
		routing.addObstacle(bendTopRect.getCopy());
		routing.addObstacle(bendBottomRect.getCopy());
		routing.addObstacle(bendRightRect.getCopy());

		org.eclipse.draw2dl.graph.Path a = new org.eclipse.draw2dl.graph.Path(bendAStart.getCopy(), bendAEnd.getCopy());
		a.setBendPoints(new PointList(new int[] { bend.x, bend.y }));

		routing.addPath(a);

		assertTrue("Should have solved path", routing.solve().size() > 0);

		pathA = a.getPoints();

		doAssertNumPoints(pathA, 3);
	}

	public void testBlockedPath() {
		routing.addObstacle(blockRect1.getCopy());
		routing.addObstacle(blockRect2.getCopy());
		routing.addObstacle(blockRect3.getCopy());

		doSetUp(blockAStart, blockAEnd, blockRect4);

		doAssertNumPoints(pathA, 0);
	}

	public void testBottomLeftIntersection() {
		doSetUp(corner2AStart, corner2AEnd, corner2BStart, corner2BEnd,
				corner2CStart, corner2CEnd, bl);
		doTestBottomLeftIntersection();
	}

	public void testBottomLeftIntersectionCross() {
		doSetUp(corner2BStart, corner2BEnd, corner2AStart, corner2AEndCross,
				corner2CStart, corner2CEnd, bl);
		doTestBottomLeftIntersection();
	}

	public void testBottomLeftIntersectionCrossInverted() {
		doSetUp(corner2BStart, corner2BEnd, corner2AStart, corner2AEndCross,
				corner2CEnd, corner2CStart, bl);
		doTestBottomLeftIntersection();
	}

	public void testBottomLeftIntersectionInverted() {
		doSetUp(corner2AStart, corner2AEnd, corner2BEnd, corner2BStart,
				corner2CStart, corner2CEnd, bl);
		doTestBottomLeftIntersection();
	}

	public void testBottomRightIntersection() {
		doSetUp(cornerAStart, cornerAEnd, cornerBStart, cornerBEnd, br);
		doTestBottomRightIntersection();
	}

	public void testBottomRightIntersectionInverted() {
		doSetUp(cornerAStart, cornerAEnd, cornerBEnd, cornerBStart, br);
		doTestBottomRightIntersection();
	}

	public void testDeformed() {
		routing.addObstacle(deformedLeft.getCopy());
		routing.addObstacle(deformedLeftMid.getCopy());
		routing.addObstacle(deformedRightMid.getCopy());
		doSetUp(tangentAEnd, tangentAStart, tangentBStart, tangentBEnd,
				tangentRight);
		doTestDeformed();
	}

	public void testDeformedInverted() {
		routing.addObstacle(deformedLeft.getCopy());
		routing.addObstacle(deformedLeftMid.getCopy());
		routing.addObstacle(deformedRightMid.getCopy());
		doSetUp(tangentAEnd, tangentAStart, tangentBEnd, tangentBStart,
				tangentRight);
		doTestDeformed();
	}

	public void testDeltasAddObstacleIntersection() {
		doSetUp(deltaAStart, deltaAEnd, deltaBStart, deltaBEnd, deltaRect);

		routing.addObstacle(deltaNewRectIntersec.getCopy());

		assertTrue("Both paths should have been solved.", routing.solve()
				.size() == 2);
	}

	public void testDeltasAddObstacleNoIntersection() {
		doSetUp(deltaAStart, deltaAEnd, deltaBStart, deltaBEnd, deltaRect);

		routing.addObstacle(deltaNewRect.getCopy());

		doAssertNoPathsSolved();
	}

	public void testDeltasAddPath() {
		doSetUp(corner2AStart, corner2AEnd, corner2BStart, corner2BEnd, bl);

		// should be no change.
		doAssertNoPathsSolved();

		// only one path should have changed.
		org.eclipse.draw2dl.graph.Path c = new org.eclipse.draw2dl.graph.Path(corner2CStart.getCopy(), corner2CEnd.getCopy());
		routing.addPath(c);
		assertTrue("Only the new path should have been solved.", routing
				.solve().size() > 0);

		pathC = c.getPoints();
		// the new path, however, should have moved the other two paths.
		doAssertPointAbove(pathC.getPoint(1), pathA.getPoint(1));
		doAssertPointAbove(pathC.getPoint(1), pathB.getPoint(1));
	}

	public void testDeltasMoveObstacleIntersection() {
		doSetUp(deltaAStart, deltaAEnd, deltaBStart, deltaBEnd, deltaRect);

		routing.updateObstacle(deltaRect.getCopy(),
				deltaRectNewBounds.getCopy());

		assertTrue("Both paths should have been solved.", routing.solve()
				.size() > 0);
	}

	public void testDeltasMoveObstacleNoIntersection() {
		doSetUp(deltaAStart, deltaAEnd, deltaBStart, deltaBEnd, deltaRect);

		routing.addObstacle(deltaNewRect.getCopy());

		routing.updateObstacle(deltaNewRect.getCopy(),
				deltaNewRectNewBounds.getCopy());

		doAssertNoPathsSolved();
	}

	public void testDeltasRemoveObstacleIntersection() {
		doSetUp(deltaAStart, deltaAEnd, deltaBStart, deltaBEnd, deltaRect);

		routing.removeObstacle(deltaRect.getCopy());

		assertTrue("Both paths should have been solved.", routing.solve()
				.size() > 0);
	}

	public void testDeltasRemoveObstacleNoIntersection() {
		doSetUp(deltaAStart, deltaAEnd, deltaBStart, deltaBEnd, deltaRect);

		routing.addObstacle(deltaNewRect.getCopy());
		routing.removeObstacle(deltaNewRect.getCopy());

		doAssertNoPathsSolved();
	}

	public void testDeltasRemovePath() {
		doSetUp(corner2AStart, corner2AEnd, corner2BStart, corner2BEnd, bl);
		org.eclipse.draw2dl.graph.Path c = new Path(corner2CStart, corner2CEnd);
		routing.removePath(c);

		// should be no change.
		doAssertNoPathsSolved();
	}

	public void testOffsetShrink() {
		routing.addObstacle(offsetRectLeft.getCopy());
		routing.addObstacle(offsetRectRight.getCopy());
		routing.addObstacle(offsetRectBottom.getCopy());
		doSetUp(offsetAStart, offsetAEnd, offsetBStart, offsetBEnd,
				offsetCStart, offsetCEnd, offsetDStart, offsetDEnd,
				offsetRectTop);
		doTestOffsetShrink();
	}

	public void testQuadBendHit() {
		routing.addObstacle(quadBendBottom.getCopy());
		routing.addObstacle(quadBendMiddleHit.getCopy());
		doSetUp(quadBendAStart, quadBendAEnd, quadBendBStart, quadBendBEnd,
				quadBendCStart, quadBendCEnd, quadBendDStart, quadBendDEnd,
				quadBendTop);
		doTestQuadBendMiss(5);
	}

	public void testQuadBendHit2() {
		routing.addObstacle(quadBendBottom.getCopy());
		routing.addObstacle(quadBendMiddleHit2.getCopy());
		doSetUp(quadBendAStart, quadBendAEnd, quadBendBStart, quadBendBEnd,
				quadBendCStart, quadBendCEnd, quadBendDStart, quadBendDEnd,
				quadBendTop);
		doTestQuadBendMiss(5);
	}

	public void testQuadBendMiss() {
		routing.addObstacle(quadBendBottom.getCopy());
		routing.addObstacle(quadBendMiddleMiss.getCopy());
		doSetUp(quadBendAStart, quadBendAEnd, quadBendBStart, quadBendBEnd,
				quadBendCStart, quadBendCEnd, quadBendDStart, quadBendDEnd,
				quadBendTop);
		doTestQuadBendMiss(4);
	}

	public void testShortestPathOutsideOval() {
		routing.addObstacle(ovalRect1.getCopy());
		routing.addObstacle(ovalRect2.getCopy());
		routing.addObstacle(ovalRect3.getCopy());
		routing.addObstacle(ovalRect4.getCopy());
		doSetUp(ovalAStart, ovalAEnd, ovalRect5);

		doAssertNumPoints(pathA, 4);
	}

	public void testSideIntersectionBottom() {
		doSetUp(sideAStart, sideAEnd, sideBStart, sideBEnd, sideBottom);
		doTestSideIntersectionBottom();
	}

	public void testSideIntersectionBottomInverted() {
		doSetUp(sideAStart, sideAEnd, sideBEnd, sideBStart, sideBottom);
		doTestSideIntersectionBottom();
	}

	public void testSideIntersectionTop() {
		doSetUp(sideAStart, sideAEnd, sideBStart, sideBEnd, sideTop);
		doTestSideIntersectionTop();
	}

	public void testSideIntersectionTopInverted() {
		doSetUp(sideAStart, sideAEnd, sideBEnd, sideBStart, sideTop);
		doTestSideIntersectionTop();
	}

	public void testSubpath() {
		routing.addObstacle(subpathLeftRect.getCopy());
		doSetUp(subpathAStart, subpathAEnd, subpathBStart, subpathBEnd,
				subpathCStart, subpathCEnd, subpathDStart, subpathDEnd,
				subpathRightRect);

		doAssertNumPoints(pathD, 3);

		doAssertPointAbove(pathD.getPoint(1), pathA.getPoint(1));
		doAssertPointAbove(pathD.getPoint(2), pathA.getPoint(2));
		doAssertPointAbove(pathD.getPoint(3), pathC.getPoint(2));
	}

	public void testTangent() {
		routing.addObstacle(tangentMiddle.getCopy());
		routing.addObstacle(tangentRight.getCopy());
		doSetUp(tangentAStart, tangentAEnd, tangentBStart, tangentBEnd,
				tangentCStart, tangentCEnd, tangentLeft);
		doTestTangent();
	}

	public void testTangentInverted() {
		routing.addObstacle(tangentMiddle.getCopy());
		routing.addObstacle(tangentRight.getCopy());
		doSetUp(tangentAEnd, tangentAStart, tangentBStart, tangentBEnd,
				tangentCEnd, tangentCStart, tangentLeft);
		doTestTangent();
	}

	public void testTopLeftIntersection() {
		doSetUp(cornerAStart, cornerAEnd, cornerBStart, cornerBEnd, tl);
		doTestTopLeftIntersection();
	}

	public void testTopLeftIntersectionCross() {
		doSetUp(cornerBStart, cornerAEnd, cornerAStart, cornerBEnd, tl);
		doTestTopLeftIntersectionCross();
	}

	public void testTopLeftIntersectionCrossInverted() {
		doSetUp(cornerBStart, cornerAEnd, cornerBEnd, cornerAStart, tl);
		doTestTopLeftIntersectionCross();
	}

	public void testTopLeftIntersectionInverted() {
		doSetUp(cornerAStart, cornerAEnd, cornerBEnd, cornerBStart, tl);
		doTestTopLeftIntersection();
	}

	public void testTopRightIntersection() {
		doSetUp(corner2AStart, corner2AEnd, corner2BStart, corner2BEnd, tr);
		doTestTopRightIntersection();
	}

	public void testTopRightIntersectionInverted() {
		doSetUp(corner2AStart, corner2AEnd, corner2BEnd, corner2BStart, tr);
		doTestTopRightIntersection();
	}

}
