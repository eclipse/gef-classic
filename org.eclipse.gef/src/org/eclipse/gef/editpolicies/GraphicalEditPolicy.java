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
 * A <code>GraphicalEditPolicy</code> is used with a {@link GraphicalEditPart}. All
 * GraphicalEditPolicies are involved with the Figure in some way. They might use the
 * Figure to interpret Requests, or they might simply decorate the Figure with graphical
 * Feedback, such as selection handles.
 * <P>
 * This class provides convenience methods for accessing the host's Figure, and for adding
 * <i>feedback</i> to the GraphicalViewer. This class does not handle any Request types
 * directly.
 */
public abstract class GraphicalEditPolicy
	extends AbstractEditPolicy
{

/**
 * Adds the specified <code>Figure</code> to the {@link LayerConstants#FEEDBACK_LAYER}.
 * @param figure the feedback to add */
protected void addFeedback(IFigure figure) {
	getLayer(LayerConstants.FEEDBACK_LAYER).
		add(figure);
}

/**
 * Convenience method to return the host's Figure.
 * @return The host GraphicalEditPart's Figure */
protected IFigure getHostFigure() {
	return ((GraphicalEditPart)getHost()).getFigure();
}

/**
 * Obtains the specified layer.
 * @param layer the key identifying the layer * @return the requested layer */
protected IFigure getLayer(Object layer) {
	return LayerManager.Helper.find(getHost()).getLayer(layer);
}

/** * Removes the specified <code>Figure</code> from the {@link
 * LayerConstants#FEEDBACK_LAYER}.
 * @param figure the feedback to remove */
protected void removeFeedback(IFigure figure) {
	getLayer(LayerConstants.FEEDBACK_LAYER).remove(figure);
}

}