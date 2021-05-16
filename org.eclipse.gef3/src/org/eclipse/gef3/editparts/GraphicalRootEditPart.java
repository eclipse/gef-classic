/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.editparts;

import org.eclipse.draw2dl.ConnectionLayer;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.Layer;
import org.eclipse.draw2dl.LayeredPane;
import org.eclipse.draw2dl.ScrollPane;
import org.eclipse.draw2dl.StackLayout;
import org.eclipse.draw2dl.Viewport;
import org.eclipse.draw2dl.geometry.Dimension;

import org.eclipse.gef3.DragTracker;
import org.eclipse.gef3.EditPart;
import org.eclipse.gef3.EditPartViewer;
import org.eclipse.gef3.LayerConstants;
import org.eclipse.gef3.Request;
import org.eclipse.gef3.RootEditPart;
import org.eclipse.gef3.commands.Command;
import org.eclipse.gef3.commands.UnexecutableCommand;
import org.eclipse.gef3.tools.MarqueeDragTracker;

/**
 * Provides support for representation of any other graphical EditPart. It
 * contains Layers which are used to represent specific type of visual
 * information. The Layers are (1) Primary - Used to hold the main EditPart's
 * Figures. (2) Connection - Used to hold the connections between EditParts. (3)
 * Handle - Takes care of holding handles for EditParts. (4) Feedback - Shows
 * feedback information for the EditParts.
 * 
 * @deprecated this class will be deleted, use ScrollingGraphicalViewer with
 *             ScalableRootEditPart instead
 */
public class GraphicalRootEditPart extends AbstractGraphicalEditPart implements
		RootEditPart, LayerConstants, org.eclipse.gef3.editparts.LayerManager {

	/**
	 * The contents
	 */
	protected EditPart contents;
	/**
	 * the viewer
	 */
	protected EditPartViewer viewer;
	private LayeredPane innerLayers;
	private LayeredPane printableLayers;

	/**
	 * @see org.eclipse.gef3.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
	}

	/**
	 * @see org.eclipse.gef3.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		innerLayers = new LayeredPane();
		printableLayers = new LayeredPane();

		Layer layer = new Layer();
		layer.setLayoutManager(new StackLayout());
		printableLayers.add(layer, PRIMARY_LAYER);

		layer = new ConnectionLayer();
		layer.setPreferredSize(new Dimension(5, 5));
		printableLayers.add(layer, CONNECTION_LAYER);

		innerLayers.add(printableLayers, PRINTABLE_LAYERS);

		layer = new Layer();
		layer.setPreferredSize(new Dimension(5, 5));
		innerLayers.add(layer, HANDLE_LAYER);

		layer = new FeedbackLayer();
		layer.setPreferredSize(new Dimension(5, 5));
		innerLayers.add(layer, FEEDBACK_LAYER);

		ScrollPane pane = new ScrollPane();
		pane.setViewport(new Viewport(true));
		pane.setContents(innerLayers);

		return pane;
	}

	/**
	 * Returns the unexecutable command.
	 * 
	 * @see org.eclipse.gef3.EditPart#getCommand(org.eclipse.gef3.Request)
	 */
	public Command getCommand(Request req) {
		return UnexecutableCommand.INSTANCE;
	}

	/**
	 * @see org.eclipse.gef3.RootEditPart#getContents()
	 */
	public EditPart getContents() {
		return contents;
	}

	/**
	 * Should never be called.
	 * 
	 * @see org.eclipse.gef3.EditPart#getDragTracker(org.eclipse.gef3.Request)
	 */
	public DragTracker getDragTracker(Request req) {
		// The drawing cannot be dragged.
		return new MarqueeDragTracker();
	}

	/**
	 * @see org.eclipse.gef3.editparts.LayerManager#getLayer(java.lang.Object)
	 */
	public IFigure getLayer(Object key) {
		if (innerLayers == null)
			return null;

		IFigure layer = printableLayers.getLayer(key);
		if (layer != null)
			return layer;
		return innerLayers.getLayer(key);
	}

	/**
	 * Returns the primary layer, which will parent the contents editpart.
	 * 
	 * @see org.eclipse.gef3.GraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane() {
		return getLayer(PRIMARY_LAYER);
	}

	/**
	 * @see org.eclipse.gef3.EditPart#getModel()
	 */
	public Object getModel() {
		return LayerManager.ID;
	}

	/**
	 * Returns <code>this</code>.
	 * 
	 * @see org.eclipse.gef3.EditPart#getRoot()
	 */
	public RootEditPart getRoot() {
		return this;
	}

	/**
	 * @see org.eclipse.gef3.EditPart#getViewer()
	 */
	public EditPartViewer getViewer() {
		return viewer;
	}

	/**
	 * Overridden to do nothing since the child is explicitly set.
	 * 
	 * @see AbstractEditPart#refreshChildren()
	 */
	protected void refreshChildren() {
	}

	/**
	 * @see org.eclipse.gef3.RootEditPart#setContents(org.eclipse.gef3.EditPart)
	 */
	public void setContents(EditPart editpart) {
		if (contents != null)
			removeChild(contents);
		contents = editpart;
		if (contents != null)
			addChild(contents, 0);
	}

	/**
	 * @see org.eclipse.gef3.RootEditPart#setViewer(org.eclipse.gef3.EditPartViewer)
	 */
	public void setViewer(EditPartViewer newViewer) {
		if (viewer == newViewer)
			return;
		if (viewer != null)
			unregister();
		viewer = newViewer;
		if (viewer != null)
			register();
	}

	class FeedbackLayer extends Layer {
		FeedbackLayer() {
			setEnabled(false);
		}
	}

}
