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
 * Messages for internationalized strings pertaining to ui elements.
 * @author Del Myers
 * @see org.eclipse.mylyn.zest.ZestException
 *
 */
public class ZestUIMessages {
	private static final String BUNDLE_NAME =
		"org.eclipse.mylyn.zest.core.messages.ZestUI"; //$NON-NLS-1$
	private static final ResourceBundle BUNDLE =
		ResourceBundle.getBundle(BUNDLE_NAME);
	public static final String VIEW_NESTED_TOP_NODE =
		getString("view.nested.top.node"); //$NON-NLS-1$
	public static final String VIEW_NESTED_CLIENT_PANE =
		getString("view.nested.client.pane"); //$NON-NLS-1$
	public static final String VIEW_NESTED_SUPPLIER_PANE =
		getString("view.nested.supplier.pane"); //$NON-NLS-1$
	public static final String TIP_MINIMIZE = 
		getString("tip.minimize");
	public static final String TIP_MAXIMIZE = 
		getString("tip.maximize");
	
	
	public static String getString(String key) {
		try {
			return BUNDLE.getString(key);
		} catch (MissingResourceException e) {
	         return "!" + key + "!";
	     }

	}
}
