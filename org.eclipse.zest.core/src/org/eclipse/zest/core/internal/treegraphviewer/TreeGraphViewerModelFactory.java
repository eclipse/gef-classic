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

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * This was originally a Draw2D Snippet that has been modified for Zest. All bugs 
 * in this should be opened against the Zest project.
 * 
 * @author hudsonr Created on Apr 22, 2003
 * @author Ian Bull
 */
public class TreeGraphViewerModelFactory {
	
	ILabelProvider labelProvider = null;
	TreeGraphViewerImpl viewerImpl = null;
	ITreeContentProvider treeContentProvider = null;
	
	public TreeGraphViewerModelFactory(TreeGraphViewerImpl viewerImpl,	
			ITreeContentProvider treeContentProvider,
			ILabelProvider labelProvider) {
		
		this.treeContentProvider = treeContentProvider;
		this.viewerImpl = viewerImpl;
		this.labelProvider = labelProvider;
	}
	
	public TreeRoot[] createModel(Object[] elements) {
		if ( elements == null ) return new TreeRoot[0];
		
		TreeRoot[] roots = new TreeRoot[elements.length];
		for (int i = 0; i < elements.length; i++) {
			roots[i] = createRoot(elements[i]);
			createChildren(roots[i], elements[i]);
		}
		return roots;
	}
	
	private void createChildren(TreeBranch parent, Object element) {
		Object[] children = treeContentProvider.getChildren(element);
		if ( children == null ) return;
		for (int i = 0; i < children.length; i++) {
			Object currentChild = children[i];
			TreeBranch childBranch = createBranch(parent, currentChild);
			createChildren(childBranch, currentChild);
		}
	}
	
	private TreeRoot createRoot(Object o) {
		String label = labelProvider.getText(o);
		int hangingStyle = viewerImpl.getHangingStyle() ? TreeBranch.STYLE_HANGING : TreeBranch.STYLE_NORMAL;
		TreeRoot root = new TreeRoot(createPageNode(label), hangingStyle);
		return root;
	}
	
	private TreeBranch createBranch(TreeBranch parent, Object o) {
		String label = labelProvider.getText(o);
		int hangingStyle = viewerImpl.getHangingStyle() ? TreeBranch.STYLE_HANGING : TreeBranch.STYLE_NORMAL;
		TreeBranch branch = new TreeBranch(createPageNode(label), hangingStyle);
		parent.getContentsPane().add(branch);
		// @tag hack : I should not expand and then collapse the nodes
		parent.setExpanded(true);
		parent.setExpanded(false);
		return branch;
	}
	
	
	private PageNode createPageNode(String title) {
		final PageNode node = new PageNode(title);
		node.addMouseListener(new MouseListener.Stub() {
			public void mousePressed(MouseEvent me) {
				viewerImpl.setSelected(node);
			}

			public void mouseDoubleClicked(MouseEvent me) {
				viewerImpl.doExpandCollapse();
			}
		});
		return node;
	}
	
	

}
