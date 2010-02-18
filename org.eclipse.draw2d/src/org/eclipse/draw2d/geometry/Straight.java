/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Research Group Software Construction,
 *     RWTH Aachen University, Germany - initial API and implementation
 */
package org.eclipse.draw2d.geometry;

/**
 * Represents a straight line within a 2-dimensional space. Together with Ray,
 * this allows computations defined within Euclidean geometry.
 * 
 * @author Alexander Nyssen
 * @since 3.6
 */
public class Straight {

	private Ray p; // Ray indicating the position vector of the straight
	private Ray a; // Ray representing the direction vector of this straight

	/**
	 * Constructs a new Straight with the given position and direction.
	 * 
	 * @param position
	 * @param direction
	 */
	public Straight(Ray position, Ray direction) {
		this.p = position;
		this.a = direction;
	}

	/**
	 * Constructs a new Straight between the two given Points.
	 * 
	 * @param point1
	 * @param point2
	 */
	public Straight(Point point1, Point point2) {
		this(new Ray(point1), new Ray(point1, point2));
	}

	/**
	 * Returns the position of this Straight.
	 * 
	 * @return A Ray indicating the position of this Straight.
	 */
	public Ray getPosition() {
		return p;
	}

	/**
	 * Returns the direction of this Straight.
	 * 
	 * @return A Ray indicating the position of this Straight.
	 */
	public Ray getDirection() {
		return a;
	}

	/**
	 * Checks whether this Straight and the provided one have an intersection
	 * point.
	 * 
	 * @param g2
	 *            The Straight to use for calculations. It may not be equal to
	 *            this Straight.
	 * @return true if the two Straights intersect, false otherwise.
	 */
	public boolean intersects(Straight g2) {
		// check if there is an intersection point
		return a.dotProduct(g2.getDirection().getOrthogonalComplement()) != 0;
	}

	/**
	 * Computes the intersection point of this Straight and the provided one, if it exists.
	 * 
	 * @param g2
	 *            The Straight to use for calculations. It may not be equal to
	 *            this Straight.
	 * @return A Ray pointing to the intersection point, if it exists, null
	 *         if no intersection point exists (or the Straights are equal).
	 */
	public Ray getIntersection(Straight g2) {
		if (!intersects(g2)) {
			return null;
		}

		// retrieve position and direction Rays of other straight
		Ray q = g2.getPosition();
		Ray b = g2.getDirection();

		// retrieve orthogonal complements needed during computation
		Ray aOC = a.getOrthogonalComplement(); // orthogonal complement of a
		Ray bOC = b.getOrthogonalComplement(); // orthogonal complement of b

		// compute intersection point
		int[] intersection = new int[2];
		intersection[0] = (q.dotProduct(bOC) * a.x - p.dotProduct(aOC) * b.x)
				/ a.dotProduct(bOC);
		intersection[1] = (q.dotProduct(bOC) * a.y - p.dotProduct(aOC) * b.y)
				/ a.dotProduct(bOC);
		return new Ray(intersection[0], intersection[1]);
	}

	/**
	 * Returns the (smallest) angle between this Ray and the provided one.
	 * 
	 * @param g2
	 *            The Ray to be used for calculations.
	 * @return The angle spanned between the two Rays.
	 */
	public double getAngle(Straight g2) {
		return a.getAngle(g2.getDirection());
	}

	/**
	 * Returns the projection of the given Ray onto this Straight, which is the
	 * point on this Straight with the minimal distance to the point, denoted by
	 * the provided Ray.
	 * 
	 * @param q
	 *            The Ray whose projection should be determined.
	 * @return A new Ray representing the projection of the provided Ray onto
	 *         this Straight.
	 */
	public Ray getProjection(Ray q) {
		Ray s = getIntersection(new Straight(q, a.getOrthogonalComplement()));
		return s;
	}

	/**
	 * Returns the distance of the provided Ray to this Straight, which is the
	 * distance between the provided Ray and its projection onto this Straight.
	 * 
	 * @param q
	 *            The Ray whose distance is to be calculated.
	 * @return the distance between this Straight and the provided Ray.
	 */
	public double getDistance(Ray q) {
		Ray s = getProjection(q);
		return s.getSubtracted(q).length();
	}

	/**
	 * Calculates wether the point indicated by the provided Ray is a point on
	 * this Straight.
	 * 
	 * @param q
	 *            the Ray who has to be checked.
	 * @return true if the position indicated by the given Ray is a point of
	 *         this Straight, false otherwise.
	 */
	public boolean contains(Ray q) {
		return getDistance(q) == 0.0;
	}

	/**
	 * Checks whether this Straight is equal to the provided Straight. Two
	 * Straights s1 and s2 are equal, if the position vector of s2 is a point on
	 * s1 and the direction vectors of s1 and s2 are parallel.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (!(other instanceof Straight)) {
			return false;
		} else {
			Straight otherLine = (Straight) other;
			return contains(otherLine.getPosition())
					&& a.getOrthogonalComplement().dotProduct(
							otherLine.getDirection()) == 0;
		}
	}

}