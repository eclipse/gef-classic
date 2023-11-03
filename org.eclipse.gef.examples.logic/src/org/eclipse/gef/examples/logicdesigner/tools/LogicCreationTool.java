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

import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.tools.CreationTool;

import org.eclipse.gef.examples.logicdesigner.LogicPlugin;

/**
 * Creation tool that ensures size constraints for size-on-drop
 *
 * @author anyssen
 *
 */
public class LogicCreationTool extends CreationTool {

	@Override
	protected Dimension getMaximumSizeFor(CreateRequest request) {
		return LogicPlugin.getMaximumSizeFor(request.getNewObject().getClass());
	}

	@Override
	protected Dimension getMinimumSizeFor(CreateRequest request) {
		return LogicPlugin.getMinimumSizeFor(request.getNewObject().getClass());
	}
}