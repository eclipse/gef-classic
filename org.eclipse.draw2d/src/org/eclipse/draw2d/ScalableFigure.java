package org.eclipse.draw2d;

/**
 * A figure that can be scaled.
 * @author Eric Bordeau
 * @since 2.1.1
 */
public interface ScalableFigure extends IFigure {

/**
 * Returns the current scale.
 * @return the current scale
 */
double getScale();

/**
 * Sets the new scale factor.
 * @param scale the scale
 */
void setScale(double scale);

}
