/*******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.internal;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.eclipse.draw2d.BasicColorProvider;
import org.eclipse.draw2d.ColorProvider;

import org.eclipse.gef.GEFColorProvider;

import org.osgi.framework.BundleContext;

public class InternalGEFPlugin extends AbstractUIPlugin {

	private static BundleContext context;
	private static AbstractUIPlugin singleton;

	public InternalGEFPlugin() {
		singleton = this;
	}

	public void start(BundleContext bc) throws Exception {
		super.start(bc);
		context = bc;
		// Overloads the basic color provider with customizable one
		if (ColorProvider.SystemColorFactory.getColorProvider() instanceof BasicColorProvider
			&& PlatformUI.isWorkbenchRunning() && !PlatformUI.getWorkbench().isClosing()) {
			ColorProvider.SystemColorFactory.setColorProvider(new GEFColorProvider());
		}
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
	}

	public static BundleContext getContext() {
		return context;
	}

	public static AbstractUIPlugin getDefault() {
		return singleton;
	}

	/**
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		savePluginPreferences();
		super.stop(context);
	}

}
