package org.eclipse.draw2d.geometry;


/**
 * @author Randy Hudson
 */
public class PrecisionDimension extends Dimension {

/**
 * The width in double precision.
 */
public double preciseWidth;
/**
 * The height in double precision.
 */
public double preciseHeight;

/**
 * Constructs a new precision dimension.
 */
public PrecisionDimension() {
}

/**
 * Constructs a precision representation of the given dimension.
 * @param d the reference dimension
 */
public PrecisionDimension(Dimension d) {
	super(d);
	preciseHeight = d.height;
	preciseWidth = d.width;
}

/**
 * @see org.eclipse.draw2d.geometry.Dimension#performScale(double)
 */
public void performScale(double factor) {
	preciseHeight *= factor;
	preciseWidth *= factor;
	updateInts();
}

/**
 * Updates the integer fields using the precise versions.
 */
public final void updateInts() {
	width = (int)Math.floor(preciseWidth + 0.000000001);
	height = (int)Math.floor(preciseHeight + 0.000000001);	
}

}
