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
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.policies;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart;



/**
 * The ColorSelectionEditPolicy uses a different color for the selected object.
 * When an edit part is selected the edit part fires of a highlight property change event,
 * and when the selection is hidden the unhighlight property event is fired.
 * 
 * @author Chris Callendar
 */
public class ColorSelectionEditPolicy extends ResizableEditPolicy {

	private GraphNodeEditPart editPart;
	
	/**
	 * ColorSelectionEditPolicy constructor.
	 */
	public ColorSelectionEditPolicy(GraphNodeEditPart editPart) {
		this.editPart = editPart;
 	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideSelection()
	 */
	protected void hideSelection() {
		super.hideSelection();
		PropertyChangeEvent evt = new PropertyChangeEvent(GraphModelNode.UNHIGHLIGHT_PROP, GraphModelNode.UNHIGHLIGHT_PROP, null, ColorConstants.white);
		editPart.propertyChange(evt);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showSelection()
	 */
	protected void showSelection() {
		super.showSelection();
		//System.out.println(editPart.getFigure().getBounds());
		PropertyChangeEvent evt = new PropertyChangeEvent(GraphModelNode.HIGHLIGHT_PROP, GraphModelNode.HIGHLIGHT_PROP, null, ColorConstants.red);
		editPart.propertyChange(evt);
		evt = new PropertyChangeEvent(GraphModelNode.BRING_TO_FRONT, GraphModelNode.BRING_TO_FRONT, null,null);
		editPart.propertyChange(evt);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showChangeBoundsFeedback(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		super.showChangeBoundsFeedback(request);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#eraseChangeBoundsFeedback(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		super.eraseChangeBoundsFeedback(request);
	}

}
