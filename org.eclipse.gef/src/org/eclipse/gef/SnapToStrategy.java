package org.eclipse.gef;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * @author Randy Hudson
 */
public interface SnapToStrategy {

/**
 * Returns the snapped drag delta based on the given parameters.  The receiver may modify
 * and return the same instance of Point which was passed in.  The receiver may modify the
 * baseRect parameter for whatever purpose.
 * 
 * @param raw the un-snapped drag delta
 * @param baseRect a copy of the base rectangle which is being dragged
 * @return the snapped drag delta
 */
void snapMoveRequest(ChangeBoundsRequest req, PrecisionRectangle baseRect);

void snapResizeRequest(ChangeBoundsRequest req, PrecisionRectangle baseRec);

}
