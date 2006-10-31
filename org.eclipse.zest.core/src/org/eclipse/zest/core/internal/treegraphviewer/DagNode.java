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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

public class DagNode extends Item {

	private ArrayList connectedNodes = null; // List of connected nodes
	private String text;
	private DagViewerImpl viewer = null;
	private int initialHangingStyle = 0;

	public DagNode(Widget parent, 
					int style, 
					Object element, 
					String text, 
					DagViewerImpl viewer,
					int initialHangingStyle ) {
		super(parent, style);
		this.setData(element);
		this.text = text;
		this.viewer = viewer;
		this.initialHangingStyle = initialHangingStyle;
		connectedNodes = new ArrayList();
	}

	public void addConnectedNode(DagNode node) {
		connectedNodes.add(node);
	}
	
	public int getInitialHangingStyle() {
		return this.initialHangingStyle;
	}

	void placeFigure(IFigure f, DagNode parentNode) {
		DagBranch branch = new DagBranch(text, connectedNodes, this);
		f.add(branch);
	}

	public void placeFigure(IFigure f) {
		DagRoot root = new DagRoot(text, connectedNodes, this);
		f.add(root);

	}

	DagViewerImpl getViewer() {
		return this.viewer;
	}
}

class DagBranch extends DagRoot {

	public DagBranch(String text, List connectedNodes, DagNode dagNode) {
		super(text, connectedNodes, dagNode);

	}

}

class DagRoot extends TreeRoot {

	List connectedNodes = null;
	DagNode correspondindDagNode = null;
	private boolean expanded = false;

	public DagRoot(String text, List connectedNodes, DagNode dagNode) {
		super(text);
		this.connectedNodes = connectedNodes;
		this.correspondindDagNode = dagNode;
		this.setStyle(dagNode.getInitialHangingStyle());

		this.addExpandListener(new ExpandListener() {
			public void collapse(TreeBranch branch) {
			}

			public void expand(TreeBranch branch) {
				if (!expanded) {
					for (int i = 0; i < DagRoot.this.connectedNodes.size(); i++) {
						DagNode node = (DagNode) DagRoot.this.connectedNodes.get(i);
						node.placeFigure(DagRoot.this.getContentsPane(), DagRoot.this.correspondindDagNode);
					}
				}
				expanded = true;
			}
		});

		this.addMouseListener(new MouseListener.Stub() {
			public void mousePressed(MouseEvent me) {
				correspondindDagNode.getViewer().setSelected(node);
			}

			public void mouseDoubleClicked(MouseEvent me) {
				correspondindDagNode.getViewer().doExpandCollapse();
			}
		});

	}

	protected boolean hasChildren() {
		return connectedNodes.size() > 0;
	}

}
