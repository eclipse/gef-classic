package org.eclipse.gef;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * @author Randy Hudson
 */
public class CompoundSnapToStrategy implements SnapToStrategy {

private SnapToStrategy[] delegates;

public CompoundSnapToStrategy(SnapToStrategy delegates[]) {
	Assert.isTrue(delegates.length != 0);
	this.delegates = delegates;
}

/**
 * @see SnapToStrategy#snapMoveRequest(ChangeBoundsRequest, PrecisionRectangle)
 */
public boolean snapMoveRequest(ChangeBoundsRequest request,	PrecisionRectangle baseRect) {
	for (int i = 0; i < delegates.length; i++) {
		if (delegates[i] != null && delegates[i].snapMoveRequest(request, baseRect.getPreciseCopy()))
			return true;
	}

	return false;
}

/**
 * @see org.eclipse.gef.SnapToStrategy#snapResizeRequest(org.eclipse.gef.requests.ChangeBoundsRequest,
 * org.eclipse.draw2d.geometry.PrecisionRectangle)
 */
public boolean snapResizeRequest(
	ChangeBoundsRequest req,
	PrecisionRectangle baseRect) {
	for (int i = 0; i < delegates.length; i++) {
		if (delegates[i].snapResizeRequest(req, baseRect.getPreciseCopy()))
			return true;
	}
	return false;
}

}
