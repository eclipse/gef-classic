package org.eclipse.gef;

import java.util.MissingResourceException;

import org.eclipse.core.runtime.Platform;

import org.osgi.framework.Bundle;

/**
 * This class contains UI strings (translated, if available) that clients can use.
 * @author Eric Bordeau
 */
public class SharedMessages {

static class Helper {
	private static final Bundle bundle = Platform.getBundle("org.eclipse.gef"); //$NON-NLS-1$
	
	public static String getString(String key) {
		try {
			return Platform.getResourceString(bundle, key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
}

/**
 * The string "Page".
 */
public static String FitAllAction_Label = Helper.getString("%FitAllAction.Label"); //$NON-NLS-1$
/**
 * The string "Width".
 */
public static String FitWidthAction_Label = Helper.getString("%FitWidthAction.Label"); //$NON-NLS-1$
/**
 * The string "Height".
 */
public static String FitHeightAction_Label =
	Helper.getString("%FitHeightAction.Label"); //$NON-NLS-1$

}
