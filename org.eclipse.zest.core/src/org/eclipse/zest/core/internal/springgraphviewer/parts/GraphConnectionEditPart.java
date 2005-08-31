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
package org.eclipse.mylar.zest.core.internal.springgraphviewer.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection;


/**
 * @author ccallendar
 */
public class GraphConnectionEditPart extends AbstractConnectionEditPart implements PropertyChangeListener {

	/**
	 * 
	 */
	public GraphConnectionEditPart() {
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
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		PolylineConnection connection = (PolylineConnection) super.createFigure();
		//connection.setTargetDecoration(new PolygonDecoration()); // arrow at target endpoint
		connection.setForegroundColor(getCastedModel().getLineColor());
		connection.setLineWidth(getCastedModel().getLineWidth());
		connection.setLineStyle(getCastedModel().getLineStyle());
		return connection;
	}
	
	/**
	 * Gets the model casted into a GraphModelConnection
	 * @return the model casted into a GraphModelConnection
	 */
	protected GraphModelConnection getCastedModel() {
		return (GraphModelConnection)getModel();
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getPropertyName();

		if (GraphModelConnection.LINECOLOR_PROP.equals(property)) {
			((PolylineConnection) getFigure()).setForegroundColor(getCastedModel().getLineColor());
		} else if (GraphModelConnection.LINEWIDTH_PROP.equals(property)) {
			((PolylineConnection) getFigure()).setLineWidth(getCastedModel().getLineWidth());
		} else if (GraphModelConnection.LINESTYLE_PROP.equals(property)) {
			((PolylineConnection) getFigure()).setLineStyle(getCastedModel().getLineStyle());
		}
		else if ( GraphModelConnection.DIRECTED_EDGE_PROP.equals( property )) {
		  	GraphModel graphModel = this.getCastedModel().getGraphModel();
		  	
		  	if ( graphModel.getDirectedEdges() == true ) {
		  		((PolylineConnection)getFigure()).setTargetDecoration(new PolygonDecoration());
		  	}
		  	else {
		  		((PolylineConnection)getFigure()).setTargetDecoration( null );
		  	}
		}
	}
	
	
	protected void refreshVisuals() {
		super.refreshVisuals();
		((PolylineConnection) getFigure()).setForegroundColor(getCastedModel().getLineColor());

		((PolylineConnection) getFigure()).setLineWidth(getCastedModel().getLineWidth());
		
		((PolylineConnection) getFigure()).setLineStyle(getCastedModel().getLineStyle());
		
		GraphModel graphModel = this.getCastedModel().getGraphModel();
	 	if ( graphModel.getDirectedEdges() == true ) {
	  		((PolylineConnection)getFigure()).setTargetDecoration(new PolygonDecoration());
	  	}
	  	else {
	  		((PolylineConnection)getFigure()).setTargetDecoration( null );
	  	}

	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.GraphicalEditPart#getFigure()
	 */
	public IFigure getFigure() {
	  	PolylineConnection connectionFigure =  (PolylineConnection)super.getFigure();
	  	MidpointLocator m1 = new MidpointLocator(connectionFigure,0);
	  	if ( getCastedModel().getText() != null ||
	  		getCastedModel().getImage() != null ) {
	  		Label l = new Label(getCastedModel().getText(), getCastedModel().getImage());
	  		l.setFont(getCastedModel().getFont());
	  		connectionFigure.add(l,m1);
	  	}
	  	return connectionFigure;
	}	

}
