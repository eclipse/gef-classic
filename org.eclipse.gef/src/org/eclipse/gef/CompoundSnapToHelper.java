package org.eclipse.gef;

import org.eclipse.jface.util.Assert;

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

public int snapRectangle(Request request, int snapOrientation, 
		PrecisionRectangle baseRect, PrecisionRectangle result) {
	for (int i = 0; i < delegates.length && snapOrientation != NONE; i++)
		snapOrientation = delegates[i].snapRectangle(request, snapOrientation,
				baseRect, result);
	return snapOrientation;
}

}