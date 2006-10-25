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
package org.eclipse.mylar.zest.core.internal.graphviewer.overview.parts;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.tools.OverviewRectangleTool;

/**
 * A simple root edit part that allows the viewport to be sized down.
 * @author Del Myers
 *
 */
public class OverviewRootEditPart extends ScalableRootEditPart {
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.ScalableRootEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		final Viewport figure = (Viewport) super.createFigure();
		figure.setContentsTracksHeight(true);
		figure.setContentsTracksWidth(true);
		figure.getContents().setLayoutManager(new StackLayout(){
			public Dimension getMinimumSize(IFigure container, int w, int h) {
				return new Dimension(w,h);
			}
			/* (non-Javadoc)
			 * @see org.eclipse.draw2d.StackLayout#layout(org.eclipse.draw2d.IFigure)
			 */
			public void layout(IFigure parent) {
				List children = parent.getChildren();
				RangeModel vertical = figure.getVerticalRangeModel();
				RangeModel horizontal = figure.getHorizontalRangeModel();
				for (Iterator i = children.iterator(); i.hasNext();) {
					IFigure child = (IFigure) i.next();
					child.setBounds(new Rectangle(0,0,horizontal.getMaximum(), vertical.getMaximum()));
					
				}
			}
		});
		return figure;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.ScalableRootEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request req) {
		return new OverviewRectangleTool();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		return this;
	}
}
