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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.tools.MarqueeDragTracker;

/**
 * A graphical root composed of regular {@link org.eclipse.draw2d.Layer Layers}. The
 * layers are added to {@link org.eclipse.draw2d.LayeredPane} or {@link
 * org.eclipse.draw2d.ScalableLayeredPane}.  All layers are positioned by {@link
 * org.eclipse.draw2d.StackLayout}s, which means that the diagrams preferred size is the
 * union of the preferred size of each layer, and all layers will be positioned to fill
 * the entire diagram.
 * <P>
 * <EM>IMPORTANT</EM>ScalableRootEditPart uses a <code>Viewport</code> as its primary
 * figure. It must be used with a {@link
 * org.eclipse.gef.ui.parts.ScrollingGraphicalViewer}. The viewport gets installed into
 * that viewer's {@link org.eclipse.draw2d.FigureCanvas}, which provides native scrollbars
 * for scrolling the viewport.
 * <P>
 * The layer structure (top-to-bottom) for this root is:
 * <table cellspacing="0" cellpadding="0">
 *   <tr>
 *     <td colspan="4">Root Layered Pane</td>
 *   </tr>
 *   <tr>
 *     <td>&#9500;</td>
 *     <td colspan="3">&nbsp;Feedback Layer</td>
 *   </tr>
 *   <tr>
 *     <td>&#9500;</td>
 *     <td colspan="3">&nbsp;Handle Layer</td>
 *   </tr>
 *   <tr>
 *     <td>&#9492;</td>
 *     <td colspan="2">&nbsp;Scalable Layers</td>
 *     <td>({@link ScalableLayeredPane})</td>
 *   </tr>
 *   <tr>
 *     <td>&nbsp;</td>
 *     <td>&#9492;</td>
 *     <td colspan="2">Printable Layers</td>
 *   </tr>
 *   <tr>
 *     <td>&nbsp;</td>
 *     <td>&nbsp;</td>
 *     <td colspan="2">&#9500; Connection Layer</td>
 *   </tr>
 *   <tr>
 *     <td>&nbsp;</td>
 *     <td>&nbsp;</td>
 *     <td>&#9492;&nbsp;Primary Layer</td>
 *     <td>&nbsp;</td>
 *   </tr>
 * </table>
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

/**
 * The contents editpart.
 * @deprecated call getContents()
 */
protected EditPart contents;
private LayeredPane innerLayers;
private LayeredPane printableLayers;
private ScalableLayeredPane scaledLayers;
private PropertyChangeListener gridListener = new PropertyChangeListener() {
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (property.equals(SnapToGrid.PROPERTY_GRID_ORIGIN)
				|| property.equals(SnapToGrid.PROPERTY_GRID_SPACING)
				|| property.equals(SnapToGrid.PROPERTY_GRID_ENABLED))
			updateGridProperties();
	}
};

/**
 * The viewer.
 * @deprecated call getViewer() to access
 */
protected EditPartViewer viewer;
private ZoomManager zoomManager;

/**
 * Constructor for ScalableFreeformRootEditPart
 */
public ScalableRootEditPart() {
	zoomManager =
		new ZoomManager((ScalableLayeredPane)getScaledLayers(), ((Viewport)getFigure()));
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
 */
protected void createEditPolicies() { }

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	Viewport viewport = createViewport();

	innerLayers = new LayeredPane();
	createLayers(innerLayers);

	viewport.setContents(innerLayers);
	return viewport;
}

protected GridLayer createGridLayer() {
	return new GridLayer();
}

/**
 * @see FreeformGraphicalRootEditPart#createLayers(LayeredPane)
 */
protected void createLayers(LayeredPane layeredPane) {
	layeredPane.add(getScaledLayers(), SCALABLE_LAYERS);
	layeredPane.add(new Layer() {
		public Dimension getPreferredSize(int wHint, int hHint) {
			return new Dimension();
		}
	}, HANDLE_LAYER);
	layeredPane.add(new FeedbackLayer(), FEEDBACK_LAYER);
	layeredPane.add(new GuideLayer(), GUIDE_LAYER);
}

/**
 * Creates a layered pane and the layers that should be printed.
 * @see org.eclipse.gef.print.PrintGraphicalViewerOperation
 * @return a new LayeredPane containing the printable layers
 */
protected LayeredPane createPrintableLayers() {
	LayeredPane pane = new LayeredPane();
	
	Layer layer = new Layer();
	layer.setLayoutManager(new StackLayout());
	pane.add(layer, PRIMARY_LAYER);

	layer = new ConnectionLayer();
	layer.setPreferredSize(new Dimension(5, 5));
	pane.add(layer, CONNECTION_LAYER);
	
	return pane;
}

/**
 * Creates a scalable layered pane and the layers that should be scaled.
 * @return a new <code>ScalableLayeredPane</code> containing the scalable layers
 */
protected ScalableLayeredPane createScaledLayers() {
	ScalableLayeredPane layers = new ScalableLayeredPane();
	layers.add(createGridLayer(), GRID_LAYER);
	layers.add(getPrintableLayers(), PRINTABLE_LAYERS);
	return layers;
}

/**
 * Constructs the viewport that will be used to contain all of the layers.
 * @return a new Viewport
 */
protected Viewport createViewport() {
	return new Viewport(true);
}

/**
 * The RootEditPart should never be asked for a command. The implementation returns an
 * unexecutable command.
 * @see org.eclipse.gef.EditPart#getCommand(org.eclipse.gef.Request)
 */
public Command getCommand(Request req) {
	return UnexecutableCommand.INSTANCE;
}

/**
 * The contents' Figure will be added to the PRIMARY_LAYER.
 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
 */
public IFigure getContentPane() {
	return getLayer(PRIMARY_LAYER);
}

/**
 * @see org.eclipse.gef.RootEditPart#getContents()
 */
public EditPart getContents() {
	return contents;
}

/**
 * Should not be called, but returns a MarqeeDragTracker for good measure.
 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
 */
public DragTracker getDragTracker(Request req) {
	/* 
	 * The root will only be asked for a drag tracker if for some reason the contents
	 * editpart says it is neither selector nor opaque.
	 */
	return new MarqueeDragTracker();
}

/**
 * Returns the layer indicated by the key. Searches all layered panes.
 * @see LayerManager#getLayer(Object)
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
 * The root editpart does not have a real model.  The LayerManager ID is returned so that
 * this editpart gets registered using that key.
 * @see org.eclipse.gef.EditPart#getModel()
 */
public Object getModel() {
	return LayerManager.ID;
}

/**
 * Returns the LayeredPane that should be used during printing. This layer will be
 * identified using {@link LayerConstants#PRINTABLE_LAYERS}.
 * @return the layered pane containing all printable content
 */
protected LayeredPane getPrintableLayers() {
	if (printableLayers == null)
		printableLayers = createPrintableLayers();
	return printableLayers;
}

/**
 * Returns <code>this</code>.
 * @see org.eclipse.gef.EditPart#getRoot()
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
 * Returns the viewer that was set.
 * @see org.eclipse.gef.EditPart#getViewer()
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

/**
 * Overridden to do nothing, child is set using setContents(EditPart)
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
 */
protected void refreshChildren() { }

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractEditPart#register()
 */
protected void register() {
	super.register();
	viewer.setProperty(ZoomManager.class.toString(), getZoomManager());
	if (getLayer(GRID_LAYER) != null) {
		getViewer().addPropertyChangeListener(gridListener);
		updateGridProperties();
	}
}

/**
 * @see org.eclipse.gef.RootEditPart#setContents(org.eclipse.gef.EditPart)
 */
public void setContents(EditPart editpart) {
	if (contents != null)
		removeChild(contents);
	contents = editpart;
	if (contents != null)
		addChild(contents, 0);
}

/**
 * Sets the EditPartViewer.
 * @param newViewer the viewer
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

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractEditPart#unregister()
 */
protected void unregister() {
	getViewer().removePropertyChangeListener(gridListener);
	super.unregister();
	getViewer().setProperty(ZoomManager.class.toString(), null);
}

protected void updateGridProperties() {
	GridLayer grid = (GridLayer)getLayer(GRID_LAYER);
	boolean visible = false;
	Boolean val = (Boolean)getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
	if (val != null)
		visible = val.booleanValue();
	grid.setVisible(visible);
	grid.setOrigin((Point)getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ORIGIN));
	grid.setSpacing((Dimension)getViewer().getProperty(SnapToGrid.PROPERTY_GRID_SPACING));
}

}