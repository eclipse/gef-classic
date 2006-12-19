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

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.layouts.LayoutEntity;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * An abstraction of nodes in the graph model to allow different kinds of nodes to have the
 * same interface.
 * @author Del Myers
 *
 */
//@tag bug(154259-Abstraction(fix))
public interface IGraphModelNode extends IGraphItem, LayoutEntity {
	public static final String LOCATION_PROP = "GraphModelNode.Location";
	public static final String SIZE_PROP = "GraphModelNode.Size";
	public static final String FORCE_REDRAW = "GraphModelNode.Redraw";
	public static final String COLOR_BG_PROP = "GraphModelNode.BGColor";
	public static final String COLOR_FG_PROP = "GraphModelNode.FGColor";
	public static final String COLOR_BD_PROP = "GraphModelNode.BDColor";
	public static final String HIGHLIGHT_PROP = "GraphModelNode.Highlight";
	public static final String SOURCE_CONNECTIONS_PROP = "GraphModelNode.SourceConn";
	public static final String TARGET_CONNECTIONS_PROP = "GraphModelNode.TargetConn";
	public static final String BRING_TO_FRONT = "GraphModelNode.BrintToFront";
	
	/**
	 * Returns the node in the user's model.
	 * @return the node in the user's model.
	 */
	Object getExternalNode();
	/**
	 * The background color.
	 * @return the background color.
	 */
	Color getBackgroundColor();
	
	/**
	 * Should the text on the labels be cached.
	 * @return
	 */
	boolean cacheLabel();
	
	/**
	 * Sets should the labels be cached
	 * @param cacheLabel
	 */
	void setCacheLabel(boolean cacheLabel);
	
	/**
	 * Returns the color that the background will return to after unhighlighting. 
	 * @return the color that the background will return to after unhighlighting.
	 */
	Color getUnhiglightColor();
	/**
	 * The border color.
	 * @return the border color.
	 */
	Color getBorderColor();
	/**
	 * Return the border highlight color;
	 * @return the border highlight color.
	 */
	Color getBorderHighlightColor();
	
	/***
	 * Return the border unhighlight color.
	 * @return the border unhighlight color.
	 */
	Color getBorderUnhiglightColor();
	/**
	 * The border width in pixels.
	 * @return the border width.
	 */
	int getBorderWidth();
	/**
	 * The font.
	 * @return the font.
	 */
	Font getFont();
	/**
	 * The forground color.
	 * @return the forground color.
	 */
	Color getForegroundColor();
	/**
	 * The root graph model.
	 * @return the root graph model.
	 */
	GraphModel getGraphModel();
	/**
	 * The color used to highlight adjacent nodes.
	 * @return the color used to highlight adjacent nodes.
	 */
	Color getHighlightAdjacentColor();
	/**
	 * The color used to highlight this node.
	 * @return the color used to highlight this node.
	 */
	Color getHighlightColor();
	/**
	 * The location of the node.
	 * @return the location of the node.
	 */
	Point getLocation();
	/**
	 * The style of the node.
	 * @return the style of the node.
	 * @see ZestStyles
	 */
	int getNodeStyle();
	/**
	 * The size of this node.
	 * @return the size of this node.
	 */
	Dimension getSize();
	/**
	 * Returns a list of {@link IGraphModelConnection}s that have this node as their source.
	 * @return a list of {@link IGraphModelConnection}s that have this node as their source.
	 */
	List getSourceConnections();
	/**
	 * Returns a list of {@link IGraphModelConnection}s that have this node as their target.
	 * @return a list of {@link IGraphModelConnection}s that have this node as their target.
	 */
	List getTargetConnections();
	/**
	 * Highlights this node (and its adjacent nodes/connections, if applicable).
	 *
	 */
	void highlight();
	/**
	 * Highlights the nodes adjacent to this one.
	 *
	 */
	void highlightAdjacent();
	/**
	 * Returns true iff the style is set to highlight adjacent nodes.
	 * @return true iff the style is set to highlight adjacent nodes.
	 */
	boolean isHighlightAdjacentNodes();
	/**
	 * Removes the given connection from this node, if it is connected to this node.
	 * @param conn the connection to remove.
	 * @return true iff the connection was removed.
	 */
	boolean removeConnection(IGraphModelConnection conn);
	/**
	 * Permanently sets the background color of this node.
	 * @param c the new background color.
	 */
	void setBackgroundColor(Color c);
	/**
	 * Temporarily changes the background color of this node. Used as an intermediary
	 * step for graphical feedback such as highlighting.
	 * @param c the color to change to.
	 */
	void changeBackgroundColor(Color c);
	/**
	 * Sets the border color for this node.
	 * @param c the new border color.
	  */
	void setBorderColor(Color c);
	/**
	 * Sets the color used on the border for when this node is highlighted.
	 * @param c the new color.
	 */
	void setBorderHighlightColor(Color c);
	/**
	 * Sets the border width in pixels.
	 * @param width the new width.
	 */
	void setBorderWidth(int width);
	/**
	 * Sets the font to be used for labels.
	 * @param f the new font.
	 */
	void setFont(Font f);
	/**
	 * Sets the foreground color for labels.
	 * @param c the new foreground color.
	 */
	void setForegroundColor(Color c);
	/**
	 * Sets whether or not this node has a preferred location.
	 * @param hasPreferredLocation whether or not this node has a preferred location.
	 */
	void setHasPreferredLocation(boolean hasPreferredLocation);
	/**
	 * Returns true if this node has a preferred location.
	 * @return true iff this node has a preferred location.
	 */
	public boolean hasPreferredLocation();
	/**
	 * Sets the color that will be used to highlight adjacent nodes.
	 * @param c the color that will be used to highlight adjacent nodes.
	 */
	void setHighlightAdjacentColor(Color c);
	/**
	 * Sets whether or not this node should highlight its adjacent nodes/connections.
	 * @param highlightAdjacent whether or not this node should highlight its adjacent nodes/connections.
	 */
	void setHighlightAdjacentNodes(boolean highlightAdjacent);
	/**
	 * Sets the highlight color for this node.
	 * @param c the highlight color for this node.
	 */
	void setHighlightColor(Color c);
	/**
	 * Sets the image for this node.
	 * @param image the image for this node.
	 */
	void setImage(Image image);
	/**
	 * Gets the image for this node.
	 * @return the image for this node.
	 */
	Image getImage();
	/**
	 * Sets the location of this node.
	 * @param x x location of this node.
	 * @param y y location of this node.
	 */
	void setLocation(double x, double y);
	/**
	 * Sets the size.
	 * @param width
	 * @param height
	 */
	void setSize(double width, double height);
	/**
	 * Sets the node style.
	 * @param style the node style.
	 * @see ZestStyles
	 */
	void setNodeStyle(int style);
	/**
	 * Sets the preferred location of this node.
	 * @param x the preferred x location.
	 * @param y the preferred y location.
	 */
	void setPreferredLocation(double x, double y);
	/**
	 * Sets whether or not this node is selected.
	 * @param selected whether or not this node is selected.
	 */
	void setSelected(boolean selected);
	/**
	 * Returns true if this node is selected.
	 * @return true iff this node is selected.
	 */
	boolean isSelected();
	/**
	 * Sets the text label for this node.
	 * @param text the text label for this node.
	 */
	void setText(String text);
	/**
	 * Unhighlights this node and its neighbors if applicable.
	 *
	 */
	void unhighlight();
	/**
	 * Gets the text of this node.
	 * @return
	 */
	String getText();
	
	/**
	 * Adds the given connection to this graph node.
	 * @param c the connection
	 * @param source true if the connection should be added as a source, false
	 * if it should be added as a target.
	 */
	void addConnection(IGraphModelConnection c, boolean source);
	
	Dimension calculateMinimumLabelSize();
	
	Dimension calculateMinimumSize();
	
}
