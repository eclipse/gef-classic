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
import org.eclipse.draw2d.StackLayout;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.viewers.IDAGContentProvider;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class DagViewerImpl extends Canvas {

	private FigureCanvas figureCanvas = null;
	private IFigure regularLayer = null;
	private IFigure connectionLayer = null;
	private boolean treeCompression = false;
	private boolean hangingStyle = false;

	private DagNode selected;

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
		return connectionLayer;
	}

	public void setContents(Object input, IDAGContentProvider contentProvider, ILabelProvider labelProvider) {
		getFigureCanvas().setBackground(ColorConstants.white);
		connectionLayer = new Figure();
		regularLayer = new Figure();
		
		IFigure f = new Figure();
		
		StackLayout stackLayout = new StackLayout();
		f.addLayoutListener(LayoutAnimator.getDefault());
		f.setLayoutManager(stackLayout);
		
		FlowLayout flowLayout = new FlowLayout(false);
		flowLayout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
		
		regularLayer.setLayoutManager(flowLayout);
		connectionLayer.setLayoutManager(stackLayout);
		
		DagModelFactory factory = new DagModelFactory(this, contentProvider, labelProvider);
		DagNode[] roots = factory.createModel(contentProvider.getElements(input));
		for (int i = 0; i < roots.length; i++) {
			roots[i].placeFigure(regularLayer);
			//roots[i].setCompression(treeCompression);
		}
		f.add(connectionLayer);
		f.add(regularLayer);
		getFigureCanvas().setContents(f);
	}

	
	void setSelected(DagNode dagNode) {
		if (selected != null) {
			selected.setSelected(false);
		}
		// Remove all the old connections
		while ( connectionLayer.getChildren().size() > 0 ) {
			connectionLayer.remove((IFigure)connectionLayer.getChildren().get(0));
		}
		selected = dagNode;
		selected.setSelected(true);
	}
	
}
