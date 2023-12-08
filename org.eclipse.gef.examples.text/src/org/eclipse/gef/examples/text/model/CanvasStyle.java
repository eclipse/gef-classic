/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
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

package org.eclipse.gef.examples.text.model;

import org.eclipse.swt.widgets.Control;

public class CanvasStyle extends Style {

	private static final long serialVersionUID = 1;
	private transient final Control canvas;

	public CanvasStyle(Control canvas) {
		this.canvas = canvas;
	}

	@Override
	public String getFontFamily() {
		return canvas.getFont().getFontData()[0].getName();
	}

	@Override
	public int getFontHeight() {
		return canvas.getFont().getFontData()[0].getHeight();
	}

}
