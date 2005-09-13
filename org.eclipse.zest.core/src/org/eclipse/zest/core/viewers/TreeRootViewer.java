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
package org.eclipse.mylar.zest.core.viewers;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;


/**
 * Extends the JFace {@linke TreeViewer} to automatically create a 
 * Root item from the given data {@link Item}.  All {@link TreeItem} objects
 * are then created under this item.
 * 
 * @author Chris Callendar
 */
public class TreeRootViewer extends TreeViewer {

	private Item dataItem = null;
	private TreeItem rootTreeItem = null;
	
	public TreeRootViewer(Composite parent, int style) {
		super(parent, style);
	}
	
	public void setRootDataItem(Item rootDataItem) {
		if (rootDataItem != null) {
			this.dataItem = rootDataItem;
		}
	}
	
	/**
	 * Gets the root data Item.  
	 * @see #setRootDataItem(Item)
	 * @return Item or null if it hasn't been set
	 */
	public Item getRootDataItem() {
		return dataItem;
	}
	
	/**
	 * Creates a new item.  If the parent is the Tree and 
	 * {@link #setRootDataItem(Item)} has been called then 
	 * a root TreeItem will be created which all other TreeItems will go under.
	 * @see org.eclipse.jface.viewers.TreeViewer#newItem(org.eclipse.swt.widgets.Widget, int, int)
	 */
	protected Item newItem(Widget parent, int flags, int ix) {
		if (parent instanceof Tree) {
			Item rootItem = getRootDataItem();
			if (rootItem != null) {
				parent = createRootItem((Tree)parent, rootItem);
			}
		}
		return super.newItem(parent, flags, ix);
	}
	
	/**
	 * Creates the root item with the Tree as its parent.
	 * @param tree
	 * @return the root TreeItem for convenience
	 */
	private TreeItem createRootItem(Tree tree, Item rootItem) {
		if (rootItem == null)
			throw new IllegalArgumentException("Root item cannot be null");
		
		if ((rootTreeItem == null) || rootTreeItem.isDisposed()) {
			rootTreeItem = new TreeItem(tree, SWT.NULL, 0);
			rootTreeItem.setText(rootItem.getText());
			rootTreeItem.setImage(rootItem.getImage());
			rootTreeItem.setData(rootItem);	// can't be null
		}
		return rootTreeItem;
	}

	/**
	 * Tries to get the TreeItem associated with the given object.
	 * @param data
	 * @return TreeItem or null if not found.
	 */
	public TreeItem getTreeItem(Object data) {
		TreeItem item = (TreeItem)findItem(data);
		return item;
	}
	
}
