package org.eclipse.gef;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;

/**
 * @author Pratik Shah
 */
public class CompoundSnapToHelper 
	extends SnapToHelper 
{

private SnapToHelper[] delegates;

public CompoundSnapToHelper(SnapToHelper delegates[]) {
	Assert.isTrue(delegates.length != 0);
	this.delegates = delegates;
}

public int snapLocation(Request request, PrecisionPoint location, 
		PrecisionPoint result, int snapOrientation) {
	for (int i = 0; i < delegates.length && snapOrientation != NONE; i++)
		snapOrientation = delegates[i].snapLocation(request, location, result, 
				snapOrientation);
	return snapOrientation;
}

public int snapRectangle(Request request, PrecisionRectangle baseRect, 
		PrecisionRectangle result, boolean canResize, int snapOrientation) {
	for (int i = 0; i < delegates.length && snapOrientation != NONE; i++)
		snapOrientation = delegates[i].snapRectangle(request, baseRect, result, canResize,
				snapOrientation);
	return snapOrientation;
}

}