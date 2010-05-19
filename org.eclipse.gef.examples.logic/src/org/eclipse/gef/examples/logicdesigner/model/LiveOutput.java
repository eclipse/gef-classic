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
package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class LiveOutput extends SimpleOutput {

	private static Image LIVE_ICON = createImage(LiveOutput.class,
			"icons/live16.gif"); //$NON-NLS-1$
	static final long serialVersionUID = 1;

	public Image getIconImage() {
		return LIVE_ICON;
	}

	public boolean getResult() {
		return true;
	}

	public String toString() {
		return LogicMessages.LiveOutput_LabelText;
	}

}
