package org.eclipse.gef.ui.parts;

import org.eclipse.draw2d.*;

/**
 * @author hudsonr
 * @since 2.1
 */
public class ScalableFreeformRootEditPart
	extends FreeformGraphicalRootEditPart
{

private ScalableFreeformLayeredPane scaledLayers = new ScalableFreeformLayeredPane();

/**
 * @see org.eclipse.gef.ui.parts.FreeformGraphicalRootEditPart#createLayers(LayeredPane)
 */
protected void createLayers(LayeredPane layeredPane) {
	createScaledLayers(scaledLayers);
	layeredPane.add(scaledLayers, "$Scaled Layers");//$NON-NLS-1$
	layeredPane.add(new FreeformLayer(), HANDLE_LAYER);
	layeredPane.add(new FeedbackLayer(), FEEDBACK_LAYER);
}

protected void createScaledLayers(LayeredPane layeredPane) {
	scaledLayers.add(createPrintableLayers(), PRINTABLE_LAYERS);
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

}
