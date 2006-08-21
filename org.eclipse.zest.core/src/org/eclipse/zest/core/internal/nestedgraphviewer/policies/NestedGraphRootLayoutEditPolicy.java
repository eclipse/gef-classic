/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.policies;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts.NestedGraphNodeEditPart;

/**
 * Creates a constrained edit policy for root-level nested graph nodes.
 * @author Del Myers
 */
//@tag bug(152393-TopSelection(fix)) : this edit policy simply creates a command that doesn't allow any movement of the nodes.
public class NestedGraphRootLayoutEditPolicy extends ConstrainedLayoutEditPolicy {


	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.requests.ChangeBoundsRequest, org.eclipse.gef.EditPart, java.lang.Object)
	 */
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
		if (child instanceof NestedGraphNodeEditPart && constraint instanceof Rectangle) {
			return new Command() {
				/* (non-Javadoc)
				 * @see org.eclipse.gef.commands.Command#canExecute()
				 */
				public boolean canExecute() {
					return false;
				}
				
				/* (non-Javadoc)
				 * @see org.eclipse.gef.commands.Command#canUndo()
				 */
				public boolean canUndo() {
					return false;
				}
			};
		}
		return super.createChangeConstraintCommand(request, child, constraint);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
		if (child instanceof GraphNodeEditPart) {
			return new ColorSelectionEditPolicy((GraphNodeEditPart)child);
		}
		return super.createChildEditPolicy(child);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#getConstraintFor(org.eclipse.draw2d.geometry.Point)
	 */
	protected Object getConstraintFor(Point point) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#getConstraintFor(org.eclipse.draw2d.geometry.Rectangle)
	 */
	protected Object getConstraintFor(Rectangle rect) {
		return null;
	}
	

}
