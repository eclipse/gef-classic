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
package org.eclipse.mylar.zest.core.internal.graphmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.draw2d.Graphics;
import org.eclipse.gef.EditPart;
import org.eclipse.mylar.zest.core.IZestColorConstants;
import org.eclipse.mylar.zest.core.ZestPlugin;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.layouts.LayoutBendPoint;
import org.eclipse.mylar.zest.layouts.LayoutEntity;
import org.eclipse.mylar.zest.layouts.LayoutRelationship;
import org.eclipse.mylar.zest.layouts.constraints.LayoutConstraint;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;


/**
 * This is the graph connection model which stores the source and destination nodes and the properties
 * of this connection (color, line width etc).
 * 
 * @author Chris Callendar
 */
public class GraphModelConnection extends GraphItem implements LayoutRelationship {

	private Font font;

	private ArrayList weightColors;
	public static final String LINECOLOR_PROP = "LineColor";
	//@tag unreported(EdgeHighlight) : fire property change when the edge highlight state changes.
	public static final String HIGHLIGHT_PROP = "UnHighlight";
	public static final String UNHIGHLIGHT_PROP = "Highlight";
	public static final String LINEWIDTH_PROP = "LineWidth";
	public static final String LINESTYLE_PROP = "LineStyle";
	public static final String DIRECTED_EDGE_PROP = "DirectedEdgeStyle";
	
	private GraphModelNode sourceNode;
	private GraphModelNode destinationNode;
	private EditPart editPart;
	private  double weight;
	private Color color;
	private Color highlightColor;
	private Color foreground;
	private int lineWidth;
	private int lineStyle;
	private HashMap attributes;
	private boolean isConnected;
	private GraphModel graphModel;
	
	private Object internalConnection;
	private int connectionStyle;

	private int curveDepth;
	
	/**
	 * LayoutConnection constructor, initializes the nodes and the connection properties.
	 * Defaults to bidirectional and a weighting of 0.5.
	 * @param graphModel	The graph model.
	 * @param data			The data object for this connection.
	 * @param source		The source node.
	 * @param destination 	The destination node.
	 */
	public GraphModelConnection(GraphModel graphModel, Object data, GraphModelNode source, GraphModelNode destination) {
		this(graphModel, data, source, destination, true, 0.5D);
	}
	
	/**
	 * LayoutConnection constructor, initializes the nodes and the connection properties.
	 * @param graphModel	The graph model.
	 * @param data			The data object for this connection.
	 * @param source		The source node.
	 * @param destination 	The destination node.
	 * @param bidirection	If the connection is bidirectional.
	 * @param weight		The connection weight.
	 */
	public GraphModelConnection(GraphModel graphModel, Object data, GraphModelNode source, GraphModelNode destination, boolean bidirection, double weight) {
		super(graphModel);
		ZestPlugin plugin = ZestPlugin.getDefault();
		this.setData(data);
		this.connectionStyle = IZestGraphDefaults.CONNECTION_STYLE;
		this.color = plugin.getColor(IZestColorConstants.EDGE_DEFAULT);
		this.foreground = plugin.getColor(IZestColorConstants.EDGE_DEFAULT);
		this.highlightColor = plugin.getColor(IZestColorConstants.EDGE_HIGHLIGHT);
		this.weightColors = new ArrayList();
		weightColors.add(plugin.getColor(IZestColorConstants.EDGE_WEIGHT_0));
		weightColors.add(plugin.getColor(IZestColorConstants.EDGE_WEIGHT_01));
		weightColors.add(plugin.getColor(IZestColorConstants.EDGE_WEIGHT_02));
		weightColors.add(plugin.getColor(IZestColorConstants.EDGE_WEIGHT_03));
		this.lineWidth = 1;
		this.lineStyle = Graphics.LINE_SOLID;
		setWeightInLayout(weight);
		this.attributes = new HashMap();
		this.isConnected = false;
		this.graphModel = graphModel;
		this.sourceNode = source;
		this.destinationNode = destination;
		//@tag removed : can cause the edit parts to be created before the model is finished. Wait until the model is fully constructed to reconnect.
		//reconnect(source, destination);
		this.font = Display.getDefault().getSystemFont();
	}
	
	/**
	 * Gets the external connection object.
	 * @return Object
	 */
	public Object getExternalConnection() {
		return this.getData();
	}
	
	/**
	 * Returns a string like 'source -> destination'
	 * @return String
	 */
	public String toString() {
		String arrow = (isBidirectionalInLayout() ? " <--> " : " --> ");
		String src = (sourceNode != null ? sourceNode.getText() : "null");
		String dest = (destinationNode != null ? destinationNode.getText() : "null");
		String weight = "  (weight=" + getWeightInLayout() + ")";
		return ("GraphModelConnection: " + src + arrow + dest + weight);
	}

	/** 
	 * Disconnect this connection from the shapes it is attached to.
	 */
	public void disconnect() {
		if (isConnected) {
			sourceNode.removeConnection(this);
			destinationNode.removeConnection(this);
			isConnected = false;
		}
	}
	
	/** 
	 * Reconnect this connection. 
	 * The connection will reconnect with the node it was previously attached to.
	 */  
	public void reconnect() {
		if (!isConnected) {
			sourceNode.addConnection(this, true);
			destinationNode.addConnection(this, false);
			isConnected = true;
		}
	}

	/**
	 * Reconnect to a different source and/or destination node.
	 * The connection will disconnect from its current attachments and reconnect to 
	 * the new source and destination. 
	 * @param newSource 		a new source endpoint for this connection (non null)
	 * @param newDestination	a new destination endpoint for this connection (non null)
	 * @throws IllegalArgumentException if any of the paramers are null
	 */
	public void reconnect(GraphModelNode newSource, GraphModelNode newDestination) {
		if (newSource == null || newDestination == null ) {
			throw new IllegalArgumentException("Invalid source and/or destination nodes");
		}
		//@tag bug(152180-SelfLoops) : commented out to allow a fix.
		/*else if ( newSource == newDestination ) {
			throw new IllegalArgumentException("Invalid: source == destination");
		}*/
		disconnect();
		this.sourceNode = newSource;
		this.destinationNode = newDestination;
		reconnect();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#getSourceInLayout()
	 */
	public LayoutEntity getSourceInLayout() {
		return sourceNode;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#getDestinationInLayout()
	 */
	public LayoutEntity getDestinationInLayout() {
		return destinationNode;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#isBidirectionalInLayout()
	 */
	public boolean isBidirectionalInLayout() {
		return !ZestStyles.checkStyle(connectionStyle, ZestStyles.CONNECTIONS_DIRECTED);
	}

	/**
	 * Returns the style of this connection. Valid styles are those that begin with
	 * CONNECTION in ZestStyles.
	 * @return the style of this connection.
	 * @see #ZestStyles
	 */
	public int getConnectionStyle() {
		return connectionStyle;
	}
	/**
	 * Returns the style of this connection. Valid styles are those that begin with
	 * CONNECTION in ZestStyles.
	 * @return the style of this connection.
	 * @see #ZestStyles
	 */
	public void setConnectionStyle(int style) {
		this.connectionStyle = style;
	}
	
	/**
	 * Gets the weight of this connection. The weight must be in {-1, [0-1]}.
	 * A weight of -1 means that there is no force/tension between the nodes.
	 * A weight of 0 results in the maximum spring length being used (farthest apart).
	 * A weight of 1 results in the minimum spring length being used (closest together).
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#getWeightInLayout()
	 * @return the weight: {-1, [0 - 1]}.
	 */
	public double getWeightInLayout() {
		return weight;
	}

	/**
	 * Gets the font for the label on this connection
	 * @return
	 */
	public Font getFont() {
		return this.font;
	}
	
	/**
	 * Sets the weight for this connection. The weight must be in {-1, [0-1]}.
	 * A weight of -1 means that there is no force/tension between the nodes.
	 * A weight of 0 results in the maximum spring length being used (farthest apart).
	 * A weight of 1 results in the minimum spring length being used (closest together).
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#setWeightInLayout(double)
	 */
	public void setWeightInLayout(double weight) {
		if (weight < 0) {
			this.weight = -1;
		} else if (weight > 1) {
			this.weight = 1;
		} else {
			this.weight = weight;
		}
		setLineWidth(getLineWidthFromWeight());
		foreground = getColorFromWeight();
		changeLineColor(foreground);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#getAttributeInLayout(java.lang.String)
	 */
	public Object getAttributeInLayout(String attribute) {
		return attributes.get(attribute);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#setAttributeInLayout(java.lang.String, java.lang.Object)
	 */
	public void setAttributeInLayout(String attribute, Object value) {
		attributes.put(attribute, value);
	}

	/**
	 * Returns the color of this connection.
	 * @return Color
	 */
	public Color getLineColor() {
		return color;
	}

	/**
	 * Sets the highlight color.
	 * @param color the color to use for highlighting.
	 */
	public void setHighlightColor(Color color) {
		this.highlightColor = color;
	}
	
	/**
	 * @return the highlight color
	 */
	public Color getHighlightColor() {
		return highlightColor;
	}
	/**
	 * Perminently sets the color of this line to the given color. This will become the
	 * color of the line when it is not highlighted. If you would like to temporarily change
	 * the color of the line, use changeLineColor.
	 * @param color the color to be set.
	 * @see changeLineColor(Color color)
	 */
	public void setLineColor(Color color) {
		this.foreground = color;
		changeLineColor(foreground);
	}
	
	/**
	 * Sets the connection color.
	 * @param color
	 */
	public void changeLineColor(Color color) {
		Color old = this.color;
		if (this.color != color) {
			this.color = color;
			firePropertyChange(LINECOLOR_PROP, old, color);
		}
	}
	
	/**
	 * Allows the user to set a list of colors that will be used for a visual
	 * determination of the weight of this edge.
	 *
	 */
	public void setWeightColors(Color[] colors) {
		this.weightColors.clear();
		this.weightColors.addAll(Arrays.asList(colors));
	}
	
	/**
	 * Gets the line color depending on the weight.
	 * @return the line color
	 */
	protected Color getColorFromWeight() {
		Color c = (Color) weightColors.get(0);
		int index = (int)(weight * 3);
		if ((index >= 0) && (index < weightColors.size())) {
			c = (Color) weightColors.get(index);
		}
		return c;
	}

	/**
	 * Returns the connection line width.
	 * @return int
	 */
	public int getLineWidth() {
		return lineWidth;
	}

	/**
	 * Sets the connection line width.
	 * @param lineWidth 
	 */
	public void setLineWidth(int lineWidth) {
		int old = this.lineWidth;
		if (this.lineWidth != lineWidth) {
			this.lineWidth = lineWidth;
			firePropertyChange(LINEWIDTH_PROP, new Integer(old), new Integer(lineWidth) );
		}
	}
	
	/**
	 * Calculates the line width depending on the connection's weight.
	 * The line width varies from 1 to 3.
	 * @return the line width [1-3].
	 */
	protected int getLineWidthFromWeight() {
		int width = 1;
		if (weight >= 0) {
			width = 1 + (int)(weight * 2);
		}
		return width;
	}

	/**
	 * Returns the connection line style.
	 * @return int
	 */
	public int getLineStyle() {
		return lineStyle;
	}

	/**
	 * Sets the connection line style.
	 * @param lineStyle
	 */
	public void setLineStyle(int lineStyle) {
		int old = this.lineStyle;
		if (this.lineStyle != lineStyle) {
			this.lineStyle = lineStyle;
			firePropertyChange(LINESTYLE_PROP, new Integer(old), new Integer(lineStyle));
		}
	}
	
	/**
	 * Gets the source node for this relationship
	 * @return GraphModelNode
	 */
	public GraphModelNode getSource() {
		return this.sourceNode;
	}
	
	/**
	 * Gets the target node for this relationship
	 * @return GraphModelNode
	 */
	public GraphModelNode getDestination() {
		return this.destinationNode;
	}
	
	/**
	 * Gets the internal relationship object.
	 * @return Object
	 */
	public Object getLayoutInformation() {
		return internalConnection;
	}
	
	/**
	 * Sets the internal relationship object.
	 * @param layoutInformation
	 */
	public void setLayoutInformation(Object layoutInformation) {
		this.internalConnection = layoutInformation;
	}
	
	/**
	 * Highlights this node.  Uses the default highlight color.
	 */
	public void highlight() {
		if (this.color != highlightColor) {
			changeLineColor(highlightColor);
			firePropertyChange(HIGHLIGHT_PROP, null, null);
		}
	}
	
	/**
	 * Unhighlights this node.  Uses the default color.
	 */
	public void unhighlight() {
		if (this.color != foreground) {
			changeLineColor(foreground);
			firePropertyChange(UNHIGHLIGHT_PROP, null, null);
		}
	}
	
	/**
	 * Gets the graph model that this connection is in
	 * @return The graph model that this connection is contained in
	 */
	public GraphModel getGraphModel() {
		return this.graphModel;
	}


	public void setBendPoints(LayoutBendPoint[] bendPoints) {
		
	}

	public void clearBendPoints() {
		
	}

	/**
	 * Returns the curve depth for this connection. The return value is only meaningful
	 * if the connection style has the ZestStyles.CONNECTIONS_CURVED style set.
	 * @return the curve depth.
	 */
	public int getCurveDepth() {
		return curveDepth;
	}
	
	public void setCurveDepth(int curveDepth) {
		this.curveDepth = curveDepth;
	}

	/**
	 * @return the editPart
	 * @deprecated by Del Myers. The model shouldn't reference the edit part. 
	 * If an edit part is needed for the model, use getViewer().getEditPartRegistry().get(model) instead.
	 */
	public EditPart getEditPart() {
		return editPart;
	}

	/**
	 * @param editPart the editPart to set
	 * @deprecated by Del Myers. 
	 */
	public void setEditPart(EditPart editPart) {
		this.editPart = editPart;
	}

	public void populateLayoutConstraint(LayoutConstraint constraint) {
		// TODO Auto-generated method stub
		
	}
	
}
