package org.eclipse.gef;

import java.util.MissingResourceException;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Platform;

/**
 * This class contains UI strings (translated, if available) that clients can use.
 * @author Eric Bordeau
 */
public class SharedMessages {

static class Helper {
	private static IPluginDescriptor desc = Platform.getPluginRegistry()
			.getPluginDescriptor("org.eclipse.gef"); //$NON-NLS-1$

	public static String getString(String key) {
		try {
			return desc.getResourceString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
}

public static String FitAllAction_Label = Helper.getString("%FitAllAction.Label"); //$NON-NLS-1$
public static String FitWidthAction_Label = Helper.getString("%FitWidthAction.Label"); //$NON-NLS-1$
public static String FitHeightAction_Label = Helper.getString("%FitHeightAction.Label"); //$NON-NLS-1$

}
