/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.examples.logicdesigner.tools;

import org.eclipse.draw2dl.geometry.Dimension;

import org.eclipse.gef3.requests.CreateRequest;
import org.eclipse.gef3.tools.CreationTool;

import org.eclipse.gef3.examples.logicdesigner.LogicPlugin;

/**
 * Creation tool that ensures size constraints for size-on-drop
 * 
 * @author anyssen
 * 
 */
public class LogicCreationTool extends CreationTool {

	protected Dimension getMaximumSizeFor(CreateRequest request) {
		return LogicPlugin.getMaximumSizeFor(request.getNewObject().getClass());
	}

	protected Dimension getMinimumSizeFor(CreateRequest request) {
		return LogicPlugin.getMinimumSizeFor(request.getNewObject().getClass());
	}
}