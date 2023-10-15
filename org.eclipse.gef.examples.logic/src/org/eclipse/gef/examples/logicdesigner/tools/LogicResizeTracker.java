/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
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

	@Override
	protected Dimension getMaximumSizeFor(ChangeBoundsRequest request) {
		return LogicPlugin.getMaximumSizeFor(getOwner().getModel().getClass());
	}

	@Override
	protected Dimension getMinimumSizeFor(ChangeBoundsRequest request) {
		return LogicPlugin.getMinimumSizeFor(getOwner().getModel().getClass());
	}
}
