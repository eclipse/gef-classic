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
package org.eclipse.mylar.zest.core.internal.graphviewer.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphviewer.policies.GraphXYLayoutEditPolicy;


/**
 * The EditPart associated with the GraphModel.  The view creates a FreeformLayer figure.
 *  
 * @author Chris Callendar
 */
public class GraphEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

	public GraphEditPart() {
		super();
	}
	
	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate();
			((GraphItem) getModel()).addPropertyChangeListener(this);
		}
	}	
	
	/**
	 * Upon deactivation, detach from the model element as a property change listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((GraphItem) getModel()).removePropertyChangeListener(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		f.setBorder(new MarginBorder(3));
		f.setLayoutManager(new FreeformLayout());
		return f;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		// disallows the removal of this edit part from its parent
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());

		installEditPolicy(EditPolicy.LAYOUT_ROLE,  new GraphXYLayoutEditPolicy());
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		// these properties are fired when Nodes and connections are added into or removed from 
		// the LayoutDiagram instance and must cause a call of refreshChildren()
		// to update the diagram's contents.
		if (GraphModel.NODE_ADDED_PROP.equals(prop) || GraphModel.NODE_REMOVED_PROP.equals(prop)) {
			refreshChildren();
		} else if ( GraphModelNode.HIGHLIGHT_PROP.equals( prop ) ) {
			setSelected( 1 );
		} else if ( GraphModelNode.UNHIGHLIGHT_PROP.equals( prop ) ) {
			setSelected( 0 );
		}
	}
		
	private GraphModel getCastedModel() {
		return (GraphModel)getModel();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return getCastedModel().getNodes();
	}	

}
