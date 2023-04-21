/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.zest.core.widgets;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * A Custom Graph Node
 */
public class CGraphNode extends GraphNode {

	IFigure figure = null;

	public CGraphNode(IContainer graphModel, int style, IFigure figure) {
		super(graphModel, style, figure);
	}

	public IFigure getFigure() {
		return super.getFigure();
	}

	protected IFigure createFigureForModel() {
		this.figure = (IFigure) this.getData();
		return this.figure;
	}

	public void setBackgroundColor(Color c) {
		getFigure().setBackgroundColor(c);
	}

	public void setFont(Font font) {
		getFigure().setFont(font);
	}

	public Color getBackgroundColor() {
		return getFigure().getBackgroundColor();
	}

	public Font getFont() {
		return getFigure().getFont();
	}

	public Color getForegroundColor() {
		return getFigure().getForegroundColor();
	}

	protected void updateFigureForModel(IFigure currentFigure) {
		// Undefined
	}

}
