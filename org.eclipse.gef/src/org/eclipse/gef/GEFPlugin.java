/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.properties.IPropertySheetEntry;

import org.eclipse.gef.commands.CommandStack;

/**
 * 
 */
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
 * This method will be deleted to remove the hard dependency on the
 * org.eclipse.ui.views plug-in.
 * @deprecated use org.eclipse.gef.ui.properties.UndoablePropertySheetEntry
 * @param stack a command stack
 * @return the implementation for the entry
 */
public static IPropertySheetEntry createUndoablePropertySheetEntry(CommandStack stack) {
	return new org.eclipse.gef.ui.properties.UndoablePropertySheetEntry(stack);
}

}
