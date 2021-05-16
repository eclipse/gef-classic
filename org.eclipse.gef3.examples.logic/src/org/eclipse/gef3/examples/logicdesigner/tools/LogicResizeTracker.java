package org.eclipse.gef3.examples.logicdesigner.tools;

import org.eclipse.draw2dl.geometry.Dimension;

import org.eclipse.gef3.GraphicalEditPart;
import org.eclipse.gef3.requests.ChangeBoundsRequest;
import org.eclipse.gef3.tools.ResizeTracker;

import org.eclipse.gef3.examples.logicdesigner.LogicPlugin;

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
