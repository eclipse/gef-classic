/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.gef.examples.text.actions;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 3.1
 */
public class StyleService {
public static final Object STATE_EDITABLE = new Object();

public static final Object STATE_READ_ONLY = new Object();
public static final Object UNDEFINED = new Object();

private List listeners = new ArrayList();
private StyleProvider provider;
private StyleListener providerListener = new StyleListener() {
	public void styleChanged(String styleID) {
		propogateChange(styleID);
	}
};

/**
 * Constructs a new StyleService object 
 * @since 3.1
 */
public StyleService() {
	super();
}

public void addStyleListener(StyleListener listener) {
	listeners.add(listener);
}

public Object getStyle(String styleID) {
	if (provider != null)
		return provider.getStyle(styleID);
	return UNDEFINED;
}

public Object getStyleState(String styleID) {
	if (provider != null)
		return provider.getStyleState(styleID);
	return UNDEFINED;
}

/**
 * @param styleID
 * @since 3.1
 */
protected void propogateChange(String styleID) {
	for (int i = 0; i < listeners.size(); i++) {
		StyleListener listener = (StyleListener)listeners.get(i);
		listener.styleChanged(styleID);
	}
}

/**
 * Removes the first occurrence of the given listener.
 * @param listener the style listener
 * @since 3.1
 */
public void removeStyleListener(StyleListener listener) {
	listeners.remove(listener);
}

public void setStyle(String styleID, Object value) {
	if (this.provider != null)
		this.provider.setStyle(styleID, value);
}

public void setStyleProvider(StyleProvider provider) {
	if (this.provider == provider)
		return;
	if (this.provider != null)
		this.provider.removeStyleListener(providerListener);
	this.provider = provider;
	if (this.provider != null)
		this.provider.addStyleListener(providerListener);
	propogateChange(null);
}

}
