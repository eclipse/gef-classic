/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editpolicies;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;

/**
 * A <code>GraphicalEditPolicy</code> is used with a {@link GraphicalEditPart}.
 * All GraphicalEditPolicies are involved with the Figure in some way. They
 * might use the Figure to interpret Requests, or they might simply decorate the
 * Figure with graphical Feedback, such as selection handles.
 * <P>
 * This class provides convenience methods for accessing the host's Figure, and
 * for adding <i>feedback</i> to the GraphicalViewer. This class does not handle
 * any Request types directly.
 */
public abstract class GraphicalEditPolicy extends AbstractEditPolicy {

	/**
	 * Adds the specified <code>Figure</code> to the
	 * {@link LayerConstants#FEEDBACK_LAYER}.
	 *
	 * @param figure the feedback to add
	 */
	protected void addFeedback(IFigure figure) {
		getFeedbackLayer().add(figure);
	}

	/**
	 * Returns the layer used for displaying feedback.
	 *
	 * @return the feedback layer
	 */
	protected IFigure getFeedbackLayer() {
		return getLayer(LayerConstants.FEEDBACK_LAYER);
	}

	/**
	 * Cast the parent getHost to GraphicalEditPart
	 *
	 * This reduces the necessary cast operations in this and all child classes as
	 * well as in any users of a GraphicalEditPolicy.
	 *
	 * @since 3.16
	 */
	@Override
	public GraphicalEditPart getHost() {
		return (GraphicalEditPart) super.getHost();
	}

	/**
	 * Convenience method to return the host's Figure.
	 *
	 * @return The host GraphicalEditPart's Figure
	 */
	protected IFigure getHostFigure() {
		return getHost().getFigure();
	}

	/**
	 * Obtains the specified layer.
	 *
	 * @param layer the key identifying the layer
	 * @return the requested layer
	 */
	protected IFigure getLayer(Object layer) {
		return LayerManager.Helper.find(getHost()).getLayer(layer);
	}

	/**
	 * Removes the specified <code>Figure</code> from the
	 * {@link LayerConstants#FEEDBACK_LAYER}.
	 *
	 * @param figure the feedback to remove
	 */
	protected void removeFeedback(IFigure figure) {
		getFeedbackLayer().remove(figure);
	}

}
