/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
package org.eclipse.zest.core.widgets;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import org.eclipse.draw2d.IFigure;

/**
 * A Custom Graph Node
 */
// TODO zest 2.x: Move to org.eclipse.zest.core.widgets.custom
public class CGraphNode extends GraphNode {

	IFigure figure = null;

	public CGraphNode(IContainer graphModel, int style, IFigure figure) {
		super(graphModel, style, figure);
	}

	@Override
	public IFigure getFigure() {
		return super.getFigure();
	}

	@Override
	protected IFigure createFigureForModel() {
		this.figure = (IFigure) this.getData();
		return this.figure;
	}

	@Override
	public void setBackgroundColor(Color c) {
		getFigure().setBackgroundColor(c);
	}

	@Override
	public void setFont(Font font) {
		getFigure().setFont(font);
	}

	@Override
	public Color getBackgroundColor() {
		return getFigure().getBackgroundColor();
	}

	@Override
	public Font getFont() {
		return getFigure().getFont();
	}

	@Override
	public Color getForegroundColor() {
		return getFigure().getForegroundColor();
	}

	@Override
	protected void updateFigureForModel(IFigure currentFigure) {
		// Undefined
	}

}
