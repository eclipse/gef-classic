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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This was originally a Draw2D Snippet that has been modified for Zest. All bugs 
 * in this should be opened against the Zest project.
 * 
 * @author hudsonr Created on Apr 22, 2003
 * @author Ian Bull
 */
public class TreeBranch extends Figure {
	public static final int ANIMATION_TIME = 200;
	List expandListeners = new LinkedList();

	class AnimatingLayer extends Layer {
		
		public AnimatingLayer() {
			this.addLayoutListener(LayoutAnimator.getDefault());
			this.setOpaque(false);
		}

		
		/**
		 * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
		 */
		public void setBounds(Rectangle rect) {			
			int x = bounds.x, y = bounds.y;

			
			boolean resize = (rect.width != bounds.width) || (rect.height != bounds.height), 
				translate = (rect.x != x)|| (rect.y != y);

			if (isVisible() && (resize || translate))
				erase();
			if (translate) {
				int dx = rect.x - x;
				int dy = rect.y - y;
				primTranslate(dx, dy);
			}
			bounds.width = rect.width;
			bounds.height = rect.height;
			// if (resize) Layouts dont depend on size.
			// invalidate();
			if (resize || translate) {
				fireFigureMoved();
				repaint();
			}
		}
		
		public void layout() {
			super.layout();
		}

	}

	public static final int STYLE_HANGING = 1;
	public static final int STYLE_NORMAL = 2;
	int aligment = PositionConstants.LEFT;

	/*
	 * A layer is being used simply because it is the only "transparent" figure
	 * in draw2d. See the implementation of Layer.containsPoint(...) for what is
	 * meant by "transparent". If a layer is not used, then overlapping branches
	 * will cause hit-test problems.
	 */
	AnimatingLayer contents = new AnimatingLayer();
	boolean expanded = false;
	PageNode node;
	int style;
	
	public TreeBranch( String label ) {
		this(new PageNode(label));
	}

	public TreeBranch(PageNode title) {
		this(title, STYLE_NORMAL);
	}

	public TreeBranch(PageNode title, int style) {
		this.setLayoutManager(new NormalLayout(this));
		this.addLayoutListener(LayoutAnimator.getDefault());
		if (title.getBorder() == null)
			title.setBorder(new LineBorder(ColorConstants.gray, 2));
		this.node = title;
		
		setStyle(style);
		this.setExpanded(expanded);
		
		this.node.addClickChangeListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				
				if ( isExpanded() ) collapse();
				else expand();
				
				
			}
		});
		this.node.layoutChangedListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				Animation.markBegin();
				TreeStyle plusMinusEvent = (TreeStyle) event.getSource();
				if ( plusMinusEvent.isSelected() )
					setStyle(TreeBranch.STYLE_HANGING);
				else 
					setStyle(TreeBranch.STYLE_NORMAL);
				Animation.run(ANIMATION_TIME);
			}
			
		});
		add(contents);
		add(title);
	}

	/**
	 * recursively set all nodes and sub-treebranch nodes to the same location.
	 * This gives the appearance of all nodes coming from the same place.
	 * 
	 * @param bounds
	 *            where to set
	 */
	public void animationReset(Rectangle bounds) {
		List subtrees = contents.getChildren();
		contents.setBounds(bounds);

		// Make the center of this node match the center of the given bounds
		Rectangle r = node.getBounds();
		int dx = bounds.x + bounds.width / 2 - r.x - r.width / 2;
		int dy = bounds.y + bounds.height / 2 - r.y - r.height / 2;
		node.translate(dx, dy);
		revalidate(); // Otherwise, this branch will not layout

		// Pass the location to all children
		for (int i = 0; i < subtrees.size(); i++) {
			TreeBranch subtree = (TreeBranch) subtrees.get(i);
			subtree.setBounds(bounds);
			subtree.animationReset(bounds);
		}
	}
	
	
	private void collapseLocation(Rectangle bounds) {
		List subtrees = contents.getChildren();
		for ( int i = 0; i < subtrees.size(); i++ ) {
			TreeBranch subtree = (TreeBranch) subtrees.get(i);
			subtree.invalidate();
			subtree.setBounds(bounds);
			revalidate();
		}
	}


	public void collapse() {
		if (!expanded)
			return;
		fireExpanded(false);
		IFigure root = this;
		Viewport port = null;
		Point viewportStart = null;
		while (root.getParent() != null) {
			if (root instanceof Viewport)
				port = ((Viewport) root);
			root = root.getParent();
		}
		viewportStart = port.getViewLocation();

		Animation.markBegin();
		
		LayoutManager layout1 = contents.getLayoutManager();
		contents.setLayoutManager(new FreeformLayout());
		contents.invalidate();
		collapseLocation(getNodeBounds());
		
		Animation.run(ANIMATION_TIME);
		port.setViewLocation(viewportStart);
		contents.setLayoutManager(layout1);
		setExpanded(false);
	}

	/**
	 * @see org.eclipse.draw2d.Figure#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {
		return node.containsPoint(x, y) || contents.containsPoint(x, y);
	}

	public void expand() {
		if (expanded)
			return;
		fireExpanded(true);
		Animation.markBegin();
		animationReset(getNodeBounds());
		setExpanded(true);
		Animation.run(ANIMATION_TIME);
	}

	public int getAlignment() {
		return aligment;
	}

	protected BranchLayout getBranchLayout() {
		return (BranchLayout) getLayoutManager();
	}

	public IFigure getContentsPane() {
		return contents;
	}

	public int[] getContourLeft() {
		return getBranchLayout().getContourLeft();
	}

	public int[] getContourRight() {
		return getBranchLayout().getContourRight();
	}

	public int getDepth() {
		return getBranchLayout().getDepth();
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getMinimumSize(int, int)
	 */
	public Dimension getMinimumSize(int wHint, int hHint) {
		
		//if (!AnimationoldAnimation)
			validate();
		return super.getMinimumSize(wHint, hHint);
	}

	public PageNode getNode() {
		return node;
	}

	public Rectangle getNodeBounds() {
		return node.getBounds();
	}

	public int[] getPreferredRowHeights() {
		return getBranchLayout().getPreferredRowHeights();
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		//if (!AnimationoldAnimation)
			validate();
		return super.getPreferredSize(wHint, hHint);
	}

	public TreeRoot getRoot() {
		return ((TreeBranch) getParent().getParent()).getRoot();
	}

	public int getStyle() {
		return style;
	}

	/**
	 * @return
	 */
	public boolean isExpanded() {
		return expanded;
	}
	
	protected boolean hasChildren() {
		return (contents.getChildren().size() > 0);
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		node.setHasChildren(this.hasChildren());
		if (isExpanded())
			getBranchLayout().paintLines(graphics);
		// if (getDepth() == 2)
		// graphics.drawRectangle(getBounds().getResized(-1, -1));
	}

	public void setAlignment(int value) {
		aligment = value;
		revalidate();
	}
	
	/**
	 * @param b
	 */
	public void setExpanded(boolean b) {
		if (expanded == b)
			return;
		expanded = b;
		contents.setVisible(b);
		node.setExpanded(b);
		revalidate();
	}

	public void setNode(IFigure node) {
		remove(this.node);
		add(this.node, 0);
	}

	public void setRowHeights(int heights[], int offset) {
		getBranchLayout().setRowHeights(heights, offset);
	}

	public void setStyle(int style) {
		if (this.style == style)
			return;
		this.style = style;
		switch (style) {
		case STYLE_HANGING:
			setLayoutManager(new HangingLayout(this));
			node.setHangingLayoutStyle(true);
			contents.setLayoutManager(new ToolbarLayout(false));
			break;

		default:
			setLayoutManager(new NormalLayout(this));
			node.setHangingLayoutStyle(false);
			contents.setLayoutManager(new TreeLayout());
			break;
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return toString(0);
	}
	
	public void addExpandListener(ExpandListener listener) {
		Iterator iterator = expandListeners.iterator();
		while ( iterator.hasNext() ) {
			ExpandListener listener2 =(ExpandListener) iterator.next();
			if ( listener2 == listener ) {
				iterator.remove();
			}
		}
		expandListeners.add(listener);
	}
	
	public void removeExpandListener(ExpandListener listener) {
		Iterator iterator = expandListeners.iterator();
		while ( iterator.hasNext() ) {
			ExpandListener listener2 =(ExpandListener) iterator.next();
			if ( listener2 == listener ) {
				iterator.remove();
			}
		}
	}
	
	private void fireExpanded( boolean expanded ) {
		Iterator iterator = expandListeners.iterator();
		while ( iterator.hasNext() ) {
			ExpandListener listener =(ExpandListener) iterator.next();
			if ( expanded )
				listener.expand(this);
			else
				listener.collapse(this);
		}
	}

	public String toString(int level) {
		String result = "";
		for (int i = 0; i < level; i++)
			result += "  ";
		result += getChildren().get(1) + "\n";
		for (int i = 0; i < contents.getChildren().size(); i++)
			result += ((TreeBranch) contents.getChildren().get(i)).toString(level + 1);
		return result;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#validate()
	 */
	public void validate() {
		if (isValid())
			return;
//		/*
//		if (style == STYLE_HANGING) {
//			ToolbarLayout layout = new ToolbarLayout(!getRoot().isHorizontal()) {
//				public void layout(IFigure parent) {
//					//Animation.markBegin();
//					/*
//					oldAnimation.recordInitialState(parent);
//					if (oldAnimation.playbackState(parent))
//						return;
//						*/
//
//					super.layout(parent);
//					//Animation.run(200);
//				}
//
//			};
//			layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
//			layout.setStretchMinorAxis(false);
//			contents.setLayoutManager(layout);
//		}
		
		repaint();
		super.validate();
	}

}
