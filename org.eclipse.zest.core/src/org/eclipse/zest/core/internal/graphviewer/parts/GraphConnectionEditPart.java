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

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.Shape;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.gefx.BezierConnection;
import org.eclipse.mylar.zest.core.internal.gefx.GraphRootEditPart;
import org.eclipse.mylar.zest.core.internal.gefx.MidBendpointLocator;
import org.eclipse.mylar.zest.core.internal.gefx.PolylineArcConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelConnection;


/**
 * @author Chris Callendar
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
			((IGraphItem)getModel()).addPropertyChangeListener(this);
		}
	}

	
	/**
	 * Upon deactivation, detach from the model element as a property change listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((IGraphItem) getModel()).removePropertyChangeListener(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#removeNotify()
	 */
	public void removeNotify() {
		IFigure edge = getFigure();
		IFigure layer = getLayer(CONNECTION_LAYER);
		if (layer != null) {
			if (edge.getParent() != layer) {
				edge.getParent().remove(edge);
				layer.add(edge);
			}
		}
		super.removeNotify();
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
		Connection connection;
		IGraphModelConnection model = getCastedModel();
		int connectionStyle = model.getConnectionStyle();
		
		//styles should have been set by the GraphItemStyler by this point.
		if (model.getSource() == model.getDestination()) {
			//@tag bug(152180-SelfLoops) : create an arc connection, despite the styles that have been set.
			//allow for a self-loop.
			//@tag bug(154391-ArcEnds(fix)) :use a polyline arc connection
			connection = new PolylineArcConnection();
			if (model.getCurveDepth() <= 0) {
				//it has to have a curve.
				((PolylineArcConnection)connection).setDepth(10);
			} else {
				((PolylineArcConnection)connection).setDepth(model.getCurveDepth());
			}
		} else 	if (ZestStyles.checkStyle(connectionStyle, ZestStyles.CONNECTIONS_CURVED)) {
			connection = new PolylineArcConnection();
			((PolylineArcConnection)connection).setDepth(getCastedModel().getCurveDepth());
		} else if (ZestStyles.checkStyle(connectionStyle, ZestStyles.CONNECTIONS_BEZIER)) {
			connection = new BezierConnection(model.getStartAngle(), model.getStartLength(), model.getEndAngle(), model.getEndLength());
		} else {
			connection = (PolylineConnection) super.createFigure();
		} 
		connection.setForegroundColor(getCastedModel().getLineColor());
		if (connection instanceof Shape) {
			((Shape)connection).setLineWidth(getCastedModel().getLineWidth());
			((Shape)connection).setLineStyle(getCastedModel().getLineStyle());
		}
		
	  	Locator m1 = new MidBendpointLocator(connection);
	  	if ( getCastedModel().getText() != null ||
	  		getCastedModel().getImage() != null ) {
	  		Label l = new Label(getCastedModel().getText(), getCastedModel().getImage());
	  		l.setFont(getCastedModel().getFont());
	  		connection.add(l,m1);
	  	}
		
		return connection;
	}
	
	/**
     * 
     */
	public void highlightEdge() {
		IFigure thisEdge = getFigure(); 
		IFigure layer = getLayer(CONNECTION_LAYER);
		IFigure feedbackLayer = getLayer(GraphRootEditPart.CONNECTION_FEEDBACK_LAYER );
		if (thisEdge.getParent() == layer) {
			layer.remove(thisEdge);
			feedbackLayer.add(thisEdge);
		}

	}
	
	/**
     * 
     */
	public void unHighlightEdge() {
		IFigure thisEdge = getFigure(); 
		IFigure layer = getLayer(CONNECTION_LAYER);
		IFigure feedbackLayer = getLayer(GraphRootEditPart.CONNECTION_FEEDBACK_LAYER );
		if (thisEdge.getParent() == feedbackLayer) {
			feedbackLayer.remove(thisEdge);
			layer.add(thisEdge);
		}

	}
	
	/**
	 * Gets the model casted into a GraphModelConnection
	 * @return the model casted into a GraphModelConnection
	 */
	protected IGraphModelConnection getCastedModel() {
		return (IGraphModelConnection)getModel();
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getPropertyName();
		IFigure figure = getFigure();
		if (IGraphModelConnection.HIGHLIGHT_PROP.equals(property)) {
			//@tag unreported(EdgeHighlight) : respond to model highlight changes.
			highlightEdge();
		} else	if (IGraphModelConnection.UNHIGHLIGHT_PROP.equals(property)) { 
//			@tag unreported(EdgeHighlight) : respond to model highlight changes.
			unHighlightEdge();
		} else if (IGraphModelConnection.LINECOLOR_PROP.equals(property)) {
			figure.setForegroundColor(getCastedModel().getLineColor());
		} else if (IGraphModelConnection.LINEWIDTH_PROP.equals(property)) {
			if (figure instanceof Shape)
				((Shape)figure).setLineWidth(getCastedModel().getLineWidth());
		} else if (IGraphModelConnection.LINESTYLE_PROP.equals(property)) {
			if (figure instanceof Shape)
				((Shape)figure).setLineStyle(getCastedModel().getLineStyle());
		} else if (IGraphModelConnection.DIRECTED_EDGE_PROP.equals( property )) {
		  	boolean directed = isDirected();
		  	if (figure instanceof PolylineConnection) {
		  		if (directed) {
		  			((PolylineConnection)figure).setTargetDecoration(new PolygonDecoration());
		  		}
		  		else {
		  			((PolylineConnection)figure).setTargetDecoration( null );
		  		}
		  	} else if (figure instanceof PolylineArcConnection) {
				if (directed) {
					((PolylineArcConnection)getFigure()).setTargetDecoration(new PolygonDecoration());
				}
				else {
					((PolylineArcConnection)getFigure()).setTargetDecoration( null );
				}
			} else if (figure instanceof BezierConnection) {
				if (directed) {
		  			((BezierConnection)figure).setTargetDecoration(new PolygonDecoration());
		  		}
		  		else {
		  			((BezierConnection)figure).setTargetDecoration( null );
		  		}
			}
		}
	}
	
	
	/**
	 * @return
	 */
	private boolean isDirected() {
		int connectionStyle = getCastedModel().getConnectionStyle();
		return ZestStyles.checkStyle(connectionStyle, ZestStyles.CONNECTIONS_DIRECTED);
		
	}


	protected void refreshVisuals() {
		super.refreshVisuals();
		IFigure figure = getFigure();
		figure.setForegroundColor(getCastedModel().getLineColor());
		if (figure instanceof Shape) {
			((Shape)figure).setLineWidth(getCastedModel().getLineWidth());

			((Shape)figure).setLineStyle(getCastedModel().getLineStyle());
		}
		//@tag bug(154595(fix)) : change the arrow size based on the line width.
		boolean directed = isDirected();
		PolygonDecoration dec = new PolygonDecoration();
		dec.setScale((getCastedModel().getLineWidth()+2), (getCastedModel().getLineWidth()+2));
		if (figure instanceof PolylineConnection) { 	
			if (directed) {
				((PolylineConnection)getFigure()).setTargetDecoration(dec);
			}
			else {
				((PolylineConnection)getFigure()).setTargetDecoration( null );
			}
		} else if (figure instanceof PolylineArcConnection) {
			if (directed) {
				((PolylineArcConnection)getFigure()).setTargetDecoration(dec);
			}
			else {
				((PolylineArcConnection)getFigure()).setTargetDecoration( null );
			}
		} else if (figure instanceof BezierConnection) {
			if (directed) {
	  			((BezierConnection)figure).setTargetDecoration(dec);
	  		}
	  		else {
	  			((BezierConnection)figure).setTargetDecoration( null );
	  		}
		}

	}

}
