/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.mylar.zest.core.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

/**
 * Provides support for property changes. All model elements extend this class.
 * Also extends the Item (Widget) class to be used inside a StructuredViewer.
 * 
 * @author Chris Callendar
 */
public abstract class GraphItem extends Item implements IGraphItem {

	/**
	 * @param parent
	 * @param style
	 */
	public GraphItem(Widget parent) {
		super(parent, SWT.NO_BACKGROUND);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		//@tag zest.bug.167132-ListenerDispose : remove all listeners.
		//pcsDelegate = new PropertyChangeSupport(this);
		super.dispose();
	}

}
