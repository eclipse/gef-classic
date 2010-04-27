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
 *     
 *******************************************************************************/
package org.eclipse.draw2d.geometry;

/**
 * Represents a straight line within 2-dimensional Euclidean space.
 * 
 * @author Alexander Nyssen
 * @since 3.6
 */
public class Straight {

	/** position vector of this straight */
	public Vector position;

	/** direction vector of this straight */
	public Vector direction;

	/**
	 * Constructs a new Straight with the given position and direction.
	 * 
	 * @param position
	 * @param direction
	 */
	public Straight(Vector position, Vector direction) {
		this.position = position;
		this.direction = direction;
	}

	/**
	 * Constructs a new Straight between the two given Points.
	 * 
	 * @param point1
	 *            a first waypoint of the Straight to be constructed
	 * @param point2
	 *            a second waypoint of the Straight to be constructed
	 */
	public Straight(PrecisionPoint point1, PrecisionPoint point2) {
		this(new Vector(point1), new Vector(point1, point2));
	}

	/**
	 * Checks whether this Straight and the provided one have an intersection
	 * point.
	 * 
	 * @param other
	 *            The Straight to use for the calculation.
	 * @return true if the two Straights intersect, false otherwise.
	 */
	public boolean intersects(Straight other) {
		// check if there is an intersection point
		return direction.getDotProduct(other.direction
				.getOrthogonalComplement()) != 0;
	}

	/**
	 * Computes the intersection point of this Straight and the provided one, if
	 * it exists.
	 * 
	 * @param other
	 *            The Straight to use for calculations.
	 * @return A Vector pointing to the intersection point, if it exists, null
	 *         if no intersection point exists (or the Straights are equal).
	 */
	public Vector getIntersection(Straight other) {
		if (!intersects(other)) {
			return null;
		}

		// retrieve position and direction Vectors of other straight
		Vector q = other.position;
		Vector b = other.direction;

		// retrieve orthogonal complements needed during computation
		Vector aOC = direction.getOrthogonalComplement(); // orthogonal
															// complement of a
		Vector bOC = b.getOrthogonalComplement(); // orthogonal complement of b

		// compute intersection point
		double[] intersection = new double[2];
		intersection[0] = (q.getDotProduct(bOC) * direction.x - position
				.getDotProduct(aOC)
				* b.x)
				/ direction.getDotProduct(bOC);
		intersection[1] = (q.getDotProduct(bOC) * direction.y - position
				.getDotProduct(aOC)
				* b.y)
				/ direction.getDotProduct(bOC);
		return new Vector(intersection[0], intersection[1]);
	}

	/**
	 * Returns the (smallest) angle between this Straight and the provided one.
	 * 
	 * @param other
	 *            The Straight to be used for the calculation.
	 * @return The angle spanned between the two Straights.
	 */
	public double getAngle(Straight other) {
		return direction.getAngle(other.direction);
	}

	/**
	 * Returns the projection of the given Vector onto this Straight, which is
	 * the point on this Straight with the minimal distance to the point,
	 * denoted by the provided Vector.
	 * 
	 * @param vector
	 *            The Vector whose projection should be determined.
	 * @return A new Vector representing the projection of the provided Vector
	 *         onto this Straight.
	 */
	public Vector getProjection(Vector vector) {
		Vector s = getIntersection(new Straight(vector, direction
				.getOrthogonalComplement()));
		return s;
	}

	/**
	 * Returns the distance of the provided Vector to this Straight, which is
	 * the distance between the provided Vector and its projection onto this
	 * Straight.
	 * 
	 * @param vector
	 *            The Vector whose distance is to be calculated.
	 * @return the distance between this Straight and the provided Vector.
	 */
	public double getDistance(Vector vector) {
		Vector s = getProjection(vector);
		return s.getSubtracted(vector).getLength();
	}

	/**
	 * Calculates whether the point indicated by the provided Vector is a point
	 * on this Straight.
	 * 
	 * @param vector
	 *            the Vector who has to be checked.
	 * @return true if the position indicated by the given Vector is a point of
	 *         this Straight, false otherwise.
	 */
	public boolean contains(Vector vector) {
		return getDistance(vector) == 0.0;
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
			Straight otherStraight = (Straight) other;
			return contains(otherStraight.position)
					&& isParallelTo(otherStraight);
		}
	}

	/**
	 * Checks whether this Straight and the provided one are parallel to each
	 * other.
	 * 
	 * @param other
	 *            The Straight to test for parallelism.
	 * @return true if the direction vectors of this Straight and the provided
	 *         one are parallel, false otherwise.
	 */
	public boolean isParallelTo(Straight other) {
		return direction.isParallelTo(other.direction);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return position.hashCode() + direction.hashCode();
	}

}