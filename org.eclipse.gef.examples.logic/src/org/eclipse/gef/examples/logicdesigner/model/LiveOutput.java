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
package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class LiveOutput extends SimpleOutput {

	private static final Image LIVE_ICON = createImage(LiveOutput.class, "icons/live16.gif"); //$NON-NLS-1$
	static final long serialVersionUID = 1;

	@Override
	public Image getIconImage() {
		return LIVE_ICON;
	}

	@Override
	public boolean getResult() {
		return true;
	}

	@Override
	public String toString() {
		return LogicMessages.LiveOutput_LabelText;
	}

}
