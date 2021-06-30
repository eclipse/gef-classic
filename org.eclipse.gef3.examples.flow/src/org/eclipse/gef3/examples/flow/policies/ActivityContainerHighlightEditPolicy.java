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
package org.eclipse.gef3.examples.flow.policies;

import org.eclipse.draw2dl.IFigure;
import org.eclipse.gef3.EditPart;
import org.eclipse.gef3.GraphicalEditPart;
import org.eclipse.gef3.Request;
import org.eclipse.gef3.RequestConstants;
import org.eclipse.gef3.editpolicies.GraphicalEditPolicy;
import org.eclipse.swt.graphics.Color;

/**
 * @author Daniel Lee
 */
public class ActivityContainerHighlightEditPolicy extends GraphicalEditPolicy {

	private Color revertColor;
	private static Color highLightColor = new Color(null, 200, 200, 240);

	/**
	 * @see org.eclipse.gef3.EditPolicy#eraseTargetFeedback(org.eclipse.gef3.Request)
	 */
	public void eraseTargetFeedback(Request request) {
		if (revertColor != null) {
			setContainerBackground(revertColor);
			revertColor = null;
		}
	}

	private Color getContainerBackground() {
		return getContainerFigure().getBackgroundColor();
	}

	private IFigure getContainerFigure() {
		return ((GraphicalEditPart) getHost()).getFigure();
	}

	/**
	 * @see org.eclipse.gef3.EditPolicy#getTargetEditPart(org.eclipse.gef3.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		return request.getType().equals(RequestConstants.REQ_SELECTION_HOVER) ? getHost()
				: null;
	}

	private void setContainerBackground(Color c) {
		getContainerFigure().setBackgroundColor(c);
	}

	/**
	 * Changes the background color of the container to the highlight color
	 */
	protected void showHighlight() {
		if (revertColor == null) {
			revertColor = getContainerBackground();
			setContainerBackground(highLightColor);
		}
	}

	/**
	 * @see org.eclipse.gef3.EditPolicy#showTargetFeedback(org.eclipse.gef3.Request)
	 */
	public void showTargetFeedback(Request request) {
		if (request.getType().equals(RequestConstants.REQ_CREATE)
				|| request.getType().equals(RequestConstants.REQ_ADD))
			showHighlight();
	}

}
