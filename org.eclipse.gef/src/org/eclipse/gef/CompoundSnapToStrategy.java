package org.eclipse.gef;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author Randy Hudson
 */
public class CompoundSnapToStrategy implements SnapToStrategy {

private SnapToStrategy[] delegates;

public CompoundSnapToStrategy(SnapToStrategy delegates[]) {
	Assert.isTrue(delegates.length != 0);
	this.delegates = delegates;
}

public int snapCreateRequest(CreateRequest request, PrecisionRectangle baseRect,
		int snapOrientation) {
	int i = 0;
	while (snapOrientation != 0 && i < delegates.length) {
		if (delegates[i] != null)
			snapOrientation = delegates[i].snapCreateRequest(request, 
					baseRect.getPreciseCopy(), snapOrientation);
		i++;
	}
		
	return snapOrientation;
}

/**
 * @see SnapToStrategy#snapMoveRequest(ChangeBoundsRequest, PrecisionRectangle)
 */
public int snapMoveRequest(ChangeBoundsRequest request,	PrecisionRectangle baseRect,
                           PrecisionRectangle selectionRect, int snapOrientation) {
	int i = 0;
	while (snapOrientation != 0 && i < delegates.length) {
		if (delegates[i] != null)
			snapOrientation = delegates[i].snapMoveRequest(request, 
					baseRect.getPreciseCopy(), selectionRect.getPreciseCopy(), 
					snapOrientation);
		i++;
	}

	return snapOrientation;
}

/**
 * @see org.eclipse.gef.SnapToStrategy#snapResizeRequest(org.eclipse.gef.requests.ChangeBoundsRequest,
 * org.eclipse.draw2d.geometry.PrecisionRectangle)
 */
public int snapResizeRequest(ChangeBoundsRequest req, PrecisionRectangle baseRect,
                             PrecisionRectangle selectionRect, int snapOrientation) {
	int i = 0;
	while (snapOrientation != 0 && i < delegates.length) {
		if (delegates[i] != null)
			snapOrientation = delegates[i].snapResizeRequest(req, 
					baseRect.getPreciseCopy(), selectionRect.getPreciseCopy(), 
					snapOrientation);
		i++;
	}
		
	return snapOrientation;
}

}