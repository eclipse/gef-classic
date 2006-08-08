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

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.GuideLayer;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.SimpleRootEditPart;
import org.eclipse.mylar.zest.core.internal.gefx.ZestRootEditPart;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPart;

/**
 * Extends GraphRootEditPart to add zooming support.
 * Currently there are three methods of zoom: the first is to use the 
 * ZoomMananger and do "real" zooming.  The second is to fake the zooming by drawing an
 * expanding or collapsing rectangle around the object to give the impression of zooming.  The 
 * third way is to expand or collapse the current rectangle.
 * 
 * @author Chris Callendar
 */
public class NestedGraphRootEditPart extends SimpleRootEditPart
		implements LayerConstants, ZestRootEditPart, LayerManager {
	
	protected GraphEditPart graphEditPart = null;
	private LayeredPane innerLayers;
	private LayeredPane printableLayers;

	/**
	 * Initializes the root edit part with the given zoom style.
	 * This can be real zooming, fake zooming, or expand/collapse zooming.
	 */
	public NestedGraphRootEditPart( ) {
		super();
		
	}
	
	/**
	 * Gets the nested edit part
	 * TODO: Remove this once the zoom in on node is moved to the NestedGraphEditPart or panes below that
	 * @return
	 */
	public NestedGraphEditPart getNestedEditPart() {
		return (NestedGraphEditPart) getChildren().get(0);
	}
	
	/**
	 * The contents' Figure will be added to the PRIMARY_LAYER.
	 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane() {
		return getLayer(PRIMARY_LAYER);
	}

	/**
	 * The root editpart does not have a real model.  The LayerManager ID is returned so that
	 * this editpart gets registered using that key.
	 * @see org.eclipse.gef.EditPart#getModel()
	 */
	public Object getModel() {
		return LayerManager.ID;
	}
	
	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		innerLayers = new LayeredPane();
		createLayers(innerLayers);
		return innerLayers;
	}
	
	/**
	 * Creates the top-most set of layers on the given layered pane.
	 * @param layeredPane the parent for the created layers
	 */
	protected void createLayers(LayeredPane layeredPane) {
		layeredPane.add(new LayeredPane(), PRIMARY_LAYER);	
		layeredPane.add(new ConnectionLayer(), CONNECTION_LAYER);
		layeredPane.add(new ConnectionLayer(), CONNECTION_FEEDBACK_LAYER);
		layeredPane.add(new FreeformLayer(), HANDLE_LAYER);
		layeredPane.add(new FeedbackLayer(), FEEDBACK_LAYER);
		layeredPane.add(new GuideLayer(), GUIDE_LAYER);
	}
	
	

	/**
	 * Sets the main edit part for the model. You should be able to 
	 * fire changes off here and see the effect
	 */
	public void setModelRootEditPart(Object modelRootEditPart) {
		this.graphEditPart = (GraphEditPart) modelRootEditPart;
	}

	/**
	 * Returns the layer indicated by the key. Searches all layered panes.
	 * @see LayerManager#getLayer(Object)
	 */
	public IFigure getLayer(Object key) {
		if (innerLayers == null)
			return null;
		IFigure layer = innerLayers.getLayer(key);
		if (layer != null)
			return layer;
		if (printableLayers == null)
			return null;
		return printableLayers.getLayer(key);
	}
	
	class FeedbackLayer extends FreeformLayer {
		FeedbackLayer() {
			setEnabled(false);
		}
	}

	
}
