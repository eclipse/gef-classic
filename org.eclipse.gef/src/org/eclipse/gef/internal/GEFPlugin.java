/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.osgi.framework.BundleContext;

public final class GEFPlugin 
	extends AbstractUIPlugin
{

private static GEFPlugin singleton;

/**
 Creates the singleton instance of the GEF plugin.
*/
public GEFPlugin() {
	singleton = this;
}

/**
 * Gets the plugin singleton.
 *
 * @return the default GEFPlugin singleton
 */
public static GEFPlugin getDefault() {
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