/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Research Group Software Construction,
 *     RWTH Aachen University, Germany - Contribution for Bugzilla 245182
 *
 *******************************************************************************/
package org.eclipse.draw2d.geometry;

/**
 * Represents a vector within 2-dimensional Euclidean space.
 * 
 * @since 3.6
 */
public class Vector {

	/** the X value */
	public double x;
	/** the Y value */
	public double y;

	/**
	 * Constructs a Vector with no direction and magnitude.
	 * 
	 */
	public Vector() {
	}

	/**
	 * Constructs a Vector pointed in the specified direction.
	 * 
	 * @param x
	 *            X value.
	 * @param y
	 *            Y value.
	 */
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs a Vector pointed in the direction specified by a Point.
	 * 
	 * @param p
	 *            the point
	 */
	public Vector(PrecisionPoint p) {
		x = p.preciseX;
		y = p.preciseY;
	}

	/**
	 * Constructs a Vector representing the direction and magnitude between to
	 * provided Points.
	 * 
	 * @param start
	 *            starting point
	 * @param end
	 *            End Point
	 */
	public Vector(PrecisionPoint start, PrecisionPoint end) {
		x = end.preciseX - start.preciseX;
		y = end.preciseY - start.preciseY;
	}

	/**
	 * Constructs a Vector representing the difference between two provided
	 * Vectors.
	 * 
	 * @param start
	 *            The start Ray
	 * @param end
	 *            The end Ray
	 */
	public Vector(Vector start, Vector end) {
		x = end.x - start.x;
		y = end.y - start.y;
	}

	/**
	 * Calculates the magnitude of the cross product of this Vector with
	 * another. Represents the amount by which two Vectors are directionally
	 * different. Parallel Vectors return a value of 0.
	 * 
	 * @param other
	 *            Vector being compared
	 * @return The dissimilarity
	 * @see #getSimilarity(Vector)
	 */
	public double getDissimilarity(Vector other) {
		return Math.abs(getCrossProduct(other));
	}

	/**
	 * Calculates whether this Vector and the provided one are parallel to each
	 * other.
	 * 
	 * @param other
	 *            The Vector to test for parallelism
	 * @return true if this Vector and the provided one are parallel, false
	 *         otherwise.
	 */
	public boolean isParallelTo(Vector other) {
		return getDissimilarity(other) == 0.0;
	}

	/**
	 * Calculates the dot product of this Vector with another.
	 * 
	 * @param other
	 *            the Vector used to calculate the dot product
	 * @return The dot product
	 */
	public double getDotProduct(Vector other) {
		return x * other.x + y * other.y;
	}

	/**
	 * Calculates the cross product of this Vector with another.
	 * 
	 * @param other
	 *            the Vector used to calculate the cross product
	 * @return The cross product.
	 */
	public double getCrossProduct(Vector other) {
		return x * other.y - y * other.x;
	}

	/**
	 * Creates a new Vector which is the sum of this Vector with another.
	 * 
	 * @param other
	 *            Vector to be added to this Vector
	 * @return a new Vector representing the sum
	 */
	public Vector getAdded(Vector other) {
		return new Vector(other.x + x, other.y + y);
	}

	/**
	 * Creates a new Vector which is the difference of this Vector with the
	 * provided Vector.
	 * 
	 * @param other
	 *            Vector to be subtracted from this Vector
	 * @return a new Vector representing the difference.
	 */
	public Vector getSubtracted(Vector other) {
		return new Vector(x - other.x, y - other.y);
	}

	/**
	 * Returns the angle (in degrees) between this Vector and the provided Vector.
	 * 
	 * @param other
	 *            Vector to calculate the angle.
	 * @return the angle between the two Vectors in degrees.
	 */
	public double getAngle(Vector other) {
		double cosAlpha = getDotProduct(other)
				/ (getLength() * other.getLength());
		return Math.toDegrees(Math.acos(cosAlpha));
	}

	/**
	 * Creates a new Vector which represents the average of this Vector with
	 * another.
	 * 
	 * @param vector
	 *            Vector to calculate the average.
	 * @return a new Vector
	 */
	public Vector getAveraged(Vector vector) {
		return new Vector((x + vector.x) / 2, (y + vector.y) / 2);
	}

	/**
	 * Creates a new Vector which represents this Vector scaled by the amount
	 * provided.
	 * 
	 * @param scale
	 *            Value providing the amount to scale.
	 * @return a new Vector
	 */
	public Vector getScaled(double scale) {
		return new Vector(x * scale, y * scale);
	}

	/**
	 * Returns the orthogonal complement of this Vector, which is defined to be
	 * (-y, x).
	 * 
	 * @return the orthogonal complement of this Vector
	 */
	public Vector getOrthogonalComplement() {
		return new Vector(-y, x);
	}

	/**
	 * Returns the length of this Vector.
	 * 
	 * @return Length of this Vector
	 */
	public double getLength() {
		return Math.sqrt(getDotProduct(this));
	}

	/**
	 * Calculates the similarity of this Vector with another. Similarity is
	 * defined as the absolute value of the dotProduct(). Orthogonal vectors
	 * return a value of 0.
	 * 
	 * @param other
	 *            Vector being tested for similarity
	 * @return the Similarity
	 * @see #getDissimilarity(Vector)
	 */
	public double getSimilarity(Vector other) {
		return Math.abs(getDotProduct(other));
	}

	/**
	 * Calculates whether this Vector and the provided one are orthogonal to
	 * each other.
	 * 
	 * @param other
	 *            Vector being tested for orthogonality
	 * @return true, if this Vector and the provide one are orthogonal, false
	 *         otherwise
	 */
	public boolean isOrthogonalTo(Vector other) {
		return getSimilarity(other) == 0.0;
	}

	/**
	 * Returns a point representation of this Vector.
	 * 
	 * @return a PrecisionPoint representation
	 */
	public PrecisionPoint toPoint() {
		return new PrecisionPoint(x, y);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + x + "," + y + ")";//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof Vector) {
			Vector r = (Vector) obj;
			return x == r.x && y == r.y;
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (int) (x * y) ^ (int) (x + y);
	}

}
