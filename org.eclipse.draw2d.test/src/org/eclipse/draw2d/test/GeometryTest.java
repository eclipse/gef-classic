/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alexander Shatalin (Borland) - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Geometry;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;


public class GeometryTest extends TestCase {
	
	/*
	 * For Geometry.polygonContainsPoint tests
	 */
	private static final PointList RHOMB = new PointList(new int[] {0,2, 2,0, 4,2, 2,4});
	
	private static final PointList CONCAVE_PENTAGON = new PointList(new int[] {0,0, 0,8, 4,4, 8,8, 8,0});
	
	private static final PointList CONCAVE_OCTAGON = new PointList(new int[] {0,0, 0,4, 2,4, 2,2, 4,2, 4,4, 6,4, 6,0});

	/*
	 * For Geometry.polylineContainsPoint tests
	 */
	private static final PointList POLYLINE = new PointList(new int[] {0,0, 1,0, 6,5});
	
	private static final int TOLERANCE = 2;

	/*
	 * shifting all PointLists to this value
	 */
	private static final int POINT_LIST_SHIFT = TOLERANCE + 1; 
	
	/*
	 * 8 = Max(x1, y1, x2, y2, ..) for all coordinates specified in the sample PointLists
	 *   
	 */
	private static final int IMAGE_SIZE = 8 + POINT_LIST_SHIFT*2;
	
	/**
	 * Testing points inside/outside the rhomb located in top half.
	 * Excluding points of RHOMB border - separate test present for it
	 */
	public void testTopRhombHalfPoints() {
		assertFalse("This point is outside the rhomb", Geometry.polygonContainsPoint(RHOMB, 0, 1));
		assertTrue("This point is inside the rhomb", Geometry.polygonContainsPoint(RHOMB, 2, 1));
		assertFalse("This point is outside the rhomb", Geometry.polygonContainsPoint(RHOMB, 4, 1));
	}
	
	/**
	 * Testing points inside/outside the rhomb located in bottop half.
	 * Excluding points of RHOMB border - separate test present for it
	 */
	public void testBottomRhombHalfPoints() {
		assertFalse("This point is outside the rhomb", Geometry.polygonContainsPoint(RHOMB, 0, 3));
		assertTrue("This point is inside the rhomb", Geometry.polygonContainsPoint(RHOMB, 2, 3));
		assertFalse("This point is outside the rhomb", Geometry.polygonContainsPoint(RHOMB, 4, 3));
	}
	
	/**
	 * Testing points inside/outside the rhomb located on the equator.
	 * Excluding points of RHOMB border - separate test present for it
	 */
	public void testRhombEquatorPoints() {
		assertFalse("This point is outside the rhomb", Geometry.polygonContainsPoint(RHOMB, -1, 2));
		assertTrue("This point is inside the rhomb", Geometry.polygonContainsPoint(RHOMB, 2, 2));
		assertFalse("This point is outside the rhomb", Geometry.polygonContainsPoint(RHOMB, 5, 2));
	}
	
	/**
	 * Testing points outside the rhomb located on top horizontal tangent line.
	 * Excluding points of RHOMB border - separate test present for it
	 */
	public void testTopRhombTangentPoints() {
		assertFalse("This point is outside the rhomb", Geometry.polygonContainsPoint(RHOMB, 0, 0));
		assertFalse("This point is outside the rhomb", Geometry.polygonContainsPoint(RHOMB, 4, 0));
	}
	
	/**
	 * Testing points outside the rhomb located on bottom horizontal tangent line.
	 * Excluding points of RHOMB border - separate test present for it
	 */
	public void testBottomRhombTangentPoints() {
		assertFalse("This point is outside the rhomb", Geometry.polygonContainsPoint(RHOMB, 0, 4));
		assertFalse("This point is outside the rhomb", Geometry.polygonContainsPoint(RHOMB, 4, 4));
	}
	
	/**
	 * Testing points of RHOMB border - all vertexes + one point on the edge 
	 */
	public void testRhombBorderPoints() {
		assertTrue("This point is inside the rhomb", Geometry.polygonContainsPoint(RHOMB, 0, 2));
		assertTrue("This point is inside the rhomb", Geometry.polygonContainsPoint(RHOMB, 0, 2));
		assertTrue("This point is inside the rhomb", Geometry.polygonContainsPoint(RHOMB, 2, 4));
		assertTrue("This point is inside the rhomb", Geometry.polygonContainsPoint(RHOMB, 2, 2));
		assertTrue("This point is inside the rhomb", Geometry.polygonContainsPoint(RHOMB, 1, 1));
	}
	
	/**
	 * Testing points inside/outside the pentagon located on equator of concave.
	 * Excluding points of CONCAVE_PENTAGON border - separate test present for it
	 */
	public void testConcavePentagonEquatorPoints() {
		assertFalse("This point is outside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, -1, 6));
		assertTrue("This point is inside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 1, 6));
		assertFalse("This point is outside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 4, 6));
		assertTrue("This point is inside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 7, 6));
		assertFalse("This point is outside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 9, 6));
	}
	
	/**
	 * Testing points outside the pentagon located on top concave tangent.
	 * Excluding points of CONCAVE_PENTAGON border - separate test present for it
	 */
	public void testTopConcavePentagonTangentPoints() {
		assertFalse("This point is outside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, -1, 8));
		assertFalse("This point is outside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 4, 8));
		assertFalse("This point is outside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 9, 8));
	}
	
	/**
	 * Testing points inside/outside the pentagon located on bottom concave tangent.
	 * Excluding points of CONCAVE_PENTAGON border - separate test present for it
	 */
	public void testBottomConcavePentagonTangentPoints() {
		assertFalse("This point is outside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, -1, 4));
		assertTrue("This point is inside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 1, 4));
		assertTrue("This point is inside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 5, 4));
		assertFalse("This point is outside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 9, 4));
	}
	
	/**
	 * Testing points of CONCAVE_PENTAGON border - all vertexes + points on concave edges 
	 */
	public void testConcavePentagonBorderPoints() {
		assertTrue("This point is inside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 0, 8));
		assertTrue("This point is inside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 2, 6));
		assertTrue("This point is inside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 4, 4));
		assertTrue("This point is inside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 6, 6));
		assertTrue("This point is inside the pentagon", Geometry.polygonContainsPoint(CONCAVE_PENTAGON, 8, 8));
	}
	
	/**
	 * Testing points located of the horizontal line containing one of the "concave" edges 
	 */
	public void testConcaveOctagonBottomTangentPoints() {
		assertFalse("This point is outside the octagon", Geometry.polygonContainsPoint(CONCAVE_OCTAGON, -1, 2));
		assertTrue("This point is inside the octagon", Geometry.polygonContainsPoint(CONCAVE_OCTAGON, 0, 2));
		assertTrue("This point is inside the octagon", Geometry.polygonContainsPoint(CONCAVE_OCTAGON, 1, 2));
		assertTrue("This point is inside the octagon", Geometry.polygonContainsPoint(CONCAVE_OCTAGON, 2, 2));
		assertTrue("This point is inside the octagon", Geometry.polygonContainsPoint(CONCAVE_OCTAGON, 3, 2));
		assertTrue("This point is inside the octagon", Geometry.polygonContainsPoint(CONCAVE_OCTAGON, 4, 2));
		assertTrue("This point is inside the octagon", Geometry.polygonContainsPoint(CONCAVE_OCTAGON, 5, 2));
		assertFalse("This point is outside the octagon", Geometry.polygonContainsPoint(CONCAVE_OCTAGON, 7, 2));
	}
	
	public void testNotPolylinePoints() {
		// Point is outside of (polyline.bounds +- tolerance) rectangle
		Geometry.polylineContainsPoint(POLYLINE, 9, 5, TOLERANCE);
		// Point is inside of (polyline.bounds +- tolerance) rectangle, but quite far from segments
		Geometry.polylineContainsPoint(POLYLINE, 1, 4, TOLERANCE);
	}
	
	public void testPolylinePoints() {
		// point is close to horizontal segment
		Geometry.polylineContainsPoint(POLYLINE, -1, 0, TOLERANCE);
		// point is close to the slopping segment
		Geometry.polylineContainsPoint(POLYLINE, 2, 3, TOLERANCE);
		// point is on polyline
		Geometry.polylineContainsPoint(POLYLINE, 0, 0, TOLERANCE);
		Geometry.polylineContainsPoint(POLYLINE, 1, 0, TOLERANCE);
		Geometry.polylineContainsPoint(POLYLINE, 2, 1, TOLERANCE);
	}
	
	public void testDrawPolygons() {
		checkFilledPolygonPoints(translatePointList(RHOMB));
		checkFilledPolygonPoints(translatePointList(CONCAVE_PENTAGON));
		checkFilledPolygonPoints(translatePointList(CONCAVE_OCTAGON));
	}
	
	public void testDrawPolylines() {
		checkPolylinePoints(translatePointList(RHOMB));
		checkPolylinePoints(translatePointList(CONCAVE_PENTAGON));
		checkPolylinePoints(translatePointList(CONCAVE_OCTAGON));
		checkPolylinePoints(translatePointList(POLYLINE));
	}
	
	private PointList translatePointList(PointList original) {
		PointList translated = original.getCopy();
		translated.performTranslate(POINT_LIST_SHIFT, POINT_LIST_SHIFT);
		return translated;
	}
	
	private void checkFilledPolygonPoints(PointList pointlist) {
		Display display = Display.getDefault();
		Image image = new Image(display, IMAGE_SIZE, IMAGE_SIZE);
		GC gc = new GC(image);
		cleanupImage(gc);
		gc.setBackground(ColorConstants.black);
		gc.setForeground(ColorConstants.black);
		gc.fillPolygon(pointlist.toIntArray());
		gc.drawPolygon(pointlist.toIntArray());
		gc.dispose();
		ImageData imageData = image.getImageData();
		for (int x = 0; x < IMAGE_SIZE; x++) {
			for (int y = 0; y < IMAGE_SIZE; y++) {
				boolean isPolygonPoint = imageData.getPixel(x, y) == 0;
				assertTrue("Point (" + x + "," + y + ") is" + 
						(isPolygonPoint ? " " : " not ") + "a point of polygon", 
						Geometry.polygonContainsPoint(pointlist, x, y) == isPolygonPoint);
			}
		}
	}

	private void checkPolylinePoints(PointList pointlist) {
		Display display = Display.getDefault();
		Image image = new Image(display, IMAGE_SIZE, IMAGE_SIZE);
		GC gc = new GC(image);
		cleanupImage(gc);
		gc.setForeground(ColorConstants.black);
		gc.drawPolyline(pointlist.toIntArray());
		gc.dispose();
		ImageData imageData = image.getImageData();
		for (int x = 0; x < IMAGE_SIZE; x++) {
			for (int y = 0; y < IMAGE_SIZE; y++) {
				if (imageData.getPixel(x, y) == 0) {
					assertTrue("Point (" + x + "," + y + ") is a point of polyline", 
							Geometry.polylineContainsPoint(pointlist, x, y, TOLERANCE));
				}
			}
		}
	}

	// Filling initial image with white color
	private void cleanupImage(GC gc) {
		gc.setBackground(ColorConstants.white);
		gc.setForeground(ColorConstants.white);
		gc.fillRectangle(0, 0, IMAGE_SIZE, IMAGE_SIZE);
		gc.drawRectangle(0, 0, IMAGE_SIZE, IMAGE_SIZE);
	}
	
}
