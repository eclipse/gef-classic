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
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPart;
import org.eclipse.mylar.zest.core.internal.viewers.figures.NestedFreeformLayer;


/**
 * Extends GraphEditPart to allow moving and resizing of nodes.
 * 
 * @author Chris Callendar
 */
public class NestedGraphEditPart extends GraphEditPart  {
	
	/**
	 * Initializes the edit part.
	 * @param allowOverlap If nodes are allowed to overlap
	 * @param enforceBounds If nodes can be moved outside the bounds  If this is set to false
	 * then scrollbars will appear.
	 */
	public NestedGraphEditPart( ) {
		super();
	}	
	
	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate();
			((GraphItem)getCastedModel().getRootNode()).addPropertyChangeListener(this);
		}
	}	
	
	/**
	 * Upon deactivation, detach from the model element as a property change listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((GraphItem)getCastedModel().getRootNode()).removePropertyChangeListener(this);
		}
	}	

	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.viewers.parts.GraphEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {	
		// This edit part is just a free form layer, that holds 1 full size node
	}
	
	//TODO: should we change this to getNestedGraphModel?
	// and make the same method in GraphEditPart called getGraphModel?
	public NestedGraphModel getCastedModel() {
		return (NestedGraphModel)getModel();
	}	
	

	/**
	 * Resize the single figure on this layer and the 1 figure of the current selected nod
	 * @param width
	 * @param height
	 */
	public void resize( int width, int height ) {
		getFigure().setSize(width, height);
		NestedFreeformLayer freeformLayer = (NestedFreeformLayer)((NestedGraphNodeEditPart)getCastedModel().getRootNode().getEditPart()).getFigure();
		freeformLayer.resize(width, height);	
	}
	
	/**
	 * Creates a NestedFreeformLayer which contains the root NestedFigure.
	 * This NestedFigure will have an up button in the top left if the
	 * current node isn't the root node.
	 */
	protected IFigure createFigure() {	
		Figure figure = new FreeformLayer();
		figure.addLayoutListener(LayoutAnimator.getDefault());
		figure.setOpaque(false);
		return figure;
	}		
}
