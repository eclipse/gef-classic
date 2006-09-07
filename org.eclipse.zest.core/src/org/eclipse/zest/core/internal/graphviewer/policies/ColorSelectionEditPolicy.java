/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphviewer.policies;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart;

/**
 * The ColorSelectionEditPolicy class hides the selection handles at the corners
 * and instead uses a different color for the object to indicate selection.
 * 
 * @author Chris Callendar
 */
public class ColorSelectionEditPolicy extends NonResizableEditPolicy {

	private GraphNodeEditPart editPart;

	/**
	 * ColorSelectionEditPolicy constructor.
	 */
	public ColorSelectionEditPolicy(GraphNodeEditPart editPart) {
		this.editPart = editPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideSelection()
	 */
	protected void hideSelection() {
		//@tag bug(unreported(fix)) : let the model take care of highlighting, otherwise it gets unsynchronized with the view.
		((IGraphModelNode)editPart.getModel()).unhighlight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showSelection()
	 */
	protected void showSelection() {
//		@tag bug(unreported(fix)) : let the model take care of highlighting, otherwise it gets unsynchronized with the view.
		((IGraphModelNode)editPart.getModel()).highlight();

		// move the current editpart's figure to last in the list to put it on
		// top of the other nodes
		IFigure fig = editPart.getFigure();
		fig.getParent().getChildren().remove(fig);
		fig.getParent().getChildren().add(fig);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showChangeBoundsFeedback(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		IFigure feedback = getDragSourceFeedbackFigure();
		IGraphModelNode node = (IGraphModelNode)editPart.getModel();
		PrecisionRectangle rect = new PrecisionRectangle(new Rectangle(node.getLocation(), node.getSize()));
		getCurrentLocation(request, rect, feedback);
		feedback.getParent().setConstraint(feedback, rect);
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#getDragSourceFeedbackFigure()
	 */
	protected IFigure getDragSourceFeedbackFigure() {
		return editPart.getFigure();
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#eraseChangeBoundsFeedback(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		IFigure feedback = getDragSourceFeedbackFigure();
		IGraphModelNode node = (IGraphModelNode)editPart.getModel();
		PrecisionRectangle rect = new PrecisionRectangle(new Rectangle(node.getLocation(), node.getSize()));
		getCurrentLocation(request, rect, feedback);
		((IGraphModelNode) editPart.getModel()).setHasPreferredLocation(false);
		((IGraphModelNode) editPart.getModel()).setPreferredLocation(rect.x, rect.y);
	}
	
	
	/**
	 * Gets the current location based on the original location of the node, 
	 * and the current request.
	 * @param request The current request
	 * @param startingBounds The starting location of the node
	 * @param feedback The feedback figure being moved
	 * @return The current position of the node
	 */
	private Point getCurrentLocation(ChangeBoundsRequest request, PrecisionRectangle startingBounds, IFigure feedback) {
		getHostFigure().translateToAbsolute(startingBounds);
		startingBounds.translate(request.getMoveDelta());
		startingBounds.resize(request.getSizeDelta());
		feedback.translateToRelative(startingBounds);
		return new Point(startingBounds.x, startingBounds.y);
	}


}
