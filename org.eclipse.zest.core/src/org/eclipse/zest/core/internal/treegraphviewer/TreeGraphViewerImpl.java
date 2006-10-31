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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;


/**
 * This was originally a Draw2D Snippet that has been modified for Zest. All bugs 
 * in this should be opened against the Zest project.
 * 
 * @author hudsonr Created on Apr 22, 2003
 * @author Ian Bull
 */
public class TreeGraphViewerImpl extends Canvas {
	
	FigureCanvas figureCanvas = null;
	IFigure contents = null;
	boolean animate = true;
	TreeRoot root;
	PageNode selected;
	boolean treeCompression = false;
	boolean hangingStyle = false;


	public TreeGraphViewerImpl(Composite parent, int style) {
		super(parent, style);
		
		if ( (style & ZestStyles.TREE_GRAPH_COMPRESS) > 0 ) {
			// @tag style(tree_graph(compress))
			treeCompression = true;
		}
		if ( (style & ZestStyles.TREE_GRAPH_HANGING_LAYOUT ) > 0 ) {
			// @tag style(tree_graph(hanging))
			hangingStyle = true;
		}
		this.setLayout(new FillLayout());
		figureCanvas = new FigureCanvas(this);
		figureCanvas.setLayout(new FillLayout());	
	}
	
	private FigureCanvas getFigureCanvas() {
		return figureCanvas;
	}


	PageNode createPageNode(String title) {
		final PageNode node = new PageNode(title);
		node.addMouseListener(new MouseListener.Stub() {
			public void mousePressed(MouseEvent me) {
				setSelected(node);
			}

			public void mouseDoubleClicked(MouseEvent me) {
				doExpandCollapse();
			}
		});
		return node;
	}

	void doAddChild() {
		if (selected == null)
			return;
		TreeBranch parent = (TreeBranch) selected.getParent();
		parent.getContentsPane().add(new TreeBranch(createPageNode("child"), parent.getStyle()));
	}

	void doAlignCenter() {
		if (selected == null)
			return;
		TreeBranch parent = (TreeBranch) selected.getParent();
		parent.setAlignment(PositionConstants.CENTER);
	}

	void doAlignLeft() {
		if (selected == null)
			return;
		TreeBranch parent = (TreeBranch) selected.getParent();
		parent.setAlignment(PositionConstants.LEFT);
	}

	void doDeleteChild() {
		if (selected == null)
			return;
		TreeBranch parent = (TreeBranch) selected.getParent();
		IFigure contents = parent.getContentsPane();
		if (contents.getChildren().isEmpty())
			return;
		contents.remove((IFigure) contents.getChildren().get(contents.getChildren().size() - 1));
	}

	void doExpandCollapse() {
		if (selected == null)
			return;
		TreeBranch parent = (TreeBranch) selected.getParent();
		if (parent.getContentsPane().getChildren().isEmpty())
			return;
		if (animate) {
			
			if (parent.isExpanded())
				parent.collapse();
			else
				parent.expand();
				
		} else
			parent.setExpanded(!parent.isExpanded());
	}

	void doStyleHanging() {
		if (selected == null)
			return;
		TreeBranch parent = (TreeBranch) selected.getParent();
		parent.setStyle(TreeBranch.STYLE_HANGING);
	}

	void doStyleNormal() {
		if (selected == null)
			return;
		TreeBranch parent = (TreeBranch) selected.getParent();
		parent.setStyle(TreeBranch.STYLE_NORMAL);
	}

	
	
	/**
	 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
	 */
	public void setContents(Object input, ITreeContentProvider contentProvider, ILabelProvider labelProvider) {
		
		getFigureCanvas().setBackground(ColorConstants.white);
		IFigure f = new Figure();
		
		FlowLayout flowLayout = new FlowLayout(false);
		f.addLayoutListener(LayoutAnimator.getDefault());
		flowLayout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
		
		f.setLayoutManager(flowLayout);
		TreeGraphViewerModelFactory factory = new TreeGraphViewerModelFactory(this, contentProvider, labelProvider);
		TreeRoot[] roots = factory.createModel(contentProvider.getElements(input));
		for (int i = 0; i < roots.length; i++) {
			f.add( roots[i] );
			// @tag style(tree_graph) : Sets the compression on the tree
			roots[i].setCompression(treeCompression);
		}
		getFigureCanvas().setContents(f);
		this.contents = f;
	}

	

	void setSelected(PageNode node) {
		if (selected != null) {
			selected.setSelected(false);
		}
		selected = node;
		selected.setSelected(true);
	}

	public boolean getHangingStyle() {
		// @tag style(tree_graph(hanging))
		return hangingStyle;
	}

}
