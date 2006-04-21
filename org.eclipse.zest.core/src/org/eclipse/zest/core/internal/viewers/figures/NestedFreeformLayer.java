/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.viewers.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A special type of FreeformLayer which has a single NestedFigure.
 * 
 * @author Chris Callendar
 */
public class NestedFreeformLayer extends FreeformLayer {

	private NestedFigure nestedFigure;
	
	private Rectangle nestedBounds = null;
	

	
	public double getWidthScale() {
		return nestedFigure.getWidthScale();
	}
	
	public double getHeightScale() {
		return nestedFigure.getHeightScale();
	}
	
	/**
	 * Creates a new NestedFreeformLayer which contains a NestedFigure.
	 * @param nestedFigure
	 */
	public NestedFreeformLayer(NestedFigure nestedFigure) {
		super();
		this.nestedFigure = nestedFigure;
		
		this.setLayoutManager(new FreeformLayout());
		this.nestedBounds = new Rectangle(8, 20, -1, -1);
		super.add(nestedFigure, nestedBounds, 0);
	}
	
	
//	public Rectangle getBounds() {
//		// TODO Auto-generated method stub
//		return nestedFigure.getBounds().getCopy();
//	}
	
	
	/**
	 * Gets the nested figure which is added to this free form layer.
	 * @return NestedFigure
	 */
	public Figure getNestedFigure() {
		return nestedFigure;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
	 */
//	public void setBounds(Rectangle rect) {
//		super.setBounds(rect);
//		
//	}
	
	/**
	 * Adds the given child to the nested Figure instead of to this Figure.
	 * @see org.eclipse.draw2d.FreeformLayer#add(org.eclipse.draw2d.IFigure, java.lang.Object, int)
	 */
	public void add(IFigure child, Object constraint, int index) {
		nestedFigure.add(child, constraint, index);
	}
	
	/**
	 * Gets the area that nodes can be placed in a nested free form layer
	 */
	public Rectangle getClientArea(Rectangle rect) {
		//rect = nestedFigure.getClientArea( rect );
		super.getClientArea(rect);
		//rect.scale(nestedFigure.getWidthScale(), nestedFigure.getHeightScale());
		return rect;
	}
	

	public boolean isCoordinateSystem() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		if (!containsPoint(x, y))
			return null;
		if (search.prune(this))
			return null;
		IFigure child = findDescendantAtExcluding(x, y, search);
		if (child != null)
			return child;
		if (search.accept(this))
			return this;
		return null;
	}
	
	
	/**
	 * Resizes the figure.
	 * @param width
	 * @param height
	 */
	public Rectangle resize(int width, int height) {
		width = (width <= 0 ? -1 : width - 16);
		height= (height <= 0 ? -1 : height - 50);
		nestedBounds.setSize(width, height);
		getLayoutManager().setConstraint(nestedFigure, nestedBounds);
		
		return nestedBounds;
	}

}
