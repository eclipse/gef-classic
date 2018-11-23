/*******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *******************************************************************************/

package org.eclipse.gef.examples.ui.pde.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class GefExamplesPlugin extends AbstractUIPlugin {

	/** The shared instance. */
	private static GefExamplesPlugin fPlugin;

	/**
	 * Constructor.
	 */
	public GefExamplesPlugin() {
		super();
		fPlugin = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return The plugin instance of <code>GefExamplesPlugin</code>
	 */
	public static GefExamplesPlugin getDefault() {
		return fPlugin;
	}

}