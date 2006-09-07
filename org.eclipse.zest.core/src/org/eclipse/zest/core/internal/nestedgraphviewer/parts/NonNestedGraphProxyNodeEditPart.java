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
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts;

import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.NonNestedProxyNode;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart;
import org.eclipse.mylar.zest.core.internal.viewers.figures.SimpleLabelBox;

/**
 * An edit part for duplicating model nodes visually, without duplicating them
 * in the model. The duplicates are not nested.
 * @author Del Myers
 *
 */
//@tag bug(153466-NoNestedClientSupply(fix)) : create an edit part for proxy nodes.
//@tag bug(154256-ClientSupplySelect(fix)) : make NonNestedGraphNodeEditPart extend GraphNodeEditPart so that colors will change.
public class NonNestedGraphProxyNodeEditPart extends GraphNodeEditPart
	implements NodeEditPart, PropertyChangeListener {
	ConnectionAnchor anchor;
	private Label label;
	

	public NonNestedProxyNode getCastedModel() {
		return (NonNestedProxyNode)getModel();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart#createFigureForModel()
	 */
	protected IFigure createFigureForModel() {
		IGraphModelNode node = getCastedModel().getProxy();
		SimpleLabelBox box = new SimpleLabelBox();
		label = box.getLabel();
		label.setFont(node.getFont());
		label.setText(node.getText());
		label.setIcon(node.getImage());
		label.setForegroundColor(node.getForegroundColor());
		box.setBackgroundColor(node.getBackgroundColor());
		return box;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		return createFigureForModel();
	}


	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getSourceConnections()
	 */
	public List getModelSourceConnections() {
		if (getCastedModel() == null) return Collections.EMPTY_LIST;
		return getCastedModel().getSourceConnections();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getTargetConnections()
	 */
	public List getModelTargetConnections() {
		if (getCastedModel() == null) return Collections.EMPTY_LIST;
		return getCastedModel().getTargetConnections();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return getDefaultAnchor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getDefaultAnchor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return getDefaultAnchor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getDefaultAnchor();
	}
	
	protected ConnectionAnchor getDefaultAnchor() {
		if (anchor == null) {
			anchor = new ChopboxAnchor(getFigure());
		}
		return anchor;
	}



	/**
	 * Refreshes the figures colors from the model.  This includes the border color and width.
	 */
	protected void refreshColors() {
		IFigure figure = getFigure();
		IGraphModelNode model = getCastedModel();
		figure.setForegroundColor(model.getForegroundColor());
		figure.setBackgroundColor(model.getBackgroundColor());
		
		if (label != null) {
			label.setForegroundColor(model.getForegroundColor());
			label.setBackgroundColor(model.getBackgroundColor());
		}
		
		//this.getFigure().revalidate();	
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	public void activate() {
		if (!isActive()) {
			refreshVisuals();
			refreshSourceConnections();
			refreshTargetConnections();
			super.activate();
			//we need to listen to the proxy.
			getCastedModel().getProxy().addPropertyChangeListener(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	public void deactivate() {
		super.deactivate();
		if (!isActive()) {
			getCastedModel().getProxy().removePropertyChangeListener(this);
		}
	}
		
}
