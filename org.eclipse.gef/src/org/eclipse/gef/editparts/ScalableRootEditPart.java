/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editparts;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.*;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.tools.MarqueeDragTracker;

/**
 * A Scalable RootEditPart
 * @author Eric Bordeau
 * @since 2.1.1
 */
public class ScalableRootEditPart
	extends AbstractGraphicalEditPart
	implements RootEditPart, LayerConstants, LayerManager
{

class FeedbackLayer
	extends Layer
{
	FeedbackLayer() {
		setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		Rectangle rect = new Rectangle();
		for (int i = 0; i < getChildren().size(); i++)
			rect.union(((IFigure)getChildren().get(i)).getBounds());
		return rect.getSize();
	}

}
protected EditPart contents;
private LayeredPane innerLayers;
private LayeredPane printableLayers;

private ScalableLayeredPane scaledLayers;
protected EditPartViewer viewer;
private ZoomManager zoomManager;

/**
 * Constructor for ScalableFreeformRootEditPart
 */
public ScalableRootEditPart() {
	zoomManager =
		new ZoomManager((ScalableLayeredPane)getScaledLayers(), ((Viewport)getFigure()));
}

protected void createEditPolicies() {}

protected IFigure createFigure() {
	Viewport viewport = createViewport();

	innerLayers = new LayeredPane();
	createLayers(innerLayers);

	viewport.setContents(innerLayers);
	return viewport;
}

/**
 * @see org.eclipse.gef.ui.parts.FreeformGraphicalRootEditPart#createLayers(LayeredPane)
 */
protected void createLayers(LayeredPane layeredPane) {
	layeredPane.add(getScaledLayers(), SCALABLE_LAYERS);
	layeredPane.add(new Layer(){
		public Dimension getPreferredSize(int wHint, int hHint) {
			return new Dimension();
		}

	}, HANDLE_LAYER);
	layeredPane.add(new FeedbackLayer(), FEEDBACK_LAYER);
}

protected LayeredPane createPrintableLayers() {
	printableLayers = new LayeredPane();
	
	Layer layer = new Layer();
	layer.setLayoutManager(new StackLayout());
	printableLayers.add(layer, PRIMARY_LAYER);

	layer = new ConnectionLayer();
	layer.setPreferredSize(new Dimension(5,5));
	printableLayers.add(layer, CONNECTION_LAYER);
	
	return printableLayers;
}

/**
 * Creates and returns the scalable layers of this EditPart
 * 
 * @return ScalableFreeformLayeredPane Pane that contains the scalable layers
 */
protected ScalableLayeredPane createScaledLayers() {
	ScalableLayeredPane layers = new ScalableLayeredPane();
	layers.add(getPrintableLayers(), PRINTABLE_LAYERS);
	return layers;
}

protected Viewport createViewport() {
	return new Viewport(true);
}

/** 
 * Doesnt provide any command support, returns an unexecutable command.
 */
public Command getCommand(Request req) {
	return UnexecutableCommand.INSTANCE;
}

/**
 * Returns the figure to which childrens' figures will be added.
 * An example would be a ScrollPane.  Figures of child editpart are not
 * added to the ScrollPane, but to its ViewPort's View.
 */
public IFigure getContentPane() {
	return getLayer(PRIMARY_LAYER);
}

public EditPart getContents() {
	return contents;
}

/**
 * Return a drag tracker suitable for dragging this.
 */
public DragTracker getDragTracker(Request req) {
	// The drawing cannot be dragged.
	return new MarqueeDragTracker();
}

/**
 * @see org.eclipse.gef.ui.parts.FreeformGraphicalRootEditPart#getLayer(Object)
 */
public IFigure getLayer(Object key) {
	if (innerLayers == null)
		return null;
	IFigure layer = scaledLayers.getLayer(key);
	if (layer != null)
		return layer;
	layer = printableLayers.getLayer(key);
	if (layer != null)
		return layer;	
	return innerLayers.getLayer(key);
}

/**
 * Returns the model of this EditPart. 
 */
public Object getModel() {
	return LayerManager.ID;
}

protected LayeredPane getPrintableLayers() {
	if (printableLayers == null)
		printableLayers = createPrintableLayers();
	return printableLayers;
}

/**
 * Return this, as this is the root EditPart.
 * @return Root EditPart
 */
public RootEditPart getRoot() {
	return this;
}

/**
 * Returns the scalable layers of this EditPart
 * @return LayeredPane
 */
protected LayeredPane getScaledLayers() {
	if (scaledLayers == null)
		scaledLayers = createScaledLayers();
	return scaledLayers;
}

/**
 * Return the EditorView for this.
 * @param EditorView  The viewer for the Root.
 */
public EditPartViewer getViewer() {
	return viewer;
}

/**
 * Returns the zoomManager.
 * @return ZoomManager
 */
public ZoomManager getZoomManager() {
	return zoomManager;
}

public void refresh() {}

protected void refreshChildren() {}

/**
 * Sets the contents.  The root contains a single child, called it's contents.
 */
public void setContents(EditPart editpart) {
	if (contents != null)
		removeChild(contents);
	contents = editpart;
	if (contents != null)
		addChild(contents,0);
}

/**
 * Sets the viewer.
 * @param viewer EditPartViewer.
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

}