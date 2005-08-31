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


/**
 * A listener for breadcrumb selection events.  When a breadcrumb link is clicked 
 * an event will be fired.  
 * 
 * @author ccallendar
 */
public interface IBreadCrumbListener {

	/**
	 * Signals that a breadcrumb has been selected.
	 * @param item the selected item
	 */
	public void breadCrumbSelected(BreadCrumbItem item);
	
	/**
	 * Notifies listeners that the back button was clicked.
	 */
	public void handleBackButtonSelected();
	
	/**
	 * Notifies listeners that the forward button was clicked.
	 */
	public void handleForwardButtonSelected();
	
	/**
	 * Notifies listeners that the up button was clicked.
	 */
	public void handleUpButtonSelected();
	
}
