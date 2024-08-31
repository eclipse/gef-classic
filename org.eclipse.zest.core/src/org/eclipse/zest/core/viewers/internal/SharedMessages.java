/*******************************************************************************
 * Copyright (c) 2003, 2024 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.zest.core.viewers.internal;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class contains UI strings (translated, if available) that clients can
 * use.
 *
 * @author Eric Bordeau
 */
//TODO zest 2.x: Move to org.eclipse.zest.core.widgets.internal
public class SharedMessages {
	private static final String BUNDLE_NAME = SharedMessages.class.getPackageName() + ".messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private SharedMessages() {
	}

	private static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * The string "Page".
	 */
	public static String FitAllAction_Label = getString("SharedMessages.FillAllAction_Label"); // GEFMessages.FitAllAction_Label; //$NON-NLS-1$
	/**
	 * The string "Width".
	 */
	public static String FitWidthAction_Label = getString("SharedMessages.FillWidthAction_Label"); // GEFMessages.FitWidthAction_Label; //$NON-NLS-1$
	/**
	 * The string "Height".
	 */
	public static String FitHeightAction_Label = getString("SharedMessages.FillHeightAction_Label"); // GEFMessages.FitHeightAction_Label; //$NON-NLS-1$

	public static String NodeSearchDialog_Title = getString("NodeSearchDialog.Title"); //$NON-NLS-1$
	public static String NodeSearchDialog_Find = getString("NodeSearchDialog.Find"); //$NON-NLS-1$
	public static String NodeSearchDialog_Options = getString("NodeSearchDialog.Options"); //$NON-NLS-1$
	public static String NodeSearchDialog_WholeWord = getString("NodeSearchDialog.WholeWord"); //$NON-NLS-1$
	public static String NodeSearchDialog_CaseSensitive = getString("NodeSearchDialog.CaseSensitive"); //$NON-NLS-1$
	public static String NodeSearchDialog_Next = getString("NodeSearchDialog.Next"); //$NON-NLS-1$
	public static String NodeSearchDialog_Previous = getString("NodeSearchDialog.Previous"); //$NON-NLS-1$
	public static String NodeSearchDialog_Close = getString("NodeSearchDialog.Close"); //$NON-NLS-1$
}
