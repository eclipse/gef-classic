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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.mylar.zest.core.viewers.IDAGContentProvider;
import org.eclipse.swt.SWT;

/**
 * 
 * @author Ian Bull
 */
public class DagModelFactory {

	ILabelProvider labelProvider = null;
	DagViewerImpl viewerImpl = null;
	IDAGContentProvider dagContentProvider = null;
	
	Map itemMap = null;
	
	public DagModelFactory( DagViewerImpl dagViewerImpl,
							IDAGContentProvider dagContentProvider,
							ILabelProvider labelProvider ) {
		this.dagContentProvider = dagContentProvider;
		this.viewerImpl = dagViewerImpl;
		this.labelProvider = labelProvider;
		itemMap = new HashMap();
	}
	
	public DagNode[] createModel(Object[] elements) {
		if ( elements == null ) return new DagNode[0];
		
		ArrayList dagNodeList = new ArrayList(); 
		for (int i = 0; i < elements.length; i++) {
			DagNode node = createRoot(elements[i]);
			if ( node != null ) {
				dagNodeList.add(node);
				createChildren(node, elements[i]);
			}
		}
		return (DagNode[])dagNodeList.toArray(new DagNode[dagNodeList.size()]);
	}
	
	private void createChildren(DagNode parent, Object element) {
		Object[] children = this.dagContentProvider.getAdjacent(element);
		if ( children == null ) return;
		for (int i = 0; i < children.length; i++) {
			Object currentChild = children[i];
			DagNode childBranch = createBranch(parent, currentChild);
			if ( childBranch != null ) {
				createChildren(childBranch, currentChild);
			}
		}
	}
	
	private DagNode createBranch(DagNode parent, Object o) {
		if ( itemMap.containsKey(o) ) {
			parent.addConnectedNode((DagNode) itemMap.get(o));
			return null;
		}
		String label = labelProvider.getText(o);
		int hangingStyle = viewerImpl.getHangingStyle() ? TreeBranch.STYLE_HANGING : TreeBranch.STYLE_NORMAL;
		DagNode branch = new DagNode(parent, SWT.NONE, o, label,viewerImpl, hangingStyle);
		parent.addConnectedNode(branch);
		itemMap.put(o, branch);
		return branch;
	}

	private DagNode createRoot(Object o) {
		if ( itemMap.containsKey(o) ) {
			return null;
		}
		String label = labelProvider.getText(o);
		int hangingStyle = viewerImpl.getHangingStyle() ? TreeBranch.STYLE_HANGING : TreeBranch.STYLE_NORMAL;
		DagNode root = new DagNode(viewerImpl, SWT.NONE, o, label,viewerImpl, hangingStyle);
		itemMap.put(o, root);
		return root;
	}
}
