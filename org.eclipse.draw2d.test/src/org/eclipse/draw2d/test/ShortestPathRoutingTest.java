/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import java.util.Collections;

import junit.framework.TestCase;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.graph.*;

public class ShortestPathRoutingTest extends TestCase {
	
private static final Point bend = new Point(620, 309);
private static final Point bendAEnd = new Point(264, 472);
private static final Point bendAStart = new Point(283, 98);
private static final Rectangle bendBottomRect = new Rectangle(426, 335, 92, 167);
private static final Rectangle bendRightRect = new Rectangle(588, 192, 101, 176);
private static final Rectangle bendTopRect = new Rectangle(428, 59, 99, 205);

private static final Point blockAEnd = new Point(696, 279);
private static final Point blockAStart = new Point(181, 306);
private static final Rectangle blockRect1 = new Rectangle(45, 163, 102, 373);
private static final Rectangle blockRect2 = new Rectangle(21, 403, 412, 109);
private static final Rectangle blockRect3 = new Rectangle(229, 214, 134, 259);
private static final Rectangle blockRect4 = new Rectangle(73, 174, 352, 96);

private static final Point corner2AEnd = new Point(865, 600);
private static final Point corner2AEndCross = new Point(829, 500);
private static final Point corner2AStart = new Point(212, 193);
private static final Point corner2BEnd = new Point(802, 585);
private static final Point corner2BStart = new Point(276, 238);
private static final Point corner2CEnd = new Point(749, 305);
private static final Point corner2CStart = new Point(305, 291);
	
private static final Rectangle bl = new Rectangle(374, 78, 189, 396);
private static final Rectangle br = new Rectangle(200, 132, 309, 263);
private static final Rectangle tl = new Rectangle(340, 257, 309, 263);
private static final Rectangle tr = new Rectangle(426, 255, 275, 436);

private static final Point cornerAEnd = new Point(605, 180);
private static final Point cornerAStart = new Point(179, 468);
private static final Point cornerBEnd = new Point(655, 214);
private static final Point cornerBStart = new Point(261, 463);

private static final Rectangle deformedLeft = new Rectangle(201, 151, 89, 127);
private static final Rectangle deformedLeftMid = new Rectangle(302, 151, 72, 118);
private static final Rectangle deformedRightMid = new Rectangle(381, 151, 79, 137);

private static final Point deltaAEnd = new Point(547, 96);
private static final Point deltaAStart = new Point(194, 400);
private static final Point deltaBEnd = new Point(469, 195);
private static final Point deltaBStart = new Point(262, 334);
private static final Rectangle deltaNewRect = new Rectangle(25, 83, 102, 74);
private static final Rectangle deltaNewRectIntersec = new Rectangle(224, 225, 87, 43);
private static final Rectangle deltaNewRectNewBounds = new Rectangle(83, 225, 56, 66);
private static final Rectangle deltaRect = new Rectangle(322, 220, 149, 249);
private static final Rectangle deltaRectNewBounds = new Rectangle(439, 373, 149, 249);

private static final Point offsetAEnd = new Point(755, 614);
private static final Point offsetAStart = new Point(163, 107);
private static final Point offsetBEnd = new Point(675, 592);
private static final Point offsetBStart = new Point(195, 145);
private static final Point offsetCEnd = new Point(796, 458);
private static final Point offsetCStart = new Point(529, 212);
private static final Point offsetDEnd = new Point(803, 419);
private static final Point offsetDStart = new Point(571, 241);
private static final Rectangle offsetRectBottom = new Rectangle(539, 345, 88, 161);
private static final Rectangle offsetRectLeft = new Rectangle(170, 168, 271, 306);
private static final Rectangle offsetRectRight = new Rectangle(630, 199, 178, 145);
private static final Rectangle offsetRectTop = new Rectangle(448, 102, 170, 84);

private static final Point ovalAEnd = new Point(717, 294);
private static final Point ovalAStart = new Point(68, 289);
private static final Rectangle ovalRect1 = new Rectangle(191, 206, 59, 146);
private static final Rectangle ovalRect2 = new Rectangle(303, 203, 62, 139);
private static final Rectangle ovalRect3 = new Rectangle(381, 333, 92, 103);
private static final Rectangle ovalRect4 = new Rectangle(409, 136, 83, 144);
private static final Rectangle ovalRect5 = new Rectangle(535, 221, 57, 174);

private static final Point quadBendAEnd = new Point(701, 368);
private static final Point quadBendAStart = new Point(249, 33);
private static final Point quadBendBEnd = new Point(708, 397);
private static final Rectangle quadBendBottom = new Rectangle(410, 294, 432, 24);
private static final Point quadBendBStart = new Point(309, 64);
private static final Point quadBendCEnd = new Point(725, 421);
private static final Point quadBendCStart = new Point(291, 82);
private static final Point quadBendDEnd = new Point(730, 445);
private static final Point quadBendDStart = new Point(255, 97);
private static final Rectangle quadBendMiddleHit = new Rectangle(385, 209, 49, 40);
private static final Rectangle quadBendMiddleHit2 = new Rectangle(491, 210, 60, 44);
private static final Rectangle quadBendMiddleMiss = new Rectangle(410, 184, 49, 40);
private static final Rectangle quadBendTop = new Rectangle(84, 131, 416, 47);

private static final Point sideAEnd = new Point(500, 100);
private static final Point sideAStart = new Point(100, 100);
private static final Point sideBEnd = new Point(500, 150);
private static final Rectangle sideBottom = new Rectangle(150, 0, 50, 153);
private static final Point sideBStart = new Point(100, 150);
private static final Rectangle sideTop = new Rectangle(150, 25, 50, 450);

private static final Point subpathAEnd = new Point(283, 237);
private static final Point subpathAStart = new Point(746, 335);
private static final Point subpathBEnd  = new Point(426, 135);
private static final Point subpathBStart = new Point(386, 357);
private static final Point subpathCEnd  = new Point(519, 231);
private static final Point subpathCStart = new Point(308, 194);
private static final Point subpathDEnd  = new Point(461, 150);
private static final Point subpathDStart = new Point(666, 385);
private static final Rectangle subpathLeftRect = new Rectangle(328, 174, 187, 138);
private static final Rectangle subpathRightRect = new Rectangle(560, 94, 101, 308);

private static final Point tangentAEnd = new Point(778, 185);
private static final Point tangentAStart = new Point(73, 185);
private static final Point tangentBEnd = new Point(781, 201);
private static final Point tangentBStart = new Point(66, 203);
private static final Point tangentCEnd = new Point(459, 180);
private static final Point tangentCStart = new Point(364, 208);
private static final Rectangle tangentLeft = new Rectangle(156, 151, 200, 168);
private static final Rectangle tangentMiddle = new Rectangle(383, 151, 60, 95);
private static final Rectangle tangentRight = new Rectangle(473, 151, 251, 138);

// working vars
private PointList pathA, pathB, pathC, pathD;
private Rectangle rect;

private ShortestPathRouter routing;

private void doAssertAbove(Point pt1, Point pt2, Rectangle r) {
	assertTrue("Both points " + pt1 + " " + pt2 + " should bend above the rectangle. " + r, pt1.y < r.y && pt2.y < r.y);
}

private void doAssertBelow(Point pt1, Point pt2, Rectangle r) {
	assertTrue("Both points " + pt1 + " " + pt2 + " should bend below the rectangle. " + r, pt1.y > r.bottom() && pt2.y > r.bottom());
}

private void doAssertLeft(Point pt1, Point pt2, Rectangle r) {
	assertTrue("Both points " + pt1 + " " + pt2 + " should bend to the left of the rectangle. " + r, pt1.x < r.x && pt2.x < r.x);
}

private void doAssertNoPathsSolved() {
	assertTrue("No paths should have been solved.", routing.solve() == 0);
}

private void doAssertNumPoints(PointList path, int expectedBends) {
	assertTrue("Path should have " + expectedBends + " but had " + (path.size() - 2), expectedBends == path.size() - 2);
}

private void doAssertOffsetShrink(Point pt1, Point pt2) {
	assertTrue("Point : " + pt1 + " and " + pt2 + " should be separated by less than the normal offset: " + 4, 
			Math.abs(pt2.x - pt1.x) < 4 && Math.abs(pt2.y - pt1.y) < 4);
}

private void doAssertPathAbove(PointList pathA, PointList pathB) {
	assertEquals("Paths should have same number of bends " + pathA.size() + " " + pathB.size(), pathA.size(), pathB.size());
	
	for (int i = 1; i < pathA.size() - 1; i++) {
		Point a = pathA.getPoint(i);
		Point b = pathB.getPoint(i);
		assertTrue("Point " + a + " should have been above " + b, a.y < b.y);
	}
}

private void doAssertPathRight(PointList pathA, PointList pathB) {
	assertEquals("Paths should have same number of bends " + pathA.size() + " " + pathB.size(), pathA.size(), pathB.size());
	
	for (int i = 1; i < pathA.size() - 1; i++) {
		Point a = pathA.getPoint(i);
		Point b = pathB.getPoint(i);
		assertTrue("Point " + a + " should have been right of " + b, a.x > b.x);
	}
}

private void doAssertPointAbove(Point above, Point point) {
	assertTrue("Point : " + above + " should be below " + point, above.y < point.y);
}

private void doAssertPointBelow(Point below, Point point) {
	assertTrue("Point : " + below + " should be below " + point, below.y > point.y);
}

private void doAssertPointLeft(Point left, Point point) {
	assertTrue("Point : " + left + " should be left of " + point, left.x < point.x);
}

private void doAssertPointRight(Point right, Point point) {
	assertTrue("Point : " + right + " should be right of " + point, right.x > point.x);
}

private void doAssertRight(Point pt1, Point pt2, Rectangle r) {
	assertTrue("Both points " + pt1 + " " + pt2 + " should bend to the right of the rectangle. " + r, pt1.x > r.right() && pt2.x > r.right());
}

private void doSetUp(Point aStartStatic, Point aEndStatic, Point bStartStatic, Point bEndStatic, 
		Point cStartStatic, Point cEndStatic, Point dStartStatic, Point dEndStatic, Rectangle rectStatic) {
	Path d = new Path(dStartStatic.getCopy(), dEndStatic.getCopy());
	
	routing.addPath(d);
	
	doSetUp(aStartStatic, aEndStatic, bStartStatic, bEndStatic, cStartStatic, cEndStatic, rectStatic);
	
	pathD = d.getPoints();
}

private void doSetUp(Point aStartStatic, Point aEndStatic, Point bStartStatic, Point bEndStatic, 
		Point cStartStatic, Point cEndStatic, Rectangle rectStatic) {
	Path c = new Path(cStartStatic.getCopy(), cEndStatic.getCopy());
	
	routing.addPath(c);
	
	doSetUp(aStartStatic, aEndStatic, bStartStatic, bEndStatic, rectStatic);
	
	pathC = c.getPoints();	
}

private void doSetUp(Point aStartStatic, Point aEndStatic, Point bStartStatic, Point bEndStatic, Rectangle rectStatic) {
	Path b = new Path(bStartStatic.getCopy(), bEndStatic.getCopy());
	
	routing.addPath(b);
	
	doSetUp(aStartStatic, aEndStatic,rectStatic);
	
	pathB = b.getPoints();
}

private void doSetUp(Point aStartStatic, Point aEndStatic, Rectangle rectStatic) {
	rect = rectStatic.getCopy();
	
	Path a = new Path(aStartStatic.getCopy(), aEndStatic.getCopy());
	
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
	
	doAssertAbove(pathA.getPoint(numPoints - 1), pathB.getPoint(numPoints - 1), quadBendBottom);
	doAssertAbove(pathC.getPoint(numPoints - 1), pathD.getPoint(numPoints - 1), quadBendBottom);
	doAssertLeft(pathA.getPoint(numPoints - 1), pathB.getPoint(numPoints - 1), quadBendBottom);
	doAssertLeft(pathC.getPoint(numPoints - 1), pathD.getPoint(numPoints - 1), quadBendBottom);
	
	doAssertBelow(pathA.getPoint(numPoints), pathB.getPoint(numPoints), quadBendBottom);
	doAssertBelow(pathC.getPoint(numPoints), pathD.getPoint(numPoints), quadBendBottom);
	doAssertLeft(pathA.getPoint(numPoints), pathB.getPoint(numPoints), quadBendBottom);
	doAssertLeft(pathC.getPoint(numPoints), pathD.getPoint(numPoints), quadBendBottom);
	
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
	
	Path a = new Path(bendAStart.getCopy(), bendAEnd.getCopy());
	a.setBendPoints(Collections.singletonList(new AbsoluteBendpoint(bend.getCopy())));
	
	routing.addPath(a);
	
	assertTrue("Should have broken into two paths", routing.solve() == 2);
	
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
	doSetUp(corner2AStart, corner2AEnd, corner2BStart, corner2BEnd, corner2CStart, corner2CEnd, bl);
	doTestBottomLeftIntersection();
}

public void testBottomLeftIntersectionCross() {
	doSetUp(corner2BStart, corner2BEnd, corner2AStart, corner2AEndCross, corner2CStart, corner2CEnd, bl);
	doTestBottomLeftIntersection();
}

public void testBottomLeftIntersectionCrossInverted() {
	doSetUp(corner2BStart, corner2BEnd, corner2AStart, corner2AEndCross, corner2CEnd, corner2CStart, bl);
	doTestBottomLeftIntersection();
}

public void testBottomLeftIntersectionInverted() {
	doSetUp(corner2AStart, corner2AEnd, corner2BEnd, corner2BStart, corner2CStart, corner2CEnd, bl);
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
	doSetUp(tangentAEnd, tangentAStart, tangentBStart, tangentBEnd, tangentRight);
	doTestDeformed();
}

public void testDeformedInverted() {
	routing.addObstacle(deformedLeft.getCopy());
	routing.addObstacle(deformedLeftMid.getCopy());
	routing.addObstacle(deformedRightMid.getCopy());
	doSetUp(tangentAEnd, tangentAStart, tangentBEnd, tangentBStart, tangentRight);
	doTestDeformed();
}

public void testDeltasAddObstacleIntersection() {
	doSetUp(deltaAStart, deltaAEnd, deltaBStart, deltaBEnd, deltaRect);
	
	routing.addObstacle(deltaNewRectIntersec.getCopy());
	
	assertTrue("Both paths should have been solved.", routing.solve() == 2);
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
	Path c = new Path(corner2CStart.getCopy(), corner2CEnd.getCopy());
	routing.addPath(c);
	assertTrue("Only the new path should have been solved.", routing.solve() == 1);
	
	pathC = c.getPoints();
	// the new path, however, should have moved the other two paths.
	doAssertPointAbove(pathC.getPoint(1), pathA.getPoint(1));
	doAssertPointAbove(pathC.getPoint(1), pathB.getPoint(1));
}
public void testDeltasMoveObstacleIntersection() {
	doSetUp(deltaAStart, deltaAEnd, deltaBStart, deltaBEnd, deltaRect);
	
	routing.updateObstacle(deltaRect.getCopy(), deltaRectNewBounds.getCopy());
	
	assertTrue("Both paths should have been solved.", routing.solve() == 2);
}

public void testDeltasMoveObstacleNoIntersection() {
	doSetUp(deltaAStart, deltaAEnd, deltaBStart, deltaBEnd, deltaRect);
	
	routing.addObstacle(deltaNewRect.getCopy());
	
	routing.updateObstacle(deltaNewRect.getCopy(), deltaNewRectNewBounds.getCopy());

	doAssertNoPathsSolved();
}

public void testDeltasRemoveObstacleIntersection() {
	doSetUp(deltaAStart, deltaAEnd, deltaBStart, deltaBEnd, deltaRect);
	
	routing.removeObstacle(deltaRect.getCopy());
	
	assertTrue("Both paths should have been solved.", routing.solve() == 2);
}

public void testDeltasRemoveObstacleNoIntersection() {
	doSetUp(deltaAStart, deltaAEnd, deltaBStart, deltaBEnd, deltaRect);
	
	routing.addObstacle(deltaNewRect.getCopy());
	routing.removeObstacle(deltaNewRect.getCopy());
	
	doAssertNoPathsSolved();
}

public void testDeltasRemovePath() {
	doSetUp(corner2AStart, corner2AEnd, corner2BStart, corner2BEnd, bl);
	Path c = new Path(corner2CStart, corner2CEnd);
	routing.removePath(c);
	
	// should be no change.
	doAssertNoPathsSolved();
}

public void testOffsetShrink() {
	routing.addObstacle(offsetRectLeft.getCopy());
	routing.addObstacle(offsetRectRight.getCopy());
	routing.addObstacle(offsetRectBottom.getCopy());
	doSetUp(offsetAStart, offsetAEnd, offsetBStart, offsetBEnd, offsetCStart, offsetCEnd, offsetDStart, offsetDEnd, offsetRectTop);
	doTestOffsetShrink();
}

public void testQuadBendHit() {
	routing.addObstacle(quadBendBottom.getCopy());
	routing.addObstacle(quadBendMiddleHit.getCopy());
	doSetUp(quadBendAStart, quadBendAEnd, quadBendBStart, quadBendBEnd, quadBendCStart, quadBendCEnd, quadBendDStart, quadBendDEnd, quadBendTop);
	doTestQuadBendMiss(5);
}

public void testQuadBendHit2() {
	routing.addObstacle(quadBendBottom.getCopy());
	routing.addObstacle(quadBendMiddleHit2.getCopy());
	doSetUp(quadBendAStart, quadBendAEnd, quadBendBStart, quadBendBEnd, quadBendCStart, quadBendCEnd, quadBendDStart, quadBendDEnd, quadBendTop);
	doTestQuadBendMiss(5);
}

public void testQuadBendMiss() {
	routing.addObstacle(quadBendBottom.getCopy());
	routing.addObstacle(quadBendMiddleMiss.getCopy());
	doSetUp(quadBendAStart, quadBendAEnd, quadBendBStart, quadBendBEnd, quadBendCStart, quadBendCEnd, quadBendDStart, quadBendDEnd, quadBendTop);
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
	doSetUp(subpathAStart, subpathAEnd, subpathBStart, subpathBEnd, subpathCStart, 
			subpathCEnd, subpathDStart, subpathDEnd, subpathRightRect);
	
	doAssertNumPoints(pathD, 3);
	
	doAssertPointAbove(pathD.getPoint(1), pathA.getPoint(1));
	doAssertPointAbove(pathD.getPoint(2), pathA.getPoint(2));
	doAssertPointAbove(pathD.getPoint(3), pathC.getPoint(2));
}

public void testTangent() {
	routing.addObstacle(tangentMiddle.getCopy());
	routing.addObstacle(tangentRight.getCopy());
	doSetUp(tangentAStart, tangentAEnd, tangentBStart, tangentBEnd, tangentCStart, tangentCEnd, tangentLeft);
	doTestTangent();
}

public void testTangentInverted() {
	routing.addObstacle(tangentMiddle.getCopy());
	routing.addObstacle(tangentRight.getCopy());
	doSetUp(tangentAEnd, tangentAStart, tangentBStart, tangentBEnd, tangentCEnd, tangentCStart, tangentLeft);
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