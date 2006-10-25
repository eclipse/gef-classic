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
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.NonNestedProxyNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedPane;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.policies.NestedGraphRootLayoutEditPolicy;
import org.eclipse.mylar.zest.core.internal.viewers.figures.PaneFigure;

/**
 * A nested pane area part will create one of three kinds of pane figures,
 * depending on context. The types are NestedPane.SUPPLIER_PANE, NestedPane.CLIENT_PANE,
 * or NestedPain.MAIN_PANE. The main pane contains the nested graph element that
 * the user is currently focussed on. The supplier pane contains all nodes that
 * have arcs running "from" that node "to" the focus node, or any of its children.
 * The client pane contains all nodes that have arcs running "to" that node "from"
 * the focus node or any of its children.
 * 
 * @author Ian bull
 * @author Del Myers
 */

//@tag bug(152613-Client-Supplier(fix)) : creates the figures that will hold all the nodes.
public class NestedPaneAreaEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {
	
	private int paneType = 0;
	private boolean initialClostedState;
	
	/**
	 * 
	 * @param paneType one of three types: NestedPane.MAIN_PANE, NestedPane.CLIENT_PANE,
	 * NestedPane.SUPPLIER_PANE.
	 */
	public NestedPaneAreaEditPart(int paneType, boolean initialClosedState) {
		this.paneType = paneType;
		//@tag bug(152613-Client-Supplier(fix)) : add an initial closed state so that the states can be carried accross new contents in the viewer.
		this.initialClostedState = initialClosedState;
	}
	
	protected IFigure createFigure() {
		PaneFigure f = new PaneFigure(paneType);
		switch (paneType) {
		case NestedPane.MAIN_PANE:
			f.getClientPanel().setLayoutManager(new FreeformLayout() {
				protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
					return null;
				}
				public void layout(IFigure container) {
					if (container.getChildren().size() != 1) return;
					IFigure figure = (IFigure) container.getChildren().get(0);
					Rectangle bounds = container.getBounds().getCopy();
					figure.translateToAbsolute(bounds);
					figure.setBounds(bounds);
				}
			});
		break;
		}
		f.setClosed(initialClostedState);
		return f;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.SimpleRootEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		//@tag zest(bug(152393-TopSelection(fix))) : add an edit policy to the EditPart which will create policies that don't allow the nodes to move.
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new NestedGraphRootLayoutEditPolicy());
	}
	
	protected List getModelChildren() {
		if (getCastedModel() == null) return Collections.EMPTY_LIST;
		return getCastedModel().getChildren();
	}
	
	protected NestedPane getCastedModel() {
		return (NestedPane) getModel();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	public void activate() {
		getCastedModel().getModel().addPropertyChangeListener(this);
		super.activate();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	public void deactivate() {
		getCastedModel().getModel().removePropertyChangeListener(this);
		super.deactivate();
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (GraphModel.NODE_PROXY_REMOVED_PROP.equals(evt.getPropertyName())) {
			getCastedModel().removeProxy((NonNestedProxyNode) evt.getOldValue());
			refreshChildren();
		}
		
	}
}
