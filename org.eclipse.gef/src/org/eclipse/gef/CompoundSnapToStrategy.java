package org.eclipse.gef;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author Randy Hudson
 */
public class CompoundSnapToStrategy implements SnapToHelper {

private SnapToHelper[] delegates;

public CompoundSnapToStrategy(SnapToHelper delegates[]) {
	Assert.isTrue(delegates.length != 0);
	this.delegates = delegates;
}

public int snapCreateRequest(CreateRequest request, PrecisionRectangle baseRect,
		int snapOrientation) {
	int i = 0;
	while (snapOrientation != 0 && i < delegates.length) {
		if (baseRect == null) {
			baseRect = new PrecisionRectangle(
					new Rectangle(request.getLocation(), request.getSize()));
		}	
		if (delegates[i] != null)
			snapOrientation = delegates[i].snapCreateRequest(request, 
					baseRect, snapOrientation);
		baseRect = null;
		i++;
	}
		
	return snapOrientation;
}

/**
 * @see SnapToHelper#snapMoveRequest(ChangeBoundsRequest, PrecisionRectangle)
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
 * @see org.eclipse.gef.SnapToHelper#snapResizeRequest(org.eclipse.gef.requests.ChangeBoundsRequest,
 * org.eclipse.draw2d.geometry.PrecisionRectangle)
 */
public int snapResizeRequest(ChangeBoundsRequest req, PrecisionRectangle baseRect,
                             int snapOrientation) {
	int i = 0;
	while (snapOrientation != 0 && i < delegates.length) {
		if (delegates[i] != null)
			snapOrientation = delegates[i].snapResizeRequest(req, 
					baseRect.getPreciseCopy(), snapOrientation);
		i++;
	}
		
	return snapOrientation;
}

}