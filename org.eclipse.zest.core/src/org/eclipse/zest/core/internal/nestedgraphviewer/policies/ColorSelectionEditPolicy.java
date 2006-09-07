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

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelNode;
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
	//@tag bug(154256-ClientSupplySelect(fix)) : let it take a regular node edit part.
	public ColorSelectionEditPolicy(GraphNodeEditPart editPart) {
		this.editPart = editPart;
 	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideSelection()
	 */
	protected void hideSelection() {
		super.hideSelection();
		//@tag bug(154256-ClientSupplySelect(fix)) : let the model take care of it.
		((IGraphModelNode)editPart.getModel()).unhighlight();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showSelection()
	 */
	protected void showSelection() {
		super.showSelection();
		//@tag bug(154256-ClientSupplySelect(fix)) : let the model take care of it.
		((IGraphModelNode)editPart.getModel()).highlight();
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ResizableEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		//@tag bug(152393-TopSelection(fix)) : no handles on the top-level nodes.
		Object model = editPart.getModel();
		if (model instanceof NestedGraphModelNode) {
			NestedGraphModelNode node = (NestedGraphModelNode) model;
			if (node == ((NestedGraphModel)node.getGraphModel()).getCurrentNode()) {
				return Collections.EMPTY_LIST;
			}
		}
		return super.createSelectionHandles();
	}

}
