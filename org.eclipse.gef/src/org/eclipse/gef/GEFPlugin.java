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
 * @deprecated in 3.2.
 */
public final class GEFPlugin
	extends AbstractUIPlugin
{

/**
 * Gets the singleton.
 *
 * @return the default GEFPlugin singleton
 */
public static AbstractUIPlugin getDefault() {
	return org.eclipse.gef.internal.GEFPlugin.getDefault();
}

/**
 * Creates an UndoablePropertySheetEntry.
 * @deprecated use org.eclipse.gef.ui.properties.UndoablePropertySheetEntry
 * @param stack a command stack
 * @return the implementation for the entry
 */
public static IPropertySheetEntry createUndoablePropertySheetEntry(CommandStack stack) {
	return new org.eclipse.gef.ui.properties.UndoablePropertySheetEntry(stack);
}

}