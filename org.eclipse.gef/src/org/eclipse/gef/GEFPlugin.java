package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.properties.IPropertySheetEntry;

import org.eclipse.gef.commands.CommandStack;

/**
  Provides shared {@link org.eclipse.swt.graphics.Image}s
  and {@link org.eclipse.jface.resource.ImageDescriptor}s.
 * @see SharedImageConstants
 * @see #getImage(String)
 * @see #getImageDescriptor(String)
 */
public final class GEFPlugin
	extends AbstractUIPlugin
{

private static GEFPlugin singleton;

/**
 Creates the singleton instance of the GEF plugin.
*/
public GEFPlugin(IPluginDescriptor descriptor) {
	super(descriptor);
	if (singleton==null)
		singleton = this;
}

/**
 * Gets the plugin singleton.
 *
 * @return the default GEFPlugin singleton
 */
static public GEFPlugin getDefault() {
	return singleton;
}

/**
 * Create a root undoable property sheet entry.
 */
public static IPropertySheetEntry createUndoablePropertySheetEntry(CommandStack stack){
	return new org.eclipse.gef.internal.ui.properties.UndoablePropertySheetEntry(stack);
}

}
