/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.viewers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart;


/**
 * The NoOverlapLayout class will check all sibling nodes to ensure that the given
 * bounds don't overlap.   
 * 
 * @author Chris Callendar
 */
public class NoOverlapLayout  {

	public static final int DEFAULT_SPACING = 30;
	
	private int spacing;
	
	/**
	 * Initializes the layout with the default spacing.
	 */
	public NoOverlapLayout() {
		this(DEFAULT_SPACING);
	}
	
	/**
	 * Initializes the layout with the given spacing.
	 * @param spacing the spacing between figures (positive #).
	 */
	public NoOverlapLayout(int spacing) {
		this.spacing = (spacing <= 0 ? DEFAULT_SPACING : spacing);
	}
	
	/**
	 * Applies the no overlap layout algorithm.  The bounds Rectangle
	 * <b>will</b> be modified if overlapping occurs.
	 * All the sibling nodes' bounds will <b>not</b> be checked for overlapping, only the
	 * given bounds will be checked against the existing nodes' bounds.
	 * When overlapping occurs the nodes are layed out beside, above, or below.  This 
	 * position will be the closest position in which no overlapping occurs.
	 * @param bounds the bounds to check
	 * @return Rectangle the same Rectangle adjusted if necessary (returned for convenience)
	 */
	public Rectangle checkBounds(GraphNodeEditPart editPart, Rectangle bounds) {
		ArrayList rectangles = new ArrayList(editPart.getParent().getChildren().size());
		for (Iterator iter = editPart.getParent().getChildren().iterator(); iter.hasNext(); ) {
			EditPart part = (EditPart)iter.next();
			if (part instanceof GraphNodeEditPart) {
				GraphNodeEditPart graphEditPart = (GraphNodeEditPart)part;
				if (graphEditPart != editPart) {
					Rectangle rect = ((GraphModelNode)graphEditPart.getModel()).getBounds();
					rectangles.add(rect);
				}
			}
		}
		// now check if this bounds overlaps and adjust if necessary
		addRectangle(rectangles, bounds);
		return bounds;
	}
	
	/**
	 * Checks if the rectangle overlaps with any existing Rectangle in the list.  
	 * If it does then the bounds are adjusted to try and ensure no overlap.
	 * @param rectangles A list of Rectangle objects 
	 * @param bounds the rectangle to check
	 * @return Rectangle the same Rectangle, adjusted in location if necessary
	 */
	public Rectangle addRectangle(ArrayList rectangles, Rectangle bounds) {
		for (int i = 0; i < rectangles.size(); i++) {
			Object obj = rectangles.get(i);
			if (obj instanceof Rectangle) {
				Rectangle rect = (Rectangle)obj;
				Rectangle intersect = rect.getIntersection(bounds);
				if (!intersect.isEmpty()) {
					//boolean onleft = (bounds.x < rect.x);
					//boolean onup = (bounds.y < rect.y);
					int xleft = rect.x - bounds.width - spacing;
					int xright = rect.x + rect.width + spacing;
					int yup = rect.y - bounds.height - spacing;
					int ydown = rect.y + rect.height + spacing;
					
					Point ref = intersect.getCenter();
					Point xl = new Point(xleft, bounds.y);
					Point xr = new Point(xright, bounds.y);
					Point yu = new Point(bounds.x, yup);
					Point yd = new Point(bounds.x, ydown);
					Point left = new Point(xleft, rect.y);
					Point right = new Point(xright, rect.y);
					Point above = new Point(rect.x, yup);
					Point below = new Point(rect.x, ydown);
					Point[] sortedPoints = sortPoints(ref, new Point[] { xl, left, xr, right, yu, above, yd, below });
					for (int j = 0; j < sortedPoints.length; j++) {
						bounds.setLocation(sortedPoints[j]);
						boolean ok = true;
						for (int k = 0; k < i; k++) {
							Rectangle r = (Rectangle)rectangles.get(k);
							if (!r.getIntersection(bounds).isEmpty()) {
								ok = false;
								break;
							}
						}
						if (ok) {
							break;
						}
						// set it back to the first point since it is the smallest movement
						if (j == (sortedPoints.length - 1)) {
							bounds.setLocation(sortedPoints[0]);
						}
					}
				}
			}				
		}
		rectangles.add(bounds);
		return bounds;
	}
	
	/**
	 * Sorts the points putting the points closest to the reference
	 * point first.  A new array is returned.
	 * @param ref	 The reference point.
	 * @param points The points to sort.
	 * @return Point[] the same Points array for convenience
	 */
	private Point[] sortPoints(Point ref, Point[] points) {
		Arrays.sort(points, new PointComparator(ref, true));
		return points;
	}

		
	/**
	 * A class for comparing {@link Point} objects to a reference Point.
	 * 
	 * @author Chris Callendar
	 */
	class PointComparator implements Comparator {

		private Point ref;
		private boolean preferPositivePoints;

		/**
		 * Initializes this comparator with the given reference point.
		 * Positive points get preference over points containing negative values.
		 * @param ref the reference point to compare all points to.
		 */
		public PointComparator(Point ref) {
			this(ref, true);
		}

		/**
		 * Initializes this comparator with the given reference point.
		 * @param ref the reference point to compare all points to.
		 * @param preferPositivePoints if points whose (x,y) values are positive are preferred.
		 */
		public PointComparator(Point ref, boolean preferPositivePoints) {
			this.ref = ref;
			this.preferPositivePoints = preferPositivePoints;
		}
		
		/**
		 * Compares two Points and returns -1 if the first Point is 
		 * closer to the reference point than the second.
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object obj1, Object obj2) {
			int rv = 0;
			if ((obj1 instanceof Point) && (obj2 instanceof Point)) {
				Point p1 = (Point)obj1;
				Point p2 = (Point)obj2;
				int dist1 = ref.getDistance2(p1);
				int dist2 = ref.getDistance2(p2);

				// if preferPositivePoints is true, then give preference
				// to positive points.  If both are positive or both are negative 
				// then go with the closest point
				boolean p1pos = ((p1.x >= 0) && (p1.y >= 0));
				boolean p2pos = ((p2.x >= 0) && (p2.y >= 0));
				if (preferPositivePoints && (p1pos && p2pos)) {
					rv = (dist1 - dist2);
				} else if (p1pos) {
					rv = -1;
				} else if (p2pos) {
					rv = 1;
				} else {
					rv = (dist1 - dist2);
				}
			}
			return rv;
		}
		
	}

}
