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
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.Shape;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.gefx.AligningBendpointLocator;
import org.eclipse.mylar.zest.core.internal.gefx.BezierConnection;
import org.eclipse.mylar.zest.core.internal.gefx.GraphRootEditPart;
import org.eclipse.mylar.zest.core.internal.gefx.PolylineArcConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelConnection;


/**
 * @author Chris Callendar
 */
public class GraphConnectionEditPart extends AbstractConnectionEditPart implements PropertyChangeListener {
	private RotatableDecoration dec;


	/**
	 * The constraint placed on the label for its alignment.
	 */
	//@tag zest.bug.160368-ConnectionAlign.fix
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
		//disconnect

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
			//@tag zest(bug(152180-SelfLoops)) : create an arc connection, despite the styles that have been set.
			//allow for a self-loop.
			//@tag zest(bug(154391-ArcEnds(fix))) :use a polyline arc connection
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
			connection = (PolylineConnection)super.createFigure();
		} 
		connection.setForegroundColor(getCastedModel().getLineColor());
		if (connection instanceof Shape) {
			((Shape)connection).setLineWidth(getCastedModel().getLineWidth());
			((Shape)connection).setLineStyle(getCastedModel().getLineStyle());
		}
		
	  	AligningBendpointLocator labelLocator = new AligningBendpointLocator(connection);
	  	if ( getCastedModel().getText() != null ||
	  		getCastedModel().getImage() != null ) {
	  		Label l = new Label(getCastedModel().getText(), getCastedModel().getImage());
	  		l.setFont(getCastedModel().getFont());
	  		connection.add(l,labelLocator);
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
			RotatableDecoration dec = getDecoration();
			if (dec instanceof PolylineDecoration) {
				((PolylineDecoration)dec).setScale(getCastedModel().getLineWidth()+2, getCastedModel().getLineWidth()+2);
			} else if (dec instanceof PolygonDecoration) {
				((PolygonDecoration)dec).setScale(getCastedModel().getLineWidth()+2, getCastedModel().getLineWidth()+2);
			} else if (dec instanceof Shape) {
				((Shape)dec).setLineWidth(getCastedModel().getLineWidth());
			}
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
		} else if (IGraphItem.VISIBLE_PROP.equals(property)) {
			getFigure().setVisible(((Boolean)event.getNewValue()).booleanValue());
		} else if (IGraphModelConnection.CURVE_PROP.equals(property)) {
			if (figure instanceof PolylineArcConnection) {
				((PolylineArcConnection)figure).setDepth(getCastedModel().getCurveDepth());
			} else if (figure instanceof BezierConnection) {
				BezierConnection bezier = (BezierConnection) figure;
				IGraphModelConnection conn = getCastedModel();
				bezier.setStartAngle((int) Math.round(conn.getStartLength()));
				bezier.setEndAngle((int) Math.round(conn.getEndAngle()));
				bezier.setStartLength(conn.getStartLength());
				bezier.setEndLength(conn.getEndLength());
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

	protected RotatableDecoration createDecoration() {
		RotatableDecoration dec = new PolygonDecoration();
		if (ZestStyles.checkStyle(getCastedModel().getConnectionStyle(), ZestStyles.CONNECTIONS_OPEN)) {
			dec = new PolylineDecoration();
			((PolylineDecoration)dec).setFill(false);
			((PolylineDecoration)dec).setOutline(true);
		}
		return dec;
	}
	
	public final RotatableDecoration getDecoration() {
		if (dec == null) {
			dec = createDecoration();
			getFigure().add(dec);
		}
		return dec;
	}
	
	protected void refreshVisuals() {
		super.refreshVisuals();
		IFigure figure = getFigure();
		figure.setForegroundColor(getCastedModel().getLineColor());
		if (figure instanceof Shape) {
			((Shape)figure).setLineWidth(getCastedModel().getLineWidth());
			((Shape)figure).setLineStyle(getCastedModel().getLineStyle());
		}
		//@tag zest(bug(154595(fix))) : change the arrow size based on the line width.
		boolean directed = isDirected();
		RotatableDecoration dec = getDecoration();
		if (dec instanceof PolylineDecoration) {
			((PolylineDecoration)dec).setScale(getCastedModel().getLineWidth()+2, getCastedModel().getLineWidth()+2);
		} else if (dec instanceof PolygonDecoration) {
			((PolygonDecoration)dec).setScale(getCastedModel().getLineWidth()+2, getCastedModel().getLineWidth()+2);
		} else if (dec instanceof Shape) {
			((Shape)dec).setLineWidth(getCastedModel().getLineWidth());
		}
		if (figure instanceof PolylineArcConnection) {
			if (directed) {
				((PolylineArcConnection)getFigure()).setTargetDecoration(dec);
			}
			else {
				((PolylineArcConnection)getFigure()).setTargetDecoration( null );
			}
			((PolylineArcConnection)figure).setDepth(getCastedModel().getCurveDepth());
		} else if (figure instanceof BezierConnection) {
			if (directed) {
	  			((BezierConnection)figure).setTargetDecoration(dec);
	  		}
	  		else {
	  			((BezierConnection)figure).setTargetDecoration( null );
	  		}
		} else if (figure instanceof PolylineConnection) { 	
			if (directed) {
				((PolylineConnection)getFigure()).setTargetDecoration(dec);
			}
			else {
				((PolylineConnection)getFigure()).setTargetDecoration( null );
			}
		}
		refreshLineStyle();
		refreshLabelLocation();

	}
	/**
	 * Refresh the alignment of labels.
	 */
	private void refreshLabelLocation() {
		List children = getFigure().getChildren();
		for (Iterator i = children.iterator(); i.hasNext();) {
			IFigure child = (IFigure) i.next();
			int style = getCastedModel().getConnectionStyle();
			AligningBendpointLocator labelLocator = null;
			try {
				Object constraint = getFigure().getLayoutManager().getConstraint(child);
				if (constraint instanceof AligningBendpointLocator) {
					labelLocator = (AligningBendpointLocator)constraint;
				}
			} catch (Exception e) {
				return;
			}
			if (labelLocator == null) return;
			if (ZestStyles.checkStyle(style, ZestStyles.CONNECTIONS_VALIGN_TOP)) {
				labelLocator.setVerticalAlginment(AligningBendpointLocator.ABOVE);
			} else if (ZestStyles.checkStyle(style, ZestStyles.CONNECTIONS_VALIGN_BOTTOM)) {
				labelLocator.setVerticalAlginment(AligningBendpointLocator.BELOW);
			} else {
				labelLocator.setVerticalAlginment(AligningBendpointLocator.MIDDLE);
			}
			if (ZestStyles.checkStyle(style, ZestStyles.CONNECTIONS_HALIGN_START)) {
				labelLocator.setHorizontalAlignment(AligningBendpointLocator.BEGINNING);
			} else if (ZestStyles.checkStyle(style, ZestStyles.CONNECTIONS_HALIGN_END)) {
				labelLocator.setHorizontalAlignment(AligningBendpointLocator.END);
			} else if (ZestStyles.checkStyle(style, ZestStyles.CONNECTIONS_HALIGN_CENTER_START)) {
				labelLocator.setHorizontalAlignment(AligningBendpointLocator.CENTER_BEGINNING);
			} else if (ZestStyles.checkStyle(style, ZestStyles.CONNECTIONS_HALIGN_CENTER_END)) {
				labelLocator.setHorizontalAlignment(AligningBendpointLocator.CENTER_END);
			} else {
				labelLocator.setHorizontalAlignment(AligningBendpointLocator.CENTER);
			}
		}
	}


	/**
	 * Refresh the line style for dashes, dots, etc.
	 *
	 */
	private void refreshLineStyle() {
		IFigure figure = getFigure();
		int style = getCastedModel().getLineStyle();
		if (figure instanceof Shape) {
			((Shape)figure).setLineStyle(style);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#unregister()
	 */
	protected void unregister() {
		// TODO Auto-generated method stub
		super.unregister();
	}

}
