package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.gef.editparts.*;
import org.eclipse.gef.*;

/**
 A Graphical edit policy knows about the EditPart's view as a
 {@link org.eclipse.draw2d.Figure Figure}.  This class provides convenience methods for
 accessing the EditPart's figure, and for adding graphical feedback to the viewer.
 */
abstract public class GraphicalEditPolicy
	extends AbstractEditPolicy
{

protected void addFeedback(IFigure f) {
	getLayer(LayerConstants.FEEDBACK_LAYER).
		add(f);
}

protected IFigure getHostFigure() {
	return ((GraphicalEditPart)getHost()).getFigure();
}

protected IFigure getLayer(Object layer) {
	LayerManager manager = (LayerManager)getHost().getRoot().getViewer().
		getEditPartRegistry().
		get(LayerManager.ID);
	return manager.getLayer(layer);
}

protected void removeFeedback(IFigure f) {
	getLayer(LayerConstants.FEEDBACK_LAYER).remove(f);
}

}