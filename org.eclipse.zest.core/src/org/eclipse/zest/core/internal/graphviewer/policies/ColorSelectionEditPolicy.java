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

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart;



/**
 * The ColorSelectionEditPolicy class hides the selection handles at the corners
 * and instead uses a different color for the object to indicate selection.
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		return Collections.EMPTY_LIST;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideSelection()
	 */
	protected void hideSelection() {
		PropertyChangeEvent evt = new PropertyChangeEvent(GraphModelNode.UNHIGHLIGHT_PROP, GraphModelNode.UNHIGHLIGHT_PROP, null, ColorConstants.white);
		editPart.propertyChange(evt);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showSelection()
	 */
	protected void showSelection() {
		PropertyChangeEvent evt = new PropertyChangeEvent(GraphModelNode.HIGHLIGHT_PROP, GraphModelNode.HIGHLIGHT_PROP, null, ColorConstants.red);
		editPart.propertyChange(evt);
		
		// move the current editpart's figure to last in the list to put it on top of the other nodes
		IFigure fig = editPart.getFigure();
		fig.getParent().getChildren().remove(fig);
		fig.getParent().getChildren().add(fig);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showChangeBoundsFeedback(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		Point p = request.getLocation().getCopy();
		// this translation is needed for different zooming levels 
		getHostFigure().translateToRelative(p);
		((GraphModelNode)editPart.getModel()).setHasPreferredLocation(true);
		((GraphModelNode)editPart.getModel()).setPreferredLocation(p.x, p.y);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#eraseChangeBoundsFeedback(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		((GraphModelNode)editPart.getModel()).setHasPreferredLocation(false);
	}

}
