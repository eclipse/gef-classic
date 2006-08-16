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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.NonNestedProxyNode;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphConnectionEditPart;
import org.eclipse.mylar.zest.core.internal.viewers.figures.GraphLabel;
import org.eclipse.mylar.zest.core.internal.viewers.figures.SimpleLabelBox;

/**
 * An edit part for duplicating model nodes visually, without duplicating them
 * in the model. The duplicates are not nested.
 * @author Del Myers
 *
 */
//@tag bug(153466-NoNestedClientSupply(fix)) : create an edit part for proxy nodes.
public class NonNestedGraphProxyNodeEditPart extends AbstractGraphicalEditPart
	implements NodeEditPart, PropertyChangeListener {
	ConnectionAnchor anchor;
	

	public NonNestedProxyNode getCastedModel() {
		return (NonNestedProxyNode)getModel();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphNodeEditPart#createFigureForModel()
	 */
	protected IFigure createFigureForModel() {
		GraphModelNode node = getCastedModel().getProxy();
		SimpleLabelBox box = new SimpleLabelBox();
		Label label = box.getLabel();
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
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		
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


	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
				
		String prop = evt.getPropertyName();
		if ( GraphModelNode.FORCE_REDRAW.equals(prop)) {
			refreshVisuals();
			refreshChildren();
			refreshColors();
			getCastedModel().highlight();
			getCastedModel().unhighlight();
			
			
		}
		else if (GraphModelNode.LOCATION_PROP.equals(prop) || GraphModelNode.SIZE_PROP.equals(prop)) {
			refreshVisuals();
		} else if (GraphModelNode.SOURCE_CONNECTIONS_PROP.equals(prop)) {
			refreshSourceConnections();
		} else if (GraphModelNode.TARGET_CONNECTIONS_PROP.equals(prop)) {
			refreshTargetConnections();
		} else if (GraphModelNode.HIGHLIGHT_PROP.equals(prop)) {
			getCastedModel().highlight();
			List listOfSourceConnections = getSourceConnections();
			for (Iterator iter = listOfSourceConnections.iterator(); iter.hasNext();) {
				GraphConnectionEditPart element = (GraphConnectionEditPart) iter.next();
				element.highlightEdge();
			}
			// TODO pin highlighted node?  
			//getCastedModel().setHasPreferredLocation( true );
			refreshColors();
		} else if (GraphModelNode.UNHIGHLIGHT_PROP.equals(prop)) {
			getCastedModel().unhighlight();
			//getCastedModel().setHasPreferredLocation( false );
			List listOfSourceConnections = getSourceConnections();
			for (Iterator iter = listOfSourceConnections.iterator(); iter.hasNext();) {
				GraphConnectionEditPart element = (GraphConnectionEditPart) iter.next();
				element.unHighlightEdge();
			}
			refreshColors();
		} else if (GraphModelNode.COLOR_BG_PROP.equals(prop)) {
			refreshColors();
		} else if (GraphModelNode.COLOR_FG_PROP.equals(prop)) {
			refreshColors();
		} 
		else if ( GraphModelNode.BRING_TO_FRONT.equals(prop) ) {
			IFigure figure = getFigure();
			IFigure parent = figure.getParent();
			parent.remove(figure);
			parent.add(figure);
			
		}
		
	}
	/**
	 * Refreshes the figures colors from the model.  This includes the border color and width.
	 */
	protected void refreshColors() {
		IFigure figure = getFigure();
		GraphModelNode model = getCastedModel();
		figure.setForegroundColor(model.getForegroundColor());
		figure.setBackgroundColor(model.getBackgroundColor());
		
		if (figure instanceof GraphLabel) {
			GraphLabel label = (GraphLabel) figure;
			label.setBorderColor(model.getBorderColor());
			label.setBorderWidth(model.getBorderWidth());
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
			getCastedModel().setEditPart(this);
			getCastedModel().addPropertyChangeListener(this);
			getCastedModel().getProxy().addPropertyChangeListener(this);
			
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	public void deactivate() {
		super.deactivate();
		if (!isActive()) {
			getCastedModel().removePropertyChangeListener(this);
			getCastedModel().getProxy().removePropertyChangeListener(this);
			getCastedModel().setEditPart(null);
		}
	}
		
}
