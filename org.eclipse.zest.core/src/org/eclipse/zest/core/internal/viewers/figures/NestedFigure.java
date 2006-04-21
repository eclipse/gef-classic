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
package org.eclipse.mylar.zest.core.internal.viewers.figures;

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * A simple Figure which has a title and a CompartmentFigure which holds
 * other nested figures.
 * 
 * @author Chris Callendar
 */
public class NestedFigure extends Figure {

	private Label label = null;
	
	//private Clickable button = null;[irbull]
	
	/** Container figure for other figures */
	private AspectRatioScaledFigure scaledFigure = null;
	
	
	/**
	 * Initializes this nested figure without a Clickable figure and no ScrollPane.
	 * @param label	The label to display at the top of the nested figure
	 */
	public NestedFigure(Label label) {
		this(label, null, false);
	}
	
	/**
	 * Initializes this nested figure and possibly adds a Clickable figure
	 * in the top left corner.  The ScaledFigure is not put inside a ScrollPane.
	 * @param label	The label to display at the top of the nested figure
	 * @param button A Clickable figure which is put in the top left corner
	 */
	public NestedFigure(Label label, Clickable button) {
		this(label, button, false);
	}
	
	

	public Insets getInsets() {
		// TODO Auto-generated method stub
		return new Insets(0,0,0,0);
	}
	
	/**
	 * Creates a nested figure possibly with a plus/minus figure.
	 * @param label	The label to display at the top of the nested figure
	 * @param button A Clickable figure which is put in the top left corner
	 * @param addScrollPane If the ScaledFigure should be contained inside a ScrollPane
	 */
	public NestedFigure(Label label, Clickable button, boolean addScrollPane) {
		super();
		this.label = label;
		this.label.setOpaque(true);
		this.label.setLabelAlignment(PositionConstants.CENTER);
		//this.button = button; [irbull]
		//this.scaledFigure = new ScaledFigure();
		this.scaledFigure = new AspectRatioScaledFigure(label.getText());
		this.scaledFigure.setVisible(false);
		
		this.scaledFigure.setOpaque(false);
		//this.scaledFigure.setBorder(new EdgeBorder(ColorConstants.black, 1, 0, 0, 0));
		
		
		FreeformLayout layout = new FreeformLayout();
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setOpaque(false);
		scaledFigure.setBackgroundColor(ColorConstants.white);
		
		add(label, new Rectangle(0, 0, -1, -1), 0);
		if (button != null) {
			add(button, new Rectangle(0, 0, -1, -1));
		}
		
		add(scaledFigure, new Rectangle(0, 18, -1, -1));
		
	}
	
	protected Dimension calculateLabelSize() {
		String text = label.getText();
		Dimension minSize = new Dimension(0, 0);
		/*[irbull]
		if (button != null) {
			minSize.expand(button.getPreferredSize());
		}
		*/
		
		if ((text != null) && (text.length() > 0)) {
			Font font = label.getFont();
			if (font == null) {
				font = Display.getDefault().getSystemFont();
			}
			Dimension textSize = FigureUtilities.getStringExtents(text, font); 
			minSize.expand(textSize.width + 8, Math.max(0, textSize.height + 4 - minSize.height));
		}
		if (label.getIcon() != null) {
			org.eclipse.swt.graphics.Rectangle imageRect = label.getIcon().getBounds();
			minSize.expand(imageRect.width + 8, Math.max(0, imageRect.height + 6 - minSize.height));
		}
		return minSize;
	}
	
	public boolean isVisible() {
		return true;
	}
	
	
	public Rectangle getRectangle( Rectangle rect ) {
		rect.x = 0;
		rect.y = 0;
		rect.width = 50;
		rect.height = 50;
		return rect;
	}
	
	public boolean isCoordinateSystem() {
		// TODO Auto-generated method stub
		return true;
	}
	
//	public Rectangle getClientArea(Rectangle rect) {
//		// TODO Auto-generated method stub
//		rect.x = 0;
//		rect.y= 0;
//		rect.width = XSIZE;
//		rect.height = YSIZE;
//		return rect;
//	}
//	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
	 */
	
	public static final int XSIZE = 500;
	public static final int YSIZE = 500;
	
	
	public void setBounds(Rectangle bounds) {
		// resize the label & nested figures
		//int XSIZE = clienRectangle.width;
		//int YSIZE = clienRectangle.height;
		if ( XSIZE == 0 || YSIZE == 0 ) {
			super.setBounds(bounds);
			return;
		}
		
		int width = bounds.width;
		int height = bounds.height;
		Dimension labelSize = label.getSize();
		if ((labelSize.width == 0) || (labelSize.height == 0)) {
			labelSize = calculateLabelSize();
		}
		int labelHeight = Math.min(height, labelSize.height);
		labelSize.setSize(new Dimension(width, labelHeight));
		Point labelLoc = new Point(0, 0);
		/*[irbull]
		if (button != null) {
			Dimension prefSize = button.getPreferredSize(16, 16);
			getLayoutManager().setConstraint(button, new Rectangle(new Point(0, 0), prefSize));
		}
		*/
		getLayoutManager().setConstraint(label, new Rectangle(labelLoc, labelSize));
		
		boolean vis = scaledFigure.isVisible();
		Point loc = new Point(0, labelSize.height);
		Dimension dim = new Dimension(width, height - labelHeight);
		//Dimension dim = new Dimension(XSIZE, YSIZE);
		double xScale = (double)width / (double) XSIZE;
		double yScale = (double)(height - labelHeight) / (double)YSIZE;
		
		Dimension scaledDim = (vis ? dim : new Dimension(0, 0));
		scaledDim.scale(xScale * width, yScale * height );
		Rectangle rect = new Rectangle(loc, scaledDim);
		getLayoutManager().setConstraint(scaledFigure, rect);
		scaledFigure.setScale(xScale, yScale);
		super.setBounds(bounds);
	}
	
	public Label getLabel() {
		return label;
	}
	
	public AspectRatioScaledFigure getScaledFigure() {
		return scaledFigure;
	}
	
	protected boolean useLocalCoordinates() {
		return true;
	}	

	/** 
	 * Adds any NestedFigure objects to the NestedFigure instead.
	 * @see org.eclipse.draw2d.Figure#add(org.eclipse.draw2d.IFigure, java.lang.Object, int)
	 */
	public void add(IFigure figure, Object constraint, int index) {
		if (figure instanceof NestedFigure) {
			NestedFigure nestedFigure = (NestedFigure)figure;
			scaledFigure.add(nestedFigure, constraint, index);
		} else {
			super.add(figure, constraint, index);
		}
	}
	
	/**
	 * Sets the background color of the <b>label</b>.  If you want to set the background
	 * color for the nested figures then call {@link #getScaledFigure().setBackgroundColor(Color)}.
	 */
	public void setBackgroundColor(Color bg) {
		label.setBackgroundColor(bg);
		/*[irbull]
		if (button != null) {
			button.setBackgroundColor(bg);
		}
		*/
	}
	
	/**
	 * Sets the foreground color of the <b>label</b>.
	 */
	public void setForegroundColor(Color fg) {
		label.setForegroundColor(fg);
		/*[irbull]
		if (button != null) {
			button.setForegroundColor(fg);
		}
		*/
	}
	
	/**
	 * Sets the scale of the ScaledFigure.
	 * @param scale the scale
	 */
	public void setScale(double wScale, double hScale) {
		scaledFigure.setScale(wScale, hScale);
	}
	
	/**
	 * Gets the scale of the ScaledFigure.
	 * @return double the scale
	 */
	/*
	public double getScale() {
		return scaledFigure.getScale();
	}
	*/
	public double getWidthScale() {
		return scaledFigure.getWidthScale();
	}
	
	public double getHeightScale() {
		return scaledFigure.getHeightScale();
	}

	
	public double calculateTotalWidthScale() {
		double scale = getWidthScale();
		NestedFigure fig = this;
		while ( ( fig.getParent() instanceof AspectRatioScaledFigure ) && ( fig.getParent().getParent() instanceof NestedFigure ) ) {
			fig = (NestedFigure)fig.getParent().getParent();
			scale *= fig.getWidthScale();
		}
		return scale;
	}
	
	
	public double calculateTotalHeightScale() {
		double scale = getHeightScale();
		NestedFigure fig = this;
		while ( ( fig.getParent() instanceof AspectRatioScaledFigure ) && ( fig.getParent().getParent() instanceof NestedFigure ) ) {
			fig = (NestedFigure)fig.getParent().getParent();
			scale *= fig.getHeightScale();
		}
		return scale;
	}

	
	/**
	 * Calculates the total scale for this figure.  It traverses up the
	 * parent hierarchy multiplying the scales of each NestedFigure.
	 * @return double
	 */
	/*
	public double calculateTotalScale() {
		double scale = getScale();
		NestedFigure fig = this;
		while ((fig.getParent() instanceof ScaledFigure) && (fig.getParent().getParent() instanceof NestedFigure)) {
			fig = (NestedFigure)fig.getParent().getParent();
			scale *= fig.getScale();
		}
		return scale;
	}
	*/
	
	/**
	 * Sets the visibility of the nested figures.
	 * @param visible
	 */
	public void setNestedFiguresVisible(boolean visible) {
		/*[irbull]
		if ((button != null) && (button.isSelected() != visible)) {
			button.setSelected(visible);
		}
		*/
		/*
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			IFigure element = (IFigure) iter.next();
			//element.setVisible(visible);
		}
		*/
		scaledFigure.setVisible(visible);
		//scaledFigure.setVisible(visible);
		//scaledFigure.setFlag(Figure.FLAG_VISIBLE, visible);
		/*
		scaledFigure.erase();
		scaledFigure.repaint();
		scaledFigure.revalidate();
		
		scaledFigure.repaint();
		revalidate();
		*/
		
		//scaledFigure.revalidate();
		//scaledFigure.setVisible(visible);
	}
		
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "NestedFigure " + label.getText();
	}

	
}