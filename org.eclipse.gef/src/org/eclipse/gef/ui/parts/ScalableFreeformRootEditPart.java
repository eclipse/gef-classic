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

/**
 * @see org.eclipse.gef.ui.parts.FreeformGraphicalRootEditPart#createLayers(LayeredPane)
 */
protected void createLayers(LayeredPane layeredPane) {
	layeredPane.add(getScaledLayers(), "$Scaled Layers");//$NON-NLS-1$
	layeredPane.add(new FreeformLayer(), HANDLE_LAYER);
	layeredPane.add(new FeedbackLayer(), FEEDBACK_LAYER);
}

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

protected LayeredPane getScaledLayers() {
	if (scaledLayers == null)
		scaledLayers = createScaledLayers();
	return scaledLayers;
}

}
