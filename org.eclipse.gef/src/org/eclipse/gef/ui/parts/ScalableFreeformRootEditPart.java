package org.eclipse.gef.ui.parts;

import org.eclipse.draw2d.*;

/**
 * @author hudsonr
 * @since 2.1
 */
public class ScalableFreeformRootEditPart
	extends FreeformGraphicalRootEditPart
{

private ScalableFreeformLayeredPane scaledLayers;
private ZoomManager zoomManager;

/**
 * Constructor for ScalableFreeformRootEditPart
 */
public ScalableFreeformRootEditPart() {
	zoomManager = new ZoomManager((ScalableFreeformLayeredPane)getScaledLayers(),
									((Viewport)getFigure()));
}

/**
 * @see org.eclipse.gef.ui.parts.FreeformGraphicalRootEditPart#createLayers(LayeredPane)
 */
protected void createLayers(LayeredPane layeredPane) {
	layeredPane.add(getScaledLayers(), "$Scaled Layers");//$NON-NLS-1$
	layeredPane.add(new FreeformLayer(), HANDLE_LAYER);
	layeredPane.add(new FeedbackLayer(), FEEDBACK_LAYER);
}

/**
 * Creates and returns the scalable layers of this EditPart
 * 
 * @return ScalableFreeformLayeredPane Pane that contains the scalable layers */
protected ScalableFreeformLayeredPane createScaledLayers() {
	ScalableFreeformLayeredPane layers = new ScalableFreeformLayeredPane();
	layers.add(getPrintableLayers(), PRINTABLE_LAYERS);
	return layers;
}

/**
 * @see org.eclipse.gef.ui.parts.FreeformGraphicalRootEditPart#getLayer(Object)
 */
public IFigure getLayer(Object key) {
	IFigure layer = scaledLayers.getLayer(key);
	if (layer != null)
		return layer;
	return super.getLayer(key);
}

/**
 * Returns the scalable layers of this EditPart
 * @return LayeredPane */
protected LayeredPane getScaledLayers() {
	if (scaledLayers == null)
		scaledLayers = createScaledLayers();
	return scaledLayers;
}

/**
 * Returns the zoomManager.
 * @return ZoomManager
 */
public ZoomManager getZoomManager() {
	return zoomManager;
}

}