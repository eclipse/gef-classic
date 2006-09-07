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
package org.eclipse.mylar.zest.core.internal.graphmodel;


import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.layouts.LayoutRelationship;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * Abstraction of connections in the graph model to allow for a common
 * interface for different types of connections.
 * @author Del Myers
 *
 */
//@tag bug(154259-Abstraction(fix))
public interface IGraphModelConnection extends IGraphItem, LayoutRelationship {
	//property changes for graph model connections.
	/** The line color has changed for this connection **/
	public static final String LINECOLOR_PROP = "LineColor";
	//@tag unreported(EdgeHighlight) : fire property change when the edge highlight state changes.
	/** The connection has been highlighted **/
	public static final String HIGHLIGHT_PROP = "Highlight";
	/** The connection has been unhighlighted **/
	public static final String UNHIGHLIGHT_PROP = "UnHighlight";
	/** The line width has changed **/
	public static final String LINEWIDTH_PROP = "LineWidth";
	/** The line style has changed **/
	public static final String LINESTYLE_PROP = "LineStyle";
	/** The state of whether this line is directed has changed **/
	public static final String DIRECTED_EDGE_PROP = "DirectedEdgeStyle";
	
	
	/**
	 * Returns the representation of this connection in the user's model.
	 * @return the representation of this connection in the user's model.
	 */
	Object getExternalConnection();
	/**
	 * Reconnects the connection to its source and destination nodes.
	 *
	 */
	void reconnect();
	/**
	 * Reconnects this connection to the given source and destination nodes.
	 * Sets this connection's source and destination to the given nodes.
	 * @param source the source node.
	 * @param dest the destination node.
	 */
	void reconnect(IGraphModelNode source, IGraphModelNode dest);
	/**
	 * Disconnects this connection.
	 *
	 */
	void disconnect();
	/**
	 * Gets the root graph model that this connection is on.
	 * @return
	 */
	GraphModel getGraphModel();
	/**
	 * Returns the source node.
	 * @return the source node.
	 */
	IGraphModelNode getSource();
	/**
	 * Returns the destination node.
	 * @return the destination node.
	 */
	IGraphModelNode getDestination();
	/**
	 * Sets the style for this connection.
	 * @param style the style for this connection.
	 * @see ZestStyles
	 */
	void setConnectionStyle(int style);
	/**
	 * Returns the style for this connection.
	 * @return the style for this connection.
	 * @see ZestStyles
	 */
	int getConnectionStyle();
	/**
	 * Sets the depth of the curve on this connection. Normally ignored unless
	 * the style has ZestStyles.CONNECTIONS_CURVED set.
	 * @param depth
	 * @see ZestStyles
	 */
	void setCurveDepth(int depth);
	/**
	 * Returns the depth of the curve on this connection. Normally ignored unless
	 * the style has ZestStyles.CONNECTIONS_CURVED set.
	 * @return the curve depth.
	 * @see ZestStyles
	 */
	int getCurveDepth();
	/**
	 * Returns the end angle for this connection. Used for connections that have
	 * ZestStyles.CONNECTIONS_BEZIER set.
	 * @return the end angle.
	 * @see ZestStyles
	 */
	double getEndAngle();
	/**
	 * Returns the start angle for this connection. Used for connections that have
	 * ZestStyles.CONNECTIONS_BEZIER set.
	 * @return the start angle.
	 * @see ZestStyles
	 */
	double getStartAngle();
	/**
	 * Returns the start length for this connection. Used for connections that have
	 * ZestStyles.CONNECTIONS_BEZIER set.
	 * @return the start length.
	 * @see ZestStyles
	 */
	double getStartLength();
	/**
	 * Returns the end length for this connection. Used for connections that have
	 * ZestStyles.CONNECTIONS_BEZIER set.
	 * @return the end length.
	 * @see ZestStyles
	 */
	double getEndLength();
	/**
	 * Sets the start angle for this connection. Used for connections that have
	 * ZestStyles.CONNECTIONS_BEZIER set.
	 * @param angle the start angle.
	 * @see ZestStyles
	 */
	void setStartAngle(double angle);
	/**
	 * Sets the start length for this connection. Used for connections that have
	 * ZestStyles.CONNECTIONS_BEZIER set.
	 * @param length the start length.
	 * @see ZestStyles
	 */
	void setStartLength(double length);
	/**
	 * Sets the end angle for this connection. Used for connections that have
	 * ZestStyles.CONNECTIONS_BEZIER set.
	 * @param angle the end angle.
	 * @see ZestStyles
	 */
	void setEndAngle(double angle);
	/**
	 * Sets the end length for this connection. Used for connections that have
	 * ZestStyles.CONNECTIONS_BEZIER set.
	 * @param length the end length.
	 * @see ZestStyles
	 */
	void setEndLength(double length);
	/**
	 * Returns the font for this connection.
	 * @return the font for this connection.
	 */
	Font getFont();
	/**
	 * Sets the font for this connection.
	 * @param font the font for this connection.
	 */
	void setFont(Font font);
	/**
	 * Returns the color that is used when this connection is highlighted.
	 * @return the color that is used when this connection is highlighted.
	 */
	Color getHighlightColor();
	/**
	 * Sets the color that is used when this connection is highlighted.
	 * @param color the color that is used when this connection is highlighted.
	 */
	void setHighlightColor(Color color);
	/**
	 * Returns the line color that is used for this connection.
	 * @return the line color that is used for this connection.
	 */
	Color getLineColor();
	/**
	 * Permanently sets the line color that is used for this connection.
	 * @param c the line color that is used for this connection.
	 */
	void setLineColor(Color c);
	/**
	 * Temporarily changes the line color that is used for this connection. Used as
	 * an intermediary step for highlighting, and other graphical changes.
	 * @return the line color that is used for this connection.
	 */
	void changeLineColor(Color c);
	/**
	 * Returns the line style for this connection, based on the SWT line styles.
	 * @return the line style for this connection, based on the SWT line styles.
	 * @see SWT.LINE_SOLID
	 * @see SWT.LINE_DASH
	 * @see SWT.LINE_DOT
	 * @see SWT.LINE_DASHDOT
	 * @see SWT.LINE_DASHDOTDOT
	 */
	//@tag zest(discussion) : sould this be set in the connection style instead, using custom zest styles?
	int getLineStyle();
	/**
	 * Returns the line width in pixels for this connection. 
	 */
	int getLineWidth();
	/**
	 * Sets the line width in pixels for this connection.
	 * @param width the line width.
	 */
	void setLineWidth(int width);
	/**
	 * Changes the color of this line to the highlight color.
	 *
	 */	
	void highlight();
	/**
	 * Reverts back from the highlighted state.
	 *
	 */
	void unhighlight();
	/**
	 * Sets the line style for this connection, based on the SWT line styles.
	 * @param style the line style for this connection, based on the SWT line styles.
	 * @see SWT.LINE_SOLID
	 * @see SWT.LINE_DASH
	 * @see SWT.LINE_DOT
	 * @see SWT.LINE_DASHDOT
	 * @see SWT.LINE_DASHDOTDOT
	 */
	//@tag zest(discussion) : sould this be set in the connection style instead, using custom zest styles?
	void setLineStyle(int style);
	
	/**
	 * Sets the image.
	 * @param image
	 */
	void setImage(Image image);
	
	/**
	 * Gets the image.
	 * @return
	 */
	Image getImage();
	
	/**
	 * Sets the text.
	 * @param text
	 */
	void setText(String text);
	/**
	 * returns the text.
	 * @return the text. 
	 */
	String getText();

	/**
	 * Sets the weight in the layout.
	 * @param weight the weight in the layout.
	 */
	public void setWeightInLayout(double weight);
	
	/**
	 * 
	 * @return the wieght in the layout.
	 */
	public double getWeightInLayout();
}
