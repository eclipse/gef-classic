/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
 * Create a property sheet entry which will execute commands on a command stack.  Calls to
 * setting property values will be wrapped inside a single command which is executed on
 * the given stack.
 * @param stack a command stack
 * @return the implementation for the entry
 */
public static IPropertySheetEntry createUndoablePropertySheetEntry(CommandStack stack) {
	return new org.eclipse.gef.internal.ui.properties.UndoablePropertySheetEntry(stack);
}

}
