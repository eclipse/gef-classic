package org.eclipse.gef.examples.logicdesigner.tools;

import org.eclipse.draw2d.geometry.Dimension;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;

import org.eclipse.gef.examples.logicdesigner.LogicPlugin;

public final class LogicResizeTracker extends ResizeTracker {

	public LogicResizeTracker(GraphicalEditPart owner, int direction) {
		super(owner, direction);
	}

	protected Dimension getMaximumSizeFor(ChangeBoundsRequest request) {
		return LogicPlugin.getMaximumSizeFor(getOwner().getModel().getClass());
	}

	protected Dimension getMinimumSizeFor(ChangeBoundsRequest request) {
		return LogicPlugin.getMinimumSizeFor(getOwner().getModel().getClass());
	}
}
