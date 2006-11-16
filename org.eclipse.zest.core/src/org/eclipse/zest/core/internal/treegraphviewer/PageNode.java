/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.treegraphviewer;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * This was originally a Draw2D Snippet that has been modified for Zest. All bugs 
 * in this should be opened against the Zest project.
 * 
 * @author hudsonr Created on Apr 22, 2003
 * @author Ian Bull
 */
public class PageNode extends Figure {

	private boolean selected;
	private boolean highlighted;
	static final Color gradient1 = new Color(null, 232, 232, 240);
	static final Color gradient2 = new Color(null, 176, 184, 216);
	static final Color corner1 = new Color(null, 200, 208, 223);
	static final Color corner2 = new Color(null, 160, 172, 200);
	static final Color blue = new Color(null, 152, 168, 200);
	//static final Font font = new Font()  Look below where I create the font... do this in a registy
	static final Color shadow = new Color(null, 202, 202, 202);
	static final int CORNER_SIZE = 10;
	static final Border BORDER = new CompoundBorder(new FoldedPageBorder(), new MarginBorder(0, 2,1, 10));

	private PlusMinus plusMinus = new PlusMinus();
	private TreeStyle layoutDisplay = null; 
	
	static class FoldedPageBorder extends AbstractBorder {
		static final PointList CORNER_ERASE;

		static final PointList CORNER_PAINT;
		
		static {
			CORNER_ERASE = new PointList(3);
			CORNER_ERASE.addPoint(1, 0);
			CORNER_ERASE.addPoint(1, CORNER_SIZE + 1);
			CORNER_ERASE.addPoint(-CORNER_SIZE, 0);
			CORNER_PAINT = new PointList(3);
			CORNER_PAINT.addPoint(-CORNER_SIZE, 0);
			CORNER_PAINT.addPoint(0, CORNER_SIZE);
			CORNER_PAINT.addPoint(-CORNER_SIZE, CORNER_SIZE);
			
		}
		

		static final Insets insets = new Insets(CORNER_SIZE, 2, 4, 4);

		public Insets getInsets(IFigure figure) {
			return insets;
		}

		public void paint(IFigure figure, Graphics g, Insets insets) {
			Rectangle r = getPaintRectangle(figure, insets);
			g.setLineWidth(4);
			r.resize(-2, -2);
			g.setForegroundColor(shadow);
			g.drawLine(r.x + 3, r.bottom(), r.right() - 1, r.bottom());
			g.drawLine(r.right(), r.y + 3 + CORNER_SIZE, r.right(), r.bottom() - 1);
			g.restoreState();
			r.resize(-1, -1);
			g.drawRectangle(r);
			g.setForegroundColor(blue);
			g.drawRectangle(r.x + 1, r.y + 1, r.width - 2, r.height - 2);
			g.translate(r.getTopRight());
			g.fillPolygon(CORNER_ERASE);
			g.setBackgroundColor(corner1);
			g.fillPolygon(CORNER_PAINT);
			g.setForegroundColor(figure.getForegroundColor());
			g.drawPolygon(CORNER_PAINT);
			g.restoreState();
			g.setForegroundColor(corner2);
			g.drawLine(r.right() - CORNER_SIZE + 1, r.y + 2, r.right() - 2, r.y + CORNER_SIZE - 1);
		}
	}

	private Label label = new Label();

	public PageNode(String text) {
		this();
		FontData[] fd = Display.getDefault().getSystemFont().getFontData();
		fd[0].height= 9;
		label.setFont(new Font(Display.getDefault(), fd));
		label.setText(text);
		
	}

	public PageNode() {
		this.addLayoutListener(LayoutAnimator.getDefault());
		layoutDisplay = new TreeStyle(this);
		setBorder(BORDER);

		FlowLayout flowLayout = new FlowLayout(false);
		flowLayout.setMajorSpacing(0);
		flowLayout.setMinorSpacing(0);
		setLayoutManager(flowLayout);
		
		layoutDisplay.setBounds(new Rectangle(0,0,-1,-1));

		IFigure top = new Figure();
		top.setLayoutManager(new FlowLayout(true));
		top.add(plusMinus);
		top.add(layoutDisplay);
		add(top);
		add(label);
	}
	
	public void addClickChangeListener( ActionListener listener ) {
		plusMinus.addActionListener(listener);
	}
	
	public void layoutChangedListener( ActionListener listener ) {
		layoutDisplay.addActionListener(listener);
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	public void setHasChildren(boolean hasChildren) {
//		plusMinus.setVisible(hasChildren);
//		layoutDisplay.setVisible(hasChildren);
		plusMinus.setVisible(hasChildren);
		layoutDisplay.setVisible(hasChildren);
	}
	
	public void setExpanded(boolean expanded) {
		plusMinus.setSelected(expanded);
	}
	
	public void setHangingLayoutStyle(boolean hanging) {
		layoutDisplay.setSelected(hanging);
	}

	public Insets getInsets() {
		Insets currentInsets = super.getInsets();
		currentInsets.top = 3;
		return currentInsets;
	}
	
	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		if (selected) {
			g.setForegroundColor(ColorConstants.menuBackgroundSelected);
			g.setBackgroundColor(ColorConstants.titleGradient);
		} else if (highlighted) {
			g.setForegroundColor(blue);
			g.setBackgroundColor(corner1);
		}
		else {
			g.setForegroundColor(gradient1);
			g.setBackgroundColor(gradient2);
		}
		g.fillGradient(getBounds().getResized(-3, -3), true);

	}
	
	public void setHighlighted(boolean value) {
		this.highlighted = value;
		repaint();
	}

	public void setSelected(boolean value) {
		this.selected = value;
		if (selected)
			label.setForegroundColor(ColorConstants.white);
		else
			label.setForegroundColor(null);
		repaint();
	}



}
