package org.eclipse.gef;

import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * @author Randy Hudson
 */
public interface SnapToStrategy {

/**
 * Returns <code>true</code> if the request was modified or "snapped".  The receiver may
 * modify and return the same instance of Point which was passed in.  The receiver may
 * modify the baseRect parameter for whatever purpose.
 * @param request request
 * @return <code>true</code> if the request was snapped
 */
boolean snapMoveRequest(ChangeBoundsRequest request, PrecisionRectangle baseRect);

boolean snapResizeRequest(ChangeBoundsRequest req, PrecisionRectangle baseRec);

}
