/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.flow.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.examples.flow.parts.DummyLayout;

/**
 * 
 * @author hudsonr Created on Jul 23, 2003
 */
public class SubgraphFigure extends Figure {

	IFigure contents;
	IFigure footer;
	IFigure header;

	public SubgraphFigure(IFigure header, IFigure footer) {
		contents = new Figure();
		contents.setLayoutManager(new DummyLayout());
		add(contents);
		add(this.header = header);
		add(this.footer = footer);
	}

	public IFigure getContents() {
		return contents;
	}

	public IFigure getFooter() {
		return footer;
	}

	public IFigure getHeader() {
		return header;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension dim = new Dimension();
		dim.width = getFooter().getPreferredSize().width;
		dim.width += getInsets().getWidth();
		dim.height = 50;
		return dim;
	}

	public void setBounds(Rectangle rect) {
		super.setBounds(rect);
		rect = Rectangle.SINGLETON;
		getClientArea(rect);
		contents.setBounds(rect);
		Dimension size = footer.getPreferredSize();
		footer.setLocation(rect.getBottomLeft().translate(0, -size.height));
		footer.setSize(size);

		size = header.getPreferredSize();
		header.setSize(size);
		header.setLocation(rect.getLocation());
	}

	public void setSelected(boolean value) {
	}

}
