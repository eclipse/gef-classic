/*******************************************************************************
 * Copyright (c) 2011, 2024 Stephan Schwiebert and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Stephan Schwiebert - initial API and implementation
 ******************************************************************************/
package org.eclipse.zest.cloudio;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;

import org.eclipse.core.runtime.Assert;
import org.eclipse.zest.cloudio.util.RectTree;

/**
 * Helper class which stores all data required to render an element.
 *
 * @author sschwieb
 *
 */
public class Word {

	public Word(String string) {
		this.string = string;
	}

	public final String string;

	public double weight;

	public int x;

	public int y;

	private Color color;

	public RectTree tree;

	public float angle;

	private FontData[] fontData;

	public FontData[] getFontData() {
		return fontData;
	}

	public void setFontData(FontData[] fontData) {
		Assert.isLegal(fontData != null, "FontData-Array must not be null!");
		this.fontData = fontData.clone();
	}

	public short id;

	public int height;

	public int width;

	public Object data;

	public Point stringExtent;

	@Override
	public String toString() {
		return string;
	}

	public void setColor(Color color) {
		Assert.isLegal(color != null, "Color must not be null!");
		Assert.isLegal(!color.isDisposed(), "Color is disposed!");
		this.color = color;
	}

	public Color getColor() {
		Assert.isLegal(color != null, "Color must not be null!");
		Assert.isLegal(!color.isDisposed(), "Color is disposed!");
		return color;
	}

}
