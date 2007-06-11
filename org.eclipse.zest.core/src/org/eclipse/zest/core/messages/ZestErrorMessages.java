/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylyn.zest.core.messages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;




/**
 * Messages for internationalized strings pertaining to errors.
 * @author Del Myers
 * @see org.eclipse.mylyn.zest.ZestException
 *
 */
public class ZestErrorMessages {
	private static final String BUNDLE_NAME =
		"org.eclipse.mylyn.zest.core.messages.ZestError"; //$NON-NLS-1$
	private static final ResourceBundle BUNDLE =
		ResourceBundle.getBundle(BUNDLE_NAME);
	public static final String ERROR_INVALID_INPUT =
		getString("error.invalid.input"); //$NON-NLS-1$
	public static final String ERROR_CANNOT_SET_STYLE =
		getString("error.cannot.set.style"); //$NON-NLS-1$
	public static final String ERROR_INVALID_STYLE =
		getString("error.invalid.style"); //$NON-NLS-1$
		
	
	
	public static String getString(String key) {
		try {
			return BUNDLE.getString(key);
		} catch (MissingResourceException e) {
	         return "!" + key + "!";
	     }

	}
}
