package org.eclipse.gef;

import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author Randy Hudson
 */
public interface SnapToHelper {

public static final int SNAP_HORIZONTAL = 1;
public static final int SNAP_VERTICAL = 2;

int snapCreateRequest(CreateRequest request, PrecisionRectangle baseRect, 
                      int snapOrientation);

/**
 * Returns <code>true</code> if the request was modified or "snapped".  The receiver may
 * modify and return the same instance of Point which was passed in.  The receiver may
 * modify the baseRect parameter for whatever purpose.
 * @param request request
 * @return <code>true</code> if the request was snapped
 */
int snapMoveRequest(ChangeBoundsRequest request, PrecisionRectangle baseRect,
                    PrecisionRectangle compoundRect, int snapOrientation);

int snapResizeRequest(ChangeBoundsRequest req, PrecisionRectangle baseRec,
                      int snapOrientation);

}