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
package org.eclipse.mylar.zest.core.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Item;


/**
 * An item that gets put into a {@link BreadCrumbBar}.
 * It contains text to display as a link and a data Object.
 * 
 * @author ccallendar
 */
public class BreadCrumbItem extends Item {

	/**
	 * Creates a new BreadCrumbItem and adds it to the BreadCrumbBar at the end.
	 * @param breadCrumbBar	The bar to add this item to.
	 * @param text	The text to display in the breadcrumb.
	 * @param data	The data associated with the breadcrumb.
	 */
	public BreadCrumbItem(BreadCrumbBar breadCrumbBar, String text, Object data) {
		this(breadCrumbBar, breadCrumbBar.getItemCount(), text, data);
	}

	/**
	 * Creates a new BreadCrumbItem and adds it to the BreadCrumbBar at the given index.
	 * @param breadCrumbBar	The bar to add this item to.
	 * @param index	The position in the breadcrumb list to add this into.
	 * @param text	The text to display in the breadcrumb.
	 * @param data	The data associated with the breadcrumb.
	 */
	public BreadCrumbItem(BreadCrumbBar breadCrumbBar, /*int style,*/ int index, String text, Object data) {
		super(breadCrumbBar.getGroup(), SWT.NONE, index);
		setText(text);
		setData(data);
		breadCrumbBar.createItem(this, index);
	}
	
}
