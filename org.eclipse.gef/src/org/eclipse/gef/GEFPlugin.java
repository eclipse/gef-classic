package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.swt.graphics.Image;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.jface.resource.*;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.properties.IPropertySheetEntry;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.internal.Internal;

/**
  Provides shared {@link org.eclipse.swt.graphics.Image}s
  and {@link org.eclipse.jface.resource.ImageDescriptor}s.
 * @see SharedImageConstants
 * @see #getImage(String)
 * @see #getImageDescriptor(String)
 */
public final class GEFPlugin
	extends AbstractUIPlugin
	implements SharedImageConstants
{

private static GEFPlugin singleton;
private static ImageRegistry imageRegistry = new ImageRegistry();
private static Map imageDescriptors = new HashMap();

static{
	declareImage(ICON_TOOL_ARROW_16, "icons/arrow16.gif");         //$NON-NLS-1$
	declareImage(ICON_TOOL_ARROW_32, "icons/arrow32.gif");         //$NON-NLS-1$
	declareImage(ICON_TOOL_MARQUEE_16, "icons/marquee16.gif");     //$NON-NLS-1$
}

/**
 Creates the singleton instance of the GEF plugin.
*/
public GEFPlugin(IPluginDescriptor descriptor) {
	super(descriptor);
	if (singleton==null)
		singleton = this;
}

private static void declareImage(String key, String fileName){
	imageRegistry.put(key,
		ImageDescriptor.createFromFile(Internal.class, fileName));
}

private static void declareImageDescriptor(String key, String fileName){
	imageDescriptors.put(key,
		ImageDescriptor.createFromFile(Internal.class, fileName));
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
 * Returns the Image as defined in {@link SharedImageConstants}.
 * Only constants starting with ICON are available as Images.
 */
public static Image getImage(String imageName){
	return imageRegistry.get(imageName);
}

/**
 * Returns an ImageDescriptor as defined in {@link SharedImageConstants}.
 * Only constants starting with DESC are available as descriptors.
 */
public static ImageDescriptor getImageDescriptor(String imageName){
	return (ImageDescriptor)imageDescriptors.get(imageName);
}

/**
 * Create a root undoable property sheet entry.
 */
public static IPropertySheetEntry createUndoablePropertySheetEntry(CommandStack stack){
	return new org.eclipse.gef.internal.ui.properties.UndoablePropertySheetEntry(stack);
}

}
