/*******************************************************************************
 * Copyright (c) 2024 Patrick Ziegler and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Ziegler - initial API and implementation
 *******************************************************************************/

package org.eclipse.zest.cloudio;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
	public static String CloudOptionsComposite_AddColor;
	public static String CloudOptionsComposite_AddFonts;
	public static String CloudOptionsComposite_Angles;
	public static String CloudOptionsComposite_Background;
	public static String CloudOptionsComposite_Boost;
	public static String CloudOptionsComposite_BoostFactor;
	public static String CloudOptionsComposite_Colors;
	public static String CloudOptionsComposite_Deg45;
	public static String CloudOptionsComposite_Deg45Horizontal;
	public static String CloudOptionsComposite_Fonts;
	public static String CloudOptionsComposite_HorizontalAndVertical;
	public static String CloudOptionsComposite_HorizontalOnly;
	public static String CloudOptionsComposite_MaxFontSize;
	public static String CloudOptionsComposite_MinFontSize;
	public static String CloudOptionsComposite_NumberOfWords;
	public static String CloudOptionsComposite_Random;
	public static String CloudOptionsComposite_RemoveSelectedColors;
	public static String CloudOptionsComposite_RemoveSelectedFont;
	public static String CloudOptionsComposite_Selection;
	public static String CloudOptionsComposite_ToggleColors;
	public static String CloudOptionsComposite_VerticalOnly;
	public static String TagCloud_CalculatingWordBoundaries;
	public static String TagCloud_ErrorWhileLayouting_Message;
	public static String TagCloud_ErrorWhileLayouting_Title;
	public static String TagCloud_PlacingWords;
	public static String TagCloudViewer_Layouting;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
