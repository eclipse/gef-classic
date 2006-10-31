/*******************************************************************************
 * Copyright 2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.treegraphviewer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.viewers.IDAGContentProvider;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class DagViewerImpl extends Canvas {

	private FigureCanvas figureCanvas = null;
	private boolean treeCompression = false;
	private boolean hangingStyle = false;
	private IFigure contents = null;
	PageNode selected;

	public DagViewerImpl(Composite parent, int style) {
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
	
	public boolean getTreeCompression() {
		return this.treeCompression;
	}
	
	public boolean getHangingStyle() {
		return hangingStyle;
	}
	
	public IFigure getConnectionLayer() {
		return contents;
	}

	public void setContents(Object input, IDAGContentProvider contentProvider, ILabelProvider labelProvider) {
		getFigureCanvas().setBackground(ColorConstants.white);
		IFigure f = new Figure();
		this.contents = f;
		
		FlowLayout flowLayout = new FlowLayout(false);
		f.addLayoutListener(LayoutAnimator.getDefault());
		flowLayout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
		
		f.setLayoutManager(flowLayout);
		DagModelFactory factory = new DagModelFactory(this, contentProvider, labelProvider);
		DagNode[] roots = factory.createModel(contentProvider.getElements(input));
		for (int i = 0; i < roots.length; i++) {
			roots[i].placeFigure(f);
			//roots[i].setCompression(treeCompression);
		}
		getFigureCanvas().setContents(f);
	}
	
	void doExpandCollapse() {
		if (selected == null)
			return;
		TreeBranch parent = (TreeBranch) selected.getParent();
		if (parent.getContentsPane().getChildren().isEmpty())
			return;			
			
		if (parent.isExpanded())
			parent.collapse();
		else
			parent.expand();
				
	}
	
	void setSelected(PageNode node) {
		if (selected != null) {
			selected.setSelected(false);
		}
		selected = node;
		selected.setSelected(true);
	}
	
}
