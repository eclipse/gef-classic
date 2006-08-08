/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A layout used to place figures in an automatic grid.
 * @author Del Myers
 *
 */

//@tag bug(150585-TopArcs(fix)) : This layout is used in the client/supplier panes that are related to the top-level node in the nested graph.
public class FigureGridLayout extends FreeformLayout {
	public static final Comparator SimpleComparator = new SimpleStringComparator();
	
	
	private static final class SimpleStringComparator implements Comparator {
		public int compare(Object arg0, Object arg1) {
			return arg0.toString().compareTo(arg1.toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.XYLayout#layout(org.eclipse.draw2d.IFigure)
	 */
	public void layout(IFigure parent) {
		List children = parent.getChildren();
		IFigure[] figures = (IFigure[])children.toArray(new IFigure[children.size()]);
		Arrays.sort(figures, SimpleComparator);
		Rectangle[] boundss = layoutInBounds(parent.getBounds(), figures);
		for (int i = 0; i < boundss.length; i++) {
			figures[i].setBounds(boundss[i]);
		}
	}
	/**
	 * Convenience method for laying out the given figures within the given bounds.
	 * An optional comparator can be provided in order to guarantee the order that
	 * the figures will be layed out.
	 * @param bounds
	 * @param figures
	 * @param sorter
	 */
	public static Rectangle[] layoutInBounds(Rectangle bounds, IFigure[] figures) {
		if (figures.length <= 0) return new Rectangle[0];
		ArrayList boundss = new ArrayList();
		int cols = Math.max (1, (int)Math.ceil(Math.sqrt(figures.length)));
		int width = bounds.width/cols;
		int height = bounds.height/cols;
		int pad = 4;
		int index = 0;
		for (int i = 0; i < figures.length; i++) {
			int x = (index % cols)*width;
			int y = (index/cols)*height;
			Rectangle figureBounds = new Rectangle(bounds.x+x+pad, bounds.y+y+pad, width-2*pad, height-2*pad);
			boundss.add(figureBounds);
			index++;
		}
		return (Rectangle[])boundss.toArray(new Rectangle[boundss.size()]);
	}
}
