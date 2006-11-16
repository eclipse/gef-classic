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
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

public class DagNode extends Item {

	private ArrayList connectedNodes = null; // List of connected nodes
	private ArrayList instanceNodes = null; // These are all the places this dag
	private String text;
	private DagViewerImpl viewer = null;
	private int initialHangingStyle = 0;

	// private boolean selected = false;

	public DagNode(Widget parent, int style, Object element, String text,
			DagViewerImpl viewer, int initialHangingStyle) {
		super(parent, style);
		this.setData(element);
		this.text = text;
		this.viewer = viewer;
		this.initialHangingStyle = initialHangingStyle;
		this.instanceNodes = new ArrayList();
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
		instanceNodes.add(branch);
		f.add(branch);
	}

	public void showConnectedSelected(DagBranch dagItem) {
		Iterator iterator = instanceNodes.iterator();
		while (iterator.hasNext()) {
			DagBranch item = (DagBranch) iterator.next();
			if (item != dagItem) {
				PolylineConnection polylineConnection = new PolylineConnection();
				IFigure a = dagItem.getNode();
				IFigure b = item.getNode();
				polylineConnection.setSourceAnchor(new ChopboxAnchor(a));
				polylineConnection.setTargetAnchor(new ChopboxAnchor(b));
				polylineConnection.setLineWidth(1);
				viewer.getConnectionLayer().add(polylineConnection);
				polylineConnection.setBackgroundColor(ColorConstants.lightGray);
				polylineConnection.setForegroundColor(ColorConstants.lightGray);
			}
		}
	}

	public void placeFigure(IFigure f) {
		DagBranch root = new DagBranch(text, connectedNodes, this);
		instanceNodes.add(root);
		f.add(root);

	}

	DagViewerImpl getViewer() {
		return this.viewer;
	}

	public void setSelected(boolean b) {
		// this.selected = b;
		Iterator iterator = instanceNodes.iterator();
		while (iterator.hasNext()) {
			DagBranch dagRoot = (DagBranch) iterator.next();
			dagRoot.getNode().setSelected(b);
		}
	}
}

class DagBranch extends TreeRoot {

	List connectedNodes = null;
	DagNode correspondindDagNode = null;
	boolean expanded = false;

	public DagBranch(String text, List connectedNodes, DagNode dagNode) {
		super(text);
		this.connectedNodes = connectedNodes;
		this.correspondindDagNode = dagNode;
		this.setStyle(dagNode.getInitialHangingStyle());

		this.addExpandListener(new ExpandListener() {
			public void collapse(TreeBranch branch) {
			}

			public void expand(TreeBranch branch) {
				if (!expanded) {
					for (int i = 0; i < DagBranch.this.connectedNodes.size(); i++) {
						DagNode node = (DagNode) DagBranch.this.connectedNodes
								.get(i);
						node.placeFigure(DagBranch.this.getContentsPane(),
								DagBranch.this.correspondindDagNode);
					}
				}
				expanded = true;
			}
		});

		this.addMouseListener(new MouseListener.Stub() {
			public void mousePressed(MouseEvent me) {
				correspondindDagNode.getViewer().setSelected(
						DagBranch.this.correspondindDagNode);
				DagBranch.this.correspondindDagNode
						.showConnectedSelected(DagBranch.this);
			}

			public void mouseDoubleClicked(MouseEvent me) {

			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent me) {
				// TODO Auto-generated method stub

			}

			public void mouseEntered(MouseEvent me) {
				correspondindDagNode.getViewer().highlightNode(DagBranch.this);
			}

			public void mouseExited(MouseEvent me) {
				correspondindDagNode.getViewer().unHighlightNode();
			}

			public void mouseHover(MouseEvent me) {
				// TODO Auto-generated method stub

			}

			public void mouseMoved(MouseEvent me) {
				// TODO Auto-generated method stub

			}

		});

	}

	protected boolean hasChildren() {
		return connectedNodes.size() > 0;
	}


}
